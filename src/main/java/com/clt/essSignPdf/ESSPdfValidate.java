package com.clt.essSignPdf;

import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.security.CertificateInfo;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import static com.clt.essSignPdf.Utils.VerifyDocument;
import static com.clt.essSignPdf.myUtils.fileUtils.fileToByte;


public class ESSPdfValidate {

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        File file = new File("E:\\test\\test1.pdf");
        byte[] fileToByte = fileToByte(file);
        int result = VerifyDocument(fileToByte);
        System.out.println(result);
    }



    /**
     * 解析返回签名信息
     * @return 签名
     */
    public static List<PdfSignInfo> getPdfSignInfo(byte[] input){
        List<PdfSignInfo> pdfSignInfoList = null;
//        pdfSignInfoList = pdfSignInfoList();
        return pdfSignInfoList;
    }




}
