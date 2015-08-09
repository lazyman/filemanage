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
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zjtlcb.aml.util.SecurityUtil;
import com.zjtlcb.aml.util.UtilFactory;

public class FileManager {
	private static Log logger = LogFactory.getLog(FileManager.class);
	
	public final static int RECORD_TYPE_MEMERY = 0;
	public final static int RECORD_TYPE_FILE = 1;
	public final static int RECORD_TYPE_DB = 2;
	
	/**
	 * list | ananlyze | byconfig
	 */
	public final static String OPR_CMD_LIST = "list";
	public final static String OPR_CMD_ANANLYZE = "ananlyze";
	public final static String OPR_CMD_CONFIG = "config";
	
	private int recordType;
	
	/**
	 * 分析目录
	 * @param basedir 基础目录
	 */
	public void ananlyze(String basedir) {
		logger.info("针对basedir=" + basedir + "启动分析程序！！");
		File path = new File(basedir);
		
		List<FileInfo> list = ananlyze(basedir, path);
		
//		if(logger.isDebugEnabled()) {
//			for(FileInfo fi : list) {
//				logger.debug(fi);
//			}
//		}
		
		logger.info("分析程序结束，文件总数量：" + list.size());
	}
	
	/**
	 * 分析目录或文件-全路径
	 * <p>文件，计算MD5，并将结果写入数据库
	 * @param path
	 * @return 
	 */
	private List<FileInfo> ananlyze(String basedir, File fullPath) {
		SecurityUtil util = new SecurityUtil();
		if(logger.isTraceEnabled()) {
			logger.trace("分析文件：" + fullPath.toString());
		}
		
		ArrayList<FileInfo> filelist = new ArrayList<FileInfo>();
		
		if(fullPath.isFile()) {
			FileInfo fileInfo = new FileInfo();
			// 为了获取相对的 filepath
			fileInfo.setBasePath(basedir, fullPath);
			String filepath = fileInfo.getPath();
			
			if(!hasAnalyzed(filepath)) {
				FileInfo fInfo = util.getMd5Digest(fullPath);
				fInfo.setBasePath(basedir, fullPath);
				
				record(fInfo);
				filelist.add(fInfo);
			}
		} else {
			// 递归分析子目录
			File[] dirs = fullPath.listFiles();
			
			for(int i=0; i<dirs.length; i++) {
				filelist.addAll(ananlyze(basedir, dirs[i]));
			}
		}
		
		return filelist;
	}

	/**
	 * 展示指定文件信息， 是文件显示文件路径，是路径则显示路径中的文件数量（当前目录，不递归）
	 * @param basedir
	 */
	public void showList(String basedir) {
		File path = new File(basedir);
		
		if(path.isFile()) {
			String filepath = path.getAbsolutePath();
			
			logger.info(filepath);
		} else {
			File[] dirs = path.listFiles();
			
			logger.info("dir count:" + dirs.length);
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
		
		switch (recordType) {
		case RECORD_TYPE_FILE:
			logger.warn("not implemented");
			break;
			
		case RECORD_TYPE_DB:
			// 跳出switch循环，接着数据库查询
			break;

		default:
			return false;
		}
		
		
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
			
		case RECORD_TYPE_MEMERY:
			logger.info(fInfo);
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
		String md5file = fInfo.getFullPath();
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
			prestmt.setString(parameterIndex++, fInfo.getPath());
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

	public void parse(String configfile) {
		// TODO 根据指定配置文件执行分析操作，主要用于排除一些文件、目录
		System.out.println("it does not implement!");
	}
	
	/**
	 * 启动
	 * @param args
	 */
	public static void main(String[] args) {
		FileManager manager = new FileManager();

//		manager.setRecordType(FileManager.RECORD_TYPE_FILE);
		manager.setRecordType(FileManager.RECORD_TYPE_DB);
		manager.setRecordType(FileManager.RECORD_TYPE_MEMERY);
		
//		String basedir = "J:\\kuaipan\\60-soft";
//		String basedir = "J:\\kuaipan";
//		String basedir = "G:\\迅雷下载";
		if(args.length ==0 ) {
			StringBuilder cmdNote = new StringBuilder();
			cmdNote.append("please add a string as ananlyzed basedir.");
			cmdNote.append('\n');
			cmdNote.append("Usage: ");
			cmdNote.append("\n\t");
			
			cmdNote.append("ananlyze <path> [operation]");
			cmdNote.append("\n");
			cmdNote.append("operation: list | ananlyze | config");
			cmdNote.append("\n");
			cmdNote.append("list: list the files under the special <path>, or show the <path> unless is file");
			cmdNote.append("\n");
			cmdNote.append("ananlyze: the default operation");
			cmdNote.append("\n");
			cmdNote.append("byconfig: analyze file by the config, <path> and <exclude> all in one");
			cmdNote.append("\n");
			
			System.out.println(cmdNote);
		} else if(args.length == 2){
			String basedir = args[0];
			String operation = args[1];
			
			if(OPR_CMD_ANANLYZE.equals(operation)) {
				// 计算指定目录下的md5
				manager.ananlyze(basedir);
			} else if(OPR_CMD_LIST.equals(operation)) {
				// 展示当前目录的文件
				manager.showList(basedir);
			} else if(OPR_CMD_CONFIG.equals(operation)) {
				// 根据指定配置文件执行操作，基本上用于排除某些文件
				manager.parse(basedir );
			}
		} else {
			// 计算指定目录下的md5
			String basedir = args[0];
			manager.ananlyze(basedir);
		}
	}
}
