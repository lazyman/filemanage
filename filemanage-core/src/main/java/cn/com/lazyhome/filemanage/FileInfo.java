package cn.com.lazyhome.filemanage;

import java.io.File;
import java.util.Date;

/**
 * 文件信息。
 * fullPath、basePath、size、md5、annalyzeUse必填
 * 如完整path是/d/1st/2nd/3rd.txt，基础路径是/d/1st/，则：
 * fullPath = /d/1st/2nd/3rd.txt
 * basePath = /d/1st/
 * path = /2nd/3rd.txt
 * name = 3rd.txt
 * @author dch
 *
 */
public class FileInfo {
	private File file;
	
	/**
	 * 相对基础目录的目录名称，基于baseName之下是唯一的。第一个字符不是斜杠（“/”）。
	 */
	private String path;
	/**
	 * 基础路径（目录），以斜杠（“/”）结尾
	 */
	private String basePath;
	/**
	 * 短名称
	 */
	private String name;
	/**
	 * 全路径。basePath + path
	 */
	private String fullPath;
	/**
	 * 文件大小，单位字节
	 */
	private long size;
	/**
	 * 文件大小，单位Mb
	 */
	private long sizeM;
	/**
	 * 文件大小，单位自动计算，以适合人阅读。如20.0M，12.3G，23.45K等等
	 */
	private String sizeHumen;
	private Date createTime;
	private long modifyTime;
	private Date lastAnalyzeTime;
	
	private Date begintime;
	private Date endtime;
	public Date getBegintime() {
		return begintime;
	}

	public void setBegintime(Date begintime) {
		this.begintime = begintime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	/**
	 * 分析耗时，单位毫秒
	 */
	private long annalyzeUse;
	private String md5;
	private byte[] md5byte;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FileInfo [fullPath=");
		builder.append(fullPath);
		builder.append(", size=");
		builder.append(size);
		builder.append(", md5=");
		builder.append(md5);
		builder.append("]");
		return builder.toString();
	}
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String name) {
		this.path = name;
	}
	public String getBasePath() {
		return basePath;
	}
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	public String getName() {
		return name;
	}
	public void setName(String simpleName) {
		this.name = simpleName;
	}
	public String getFullPath() {
		return fullPath;
	}
	public void setFullPath(String fullName) {
		this.fullPath = fullName;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public long getSizeM() {
		return sizeM;
	}
	public void setSizeM(long sizeM) {
		this.sizeM = sizeM;
	}
	public String getSizeHumen() {
		return sizeHumen;
	}
	public void setSizeHumen(String sizeHumen) {
		this.sizeHumen = sizeHumen;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public long getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(long modifyTime) {
		this.modifyTime = modifyTime;
	}
	public Date getLastAnalyzeTime() {
		return lastAnalyzeTime;
	}
	public void setLastAnalyzeTime(Date lastAnalyzeTime) {
		this.lastAnalyzeTime = lastAnalyzeTime;
	}
	public long getAnnalyzeUse() {
		return annalyzeUse;
	}
	public void setAnnalyzeUse(long annalyzeUse) {
		this.annalyzeUse = annalyzeUse;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public byte[] getMd5byte() {
		return md5byte;
	}
	public void setMd5byte(byte[] md5byte) {
		this.md5byte = md5byte;
	}

	/**
	 * 根据基础路径和全路径得出短名称和相对名称。
	 * @param basePath 基础路径
	 * @param fullPathFile 全路径
	 */
	public void setBasePath(String basePath, File fullPathFile) {
		this.file = fullPathFile;
		
		this.basePath = basePath;
		this.fullPath = fullPathFile.getAbsolutePath();
		
		this.name = fullPathFile.getName();
		this.path = this.fullPath.substring(basePath.length());
	}

}
