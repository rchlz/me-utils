package cn.sinapp.meutils.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import cn.sinapp.meutils.util.encryption.Base64Util;

public class ImageUtil {
	
	public static void main(String[] args) throws IOException  
    {  
//        String strImg = toBase64("d://test.jpg");  
//        System.out.println(strImg);  
//        fromBase64(strImg,"d://222.jpg");  
        
        BufferedImage image = ImageIO.read(new File("d://test.jpg"));
        image = corner(image, 60);
        ImageIO.write(image, "png", new File("d://test1.png"));
        
    }  
	
	/**
	 * 将本地图片转换为Base64字符串
	 * @param filePath 本地文件路径
	 * @return 返回Base64字符串
	 */
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
      
    /**
     * 将base64编码的字符串转换为图片并保存在指定路径
     * @param base64Image base64编码的字符串
     * @param savePath 保存后的文件路径
     * @return 返回是否保存成功
     */
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
    
    /**
     * 将输入流转换为Base64编码的字符串
     * @param inputStream 输入流
     * @return 返回base64编码的字符串
     */
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
    
    /**
     * 将Image处理成圆形图像
     * @param image 原图像
     * @return 返回处理成圆形的图像BufferedImage
     */
    public static BufferedImage round(Image image) {
    	final int width = image.getWidth(null);
    	return corner(image, width);
    	
    }
 
    /**
     * 将Image处理成圆角图像
     * @param image 原图像
     * @param radius 圆角的半径
     * @return 返回处理成圆角的图像BufferedImage
     */
    public static BufferedImage corner(Image image, int radius) {
    	
    	final int width = image.getWidth(null);
		final int height = image.getHeight(null);
		BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = destImage.createGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  
        g.fillRoundRect(0, 0,radius, height, radius, radius);  
        g.setComposite(AlphaComposite.SrcIn); 

		// 绘制图案
		g.drawImage(image, 0, 0, width, height, null);
		
		return destImage;
    }

}
