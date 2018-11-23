package com.clt.essSignPdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSigLockDictionary;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.*;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Utils {

	/**
	 * 比较时间大小
	 * @param dt2
	 * @return
	 */
    public static boolean dateCompare(Date dt2) {  
        // TODO Auto-generated method stub  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");//创建日期转换对象HH:mm:ss为时分秒，年月日为yyyy-MM-dd  
        try {  
        	//限制时间
            Date dt1 = df.parse("2018-12-30");//将字符串转换为date类型
            if(dt1.getTime()>dt2.getTime())//比较时间大小,如果dt1大于dt2  
            {  
               return true;
            }  
            else  
            {  
            	return false;
            }  
        } catch (ParseException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }
		return false;  
    }

    public static byte[] fileToByte(File file) throws IOException {
        InputStream input = new FileInputStream(file);
        byte[] byt = new byte[input.available()];
        input.read(byt);
        return byt;
    }

    /**
     * 二进制数组转化file
     * @param bytes 二进制数组
     * @return
     * @throws IOException
     */
    public static File byteToFile(byte[] bytes) throws IOException {
        File file = new File("");
        OutputStream output  = new FileOutputStream(file);
        BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
        bufferedOutput.write(bytes);
        return file;
    }


    public static byte[] sign(byte[] pdf, byte[] img, float imgWidth, float imgHeigth, int pageNum, float x, float y,
                            File pfx, String pwd)
            throws IOException, DocumentException, GeneralSecurityException {

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        InputStream inp = null;

        //初始化盖章的私钥和证书 和签名的图片信息
        Image pic = Image.getInstance(img);

        KeyStore ks = KeyStore.getInstance("PKCS12");

        FileInputStream in = new FileInputStream(pfx);

        ks.load(in, pwd.toCharArray());

        String alias = (String) ks.aliases().nextElement();

        PrivateKey pk = (PrivateKey) ks.getKey(alias, pwd.toCharArray());

        Certificate[] chain = ks.getCertificateChain(alias);

        //相当于temp文件
//        File file = new File("");

        File signFile = new File("D:\\"+UUID.randomUUID().toString()+".pdf");

        FileOutputStream os = new FileOutputStream(signFile);

        PdfReader reader = new PdfReader(pdf);

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

        ExternalSignature es = new PrivateKeySignature(pk,
                "SHA-256", "BC");

        ExternalDigest digest = new BouncyCastleDigest();

        MakeSignature.signDetached(appearance, digest, es,
                chain, null, null, null, 0, MakeSignature.CryptoStandard.CMS);

        os.close();

        stamper.close();

        reader.close();

        in.close();

        byte[] result = fileToByte(signFile);

        signFile.delete();

        return result;
    }

}
