package cc.hongz.ztools.core.img.gif;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 动态 GIF 动画生成器.
 * <p>
 * 可生成一个或多个帧的 GIF，供 {@link cc.hongz.ztools.captcha.GifCaptcha} 使用.
 * <p>
 * 设计模式：建造者（Builder），按 {@code start → setDelay/setRepeat → addFrame → finish} 顺序组装 GIF 字节流.
 *
 * <pre>
 * 示例:
 *    AnimatedGifEncoder e = new AnimatedGifEncoder();
 *    e.start(outputFileName);
 *    e.setDelay(1000);
 *    e.addFrame(image1);
 *    e.addFrame(image2);
 *    e.finish();
 * </pre>
 * <p>
 * 来自：https://github.com/rtyley/animated-gif-lib-for-java
 *
 * @author Kevin Weiner, FM Software
 */
public class AnimatedGifEncoder {

	/**
	 * 画布宽度.
	 */
	protected int width;
	/**
	 * 画布高度.
	 */
	protected int height;
	/**
	 * 透明色，null 表示无透明色.
	 */
	protected Color transparent = null;
	/**
	 * 是否精确匹配透明色索引.
	 */
	protected boolean transparentExactMatch = false;
	/**
	 * 背景色，null 表示默认黑色.
	 */
	protected Color background = null;
	/**
	 * 透明色在调色板中的索引.
	 */
	protected int transIndex;
	/**
	 * 循环次数，-1 表示不写入 Netscape 扩展.
	 */
	protected int repeat = -1;
	/**
	 * 帧间隔（百分之一秒）.
	 */
	protected int delay = 0;
	/**
	 * 是否已开始写入.
	 */
	protected boolean started = false;
	/**
	 * 输出流.
	 */
	protected OutputStream out;
	/**
	 * 当前帧图像.
	 */
	protected BufferedImage image;
	/**
	 * 当前帧 BGR 像素字节数组.
	 */
	protected byte[] pixels;
	/**
	 * 量化后的索引像素.
	 */
	protected byte[] indexedPixels;
	/**
	 * 位平面数.
	 */
	protected int colorDepth;
	/**
	 * RGB 调色板.
	 */
	protected byte[] colorTab;
	/**
	 * 调色板项是否被使用.
	 */
	protected boolean[] usedEntry = new boolean[256];
	/**
	 * 调色板大小（bits - 1）.
	 */
	protected int palSize = 7;
	/**
	 * 处置方式（-1 表示使用默认）.
	 */
	protected int dispose = -1;
	/**
	 * 完成后是否关闭输出流.
	 */
	protected boolean closeStream = false;
	/**
	 * 是否为第一帧.
	 */
	protected boolean firstFrame = true;
	/**
	 * 是否已通过 setSize 设定尺寸.
	 */
	protected boolean sizeSet = false;
	/**
	 * NeuQuant 采样间隔，默认 10.
	 */
	protected int sample = 10;

	/**
	 * 设置帧间隔时间.
	 * <p>
	 * 作用于上一帧及后续帧.
	 *
	 * @param ms 间隔时间，单位毫秒
	 */
	public void setDelay(int ms) {
		delay = Math.round(ms / 10.0f);
	}

	/**
	 * 设置 GIF 帧处置方式.
	 * <p>
	 * 作用于上一帧及后续帧；未设透明色时默认 0，否则默认 2.
	 *
	 * @param code 处置码
	 */
	public void setDispose(int code) {
		if (code >= 0) {
			dispose = code;
		}
	}

	/**
	 * 设置 GIF 循环播放次数.
	 * <p>
	 * 须在添加第一帧前调用；0 表示无限循环.
	 *
	 * @param iter 循环次数
	 */
	public void setRepeat(int iter) {
		if (iter >= 0) {
			repeat = iter;
		}
	}

	/**
	 * 设置透明色.
	 * <p>
	 * 量化后取最接近给定颜色的调色板项作为透明色；null 表示无透明色.
	 *
	 * @param c 透明色
	 */
	public void setTransparent(Color c) {
		setTransparent(c, false);
	}

