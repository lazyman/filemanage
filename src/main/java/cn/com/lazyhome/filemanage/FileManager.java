package cn.com.lazyhome.filemanage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zjtlcb.aml.util.SecurityUtil;
import com.zjtlcb.aml.util.UtilFactory;

public class FileManager {
	private static Log logger = LogFactory.getLog(FileManager.class);
	
	public final static int RECORD_TYPE_MEMERY = 0;
	public final static int RECORD_TYPE_FILE = 1;
	public final static int RECORD_TYPE_DB = 2;
	
	private int recordType;
	
	/**
	 * 分析目录
	 * @param basedir 基础目录
	 */
	public void ananlyze(String basedir) {
		File path = new File(basedir);
		
		List<FileInfo> list = ananlyze(path);
		
		if(logger.isDebugEnabled()) {
			for(FileInfo fi : list) {
				logger.debug(fi);
			}
			
			logger.debug("文件数量：" + list.size());
		}
	}
	
	/**
	 * 分析目录或文件
	 * <p>文件，计算MD5，并将结果写入数据库
	 * @param path
	 * @return 
	 */
	private List<FileInfo> ananlyze(File path) {
		SecurityUtil util = new SecurityUtil();
		if(logger.isTraceEnabled()) {
			logger.trace("分析文件：" + path.toString());
		}
		
		Vector<FileInfo> vector = new Vector<FileInfo>();
		
		if(path.isFile()) {
			String filepath = path.getAbsolutePath();
			if(!hasAnalyzed(filepath)) {
				FileInfo fInfo = util.getMd5Digest(path);
				
				record(fInfo);
				vector.add(fInfo);
			}
		} else {
			File[] dirs = path.listFiles();
			
			for(int i=0; i<dirs.length; i++) {
				vector.addAll(ananlyze(dirs[i]));
			}
		}
		
		return vector;
	}

	public void showList(String basedir) {
		File path = new File(basedir);
		
		if(path.isFile()) {
			String filepath = path.getAbsolutePath();
			
			logger.info(filepath);
		} else {
			File[] dirs = path.listFiles();
			
			for(int i=0; i<dirs.length; i++) {
				logger.info(dirs[i]);
			}
		}
	}
	/**
	 * 检查是否已经分析过
	 * @param filepath
	 * @return
	 */
	private boolean hasAnalyzed(String filepath) {
		// TODO 检查是否已经分析过
		UtilFactory factory = UtilFactory.getInstance();
		boolean analyzed = false;
		
		try {
			String sql = "select * from filemanage where filepath = ?";
			
			Connection conn = factory.getConnection();
			PreparedStatement prestmt = conn.prepareStatement(sql);
			
			int parameterIndex=1;
			prestmt.setString(parameterIndex++, filepath);
			ResultSet rs = prestmt.executeQuery();
			if(rs.next()) {
				analyzed = true;
			}
			rs.close();
			prestmt.close();
			
			conn.close();
		} catch (SQLException e) {
			logger.error("检查是否已经分析过", e);
		}
		
		return analyzed;
	}

	/**
	 * 记录分析结果，可以是内存变量、数据库、文件系统
	 * @param fInfo
	 */
	private void record(FileInfo fInfo) {
		// TODO 记录分析结果，可以是内存变量、数据库、文件系统
		switch (recordType) {
		case RECORD_TYPE_FILE:
			saveToFile(fInfo);
			break;
			
		case RECORD_TYPE_DB:
			saveToDb(fInfo);
			break;

		default:
			break;
		}
	}

	/**
	 * 保存信息到文件系统
	 * @param fInfo
	 */
	private void saveToFile(FileInfo fInfo) {
		String suffix = ".md5";
		// TODO 保存信息到文件系统
		String md5file = fInfo.getFullName();
		if(md5file.endsWith(suffix)) {
			return;
		} else {
			md5file += ".md5";
		}
		try {
			FileWriter fw = new FileWriter(md5file);
			fw.write(fInfo.getMd5());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存信息到数据库
	 * @param fInfo
	 */
	private void saveToDb(FileInfo fInfo) {
		// TODO 保存信息到数据库
		UtilFactory factory = UtilFactory.getInstance();
		try {
			String sql = "insert into filemanage(filepath, md5, filesize, usetime,begintime, endtime, recordtime) values(?, ?, ?, ?, ?, ?, ?)";
			
			Connection conn = factory.getConnection();
			PreparedStatement prestmt = conn.prepareStatement(sql);
			
			int parameterIndex=1;
			prestmt.setString(parameterIndex++, fInfo.getFullName());
			prestmt.setString(parameterIndex++, fInfo.getMd5());
			prestmt.setLong(parameterIndex++, fInfo.getSize());
			prestmt.setLong(parameterIndex++, fInfo.getAnnalyzeUse());
			prestmt.setTimestamp(parameterIndex++, new Timestamp(fInfo.getBegintime().getTime()) );
			prestmt.setTimestamp(parameterIndex++, new Timestamp(fInfo.getEndtime().getTime()) );
			prestmt.setTimestamp(parameterIndex++, new Timestamp(System.currentTimeMillis()) );
			prestmt.execute();
			prestmt.close();
			
			conn.close();
		} catch (SQLException e) {
			logger.error("记录统计数据", e);
		}
		
	}

	public int getRecordType() {
		return recordType;
	}

	public void setRecordType(int recordType) {
		this.recordType = recordType;
	}
}
