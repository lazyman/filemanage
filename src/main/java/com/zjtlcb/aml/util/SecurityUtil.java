package com.zjtlcb.aml.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.com.lazyhome.filemanage.FileInfo;

public class SecurityUtil {
	private static Log logger = LogFactory.getLog(SecurityUtil.class);
	
	public static final String ALGORITHM_MD5 = "MD5";
	public static final String ALGORITHM_SHA = "SHA-1";
	
	public static final int BYTE_BUFFER_SIZE = 8192;
	
	public FileInfo getMd5Digest(File file) {
		return getDigest(file, ALGORITHM_MD5);
	}
	public FileInfo getShaDigest(File file) {
		return getDigest(file, ALGORITHM_SHA);
	}
	
	public FileInfo getDigest(File file, String method) {
		if(logger.isInfoEnabled()) {
			logger.info("针对文件获取" + method);
			logger.info(file.getAbsoluteFile());
		}
		FileInputStream fis = null;
		
		FileInfo fInfo = new FileInfo();
		fInfo.setFile(file);
		// 文件基本信息
		fInfo.setFullPath(file.getAbsolutePath());
		fInfo.setModifyTime(file.lastModified());
		
		try {
			MessageDigest digest = MessageDigest.getInstance(method);
			
			fis = new FileInputStream(file);
			byte[] buffer = new byte[BYTE_BUFFER_SIZE];
			int length = 0;
			long sum = 0;
			
			// 计时
			long now = System.currentTimeMillis();
			
			while((length = fis.read(buffer)) != -1) {
				sum += length;
				digest.update(buffer, 0, length);
			}
			fis.close();
			long end = System.currentTimeMillis();
			
			// 文件大小
			fInfo.setSize(sum);
			fInfo.setMd5byte(digest.digest());
			
			// 正常情况
			BigInteger bi = new BigInteger(fInfo.getMd5byte());
			
			fInfo.setMd5( bi.toString(16) );
			fInfo.setAnnalyzeUse(end - now);
			fInfo.setBegintime(new Date(now));
			fInfo.setEndtime(new Date(end));
		} catch (FileNotFoundException e) {
			logger.warn("文件不存在", e);
		} catch (IOException e) {
			logger.error("读取文件错误", e);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		}

		return fInfo;
		// 异常情况时，会有一些数据缺失
	}
	
	public static String getDigest(String str, String method) {
		if(logger.isInfoEnabled()) {
			logger.info("针对字符串(" + str + ")获取" + method);
		}
		
		try {
			MessageDigest digest = MessageDigest.getInstance(method);
			digest.update(str.getBytes());
			
			// 正常情况
			BigInteger bi = new BigInteger(digest.digest());
			return bi.toString(16);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		}
		
		// 异常情况
		return "";
	}
	
	public static void main(String[] args) {
//		File f = new File("D:\\Java\\jdk1.6.0_16\\src.zip");
		String filename;
		filename = "D:\\data\\abpf10.txt";
		File f = new File(filename);
		
		long t1 = System.currentTimeMillis();
		System.out.println(new SecurityUtil().getMd5Digest(f));
		long t2 = System.currentTimeMillis();
		
		System.out.println(new SecurityUtil().getShaDigest(f));
		long t3 = System.currentTimeMillis();
		
		System.out.println(t2 - t1);
		System.out.println(t3 - t2);
	}
}
