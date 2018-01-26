/**
 * Project Name:snsxiu-moss
 * File Name:HmacSHA1.java
 * Package Name:com.snsxiu.moss.util
 * Date:2017年2月13日下午6:11:01
 * Copyright (c) 2017, gww0426@163.com All Rights Reserved.
 *
*/

package cn.sinapp.meutils.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * ClassName:HmacSHA1 
 * Date:     2017年2月13日 下午6:11:01 
 * @author   guoweiwei gww0426@163.com
 * @since    JDK 1.7
 */
public class HmacSHA1 {
	
	private static final String MAC_NAME = "HmacSHA1";    
	private static final String ENCODING = "UTF-8";
	
	/**   
	* 生成签名数据,签名数据使用UTF-8编码
	* @param data 待加密的数据   
	* @param key  加密使用的key
	* @return 签名后的字符串
	* @throws Exception   抛出异常
	*/    
	public static String getSignature(String data,String key) throws Exception{  
		byte[] keyBytes = key.getBytes(ENCODING);  
		SecretKeySpec signingKey = new SecretKeySpec(keyBytes, MAC_NAME);     
		Mac mac = Mac.getInstance(MAC_NAME);     
		mac.init(signingKey);     
		byte[] rawHmac = mac.doFinal(data.getBytes(ENCODING));  
		StringBuilder sb=new StringBuilder();  
		for(byte b:rawHmac){  
			sb.append(byteToHexString(b));  
		}  
		return sb.toString();     
	}  

	private static String byteToHexString(byte ib){  
		char[] Digit={  
				'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'  
		};  
		char[] ob=new char[2];  
		ob[0]=Digit[(ib>>>4)& 0X0f];  
		ob[1]=Digit[ib & 0X0F];  
		String s=new String(ob);  
		return s;           
	}      

}

