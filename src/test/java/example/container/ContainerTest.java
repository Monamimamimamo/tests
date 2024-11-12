package example.container;

import example.container.Container;
import example.container.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ContainerTest {

    Container container;
    Item item1;
    Item item2;
    @BeforeEach
    public void setUp() {
        container = new Container();
        item1 = new Item(1L);
        item2 = new Item(2L);
    }

    /**
     * Тест проверяет добавление элемента в контейнер.
     *
     * Сначала проверяем, что контейнер пуст
     * Потом добавляем элемент в контейнер
     * Проверяем, что контейнер не пуст и содержит ожидаемый элемент
     */
    @Test
    public void addItemTest() {
        Assertions.assertEquals(0, container.size());

        container.add(item1);
        Assertions.assertEquals(1, container.size());
        Assertions.assertTrue(container.contains(item1));
    }

    /**
     * Тест проверяет удаление элемента из контейнера.
     *
     * Добавляем элемент
     * Удаляем элемент и проверяем, что метод вернул true (операция успешна)
     * Проверяем, что контейнер больше не содержит элемент
     */
    @Test
    public void removeItemTest() {
        container.add(item1);
        Assertions.assertTrue(container.remove(item1));
        Assertions.assertFalse(container.contains(item1));
    }

    /**
     * Тест проверяет удаление несуществующего в контейнере элемента.
     *
     * Удаляем несуществующий элемент, ожидаем false
     * На всякий случай убеждаемся, что в контейнере ничего не появилось
     */
    @Test
    void testRemoveNonExistentItem() {
        Assertions.assertFalse(container.remove(item2));
        Assertions.assertEquals(0, container.size());
    }
}
