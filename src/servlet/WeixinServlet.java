/**
 * 
 */
/**
 * @author Leon
 *
 */
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import entity.TextMessage;
import util.CheckUtil;
import util.MessageUtil;

public class WeixinServlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		//微信服务器发送的四个参数
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
		
		
		PrintWriter out = resp.getWriter();
		//验证是否与微信服务器连接
		if(CheckUtil.checkSignature(signature, timestamp, nonce)){
			out.print(echostr);
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		PrintWriter pw = resp.getWriter();
		try {
			Map<String,String> map = MessageUtil.xmlToMap(req);
			String ToUserName = map.get("ToUserName");
			System.out.println(ToUserName);
			String FromUserName = map.get("FromUserName");
			String CreateTime = map.get("CreateTime");
			System.out.println(CreateTime);
			String MsgType = map.get("MsgType");
			String Content = map.get("Content");
			String MsgId = map.get("MsgId");
			
			
			String message = null;
			if(MessageUtil.MESSAGE_TEXT.equals(MsgType)){
				
				if("1".equals(Content)){
					message = MessageUtil.initText(ToUserName, FromUserName, MessageUtil.firstMenu());
				}
				if("2".equals(Content)){
					message = MessageUtil.initNewsMessaget(ToUserName, FromUserName);
					
				}
				if("3".equals(Content)){
					message = MessageUtil.initImageMessage(ToUserName, FromUserName);
					
				}
				if("4".equals(Content)){
					message = MessageUtil.initMusicMessage(ToUserName, FromUserName);
					
				}
				if("？".equals(Content) || "?".equals(Content)){
					message = MessageUtil.initText(ToUserName, FromUserName, MessageUtil.menuText());
				}
				
//				TextMessage tm = new TextMessage();
//				tm.setFromUserName(ToUserName);
//				tm.setToUserName(FromUserName);
//				tm.setMsgType("text");
//				tm.setCreateTime(new Date().toLocaleString());
//				tm.setContent("您发的消息是："+Content);
//				message = MessageUtil.textMessageToXml(tm);
				
			}else if(MessageUtil.MESSAGE_EVENT.equals(MsgType)){
				//得到事件的类型
				String eventType = map.get("Event");
				if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){
					message = MessageUtil.initText(ToUserName, FromUserName, MessageUtil.menuText());
				}
				
				
			}
			pw.print(message);
		} catch (DocumentException e) {
			
			e.printStackTrace();
		} finally{
			
			pw.close();
		}
		
	}
	
	
}