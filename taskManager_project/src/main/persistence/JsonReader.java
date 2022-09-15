package persistence;

import model.Task;
import model.ToDoList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads ToDoList from JSON data stored in file.
// modelled after JsonSerializationDemo sample application.
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads ToDoList from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ToDoList read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseToDoList(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses ToDoList from JSON object and returns it
    private ToDoList parseToDoList(JSONObject jsonObject) {
        ToDoList list = new ToDoList();
        addTasks(list, jsonObject);
        return list;
    }

    // MODIFIES: ToDoList
    // EFFECTS: parses Tasks from JSON object and adds them to ToDoList
    private void addTasks(ToDoList list, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        for (Object json : jsonArray) {
            JSONObject nextTask = (JSONObject) json;
            addTask(list, nextTask);
        }
    }

    // MODIFIES: ToDoList
    // EFFECTS: parses Task from JSON object and adds it to ToDoList
    private void addTask(ToDoList list, JSONObject jsonObject) {
        String desc = jsonObject.getString("description");
        boolean isPriority = jsonObject.getBoolean("priority");
        boolean isComplete = jsonObject.getBoolean("completed");
        Task task = new Task(desc, isPriority);
        task.setIsComplete(isComplete);
        list.addTask(task);
    }
}
