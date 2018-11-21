package essSignPdf;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class essSign {
	public static void main (String[] args){
		boolean s = ESSUtils.dateCompare(new Date());
		System.out.print(s);
	}
	
	private boolean isCheck;
	private File sealCert;
	private File sealImage;
	private float sealX;
	private float sealY;
	private double sealPage;
	private File inputPdfFile;
	private File outputPdfFile;
	
	/**
	*校验当前签章的合法性
	**/
	public void checkValidate(){
		//如果系统时间大于指定时间则返回false,验证不通过
		isCheck = ESSUtils.dateCompare(new Date());
	}
	/**
	*根据坐标数据设置签章位置
	*参数：x坐标，y坐标，页码
	**/
	public boolean setSealLocation(float x,float y,double pageNum){
		if(x>0 || y>0 || pageNum>0){
			sealX = x;
			sealY = y;
			sealPage = pageNum;
			return true;
		}else{
			return false;
		}
	}
	/**
	*根据关键字设置签章位置
	*参数：关键字，x偏移量，y偏移量
	**/
	public void setSealLocationByKeyword(String keyword,float offsetX,float offsetY){
		
		
	}
	

	 
}
