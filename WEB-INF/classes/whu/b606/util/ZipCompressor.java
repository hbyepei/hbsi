package whu.b606.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件压缩工具
 * 
 * @author Yepei,Wuhan University
 * @version 1.2
 * @date 2015年4月18日
 */
public class ZipCompressor {
	static final int BUFFER = 1024 * 8;
	private File zipFile;

	/**
	 * 构造一个空的压缩文件
	 * 
	 * @param pathName
	 *            后缀名为zip的完整文件路径名
	 * @param rootPath
	 *            压缩文件中是否包含原目录（若为true，则解压后的文件包含目录，否则解压后直接得到文件）
	 */
	public ZipCompressor(String pathName) {
		zipFile = new File(pathName);
	}

	/**
	 * 不提供压缩输出路径的构造器，此构造器只能在仅调用compressIO方法而不具体输出文件时使用
	 */
	public ZipCompressor() {}

	/**
	 * 将目录压缩
	 * 
	 * @param srcPathName
	 *            待压缩目录
	 */
	public void compress(String srcPathName) {
		File file = new File(srcPathName);
		if (!file.exists()) throw new RuntimeException(srcPathName + "不存在！");
		ZipOutputStream out = null;
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
			CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
			out = new ZipOutputStream(cos);
			String basedir = "";
			compress(file, out, basedir);
			out.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public InputStream compressIO(String srcPathName) {
		File file = new File(srcPathName);
		if (!file.exists()) throw new RuntimeException(srcPathName + "不存在！");
		ZipOutputStream zos = null;
		CheckedOutputStream cos = null;
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			cos = new CheckedOutputStream(out, new CRC32());
			zos = new ZipOutputStream(cos);
			String basedir = "";
			compress(file, zos, basedir);
			zos.close();
			InputStream in = new ByteArrayInputStream(out.toByteArray());// 要返回的流
			return in;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void compress(File file, ZipOutputStream out, String basedir) {
		/* 判断是目录还是文件 */
		if (file.isDirectory()) {
			this.compressDirectory(file, out, basedir);
		} else {
			this.compressFile(file, out, basedir);
		}
	}

	/** 压缩一个目录 */
	private void compressDirectory(File dir, ZipOutputStream out, String basedir) {
		if (!dir.exists()) return;
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			/* 递归 */
			compress(files[i], out, basedir + dir.getName() + "/");
		}
	}

	/** 压缩一个文件 */
	private void compressFile(File file, ZipOutputStream out, String basedir) {
		if (!file.exists()) { return; }
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			ZipEntry entry = new ZipEntry(basedir + file.getName());
			out.putNextEntry(entry);
			int count;
			byte data[] = new byte[BUFFER];
			while ((count = bis.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			bis.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
