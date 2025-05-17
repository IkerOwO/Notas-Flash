package utils;
import java.awt.*;
import java.util.List;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import javax.swing.*;


public class Front extends JFrame{
    final JButton submitButton, themeButton, checkButton, deleteButton; //exitButton;
    final JLabel textLabel, confirmLabel, errorLabel, noTasks, deleteLabel;
    final JTextField textField, deleteField; 
    final JTextArea readArea;
    public boolean toggled = false;
    public boolean Deletetoggled = false;

    public Front(){
        setLayout(null);
        // textLabel
        textLabel = new JLabel("Enter a Task");
        textLabel.setBounds(220, 90, 300, 30);
        textLabel.setForeground(Color.BLACK);
        add(textLabel);

        // deleteLabel
        deleteLabel = new JLabel("Delete a Task");
        deleteLabel.setBounds(220, 90, 300, 30);
        deleteLabel.setForeground(Color.BLACK);
        add(deleteLabel);
        deleteLabel.setVisible(false);

        // confirmLabel
        confirmLabel = new JLabel("Task Confirmed");
        confirmLabel.setBounds(200,490,300,30);
        confirmLabel.setForeground(Color.GREEN);
        confirmLabel.setVisible(false);
        add(confirmLabel);

        // errorLabel
        errorLabel = new JLabel("Text Field can't be empty");
        errorLabel.setBounds(200,490,300,30);
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);
        add(errorLabel);

        // textField
        textField = new JTextField();
        textField.setBounds(160,150,200,40);
        add(textField);

        // DeleteField
        deleteField = new JTextField();
        deleteField.setBounds(160,150,200,40);
        add(deleteField);
        deleteField.setVisible(false);

        // themeButton
        themeButton = new JButton("Theme");
        themeButton.setBounds(450,0,90,20);
        themeButton.setBorder(null);
        themeButton.setBackground(Color.WHITE);
        add(themeButton);
        // themeButton Actionlistener
        themeButton.addActionListener(this::ChangeTheme);

        // submitButton
        submitButton = new JButton("Submit");
        submitButton.setBounds(200,300,120,50);
        submitButton.setBackground(Color.WHITE);
        add(submitButton);
        // submitButton Actionlistener
        submitButton.addActionListener(this::clickEnBoton);
        
        // checkButton
        checkButton = new JButton("View Tasks");
        checkButton.setBounds(200,370,120,50);
        checkButton.setBackground(Color.WHITE);
        add(checkButton);
        // checkButton Actionlistener
        checkButton.addActionListener(this::readbutton);

        // deleteButton
        deleteButton = new JButton("Delete Task");
        deleteButton.setBounds(200,440,120,50);
        deleteButton.setBackground(Color.WHITE);
        add(deleteButton);
        // deleteButton ActionListener
        deleteButton.addActionListener(this::deleteButton);

        /* 
        // exitButton
        exitButton = new JButton("Exit");
        exitButton.setBounds(200,440,120,50);
        exitButton.setBackground(Color.WHITE);
        add(exitButton);
        // exitButton Actionlistener
        exitButton.addActionListener(this::exitbutton);
        */

        // noTasks
        noTasks = new JLabel("There aren't any Tasks");
        noTasks.setBounds(200,120,300,30);
        noTasks.setVisible(false);
        add(noTasks);

        // readArea
        readArea = new JTextArea();
        readArea.setEditable(false);
        readArea.setBounds(160,120,200,100);
        readArea.setVisible(false);
        add(readArea);

