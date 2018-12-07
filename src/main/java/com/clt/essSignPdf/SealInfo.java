package com.clt.essSignPdf;

//签章的详细信息
public class SealInfo {
       public String company; //签章公司
       public String SealName;//签章名称
       public String SealFieldName;//签章区域名称
       public boolean coversWhole;//签章是否包含整个文档
       public boolean vertify;//签章验证电影是否有效
       public int  page;//签章所在页面
       public float  left;//横坐标
       public float  widht;//宽度
       public float  bottom;//纵坐标
       public float  height;//高度

    public SealInfo() {

    }

    public SealInfo(String company, String sealName, String sealFieldName, boolean coversWhole, boolean vertify, int page, float left, float widht, float bottom, float height) {
        this.company = company;
        SealName = sealName;
        SealFieldName = sealFieldName;
        this.coversWhole = coversWhole;
        this.vertify = vertify;
        this.page = page;
        this.left = left;
        this.widht = widht;
        this.bottom = bottom;
        this.height = height;
    }

    @Override
    public String toString() {
        return "SealInfo{" +
                "company='" + company + '\'' +
                ", SealName='" + SealName + '\'' +
                ", SealFieldName='" + SealFieldName + '\'' +
                ", coversWhole=" + coversWhole +
                ", vertify=" + vertify +
                ", page=" + page +
                ", left=" + left +
                ", widht=" + widht +
                ", bottom=" + bottom +
                ", height=" + height +
                '}';
    }
}
