package yang.lin35.networking_service_demo.api;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yang.lin35.networking_service_demo.dao.UserMapper;
import yang.lin35.networking_service_demo.dao.date_entitys.User;
import yang.lin35.networking_service_demo.entitys.MsgEntity;

import javax.jms.JMSException;


@RestController
public class ConnectController {
	@Autowired
	ActiveMqManager mq;
	@Autowired
	UserMapper userMapper;

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

	/**
	 * 初始化 队列消息监听
	 * @param id
	 * @return
	 */
	@RequestMapping("/init/{userId}")
	public String initConnect(@PathVariable("userId") Long id) {
		try {
			User currentUser = userMapper.findUserById(id);
			String[] contactArray = currentUser.getContactList().split(",");
			for (String contactId:contactArray){
				User c = userMapper.findUserById(Long.valueOf(contactId));
				mq.setListener(c.getId()+":"+id,id);//队列的格式,发送者ID:接受者ID
			}
		} catch (JMSException e) {
			e.printStackTrace();
			return "-1";
		}
		return "0";
	}
}
