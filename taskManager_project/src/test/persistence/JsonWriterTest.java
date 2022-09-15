package persistence;

import model.Task;
import model.ToDoList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            ToDoList list = new ToDoList();
            JsonWriter writer = new JsonWriter("./data/illegal\0filename.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyToDoList() {
        try {
            ToDoList list = new ToDoList();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyToDoList.json");
            writer.open();
            writer.write(list);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyToDoList.json");
            list = reader.read();
            assertTrue(list.isEmpty());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralList() {
        try {
            ToDoList list = new ToDoList();
            list.addTask(new Task( "handsome", false));
            Task t2 = new Task("short", true);
            t2.setIsComplete(true);
            list.addTask(t2);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralToDoList.json");
            writer.open();
            writer.write(list);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralToDoList.json");
            list = reader.read();
            assertEquals(2, list.length());
            checkTask( "short", true, true, list.getTask(0));
            checkTask( "handsome", false, false, list.getTask(1));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
