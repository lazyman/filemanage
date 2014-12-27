package cn.com.lazyhome.filemanage;

public class TestMain {
	public static void main(String[] args) {
		FileManager manager = new FileManager();

//		manager.setRecordType(FileManager.RECORD_TYPE_FILE);
		manager.setRecordType(FileManager.RECORD_TYPE_DB);
		
//		String basedir = "J:\\kuaipan\\60-soft";
//		String basedir = "J:\\kuaipan";
//		String basedir = "G:\\迅雷下载";
		if(args.length ==0 ) {
			System.out.print("please add a string as ananlyzed basedir.");
		} else if(args.length == 2){
			// 展示当前目录的文件
			String basedir = args[0];
			
			manager.showList(basedir);
		} else {
			// 计算指定目录下的md5
			String basedir = args[0];
			manager.ananlyze(basedir);
		}
	}
}
