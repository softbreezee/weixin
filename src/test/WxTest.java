/**
 * 
 */
/**
 * @author Leon
 *
 */
package test;

import java.io.IOException;

import net.sf.json.JSONObject;
import entity.AccessToken;
import util.WXUtil;

public class WxTest{
	public static void main(String[] args) throws IOException {
		AccessToken token = WXUtil.getAccessToken();
		String token2 = token.getAccess_token();
		System.out.println(token2);
		int in = token.getExpires_in();
		System.out.println(in);
		
//		String mediaId = WXUtil.upload("D:/container.jpg", token.getAccess_token(), "thumb");
//		System.out.println(mediaId);
		
		String menu = JSONObject.fromObject(WXUtil.initMenu()).toString();
		int result = WXUtil.createMenu(token.getAccess_token(), menu);
		if(result==0){
			System.out.println("创建菜单成功！");
			
		}else{
			System.out.println("错误码是："+result); 
		}
		
		
		
	}
}