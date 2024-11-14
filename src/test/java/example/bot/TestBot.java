package example.bot;

import java.util.ArrayList;
import java.util.List;

/**
 * Тестовая реализация интерфейса Bot для сбора отправленных сообщений.
 * Теперб мы работаем не с консолью, а сохраняем сообщения в очередь.
 */
public class TestBot implements Bot {

    /**
     * Поле, хранящее историю сообщений бота
     */
    private List<String> messages = new ArrayList<>();

    @Override
    public void sendMessage(Long chatId, String message) {
        messages.add(message);
    }

    public List<String> getMessages() {
        return messages;
    }
}