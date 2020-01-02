package io.renren.common.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * 读写文件工具类
 * @author 70634
 *
 */
public class FileUtils {

	/**
	 * 传入txt路径读取txt文件
	 * @param txtPath
	 * @return
	 */
	public static String readTxt(String txtPath) {
		File file=new File(txtPath);
		FileInputStream fis=null;
		if(file.isFile() && file.exists()) {
			try {
				fis=new FileInputStream(file);
				InputStreamReader isr=new InputStreamReader(fis);
				BufferedReader br=new BufferedReader(isr);
				StringBuffer sb=new StringBuffer();
				String text=null;
				while((text=br.readLine())!=null) {
					sb.append(text);
				}
				return sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	/**
	 * 写入文件方法
	 * @param txtPath
	 * @param content
	 */
	public static  void writeTxt(String txtPath,String content) {
		FileOutputStream fos=null;
		File file=new File(txtPath);
		try {
			//判断文件是否存在，不存在就创建一个
			if(file.exists()) {
				file.createNewFile();
			}
			fos=new FileOutputStream(file); 
			OutputStreamWriter  osw=new OutputStreamWriter (fos,"UTF-8");
			BufferedWriter  bw=new BufferedWriter(osw);
			bw.write(content+"\r\n");
			bw.flush();
			bw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