	/**
	 * 设置透明色及是否精确匹配索引.
	 *
	 * @param c          透明色
	 * @param exactMatch true 时精确匹配调色板索引，否则取最接近色
	 */
	public void setTransparent(Color c, boolean exactMatch) {
		transparent = c;
		transparentExactMatch = exactMatch;
	}


	/**
	 * 设置背景色.
	 * <p>
	 * 量化后取最接近给定颜色的调色板项作为背景色；null 表示默认黑色.
	 *
	 * @param c 背景色
	 */
	public void setBackground(Color c) {
		background = c;
	}

	/**
	 * 添加下一帧 GIF 图像.
	 * <p>
	 * 帧数据延迟写入，调用 {@link #finish()} 时一并刷出；若未调用 {@link #setSize(int, int)} 则以首帧尺寸为准.
	 *
	 * @param im 帧图像
	 * @return 是否成功
	 */
	public boolean addFrame(BufferedImage im) {
		if ((im == null) || !started) {
			return false;
		}
		boolean ok = true;
		try {
			if (!sizeSet) {
				/* 以首帧尺寸作为画布大小 */
				setSize(im.getWidth(), im.getHeight());
			}
			image = im;
			getImagePixels();
			analyzePixels();
			if (firstFrame) {
				writeLSD();
				writePalette();
				if (repeat >= 0) {
					writeNetscapeExt();
				}
			}
			writeGraphicCtrlExt();
			writeImageDesc();
			if (!firstFrame) {
				writePalette();
			}
			writePixels();
			firstFrame = false;
		} catch (IOException e) {
			ok = false;
		}

		return ok;
	}

	/**
	 * 刷出剩余数据并结束 GIF 写入.
	 * <p>
	 * 写入 OutputStream 时不会自动关闭流.
	 *
	 * @return 是否成功
	 */
	public boolean finish() {
		if (!started) return false;
		boolean ok = true;
		started = false;
		try {
			out.write(0x3b);
			out.flush();
			if (closeStream) {
				out.close();
			}
		} catch (IOException e) {
			ok = false;
		}

		/* 重置状态，便于复用同一实例 */
		transIndex = 0;
		out = null;
		image = null;
		pixels = null;
		indexedPixels = null;
		colorTab = null;
		closeStream = false;
		firstFrame = true;

		return ok;
	}

	/**
	 * 设置帧率（帧/秒）.
	 * <p>
	 * 等价于 {@code setDelay(1000 / fps)}.
	 *
	 * @param fps 帧率
	 */
	public void setFrameRate(float fps) {
		if (fps != 0f) {
			delay = Math.round(100f / fps);
		}
	}

	/**
	 * 设置颜色量化质量（1–256 色调色板）.
	 * <p>
	 * 值越小颜色越好但越慢，默认 10.
	 *
	 * @param quality 质量，须大于 0
	 */
	public void setQuality(int quality) {
		if (quality < 1) quality = 1;
		sample = quality;
	}

	/**
	 * 设置 GIF 画布尺寸.
	 * <p>
	 * 未调用时以首帧尺寸为准.
	 *
	 * @param w 宽度
	 * @param h 高度
	 */
	public void setSize(int w, int h) {
		if (started && !firstFrame) return;
		width = w;
		height = h;
		if (width < 1) width = 320;
		if (height < 1) height = 240;
		sizeSet = true;
	}

	/**
	 * 在指定输出流上开始写入 GIF.
	 * <p>
	 * 不会自动关闭流.
	 *
	 * @param os 输出流
	 * @return 初始写入是否成功
	 */
	public boolean start(OutputStream os) {
		if (os == null) return false;
		boolean ok = true;
		closeStream = false;
		out = os;
		try {
			writeString("GIF89a");
		} catch (IOException e) {
			ok = false;
		}
		return started = ok;
	}

	/**
	 * 在指定文件路径上开始写入 GIF.
	 *
	 * @param file 输出文件路径
	 * @return 打开或初始写入是否成功
	 */
	public boolean start(String file) {
		boolean ok;
		try {
			out = new BufferedOutputStream(Files.newOutputStream(Paths.get(file)));
			ok = start(out);
			closeStream = true;
		} catch (IOException e) {
			ok = false;
		}
		return started = ok;
	}

	/**
	 * 是否已开始写入 GIF.
	 *
	 * @return 是否已开始
	 */
	public boolean isStarted() {
		return started;
	}

