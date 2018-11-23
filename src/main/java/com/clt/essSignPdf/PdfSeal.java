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
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.UUID;

/**
 *  基本的盖章类，通过相关的参数对指定pdf文档进行盖章
 */
public class PdfSeal {

    public String add(String pdfPath, String picPath, float width, float heigth, int pageNum, float x,
                      float y, String pdxPath, String pwd)  {
        String uuid = UUID.randomUUID().toString();
        Security.addProvider(new org.bouncycastle.
                jce.provider.BouncyCastleProvider());
        InputStream inp = null;
        try {
            //初始化盖章的私钥和证书 和签名的图片信息
            Image pic = Image.getInstance(picPath);
            KeyStore ks = KeyStore.getInstance("PKCS12");
            FileInputStream in = new FileInputStream(pdxPath);
            ks.load(in, pwd.toCharArray());
            String alias = (String) ks.aliases().nextElement();
            PrivateKey pk = (PrivateKey) ks.getKey(alias, pwd.toCharArray());
            Certificate[] chain = ks.getCertificateChain(alias);
            //相当于temp文件
            File file = new File(pdfPath);
            File file2 = new File(pdfPath + "_");
            FileOutputStream os = new FileOutputStream(file2);
            PdfReader reader = new PdfReader(pdfPath);
            PdfStamper stamper = PdfStamper
                    .createSignature(reader, os, '\u0000', null, true);//注意此处的true 允许多次盖章，false则只能盖一个章。
            //设置签名的外观显示
            PdfSignatureAppearance appearance = stamper
                    .getSignatureAppearance();
            //规定签章的权限，包括三种，详见itext 5 api，这里是不允许任何形式的修改
            PdfSigLockDictionary dictionary = new PdfSigLockDictionary(PdfSigLockDictionary.LockPermissions.FORM_FILLING_AND_ANNOTATION);
            appearance.setFieldLockDict(dictionary);
            appearance.setImage(pic);
            //此处的fieldName 每个文档只能有一个，不能重名
            appearance.setVisibleSignature(
                    new Rectangle(x, y, x + width,
                            y + heigth), pageNum,
                    "ESSPDFServerSign" + uuid);//fileName 一个文档中不能有重名的filedname
            appearance.setLayer2Text("");//设置文字为空否则签章上将会有文字 影响外观
            ExternalSignature es = new PrivateKeySignature(pk,
                    "SHA-256", "BC");
            ExternalDigest digest = new BouncyCastleDigest();
            MakeSignature.signDetached(appearance, digest, es,
                    chain, null, null, null, 0, MakeSignature.CryptoStandard.CMS);
            os.close();
            stamper.close();
            reader.close();
            file.delete();
            file2.renameTo(file);
            in.close();
            return file.getAbsolutePath();
        } catch (IOException var25) {
            var25.printStackTrace();
            return null;
        } catch (GeneralSecurityException var26) {
            var26.printStackTrace();
            return null;
        } catch (DocumentException var27) {
            var27.printStackTrace();
            return null;
        }

    }

}
