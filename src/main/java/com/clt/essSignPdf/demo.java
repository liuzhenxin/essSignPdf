package com.clt.essSignPdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.clt.essSignPdf.Utils.VerifySignature;
import static com.clt.essSignPdf.Utils.getSignature;
import static com.clt.essSignPdf.Utils.verifySystem;
import static com.clt.essSignPdf.myUtils.PathUtils.getProjectRootPath;
import static com.clt.essSignPdf.myUtils.PropertiesUtils.*;
import static com.clt.essSignPdf.myUtils.fileUtils.fileToByte;

public class demo {


    public static void main(String[] args) throws Exception {

        Arrays.asList( "a", "b", "d" ).forEach( e -> System.out.println( e ) );

//        verifySystem();

//        List<PdfSignInfo> pdfSignInfoList = null;
//
//        pdfSignInfoList = getSignature(fileToByte(new File("E:\\test\\test1.pdf")));
//
//
//        if (pdfSignInfoList != null) {
//            for(PdfSignInfo p:pdfSignInfoList){
//                System.out.println(p );
//            }
//        }
//        System.out.println(pdfSignInfoList);

        //验证签名代码
//        String sealName = "ESSPDFSigne6c90b9f-ab59-4966-a0f7-d20543d425e0";
//        boolean a = VerifySignature(fileToByte(new File("E:\\test\\test1.pdf")),sealName);
//        System.out.println(a);
//        String pa = getProjectRootPath(demo.class);
//        System.out.println(pa+"config.properties");





//        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//
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
//            Certificate c = pk.getSigningCertificate();
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

    public static void verify(){
        int num = 0;
        // 先根据独立单位id查询系统验证表
        SysVerify sysVerify = new SysVerify();

        // 登录成功之后调用系统验证的函数 --- 2018.09.17
        // 获取sysVerify表中的验证结果字段
        if(sysVerify == null || sysVerify.getVerifyResult() == null){
            num++;
        }

//        if(num == 0){
//            int verifyStatus = VerifyServerAuthority.GetServerAuthorityVerifyStatus(getCurrentTimeToMinuteNoDelimiter(), sysVerify.getVerifyResult());
//            if(verifyStatus == 1 || verifyStatus == 3){
//                System.out.println("签章服务"+"sealControll-toAddPersonPage-获取到的验证结果为1或3,参数:independentUnitId-");
//                //rel = R
//            }else if(verifyStatus == 2){
//
//                int iSealMaxCount = sysVerify.getUkSealJurSize() + sysVerify.getMobileSealJurSize() +
//                        sysVerify.getUkHwJurSize() + sysVerify.getMobileHwJurSize();
//
//                String verifyServerAuth = VerifyServerAuthority.VerifyServerAuth(sysVerify.getUnitName(),
//                        sysVerify.getServerIp(), iSealMaxCount, sysVerify.getJurProductCode(), sysVerify.getDueTime(),
//                        sysVerify.getSignValue());
//
//                if(verifyServerAuth == null || "".equals(verifyServerAuth)){
//                    // 验证失败
//                }else{
//                    // 验证成功,将返回值写入查询出的sysVerify的验证结果字段中
//
//                    sysVerify.setVerifyResult(verifyServerAuth);
//
//                    sysVerify.setIndependentId(independentUnitId);
//
//                    // 执行更新操作
//                    Integer size = sysVerifyService.update(sysVerify);
//
//
//
//                }
//
//            }
//
//        }

    }
}
