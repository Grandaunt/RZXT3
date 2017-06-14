package com.sjs.dz.rzxt3.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/***
 * 拍照文件
 */

public class FileUtils {

	//SD卡路径
//	public static String SDPATH = Environment.getExternalStorageDirectory()
//			+ "/Photo_XSY/";

	//保存bitmap
	public static void saveBitmap(String bitmapstr, String PHTOTONAME, Bitmap bitmap) {

			try {
				//如果文件不存在则创建文件
				if (!isFileExist(bitmapstr)) {
					File tempf = createSDDir(bitmapstr);
				}

				File f = new File(bitmapstr, PHTOTONAME);
				Log.i("FileUtils", bitmapstr + PHTOTONAME );

				//如果同名照片存在则删除
				if (f.exists()) {
					f.delete();
				}
				FileOutputStream out = new FileOutputStream(f);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

	}
//保存字节流Bitmap,手势签名
	public static String saveByteBitmap(String bitmapstr, String PHTOTONAME, Bitmap bitmap) {
		ByteArrayOutputStream baos = null;
		try {
			//如果文件不存在则创建文件
			if (!isFileExist(bitmapstr)) {
				File tempf = createSDDir(bitmapstr);
				Log.i("FileUtils不存在创建", bitmapstr + PHTOTONAME);
			}
			else {
				Log.i("FileUtils存在不创建", bitmapstr + PHTOTONAME);
			}

			File f = new File(bitmapstr, PHTOTONAME);


			//如果同名照片存在则删除
			if (f.exists()) {
				f.delete();
			}


			baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			byte[] photoBytes = baos.toByteArray();
			if (photoBytes != null) {
				new FileOutputStream(new File(bitmapstr,PHTOTONAME)).write(photoBytes);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmapstr+PHTOTONAME;
	}
		//创建SD文件夹

	public static File createSDDir(String dirName) throws IOException {
		File dir = new File(dirName);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			dir.mkdirs();

//			System.out.println("createSDDir:" + dir.getAbsolutePath());
//			System.out.println("createSDDir:" + dir.mkdir());
		}
		return dir;
	}

	//判断文件是否存在
	public static boolean isFileExist(String fileName) {
		File file = new File(fileName);
		file.isFile();
		return file.exists();
	}

	//删除文件
	public static void delFile(File file) {

		if (file.isFile()) {
			file.delete();
		}
		if(file.isDirectory()){
			File[] childFile = file.listFiles();
			if (childFile == null|| childFile.length==0){
				file.delete();
			}
			for(File f: childFile){
				delFile(f);
			}
			file.delete();
		}
		file.exists();
	}

	/**
	 * 根据图片路径获取图片的压缩图
	 * @param filePath
	 * @return
	 */
	public static Bitmap getCompressBitmap(String filePath){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options); //此时返回bm为空
		if(bitmap == null){
		}
		//计算缩放比
		int simpleSize = (int)(options.outHeight / (float)200);
		if (simpleSize <= 0)
			simpleSize = 1;
		options.inSampleSize = simpleSize;
		options.inJustDecodeBounds = false;
		//重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
		bitmap= BitmapFactory.decodeFile(filePath,options);
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		System.out.println(w+"   "+h);
		return bitmap;
	}
}