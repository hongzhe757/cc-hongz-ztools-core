package cc.hongz.ztools.core.img.gif;

import java.io.IOException;
import java.io.OutputStream;

/* ==============================================================================
 *  Adapted from Jef Poskanzer's Java port by way of J. M. G. Elliott.
 *  K Weiner 12/00
 */

/**
 * GIF LZW 压缩编码器.
 * <p>
 * 供 {@link AnimatedGifEncoder} 写入 GIF 帧像素数据.
 * <p>
 * 设计模式：算法组件（非经典 GoF），作为 GIF 编码流水线中的 LZW 压缩步骤，由 {@link AnimatedGifEncoder} 组合调用.
 *
 * @author Kevin Weiner, FM Software
 */
class LZWEncoder {

	private static final int EOF = -1;

	/**
	 * 图像宽度.
	 */
	private final int imgW;
	/**
	 * 图像高度.
	 */
	private final int imgH;
	/**
	 * 像素数组.
	 */
	private final byte[] pixAry;
	/**
	 * 初始码大小.
	 */
	private final int initCodeSize;

	/**
	 * 剩余像素数.
	 */
	private int remaining;
	/**
	 * 当前像素位置.
	 */
	private int curPixel;

	/* GIFCOMPR.C       - GIF 图像压缩例程
	 *
	 * 基于 'compress' 的 Lempel-Ziv 压缩，GIF 修改由
	 * David Rowley (mgardi@watdcsu.waterloo.edu) 完成
	 */

	/**
	 * 编码位数.
	 */
	static final int BITS = 12;

	/**
	 * 散列表大小（80% 占用率）.
	 */
	static final int HSIZE = 5003;

	/* GIF 图像压缩 - 修改版 'compress'
	 *
	 * 基于: compress.c - 文件压缩，IEEE Computer, 1984 年 6 月.
	 *
	 * 原作者:  Spencer W. Thomas      (decvax!harpo!utah-cs!utah-gr!thomas)
	 *              Jim McKie              (decvax!mcvax!jim)
	 *              Steve Davies           (decvax!vax135!petsd!peora!srd)
	 *              Ken Turkowski          (decvax!decwrl!turtlevax!ken)
	 *              James A. Woods         (decvax!ihnp4!ames!jaw)
	 *              Joe Orost              (decvax!vax135!petsd!joe)
	 */

	/**
	 * 编码位数.
	 */
	int n_bits;
	/**
	 * 用户可设定的最大编码位数.
	 */
	int maxbits = BITS;
	/**
	 * 给定 n_bits 下的最大编码.
	 */
	int maxcode;
	/**
	 * 永远不应生成此编码.
	 */
	int maxmaxcode = 1 << BITS;

	/**
	 * 散列表.
	 */
	int[] htab = new int[HSIZE];
	/**
	 * 编码表.
	 */
	int[] codetab = new int[HSIZE];

	/**
	 * 用于动态表大小调整.
	 */
	int hsize = HSIZE;

	/**
	 * 第一个未使用的条目.
	 */
	int free_ent = 0;

	/**
	 * 块压缩参数：当所有编码用尽且压缩率变化时，重新开始.
	 */
	boolean clear_flg = false;

	/* 算法: 对前缀码/下一字符组合使用开放寻址双重散列（无链）.
	 * 采用 Knuth 算法 D（第 3 卷, 6.4 节）的变体，结合 G. Knott 的
	 * 互质二次探测。此处用更快的异或操作替代模除首次探测。
	 * 同时进行自适应重置的块压缩：当压缩率下降时清除码表。
	 * 变长输出编码在此时重新调整大小，并生成特殊的 CLEAR 码
	 * 供解压缩器使用。后期补充：根据文件大小构造表，对小文件
	 * 有显著的速度提升。关于此实现的问题请联系 ames!jaw.
	 */

	/**
	 * 初始位数.
	 */
	int g_init_bits;

	/**
	 * 清除码.
	 */
	int ClearCode;
	/**
	 * EOF 码.
	 */
	int EOFCode;

	/* 输出
	 *
	 * 将给定编码输出到文件.
	 * 输入:
	 *      code:   n_bits 位整数. 若 == -1 则为 EOF.
	 *              假定 n_bits =< wordsize - 1.
	 * 输出:
	 *      将编码写入文件.
	 * 假定:
	 *      字符为 8 位长.
	 * 算法:
	 *      维护一个 BITS 字符长的缓冲区（使 8 个编码恰好装入）.
	 * 当缓冲区满时清空并重新开始.
	 */

	/**
	 * 当前累加器.
	 */
	int cur_accum = 0;
	/**
	 * 当前位数.
	 */
	int cur_bits = 0;

	/**
	 * 位掩码表.
	 */
	final int[] masks =
		{
			0x0000,
			0x0001,
			0x0003,
			0x0007,
			0x000F,
			0x001F,
			0x003F,
			0x007F,
			0x00FF,
			0x01FF,
			0x03FF,
			0x07FF,
			0x0FFF,
			0x1FFF,
			0x3FFF,
			0x7FFF,
			0xFFFF };

	/**
	 * 当前包中已累积的字符数.
	 */
	int a_count;

	/**
	 * 包累加器存储.
	 */
	byte[] accum = new byte[256];

