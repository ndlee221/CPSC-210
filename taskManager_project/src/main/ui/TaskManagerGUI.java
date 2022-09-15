package ui;

import model.Task;
import model.ToDoList;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

// represents a task manager GUI application
public class TaskManagerGUI extends JFrame implements ListSelectionListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final String JSON_STORE = "./data/todolist.json";
    private ToDoList toDoList;
    private JPanel rightPanel;
    private JPanel controlPanel;
    private JSplitPane splitPane;
    private JList list;
    private DefaultListModel listModel;

    // EFFECTS: shows splash screen, then sets up main window composed of control panel,
    //          progress panel, and task list panel. Displays main window.
    public TaskManagerGUI() {
        super("Task Manager");
        displaySplash();
        toDoList = new ToDoList();
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        splitPane = new JSplitPane();
        splitPane.setRightComponent(rightPanel);

        addControlPanel();
        addProgressPanel();
        addTaskListPanel();

        addWindowListener(new CustomWindowListener());
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        splitPane.setOpaque(true);
        setContentPane(splitPane);
        pack();
        splitPane.setDividerLocation(0.5);
        centreOnScreen();
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: adds control panel with buttons to main window.
    private void addControlPanel() {
        controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(6, 1));
        controlPanel.add(new JButton(new AddTaskAction()));
        controlPanel.add(new JButton(new RemoveTaskAction()));
        controlPanel.add(new JButton(new CompleteTaskAction()));
        controlPanel.add(new JButton(new DisplayIncompleteTaskAction()));
        controlPanel.add(new JButton(new SaveFileAction()));
        controlPanel.add(new JButton(new LoadFileAction()));
        rightPanel.add(controlPanel, BorderLayout.NORTH);
    }

    // MODIFIES: this
    // EFFECTS: adds progress panel to main window.
    private void addProgressPanel() {
        JPanel progressPanel = new JPanel();
        rightPanel.add(progressPanel, BorderLayout.SOUTH);
    }

    // MODIFIES: this
    // EFFECTS: adds task list panel to main window.
    private void addTaskListPanel() {
        list = new JList();
        listModel = new DefaultListModel();
        list.setModel(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(this);
        JScrollPane listScrollPane = new JScrollPane(list);
        splitPane.setLeftComponent(listScrollPane);
    }

    // Image source: https://www.marketing91.com/wp-content/uploads/2020/03/To-do-List-scaled.jpg
    // EFFECTS: displays startup splashscreen temporarily.
    private void displaySplash() {
        JWindow window = new JWindow();
        ImageIcon icon = (new ImageIcon("./data/splash.jpg", String.valueOf(SwingConstants.CENTER)));
        Image img = icon.getImage();
        Image scaled = img.getScaledInstance(300, 200, 0);
        icon = new ImageIcon(scaled);
        window.getContentPane().add(new JLabel(icon));
        window.setBounds(810, 490, 300, 200);
        window.setVisible(true);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        window.setVisible(false);
    }


    // MODIFIES: this
    // EFFECTS: set frame location to center of desktop
    private void centreOnScreen() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);
    }

    // MODIFIES: this
    // EFFECTS: updates TaskListPanel to reflect changes to ToDoList
    private void updateTaskListPanel() {
        listModel.removeAllElements();
        for (Task t : toDoList.getTaskList()) {
            listModel.addElement(t.getDescription());
        }
    }


    // Represents action to be taken when user wants to add new task to task list.
    private class AddTaskAction extends AbstractAction {

        // EFFECTS: creates Action with name.
        AddTaskAction() {
            super("Add task");
        }

        // MODIFIES: this
        // EFFECTS: add task to task list when action is triggered.
        @Override
        public void actionPerformed(ActionEvent e) {
            String taskDesc = JOptionPane.showInputDialog(null,
                    "New Task?",
                    "Enter task description",
                    JOptionPane.QUESTION_MESSAGE);
            if (taskDesc != null) {
                Task t = new Task(taskDesc, false);
                toDoList.addTask(t);
                updateTaskListPanel();
            }

        }
    }

    // represents action to be taken when user wants to remove task from task list
    private class RemoveTaskAction extends AbstractAction {
        // EFFECTS: creates Action with name
        RemoveTaskAction() {
            super("Remove task");
        }

        // MODIFIES: this
        // EFFECTS: removes selected task from task list
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = list.getSelectedIndex();
            if (index == -1) {
                JOptionPane.showMessageDialog(null,
                        "Please select a task to remove.",
                        "System Error: No task selected",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                toDoList.removeNthTask(index);
                updateTaskListPanel();
            }
        }
    }

    // represents action to be taken when user wants to complete a task
    private class CompleteTaskAction extends AbstractAction {
        // EFFECTS: creates Action with name.
        CompleteTaskAction() {
            super("Complete task");
        }

        // MODIFIES: this
        // EFFECTS: completes selected task.
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = list.getSelectedIndex();
            if (index == -1) {
                JOptionPane.showMessageDialog(null,
                        "Please select a task to complete.",
                        "System Error: No task selected",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                toDoList.markComplete(index);
            }
        }
    }

    // represents action to be taken when user wants to display only incomplete tasks.
    private class DisplayIncompleteTaskAction extends AbstractAction {
        // EFFECTS: creates Action with name
        DisplayIncompleteTaskAction() {
            super("Display only incomplete tasks");
        }

        // EFFECTS: displays only incomplete tasks in task list.
        @Override
        public void actionPerformed(ActionEvent e) {
            listModel.removeAllElements();
            for (Task t : toDoList.getTaskList()) {
                if (!t.isComplete()) {
                    listModel.addElement(t.getDescription());
                }
            }
            controlPanel.remove(3);
            controlPanel.add(new JButton(new DisplayAllTasks()), 3);
            splitPane.setRightComponent(rightPanel);
            splitPane.setDividerLocation(0.5);
        }
    }

    // represents action to be taken when user wants to display all tasks
    private class DisplayAllTasks extends AbstractAction {
        // EFFECTS: creates Action with name
        DisplayAllTasks() {
            super("Display all tasks");
        }

        // MODIFIES: this
        // EFFECTS: displays all tasks in task list.
        @Override
        public void actionPerformed(ActionEvent e) {
            listModel.removeAllElements();
            for (Task t : toDoList.getTaskList()) {
                listModel.addElement(t.getDescription());
            }
            controlPanel.remove(3);
            controlPanel.add(new JButton(new DisplayIncompleteTaskAction()), 3);
            splitPane.setRightComponent(rightPanel);
            splitPane.setDividerLocation(0.5);
        }
    }

    // represents action to be taken when user wants to save task list to file.
    private class SaveFileAction extends AbstractAction {
        // EFFECTS: creates Action with name
        SaveFileAction() {
            super("Save task list to file");
        }

        // MODIFIES: this
        // EFFECTS: saves task list to file in JSON representation.
        @Override
        public void actionPerformed(ActionEvent e) {
            JsonWriter writer = new JsonWriter(JSON_STORE);
            try {
                writer.open();
                writer.write(toDoList);
                writer.close();
                JOptionPane.showMessageDialog(null,
                        "Task list was successfully saved to: " + JSON_STORE + ".",
                        "Save File",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (FileNotFoundException exception) {
                JOptionPane.showMessageDialog(null,
                        "Task list could not be saved to: " + JSON_STORE + ".",
                        "Save File Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // represents action to be taken when user wants to load task list from file.
    private class LoadFileAction extends AbstractAction {
        // EFFECTS: creates Action with name
        LoadFileAction() {
            super("Load task list from file");
        }

        // MODIFIES: this
        // EFFECTS: loads task list from JSON data from file.
        @Override
        public void actionPerformed(ActionEvent e) {
            JsonReader reader = new JsonReader(JSON_STORE);
            try {
                toDoList = reader.read();
                listModel.clear();
                for (Task t : toDoList.getTaskList()) {
                    listModel.addElement(t.getDescription());
                }
                JOptionPane.showMessageDialog(null,
                        "Task list was successfully loaded from: " + JSON_STORE + ".",
                        "Load File",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(null,
                        "Task list could not be loaded from: " + JSON_STORE + ".",
                        "Load File Failed",
                        JOptionPane.ERROR_MESSAGE);
            }

        }
    }


    /**
     * Called whenever the value of the selection changes.
     *
     * @param e the event that characterizes the change.
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
    }

    // runs the TaskManager application.
    public static void main(String[] args) {
        new TaskManagerGUI();
    }
}
