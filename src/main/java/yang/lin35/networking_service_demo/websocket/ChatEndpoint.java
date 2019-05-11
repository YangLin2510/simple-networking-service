package yang.lin35.networking_service_demo.websocket;

import org.jboss.logging.Message;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/chat/{userId}")
@Component
public class ChatEndpoint {

    private static AtomicInteger onlineUserCount = new AtomicInteger(0);

    private Session session;
    private Long userId;

    //保存用户和和队友的websocket实例
    private static Map<Long,Set<ChatEndpoint>> socketMap = new HashMap<>();


    /**
     * 给web端推送消息
     * @param userId
     * @param msg
     */
    public void sendMessage(Long userId,String msg){
        try {
            Set<ChatEndpoint> sockets = socketMap.get(userId);
            for(ChatEndpoint chat:sockets){
                chat.session.getBasicRemote().sendText(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(@PathParam("userId") Long userId, Session session){
        onlineUserCount.addAndGet(1);
        this.userId = userId;
        this.session = session;
        if(socketMap.containsKey(userId)){
            socketMap.get(userId).add(this);
        }else {
            Set<ChatEndpoint> sockets = new HashSet<>();
            sockets.add(this);
            socketMap.put(userId,sockets);
        }
    }

    @OnMessage
    public String onMessage(String message){
        System.out.println("onMessage-->"+message);
        return null;
    }

    @OnError
    public void onError(Throwable t) {
        System.out.println("onError");
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        onlineUserCount.addAndGet(-1);
        socketMap.remove(this.userId);//移除当前用户的websocket链接
        System.out.println("onClose");
    }

}
