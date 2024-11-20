package example.bot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Класс реализует модульные тесты для верхнеуровневой логики бота
 */
class BotLogicTest {

    /**
     * Исполняет верхнеуровневую логику бота
     */
    private BotLogic botLogic;

    /**
     * Сохраняет сообщения, отправленные botLogic
     */
    private TestBot testBot;

    /**
     * Пользователь
     */
    private User user;

    @BeforeEach
    void setUp() {
        testBot = new TestBot();
        botLogic = new BotLogic(testBot);
        user = new User(1L);
    }

    /**
     * Тест для команды /test:
     * <p>
     * Заходим в режим
     * Проверяем, что нам написали вопрос, успешно на него отвечаем, убеждаемся в корректности ответа
     * Проверяем, что нам написали вопрос, неуспешно на него отвечаем, убеждаемся в корректности ответа
     * Проверяем, что нам сообщили о завершении теста
     */
    @Test
    void testCommandTest() {
        int currentMessagesSize;
        botLogic.processCommand(user, "/test");

        Assertions.assertEquals("Вычислите степень: 10^2", testBot.getMessages().getLast());
        botLogic.processCommand(user, "100");
        currentMessagesSize = testBot.getMessages().size();
        Assertions.assertEquals("Правильный ответ!", testBot.getMessages().get(currentMessagesSize - 2));

        Assertions.assertEquals("Сколько будет 2 + 2 * 2", testBot.getMessages().getLast());
        botLogic.processCommand(user, "8");
        currentMessagesSize = testBot.getMessages().size();
        Assertions.assertEquals("Вы ошиблись, верный ответ: 6", testBot.getMessages().get(currentMessagesSize - 2));

        Assertions.assertEquals("Тест завершен", testBot.getMessages().getLast());
    }

    /**
     * Тест для команды /test:
     * <p>
     * Граничный случай, все тесты не пройдены
     */
    @Test
    void testFailTests() {
        int currentMessagesSize;
        botLogic.processCommand(user, "/test");

        Assertions.assertEquals("Вычислите степень: 10^2", testBot.getMessages().getLast());
        botLogic.processCommand(user, "10");
        currentMessagesSize = testBot.getMessages().size();
        Assertions.assertEquals("Вы ошиблись, верный ответ: 100", testBot.getMessages().get(currentMessagesSize - 2));

        Assertions.assertEquals("Сколько будет 2 + 2 * 2", testBot.getMessages().getLast());
        botLogic.processCommand(user, "8");
        currentMessagesSize = testBot.getMessages().size();
        Assertions.assertEquals("Вы ошиблись, верный ответ: 6", testBot.getMessages().get(currentMessagesSize - 2));
    }

    /**
     * Тест для команды /test:
     * <p>
     * Граничный случай, все тесты пройдены успешно
     */
    @Test
    void testSuccessCommandsTest() {
        int currentMessagesSize;
        botLogic.processCommand(user, "/test");

        Assertions.assertEquals("Вычислите степень: 10^2", testBot.getMessages().getLast());
        botLogic.processCommand(user, "100");
        currentMessagesSize = testBot.getMessages().size();
        Assertions.assertEquals("Правильный ответ!", testBot.getMessages().get(currentMessagesSize - 2));

        Assertions.assertEquals("Сколько будет 2 + 2 * 2", testBot.getMessages().getLast());
        botLogic.processCommand(user, "6");
        currentMessagesSize = testBot.getMessages().size();
        Assertions.assertEquals("Правильный ответ!", testBot.getMessages().get(currentMessagesSize - 2));
    }

    /**
     * Тест для команды /notify
     * <p>
     * Заходим в режим
     * Проверяем, что нам нужно ввести текст для напомнинания, вводим текст, проверяем ответ
     * Проверяем, что вывелся соответствующий текст, вводим задержку
     * Убеждаемся, что напоминание не пришло сразу и спустя какое-то время до назначенного
     * Ожидаем напоминание и сверяемся с ним
     */
    @Test
    void testCommandNotify() throws InterruptedException {
        botLogic.processCommand(user, "/notify");

        Assertions.assertEquals("Введите текст напоминания", testBot.getMessages().getLast());
        botLogic.processCommand(user, "Напоминание");

        Assertions.assertEquals("Через сколько секунд напомнить?", testBot.getMessages().getLast());
        botLogic.processCommand(user, "1");
        Assertions.assertEquals("Напоминание установлено", testBot.getMessages().getLast());

        Assertions.assertEquals(3, testBot.getMessages().size());
        Thread.sleep(900);
        Assertions.assertEquals(3, testBot.getMessages().size());

        Thread.sleep(120);
        Assertions.assertNotEquals(3, testBot.getMessages().size());
        Assertions.assertTrue(testBot.getMessages().getLast().contains("Напоминание"));
    }

    /**
     * Тест для команды /repeat
     * <p>
     * Заходим в режим
     * Вводим верный ответ
     * Проверяем, что после верного ответа нет вопросов для повторения
     * Вводим неверный ответ
     * Проверяем, что после неверного ответа есть вопросы для повторения
     * Проверяем, что если ответить верно на вопрос для повторения, он больше не будет вопросом для повторения
     */
    @Test
    void testCommandRepeat() {
        int currentMessagesSize;
        botLogic.processCommand(user, "/test");

        Assertions.assertEquals("Вычислите степень: 10^2", testBot.getMessages().getLast());
        botLogic.processCommand(user, "100");
        currentMessagesSize = testBot.getMessages().size();
        Assertions.assertEquals("Правильный ответ!", testBot.getMessages().get(currentMessagesSize - 2));

        botLogic.processCommand(user, "/repeat");
        Assertions.assertEquals("Нет вопросов для повторения", testBot.getMessages().getLast());

        currentMessagesSize = testBot.getMessages().size();
        Assertions.assertEquals("Сколько будет 2 + 2 * 2", testBot.getMessages().get(currentMessagesSize - 2));
        botLogic.processCommand(user, "8");
        currentMessagesSize = testBot.getMessages().size();
        Assertions.assertEquals("Вы ошиблись, верный ответ: 6", testBot.getMessages().get(currentMessagesSize - 2));

        botLogic.processCommand(user, "/repeat");
        Assertions.assertEquals("Сколько будет 2 + 2 * 2", testBot.getMessages().getLast());
        botLogic.processCommand(user, "6");

        botLogic.processCommand(user, "/repeat");
        Assertions.assertEquals("Нет вопросов для повторения", testBot.getMessages().getLast());
    }
}