	/**
	 * 分析图像颜色并创建调色板.
	 */
	protected void analyzePixels() {
		int len = pixels.length;
		int nPix = len / 3;
		indexedPixels = new byte[nPix];
		NeuQuant nq = new NeuQuant(pixels, len, sample);
		/* 初始化量化器 */
		colorTab = nq.process(); /* 创建缩减调色板 */
		/* 将调色板从 BGR 转换为 RGB */
		for (int i = 0; i < colorTab.length; i += 3) {
			byte temp = colorTab[i];
			colorTab[i] = colorTab[i + 2];
			colorTab[i + 2] = temp;
			usedEntry[i / 3] = false;
		}
		/* 将图像像素映射到新调色板 */
		int k = 0;
		for (int i = 0; i < nPix; i++) {
			int index =
					nq.map(pixels[k++] & 0xff,
							pixels[k++] & 0xff,
							pixels[k++] & 0xff);
			usedEntry[index] = true;
			indexedPixels[i] = (byte) index;
		}
		pixels = null;
		colorDepth = 8;
		palSize = 7;
		/* 若指定了透明色，则在调色板中查找最接近的匹配 */
		if (transparent != null) {
			transIndex = transparentExactMatch ? findExact(transparent) : findClosest(transparent);
		}
	}

	/**
	 * 返回调色板中与给定颜色最接近的索引.
	 *
	 * @param c 颜色
	 * @return 最接近的调色板索引
	 */
	protected int findClosest(Color c) {
		if (colorTab == null) return -1;
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		int minpos = 0;
		int dmin = 256 * 256 * 256;
		int len = colorTab.length;
		for (int i = 0; i < len; ) {
			int dr = r - (colorTab[i++] & 0xff);
			int dg = g - (colorTab[i++] & 0xff);
			int db = b - (colorTab[i] & 0xff);
			int d = dr * dr + dg * dg + db * db;
			int index = i / 3;
			if (usedEntry[index] && (d < dmin)) {
				dmin = d;
				minpos = index;
			}
			i++;
		}
		return minpos;
	}

	/**
	 * 判断指定颜色是否存在于调色板中且已被使用.
	 * <p>
	 * 须在 {@link #finish()} 之前调用，因为完成后调色板会被销毁，此方法将始终返回 false.
	 *
	 * @param c 颜色
	 * @return 颜色是否存在
	 */
	boolean isColorUsed(Color c) {
		return findExact(c) != -1;
	}

	/**
	 * 返回调色板中与给定颜色精确匹配的索引，无匹配时返回 -1.
	 *
	 * @param c 颜色
	 * @return 精确匹配的调色板索引，无匹配时返回 -1
	 */
	protected int findExact(Color c) {
		if (colorTab == null) {
			return -1;
		}

		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		int len = colorTab.length / 3;
		for (int index = 0; index < len; ++index) {
			int i = index * 3;
			/* 若该调色板项已被使用，则检查是否为精确匹配的颜色 */
			if (usedEntry[index] && r == (colorTab[i] & 0xff) && g == (colorTab[i + 1] & 0xff) && b == (colorTab[i + 2] & 0xff)) {
				return index;
			}
		}
		return -1;
	}

	/**
	 * 提取图像像素到字节数组.
	 */
	protected void getImagePixels() {
		int w = image.getWidth();
		int h = image.getHeight();
		int type = image.getType();
		if ((w != width)
				|| (h != height)
				|| (type != BufferedImage.TYPE_3BYTE_BGR)) {
			/* 创建正确尺寸/格式的临时图像 */
			BufferedImage temp =
					new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D g = temp.createGraphics();
			g.setColor(background);
			g.fillRect(0, 0, width, height);
			g.drawImage(image, 0, 0, null);
			image = temp;
		}
		pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	}

