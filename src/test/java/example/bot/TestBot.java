package example.bot;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Тестовая реализация интерфейса Bot для сбора отправленных сообщений.
 * Теперб мы работаем не с консолью, а сохраняем сообщения в очередь.
 */
public class TestBot implements Bot {
    private final Queue<String> messages = new LinkedList<>();

    @Override
    public void sendMessage(Long chatId, String message) {
        messages.add(message);
    }

    /**
     * Получить первое сообщение из очереди и удалить его
     * null, если сообщений нет в очереди
     */
    public String getLastMessage() {
        return messages.isEmpty() ? null : messages.poll();
    }
}