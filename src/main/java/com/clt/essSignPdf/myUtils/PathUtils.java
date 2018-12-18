package com.clt.essSignPdf.myUtils;

import com.clt.essSignPdf.demo;

import java.io.File;
import java.io.IOException;

public class PathUtils {
    /**
     * 获取工程的根路径   D:\git\daotie\daotie\target\classes
     * @param ob 加载类
     * @return 根路径
     */
    public static String getProjectRootPath(Object ob){
        return ob.getClass().getResource("/").getPath();
    }

    /**
     * 获取当前类的所在工程路径; 如果不加“/”  获取当前类的加载目录  D:\git\daotie\daotie\target\classes\my
     * @param ob 加载类
     * @return 工程路径
     */
    public static String getClassRootPath(Object ob){
        return ob.getClass().getResource("").getPath();
    }

    /**
     * 获取项目路径 D:\git\daotie\daotie
     * @return 项目路径
     */
    public static String getProjectPath() throws IOException {
        File directory = new File("");// 参数为空
        return directory.getCanonicalPath();

        // 第三种：  file:/D:/git/daotie/daotie/target/classes/
        //URL xmlpath = this.getClass().getClassLoader().getResource("");
        //System.out.println(xmlpath);

        // 第四种： D:\git\daotie\daotie
        //System.out.println(System.getProperty("user.dir"));
        /*
         * 结果： C:\Documents and Settings\Administrator\workspace\projectName
         * 获取当前工程路径
         */
        // 第五种：  获取所有的类路径 包括jar包的路径
        //System.out.println(System.getProperty("java.class.path"));

    }


}
