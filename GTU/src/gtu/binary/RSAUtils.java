package gtu.binary;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fubon.api.common.exception.ApiException;
import com.fubon.api.common.exception.ErrorCode;

import net.oauth.signature.pem.PKCS1EncodedKeySpec;

/**
 * @author gtu001
 *09:47 徐緯家 PKCS1EncodedKeySpec 是產生KEY 所用的空間
09:48 張純毓 夭壽
09:48 徐緯家 java 預設的是 PKCS8
09:48 張純毓 連觀念都建立了
java.security.spec.PKCS8EncodedKeySpec
09:49 徐緯家 預設 產生KEY的　格式　吧
09:49 張純毓 喔喔
09:50 徐緯家 當然　ｊａｖａ還有別的格式
唯獨 沒有 PKCS1EncodedKeySpec 所以 要引用別的 JAR來使用他
09:51 徐緯家 一般練習 用JAVA 預設的就好了吧
09:52 徐緯家 我這邊要用PKCS1EncodedKeySpec 完全是因為 界接C# 他用的是 PKCS1

 */
public class RSAUtils {

	private static final Logger log = LoggerFactory.getLogger(RSAUtils.class);
	private static final Charset _encoding = StandardCharsets.UTF_8;

	/**
	 * 私鑰解密
	 * 
	 * @param cipherText
	 * @param privateKeyContent
	 * @return
	 * @throws ApiException
	 */
	public static String decrypt(String cipherText, String privateKeyContent) throws ApiException {
		try {

			byte[] decText = Base64.decodeBase64(cipherText);
			byte[] decKeyContent = Base64.decodeBase64(privateKeyContent);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(decKeyContent);
			PrivateKey privKey = kf.generatePrivate(keySpecPKCS8);

			return new String(decrypt(decText, privKey), _encoding);

		} catch (Exception e) {
			log.error("rsa decrypt error", e);
			throw new ApiException(ErrorCode.CRYPT_ERROR, "rsa decrypt error");
		}
	}
	
	
	/**
	 * 私鑰解密
	 * 
	 * @param cipherText
	 * @param privateKeyContent
	 * @return
	 * @throws ApiException
	 */
	public static String decryptbypkcs1(String cipherText, String privateKeyContent) throws ApiException {
		try {

			byte[] decText = Base64.decodeBase64(cipherText);
			byte[] decKeyContent = Base64.decodeBase64(privateKeyContent);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			PKCS1EncodedKeySpec keySpecPKCS1 = new PKCS1EncodedKeySpec(decKeyContent);
			PrivateKey privKey = kf.generatePrivate(keySpecPKCS1.getKeySpec());

			return new String(decrypt(decText, privKey), _encoding);

		} catch (Exception e) {
			log.error("rsa decrypt error", e);
			throw new ApiException(ErrorCode.CRYPT_ERROR, "rsa decrypt error");
		}
	}
	
	private static byte[] decrypt(byte[] content, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decrypted = cipher.doFinal(content);
		return decrypted;
	}

