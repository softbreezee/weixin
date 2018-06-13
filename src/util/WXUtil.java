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
	//access_token�Ļ�ã�
	//����get��������appid��appsecret��΢�ź�̨��Ȼ��ش�һ��json���ݰ�
	//������access_token
	/**
	 * ���ǵ������������������������ͻ��ˣ��ύ����ĵط�
	 * ��������������������΢�ŷ�������get��ʽ
	 * Ȼ��΢�ŷ���������һ��json��ʽ�����ݰ�
	 * @param url
	 * @return
	 */
	public static JSONObject doGetStr(String url){
		//����һ��httpClient��Ϊ��������Ŀͻ���
		DefaultHttpClient httpClient = new DefaultHttpClient();
		//�����������
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		//�������󣬷�����Ӧ
		HttpResponse response;
		try {
			response = httpClient.execute(httpGet);
			//�õ���Ӧ�������Ӧ�����
			HttpEntity entity = response.getEntity();
			//�ж���Ӧ�Ƿ�Ϊ��
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
	 * ����post����ķ�ʽ�ύ
	 * @param url
	 * @param outStr
	 * @return
	 */
	public static JSONObject doPostStr(String url,String outStr){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		JSONObject jsonObject = null;
		//����post�����������
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
	 * ˵����
	 * 1����Чʱ��2Сʱ��һ���б����ڱ���
	 * 2�����⣺�ǲ���ÿһ���û����ʶ��ᴴ��һ����
	 * @return
	 */
	public static AccessToken getAccessToken(){
		AccessToken accessToken = new AccessToken();
		//ȷ����ַ
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
	 * ��װһ���˵�
	 * @return
	 */
	public static Menu initMenu(){
		Menu menu = new Menu();
		
		ClickButton clickButton = new ClickButton();
		clickButton.setName("click�˵�");
		clickButton.setType("click");
		clickButton.setKey("11");
		
		ViewButton viewButton = new ViewButton();
		viewButton.setName("view�˵�");
		viewButton.setType("view");
		//����·��������Э��
		viewButton.setUrl("http://www.imooc.com");
		
		ClickButton clickButton2 = new ClickButton();
		clickButton2.setName("ɨ���¼�");
		clickButton2.setType("scancode_push");
		clickButton2.setKey("31");
		ClickButton clickButton3 = new ClickButton();
		clickButton3.setName("��ȡλ��");
		clickButton3.setType("location_select");
		clickButton3.setKey("32");
		
		Button button = new Button();
		button.setName("�˵�");
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
	 * �ϴ��ļ������mediaId
	 * @param filePath
	 * @param accessToken
	 * @param type
	 * @return
	 * @throws IOException
	 */
	public static String upload(String filePath,String accessToken,String type) throws IOException{
		File file = new File(filePath);
		//����ļ����󲻴��ڣ�������Ŀ¼
		if(!file.exists() || !file.isFile()){
			throw new IOException("�ļ�������");
		}
		
		String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);
		URL urlObj = new URL(url);
		//����
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);//���Ի���
		
		//��������ͷ��Ϣ
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");
		
		//���ñ߽�
		String BOUNDARY = "----------"+System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data;boundary="+BOUNDARY);
		
		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition:form-data;name=\"file\";filename=\""+file.getName()+"\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");
		
		byte[] head = sb.toString().getBytes("UTF-8");
		
		//��������
		OutputStream out = new DataOutputStream(con.getOutputStream());
		//�����ͷ
		out.write(head);
		
		//�ļ����Ĳ���
		//���ļ������ļ��ķ�ʽ ���뵽url��
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while((bytes = in.read(bufferOut))!= -1){
			out.write(bufferOut, 0, bytes);
		}
		in.close();
		
		//��β����
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");//����������ݷָ���
		
		out.write(foot);
		
		out.flush();
		out.close();
		
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		String result = null;
		try{
			//����BufferedReader����������ȡURL����Ӧ
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