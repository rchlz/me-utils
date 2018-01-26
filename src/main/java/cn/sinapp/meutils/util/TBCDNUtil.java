package cn.sinapp.meutils.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * CDN小工具，主要用来判断图片是否来自CDN，以及给图片加相关尺寸的后缀.<br>
 * 注意：这个工具并不是完全准确的（依赖于CDN的变化），但是覆盖了大多数情况，请根据情况慎用
 * @author tiegai.gww
 */
public class TBCDNUtil {
	
	private static final Log logger = LogFactory.getLog(TBCDNUtil.class);
	
	//湖畔CDN
	private static final String HPCDN = "img.hpcdn.net";
	
	//MMCDN
	private static final String MMCDN = "i.mmcdn.cn";
	
	/** 淘宝CDN，主要是
	 * ^img0[1-8]\.taobao\.com$ 
	 * ^img0[1-8]\.taobaocdn\.com$ 
	 * ^dsc0[1-8]\.taobao\.com$ 
	 * ^dsc0[1-8]\.taobaocdn\.com$
	 * ^dsc\.taobao\.com$  
	 * ^dsc\.taobaocdn\.com$ 
	 * ^tu\.taobaocdn\.com$ 
	*/
	private static final Pattern TBCDN_PATTERN = Pattern.compile("(img0[1-8]|dsc0[1-8]|dsc|tu|img)\\.(daily\\.)?(taobao|taobaocdn)\\.(com|net)");
	
	private static final int PIC_SUFFIX_WIDTH = 210;
	
	private static final Pattern TFSNAME_PATTERN = Pattern.compile("(?<=/)T" +	//TfsName小文件为T开头
			"(?:\\w|.|_){17}" +					//中间字符为17个字符，可能出现中英文字母，数字，小数点、下划线
			"[^/]*?" +							//tfs后缀，可能是任何内容，但是不可能出现“/”
			"(?=(?:_[0-9]*x[0-9]*\\.jpg)?$)");	//最后有尺寸结尾或没有
	/**
	 * 淘宝CDN图片后缀的正则
	 */
	private static final Pattern TBCDN_PIC_SUFFIX = Pattern.compile("_([0-9]*)x([0-9]*)\\.jpg$");

