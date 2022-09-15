package ui;

import model.Task;
import model.ToDoList;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

// Task Manager application
public class TaskManagerConsoleUI {
    private static final String JSON_STORE = "./data/todolist.json";
    private ToDoList taskList;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the TaskManager application
    public TaskManagerConsoleUI() {
        runTaskManager();
    }


    // MODIFIES: this
    // EFFECTS: processes user input
    private void runTaskManager() {
        boolean keepGoing = true;
        String command;

        init();

        while (keepGoing) {
            displayMainMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    // MODIFIES: this
    // EFFECTS: initializes to-do list.
    private void init() {
        taskList = new ToDoList();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // EFFECTS: displays current taskList, number of incomplete tasks, number of priority tasks, and options to user.
    private void displayMainMenu() {
        displayTasks();
        displayStats();

        System.out.println("\nSelect from:");
        System.out.println("\ta -> add task");
        System.out.println("\tp -> add priority task");
        System.out.println("\tc -> complete task");
        System.out.println("\td -> delete task");
        System.out.println("\ts -> save task list to file");
        System.out.println("\tl -> load task list from file");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: displays current taskList. Prints "None." if taskList is empty.
    private void displayTasks() {
        List<Task> tasks = taskList.getTaskList();
        int counter = 1;
        System.out.println("\nCurrent Tasks:");
        if (taskList.isEmpty()) {
            System.out.println("\tNone.");
        } else {
            for (Task t : tasks) {
                System.out.println("\t" + counter + ". : " + (t.getDescription()));
                counter++;
            }
        }
    }

    // EFFECTS: displays current number of incomplete and priority task(s) in list.
    private void displayStats() {
        System.out.println("\nNumber of Incomplete Task(s): " + (taskList.numIncompleteTask()));
        System.out.println("Number of Priority Task(s): " + (taskList.numPriorityTask()));
    }

    // MODIFIES: this
    // EFFECTS: process command
    private void processCommand(String command) {
        if (command.equals("a")) {
            addTask(false);
        } else if (command.equals("p")) {
            addTask(true);
        } else if (command.equals("c")) {
            completeTask();
        } else if (command.equals("d")) {
            deleteTask();
        } else if (command.equals("s")) {
            saveToDoList();
        } else if (command.equals("l")) {
            loadToDoList();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: add task with priority status b to taskList.
    private void addTask(boolean b) {
        taskList.addTask(createTask(b));
    }

    // EFFECTS: allow user to create task with priority status b by specifying name and desc.
    private Task createTask(boolean b) {
        return new Task(getTaskDesc(), b);
    }

    // EFFECTS: returns description of task typed by user. (one line max, enter when done typing)
    private String getTaskDesc() {
        System.out.println("\nTask description?");
        return input.next();
    }

    // MODIFIES: this, Task
    // EFFECTS: completes user-selected task from taskList.
    private void completeTask() {
        System.out.println("\nPlease select a Task # to be completed.");
        taskList.markComplete(selectTask());
    }

    // MODIFIES: this
    // EFFECTS: deletes user-selected task from taskList.
    private void deleteTask() {
        System.out.println("\nPlease select a Task # to be deleted.");
        taskList.removeNthTask(selectTask());
    }

    // EFFECTS: prompts user to select task from taskList and returns zero based position of said task.
    private int selectTask() {
        boolean keepGoing = true;
        int position = 0;
        while (keepGoing) {
            boolean isInt = input.hasNextInt();
            String key = input.next();
            if (isInt) {
                int n = Integer.parseInt(key);
                if (1 <= n && n <= taskList.length()) {
                    position = n - 1;
                    keepGoing = false;
                } else {
                    System.out.println("\nTask #" + key + " does not exist.");
                }
            } else {
                System.out.println("\nInput is not a number.");
            }
        }
        return position;
    }

    // EFFECTS: saves the ToDoList to file\
    private void saveToDoList() {
        try {
            jsonWriter.open();
            jsonWriter.write(taskList);
            jsonWriter.close();
            System.out.println("Saved task list to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads ToDoList from file
    private void loadToDoList() {
        try {
            taskList = jsonReader.read();
            System.out.println("Loaded task list from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file " + JSON_STORE);
        }
    }

}