package com.kingyee.common.util.encrypt;

import com.google.gson.JsonObject;
import com.kingyee.common.util.RandomUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * AES位加密算法
 * 加密模式：AES/CBC/PKCS5Padding
 *
 */
public class AesUtil {

	private static Charset CHARSET = Charset.forName("utf-8");

    /**
     * AES 256位加密
     * 加密方法：密钥为43的字符串，拼接"="后，进行base64 decode得出真正的密钥。
     * IV为密钥的前16个字节。对加密后字节进行base64 encode后输出。与decode方法配套使用
     * @param text 明文
     * @param key 密钥（43位）
     * @return
     * @throws Exception
     */
    public static String encode(String text, String key)
            throws Exception {

        if(key == null || key.length() != 43){
            throw new Exception("秘钥长度必须为43个字符。");
        }

        byte[] aeskey = Base64.decodeBase64(key + "=");

        // 设置加密模式为AES的CBC模式
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(aeskey, "AES");
        IvParameterSpec iv = new IvParameterSpec(aeskey, 0, 16);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

        // 加密
        byte[] encrypted = cipher.doFinal(text.getBytes(CHARSET));

        // 使用BASE64对加密后的字符串进行编码
        String base64Encrypted = Base64.encodeBase64String(encrypted);

        return base64Encrypted;
    }

    /**
     * AES 256位解密
     * 解密方法：密钥为43的字符串，拼接"="后，进行base64 decode得出真正的密钥。
     * IV为密钥的前16个字节。与encode方法配套使用
     * @param text 明文
     * @param key 密钥（43位）
     * @return
     * @throws Exception
     */
    public static String decode(String text, String key)
            throws Exception {

        if(key == null || key.length() != 43){
            throw new Exception("秘钥长度必须为43个字符。");
        }
        byte[] aeskey = Base64.decodeBase64(key + "=");

        // 设置解密模式为AES的CBC模式
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key_spec = new SecretKeySpec(aeskey, "AES");
        IvParameterSpec iv = new IvParameterSpec(aeskey, 0, 16);
        cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);

        // 使用BASE64对密文进行解码
        byte[] encrypted = Base64.decodeBase64(text);

        // 解密
        byte[] original = cipher.doFinal(encrypted);
        String decode = new String(original, CHARSET);
        return decode;
    }



    /**
     * AES 128位加密
     * 加密方法：密钥为16的字符串。IV为随机的16位字符串。
     * 对加密，将IV和密文拼接起来，再进行base64 encode后输出。与decode128方法配套使用
     * @param text 明文
     * @param key 密钥（16位）
     * @return
     * @throws Exception
     */
    public static String encode128(String text, String key)throws Exception{
        if(key == null || key.length() != 16){
            throw new Exception("秘钥长度必须为16个字符。");
        }

        byte[] aeskey = key.getBytes(CHARSET);
        byte[] ivArray = RandomUtil.generateString(16).getBytes(CHARSET);

        // 设置加密模式为AES的CBC模式
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(aeskey, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivArray, 0, 16);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

        // 加密
        byte[] encrypted = cipher.doFinal(text.getBytes(CHARSET));

        // 使用BASE64对加密后的字符串进行编码
        encrypted = ArrayUtils.addAll(iv.getIV(), encrypted);
        String base64Encrypted = Base64.encodeBase64String(encrypted);

        return base64Encrypted;
    }

    /**
     * AES 128位解密
     * 解密方法：密钥为16的字符串。IV为密文的前16位字符串，先将text的前16位作为IV，后面作为真正的密文。
     * 与encode128方法配套使用
     * @param text 明文
     * @param key 密钥（16位）
     * @return
     * @throws Exception
     */
    public static String decode128(String text, String key)throws Exception{

        if(key == null || key.length() != 16){
            throw new Exception("秘钥长度必须为16个字符。");
        }

        // 使用BASE64对密文进行解码
        byte[] encrypted = Base64.decodeBase64(text);

        byte[] aeskey = key.getBytes(CHARSET);
        byte[] ivArray = ArrayUtils.subarray(encrypted, 0, 16);
        encrypted = ArrayUtils.subarray(encrypted, 16, encrypted.length);

        // 设置解密模式为AES的CBC模式
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key_spec = new SecretKeySpec(aeskey, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivArray, 0, 16);
        cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);

        // 解密
        byte[] original = cipher.doFinal(encrypted);
        String decode = new String(original, CHARSET);
        return decode;
    }


    /**
     * 将加密后的字符串，进行urlencode后输出
     * @param text 明文
     * @param key 密钥（43位）
     * @return
     * @throws Exception
     */
    public static String urlEncoder(String text, String key)throws Exception{
        if(key.length() == 16){
            return URLEncoder.encode(AesUtil.encode128(text, key), "UTF-8");
        }else{
            return URLEncoder.encode(AesUtil.encode(text, key), "UTF-8");
        }
    }

    /**
     * urldecode后，在进行解密
     * @param text 明文
     * @param key 密钥（43位）
     * @return
     * @throws Exception
     */
    public static String urlDecoder(String text, String key)throws Exception{
        if(key.length() == 16){
            return AesUtil.decode128(URLDecoder.decode(text, "UTF-8"), key);
        }else{
            return AesUtil.decode(URLDecoder.decode(text, "UTF-8"), key);
        }
    }


	
	public static void main(String[] args) throws Exception {
//		String key = RandomUtil.generateString(43);
//		System.out.println("加密key=="+key);
//		String text = "8fhByJqbCdjJEA0UGFxqv4q8lZQ2OYP+2+yNgkyLHyM=";
		String text = "xmi6SRDCSbpKxU2BNGKS210eExZU7qeYhagmrOwMbx8%3D%0D%0A";
//		String text = "2015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/162015/04/16";
		String key = "xHIAiqiOshfEOY2EHt7KIZQuyqnocyFir9ZBJ7tDSJg";
		// System.out.println(RC4Util.encode4RandomKeyAndSuffix("ph@kingyee.com.cn"));
//		System.out.println("==" + AesUtil.urlEncoder(text, key));
//		System.out.println("=="+ AesUtil.urlDecoder(text, key));
//		System.out.println("=="+ AesUtil.decode(text, key));
//		System.out.println(URLDecoder.decode(text, "UTF-8"));

        JsonObject pram = new JsonObject();
        pram.addProperty("timestamp", System.currentTimeMillis()/1000);
        pram.addProperty("username", "nurse");
        String params = AesUtil.encode128(pram.toString(), "8eHuQRBtPyNssYkl");
        System.out.println(params);

//        System.out.println(AesUtil.decode128("TXpFazBXZzhWSU8wQVpDNH0KDag9ONxaYIIXwZPmYvQSQsdvybeyVeUkwP+kBLuC0Wa57RZOY9TwfCQFA7GdqA==","WfeBzrnBxmpibRjM"));
	}
}