	/**
	 * 判断图片是否属于淘宝CDN，注意这里使用的是正则分析URL，不一定是完全准确的,具体规则参看实现代码
	 * @param picUrl 图片地址
	 * @return 淘宝CDN则返回true
	 */
	public static boolean isTBCDNPic(String picUrl){
		if (StringUtils.isBlank(picUrl))
			return false;
		
		Matcher matcher = TBCDN_PATTERN.matcher(picUrl);
		if (matcher.find()) {
			return true;
		} else if (picUrl.indexOf(HPCDN) > 0 || picUrl.indexOf(MMCDN) > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断图片是否属于淘宝CDN，注意这里使用的是正则分析URL，不一定是完全准确的,具体规则参看实现代码
	 * @param picUrl 图片地址
	 * @return tfs的文件名
	 */
	public static String getTfsName(String picUrl){
		if (StringUtils.isBlank(picUrl))
			return "";
		
		if (picUrl.startsWith("http://") && !isTBCDNPic(picUrl))
			return "";
		try {
			Matcher matcher = TFSNAME_PATTERN.matcher(picUrl);
			if (matcher.find()) {
				return matcher.group();
			}
		} catch (Exception e) {
			logger.error("Fetch tfsName from picUrl error!" + picUrl, e);
		}
		return "";
	}
	
	/**
	 * 给淘宝CDN的图片加上指定尺寸的后缀.如果已经是加了后缀的CDN图片,用_{sizeX}x{sizeY}.jpg重新覆盖之前的后缀.比如:
	 * <p>给${CDN}/ens/T15TqeXfXmXXb1upjX.jpg加上24的后缀,新的图片地址是${CDN}/ens/T15TqeXfXmXXb1upjX.jpg_24x24.jpg</p>
	 * <p>给${CDN}/ens/T15TqeXfXmXXb1upjX.jpg_24x24.jpg加上24的后缀,新的图片地址仍旧是${CDN}/ens/T15TqeXfXmXXb1upjX.jpg_24x24.jpg</p>
	 * <p>给${CDN}/ens/T15TqeXfXmXXb1upjX.jpg_24x24.jpg加上310的后缀,新的图片地址是${CDN}/ens/T15TqeXfXmXXb1upjX.jpg_310x310.jpg</p>
	 * @param picUrl 图片路径
	 * @param size 指定尺寸
	 * @return 返回指定尺寸后缀的图片.如果size=0，返回原picUrl
	 */
	public static String addPicSuffix(String picUrl,int size){
		if(size >= 0){
			return addPicSuffix(picUrl,size,size);
		}else{
			return picUrl;
		}
	}
	
	public static String fetchPicUrl(String picUrl) {
		if (picUrl == null || picUrl.length() == 0) {
			return "";
		}
		
		Matcher matcher = TBCDN_PIC_SUFFIX.matcher(picUrl);
		if(matcher.find())
			return matcher.replaceAll("");
		return picUrl;
	}
	
	public static String fetchPicSuffix(String picUrl) {
		if (picUrl == null || picUrl.length() == 0) {
			return "";
		}
		
		Matcher matcher = TBCDN_PIC_SUFFIX.matcher(picUrl);
		if(matcher.find()){
			int originX = str2Int(matcher.group(1));
			int originY = str2Int(matcher.group(2));
			int y = PIC_SUFFIX_WIDTH * originY / originX;
			return new StringBuilder().append("_").append("210")
				.append("x").append(y).append(".jpg").toString();
		}
		return "";
	}
	
	private static int str2Int(String str){
		if(StringUtils.isBlank(str)){
			return 0;
		}else{
			try{
				int result = Integer.valueOf(str);
				return result;
			}catch(Exception e){
				logger.error("get taobao cdn pic size error,", e);
				return 0;
			}
		}
	}
	
	/**
	 * 给淘宝CDN的图片加上指定尺寸的后缀.如果已经是加了后缀的CDN图片,用_{sizeX}x{sizeY}.jpg重新覆盖之前的后缀.比如:
	 * <p>给${CDN}/ens/T15TqeXfXmXXb1upjX.jpg加上210x1000的后缀,新的图片地址是${CDN}/ens/T15TqeXfXmXXb1upjX.jpg_210x1000.jpg</p>
	 * <p>给${CDN}/ens/T15TqeXfXmXXb1upjX.jpg_210x1000.jpg加上210x1000的后缀,新的图片地址仍旧是${CDN}/ens/T15TqeXfXmXXb1upjX.jpg_210x1000.jpg</p>
	 * <p>给${CDN}/ens/T15TqeXfXmXXb1upjX.jpg_24x24.jpg加上210x1000的后缀,新的图片地址是${CDN}/ens/T15TqeXfXmXXb1upjX.jpg_210x1000.jpg</p>
	 * @param picUrl 图片地址
	 * @param sizeX 图片的宽度
	 * @param sizeY 图片的高度
	 * @return 返回指定尺寸后缀的图片.如果sizeX==0或者sizeY==0，返回原picUrl
	 */
	public static String addPicSuffix(String picUrl,int sizeX, int sizeY){
		
		if(sizeX==0 || sizeY ==0){
			return picUrl;
		}
		
		Matcher matcher = TBCDN_PIC_SUFFIX.matcher(picUrl);
		if(!matcher.find()){
			StringBuilder url = new StringBuilder(picUrl);
			url.append("_").append(sizeX).append("x").append(sizeY).append(".jpg");
			return url.toString();	
		}else{
			//如果CDN图片picUrl之前就有后缀的，那用这个后缀覆盖之前的后缀
			StringBuilder urlSuffix = new StringBuilder("_").append(sizeX).append("x").append(sizeY).append(".jpg");
			return matcher.replaceAll(urlSuffix.toString());
			//return picUrl; //如果已经是_{sizeX}x{sizeY}.jpg后缀结尾的,就没必要再加这个后缀了
		}
		
	}
	
	/**
	 * 取出cdn图片的后缀
	 * @param picUrl 图片地址
	 * @return 不含图片后缀的url
	 */
	public static String removePicSuffix(String picUrl){
		if (StringUtils.isBlank(picUrl))
			return "";
		
		if (!isTBCDNPic(picUrl))
			return picUrl;
		
		try {
			Matcher matcher = TBCDN_PIC_SUFFIX.matcher(picUrl);
			if (matcher.find())
				return matcher.replaceAll("");
		} catch (Exception e) {
			logger.error("removePicSuffix error" + picUrl);
			return picUrl;
		}
		return picUrl;
	}

	/**
	 * @param args 参数
	 */
	public static void main(String[] args) {
		
//		boolean isTbCDNPic = CDNUtil.isTBCDNPic("http://img01.taobaocdn.com/tps/i3/T1dCCuXi4vXXXXXXXX-36-36.png");
//		System.out.println("expect true,actual is:" + isTbCDNPic);
//		
//		isTbCDNPic = CDNUtil.isTBCDNPic("http://img02.taobaocdn.com/tps/i2/T1Q9euXfXyXXXXXXXX-36-36.png");
//		System.out.println("expect true,actual is:" + isTbCDNPic);
//		
//		isTbCDNPic = CDNUtil.isTBCDNPic("http://img.hpcdn.net/ens/T15TqeXfXmXXb1upjX.jpg_24x24.jpg");
//		System.out.println("expect true,actual is:" + isTbCDNPic);
//		
//		isTbCDNPic = CDNUtil.isTBCDNPic("http://img02.taobaocdn.com/tps/i2/T1Q9euXfXyXXXXXXXX-36-36.png");
//		System.out.println("expect true,actual is:" + isTbCDNPic);
//		
//		isTbCDNPic = CDNUtil.isTBCDNPic("http://img02.taobaocdn.com/tps/i2/T1Q9euXfXyXXXXXXXX-36-36.png");
//		System.out.println("expect true,actual is:" + isTbCDNPic);
//		
//		isTbCDNPic = CDNUtil.isTBCDNPic("http://img03.taobaocdn.com/tps/i3/T1rOOCXo0gXXXXXXXX-310-310.png");
//		System.out.println("expect true,actual is:" + isTbCDNPic);
//		
//		isTbCDNPic = CDNUtil.isTBCDNPic("http://img.taobaocdn.com/bao/uploaded/i1/T1yxeAXdVrXXXkvsc__105540.jpg");
//		System.out.println("expect true,actual is:" + isTbCDNPic);
//		
//		System.out.println("haha:" + CDNUtil.addPicSuffix("http://img03.taobaocdn.com/tps/i3/T1rOOCXo0gXXXXXXXX-310-310.png_310x310.jpg", 210,1000));
//
//		System.out.println("haha:" + CDNUtil.addPicSuffix("http://img02.taobaocdn.com/tps/i2/T1Q9euXfXyXXXXXXXX-36-36.png_210x1000.jpg", 210,1000));
//		
//		System.out.println(CDNUtil.addPicSuffix("http://img02.taobaocdn.com/tps/i2/T1Q9euXfXyXXXXXXXX-36-36.png", 210,1000));
//		
//		System.out.println(CDNUtil.addPicSuffix("http://img02.taobaocdn.com/tps/i2/T1Q9euXfXyXXXXXXXX-36-36.png_24x24.jpg_sds.jpg", 25));
//		
//		System.out.println(CDNUtil.addPicSuffix("http://img02.taobaocdn.com/tps/i2/T1Q9euXfXyXXXXXXXX-36-36.png_24x24.jpg", 40));

		System.out.println(TBCDNUtil.getTfsName("http://img01.taobaocdn.com/tps/i3/T1dCCuXi4vXXXXXXXX-36-36.png"));
		System.out.println(TBCDNUtil.getTfsName("http://img02.taobaocdn.com/tps/i2/T1Q9euXfXyXXXXXXXX-36-36.png"));
		System.out.println(TBCDNUtil.getTfsName("http://img.hpcdn.net/ens/T15TqeXfXmXXb1upjX.jpg_24x24.jpg"));
		System.out.println(TBCDNUtil.getTfsName("http://img02.taobaocdn.com/tps/i2/T1Q9euXfXyXXXXXXXX-36-36.png_24x24.jpg"));
		System.out.println(TBCDNUtil.getTfsName("http://img02.taobaocdn.com/tps/i2/T1Q9euXfXyXXXXXXXX-36-36.png"));
		System.out.println(TBCDNUtil.getTfsName("http://img03.taobaocdn.com/tps/i3/T1rOOCXo0gXXXXXXXX-310-310.png"));
		System.out.println(TBCDNUtil.getTfsName("http://img.taobaocdn.com/bao/uploaded/i1/T1yxeAXdVrXXXkvsc__105540.jpg"));
		System.out.println(TBCDNUtil.getTfsName("http://img02.taobaocdn.com/tps/i2/T1Q9euXfXyXXXXXXXX-36-36.png_24x24.jpg"));
		
		System.out.println("-------------------------");
		
		System.out.println(TBCDNUtil.getTfsName("http://www.qq8877.com/uploads/i2/T1Q9euXfXyXXXXXXXX-36-36.png_24x24.jpg"));
		System.out.println(TBCDNUtil.getTfsName("http://img02.taobaocdn.com/i2/T1Q9euXfXyXXXXXXXX-36-36.png_24x24.jpg"));
		System.out.println(TBCDNUtil.getTfsName("http://img.taobaocdn.net/bao/uploaded/http://img02.daily.taobaocdn.net/bao/uploaded/i2/T1yGFcXmRmXXXDi3k3_050113.jpg"));
	}

}
