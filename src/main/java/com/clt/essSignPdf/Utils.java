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
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import static com.clt.essSignPdf.myUtils.PathUtils.getProjectRootPath;
import static com.clt.essSignPdf.myUtils.PropertiesUtils.readProperties;
import static com.clt.essSignPdf.myUtils.PropertiesUtils.writeProperties;

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

    /**
     * 根据证书路径获取其中的公钥信息
     * @param certName 证书路径
     * @return 公钥信息
     */
    public static PublicKey getPublicKey(String certName) throws IOException, CertificateException {
        PublicKey publicKey = null;
        InputStream inStream = new FileInputStream(new File(certName));
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
     * 格式20171222121122	没有符号间隔
     * Delimiter：定界符
     * @return
     */
    public static String getCurrentTimeToMinuteNoDelimiter(){

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");

        return df.format(new Date());
    }

    /**
     * 校验文档完整性
     * -2 : 验证过程中出现异常
     * -1 : 文档正常
     * 0 :  所有签章验证通过，但签章后，文档被修改
     * 其他: 第 N 个章验证失败
     * */
    public static final int VerifyDocument(byte[] sFilePath) throws GeneralSecurityException {
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



    public static boolean verifySystem() throws IOException {
        //读取配置文件
        Properties prop = null;
        //获取当前项目根路径
        String path = getProjectRootPath(demo.class);
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
                System.out.println(prop.getProperty("unitName"));
                if (verifyServerAuth == null || "".equals(verifyServerAuth)) {
                    // 验证失败
                    System.out.println("验证失败");
                } else {
                    // 验证成功,将返回值写入查询出的sysVerify的验证结果字段中
                    prop.setProperty("verifyResult", verifyServerAuth);
//                    sysVerify.setVerifyResult(verifyServerAuth);
                    //更新
                    writeProperties(path+"b.properties",prop,false);
                }
            }

        }else{
            System.out.println("读取文件为空");
        }
        return true;
    }
}
