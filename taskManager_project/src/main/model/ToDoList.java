package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents a to-do list of Tasks.
public class ToDoList implements Writable {
    private List<Task> taskList;


    // EFFECTS: creates an empty to-do list.
    public ToDoList() {
        taskList = new ArrayList<>();
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    // MODIFIES: this
    // EFFECTS: if task is non-priority, add to end of taskList.
    //          if task is priority add after last priority task (before first non-priority task) in taskList.
    public void addTask(Task task) {
        if (task.isPriority()) {
            int pos = numPriorityTask();
            taskList.add(pos, task);
            EventLog.getInstance().logEvent(new Event("Priority task item added to task list."));
        } else {
            taskList.add(task);
            EventLog.getInstance().logEvent(new Event("Task item added to task list."));
        }
    }

    // MODIFIES: this
    // EFFECTS: removes task from taskList,
    //          if task exists, it is removed (rest after removed task moves up in position to fill gap)
    //          if task does not exist, nothing is changed.
    public void removeTask(Task task) {
        taskList.remove(task);
        EventLog.getInstance().logEvent(new Event("Task item removed from task list."));
    }

    // REQUIRES: 0 <= n < length of taskList.
    // MODIFIES: this
    // EFFECTS: removes the n-th position (0-based) task in taskList.
    public void removeNthTask(int n) {
        taskList.remove(n);
        EventLog.getInstance().logEvent(new Event("Task item removed from task list."));
    }

    // REQUIRES: 0 <= n < length of taskList
    // MODIFIES: Task
    // EFFECTS: sets n-th position (0-based) task in taskList to completed status.
    public void markComplete(int n) {
        Task nthTask = taskList.get(n);
        nthTask.setIsComplete(true);
        EventLog.getInstance().logEvent(new Event("Completed a task item from task list."));
    }

    // REQUIRES 0 < n < length of taskList
    // EFFECTS: returns Task in position(0-based) n of taskList.
    public Task getTask(int n) {
        return taskList.get(n);
    }


    // EFFECTS: returns true if taskList is empty. False otherwise
    public boolean isEmpty() {
        return taskList.isEmpty();
    }

    // EFFECTS: returns true if taskList contains task. False otherwise
    public boolean contains(Task task) {
        return taskList.contains(task);
    }

    // EFFECTS: returns the TOTAL number of tasks in taskList
    public int length() {
        return taskList.size();
    }

    // EFFECTS: returns the number of incomplete task in taskList
    public int numIncompleteTask() {
        int num = 0;
        for (Task t : taskList) {
            if (!(t.isComplete())) {
                num++;
            }
        }
        return num;
    }

    // EFFECTS: returns the number of priority tasks in taskList
    public int numPriorityTask() {
        int num = 0;
        for (Task t : taskList) {
            if (t.isPriority()) {
                num++;
            }
        }
        return num;
    }


    // EFFECTS: returns task list as a JSON object.
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("list", listToJson());
        return json;
    }

    // EFFECTS: returns tasks in this taskList as a JSON array
    private JSONArray listToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Task t : taskList) {
            jsonArray.put(t.toJson());
        }

        return jsonArray;
    }
}
