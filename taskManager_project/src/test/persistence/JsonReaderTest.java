package persistence;

import model.ToDoList;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/dne.json");
        try {
            ToDoList list = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyToDoList() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyToDoList.json");
        try {
            ToDoList list = reader.read();
            assertEquals(0, list.length());
        } catch (IOException e) {
            fail("Couldn't read file");
        }
    }

    @Test
    void testReaderGeneralToDoList() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralToDoList.json");
        try {
            ToDoList list = reader.read();
            assertEquals(2, list.length());
            checkTask("short", false, false, list.getTask(0));
            checkTask("handsome", false, true, list.getTask(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
