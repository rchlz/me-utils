/**
 * Project Name:snsxiu-moss
 * File Name:MD5Util.java
 * Package Name:com.snsxiu.moss.util
 * Date:2017年3月3日下午5:20:31
 * Copyright (c) 2017, gww0426@163.com All Rights Reserved.
 *
*/

package cn.sinapp.meutils.util;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName: MD5Util
 * @author Brook Xu
 * @version 1.0
 */
public class MD5Util {
 
    public static String encode(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] e = md.digest(value.getBytes("UTF-8"));
            return toHexString(e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return value;
        } catch(UnsupportedEncodingException e1){
        	e1.printStackTrace();
        	return value;
        }
    }
    
    public static String encodeUpCase(String value){
    	String encodeStr = encode(value);
    	if(encodeStr !=null && encodeStr.length() > 0){
    		return encodeStr.toUpperCase();
    	}else{
    		return "";
    	}
    }
 
    public static String encode(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] e = md.digest(bytes);
            return toHexString(e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
 
    private static String toHexString(byte bytes[]) {
        StringBuilder hs = new StringBuilder();
        String stmp = "";
        for (int n = 0; n < bytes.length; n++) {
            stmp = Integer.toHexString(bytes[n] & 0xff);
            if (stmp.length() == 1)
                hs.append("0").append(stmp);
            else
                hs.append(stmp);
        }
 
        return hs.toString();
    }
    
    public static void main(String[] args){
    	String str = "ddfsdfd546v";
    	
    	System.out.println(MD5Util.encodeUpCase(str));

    }
}