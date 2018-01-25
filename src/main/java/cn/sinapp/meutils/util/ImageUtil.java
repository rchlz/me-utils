package cn.sinapp.meutils.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.sinapp.meutils.util.encryption.Base64Util;

public class ImageUtil {
	
	public static void main(String[] args)  
    {  
        String strImg = toBase64("d://test.jpg");  
        System.out.println(strImg);  
        fromBase64(strImg,"d://222.jpg");  
    }  
    //图片转化成base64字符串  
    public static String toBase64(String filePath)  
    {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理  
        InputStream in = null;  
        byte[] data = null;  
        //读取图片字节数组  
        try   
        {  
            in = new FileInputStream(filePath);          
            data = new byte[in.available()];  
            in.read(data);  
            in.close();  
        }   
        catch (IOException e)   
        {  
            e.printStackTrace();  
        }  
        //对字节数组Base64编码  
        return Base64Util.encode(data);//返回Base64编码过的字节数组字符串  
    }  
      
    //base64字符串转化成图片  
    public static boolean fromBase64(String base64Image, String savePath)  
    {   //对字节数组字符串进行Base64解码并生成图片  
        if (base64Image == null) //图像数据为空  
            return false;  
        
        try   
        {  
            //Base64解码  
            byte[] b = Base64Util.decode(base64Image); 
            for(int i=0;i<b.length;++i)  
            {  
                if(b[i]<0)  
                {//调整异常数据  
                    b[i]+=256;  
                }  
            }  
            //生成jpeg图片  
            //String imgFilePath = "d://222.jpg";//新生成的图片  
            OutputStream out = new FileOutputStream(savePath);      
            out.write(b);  
            out.flush();  
            out.close();  
            return true;  
        }   
        catch (Exception e)   
        {  
            return false;  
        }  
    }
    
    public static String toBase64(InputStream inputStream)  { 
        byte[] data = null;  
        //读取图片字节数组  
        try   
        {           
            data = new byte[inputStream.available()];  
            inputStream.read(data);  
            inputStream.close();  
        }   
        catch (IOException e)   
        {  
            e.printStackTrace();  
        }  
        //对字节数组Base64编码  
        return Base64Util.encode(data);//返回Base64编码过的字节数组字符串  
    }
 

}
