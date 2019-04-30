package yang.lin35.networking_service_demo.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;

@Component
public class HttpMessageUtils {
	
	RestTemplate restTemplate = new RestTemplate();
	
	/**
	 * 发送Http消息给指定的地址
	 * JSON格式
	 * {
	 * 		msg : "msg"
	 * }
	 * @param msg
	 * @param URI
	 */
	public void sendHttpMessageToClient(String msg,String url) {
		Map<String,Object> entity = new HashMap<String,Object>();
		entity.put("msg", msg);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application/json"));
		HttpEntity<String> httpEntity = new HttpEntity<String>(JSON.toJSONString(entity), headers);
		restTemplate.postForLocation(url, httpEntity);
	}

}
