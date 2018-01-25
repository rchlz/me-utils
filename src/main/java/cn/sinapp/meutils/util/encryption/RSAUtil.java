package cn.sinapp.meutils.util.encryption;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * RSA签名,加解密处理核心文件，注意：密钥长度1024
 * </p>
 */
public class RSAUtil {

	@SuppressWarnings("unused")
	private static Log LOGGER = LogFactory.getLog(RSAUtil.class);

	/*
	 * 签名算法
	 */
	public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
	/*
	 * 加密算法RSA
	 */
	public static final String KEY_ALGORITHM = "RSA";
	/*
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;
	/*
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;
	/*
	 * 获取公钥的key
	 */
	private static final String PUBLIC_KEY = "RSAPublicKey";
	/*
	 * 获取私钥的key
	 */
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	/**
	 * <p>
	 * 生成密钥对(公钥和私钥)
	 * </p>
	 *
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public static Map<String, Object> genKeyPair() throws Exception {
		KeyPairGenerator keyPairGen = null;
		;
		try {
			keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("generate key pair exception: " + e.getMessage());
		}
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;

	}

	/**
	 * 签名字符串
	 *
	 * @param text
	 *            需要签名的字符串
	 * @param privateKey
	 *            私钥(BASE64编码)
	 * @param charset
	 *            编码格式
	 * @return 签名结果(BASE64编码)
	 * @throws Exception
	 */
	public static String sign(String text, String privateKey, String charset) throws Exception {
		byte[] result = {};
		byte[] keyBytes = Base64Util.decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory;
		try {
			keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initSign(privateK);
			signature.update(getContentBytes(text, charset));
			result = signature.sign();
		} catch (Exception e) {
			throw new Exception("RSA sign exception!", e);
		}
		return Base64Util.encode(result);

	}

	/**
	 * 验签字符串
	 *
	 * @param text
	 *            需要签名的字符串
	 * @param sign
	 *            客户签名结果
	 * @param publicKey
	 *            公钥(BASE64编码)
	 * @param charset
	 *            编码格式
	 * @return 验签结果
	 * @throws Exception
	 */
	public static boolean verify(String text, String sign, String publicKey, String charset) throws Exception {
		byte[] keyBytes = Base64Util.decode(publicKey);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory;
		boolean b = false;
		try {
			keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			PublicKey publicK = keyFactory.generatePublic(keySpec);

			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initVerify(publicK);
			signature.update(getContentBytes(text, charset));
			b = signature.verify(Base64Util.decode(sign));
		} catch (Exception e) {
			throw new Exception("RSA verify exception! ", e);
		}
		return b;
	}

	/**
	 * <P>
	 * 私钥解密
	 * </p>
	 *
	 * @param encryptedData
	 *            已加密数据
	 * @param privateKey
	 *            私钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
		byte[] keyBytes = Base64Util.decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;

	}

	/**
	 * <p>
	 * 公钥解密
	 * </p>
	 *
	 * @param encryptedData
	 *            已加密数据
	 * @param publicKey
	 *            公钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
		byte[] keyBytes = Base64Util.decode(publicKey);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;

	}

	/**
	 * <p>
	 * 公钥加密
	 * </p>
	 *
	 * @param data
	 *            源数据
	 * @param publicKey
	 *            公钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
		byte[] keyBytes = Base64Util.decode(publicKey);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;

	}

	/**
	 * <p>
	 * 私钥加密
	 * </p>
	 *
	 * @param data
	 *            源数据
	 * @param privateKey
	 *            私钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
		byte[] keyBytes = Base64Util.decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;

	}

	/**
	 * @param content
	 * @param charset
	 * @return
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException
	 */
	private static byte[] getContentBytes(String content, String charset) {
		if (charset == null || "".equals(charset)) {
			return content.getBytes();
		}
		try {
			return content.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
		}
	}

	/**
	 * <p>
	 * 获取私钥
	 * </p>
	 *
	 * @param keyMap
	 *            密钥对
	 * @return
	 * @throws Exception
	 */
	public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return Base64Util.encode(key.getEncoded());
	}

	/**
	 * <p>
	 * 获取公钥
	 * </p>
	 *
	 * @param keyMap
	 *            密钥对
	 * @return
	 * @throws Exception
	 */
	public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return Base64Util.encode(key.getEncoded());
	}

	public static String encryptByPublicKey64(String data, String publicKey)
			throws UnsupportedEncodingException, Exception {
		String decode = Base64Util.encode(encryptByPublicKey(data.getBytes("utf-8"), publicKey));
		return decode;
	}

	public static String decryptByPrivateKey64(String data, String privateKey)
			throws UnsupportedEncodingException, Exception {
		byte[] decodeb = decryptByPrivateKey(Base64Util.decode(data), privateKey);
		return new String(decodeb, "utf-8");
	}

	private static final String PROPERTIES_PUBLIC_KEY = ".public.key=";
	private static final String PROPERTIES_PRIVATE_KEY = ".private.key=";

	/**
	 * 动态生成RSA密钥对
	 * 
	 * @param appId
	 * @throws Exception
	 * @author dongdong.zhang
	 * @date 2016年4月1日 下午7:36:37
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void rsaKeyGenerate(String appId) throws Exception {
		Map map = genKeyPair();

		System.out.println(appId + PROPERTIES_PUBLIC_KEY + RSAUtil.getPublicKey(map));
		System.out.println(appId + PROPERTIES_PRIVATE_KEY + RSAUtil.getPrivateKey(map));
	}

	public static void main(String[] args) throws Exception {
		//rsaKeyGenerate("ma");
		decryptByPrivateKey64("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");
	}
}