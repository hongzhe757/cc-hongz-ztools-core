package cc.hongz.ztools.core.img.gif;

/* NeuQuant Neural-Net Quantization Algorithm
 * ------------------------------------------
 *
 * Copyright (c) 1994 Anthony Dekker
 *
 * NEUQUANT Neural-Net quantization algorithm by Anthony Dekker, 1994.
 * See "Kohonen neural networks for optimal colour quantization"
 * in "Network: Computation in Neural Systems" Vol. 5 (1994) pp 351-367.
 * for a discussion of the algorithm.
 *
 * Any party obtaining a copy of these files from the author, directly or
 * indirectly, is granted, free of charge, a full and unrestricted irrevocable,
 * world-wide, paid up, royalty-free, nonexclusive right and license to deal
 * in this software and documentation files (the "Software"), including without
 * limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons who receive
 * copies from any such party to do so, with the only requirement being
 * that this copyright notice remain intact.
 */

/* Java 移植 K Weiner */

/**
 * NeuQuant 神经网络颜色量化算法.
 * <p>
 * 供 GIF 编码将真彩色帧压缩为 256 色调色板.
 * <p>
 * 设计模式：算法组件（非经典 GoF），由 {@link AnimatedGifEncoder} 调用完成真彩到 256 色调色板量化.
 *
 * @author Anthony Dekker
 */
public class NeuQuant {

	/**
	 * 使用的颜色数.
	 */
	protected static final int NETSIZE = 256;

	/* 500 附近的四个质数 - 假定没有图像长度能同时被这四个质数整除 */
	/**
	 * 质数 1.
	 */
	protected static final int PRIME1 = 499;
	/**
	 * 质数 2.
	 */
	protected static final int PRIME2 = 491;
	/**
	 * 质数 3.
	 */
	protected static final int PRIME3 = 487;
	/**
	 * 质数 4.
	 */
	protected static final int PRIME4 = 503;

	/**
	 * 输入图像最小尺寸.
	 */
	protected static final int MINPICTUREBYTES = (3 * PRIME4);

	/* 程序骨架
	   ----------------
	   [选择 samplefac 范围 1..30]
	   [从输入文件读取图像]
	   pic = (unsigned char*) malloc(3*width*height);
	   initnet(pic,3*width*height,samplefac);
	   learn();
	   unbiasnet();
	   [使用 writecolourmap(f) 写入输出图像头]
	   inxbuild();
	   使用 inxsearch(b,g,r) 写入输出图像      */

	/* 网络定义
	   ------------------- */

	/**
	 * 网络最大位置索引.
	 */
	protected static final int MAXNETPOS = (NETSIZE - 1);
	/**
	 * 颜色值偏移.
	 */
	protected static final int NETBIASSHIFT = 4;
	/**
	 * 学习周期数.
	 */
	protected static final int NCYCLES = 100;

	/* 频率和偏移定义 */
	/**
	 * 分数偏移.
	 */
	protected static final int INTBIASSHIFT = 16;
	protected static final int INTBIAS = (1 << INTBIASSHIFT);
	/**
	 * gamma = 1024.
	 */
	protected static final int GAMMASHIFT = 10;
	protected static final int GAMMA = (1 << GAMMASHIFT);
	protected static final int BETASHIFT = 10;
	/**
	 * beta = 1/1024.
	 */
	protected static final int BETA = (INTBIAS >> BETASHIFT);
	protected static final int BETAGAMMA =
			(INTBIAS << (GAMMASHIFT - BETASHIFT));

	/* 递减半径因子定义 */
	/**
	 * 256 色时半径起始值.
	 */
	protected static final int INITRAD = (NETSIZE >> 3);
	/**
	 * 半径偏移（为 32.0，偏移 6 位）.
	 */
	protected static final int RADIUSBIASSHIFT = 6;
	protected static final int RADIUSBIAS = (1 << RADIUSBIASSHIFT);
	/**
	 * 初始半径（每个周期递减）.
	 */
	protected static final int INITRADIUS = (INITRAD * RADIUSBIAS);
	/**
	 * 半径递减因子（1/30）.
	 */
	protected static final int RADIUSDEC = 30;

	/* 递减 alpha 因子定义 */
	/**
	 * alpha 起始偏移（起始于 1.0）.
	 */
	protected static final int ALPHABIASSHIFT = 10;
	protected static final int INITALPHA = (1 << ALPHABIASSHIFT);

