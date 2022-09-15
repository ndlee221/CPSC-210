package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    Task task1;

    @BeforeEach
    public void setup() {
        task1 = new Task("test", false);
    }


    @Test
    public void testSetDescription() {
        assertEquals("test", task1.getDescription());
        task1.setDescription("Different desc.");
        assertEquals("Different desc.", task1.getDescription());
    }

    @Test
    public void testSetIsPriority() {
        assertFalse(task1.isPriority());
        task1.setIsPriority(true);
        assertTrue(task1.isPriority());
    }

    @Test
    public void testSetIsComplete() {
        assertFalse(task1.isComplete());
        task1.setIsComplete(true);
        assertTrue(task1.isComplete());
    }
}