	/**
	 * 公鑰加密
	 * 
	 * @param plainText
	 * @param publicKeyContent
	 *            X509格式Base64編碼
	 * @return
	 * @throws ApiException
	 */
	public static String encrypt(String plainText, String publicKeyContent) throws ApiException {
		String result = null;
		try {
			byte[] dec = Base64.decodeBase64(publicKeyContent);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(dec);
			RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);
			byte[] encrypt = encrypt(plainText.getBytes(_encoding), pubKey);
			result = Base64.encodeBase64String(encrypt);
		} catch (Exception e) {
			log.error("rsa encrypt error", e);
			throw new ApiException(ErrorCode.CRYPT_ERROR, "rsa encrypt error");
		}
		return result;
	}
	
	
	
	/**
	 * 公鑰加密
	 * 
	 * @param plainText
	 * @param publicKeyContent
	 *         PKCS1EncodedKeySpec 格式Base64編碼
	 * @return
	 * @throws ApiException
	 */
	public static String encryptbypkcs1(String plainText, String publicKeyContent) throws ApiException {
		String result = null;
		try {
			byte[] dec = Base64.decodeBase64(publicKeyContent);
			org.bouncycastle.asn1.pkcs.RSAPublicKey key = org.bouncycastle.asn1.pkcs.RSAPublicKey.getInstance(dec);
			RSAPublicKeySpec spec = new RSAPublicKeySpec(key.getModulus(), key.getPublicExponent());
			KeyFactory kf = KeyFactory.getInstance("RSA");
			RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(spec);
			byte[] encrypt = encrypt(plainText.getBytes(_encoding), pubKey);
			result = Base64.encodeBase64String(encrypt);
		} catch (Exception e) {
			log.error("rsa encrypt error", e);
			throw new ApiException(ErrorCode.CRYPT_ERROR, "rsa encrypt error");
		}
		return result;
	}
	private static byte[] encrypt(byte[] content, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encrypt = cipher.doFinal(content);
		return encrypt;
	}

	/**
	 * 私鑰 簽章 使用 PKCS1EncodedKeySpec
	 * 
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String sign(String plainText, String privateKey) throws Exception {
		Signature signature = null;
		try {
			byte[] keyBytes = Base64.decodeBase64(privateKey);
			PKCS1EncodedKeySpec pkcs1KeySpec = new PKCS1EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyFactory.generatePrivate(pkcs1KeySpec.getKeySpec());
			signature = Signature.getInstance("SHA1withRSA");
			signature.initSign(priKey);
			signature.update(plainText.getBytes("UTF-8"));
		} catch (Exception e) {
			log.error("rsa sign error", e);
			throw new ApiException(ErrorCode.CRYPT_ERROR, "rsa sign error");
		}
		return Base64.encodeBase64String(signature.sign());
	}

	/**
	 * 私鑰 簽章 使用 PKCS1EncodedKeySpec
	 * 
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String sign(byte[] data, String privateKey) throws Exception {
		Signature signature = null;
		try {
			byte[] keyBytes = Base64.decodeBase64(privateKey);
			PKCS1EncodedKeySpec pkcs1KeySpec = new PKCS1EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyFactory.generatePrivate(pkcs1KeySpec.getKeySpec());
			signature = Signature.getInstance("SHA1withRSA");
			signature.initSign(priKey);
			signature.update(data);
		} catch (Exception e) {
			log.error("rsa sign error", e);
			throw new ApiException(ErrorCode.CRYPT_ERROR, "rsa sign error");
		}
		return Base64.encodeBase64String(signature.sign());
	}

	/**
	 * 驗證簽名
	 * 
	 * @param content
	 *            簽名的內容 
	 * @param sign
	 *            簽名
	 * @param publicKeY
	 *            公鑰
	 * @return
	 * @throws Exception
	 */
	public static boolean validateSign(String content, String sign, String publicKey) throws Exception {
		try {
			byte[] dec = Base64.decodeBase64(publicKey);
			org.bouncycastle.asn1.pkcs.RSAPublicKey key = org.bouncycastle.asn1.pkcs.RSAPublicKey.getInstance(dec);
			RSAPublicKeySpec spec = new RSAPublicKeySpec(key.getModulus(), key.getPublicExponent());
			KeyFactory kf = KeyFactory.getInstance("RSA");
			RSAPublicKey publickey = (RSAPublicKey) kf.generatePublic(spec);
			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initVerify(publickey);
			signature.update(content.getBytes());
			return signature.verify(Base64.decodeBase64(sign));
		} catch (Exception e) {
			log.error("rsa validateSign error", e);
			throw new ApiException(ErrorCode.CRYPT_ERROR, "rsa validateSign error");
		}
	}

	private static KeyPair genRSAKeyPair(int bitLen) {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(bitLen);

			KeyPair keyPair = keyPairGenerator.generateKeyPair();

			return keyPair;
		} catch (Exception e) {
			log.error("genRSAKeyPair error", e);
		}

		return null;
	}

	private static Map<String, String> generateRSA() throws IOException {
		KeyPair keyPair = genRSAKeyPair(2048);
		Map<String, String> map = new HashMap<String, String>();
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
		String privateKeyEncode = Base64.encodeBase64String(privateKey.getEncoded());
		String publicKeyEncode = Base64.encodeBase64String(publicKey.getEncoded());
		log.info("publicKey:{}", publicKeyEncode);
		log.info("privateKey:{}", privateKeyEncode);
		map.put("publicKey", publicKeyEncode);
		map.put("privateKey", privateKeyEncode);
		return map;
	}
	
	
	private static Map<String, String> generateRSAbypkcs1Key() throws IOException {
		KeyPair keyPair = genRSAKeyPair(2048);
		Map<String, String> map = new HashMap<String, String>();
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
		PrivateKeyInfo pkInfo = PrivateKeyInfo.getInstance(privateKey.getEncoded());
		ASN1Encodable encodable = pkInfo.parsePrivateKey();
		ASN1Primitive primitive = encodable.toASN1Primitive();
		byte[] privateKeyPKCS1 = primitive.getEncoded();
		SubjectPublicKeyInfo spkInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
		ASN1Primitive pbimitive = spkInfo.parsePublicKey();
		byte[] publicKeyPKCS1 = pbimitive.getEncoded();
		String privateKeyEncode = Base64.encodeBase64String(privateKeyPKCS1);
		String publicKeyEncode = Base64.encodeBase64String(publicKeyPKCS1);
		log.info("publicKey:{}", publicKeyEncode);
		log.info("privateKey:{}", privateKeyEncode);
		map.put("publicKey", publicKeyEncode);
		map.put("privateKey", privateKeyEncode);
		return map;
	}
	
	public static void main(String[] args) {
		try {
			//Map<String, String> keyMap = generateRSAKey(); PKCS8EncodedKeySpec
			Map<String, String> keyMap = generateRSAbypkcs1Key();
			String publicKey = keyMap.get("publicKey");
			String privateKey = keyMap.get("privateKey");
			String plainText = "abcd";
			String encryptStr = encryptbypkcs1(plainText, publicKey);
			log.info("加密結果:{}", encryptStr);
			log.info("解密結果:{}", decryptbypkcs1(encryptStr, privateKey));
			String sing = sign(plainText, privateKey);
			log.info("簽章結果:{}", sing);
			log.info("驗章結果:{}", validateSign(plainText, sing, publicKey));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
