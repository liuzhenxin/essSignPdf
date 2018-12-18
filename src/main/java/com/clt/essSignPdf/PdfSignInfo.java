package com.clt.essSignPdf;

import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;

public class PdfSignInfo {
    //签章时间
    private Date signDate;
    //摘要算法
    private String digestAlgorithm;
    //签章原因
    private String reason;
    //签章名称
    private String signatureName;
    //加密算法
    private String encryptionAlgorithm;
    //签章人名称
    private String signerName;
    //签章版本号
    private int revisionNumber;
    //签章包含证书二进制
    private X509Certificate certificate;
    //签章页码
    private int pageNum;
    //签章x坐标
    private float x;
    //签章Y坐标
    private float y;
    //签章宽度
    private float width;
    //签章高度
    private float heigth;

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    public String getDigestAlgorithm() {
        return digestAlgorithm;
    }

    public void setDigestAlgorithm(String digestAlgorithm) {
        this.digestAlgorithm = digestAlgorithm;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSignatureName() {
        return signatureName;
    }

    public void setSignatureName(String signatureName) {
        this.signatureName = signatureName;
    }

    public String getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    public void setEncryptionAlgorithm(String encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }

    public String getSignerName() {
        return signerName;
    }

    public void setSignerName(String signerName) {
        this.signerName = signerName;
    }

    public int getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(int revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    public X509Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(X509Certificate certificate) {
        this.certificate = certificate;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeigth() {
        return heigth;
    }

    public void setHeigth(float heigth) {
        this.heigth = heigth;
    }

    @Override
    public String toString() {
        return "PdfSignInfo{" +
                "signDate=" + signDate +
                ", digestAlgorithm='" + digestAlgorithm + '\'' +
                ", reason='" + reason + '\'' +
                ", signatureName='" + signatureName + '\'' +
                ", encryptionAlgorithm='" + encryptionAlgorithm + '\'' +
                ", signerName='" + signerName + '\'' +
                ", revisionNumber=" + revisionNumber +
                ", certificate=" + certificate +
                ", pageNum=" + pageNum +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", heigth=" + heigth +
                '}';
    }
}
