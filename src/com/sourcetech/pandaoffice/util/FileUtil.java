package com.sourcetech.pandaoffice.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import com.sourcetech.pandaoffice.detail.Res;
import com.sourcetech.pandaoffice.detail.Res.DicRes;
import com.sun.xml.internal.ws.Closeable;

public class FileUtil {

	public static List<String> getAllFileList(String dir) {
		List<String> listFile = new ArrayList<>();
		File dirFile = new File(dir);
		// 如果不是目录文件，则直接返回
		if (dirFile.isDirectory()) {
			// 获得文件夹下的文件列表，然后根据文件类型分别处理
			File[] files = dirFile.listFiles();
			if (null != files && files.length > 0) {
				// 根据时间排序
				Arrays.sort(files, new Comparator<File>() {
					public int compare(File f1, File f2) {
						return (int) (f1.lastModified() - f2.lastModified());
					}

					public boolean equals(Object obj) {
						return true;
					}
				});
				for (File file : files) {
					// 如果不是目录，直接添加
					if (!file.isDirectory()) {
						listFile.add(file.getAbsolutePath());
					} else {
						// 对于目录文件，递归调用
						listFile.addAll(getAllFileList(file.getAbsolutePath()));
					}
				}
			}
		}
		return listFile;
	}
	
	public static String getName(File file){
		String fullName = file.getName();
		int index = fullName.lastIndexOf('.');
		if(index==-1)
			return fullName;
		else{
			return fullName.substring(0, index);
		}
	}
	
	public static String getType(File file){
		String fullName = file.getName();
		int index = fullName.lastIndexOf('.');
		if(index==-1)
			return "";
		else{
			return fullName.substring(index+1, fullName.length());
		}
	}
	
	public static List<String> getFileList(String dir) {
		List<String> listFile = new ArrayList<>();
		File dirFile = new File(dir);
		// 如果不是目录文件，则直接返回
		if (dirFile.isDirectory()) {
			File[] files = dirFile.listFiles();
			if (null != files && files.length > 0) {
				// 根据时间排序
				Arrays.sort(files, new Comparator<File>() {
					public int compare(File f1, File f2) {
						return (int) (f1.lastModified() - f2.lastModified());
					}

					public boolean equals(Object obj) {
						return true;
					}
				});
				for (File file : files) {
					// 如果不是目录，直接添加
					if (!file.isDirectory()) {
						listFile.add(file.getAbsolutePath());
					}
				}
			}
		}
		return listFile;
	}

	public static void TransferCopy(File source, File target) {
		FileChannel in = null;
		FileChannel out = null;
		FileInputStream inStream = null;
		FileOutputStream outStream = null;
		try {
			inStream = new FileInputStream(source);
			outStream = new FileOutputStream(target);
			in = inStream.getChannel();
			out = outStream.getChannel();
			in.transferTo(0, in.size(), out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inStream.close();
				in.close();
				outStream.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean isImageFile(File f) {
		try {
			FileInputStream is = new FileInputStream(f);
			byte[] b = new byte[3];
			is.read(b, 0, b.length);
			switch (bytesToHexString(b)) {
			case "89504e":
				return true;
			case "ffd8ff":
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static String getFileType(File f){
		try {
			FileInputStream is = new FileInputStream(f);
			byte[] b = new byte[3];
			is.read(b, 0, b.length);
			switch (bytesToHexString(b)) {
			case "89504e":
				return "png";
			case "ffd8ff":
				return "jpg";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
	
	public static byte[] getContent(File file) throws IOException {
        long fileSize = file.length();  
        if (fileSize > Integer.MAX_VALUE) {
            return null;  
        }  
        FileInputStream fi = new FileInputStream(file);  
        byte[] buffer = new byte[(int) fileSize];  
        int offset = 0;  
        int numRead = 0;  
        while (offset < buffer.length  
        && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {  
            offset += numRead;  
        }  
        // 确保所有数据均被读取  
        if (offset != buffer.length) {  
        throw new IOException("Could not completely read file "  
                    + file.getName());  
        }  
        fi.close();  
        return buffer;  
    }  
	
	public static File byte2File(byte[] buf, com.sourcetech.pandaoffice.dao.File fileInfo, DicRes dicRes) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			file = new File(Res.Dic(dicRes) + "\\" + fileInfo.getName() + "." + fileInfo.getType());
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return file;
		}
	}
	
	public static File byte2File(byte[] buf, com.sourcetech.pandaoffice.dao.File fileInfo, String path) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			file = new File(path + "\\" + fileInfo.getName() + "." + fileInfo.getType());
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return file;
		}
	}


}
