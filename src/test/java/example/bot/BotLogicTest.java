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
     * Заходим в режим, проверяем что мы в него зашли
     * Проверяем, что нам написали вопрос, успешно на него отвечаем, убеждаемся в корректности ответа
     * Проверяем, что нам написали вопрос, неуспешно на него отвечаем, убеждаемся в корректности ответа
     * Проверяем, что нам сообщили о завершении теста
     */
    @Test
    void testCommandTest() {
        int currentMessagesSize;
        botLogic.processCommand(user, "/test");
        Assertions.assertEquals(State.TEST, user.getState());

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
     * Тест для команды /notify
     * <p>
     * Заходим в режим, проверяем что мы в него зашли
     * Проверяем, что нам нужно ввести текст для напомнинания, вводим текст, проверяем ответ
     * Проверяем, что мы в режиме задержки, и что вывелся соответствующий текст, вводим задержку
     * Убеждаемся, что напоминание не пришло сразу и спустя какое-то время до назначенного
     * Проверяем, что мы перешли в начальное состояние, ожидаем напоминание и сверяемся с ним
     */
    @Test
    void testCommandNotify() throws InterruptedException {
        botLogic.processCommand(user, "/notify");

        Assertions.assertEquals(State.SET_NOTIFY_TEXT, user.getState());
        Assertions.assertEquals("Введите текст напоминания", testBot.getMessages().getLast());
        botLogic.processCommand(user, "Напоминание");

        Assertions.assertEquals(State.SET_NOTIFY_DELAY, user.getState());
        Assertions.assertEquals("Через сколько секунд напомнить?", testBot.getMessages().getLast());
        botLogic.processCommand(user, "5");
        Assertions.assertEquals("Напоминание установлено", testBot.getMessages().getLast());

        Assertions.assertNotEquals("Сработало напоминание: 'Напоминание'", testBot.getMessages().getLast());
        Thread.sleep(4900);
        Assertions.assertNotEquals("Сработало напоминание: 'Напоминание'", testBot.getMessages().getLast());

        Assertions.assertEquals(State.INIT, user.getState());
        Thread.sleep(5020);
        Assertions.assertEquals("Сработало напоминание: 'Напоминание'", testBot.getMessages().getLast());
    }

    /**
     * Тест для команды /repeat
     * <p>
     * Заходим в режим, проверяем что мы в него зашли
     * Вводим неверный ответ, убеждаемся в неправильности ответа, ожидаем следующий вопрос
     * Возвращаемся к предыдущему вопросу, проверяем что мы в него зашли
     * Вводим верный ответ, убеждаемся в его правильности
     * Повторно вводим /repeat, убеждаемся, что бот ответил об отсутствии вопросов, и что список вопросов с ошибками пуст
     * Проверяем, что мы в стандартном состоянии
     */
    @Test
    void testCommandRepeat() {
        int currentMessagesSize;
        botLogic.processCommand(user, "/test");
        Assertions.assertEquals(State.TEST, user.getState());

        Assertions.assertEquals("Вычислите степень: 10^2", testBot.getMessages().getLast());
        botLogic.processCommand(user, "Неправильный ответ");
        currentMessagesSize = testBot.getMessages().size();
        Assertions.assertEquals("Вы ошиблись, верный ответ: 100", testBot.getMessages().get(currentMessagesSize - 2));

        botLogic.processCommand(user, "/repeat");
        Assertions.assertEquals(State.REPEAT, user.getState());
        Assertions.assertEquals("Вычислите степень: 10^2", testBot.getMessages().getLast());
        botLogic.processCommand(user, "100");
        currentMessagesSize = testBot.getMessages().size();
        Assertions.assertEquals("Правильный ответ!", testBot.getMessages().get(currentMessagesSize - 2));

        botLogic.processCommand(user, "/repeat");
        Assertions.assertEquals("Нет вопросов для повторения", testBot.getMessages().getLast());
        Assertions.assertTrue(user.getWrongAnswerQuestions().isEmpty());

        Assertions.assertEquals(State.INIT, user.getState());
    }
}
