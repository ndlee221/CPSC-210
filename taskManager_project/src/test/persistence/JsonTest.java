package persistence;

import model.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkTask(String desc, boolean priority, boolean isComplete, Task t) {
        assertEquals(desc, t.getDescription());
        assertEquals(priority, t.isPriority());
        assertEquals(isComplete, t.isComplete());
    }
}
