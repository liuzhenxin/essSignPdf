package com.clt.essSignPdf;

import com.itextpdf.text.DocumentException;
import java.io.*;
import java.security.*;
import com.clt.essSignPdf.Utils.*;

import static com.clt.essSignPdf.Utils.sign;
import static com.clt.essSignPdf.myUtils.fileUtils.fileToByte;

public class demo2 {

    public String imgPath = "E:\\test\\test\\demo.gif";
    public String pdfPath = "E:\\test\\原文.pdf";
    public String signPdfPath = "E:\\test\\test\\原文Sign.pdf";
    public static float imgWidth = 100;
    public static float imgHeigth = 100;
    public static int pageNum = 1;
    public static float x = 200;
    public static float y = 400;
    private String pfxPath = "E:\\test\\test\\PDFtest.pfx";
    public static String pwd = "111111";


    public static void main(String[] args) throws DocumentException, GeneralSecurityException, IOException {

        byte[] pdfBytes = fileToByte(new File("E:\\test\\原文.pdf"));
        byte[] imgBytes = fileToByte(new File("E:\\test\\demo.gif"));

        File pfxFile = new File("E:\\test\\test\\PDFtest.pfx");

        OutputStream os = new FileOutputStream("E:\\test\\原文2018.pdf");

        sign(pdfBytes,imgBytes,imgWidth,imgHeigth,pageNum,x,y,pfxFile,pwd,os);

        os.close();

    }








}
