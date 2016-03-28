package com.ag.common.file;

import android.os.Environment;

import com.ag.common.other.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;


public class AGFile {

	private static String SDPATH=Environment.getExternalStorageDirectory() + "/";

	public String getSDPATH() {
		return SDPATH;
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @throws IOException
	 */
	public static File creatSDFile(String fileName) throws IOException {
		File file = new File(SDPATH + fileName);
		file.createNewFile();
		return file;
	}
	
	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 */
	public static File creatSDDir(String dirName) {
		File dir = new File(SDPATH + dirName);
		dir.mkdir();
		return dir;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 */
	public static boolean isFileExist(String fileName){
		File file = new File(SDPATH + fileName);
		return file.exists();
	}
	
	/**
	 * 根据文件绝对路径获取文件名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		if (StringUtils.isEmpty(filePath))
			return "";
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
	}
	
	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 */
	public File write2SDFromInput(String path,String fileName,InputStream input){
		File file = null;
		OutputStream output = null;
		try{
			creatSDDir(path);
			file = creatSDFile(path + fileName);
			output = new FileOutputStream(file);
			byte buffer [] = new byte[4 * 1024];
			while((input.read(buffer)) != -1){
				output.write(buffer);
			}
			output.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				output.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 读取文件大小
	 * @param filePath
	 * @return
	 */
	public static long getFileLength(String filePath){
		File file=new File(filePath);
		return file.length();
	}
	/**
	 * 读取文件夹大小
	 * @param dirPath
	 * @return
	 */
	public static long getPathLength(String dirPath){
		File dir=new File(dirPath);
		return getDirSize(dir);
	}
	/**
	 * 读取文件夹大小
	 * @param dir
	 * @return
	 */
	private static long getDirSize(File dir) {
		if (dir == null) {
			return 0;
		}
		if (!dir.isDirectory()) {
			return 0;
		}
		long dirSize = 0;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				dirSize += file.length();
			} else if (file.isDirectory()) {
				dirSize += file.length();
				dirSize += getDirSize(file); // 如果遇到目录则通过递归调用继续统计
			}
		}
		return dirSize;
	}
	/**
	 * 将字长长度转换为KB/MB
	 * @param size
	 * @return
	 */
	public static String getFileSize(long size){
		int kbSize=(int)size/1024;
		if(kbSize>1024){
			float mbSize=kbSize/1024;
			DecimalFormat formator=new DecimalFormat( "##,###,###.## ");
			return formator.format(mbSize) + "M";
		}
		return kbSize + "K";
	}

	/**
	 * 删除此路径名表示的文件或目录。 如果此路径名表示一个目录，则会先删除目录下的内容再将目录删除，所以该操作不是原子性的。
	 * 如果目录中还有目录，则会引发递归动作。
	 *
	 * @param filePath
	 *            要删除文件或目录的路径。
	 * @return 当且仅当成功删除文件或目录时，返回 true；否则返回 false。
	 */
	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.listFiles() == null)
			return true;
		else {
			File[] files = file.listFiles();
			for (File deleteFile : files) {
				if (deleteFile.isDirectory())
					deleteAllFile(deleteFile);
				else
					deleteFile.delete();
			}
		}
		return true;
	}
	/**
	 * 删除全部文件
	 *
	 * @param file
	 * @return
	 */
	private static boolean deleteAllFile(File file) {
		File[] files = file.listFiles();
		for (File deleteFile : files) {
			if (deleteFile.isDirectory()) {
				// 如果是文件夹，则递归删除下面的文件后再删除该文件夹
				if (!deleteAllFile(deleteFile)) {
					// 如果失败则返回
					return false;
				}
			} else {
				if (!deleteFile.delete()) {
					// 如果失败则返回
					return false;
				}
			}
		}
		return file.delete();
	}

	public static List<String> getJpgsFile(File file){
		if(file==null || !file.exists())
			return null;
		return Arrays.asList(file.list(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String filename)
			{
				filename= filename.toLowerCase();
				if (filename.endsWith(".jpg")
						|| filename.endsWith(".png")
						|| filename.endsWith(".jpeg"))
					return true;
				return false;
			}
		}));
	}

}