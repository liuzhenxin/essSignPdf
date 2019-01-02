package com.clt.essSignPdf.myUtils;

import java.io.*;
import java.util.Properties;

import static com.clt.essSignPdf.myUtils.PathUtils.getProjectRootPath;

public class PropertiesUtils {

    public static Properties readProperties(String filePath) throws IOException {
        Properties prop = new Properties();
        //获取当前项目根路径
        String path = getProjectRootPath(PropertiesUtils.class);
        //读取Properties 文件内容
        InputStream in = null;
        in = new BufferedInputStream(new FileInputStream(path+filePath));
        prop.load(in);
        return prop;

    }
    public static String readProperties(String filePath,String key) throws IOException {
        Properties prop = readProperties(filePath);
        return prop.getProperty(key);
    }

    /**
     *
     * @param filePath 需要执行操作的文件
     * @param prop 属性
     * @param b 是否开启追加内容
     * @throws IOException
     */
    public static void writeProperties(String filePath,Properties prop,boolean b) throws IOException {
        //保存属性到b.properties文件
        FileOutputStream oFile = new FileOutputStream(filePath, b);//true表示追加打开
        prop.store(oFile, "");
        oFile.close();
    }
    public static void main(String[] args) {

    }
}




