/**
 * 
 */
/**
 * @author Leon
 *
 */
package util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import entity.AccessToken;
import menu.Button;
import menu.ClickButton;
import menu.Menu;
import menu.ViewButton;
import net.sf.json.JSONObject;

public class WXUtil{
	private static final String APPID = "wxc5ceffc15627d911";  
	private static final String APPSECRET = "abc3333e3c6e54dd2aa6042b00342ce0";  
	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";	
	private static final String CREATE_MENU_URL= "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	//access_token的获得，
	//先用get方法传递appid与appsecret到微信后台，然后回传一个json数据包
	//包含着access_token
	/**
	 * 这是第三方服务器，服务器又做客户端，提交请求的地方
	 * 从这个服务器发送请求给微信服务器，get方式
	 * 然后微信服务器返回一个json格式的数据包
	 * @param url
	 * @return
	 */
	public static JSONObject doGetStr(String url){
		//创建一个httpClient作为发送请求的客户端
		DefaultHttpClient httpClient = new DefaultHttpClient();
		//创建请求对象，
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		//发送请求，返回响应
		HttpResponse response;
		try {
			response = httpClient.execute(httpGet);
			//得到响应对象的响应体对象
			HttpEntity entity = response.getEntity();
			//判断响应是否为空
			if(entity!=null){
				String result = EntityUtils.toString(entity);
				jsonObject = JSONObject.fromObject(result);
				
			}
		} catch (ClientProtocolException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return jsonObject;
	}
	/**
	 * 设置post请求的方式提交
	 * @param url
	 * @param outStr
	 * @return
	 */
	public static JSONObject doPostStr(String url,String outStr){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		JSONObject jsonObject = null;
		//设置post请求的请求体
		try {
			httpPost.setEntity(new StringEntity(outStr,"UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			String result = EntityUtils.toString(response.getEntity(),"UTF-8");
			jsonObject = JSONObject.fromObject(result);
		} catch (ClientProtocolException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return jsonObject;
	}
	/**
	 * 说明：
	 * 1、有效时间2小时，一般有保存在本地
	 * 2、问题：是不是每一个用户访问都会创建一个？
	 * @return
	 */
	public static AccessToken getAccessToken(){
		AccessToken accessToken = new AccessToken();
		//确定地址
		String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
		JSONObject jsonObject = doGetStr(url);
//		System.out.println(jsonObject);
		if(jsonObject != null){
			accessToken.setAccess_token(jsonObject.getString("access_token"));
			accessToken.setExpires_in(jsonObject.getInt("expires_in"));
		}
		return accessToken;
	}
	
	/**
	 * 组装一个菜单
	 * @return
	 */
	public static Menu initMenu(){
		Menu menu = new Menu();
		
		ClickButton clickButton = new ClickButton();
		clickButton.setName("click菜单");
		clickButton.setType("click");
		clickButton.setKey("11");
		
		ViewButton viewButton = new ViewButton();
		viewButton.setName("view菜单");
		viewButton.setType("view");
		//完整路径，包括协议
		viewButton.setUrl("http://www.imooc.com");
		
		ClickButton clickButton2 = new ClickButton();
		clickButton2.setName("扫码事件");
		clickButton2.setType("scancode_push");
		clickButton2.setKey("31");
		ClickButton clickButton3 = new ClickButton();
		clickButton3.setName("获取位置");
		clickButton3.setType("location_select");
		clickButton3.setKey("32");
		
		Button button = new Button();
		button.setName("菜单");
		button.setSub_button(new Button[]{clickButton2,clickButton3});
		
		menu.setButton(new Button[]{clickButton,viewButton,button});
		
		return menu;
	}
	
	public static int createMenu(String token,String menu){
		int result = 0;
		String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = doPostStr(url, menu);
		if(jsonObject!=null){
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
	
	/**
	 * 上传文件，获得mediaId
	 * @param filePath
	 * @param accessToken
	 * @param type
	 * @return
	 * @throws IOException
	 */
	public static String upload(String filePath,String accessToken,String type) throws IOException{
		File file = new File(filePath);
		//如果文件对象不存在，或者是目录
		if(!file.exists() || !file.isFile()){
			throw new IOException("文件不存在");
		}
		
		String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);
		URL urlObj = new URL(url);
		//连接
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);//忽略缓存
		
		//设置请求头信息
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");
		
		//设置边界
		String BOUNDARY = "----------"+System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data;boundary="+BOUNDARY);
		
		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition:form-data;name=\"file\";filename=\""+file.getName()+"\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");
		
		byte[] head = sb.toString().getBytes("UTF-8");
		
		//获得输出流
		OutputStream out = new DataOutputStream(con.getOutputStream());
		//输出表头
		out.write(head);
		
		//文件正文部分
		//把文件已流文件的方式 推入到url中
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while((bytes = in.read(bufferOut))!= -1){
			out.write(bufferOut, 0, bytes);
		}
		in.close();
		
		//结尾部分
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");//定义最后数据分隔线
		
		out.write(foot);
		
		out.flush();
		out.close();
		
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		String result = null;
		try{
			//定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while((line = reader.readLine())!=null){
				buffer.append(line);
			}
			if(result == null){
				result = buffer.toString();
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(reader!=null){
				reader.close();
			}
		}
		JSONObject jsonObj = JSONObject.fromObject(result);
		System.out.println(jsonObj);
		String media_Id = "media_id";
		if(type.equals("thumb")){
			media_Id = type+"_"+media_Id;
		}
		String mediaId = jsonObj.getString(media_Id);
		return mediaId;
	}
	
	
	
	
	
}