package com.clt.essSignPdf.myUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;


public class PropertiesUtils {

    public static Properties readProperties(String filePath) throws IOException {
        Properties prop = new Properties();
        InputStream in = null;
        //读取属性文件a.properties
//        if(env.equals("local")){
//            in = this.getClass().getResourceAsStream(proFilePath);
//        }else{
//            String filePath1 = System.getProperty("user.dir")+proFilePath;
//            in = new BufferedInputStream(new FileInputStream(filePath1));
//        }
        in = new BufferedInputStream(new FileInputStream(filePath));
        //加载属性列表
        prop.load(new InputStreamReader(in, "gbk"));
        in.close();
        return prop;
    }

    /**
     *
     * @param filePath 需要执行操作的文件
     * @param prop 属性
     * @param b 是否开启追加内容
     * @throws IOException
     */
    public static void writeProperties(String filePath,Properties prop,boolean b) throws IOException {
        ///保存属性到b.properties文件
        FileOutputStream oFile = new FileOutputStream(filePath, b);//true表示追加打开
        prop.store(oFile, "");
        oFile.close();
    }
    public static void main(String[] args) {

    }
}




