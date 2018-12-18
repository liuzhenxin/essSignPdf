package com.clt.essSignPdf;

import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.security.CertificateInfo;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import static com.clt.essSignPdf.Utils.getPublicKey;

public class ESSPdfValidate {

    public static boolean VerifySignature(byte[] bytes){



        return true;
    }
    public static boolean VerifyDocument(byte[] bytes){
        //添加BC库支持
        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);
        try {
            PdfReader reader = new PdfReader(bytes);
            AcroFields acroFields = reader.getAcroFields();
            List<String> signedNames = acroFields.getSignatureNames();

            for (String signedName : signedNames) {
                PdfPKCS7 pdfPKCS7 = acroFields.verifySignature(signedName , provider.getName());

                System.out.println("Signature name: " + signedName);
                System.out.println("Signature covers whole document: " + acroFields.signatureCoversWholeDocument(signedName));
                System.out.println("Subject: " + CertificateInfo.getSubjectFields(pdfPKCS7.getSigningCertificate()));
                System.out.println("Document verifies: " + pdfPKCS7.verify());
            }


        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return true;
    }

    public static boolean getSignature(){

        return true;
    }

    /**
     * 解析返回签名信息
     * @return 签名
     */
    public static List<PdfSignInfo> getPdfSignInfo(byte[] input){
        //添加BC库支持
        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);
        List<PdfSignInfo> signInfoList = new ArrayList<>();
        try {
            PdfReader reader = new PdfReader(input);
            AcroFields acroFields = reader.getAcroFields();
            List<String> signedNames = acroFields.getSignatureNames();

            for (String signedName : signedNames) {
                PdfPKCS7 pdfPKCS7 = acroFields.verifySignature(signedName , provider.getName());
                System.out.println("bc: " + provider.getName());
                PdfSignInfo pdfSignInfo = new PdfSignInfo();
                //签章名称
                pdfSignInfo.setSignatureName(signedName);
                System.out.println("签章名称: " + signedName);
                //修订版本号
                pdfSignInfo.setRevisionNumber(acroFields.getRevision(signedName));
                System.out.println("修订版本号: " + acroFields.getRevision(signedName));
                //签章时间
                pdfSignInfo.setSignDate(pdfPKCS7.getSignDate().getTime());
                System.out.println("签章时间: " + pdfPKCS7.getSignDate().getTime());

                pdfSignInfo.setDigestAlgorithm(pdfPKCS7.getDigestAlgorithm());
                System.out.println("摘要算法: " + pdfPKCS7.getDigestAlgorithm());

                AcroFields.FieldPosition fieldPositions = acroFields.getFieldPositions(signedName).get(0);
                pdfSignInfo.setPageNum(fieldPositions.page);
                System.out.println("页码: " + fieldPositions.page);
                pdfSignInfo.setX(fieldPositions.position.getLeft());
                System.out.println("横坐标: " + fieldPositions.position.getLeft());
                pdfSignInfo.setY(fieldPositions.position.getBottom());
                System.out.println("纵坐标: " + fieldPositions.position.getBottom());
                pdfSignInfo.setWidth(fieldPositions.position.getWidth());
                System.out.println("宽: " + fieldPositions.position.getWidth());
                pdfSignInfo.setHeigth(fieldPositions.position.getHeight());
                System.out.println("高: " + fieldPositions.position.getHeight());

                pdfSignInfo.setReason(pdfPKCS7.getReason());
                System.out.println("签章原因: " + pdfPKCS7.getReason());

                pdfSignInfo.setEncryptionAlgorithm(pdfPKCS7.getEncryptionAlgorithm());
                System.out.println("加密算法: " + pdfPKCS7.getEncryptionAlgorithm());


                X509Certificate signCert = pdfPKCS7.getSigningCertificate();
                pdfSignInfo.setSignerName(CertificateInfo.getSubjectFields(signCert).getField("CN"));
                System.out.println("签章人名称: " + CertificateInfo.getSubjectFields(signCert).getField("CN"));
                PdfDictionary sigDict = acroFields.getSignatureDictionary(signedName);

                System.out.println("Signature name: " + signedName);
                System.out.println("Signature covers whole document: " + acroFields.signatureCoversWholeDocument(signedName));
                try{
                    signCert.verify(getPublicKey("E:\\test\\CLTRootcert.cer"));
                    System.out.println("verifyPublicKey : " + "true");
                }catch (SignatureException signatureException){
                    System.out.println("verifyPublicKey : " + "false");
                }
                System.out.println("Subject: " + CertificateInfo.getSubjectFields(pdfPKCS7.getSigningCertificate()));
                System.out.println("Document verifies: " + pdfPKCS7.verify());
                System.out.println(pdfSignInfo);
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return signInfoList;

    }

    public static void main(String[] args) throws IOException {
        File file = new File("E:\\test\\test1.pdf");

        InputStream input = new FileInputStream(file);
        byte[] byt = new byte[input.available()];
        input.read(byt);

        getPdfSignInfo(byt);
    }
}
