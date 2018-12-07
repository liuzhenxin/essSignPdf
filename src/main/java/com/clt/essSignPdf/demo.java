package com.clt.essSignPdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.security.CertificateInfo;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import com.multica.crypt.VerifyServerAuthority;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.multica.crypt.VerifyServerAuthority.VerifyServerAuth;

public class demo {

    public static void main(String[] args) throws Exception {

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());



//        PdfReader reader = new PdfReader("E:\\test\\test1.pdf");
//
//        AcroFields fields = reader.getAcroFields();
//
//        ArrayList<String> lists = fields.getSignatureNames();
//
//        SealInfo sealInfo = new SealInfo();
//
//        for (String s : lists) {
//
//            sealInfo.SealFieldName = s;
//
//            sealInfo.coversWhole = fields.signatureCoversWholeDocument(s);
//
//            PdfPKCS7 pk = fields.verifySignature(s, "BC");
//
//            sealInfo.company = (CertificateInfo.getSubjectFields(pk.getSigningCertificate())).getField("O");
//
//            sealInfo.SealName = (CertificateInfo.getSubjectFields(pk.getSigningCertificate())).getField("CN");
//            Field rsaDataField = PdfPKCS7.class.getDeclaredField("RSAdata");
//            rsaDataField.setAccessible(true);
//            //                Object rsaDataFieldContent = rsaDataField.get(pk);
//            sealInfo.vertify = pk.verify();
//            AcroFields.FieldPosition fieldPositions = fields.getFieldPositions(s).get(0);
//
//            sealInfo.page = fieldPositions.page;
//            sealInfo.left = fieldPositions.position.getLeft();
//            sealInfo.bottom = fieldPositions.position.getBottom();
//            sealInfo.widht = fieldPositions.position.getWidth();
//            sealInfo.height = fieldPositions.position.getHeight();
//        }
//
//        System.out.println(sealInfo.toString());
    }
    /**
     * 获取当前系统时,精确分
     * 格式20171222121122	没有符号间隔
     * Delimiter：定界符
     * @return
     */
    public static String getCurrentTimeToMinuteNoDelimiter(){

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");

        return df.format(new Date());
    }

    public static void verify(){

        // 先根据独立单位id查询系统验证表
        SysVerify sysVerify = new SysVerify();
        // 登录成功之后调用系统验证的函数 --- 2018.09.17
        // 获取sysVerify表中的验证结果字段
        if(sysVerify == null || sysVerify.getVerifyResult() == null){

        }
        //获取
        int verifyStatus = VerifyServerAuthority.GetServerAuthorityVerifyStatus(getCurrentTimeToMinuteNoDelimiter(),
                sysVerify.getVerifyResult());
        if(verifyStatus == 1 || verifyStatus == 3){

        }else if(verifyStatus == 2){

            int iSealMaxCount = sysVerify.getUkSealJurSize() + sysVerify.getMobileSealJurSize() + sysVerify.getUkHwJurSize() + sysVerify.getMobileHwJurSize();
            String verifyServerAuth = VerifyServerAuth(sysVerify.getUnitName(),
                    sysVerify.getServerIp(), iSealMaxCount, sysVerify.getJurProductCode(), sysVerify.getDueTime(), sysVerify.getSignValue());
            if(verifyServerAuth == null || "".equals(verifyServerAuth)){
                // 验证失败

            }else{
                // 验证成功,将返回值写入查询出的sysVerify的验证结果字段中
                sysVerify.setVerifyResult(verifyServerAuth);
                // 执行更新操作
            }
        }
    }


    /**
     * 验签
     * @throws Exception
     */



}
