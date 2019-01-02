package com.clt.essSignPdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.security.*;
import com.multica.crypt.VerifyServerAuthority;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.clt.essSignPdf.myUtils.PathUtils.getProjectRootPath;
import static com.clt.essSignPdf.myUtils.PropertiesUtils.readProperties;
import static com.clt.essSignPdf.myUtils.PropertiesUtils.writeProperties;
import static com.clt.essSignPdf.myUtils.fileUtils.fileToByte;
import static com.clt.essSignPdf.myUtils.fileUtils.getFileList;
import static com.clt.essSignPdf.myUtils.uuidUtil.getUUID;

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


    /**
     * 对证书签章
     * @param pdf
     * @param img
     * @param imgWidth
     * @param imgHeigth
     * @param pageNum
     * @param x
     * @param y
     * @param pfx
     * @param pwd
     * @return
     * @throws IOException
     * @throws DocumentException
     * @throws GeneralSecurityException
     */
    public static void sign(byte[] pdf, byte[] img, float imgWidth, float imgHeigth, int pageNum, float x, float y,
                            File pfx, String pwd,OutputStream os)
            throws IOException, DocumentException, GeneralSecurityException {
        //添加BC支持
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        //初始化一个输入流
        InputStream inp = null;

        //初始化盖章的私钥和证书 和签名的图片信息
        //图片pic
        Image pic = Image.getInstance(img);
        //创建keyStore实例
        KeyStore ks = KeyStore.getInstance("PKCS12");
        //将pfx文件读取成输入流
        FileInputStream in = new FileInputStream(pfx);
        //keyStore加载证书
        ks.load(in, pwd.toCharArray());
        //获取证书别名
        String alias = (String) ks.aliases().nextElement();
        //获取pfx证书中的私钥数据
        PrivateKey pk = (PrivateKey) ks.getKey(alias, pwd.toCharArray());
        //获取证书链
        Certificate[] chain = ks.getCertificateChain(alias);

        //相当于temp文件
//        File file = new File("");

//        File signFile = new File("D:\\"+UUID.randomUUID().toString()+".pdf");

//        FileOutputStream os = new FileOutputStream(signFile);
        //读取输入的pdf文件
        PdfReader reader = new PdfReader(pdf);

        //注意此处的true 允许多次盖章，false则只能盖一个章。
        PdfStamper stamper = PdfStamper
                .createSignature(reader, os, '\u0000', null, true);

        //设置签名的外观显示
        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();

        //规定签章的权限，包括三种，详见itext 5 api，这里是不允许任何形式的修改
        //权限类型
        PdfSigLockDictionary dictionary = new PdfSigLockDictionary(PdfSigLockDictionary.LockPermissions.FORM_FILLING_AND_ANNOTATION);
        appearance.setFieldLockDict(dictionary);
        //设置图片
        appearance.setImage(pic);
        //设置签章的坐标和长宽和所在页码以及签章名称
        //签章名称，每个文档只能有一个，不能重名。
        appearance.setVisibleSignature(new Rectangle(x, y, x + imgWidth, y + imgHeigth), pageNum,
                "ESSPDFSign" + getUUID());
        //设置文字为空 否则签章上将会有文字 影响外观
        appearance.setLayer2Text("");

        ExternalSignature es = new PrivateKeySignature(pk, "SHA-256", "BC");

        ExternalDigest digest = new BouncyCastleDigest();

        MakeSignature.signDetached(appearance, digest, es,
                chain, null, null, null, 0, MakeSignature.CryptoStandard.CMS);

//        os.close();

        stamper.close();

        reader.close();

        in.close();

    }

    /**
     * 根据证书路径获取其中的公钥信息
     * @param cert 证书文件
     * @return 公钥信息
     */
    public static PublicKey getPublicKey(File cert) throws IOException, CertificateException {
        PublicKey publicKey = null;
        InputStream inStream = new FileInputStream(cert);
        //创建X509工厂类
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        //创建证书对象
        X509Certificate oCert = (X509Certificate)cf.generateCertificate(inStream);
        inStream.close();
        publicKey = oCert.getPublicKey();
        return publicKey;
    }

    /**
     * 获取当前系统时,精确分
     * 格式20171222121122 没有符号间隔
     * Delimiter：定界符
     * @return
     */
    public static String getCurrentTimeToMinuteNoDelimiter(){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        return df.format(new Date());
    }

    /**
     * 获取pdf文件中的数字签名信息
     * @param pdfBytes pdf文件
     * @return 数字签名信息列表
     */
    public static List<PdfSignInfo> getSignature(byte[] pdfBytes) throws IOException, GeneralSecurityException {
        //添加BC库支持
        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);

        List<PdfSignInfo> signInfoList = new ArrayList<>();

        PdfReader reader = new PdfReader(pdfBytes);
        AcroFields acroFields = reader.getAcroFields();
        List<String> signedNames = acroFields.getSignatureNames();
        for (String signedName : signedNames) {
            PdfPKCS7 pdfPKCS7 = acroFields.verifySignature(signedName , provider.getName());
            System.out.println("bc: " + provider.getName());
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
            //页码
            pdfSignInfo.setPageNum(fieldPositions.page);
            System.out.println("页码: " + fieldPositions.page);
            //横坐标
            pdfSignInfo.setX(fieldPositions.position.getLeft());
            System.out.println("横坐标: " + fieldPositions.position.getLeft());
            //纵坐标
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

            System.out.println("Signature name: " + signedName);
            System.out.println("Signature covers whole document: " + acroFields.signatureCoversWholeDocument(signedName));

            System.out.println("Subject: " + CertificateInfo.getSubjectFields(pdfPKCS7.getSigningCertificate()));
            System.out.println("Document verifies: " + pdfPKCS7.verify());
            System.out.println(pdfSignInfo);
        }
        return null;
    }

    /**
     * 校验文档指定签名的证书是否可信机构颁发的
     * @param pdfBytes pdf文件
     * @param signatureName 签章名称
     * @return 签章是否可信颁发机构颁发
     */
    public static boolean VerifySignature(byte[] pdfBytes,String signatureName)
            throws IOException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        //添加BC支持
        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);
        //先获取可信机构根证书文件夹
        String ISSUER_CERT_PATH = readProperties("config.properties","ISSUER_CERT_PATH");
        //获取可信机构公钥证书
        List<File> certList = getFileList(new File(ISSUER_CERT_PATH));
        //读取pdf文件
        PdfReader reader = new PdfReader(pdfBytes);
        //获取签章信息域
        AcroFields acroFields = reader.getAcroFields();
        //获取签章名称
        List<String> signedNames = acroFields.getSignatureNames();
        //取出需要校验的证书
        X509Certificate VerifyCert = null;
        //遍历签章
        for (String signedName : signedNames) {
            //如果当前signedName等于要验证的签章名称
            if(signatureName.equals(signedName)){
                //获取签章中的cer证书
                PdfPKCS7 pdfPKCS7 = acroFields.verifySignature(signedName , provider.getName());
                VerifyCert = pdfPKCS7.getSigningCertificate();
            }
        }
        //根据颁发者公钥对签章证书验证
        if(VerifyCert!=null &&certList!=null){
            for(File f :certList){
                try{
                    //调用verify()验证是否可信机构颁发，如果通过则返回，如果不通过则跑出异常，并继续循环
                    VerifyCert.verify(getPublicKey(f));
                    return true;
                } catch (SignatureException | CertificateException signatureException){
                   //抛出异常，继续循环
                }
            }
        }
        return false;
    }
    /**
     * 校验文档完整性
     * -2 : 验证过程中出现异常
     * -1 : 文档正常
     * 0 :  所有签章验证通过，但签章后，文档被修改
     * 其他: 第 N 个章验证失败
     * */
    public static int VerifyDocument(byte[] sFilePath) throws GeneralSecurityException {
        int iRet = -2;
        Security.addProvider(new BouncyCastleProvider());
        PdfReader reader = null;
        try {
            reader = new PdfReader(sFilePath);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return -2;
        }
        AcroFields af = reader.getAcroFields();
        ArrayList<String> names = af.getSignatureNames();
        int iTotalRevisions = af.getTotalRevisions();
        int iTotalSignature = names.size();
        if(iTotalSignature == 0 )
            return -1;
        for (int k = 0; k < names.size(); ++k) {
            String name = (String)names.get(k);
            PdfPKCS7 pk = af.verifySignature(name);
            try {
                if(pk.verify() == false){
                    iRet = k + 1;
                    break;
                }
            } catch (SignatureException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return -2;
            } catch (IllegalArgumentException e){
                e.printStackTrace();
                return -2;
            }
        }
        if(iRet == -2){
            if(iTotalRevisions == iTotalSignature )
                return -1;
            if(iTotalRevisions > iTotalSignature )
                return 0;
        }
        return iRet;
    }

    /**
     * 验证系统是否可用
     * @return
     * @throws IOException
     */
    public static boolean verifySystem() throws IOException {
        //读取配置文件
        Properties prop = null;
        //获取当前项目根路径
        String path = getProjectRootPath(Utils.class);
        try{
            //读取Properties 文件内容
            prop = readProperties(path+"ess.properties");
        } catch(Exception e){
            e.printStackTrace();
        }
        if (prop != null) {
            int verifyStatus = 0;
            //验证状态码
            verifyStatus = VerifyServerAuthority.GetServerAuthorityVerifyStatus(getCurrentTimeToMinuteNoDelimiter(),
                    prop.getProperty("verifyResult"));
            System.out.println("verifyStatus:"+verifyStatus);
            if (verifyStatus == 1 || verifyStatus == 3) {
                return false;
            } else if (verifyStatus == 2) {
                //验证成功
                int iSealMaxCount = 1;
                //校验
                String verifyServerAuth = VerifyServerAuthority.VerifyServerAuth(prop.getProperty("unitName"),
                        prop.getProperty("serverIp"), iSealMaxCount, prop.getProperty("jurProductCode"),
                        prop.getProperty("dueTime"), prop.getProperty("signValue"));
//                System.out.println(prop.getProperty("unitName"));
                if (verifyServerAuth == null || "".equals(verifyServerAuth)) {
                    // 验证失败
                    System.out.println("验证失败");
                } else {
                    // 验证成功,将返回值写入查询出的sysVerify的验证结果字段中
                    prop.setProperty("verifyResult", verifyServerAuth);
//                    sysVerify.setVerifyResult(verifyServerAuth);
                    //更新
                    writeProperties(path+ "config.properties",prop,false);
                }
            }
        }else{
            System.out.println("读取文件为空");
        }
        return true;
    }
}
