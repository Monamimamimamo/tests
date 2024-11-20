package example.note;

import example.note.NoteLogic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Класс реализует модульные тесты для логики класса Note
 */
public class NoteLogicTest {

    NoteLogic noteLogic = new NoteLogic();

    /**
     * Тест проверяет добавление записи
     */
    @Test
    public void testAddNewNote(){
        String commandResult = noteLogic.handleMessage("/add New Note");
        Assertions.assertEquals("Note added!", commandResult);
        String result = noteLogic.handleMessage("/notes");
        Assertions.assertEquals("Your notes: New Note", result);
    }


    /**
     * Тест проверяет корректный вывод всех добавленных записей
     */
    @Test
    public void testGetAllNotes(){
        String result1 = noteLogic.handleMessage("/notes");
        Assertions.assertEquals("Your notes:", result1);
        noteLogic.handleMessage("/add New Note");
        String result2 = noteLogic.handleMessage("/notes");
        Assertions.assertEquals("Your notes: New Note", result2);
    }


    /**
     * Тест проверяет изменение существующей записи
     */
    @Test
    public void testEditNote(){
        noteLogic.handleMessage("/add New Note");
        String commandResult = noteLogic.handleMessage("/edit Edited Note");
        Assertions.assertEquals("Note edited!", commandResult);
        String result = noteLogic.handleMessage("/notes");
        Assertions.assertEquals("Your notes: Edited Note", result);
    }


    /**
     * Тест проверяет удаление существующей записи
     */
    @Test
    public void testDeleteNote(){
        noteLogic.handleMessage("/add New Note");
        noteLogic.handleMessage("/add Deleted Note");
        noteLogic.handleMessage("/del Deleted Note");
        Assertions.assertEquals("Your notes: New Note", noteLogic.handleMessage("/notes"));
    }
}
