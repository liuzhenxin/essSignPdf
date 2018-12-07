package com.clt.essSignPdf;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.security.CertificateInfo;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.lang.reflect.Field;
import java.security.Security;
import java.util.List;

public class verifyUtils {
    public static void setUp() throws Exception {
        BouncyCastleProvider bcp = new BouncyCastleProvider();
        //Security.addProvider(bcp);
        Security.insertProviderAt(bcp, 1);
    }
    public static void testVerifyTestMGomez() throws Exception {
        System.out.println("\n\nTestMGomez.pdf\n==============");
        setUp();
        try  {
//            InputStream resource = getClass().getResourceAsStream("TestMGomez.pdf")
            PdfReader reader = new PdfReader("E:\\test\\test1.pdf");
            AcroFields acroFields = reader.getAcroFields();

            List<String> names = acroFields.getSignatureNames();
            for (String name : names) {
                System.out.println("Signature name: " + name);
                System.out.println("Signature covers whole document: " + acroFields.signatureCoversWholeDocument(name));
                PdfPKCS7 pk = acroFields.verifySignature(name);
                System.out.println("Subject: " + CertificateInfo.getSubjectFields(pk.getSigningCertificate()));
                System.out.println("Document verifies: " + pk.verify());
            }
        } finally {

        }

        System.out.println();

        Field rsaDataField = PdfPKCS7.class.getDeclaredField("RSAdata");
        rsaDataField.setAccessible(true);

        try {
//            (InputStream resource = getClass().getResourceAsStream("TestMGomez.pdf"))
            PdfReader reader = new PdfReader("E:\\test\\test1.pdf");
            AcroFields acroFields = reader.getAcroFields();

            List<String> names = acroFields.getSignatureNames();
            for (String name : names) {
                System.out.println("Signature name: " + name);
                System.out.println("Signature covers whole document: " + acroFields.signatureCoversWholeDocument(name));
                PdfPKCS7 pk = acroFields.verifySignature(name);
                System.out.println("Subject: " + CertificateInfo.getSubjectFields(pk.getSigningCertificate()));

                Object rsaDataFieldContent = rsaDataField.get(pk);
                if (rsaDataFieldContent != null && ((byte[]) rsaDataFieldContent).length == 0) {
                    System.out.println("Found zero-length encapsulated content: ignoring");
                    rsaDataField.set(pk, null);
                }
                System.out.println("Document verifies: " + pk.verify());
            }
        }finally {

        }
    }
}
