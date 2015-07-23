package whu.b606.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 生成图片缩略图工具
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2014年11月6日
 **/
public class ImageUtil {
	/**
	 * 将本地图片加载到内存缓冲区
	 */
	public static BufferedImage loadImageLocal(String imgName) {
		try {
			return ImageIO.read(new File(imgName));
		} catch (IOException e) {// 加载不成功
			// System.out.println("未能将指定路径的图片加载至内存，图片文件可能不存在(当删除服务器目录中的文件后又重新发布后可能存在这种情况)，请检查！此异常位于：whu.b606.util.ImageUtil.java：28行");
			return null;
		}
	}

	/**
	 * 生成缩略图
	 * 
	 * @param fromPath
	 *            原图片的路径
	 * @param toPath
	 *            缩略图的保存路径
	 * @param width
	 *            缩略图的宽
	 * @param height
	 *            缩略图的高
	 * @param equalProportion
	 *            是否等比缩放
	 */
	public static void thumbnail(String fromPath, String toPath, int width, int height, boolean equalProportion) throws Exception {
		BufferedImage srcImage;
		String ext = fromPath.substring(fromPath.lastIndexOf(".") + 1);
		String imgType = ext.toUpperCase();
		File fromFile = new File(fromPath);
		File saveFile = new File(toPath);
		srcImage = ImageIO.read(fromFile);
		if (width > 0 || height > 0) {
			srcImage = resize(srcImage, width, height, equalProportion);
		}
		ImageIO.write(srcImage, imgType, saveFile);
	}

	/**
	 * 生成BufferedImage对象的缩略图
	 * 
	 * @param source
	 *            原图片的BufferedImage对象
	 * @param targetW
	 *            缩略图的宽
	 * @param targetH
	 *            缩略图的高
	 * @param equalProportion
	 *            是否等比缩放
	 * @return 缩放后的BufferedImage对象
	 */
	public static BufferedImage resize(BufferedImage source, int targetW, int targetH, boolean equalProportion) {
		int type = source.getType();
		BufferedImage target = null;
		double sx = (double) targetW / source.getWidth();
		double sy = (double) targetH / source.getHeight();
		// 这里想实现在targetW，targetH范围内实现等比例的缩放
		// 如果不需要等比例的缩放则下面的if else语句注释调即可
		if (equalProportion) {
			if (sx > sy) {
				sx = sy;
				targetW = (int) (sx * source.getWidth());
			} else {
				sy = sx;
				targetH = (int) (sx * source.getHeight());
			}
		}
		if (type == BufferedImage.TYPE_CUSTOM) {
			ColorModel cm = source.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(targetW, targetH);
			boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			target = new BufferedImage(cm, raster, alphaPremultiplied, null);
		} else {
			target = new BufferedImage(targetW, targetH, type);
			Graphics2D g = target.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
			g.dispose();
		}
		return target;
	}
}
