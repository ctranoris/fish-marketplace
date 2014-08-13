
package gr.upatras.ece.nam.baker.util;


import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.Md5Hash;


public class EncryptionUtil {

    //secret key
    private static final byte[] key = new byte[]{'d', '3', '2', 't', 'p', 'd', 'M', 'o', 'I', '8', 'x', 'z', 'a', 'P', 'o', 'd'};


    /**
     * return hash value of string
     *
     * @param str unhashed string
     * @return hash value of string
     */
    public static String hash(String str) {

		Hash hash = new Md5Hash(str);
		
        return hash.toBase64();
    }

    /**
     * return encrypt value
     *
     * @param val unencrypted string
     * @return encrypted string
     */
    public static String encrypt(String val) {

        String retVal = null;
       
        return retVal;
    }

    /**
     * return decrypted value of an encrypted
     *
     * @param val encrypted string
     * @return decrypted string
     */
    public static String decrypt(String val) {
        String retVal = null;
        
        return retVal;
    }


}
