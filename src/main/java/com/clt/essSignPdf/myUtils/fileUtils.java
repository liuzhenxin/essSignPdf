package com.clt.essSignPdf.myUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class fileUtils {

    /**
     * file byte[]转换
     * @param file 输入文件对象
     * @return 输出字符节数组
     * @throws IOException
     */
    public static byte[] fileToByte(File file) throws IOException {
        //将文件读成输入流
        InputStream input = new FileInputStream(file);
        //建立适合长度的字符节数组
        byte[] bytes = new byte[input.available()];
        //将流中数据写入字符节数组
        input.read(bytes);

        return bytes;
    }

    public static void byteToFile(byte[] bytes,OutputStream os) throws IOException {

        BufferedOutputStream bufferedOutput = new BufferedOutputStream(os);
        bufferedOutput.write(bytes);
    }

    public static InputStream byteToInputStream(byte[] bytes){
        InputStream inputStream = new ByteArrayInputStream(bytes);
        return inputStream;
    }

    public static byte[] inputStreamToByte(InputStream inputStream) throws IOException {
        //建立适合长度的字符节数组
        byte[] bytes = new byte[inputStream.available()];
        //将流中数据写入字符节数组
        inputStream.read(bytes);
        //返回数据
        return bytes;
    }

    /**
     * 获取指定路径下的所有文件列表
     * @param dirFile 要查找的目录
     * @return file 集合
     */
    public static List<File> getFileList(File dirFile) {
        List<File> listFile = new ArrayList<>();
        //如果不是目录文件，则直接返回
        if (dirFile.isDirectory()) {
            //获得文件夹下的文件列表，然后根据文件类型分别处理
            File[] files = dirFile.listFiles();
            if (null != files && files.length > 0) {
                //根据时间排序
                Arrays.sort(files, new Comparator<File>() {
                    public int compare(File f1, File f2) {
                        return (int) (f1.lastModified() - f2.lastModified());
                    }

                    public boolean equals(Object obj) {
                        return true;
                    }
                });
                for (File file : files) {
                    //如果不是目录，直接添加
                    if (!file.isDirectory()) {
                        listFile.add(file);
                    } else {
                        //对于目录文件，递归调用
                        listFile.addAll(getFileList(file));
                    }
                }
            }
        }else{
            return null;
        }
        return listFile;
    }


}
