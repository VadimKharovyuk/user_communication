package com.example.user_communication.Config;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.CloseStatus;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyWebSocketHandler extends TextWebSocketHandler {
    private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // Сохраняем сессию
        sessions.put(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // Удаляем сессию
        sessions.remove(session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // Обрабатываем полученное сообщение и передаем его другим клиентам
        for (WebSocketSession s : sessions.values()) {
            try {
                if (s.isOpen() && !s.getId().equals(session.getId())) {
                    s.sendMessage(new TextMessage("Echo: " + message.getPayload()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
