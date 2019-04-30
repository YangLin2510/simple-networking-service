package yang.lin35.networking_service_demo.api;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import yang.lin35.networking_service_demo.entitys.MsgEntity;
@Component
public class ActiveMqManager {
	@Autowired
	ConnectionFactory factory;
	@Autowired
	HttpMessageUtils httpUtils;
		
	public void sendMessageToQueue(String queue,String message) throws JMSException {
		Connection connection = null;
		Session session = null;
		Queue q = null;
		MessageProducer producer = null;
		
		connection = factory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		q = session.createQueue(queue);
		producer = session.createProducer(q);
		TextMessage msg = session.createTextMessage(message);
		producer.send(msg);
		producer.close();
		session.close();
		connection.close();
	}
	
	public void setListener(String queue,String url)  throws JMSException{
		Connection connection = null;
		Session session = null;
		Queue q = null;
		
		connection = factory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		q = session.createQueue(queue);
		MessageConsumer consumer = session.createConsumer(q);
		consumer.setMessageListener(msg->{
			if(msg instanceof TextMessage) {
				TextMessage m = (TextMessage)msg;
				try {
					String text = m.getText();
					messageReceiver(text,url);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}
	
	private void messageReceiver(String msg,String url) {
		MsgEntity msgMap = JSON.parseObject(msg,MsgEntity.class);
		
		httpUtils.sendHttpMessageToClient(msgMap.getMsg(),url);
	}
}