	/**
	 * 写入图形控制扩展块.
	 *
	 * @throws IOException IO异常
	 */
	protected void writeGraphicCtrlExt() throws IOException {
		out.write(0x21); /* 扩展块引导符 */
		out.write(0xf9); /* 图形控制扩展标签 */
		out.write(4); /* 数据块大小 */
		int transp, disp;
		if (transparent == null) {
			transp = 0;
			disp = 0; /* 处置方式 = 无操作 */
		} else {
			transp = 1;
			disp = 2; /* 使用透明色时强制清除 */
		}
		if (dispose >= 0) {
			disp = dispose & 7; /* 用户覆盖 */
		}
		disp <<= 2;

		/* 压缩字段 */
		//noinspection PointlessBitwiseExpression
		out.write(0 | /* 1:3 保留 */
				disp | /* 4:6 处置方式 */
				0 | /* 7   用户输入 - 0 = 无 */
				transp); /* 8   透明度标志 */

		writeShort(delay); /* 延迟 x 1/100 秒 */
		out.write(transIndex); /* 透明色索引 */
		out.write(0); /* 块终结符 */
	}

	/**
	 * 写入图像描述符.
	 *
	 * @throws IOException IO异常
	 */
	protected void writeImageDesc() throws IOException {
		out.write(0x2c); /* 图像分隔符 */
		writeShort(0); /* 图像位置 x,y = 0,0 */
		writeShort(0);
		writeShort(width); /* 图像尺寸 */
		writeShort(height);
		/* 压缩字段 */
		if (firstFrame) {
			/* 首帧（或唯一帧）使用全局颜色表，不使用局部颜色表 */
			out.write(0);
		} else {
			/* 指定使用局部颜色表 */
			//noinspection PointlessBitwiseExpression
			out.write(0x80 | /* 1 局部颜色表  1=是 */
					0 | /* 2 隔行扫描 - 0=否 */
					0 | /* 3 排序 - 0=否 */
					0 | /* 4-5 保留 */
					palSize); /* 6-8 颜色表大小 */
		}
	}

	/**
	 * 写入逻辑屏幕描述符.
	 *
	 * @throws IOException IO异常
	 */
	protected void writeLSD() throws IOException {
		/* 逻辑屏幕尺寸 */
		writeShort(width);
		writeShort(height);
		/* 压缩字段 */
		//noinspection PointlessBitwiseExpression
		out.write((0x80 | /* 1   : 全局颜色表标志 = 1（使用全局颜色表） */
				0x70 | /* 2-4 : 颜色分辨率 = 7 */
				0x00 | /* 5   : 全局颜色表排序标志 = 0 */
				palSize)); /* 6-8 : 全局颜色表大小 */

		out.write(0); /* 背景色索引 */
		out.write(0); /* 像素宽高比 - 假定 1:1 */
	}

	/**
	 * 写入 Netscape 应用扩展块以定义循环次数.
	 *
	 * @throws IOException IO异常
	 */
	protected void writeNetscapeExt() throws IOException {
		out.write(0x21); /* 扩展块引导符 */
		out.write(0xff); /* 应用扩展标签 */
		out.write(11); /* 块大小 */
		writeString("NETSCAPE" + "2.0"); /* 应用标识 + 认证码 */
		out.write(3); /* 子块大小 */
		out.write(1); /* 循环子块标识 */
		writeShort(repeat); /* 循环次数（额外迭代次数，0=无限循环） */
		out.write(0); /* 块终结符 */
	}

	/**
	 * 写入调色板.
	 *
	 * @throws IOException IO异常
	 */
	protected void writePalette() throws IOException {
		out.write(colorTab, 0, colorTab.length);
		int n = (3 * 256) - colorTab.length;
		for (int i = 0; i < n; i++) {
			out.write(0);
		}
	}

	/**
	 * 编码并写入像素数据.
	 *
	 * @throws IOException IO异常
	 */
	protected void writePixels() throws IOException {
		LZWEncoder encoder = new LZWEncoder(width, height, indexedPixels, colorDepth);
		encoder.encode(out);
	}

	/**
	 * 写入 16 位值到输出流，低位字节优先.
	 *
	 * @param value 16 位值
	 * @throws IOException IO异常
	 */
	protected void writeShort(int value) throws IOException {
		out.write(value & 0xff);
		out.write((value >> 8) & 0xff);
	}

	/**
	 * 写入字符串到输出流.
	 *
	 * @param s 字符串
	 * @throws IOException IO异常
	 */
	protected void writeString(String s) throws IOException {
		for (int i = 0; i < s.length(); i++) {
			out.write((byte) s.charAt(i));
		}
	}
}
