package io.renren.common.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>上传文件类<BR>
 * <B>概要说明：</B><BR>
 *
 * @author （Tom）
 * @since 2017年9月8日
 */
public class UploadFileUtils {
	
	/**
	 *
	 * <B>方法名称：</B>文件上传<BR>
	 * <B>概要说明：</B><BR>
	 *
	 * @author Tom
	 * @since 2017年9月13日
	 * @param dirStr
	 *            目录文件夹
	 * @param tmpFile
	 *            文件上传
	 * @param request
	 */
	public static String getUpload(String dirStr, MultipartFile tmpFile,HttpServletRequest request){
		String returnMsg="";
		if(null!=tmpFile){
			//获取物理路径
			String targetDirectory=request.getSession().getServletContext().getRealPath("/"+dirStr);
			File fileDir=new File(targetDirectory);
			if(fileDir.exists()==false){
				fileDir.mkdir();
			}
			String tmpFileName=tmpFile.getOriginalFilename();//上传文件名
			int dot=tmpFileName.lastIndexOf(".");
			String ext="";//后缀名
			if ((dot > -1) && (dot < (tmpFileName.length() - 1))) {
                ext = tmpFileName.substring(dot + 1);
            }
            // 其他文件格式不处理
            if ("png".equalsIgnoreCase(ext) || "jpeg".equalsIgnoreCase(ext) || "bmp".equalsIgnoreCase(ext) || "jpg".equalsIgnoreCase(ext) || "gif".equalsIgnoreCase(ext)) {
                // 重命名上传的文件名
                String targetFileName=renameFileName(tmpFileName);
                // 保存的新文件
                File target = new File(targetDirectory, targetFileName);
                try {
                    // 保存文件
                    FileUtils.copyInputStreamToFile(tmpFile.getInputStream(), target);
                    returnMsg=targetFileName;//文件上传成功,返回重命名的图片名
                } catch (IOException e) {
                    e.printStackTrace();
                    returnMsg="0";//文件上传失败
                }
            }else{
            	returnMsg="-1";//图片格式错误
            }
		}
		return returnMsg;
	}

	/**
	 *
	 * <B>方法名称：</B>文件重命名<BR>
	 * <B>概要说明：</B><BR>
	 *
	 * @author Tom
	 * @since 2017年9月13日
	 * @param fileName
	 * @return
	 */
	public static String renameFileName(String fileName) {
        String formatDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()); // 当前时间字符串
        String extension = fileName.substring(fileName.lastIndexOf(".")); // 文件后缀

        return fileName.substring(0,fileName.lastIndexOf("."))+"-"+formatDate + extension;
    }

	/**
	 *
	 * <B>方法名称：</B>文件上传<BR>
	 * <B>概要说明：</B><BR>
	 *
	 * @author perry
	 * @since 2017年11月6日
	 * @param dirStr
	 * @param tmpFile
	 * @param request
	 * @return
	 */
	public static String uploadFile(String dirStr, MultipartFile tmpFile,HttpServletRequest request){
        String returnMsg="";
        if(null!=tmpFile){
            //获取物理路径
            //String targetDirectory=request.getSession().getServletContext().getRealPath("/"+dirStr);
        	String targetDirectory=dirStr;
            File fileDir=new File(targetDirectory);
            if(fileDir.exists()==false){
                fileDir.mkdir();
            }
            String tmpFileName=tmpFile.getOriginalFilename();//上传文件名
            int dot=tmpFileName.lastIndexOf(".");
			String ext = "";// 后缀名
            if ((dot > -1) && (dot < (tmpFileName.length() - 1))) {
                ext = tmpFileName.substring(dot + 1);
            }
            // 重命名上传的文件名
            String targetFileName=renameFileName(tmpFileName);
            // 保存的新文件
            File target = new File(targetDirectory, targetFileName);
            try {
                // 保存文件
                FileUtils.copyInputStreamToFile(tmpFile.getInputStream(), target);
                returnMsg=targetFileName;//文件上传成功,返回重命名的文件名
            } catch (IOException e) {
                e.printStackTrace();
                returnMsg="0";//文件上传失败
            }
        }
        return returnMsg;
    }
}
