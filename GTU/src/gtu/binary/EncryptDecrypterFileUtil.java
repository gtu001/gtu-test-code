package gtu.binary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;

public class EncryptDecrypterFileUtil {
    private static EncryptDecrypterFileUtil _INST = new EncryptDecrypterFileUtil();

    public static EncryptDecrypterFileUtil getInst() {
        return _INST;
    }

    public String encrypt(String str, SecretKey key) throws Exception {
        Cipher ecipher = Cipher.getInstance("DES/CBC/NoPadding");
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0 };
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        ecipher.init(1, key, ivSpec);

        byte[] data = str.getBytes("ASCII");
        byte[] joinedArray = null;
        if (data.length % 8 != 0) {
            int length = 8 - data.length % 8;
            byte[] spaces = new byte[length];
            for (int i = 0; i < spaces.length; i++) {
                spaces[i] = 32;
            }
            joinedArray = new byte[data.length + spaces.length];
            System.arraycopy(data, 0, joinedArray, 0, data.length);
            System.arraycopy(spaces, 0, joinedArray, data.length, spaces.length);

        } else {

            joinedArray = new byte[data.length];
            System.arraycopy(data, 0, joinedArray, 0, data.length);
        }

        byte[] enc = ecipher.doFinal(joinedArray);
        Base64 encoder = new Base64();
        return new String(encoder.encode(enc), StandardCharsets.UTF_8);
    }

    public String decrypt(String encrypted, SecretKey key) throws Exception {
        Cipher dcipher = Cipher.getInstance("DES/CBC/NoPadding");
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0 };
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        dcipher.init(2, key, ivSpec);

        Base64 decoder = new Base64();
        byte[] dec = decoder.decode(encrypted);

        byte[] utf8 = dcipher.doFinal(dec);

        return new String(utf8, "ASCII");
    }

    public String getDecryptContent(File encryptFile, String mapKey) throws Exception {
        EncryptBean encrypt = construct(encryptFile);
        Map secretMap = encrypt.getSecretMap();
        System.out.println("systemName========> " + encryptFile);
        System.out.println("connKey========> " + mapKey);
        System.out.println("secretMap========> " + secretMap);
        String resultDecrypt = decrypt((String) secretMap.get(mapKey), encrypt.getKey());
        System.out.println("resultDecrypt========> " + resultDecrypt);
        System.out.println("------------------------------------------------------------------------------");
        return resultDecrypt;
    }

    private SecretKeySpec getSecretKeySpec(String strValue) {
        System.out.println("getSecretKeySpec : " + strValue);
        Base64 decoder = new Base64();
        byte[] dec = decoder.decode(strValue);
        SecretKeySpec sks = new SecretKeySpec(dec, "DES");
        return sks;
    }

    private SecretKey getSecretKeySpec() {
        SecretKey key = null;
        try {
            key = KeyGenerator.getInstance("DES").generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return key;
    }

    public EncryptBean construct(File encryptFile) throws Exception {
        BufferedReader br = null;
        String line = null;
        EncryptBean encrypt = new EncryptBean();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(encryptFile)));
            String keySpecLine = br.readLine();
            encrypt.setKey(getSecretKeySpec(keySpecLine));

            Map secretMap = new HashMap();
            while ((line = br.readLine()) != null) {
                String[] splitStrs = line.split(",");
                secretMap.put(splitStrs[0], splitStrs[1]);
            }
            encrypt.setSecretMap(secretMap);
            System.out.println("construct : " + keySpecLine + " , " + secretMap);
            return encrypt;
        } catch (Exception e) {
            throw e;
        } finally {

            if (br != null) {
                br.close();
            }
        }
    }

    public String displayEncryptInfo(File encryptFile) throws Exception {
        EncryptBean encrypt = construct(encryptFile);
        SecretKey secretKey = encrypt.getKey();

        Map amap = encrypt.getSecretMap();
        Iterator it = amap.keySet().iterator();

        StringBuffer sb = new StringBuffer(200);
        while (it.hasNext()) {
            String mapKey = (String) it.next();
            String encryptstr = decrypt((String) amap.get(mapKey), secretKey);
            sb.append(mapKey).append("\t").append(encryptstr).append("\n");
        }
        return sb.toString();
    }

    private String getSecretKeyString(SecretKey secretKey) throws EncoderException {
        Base64 encoder = new Base64();
        return new String((byte[]) encoder.encode(secretKey.getEncoded()), StandardCharsets.UTF_8);
    }

    private void saveEncryptFile(EncryptBean encrypt, String filename) throws Exception {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(filename));
            bw.write(getSecretKeyString(encrypt.getKey()));
            bw.write("\n");
            Map secretMap = encrypt.getSecretMap();
            Iterator it = secretMap.keySet().iterator();

            while (it.hasNext()) {

                String connKey = (String) it.next();
                bw.write(connKey + ",");
                bw.write((String) secretMap.get(connKey));
                bw.write("\n");
            }
            bw.flush();
        } catch (Exception e) {
            throw e;
        } finally {

            if (bw != null) {
                bw.close();
            }
        }
    }

    public static class EncryptBean implements Serializable {
        private static final long serialVersionUID = 9048419172334850364L;
        SecretKey key;
        Map secretMap;

        public EncryptBean(SecretKey key, Map secretMap) throws Exception {
            this.key = key;
            this.secretMap = secretMap;
        }

        public EncryptBean() {
        }

        public SecretKey getKey() {
            return this.key;
        }

        public void setKey(SecretKey key) {
            this.key = key;
        }

        public Map getSecretMap() {
            return this.secretMap;
        }

        public void setSecretMap(Map secretMap) {
            this.secretMap = secretMap;
        }
    }

    public void createEncryptInfo(String mapKey, String content, File encryptFile) throws Exception {
        Map encryptMap = new HashMap();

        EncryptBean encrypt = new EncryptBean();
        SecretKey key = getSecretKeySpec();
        encrypt.setKey(key);
        encrypt.setSecretMap(encryptMap);

        String encryptStr = encrypt(content, encrypt.getKey());

        encryptMap.put(mapKey, encryptStr);
        encrypt.setSecretMap(encryptMap);

        saveEncryptFile(encrypt, encryptFile.getAbsolutePath());
    }

    public static void main(String[] argv) {
        try {
            File destFile = new File("C:/Users/E123474/Desktop/DEST_PASSWORD_FILE.txt");
            EncryptDecrypterFileUtil.getInst().createEncryptInfo("TEST_MAP_KEY", "AAAAAAAAAA/BBBBBBBBB", destFile);

            System.out
                .println("----------------------------------------------------------------------------------------");
            String resultStr = EncryptDecrypterFileUtil.getInst().displayEncryptInfo(destFile);
            System.out.println("resultStr : " + resultStr);

            System.out
                .println("----------------------------------------------------------------------------------------");
            String resultStr2 = EncryptDecrypterFileUtil.getInst().getDecryptContent(destFile, "TEST_MAP_KEY");
            System.out.println("resultStr2 : " + resultStr2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