	/**
	 * alpha 衰减值（偏移 10 位）.
	 */
	protected int alphadec;

	/* radbias 和 alpharadbias 用于 radpower 计算 */
	protected static final int RADBIASSHIFT = 8;
	protected static final int RADBIAS = (1 << RADBIASSHIFT);
	protected static final int ALPHARADBSHIFT = (ALPHABIASSHIFT + RADBIASSHIFT);
	protected static final int ALPHARADBIAS = (1 << ALPHARADBSHIFT);

	/* 类型和全局变量
	-------------------------- */

	/**
	 * 输入图像本身.
	 */
	protected byte[] thepicture;
	/**
	 * lengthcount = H*W*3.
	 */
	protected int lengthcount;

	/**
	 * 采样因子 1..30.
	 */
	protected int samplefac;

	/*   typedef int pixel[4];                BGRc */
	/**
	 * 网络本身 [netsize][4].
	 */
	protected int[][] network;

	/**
	 * 网络查找索引（实际 256）.
	 */
	protected int[] netindex = new int[256];

	/**
	 * 学习用的偏移数组.
	 */
	protected int[] bias = new int[NETSIZE];
	/**
	 * 学习用的频率数组.
	 */
	protected int[] freq = new int[NETSIZE];
	/**
	 * 用于预计算的 radpower.
	 */
	protected int[] radpower = new int[INITRAD];

	/**
	 * 构造函数，初始化神经网络并设置参数.
	 *
	 * @param thepic 输入图像像素数组
	 * @param len    像素数组长度
	 * @param sample 采样因子
	 */
	public NeuQuant(byte[] thepic, int len, int sample) {

		int i;
		int[] p;

		thepicture = thepic;
		lengthcount = len;
		samplefac = sample;

		network = new int[NETSIZE][];
		for (i = 0; i < NETSIZE; i++) {
			network[i] = new int[4];
			p = network[i];
			p[0] = p[1] = p[2] = (i << (NETBIASSHIFT + 8)) / NETSIZE;
			freq[i] = INTBIAS / NETSIZE; /* 1/网络大小 */
			bias[i] = 0;
		}
	}

	/**
	 * 生成颜色映射表.
	 *
	 * @return RGB 颜色映射表
	 */
	public byte[] colorMap() {
		byte[] map = new byte[3 * NETSIZE];
		int[] index = new int[NETSIZE];
		for (int i = 0; i < NETSIZE; i++)
			index[network[i][3]] = i;
		int k = 0;
		for (int i = 0; i < NETSIZE; i++) {
			int j = index[i];
			map[k++] = (byte) (network[j][0]);
			map[k++] = (byte) (network[j][1]);
			map[k++] = (byte) (network[j][2]);
		}
		return map;
	}

	/**
	 * 网络插入排序并构建查找索引.
	 */
	public void inxbuild() {

		int i, j, smallpos, smallval;
		int[] p;
		int[] q;
		int previouscol, startpos;

		previouscol = 0;
		startpos = 0;
		for (i = 0; i < NETSIZE; i++) {
			p = network[i];
			smallpos = i;
			smallval = p[1]; /* 以 g 分量索引 */
			/* 在 i..netsize-1 中找最小值 */
			for (j = i + 1; j < NETSIZE; j++) {
				q = network[j];
				if (q[1] < smallval) { /* 以 g 分量索引 */
					smallpos = j;
					smallval = q[1]; /* 以 g 分量索引 */
				}
			}
			q = network[smallpos];
			/* 交换 p (i) 和 q (smallpos) 的条目 */
			if (i != smallpos) {
				j = q[0];
				q[0] = p[0];
				p[0] = j;
				j = q[1];
				q[1] = p[1];
				p[1] = j;
				j = q[2];
				q[2] = p[2];
				p[2] = j;
				j = q[3];
				q[3] = p[3];
				p[3] = j;
			}
			/* smallval 条目现在位于位置 i */
			if (smallval != previouscol) {
				netindex[previouscol] = (startpos + i) >> 1;
				for (j = previouscol + 1; j < smallval; j++)
					netindex[j] = i;
				previouscol = smallval;
				startpos = i;
			}
		}
		netindex[previouscol] = (startpos + MAXNETPOS) >> 1;
		for (j = previouscol + 1; j < 256; j++)
			netindex[j] = MAXNETPOS; /* 实际 256 */
	}

