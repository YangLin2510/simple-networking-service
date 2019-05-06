package yang.lin35.networking_service_demo.websocket;

import org.jboss.logging.Message;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/chat")
@Component
public class ChatEndpoint {
    @OnOpen
    public void onOpen(Session session){
        System.out.println("onOpen");
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
        System.out.println("onClose");
    }

}
