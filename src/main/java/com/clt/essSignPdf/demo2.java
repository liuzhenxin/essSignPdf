package com.clt.essSignPdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.security.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.UUID;

public class demo2 {

    public String imgPath = "E:\\test\\test\\demo.gif";
    public String pdfPath = "E:\\test\\test2.pdf";
    public String signPdfPath = "E:\\test\\test\\test2sign.pdf";
    public float imgWidth = 100;
    public float imgHeigth = 100;
    public int pageNum = 1;
    public float x = 200;
    public float y = 400;
    private String pfxPath = "E:\\test\\test\\PDFtest.pfx";
    public String pwd = "111111";


    public static void main(String[] args) throws DocumentException, GeneralSecurityException, IOException {
        demo2 demo2 = new demo2();
        demo2.sign();

//        int a = verifyPdf("E:\\test\\test2sign.pdf");
//        System.out.println(a);

    }

    public void sign( ) throws IOException, DocumentException, GeneralSecurityException {

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        InputStream inp = null;

        //初始化盖章的私钥和证书 和签名的图片信息
        Image pic = Image.getInstance(fileToByte(imgPath));

        KeyStore ks = KeyStore.getInstance("PKCS12");

        FileInputStream in = new FileInputStream(new File(pfxPath));

        ks.load(in, pwd.toCharArray());

        String alias = (String) ks.aliases().nextElement();

        PrivateKey pk = (PrivateKey) ks.getKey(alias, pwd.toCharArray());

        Certificate[] chain = ks.getCertificateChain(alias);

        //相当于temp文件
//        File file = new File("");

        File signFile = new File(signPdfPath);

        FileOutputStream os = new FileOutputStream(signFile);

        PdfReader reader = new PdfReader(fileToByte(pdfPath));

        //注意此处的true 允许多次盖章，false则只能盖一个章。
        PdfStamper stamper = PdfStamper
                .createSignature(reader, os, '\u0000', null, true);

        //设置签名的外观显示
        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();

        //规定签章的权限，包括三种，详见itext 5 api，这里是不允许任何形式的修改
        PdfSigLockDictionary dictionary = new PdfSigLockDictionary(PdfSigLockDictionary.LockPermissions.FORM_FILLING_AND_ANNOTATION);
        appearance.setFieldLockDict(dictionary);
        appearance.setImage(pic);
        //此处的fieldName 每个文档只能有一个，不能重名
        appearance.setVisibleSignature(new Rectangle(x, y, x + imgWidth, y + imgHeigth), pageNum,
                "ESSPDFSign" + UUID.randomUUID().toString());
        //设置文字为空 否则签章上将会有文字 影响外观
        appearance.setLayer2Text("");

        ExternalSignature es = new PrivateKeySignature(pk, "SHA-256", "BC");

        ExternalDigest digest = new BouncyCastleDigest();

        MakeSignature.signDetached(appearance, digest, es,
                chain, null, null, null, 0, MakeSignature.CryptoStandard.CMS);

        os.close();

        stamper.close();

        reader.close();

        in.close();

//        byte[] result = fileToByte(signFile);
//
//        signFile.delete();

//        return result;
    }

    public byte[] fileToByte(String fileName) throws IOException {

        File file = new File(fileName);
        //将文件读成输入流
        InputStream input = new FileInputStream(file);
        //建立适合长度的字符节数组
        byte[] bytes = new byte[input.available()];
        //将流中数据写入字符节数组
        input.read(bytes);

        return bytes;
    }







}
