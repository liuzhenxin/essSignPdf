package essSignPdf;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ESSUtils {

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
            Date dt1 = df.parse("2018-10-30");//将字符串转换为date类型  
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
}
