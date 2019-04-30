package yang.lin35.networking_service_demo.api;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import yang.lin35.networking_service_demo.entitys.MsgEntity;


@RestController
public class ConnectController {
	@Autowired
	ActiveMqManager mq;
	
	Map<String,String> USER_QUEUE = new HashMap<String, String>();
	
	@RequestMapping("/sendToQueue")
	public String sendMessageToQueue(@RequestBody MsgEntity msg) {
		
		try {
			mq.sendMessageToQueue(getQueueName(msg), JSON.toJSONString(msg));
		} catch (JMSException e) {
			e.printStackTrace();
			return "-1";
		}
		return "0";
	}
	
	private String getQueueName(MsgEntity msg) {
		return msg.getUsername()+":"+msg.getReceiver();
	}
	
	@RequestMapping("/init")
	public String initConnect(String username,HttpRequest request) {
		String userHost =  request.getURI().getHost();
		
		/*
		 * TODO: 为用户添加MQ监听器,等待接收消息
		 * 用户的通讯录,每个通讯录代表一个Queue或者一个Topic
		 * 记录用户的IP地址，消息接收监听器收到消息时，发送HTTP消息给接收端	
		*/
		String url = null;
		try {
			mq.setListener("", url);
		} catch (JMSException e) {
			e.printStackTrace();
			return "-1";
		}
		return "0";
	}
}
