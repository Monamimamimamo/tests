package example.note;

import example.note.NoteLogic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NoteLogicTest {

    NoteLogic noteLogic = new NoteLogic();

    /**
     * Тест проверяет добавление записи
     */
    @Test
    public void testAddNewNote(){
        String commandResult = noteLogic.handleMessage("/add New Note");
        Assertions.assertEquals(commandResult, "Note added!");
        String result = noteLogic.handleMessage("/notes");
        Assertions.assertEquals("Your notes: New Note", result);
    }


    /**
     * Тест проверяет корректный вывод всех добавленных записей
     */
    @Test
    public void testGetAllNotes(){
        String result1 = noteLogic.handleMessage("/notes");
        Assertions.assertEquals(result1, "Your notes:");
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
        Assertions.assertEquals(commandResult, "Note edited!");
        String result = noteLogic.handleMessage("/notes");
        Assertions.assertEquals("Your notes: Edited Note", result);
    }


    /**
     * Тест проверяет удаление существующей записи
     */
    @Test
    public void testDeleteNote(){
        String commandResult = noteLogic.handleMessage("/del New Note");
        Assertions.assertEquals("No note found!", commandResult);
    }
}
