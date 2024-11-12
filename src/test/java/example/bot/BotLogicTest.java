package example.bot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BotLogicTest {

    private BotLogic botLogic;
    private TestBot testBot;
    private User user;

    @BeforeEach
    void setUp() {
        testBot = new TestBot();
        botLogic = new BotLogic(testBot);
        user = new User(1L);
    }

    /**
     * Тест для команды /test:
     * проверка начала теста и первого вопроса
     */
    @Test
    void testCommandTest() {
        botLogic.processCommand(user, "/test");

        Assertions.assertEquals(State.TEST, user.getState());
        Assertions.assertEquals("Вычислите степень: 10^2", testBot.getLastMessage());
    }

    /**
     * Тест для команды /notify
     * Заходим в режим, проверяем что мы в него зашли
     * Проверяем, что нам нужно ввести текст для напомнинания, вводим текст, проверяем ответ
     * Проверяем, что мы в режиме задержки, и что вывелся соответствующий текст, вводим задержку
     * Проверяем, что мы перешли в начальное состояние, ожидаем напоминание и сверяемся с ним
     */
    @Test
    void testCommandNotify() throws InterruptedException {
        botLogic.processCommand(user, "/notify");

        Assertions.assertEquals(State.SET_NOTIFY_TEXT, user.getState());
        Assertions.assertEquals("Введите текст напоминания", testBot.getLastMessage());
        botLogic.processCommand(user, "Напоминание");

        Assertions.assertEquals(State.SET_NOTIFY_DELAY, user.getState());
        Assertions.assertEquals("Через сколько секунд напомнить?", testBot.getLastMessage());
        botLogic.processCommand(user, "5");
        Assertions.assertEquals("Напоминание установлено", testBot.getLastMessage());

        Assertions.assertEquals(State.INIT, user.getState());
        Thread.sleep(5020);
        Assertions.assertEquals("Сработало напоминание: 'Напоминание'", testBot.getLastMessage());
    }

    /**
     * Тест для команды /repeat
     * Заходим в режим, проверяем что мы в него зашли
     * Вводим неверный ответ, убеждаемся в неправильности ответа, ожидаем следующий вопрос
     * Возвращаемся к предыдущему вопросу, проверяем что мы в него зашли
     * Вводим верный ответ, убеждаемся в его правильности
     * Проверяем, что мы в стандартном состоянии,  и что список вопросов с ошибками пуст
     */
    @Test
    void testCommandRepeat() {
        botLogic.processCommand(user, "/test");
        Assertions.assertEquals(State.TEST, user.getState());

        Assertions.assertEquals("Вычислите степень: 10^2", testBot.getLastMessage());
        botLogic.processCommand(user, "Неправильный ответ");
        Assertions.assertEquals("Вы ошиблись, верный ответ: 100", testBot.getLastMessage());
        Assertions.assertEquals("Сколько будет 2 + 2 * 2", testBot.getLastMessage());

        botLogic.processCommand(user, "/repeat");
        Assertions.assertEquals(State.REPEAT, user.getState());
        Assertions.assertEquals("Вычислите степень: 10^2", testBot.getLastMessage());
        botLogic.processCommand(user, "100");
        Assertions.assertEquals("Правильный ответ!", testBot.getLastMessage());


        Assertions.assertEquals(State.INIT, user.getState());
        Assertions.assertTrue(user.getWrongAnswerQuestions().isEmpty());
    }
}