	/**
	 * 构造函数.
	 *
	 * @param width      图像宽度
	 * @param height     图像高度
	 * @param pixels     索引像素数组
	 * @param color_depth 颜色深度
	 */
	LZWEncoder(int width, int height, byte[] pixels, int color_depth) {
		imgW = width;
		imgH = height;
		pixAry = pixels;
		initCodeSize = Math.max(2, color_depth);
	}

	/**
	 * 将字符追加到当前包末尾，满 254 字符时刷出包.
	 */
	void char_out(byte c, OutputStream outs) throws IOException {
		accum[a_count++] = c;
		if (a_count >= 254)
			flush_char(outs);
	}

	/**
	 * 块压缩时清除码表.
	 */
	void cl_block(OutputStream outs) throws IOException {
		cl_hash(hsize);
		free_ent = ClearCode + 2;
		clear_flg = true;

		output(ClearCode, outs);
	}

	/**
	 * 重置码表.
	 */
	void cl_hash(int hsize) {
		for (int i = 0; i < hsize; ++i)
			htab[i] = -1;
	}

	/**
	 * LZW 压缩主流程.
	 */
	void compress(int init_bits, OutputStream outs) throws IOException {
		int fcode;
		int i /* = 0 */;
		int c;
		int ent;
		int disp;
		int hsize_reg;
		int hshift;

		/* 设置全局变量: g_init_bits - 初始位数 */
		g_init_bits = init_bits;

		/* 设置必要的值 */
		clear_flg = false;
		n_bits = g_init_bits;
		maxcode = MAXCODE(n_bits);

		ClearCode = 1 << (init_bits - 1);
		EOFCode = ClearCode + 1;
		free_ent = ClearCode + 2;

		a_count = 0; /* 清空包 */

		ent = nextPixel();

		hshift = 0;
		for (fcode = hsize; fcode < 65536; fcode *= 2)
			++hshift;
		hshift = 8 - hshift; /* 设置散列码范围边界 */

		hsize_reg = hsize;
		cl_hash(hsize_reg); /* 清除散列表 */

		output(ClearCode, outs);

		outer_loop : while ((c = nextPixel()) != EOF) {
			fcode = (c << maxbits) + ent;
				i = (c << hshift) ^ ent; /* 异或散列 */

			if (htab[i] == fcode) {
				ent = codetab[i];
				continue;
			} else if (htab[i] >= 0) /* 非空槽 */
				{
				disp = hsize_reg - i; /* 二次散列（基于 G. Knott） */
				if (i == 0)
					disp = 1;
				do {
					if ((i -= disp) < 0)
						i += hsize_reg;

					if (htab[i] == fcode) {
						ent = codetab[i];
						continue outer_loop;
					}
				} while (htab[i] >= 0);
			}
			output(ent, outs);
			ent = c;
			if (free_ent < maxmaxcode) {
				codetab[i] = free_ent++; /* 编码 -> 散列表 */
				htab[i] = fcode;
			} else
				cl_block(outs);
		}
		/* 输出最后一个编码. */
		output(ent, outs);
		output(EOFCode, outs);
	}

	/**
	 * 编码入口方法.
	 *
	 * @param os 输出流
	 * @throws IOException IO异常
	 */
	void encode(OutputStream os) throws IOException {
		os.write(initCodeSize); /* 写入"初始码大小"字节 */
		
		remaining = imgW * imgH; /* 重置导航变量 */
		curPixel = 0;

		compress(initCodeSize + 1, os); /* 压缩并写入像素数据 */

		os.write(0); /* 写入块终结符 */
	}

	/**
	 * 刷出包到输出流并重置累加器.
	 */
	void flush_char(OutputStream outs) throws IOException {
		if (a_count > 0) {
			outs.write(a_count);
			outs.write(accum, 0, a_count);
			a_count = 0;
		}
	}

	/**
	 * 计算给定位数下的最大编码.
	 */
	final int MAXCODE(int n_bits) {
		return (1 << n_bits) - 1;
	}

	/**
	 * 返回图像中的下一个像素.
	 */
	private int nextPixel() {
		if (remaining == 0)
			return EOF;

		--remaining;

		byte pix = pixAry[curPixel++];

		return pix & 0xff;
	}

	/**
	 * 输出指定编码到输出流.
	 */
	void output(int code, OutputStream outs) throws IOException {
		cur_accum &= masks[cur_bits];

		if (cur_bits > 0)
			cur_accum |= (code << cur_bits);
		else
			cur_accum = code;

		cur_bits += n_bits;

		while (cur_bits >= 8) {
			char_out((byte) (cur_accum & 0xff), outs);
			cur_accum >>= 8;
			cur_bits -= 8;
		}

		/* 如果下一个条目对于当前码大小来说太大，
		 * 则在可能的情况下增加码大小.
		 */
		if (free_ent > maxcode || clear_flg) {
			if (clear_flg) {
				maxcode = MAXCODE(n_bits = g_init_bits);
				clear_flg = false;
			} else {
				++n_bits;
				if (n_bits == maxbits)
					maxcode = maxmaxcode;
				else
					maxcode = MAXCODE(n_bits);
			}
		}

		if (code == EOFCode) {
			/* 到达 EOF 时，写入缓冲区剩余内容. */
			while (cur_bits > 0) {
				char_out((byte) (cur_accum & 0xff), outs);
				cur_accum >>= 8;
				cur_bits -= 8;
			}

			flush_char(outs);
		}
	}
}
