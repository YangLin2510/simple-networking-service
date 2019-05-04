package yang.lin35.networking_service_demo.entitys;

import java.util.Date;

public class MsgEntity {

	private String username;//消息发送者
	private String msg;//消息
	private String receiver;//消息接收者
	private Date sendDateTime;//发送日期
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public Date getSendDateTime() {
		return sendDateTime;
	}
	public void setSendDateTime(Date sendDateTime) {
		this.sendDateTime = sendDateTime;
	}
	
	
}
