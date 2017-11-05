import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.util.encoders.Base64;

public class BouncyCastle{


    public KeyPair generate (String publicKeyFilename, String privateFilename){

    	
    	KeyPair pair = null;
        try {

            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

            // Create the public and private keys
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");

            SecureRandom random = createFixedRandom();
            generator.initialize(1024, random);

            pair = generator.generateKeyPair();
            Key pubKey = pair.getPublic();
            Key privKey = pair.getPrivate();

            //System.out.println("publicKey : " + b64.encode(pubKey.getEncoded()));
            //System.out.println("privateKey : " + b64.encode(privKey.getEncoded()));

            //BufferedWriter out = new BufferedWriter(new FileWriter(publicKeyFilename));
            FileOutputStream out = new FileOutputStream("C:\\Users\\gholamw\\Desktop\\Keys\\PublicKeys\\gholamwessam.txt");
            out.write(Base64.encode(pubKey.getEncoded()));
            out.close();

            FileOutputStream out1 = new FileOutputStream("C:\\Users\\gholamw\\Desktop\\Keys\\PrivateKeys\\gholamwessam.txt");
            out1.write(Base64.encode(privKey.getEncoded()));
            out1.close();


        }
        catch (Exception e) {
            System.out.println(e);
        }
        
        return pair;
    }
    
    public String encrypt (String publicKeyFilename, String inputData){

        String encryptedData = null;
        try {

            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

          
            String key = readFileAsString("C:\\Users\\gholamw\\Desktop\\Keys\\PublicKeys\\gholamwessam.txt");
            AsymmetricKeyParameter publicKey = 
                (AsymmetricKeyParameter) PublicKeyFactory.createKey(Base64.decode(key));
            AsymmetricBlockCipher e = new RSAEngine();
            e = new org.bouncycastle.crypto.encodings.PKCS1Encoding(e);
            e.init(true, publicKey);

            byte[] messageBytes = inputData.getBytes();
            byte[] hexEncodedCipher = e.processBlock(messageBytes, 0, messageBytes.length);

            System.out.println(getHexString(hexEncodedCipher));
            encryptedData = getHexString(hexEncodedCipher);
   
        }
        catch (Exception e) {
            System.out.println(e);
        }
       
        return encryptedData;
    }
    
    public String decrypt (String privateKeyFilename, String encryptedData) {

        String outputData = null;
        try {

            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

            String key = readFileAsString("C:\\Users\\gholamw\\Desktop\\Keys\\PrivateKeys\\gholamwessam.txt");
            
            AsymmetricKeyParameter privateKey = 
                (AsymmetricKeyParameter) PrivateKeyFactory.createKey(Base64.decode(key));
            AsymmetricBlockCipher e = new RSAEngine();
            e = new org.bouncycastle.crypto.encodings.PKCS1Encoding(e);
            e.init(false, privateKey);

            byte[] messageBytes = hexStringToByteArray(encryptedData);
            System.out.println("MessageBytes Length is : " + " " + messageBytes.length);
            byte[] hexEncodedCipher = e.processBlock(messageBytes, 0, messageBytes.length-1);

            //System.out.println(new String(hexEncodedCipher));
            outputData = new String(hexEncodedCipher);

        }
        catch (Exception e) {
            System.out.println(e);
        }
       
        return outputData;
    }
    
    
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    public static String getHexString(byte[] b) throws Exception {
        String result = "";
        for (int i=0; i < b.length; i++) {
            result +=
                Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }
    
    private static String readFileAsString(String filePath)
    	    throws java.io.IOException{
    	        StringBuffer fileData = new StringBuffer(1000);
    	        BufferedReader reader = new BufferedReader(
    	                new FileReader(filePath));
    	        char[] buf = new char[1024];
    	        int numRead=0;
    	        while((numRead=reader.read(buf)) != -1){
    	            String readData = String.valueOf(buf, 0, numRead);
    	            fileData.append(readData);
    	            buf = new char[1024];
    	        }
    	        reader.close();
    	        System.out.println(fileData.toString());
    	        return fileData.toString();
    	    }


    public static SecureRandom createFixedRandom()
    {
        return new FixedRand();
    }

    private static class FixedRand extends SecureRandom {

        MessageDigest sha;
        byte[] state;

        FixedRand() {
            try
            {
                this.sha = MessageDigest.getInstance("SHA-1");
                this.state = sha.digest();
            }
            catch (NoSuchAlgorithmException e)
            {
                throw new RuntimeException("can't find SHA-1!");
            }
        }

        public void nextBytes(byte[] bytes){

            int    off = 0;

            sha.update(state);

            while (off < bytes.length)
            {                
                state = sha.digest();

                if (bytes.length - off > state.length)
                {
                    System.arraycopy(state, 0, bytes, off, state.length);
                }
                else
                {
                    System.arraycopy(state, 0, bytes, off, bytes.length - off);
                }

                off += state.length;

                sha.update(state);
            }
        }
    }

}