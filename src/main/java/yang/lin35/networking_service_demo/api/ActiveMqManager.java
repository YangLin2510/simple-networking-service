package yang.lin35.networking_service_demo.api;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yang.lin35.networking_service_demo.entitys.MsgEntity;
import yang.lin35.networking_service_demo.websocket.ChatEndpoint;

import javax.jms.*;

@Component
public class ActiveMqManager {
	@Autowired
	ConnectionFactory factory;
	@Autowired
	HttpMessageUtils httpUtils;
	@Autowired
	ChatEndpoint chatEndpoint;

    /**
     * 发送消息到指定的队列
     * @param queue
     * @param message
     * @throws JMSException
     */
	public synchronized void sendMessageToQueue(String queue,String message) throws JMSException {
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

    /**
     * 监听队列的消息
     * @param queue 消息队列
     * @param userId
     * @throws JMSException
     */
	public synchronized void setListener(String queue,Long userId)  throws JMSException{
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
					messageReceiver(text,userId);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}
	
	private void messageReceiver(String msg,Long userId) {
		MsgEntity msgMap = JSON.parseObject(msg,MsgEntity.class);
		chatEndpoint.sendMessage(userId,msg);
	}
}
