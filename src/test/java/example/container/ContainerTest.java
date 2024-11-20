package example.container;

import example.container.Container;
import example.container.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Класс реализует модульные тесты для методов
 * добавления и удаления класса Container
 */
public class ContainerTest {

    private Container container;

    @BeforeEach
    public void setUp() {
        container = new Container();
    }

    /**
     * Тест проверяет добавление элемента в контейнер.
     * <p>
     * Сначала проверяем, что контейнер пуст
     * Потом добавляем элемент в контейнер
     * Проверяем, что контейнер не пуст и содержит ожидаемый элемент
     */
    @Test
    public void addItemTest() {
        Assertions.assertEquals(0, container.size());
        Item item1 = new Item(1L);

        container.add(item1);
        Assertions.assertEquals(1, container.size());
        Assertions.assertTrue(container.contains(item1));
    }

    /**
     * Тест проверяет удаление существующего элемента из контейнера.
     * <p>
     * Добавляем элемент
     * Удаляем элемент и проверяем, что контейнер пуст
     * Проверяем, что контейнер больше не содержит элемент
     */
    @Test
    public void removeItemTest() {
        Item item1 = new Item(1L);
        container.add(item1);
        container.remove(item1);
        Assertions.assertEquals(0, container.size());
        Assertions.assertFalse(container.contains(item1));
    }

    /**
     * Тест проверяет удаление несуществующего в контейнере элемента.
     * <p>
     * Добавляем элемент (1)
     * Удаляем несуществующий элемент (2), ожидаем false
     * Убеждаемся, что команда не удалила существующий элемент
     * На всякий случай убеждаемся, что в контейнере ничего не появилось
     */
    @Test
    void testRemoveNonExistentItem() {
        Item item1 = new Item(1L);
        Item item2 = new Item(2L);
        container.add(item1);
        Assertions.assertFalse(container.remove(item2));
        Assertions.assertTrue(container.contains(item1));
        Assertions.assertEquals(1, container.size());
    }
}
