package cn.com.lazyhome.filemanage.filetest;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class TestFileUsage {
	
	@Test
	public void testFileUsage() {
		String pathname = "/tmp/yxpt";
		File f = new File(pathname);
		
		// 后缀：yxpt
		System.out.println(f.getName());
		// 输入时的路径，同path：\tmp\yxpt
		System.out.println(f.toString());
		// 绝对路径：F:\tmp\yxpt
		System.out.println(f.getAbsolutePath());
		// 输入时的路径：\tmp\yxpt
		System.out.println(f.getPath());
		
		try {
			// F:\tmp\yxpt
//			System.out.println(f.getCanonicalPath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 e.printStackTrace();
		}
		// 父节点路径，不是绝对路径：\tmp
		System.out.println(f.getParent());
		
		
//		fail("Not yet implemented");
	}

}
