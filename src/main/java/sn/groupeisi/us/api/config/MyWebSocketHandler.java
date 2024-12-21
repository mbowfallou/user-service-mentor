package sn.groupeisi.us.api.config;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;

public class MyWebSocketHandler extends TextWebSocketHandler {
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Message reçu: " + message.getPayload());
        session.sendMessage(new TextMessage("Message reçu : " + message.getPayload()));
    }
}

