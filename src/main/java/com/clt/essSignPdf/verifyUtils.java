package com.clt.essSignPdf;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.security.CertificateInfo;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class verifyUtils {
    /**
     * 获取pdf中所有签章信息
     * @param input 输入流
     * @return  签章对象列表
     */
    public static List<PdfSignInfo> getSignature(byte[] input) throws  IOException {
        //添加BC库支持
        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);
        List<PdfSignInfo> signInfoList = new ArrayList<>();

        PdfReader reader = new PdfReader(input);
        AcroFields acroFields = reader.getAcroFields();
        List<String> signedNames = acroFields.getSignatureNames();
        for (String signedName : signedNames) {
            PdfPKCS7 pdfPKCS7 = acroFields.verifySignature(signedName , provider.getName());
            PdfSignInfo pdfSignInfo = new PdfSignInfo();
            //签章名称
            pdfSignInfo.setSignatureName(signedName);
            //修订版本号
            pdfSignInfo.setRevisionNumber(acroFields.getRevision(signedName));
            //签章时间
            pdfSignInfo.setSignDate(pdfPKCS7.getSignDate().getTime());
            //摘要算法
            pdfSignInfo.setDigestAlgorithm(pdfPKCS7.getDigestAlgorithm());
            AcroFields.FieldPosition fieldPositions = acroFields.getFieldPositions(signedName).get(0);
            pdfSignInfo.setPageNum(fieldPositions.page);
            pdfSignInfo.setX(fieldPositions.position.getLeft());
            pdfSignInfo.setY(fieldPositions.position.getBottom());
            pdfSignInfo.setWidth(fieldPositions.position.getWidth());
            pdfSignInfo.setHeigth(fieldPositions.position.getHeight());
            //签章原因
            pdfSignInfo.setReason(pdfPKCS7.getReason());
            //加密算法
            pdfSignInfo.setEncryptionAlgorithm(pdfPKCS7.getEncryptionAlgorithm());
            X509Certificate signCert = pdfPKCS7.getSigningCertificate();
            pdfSignInfo.setCertificate(signCert);
            //签章人名称
            pdfSignInfo.setSignerName(CertificateInfo.getSubjectFields(signCert).getField("CN"));
            PdfDictionary sigDict = acroFields.getSignatureDictionary(signedName);
            System.out.println(pdfSignInfo);
            signInfoList.add(pdfSignInfo);
        }
        return signInfoList;
    }

    /**
     * -2 : 验证过程中出现异常
     * -1 : 文档正常
     * 0 :  所有签章验证通过，但签章后，文档被修改
     * 其他: 第 N 个章验证失败
     * @param sFilePath
     * @return
     * @throws IOException
     */
    public static int VerifySignature(String sFilePath) throws IOException, GeneralSecurityException {
        int iRet = -2;
        Security.addProvider(new BouncyCastleProvider());
        PdfReader reader = new PdfReader(sFilePath);
        //获取签章信息块
        AcroFields af = reader.getAcroFields();

        ArrayList<String> names = af.getSignatureNames();
        //文档总修订版本号
        int iTotalRevisions = af.getTotalRevisions();
        //签章数量
        int iTotalSignature = names.size();
        //没有签章的情况
        if(iTotalSignature == 0 ){
            return -1;
        }
        //有签章的情况，遍历签章
        for (int k = 0; k < names.size(); ++k) {

            String name = names.get(k);
            PdfPKCS7 pk = af.verifySignature(name);
            //如果出现文档被修改的情况
            if(!pk.verify()){
                iRet = k + 1;
                break;
            }
        }
        //如果验证签章文档完成性没有错误
        if(iRet == -2){
            //文档未被修改即版本号和签章数量对应
            if(iTotalRevisions == iTotalSignature ){
                return -1;
            }
            //文档在签章完成后有修改
            if(iTotalRevisions > iTotalSignature ){
                return 0;
            }
        }
        return iRet;
    }
}
