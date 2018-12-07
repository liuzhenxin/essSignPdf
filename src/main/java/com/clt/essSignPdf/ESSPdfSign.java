package com.clt.essSignPdf;

import com.itextpdf.text.DocumentException;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.clt.essSignPdf.ESSConstant.ESSRESULTTYPE_BYTE;
import static com.clt.essSignPdf.ESSConstant.ESSRESULTTYPE_FILE;
import static com.clt.essSignPdf.Utils.byteToFile;
import static com.clt.essSignPdf.Utils.fileToByte;
import static com.clt.essSignPdf.Utils.sign;
import static com.clt.essSignPdf.GetLocation.getLastKeyWord;

public class ESSPdfSign {
	private boolean isCheck;
	private File sealCert;
	private String sealCertPwd;
	private byte[] sealImage;
	private float sealX;
	private float sealY;
	private float imgWidth = 150;
	private float imgHeigth = 150;
	private int sealPage;
	private byte[] inputPdfFile;
	private byte[] outputPdfFile;

	/**
	 * 校验当前签章环境的合法性
	 * @return 当前签章环境是否合法
	 */
	public boolean checkValidate(){
		//如果系统时间大于指定时间则返回false,验证不通过
		isCheck = Utils.dateCompare(new Date());
		return isCheck;
	}
	/**
	 * 根据坐标数据设置签章位置
	 * @param x 签章位置x坐标
	 * @param y 签章位置y坐标
	 * @param pageNum 签章位置所在
	 * @return
	 */
	public boolean setSealLocation(float x,float y,int pageNum){
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
	public boolean setSealLocationByKeyword(String keyword){
		Location location  = null;
		List<Location> locations = null;
		if(inputPdfFile !=null){
			locations = getLastKeyWord(inputPdfFile,keyword.replace(" ", "").toLowerCase());
			if (locations != null && locations.size() > 0) {  //集合不为空
				location = locations.get(locations.size() - 1);
				sealX = location.getX();
				sealY = location.getY();
				sealPage = location.getPageNum();
				return true;
			}
		}
		return false;
	}
	/**
	 *通过文件名设置签章证书
	 *参数：文件名
	 **/
	public boolean setSealCert(String fileName,String certPwd) {
		if(checkValidate()){
			if("".equals(fileName)){
				return false;
			}else{
				sealCert = new File(fileName);
				sealCertPwd = certPwd;
				return true;
			}
		}else{
			return false;
		}
	}
	/**
	 *通过文件名设置签章证书
	 *参数：文件名
	 **/
	public boolean setSealCert(File file,String certPwd) {
		if(checkValidate()){
			if(file==null){
				return false;
			}else{
				sealCert = file;
				sealCertPwd = certPwd;
				return true;
			}
		}else{
			return false;
		}
	}
	/**
	 *通过二进制数组设置签章证书
	 *参数：二进制数组
	 **/
	public boolean setSealCert(byte[] file,String certPwd) throws IOException {
		if(checkValidate()){
			if(file!=null){
				sealCert = byteToFile(file);
				sealCertPwd = certPwd;
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}

	}
	/**
	 *通过文件名设置签章图片
	 *参数：文件名
	 **/
	public boolean setSealImage(String fileName,float width,float heigth) throws IOException {
		if("".equals(fileName)){
			return false;
		}else{
			sealImage = fileToByte(new File(fileName));
			imgWidth = width;
			imgHeigth = heigth;
			return true;
		}
	}
	/**
	 *通过二进制数组设置签章图片
	 *参数：二进制数组
	 **/
	public boolean setSealImage(File file,float width,float heigth) throws IOException {
		if(file!=null){
			sealImage = fileToByte(file);
			imgWidth = width;
			imgHeigth = heigth;
			return true;
		}else{
			return false;
		}
	}
	/**
	 *通过二进制数组设置签章图片
	 *参数：二进制数组
	 **/
	public boolean setSealImage(byte[] file,float width,float heigth) throws IOException {
		if(file!=null){
			sealImage = file;
			imgWidth = width;
			imgHeigth = heigth;
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 设置签章pdf文档
	 * @param fileName 文件路径
	 * @return
	 * @throws IOException
	 */
	public boolean setPdf(String fileName) throws IOException {
		if("".equals(fileName)){
			return false;
		}else{
			inputPdfFile = fileToByte(new File(fileName));
			return true;
		}
	}
	/**
	 * 设置签章pdf文档
	 * @param file 文件
	 * @return
	 * @throws IOException
	 */
	public boolean setPdf(File file) throws IOException {
		if(file==null){
			return false;
		}else{
			inputPdfFile = fileToByte(file);
			return true;
		}
	}
	/**
	 *设置待签pdf文档
	 * @param file pdf文件的二进制数组
	 * @return 是否成功
	 * @throws IOException io异常
	 */
	public boolean setPdf(byte[] file) throws IOException {
		if(file==null){
			return false;
		}else{
			inputPdfFile = file;
			return true;
		}
	}

	/**
	 * 对文档进行签名
	 * @return 是否成功
	 * @throws DocumentException itext文档异常
	 * @throws GeneralSecurityException 安全异常
	 * @throws IOException io异常
	 */
	public boolean signPdf() throws DocumentException, GeneralSecurityException, IOException {

		if(sealCert!=null||sealImage!=null||sealX!=0||sealY!=0||sealPage!=0||inputPdfFile!=null||isCheck||!"".equals(sealCertPwd)){
			outputPdfFile = sign(inputPdfFile,sealImage,imgWidth,imgHeigth,sealPage,sealX,sealY,sealCert,sealCertPwd);
			return true;
		}
		return false;
	}

	/**
	 * 输出签章后的文件
	 * @return 	签章文件的二进制数组
	 */
	public byte[] savePdfAsByte()  {
//		switch (type){
//			case ESSRESULTTYPE_BYTE:
//				return outputPdfFile;
//		}
//		return null;
		return outputPdfFile;
	}
}