        // Icon
        setIconImage(new ImageIcon("src\\app\\assets\\icon.png").getImage());
    }

    private int taskCount = 0; // Counter to keep track of the number of tasks
    public void write() {
        try {
            String text = textField.getText();
            taskCount++; // Increment the task count
            String formattedTask = taskCount + ". " + text; // Format the task with the number
            try (FileWriter writer = new FileWriter("Tasks.txt", true)) {
                writer.write(formattedTask);
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public void read(){
        try(BufferedReader br = new BufferedReader(new FileReader("Tasks.txt"))){
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null){
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String todo = sb.toString();
            readArea.setText(todo);
            // If the tasks.txt file is empty
            if (todo.isEmpty()){
                noTasks.setVisible(true);
            } else if (!todo.isEmpty()){
                noTasks.setVisible(false);
            }
            br.close();
        } catch (IOException e) {
            System.err.println("Error writing to file" + e.getMessage());
        }
    }

    public void delete(int lineNumber) {
    try {
        // Read lines
        List<String> lines = Files.readAllLines(Paths.get("Tasks.txt"));
        // Check if the line number is valid
        if (lineNumber < 1 || lineNumber > lines.size()) {
            System.err.println("Line number incorrect");
            return;
        }
        // Remove the task at the specified line
        lines.remove(lineNumber - 1);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Tasks.txt"))) {
            for (int i = 0; i < lines.size(); i++) {
                String taskContent = lines.get(i);
                // Remove old numbering, Split
                String[] split = taskContent.split("\\. ", 2);
                // Write new line with correct number
                String newLine = (i + 1) + ". " + (split.length > 1 ? split[1] : taskContent);
                // write the file with the updated list
                writer.write(newLine);
                writer.newLine();
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading or writing to file: " + e.getMessage());
    }
}

    public void deleteButton(ActionEvent e){
        if (!Deletetoggled) {
        // Switch to delete mode
        textLabel.setVisible(false);
        textField.setVisible(false);
        readArea.setVisible(false);
        deleteLabel.setVisible(true);
        deleteField.setVisible(true);
        deleteButton.setText("Confirm Delete");
    } else {
        try {
            int tasksToDelete = Integer.parseInt(deleteField.getText());
            delete(tasksToDelete);
            confirmLabel.setText("Task Deleted");
            confirmLabel.setVisible(true);
        } catch (NumberFormatException ex) {
            errorLabel.setText("Invalid input for deletion");
            errorLabel.setVisible(true);
        }
        deleteField.setText(""); // Clear input
        textLabel.setVisible(true);
        textField.setVisible(true);
        deleteLabel.setVisible(false);
        deleteField.setVisible(false);
        deleteButton.setText("Delete Task");
    }
    Deletetoggled = !Deletetoggled;
    }

    public void exitbutton(ActionEvent e){
        System.exit(0);
    }

    public void clickEnBoton(ActionEvent e){
        // Check if there is a task written on the text box
        String verifyInput = textField.getText();
        if (verifyInput.isEmpty()){
            errorLabel.setVisible(true);
            confirmLabel.setVisible(false);
        } else{
            write();
            confirmLabel.setVisible(true);
            errorLabel.setVisible(false);
        }
    } 

    public void ChangeTheme(ActionEvent e){
        // Obtain the color of the Background
        Color getBackground = getContentPane().getBackground();
        if(getBackground == Color.WHITE){
            getContentPane().setBackground(Color.DARK_GRAY); // App Background
            themeButton.setBackground(Color.DARK_GRAY);
            themeButton.setForeground(Color.WHITE);
            readArea.setBackground(Color.DARK_GRAY);
            textLabel.setForeground(Color.WHITE);
            deleteLabel.setForeground(Color.WHITE);
            readArea.setForeground(Color.WHITE);
            noTasks.setForeground(Color.WHITE);
        } else {
            themeButton.setBackground(Color.WHITE);
            themeButton.setForeground(Color.BLACK);
            getContentPane().setBackground(Color.WHITE);
            readArea.setBackground(Color.WHITE);
            readArea.setForeground(Color.BLACK);
            noTasks.setForeground(Color.BLACK);
        }
    } 

    public void readbutton(ActionEvent e){
        if(toggled){
            readArea.setVisible(false);
            textLabel.setVisible(true);
            textField.setVisible(true);
            noTasks.setVisible(false);
        } else{
            readArea.setVisible(true);
            textLabel.setVisible(false);
            textField.setVisible(false);
            read(); 
        }
        toggled = !toggled; // Change toggle
    } 
}

