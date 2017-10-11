package yxy.pra0914.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dasu on 2017/4/7.
 */

public class FileUtils {

    private static final String EXTERNAL_DIRECTORY_NAME = "gank";   //App保存在SD卡里根目录的文件名

    /**
     * 判断外部存储是否可用
     *
     * @return true: 可用
     */
    public static boolean isSDcardAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     * 获取外部存储的根目录路径
     *
     * @return
     * @throws IOException
     */
    public static String getSDcardPath() throws IOException {
        if (isSDcardAvailable()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            throw new FileNotFoundException("没有外部存储");
        }
    }

    /**
     * 获取程序的外部存储的数据存放根目录
     *
     * @return
     */
    public static String getAppRootDirectoryPath() {
        if (isSDcardAvailable()) {
            try {
                String path = getSDcardPath();
                File file = new File(path, EXTERNAL_DIRECTORY_NAME);
                if (!file.exists()) {
                    file.mkdirs();
                }
                return file.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取外部缓存的地址
     *
     * @return
     */
    public static String getExternalCacheDirectory() {
        if (isSDcardAvailable()) {
            String path = getAppRootDirectoryPath();
            File file = new File(path, "cache");
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        }
        return null;
    }

    /**
     * 获取程序外部存储下的download目录路径
     *
     * @return
     */
    public static String getAppDownloadDirectory() {
        if (isSDcardAvailable()) {
            String path = getAppRootDirectoryPath();
            File file = new File(path, "download");
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        }
        return null;
    }

    /**
     * 创建子目录
     *
     * @param parent
     * @param name
     * @return
     */
    public static boolean createDirectory(String parent, String name) {
        File file = new File(parent, name);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }

    /**
     * 复制文件，默认删除原文件
     *
     * @param sourceFile
     * @param destFile
     */
    public static void copyFile(File sourceFile, File destFile) {
        copyFile(sourceFile, destFile, true);
    }

    /**
     * 复制文件
     *
     * @param sourceFile
     * @param destFile
     * @param isDeleteSource
     */
    public static void copyFile(File sourceFile, File destFile, boolean isDeleteSource) {
        try {
            int byteRead = 0;
            if (sourceFile.exists()) {
                FileInputStream is = new FileInputStream(sourceFile);
                FileOutputStream os = new FileOutputStream(destFile);
                byte[] buffer = new byte[1024];
                while ((byteRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, byteRead);
                }
                if (isDeleteSource) {
                    sourceFile.delete();
                }
                is.close();
                os.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件或文件夹
     *
     * @param file
     * @throws IOException
     */
    public static void deleteFile(File file) throws IOException {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                deleteFile(f);
            }
        } else {
            file.delete();
        }
    }

    /**
     * 压缩图片（质量压缩）
     * @param bitmap
     */
    public static File compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            long length = baos.toByteArray().length;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String filename = format.format(date);
        File file = new File(Environment.getExternalStorageDirectory(),filename+".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                TLog.error("onError " + e.toString());
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            TLog.error("onError " + e.toString());
            e.printStackTrace();
        }
        recycleBitmap(bitmap);
        return file;
    }

    public static void recycleBitmap(Bitmap... bitmaps) {
        if (bitmaps==null) {
            return;
        }
        for (Bitmap bm : bitmaps) {
            if (null != bm && !bm.isRecycled()) {
                bm.recycle();
            }
        }
    }

}
