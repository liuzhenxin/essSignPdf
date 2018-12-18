package com.clt.essSignPdf.myUtils;

import java.io.*;

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


}
