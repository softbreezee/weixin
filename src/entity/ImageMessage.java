package entity;


//<xml>
//	<ToUserName>< ![CDATA[toUser] ]></ToUserName>
//	<FromUserName>< ![CDATA[fromUser] ]></FromUserName>
//	<CreateTime>12345678</CreateTime>
//	<MsgType>< ![CDATA[image] ]></MsgType>
//	<Image>
//		<MediaId>< ![CDATA[media_id] ]></MediaId>
//	</Image>
//</xml>
public class ImageMessage extends BaseMessage{
	private Image Image;

	public Image getImage() {
		return Image;
	}
	public void setImage(Image image) {
		Image = image;
	}


}
