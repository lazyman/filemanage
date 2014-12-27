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
			System.out.println("Usage: ");
			
			String commad = "/tananlyze ";
			System.out.println(commad + "<path> [operation]");
			System.out.println("operation=list | ananlyze | byconfig");
			System.out
					.println("list: list the files under the special <path>, or show the <path> unless is file");
			System.out.println("ananlyze: the default operation");
			System.out
					.println("byconfig: analyze file by the config, <path> and <exclude> all in one");
		} else if(args.length == 2){
			String basedir = args[0];
			String operation = args[1];
			
			if("ananlyze".equals(operation)) {
				// 计算指定目录下的md5
				manager.ananlyze(basedir);
			} else if("list".equals(operation)) {
				// 展示当前目录的文件
				manager.showList(basedir);
			} else if("byconfig".equals(operation)) {
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
