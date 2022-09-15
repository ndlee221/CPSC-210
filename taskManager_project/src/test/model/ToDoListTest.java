package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ToDoListTest {
    ToDoList list;
    ToDoList filledList;
    Task task1;
    Task task2;
    Task task3;
    Task task4;
    Task task5;

    @BeforeEach
    public void setup() {
        list = new ToDoList();
        filledList = new ToDoList();
        task1 = new Task("test", false);
        task2 = new Task("different task", true);
        task3 = new Task("", false);
        task4 = new Task("task", false);
        task5 = new Task("last one", true);
        filledList.addTask(task1);
        filledList.addTask(task2);
        filledList.addTask(task3);

    }

    @Test
    public void testToDoList() {
        assertTrue(list.isEmpty());
        assertEquals(0, list.length());
    }

    @Test
    public void testGetTaskList() {
        ArrayList<Task> expectedList = new ArrayList<>();
        expectedList.add(task2);
        expectedList.add(task1);
        expectedList.add(task3);
        List<Task> actualList = filledList.getTaskList();
        assertEquals(expectedList.size(), actualList.size());
        assertEquals(expectedList.get(0), actualList.get(0));
        assertEquals(expectedList.get(1), actualList.get(1));
        assertEquals(expectedList.get(2), actualList.get(2));
    }

    @Test
    public void testAddTaskNonPrioritySingle() {
        filledList.addTask(task4);
        assertEquals(task4, filledList.getTask(3));
        assertEquals(4, filledList.length());
    }

    @Test
    public void testAddTaskPrioritySingle() {
        filledList.addTask(task5);
        assertEquals(task5, filledList.getTask(1));
        assertEquals(4, filledList.length());

    }

    @Test
    public void testAddTaskMultiple() {
        list.addTask(task1);
        list.addTask(task3);
        list.addTask(task2);
        list.addTask(task5);
        assertEquals(4, list.length());
        assertEquals(task2, list.getTask(0));
        assertEquals(task5, list.getTask(1));
        assertEquals(task1, list.getTask(2));
        assertEquals(task3, list.getTask(3));
    }

    @Test
    public void testRemoveTask() {
        filledList.removeTask(task2);
        checkFilledListWithoutTask2();
    }

    @Test
    public void testRemoveTaskMultiple() {
        filledList.removeTask(task1);
        filledList.removeTask(task3);
        checkFilledListOnlyTask2();
    }

    @Test
    public void testRemoveTaskNotInList() {
        checkFilledList();
        filledList.removeTask(new Task("", false));
        checkFilledList();
    }

    @Test
    public void testRemoveNthTask() {
        filledList.removeNthTask(0);
        checkFilledListWithoutTask2();
    }

    @Test
    public void testRemoveNthTaskMultiple() {
        filledList.removeNthTask(1);
        filledList.removeNthTask(1);
        checkFilledListOnlyTask2();
    }

    @Test
    public void testMarkComplete() {
        Task taskToComplete = filledList.getTask(0);
        assertFalse(taskToComplete.isComplete());
        filledList.markComplete(0);
        assertTrue(taskToComplete.isComplete());
    }

    @Test
    public void testMarkCompleteAlreadyComplete() {
        Task complete = filledList.getTask(0);
        assertFalse(complete.isComplete());
        filledList.markComplete(0);
        assertTrue(complete.isComplete());
        filledList.markComplete(0);
        assertTrue(complete.isComplete());
    }

    @Test
    public void testGetTask() {
        checkFilledList();
    }

    @Test
    public void testIsEmpty() {
        assertTrue(list.isEmpty());
        assertFalse(filledList.isEmpty());
    }

    @Test
    public void testContains() {
        assertFalse(list.contains(task2));
        assertTrue(filledList.contains(task2));
    }

    @Test
    public void testLength() {
        assertEquals(0, list.length());
        assertEquals(3, filledList.length());
        list.addTask(task1);
        assertEquals(1, list.length());
    }

    @Test
    public void testNumIncompleteTask() {
        assertEquals(3, filledList.numIncompleteTask());
        filledList.markComplete(1);
        assertEquals(2, filledList.numIncompleteTask());
    }

    @Test
    public void testNumPriorityTask() {
        assertEquals(1, filledList.numPriorityTask());
        filledList.addTask(task5);
        assertEquals(2, filledList.numPriorityTask());
    }

    // EFFECTS: checks if filledList only contains task1, task3, and does not contain task2
    private void checkFilledListWithoutTask2() {
        assertEquals(2, filledList.length());
        assertEquals(task1, filledList.getTask(0));
        assertEquals(task3, filledList.getTask(1));
        assertFalse(filledList.contains(task2));
    }

    // EFFECTS: checks if filledList only contains task2
    private void checkFilledListOnlyTask2() {
        assertEquals(1, filledList.length());
        assertEquals(task2, filledList.getTask(0));
        assertFalse(filledList.contains(task1));
        assertFalse(filledList.contains(task3));
    }

    // EFFECTS: checks if fillList contains task2, task1, task3 in this order and size 3.
    private void checkFilledList() {
        assertEquals(task2, filledList.getTask(0));
        assertEquals(task1, filledList.getTask(1));
        assertEquals(task3, filledList.getTask(2));
        assertEquals(3, filledList.length());
    }
}
