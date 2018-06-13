/**
 * 
 */
/**
 * @author Leon
 *
 */
package util;

import java.util.Arrays;

public class CheckUtil{
	private static final String token = "torroToken";
	public static boolean checkSignature(String signature,String timestamp,String nonce){
		
		String[] arr = new String[]{token,timestamp,nonce};
		//ÅÅÐò
		Arrays.sort(arr);
		//Éú³É×Ö·û´®
		StringBuffer content = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		//sha1¼ÓÃÜ
		String encode = SHA1.encode(content.toString());
		return encode.equals(signature);
		
		
		
	}
	
	
	
}