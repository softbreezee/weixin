package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;

import entity.Image;
import entity.ImageMessage;
import entity.Music;
import entity.MusicMessage;
import entity.News;
import entity.NewsMessage;
import entity.TextMessage;

public class MessageUtil {
	
	public static final String MESSAGE_TEXT = "text";
	public static final String MESSAGE_IMAGE = "image";
	public static final String MESSAGE_VOICE = "voice";
	public static final String MESSAGE_VIDEO = "video";
	public static final String MESSAGE_LOCATION = "location";
	public static final String MESSAGE_EVENT = "event";
	public static final String MESSAGE_SUBSCRIBE = "subscribe";
	public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
	public static final String MESSAGE_CLICK = "CLICK";
	public static final String MESSAGE_VIEW = "VIEW";
	public static final String MESSAGE_NEWS = "news";
	public static final String MESSAGE_MUSIC = "music";
	
	
	/**
	 * xmlתΪmap����
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Map<String,String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException{
		Map<String,String> map = new HashMap<String,String>();
		SAXReader reader = new SAXReader();
		
		ServletInputStream is = request.getInputStream();
		Document document = reader.read(is);
		Element rootEle = document.getRootElement();
		
		List<Element> list = rootEle.elements();
		
		for (Element element : list) {
			map.put(element.getName(), element.getText());
		}
		is.close();
		return map;
	}
	
	/**
	 * ��������Ϣת��Ϊxml
	 * @param musicMessage
	 * @return
	 */
	public static String musicMessageToXml(MusicMessage musicMessage) {
		XStream xstream = new XStream();
		//��һ����ʲô��˼��
		xstream.alias("xml", musicMessage.getClass());
		return xstream.toXML(musicMessage);
	}
		
	/**
	 * ��ͼ����Ϣת��Ϊxml
	 * @param newsMessage
	 * @return
	 */
	public static String newsMessageToXml(NewsMessage newsMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new News().getClass());
		return xstream.toXML(newsMessage);
	}
	
	/**
	 * ���ı���Ϣ����תΪxml
	 * @param textMessage
	 * @return
	 */
	public static String textMessageToXml(TextMessage textMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}
	
	/**
	 * ͼƬ��Ϣת��xml����
	 * @param imageMessage
	 * @return
	 */
	public static String imageMessageToXml(ImageMessage imageMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", imageMessage.getClass());
		return xstream.toXML(imageMessage);
	}
	/**
	 * ���˵�
	 * @return
	 */
	public static String menuText(){
		StringBuffer sb = new StringBuffer();
		sb.append("��ӭ���Ĺ�ע���밴�ղ˵�����ʾ���в�����\n\n");
		sb.append("1�����ںŽ���\n");
		sb.append("2����Ϸ��\n\n");
		sb.append("�ظ��������˲˵���");
		return sb.toString();
	}
	public static String firstMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("1......\n");
		return sb.toString();
	}
	public static String secondMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("2..........\n");
		return sb.toString();
	}
	
	/**
	 * ����text����
	 * @param ToUserName
	 * @param FromUserName
	 * @param Content
	 * @return
	 */
	public static String initText(String ToUserName,String FromUserName,String Content){
		TextMessage tm = new TextMessage();
		tm.setFromUserName(ToUserName);
		tm.setToUserName(FromUserName);
		tm.setMsgType(MESSAGE_TEXT);
		tm.setCreateTime(new Date().toLocaleString());
		tm.setContent(Content);
		
		return textMessageToXml(tm);
	}
	
	/**
	 * ͼ����Ϣ��ʼ��
	 * @param ToUserName
	 * @param FromUserName
	 * @param Content
	 * @return
	 */
	
	public static String initNewsMessaget(String ToUserName,String FromUserName){
		String message = null;
		List<News> newsList = new ArrayList<News>();
		NewsMessage newsMessage = new NewsMessage();
		
		News news = new News();
		news.setTitle("����ͼ��");
		news.setDescription("����ͼ�Ľӿڻ�ã����⣬ͼƬ���أ��Լ���ת");
		news.setPicUrl("http://mytest.ngrok.xiaomiqiu.cn/weixin/image/perfect.png");
		news.setUrl("http://www.baidu.com");
		
		newsList.add(news);
		newsMessage.setFromUserName(ToUserName);
		newsMessage.setToUserName(FromUserName);
		newsMessage.setCreateTime(new Date().toLocaleString());
		newsMessage.setMsgType(MESSAGE_NEWS);
		newsMessage.setArticleCount(newsList.size());
		newsMessage.setArticles(newsList);
		
		message = newsMessageToXml(newsMessage);
		return message;
		
	}
	
	
	/**
	 * ��ʼ��ͼƬ��Ϣ
	 * @param ToUserName
	 * @param FromUserName
	 * @return
	 */
	public static String initImageMessage(String ToUserName,String FromUserName ){
		String message = null;
		Image image = new Image();
		image.setMediaId("HShVZo3kFERdOvSjY2OGXIdKG5ISZNmHbhAXMNEu6R2t7ORR7OiZyjjWPHXP_Vzd");
		ImageMessage im = new ImageMessage();
		im.setImage(image);
		im.setFromUserName(ToUserName);
		im.setToUserName(FromUserName);
		im.setMsgType(MESSAGE_IMAGE);
		im.setCreateTime(new Date().toLocaleString());
		System.out.println(new Date().toLocaleString());
		
		message = imageMessageToXml(im);
		return message;
		
	}
	
	/**
	 * ��ʼ������
	 * @param ToUserName
	 * @param FromUserName
	 * @return
	 */
	public static String initMusicMessage(String ToUserName,String FromUserName ){
		String message = null;
		Music m = new Music();
		m.setThumbMediaId("CgaStzZ-OyGGj-yaeGdws25Q4N7xQeP9g6UQk2ExzA8YzxKGsuGgS6YwC3GlTqm_");
		m.setTitle("the world is so beautiful");
		m.setDescription("����test");
		m.setMusicUrl("http://mytest.ngrok.xiaomiqiu.cn/weixin/music/Rameses.mp3");
		m.setHQMusicUrl("http://mytest.ngrok.xiaomiqiu.cn/weixin/music/Rameses.mp3");
		
		MusicMessage musicMessage = new MusicMessage();
		musicMessage.setToUserName(FromUserName);
		musicMessage.setFromUserName(ToUserName);
		musicMessage.setCreateTime(new Date().toLocaleString());
		musicMessage.setMsgType(MESSAGE_MUSIC);
		musicMessage.setMusic(m);
		message = musicMessageToXml(musicMessage);
		return message;
		
		
	}


	
}