	/**
	 * 神经网络学习训练.
	 */
	public void learn() {

		int i, j, b, g, r;
		int radius, rad, alpha, step, delta, samplepixels;
		byte[] p;
		int pix, lim;

		if (lengthcount < MINPICTUREBYTES)
			samplefac = 1;
		alphadec = 30 + ((samplefac - 1) / 3);
		p = thepicture;
		pix = 0;
		lim = lengthcount;
		samplepixels = lengthcount / (3 * samplefac);
		delta = samplepixels / NCYCLES;
		alpha = INITALPHA;
		radius = INITRADIUS;

		rad = radius >> RADIUSBIASSHIFT;
		for (i = 0; i < rad; i++)
			radpower[i] =
					alpha * (((rad * rad - i * i) * RADBIAS) / (rad * rad));

		/* fprintf(stderr,"开始一维学习: 初始半径=%d\n", rad); */

		if (lengthcount < MINPICTUREBYTES)
			step = 3;
		else if ((lengthcount % PRIME1) != 0)
			step = 3 * PRIME1;
		else {
			if ((lengthcount % PRIME2) != 0)
				step = 3 * PRIME2;
			else {
				if ((lengthcount % PRIME3) != 0)
					step = 3 * PRIME3;
				else
					step = 3 * PRIME4;
			}
		}

		i = 0;
		while (i < samplepixels) {
			b = (p[pix] & 0xff) << NETBIASSHIFT;
			g = (p[pix + 1] & 0xff) << NETBIASSHIFT;
			r = (p[pix + 2] & 0xff) << NETBIASSHIFT;
			j = contest(b, g, r);

			altersingle(alpha, j, b, g, r);
			if (rad != 0)
				alterneigh(rad, j, b, g, r); /* 调整邻近神经元 */

			pix += step;
			if (pix >= lim)
				pix -= lengthcount;

			i++;
			if (delta == 0)
				delta = 1;
			if (i % delta == 0) {
				alpha -= alpha / alphadec;
				radius -= radius / RADIUSDEC;
				rad = radius >> RADIUSBIASSHIFT;
				if (rad <= 1)
					rad = 0;
				for (j = 0; j < rad; j++)
					radpower[j] =
							alpha * (((rad * rad - j * j) * RADBIAS) / (rad * rad));
			}
		}
		/* fprintf(stderr,"完成一维学习: 最终 alpha=%f !\n",((float)alpha)/initalpha); */
	}

	/**
	 * 搜索 BGR 值并返回最接近的颜色索引.
	 *
	 * @param b 蓝色分量
	 * @param g 绿色分量
	 * @param r 红色分量
	 * @return 最接近的颜色索引
	 */
	public int map(int b, int g, int r) {

		int i, j, dist, a, bestd;
		int[] p;
		int best;

		bestd = 1000; /* 最大可能距离为 256*3 */
		best = -1;
		i = netindex[g]; /* 以 g 分量索引 */
		j = i - 1; /* 从 netindex[g] 开始向外扩展 */

		while ((i < NETSIZE) || (j >= 0)) {
			if (i < NETSIZE) {
				p = network[i];
				dist = p[1] - g; /* 索引键 */
				if (dist >= bestd)
					i = NETSIZE; /* 停止迭代 */
				else {
					i++;
					if (dist < 0)
						dist = -dist;
					a = p[0] - b;
					if (a < 0)
						a = -a;
					dist += a;
					if (dist < bestd) {
						a = p[2] - r;
						if (a < 0)
							a = -a;
						dist += a;
						if (dist < bestd) {
							bestd = dist;
							best = p[3];
						}
					}
				}
			}
			if (j >= 0) {
				p = network[j];
				dist = g - p[1]; /* 索引键 - 反向差值 */
				if (dist >= bestd)
					j = -1; /* 停止迭代 */
				else {
					j--;
					if (dist < 0)
						dist = -dist;
					a = p[0] - b;
					if (a < 0)
						a = -a;
					dist += a;
					if (dist < bestd) {
						a = p[2] - r;
						if (a < 0)
							a = -a;
						dist += a;
						if (dist < bestd) {
							bestd = dist;
							best = p[3];
						}
					}
				}
			}
		}
		return (best);
	}

