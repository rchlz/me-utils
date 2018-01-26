package cn.sinapp.meutils.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author guoweiwei gww0426@163.com
 *
 */
public class ArrayUtil {
	
	private static Log logger = LogFactory.getLog(ArrayUtil.class);
	
	/**
	 * 将一个字符串转换为int数组,比如对"1,2,4,5,7" 转换为[1,2,4,5,7]
	 * @param str 需要转化的字符串，主要不包含[]
	 * @return 返回[1,2,4,5,7]形式的字符串
	 */
	public static int[] fromString(String str){
		
		if(StringUtil.isNotEmpty(str)){
			
			String[] strArray = str.split(",");
			
			int[] intArray = new int[strArray.length];
			
			int i = 0;
			
			for(String s : strArray){
				try{
					int value = Integer.valueOf(s.trim());
					intArray[i++] = value;
				}catch(NumberFormatException ex){
					logger.error("ids error,str is:" + str, ex);
				}
			}
			
			int[] intArray2 = new int[i];
			
			System.arraycopy(intArray, 0, intArray2, 0, i);
			
			return intArray2;
			
		}else{
			
			return ArrayUtils.EMPTY_INT_ARRAY;
			
		}
		
	}
	
	/**
	 * 将一个int类型的数组转换为字符串，转换后的字符串不包含[]
	 * @param intArray 整形数组
	 * @return 返回不包含[]的数组字符串
	 */
	public static String toString(int[] intArray){
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < intArray.length; i++){
			sb.append(intArray[i]);
			if(i < intArray.length - 1){
				sb.append(",");
			}
		}
		
		return sb.toString();
		
	}
	
	/**
	 * 将一个String类型的数组转换为字符串，转换后的字符串不包含[]
	 * @param strArray 字符串数组
	 * @return 返回不包含[]的数组字符串
	 */
	public static String toString(String[] strArray){
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < strArray.length; i++){
			sb.append(strArray[i]);
			if(i < strArray.length - 1){
				sb.append(",");
			}
		}
		
		return sb.toString();
		
	}
	
	public static void main(String[] args){
		
		int[] intArray = new int[]{1,2,5,7,8,11};
		
		String str = "1, 3, 4,5,6,10";
		
		String str2 = "1,4,7,8,12,32,45,";
		
		System.out.println(ArrayUtil.toString(intArray));
		
		System.out.println(ArrayUtil.fromString(str));
		
		System.out.println(ArrayUtil.fromString(str2));
		
	}

}
