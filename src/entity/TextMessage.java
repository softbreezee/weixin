/**
 * 
 * 
 */
/**
 * @author Leon
 *
 */
package entity;

public class TextMessage extends BaseMessage{

	private String Content;//�ı���Ϣ����
	private String MsgId;//��Ϣid��64λ����

	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getMsgId() {
		return MsgId;
	}
	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
	
	
	
	
	
}