	/**
	 * 执行完整的量化流程.
	 *
	 * @return 颜色映射表
	 */
	public byte[] process() {
		learn();
		unbiasnet();
		inxbuild();
		return colorMap();
	}

	/**
	 * 网络去偏，将神经元值转换为 0..255 范围.
	 */
	public void unbiasnet() {
		for (int i = 0; i < NETSIZE; i++) {
			network[i][0] >>= NETBIASSHIFT;
			network[i][1] >>= NETBIASSHIFT;
			network[i][2] >>= NETBIASSHIFT;
			network[i][3] = i; /* 记录颜色编号 */
		}
	}

	/**
	 * 调整命中神经元的相邻神经元.
	 *
	 * @param rad 影响半径
	 * @param i   命中神经元索引
	 * @param b   蓝色分量
	 * @param g   绿色分量
	 * @param r   红色分量
	 */
	protected void alterneigh(int rad, int i, int b, int g, int r) {

		int j, k, lo, hi, a, m;
		int[] p;

		lo = i - rad;
		if (lo < -1)
			lo = -1;
		hi = i + rad;
		if (hi > NETSIZE)
			hi = NETSIZE;

		j = i + 1;
		k = i - 1;
		m = 1;
		while ((j < hi) || (k > lo)) {
			a = radpower[m++];
			if (j < hi) {
				p = network[j++];
				try {
					p[0] -= (a * (p[0] - b)) / ALPHARADBIAS;
					p[1] -= (a * (p[1] - g)) / ALPHARADBIAS;
					p[2] -= (a * (p[2] - r)) / ALPHARADBIAS;
				} catch (Exception ignored) {
				} /* 防止 1.3 编译错误 */
			}
			if (k > lo) {
				p = network[k--];
				try {
					p[0] -= (a * (p[0] - b)) / ALPHARADBIAS;
					p[1] -= (a * (p[1] - g)) / ALPHARADBIAS;
					p[2] -= (a * (p[2] - r)) / ALPHARADBIAS;
				} catch (Exception ignored) {
				}
			}
		}
	}

	/**
	 * 调整命中神经元向目标颜色移动.
	 *
	 * @param alpha 学习率
	 * @param i     命中神经元索引
	 * @param b     蓝色分量
	 * @param g     绿色分量
	 * @param r     红色分量
	 */
	protected void altersingle(int alpha, int i, int b, int g, int r) {

		/* 调整命中神经元 */
		int[] n = network[i];
		n[0] -= (alpha * (n[0] - b)) / INITALPHA;
		n[1] -= (alpha * (n[1] - g)) / INITALPHA;
		n[2] -= (alpha * (n[2] - r)) / INITALPHA;
	}

	/**
	 * 竞争学习，找到最佳匹配神经元.
	 *
	 * @param b 蓝色分量
	 * @param g 绿色分量
	 * @param r 红色分量
	 * @return 最佳匹配神经元位置
	 */
	protected int contest(int b, int g, int r) {

		/* 找到最近的神经元（最小距离）并更新频率 */
		/* 找到最佳的神经元（最小距离-偏移）并返回位置 */
		/* 对于频繁被选中的神经元，freq[i] 较高且 bias[i] 为负值 */
		/* bias[i] = gamma*((1/netsize)-freq[i]) */

		int i, dist, a, biasdist, betafreq;
		int bestpos, bestbiaspos, bestd, bestbiasd;
		int[] n;

		bestd = ~(1 << 31);
		bestbiasd = bestd;
		bestpos = -1;
		bestbiaspos = bestpos;

		for (i = 0; i < NETSIZE; i++) {
			n = network[i];
			dist = n[0] - b;
			if (dist < 0)
				dist = -dist;
			a = n[1] - g;
			if (a < 0)
				a = -a;
			dist += a;
			a = n[2] - r;
			if (a < 0)
				a = -a;
			dist += a;
			if (dist < bestd) {
				bestd = dist;
				bestpos = i;
			}
			biasdist = dist - ((bias[i]) >> (INTBIASSHIFT - NETBIASSHIFT));
			if (biasdist < bestbiasd) {
				bestbiasd = biasdist;
				bestbiaspos = i;
			}
			betafreq = (freq[i] >> BETASHIFT);
			freq[i] -= betafreq;
			bias[i] += (betafreq << GAMMASHIFT);
		}
		freq[bestpos] += BETA;
		bias[bestpos] -= BETAGAMMA;
		return (bestbiaspos);
	}
}
