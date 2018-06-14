package cn.sinapp.meutils.util.encryption;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类
 * @author Brook Xu
 * @version 1.0
 */
public class MD5Util {
 
	/**
	 * MD5加密
	 * @param value 待加密字符串
	 * @return 返回加密后的32位MD5字符串
	 */
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
    
    /**
	 * MD5加密（大写）
	 * @param value 待加密字符串
	 * @return 返回加密后的32位MD5字符串（大写）
	 */
    public static String encodeUpCase(String value){
    	String encodeStr = encode(value);
    	if(encodeStr !=null && encodeStr.length() > 0){
    		return encodeStr.toUpperCase();
    	}else{
    		return "";
    	}
    }
 
    /**
	 * MD5加密
	 * @param bytes 字节数组
	 * @return 返回加密后的32位MD5字符串
	 */
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
    
    /**
     * 带盐值的MD5加密
     * @param value 待加密字符串
     * @param salt 盐值
     * @return 返回加密后的32位MD5字符串
     */
    public static String encode(String value, String salt) {
    	return encode(value, salt, 1);
    }
    
    /**
     * 带盐值的MD5加密
     * @param value 待加密字符串
     * @param salt 盐值
     * @param iterations 迭代次数
     * @return 返回加密后的32位MD5字符串
     */
    public static String encode(String value, String salt, int iterations) {
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			
			if (salt != null) {
	        	md.reset();
	        	md.update(salt.getBytes("UTF-8"));
	        }
	        byte[] hashed = md.digest(value.getBytes("UTF-8"));
	        iterations = iterations - 1; //already hashed once above
	        //iterate remaining number:
	        for (int i = 0; i < iterations; i++) {
	            md.reset();
	            hashed = md.digest(hashed);
	        }
	        return toHexString(hashed);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        
        return "";
        
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