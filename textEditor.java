import javax.swing.*;
import java.awt.*;

public class textEditor {
    public static void main(String[] args) {
        // Create the main window (frame)
        JFrame frame = new JFrame("Basic Text Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Add a text area
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea); // Add scroll functionality
        frame.add(scrollPane, BorderLayout.CENTER);

        // Use MenuBarManager
        MenuBarManager menuBarManager = new MenuBarManager(textArea);
        frame.setJMenuBar(menuBarManager.createMenuBar(frame));

        // Show the window
        frame.setVisible(true);
    }
}
