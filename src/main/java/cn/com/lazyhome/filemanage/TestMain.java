package cn.com.lazyhome.filemanage;

public class TestMain {
	public static void main(String[] args) {
		FileManager manager = new FileManager();

//		manager.setRecordType(FileManager.RECORD_TYPE_FILE);
		manager.setRecordType(FileManager.RECORD_TYPE_DB);
		
//		String basedir = "J:\\kuaipan\\60-soft";
//		String basedir = "J:\\kuaipan";
		String basedir = "G:\\迅雷下载";
		manager.ananlyze(basedir);
	}
}
