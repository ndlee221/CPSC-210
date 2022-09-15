package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a task having a description, priority status, and completion status.
public class Task implements Writable {
    private String description;     //description of task
    private boolean isPriority;       //true if priority, false if normal.
    private boolean isComplete;      //true if complete, false if incomplete


    // EFFECTS: creates a task with description, and priority status. Task will be incomplete.
    public Task(String description, Boolean priority) {
        this.description = description;
        isPriority = priority;
        isComplete = false;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPriority() {
        return isPriority;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setIsPriority(boolean p) {
        this.isPriority = p;
    }

    public void setIsComplete(boolean c) {
        this.isComplete = c;
    }

    // EFFECTS: returns task as JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("description", description);
        json.put("priority", isPriority);
        json.put("completed", isComplete);
        return json;
    }
}
