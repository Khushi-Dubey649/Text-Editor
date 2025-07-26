import javax.swing.*;
import java.util.Stack;

public class MenuBarManager {
    private JTextArea textArea;
    private Stack<String> undoStack = new Stack<>();
    private Stack<String> redoStack = new Stack<>();
    private Stack<String> sStack = new Stack<>();

    public MenuBarManager(JTextArea textArea) {
        this.textArea = textArea;
        textArea.getDocument().addUndoableEditListener(e -> {
            undoStack.push(textArea.getText());
            redoStack.clear();
        });
    }

    public JMenuBar createMenuBar(JFrame frame) {
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem newFile = new JMenuItem("New");
        JMenuItem openFile = new JMenuItem("Open");
        JMenuItem saveFile = new JMenuItem("Save");

        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        menuBar.add(fileMenu);

        // Edit Menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem undo = new JMenuItem("Undo");
        JMenuItem redo = new JMenuItem("Redo");
        JMenuItem cut = new JMenuItem("Cut");
        JMenuItem copy = new JMenuItem("Copy");
        JMenuItem paste = new JMenuItem("Paste");

        editMenu.add(undo);
        editMenu.add(redo);
        editMenu.addSeparator();
        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);
        menuBar.add(editMenu);

        // File Menu Actions
        newFile.addActionListener(e -> textArea.setText(""));
        openFile.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                try {
                    java.nio.file.Path filePath = fileChooser.getSelectedFile().toPath();
                    String content = java.nio.file.Files.readString(filePath);
                    textArea.setText(content);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error opening file!");
                }
            }
        });

        saveFile.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                try {
                    java.nio.file.Path filePath = fileChooser.getSelectedFile().toPath();
                    java.nio.file.Files.writeString(filePath, textArea.getText());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error saving file!");
                }
            }
        });

        // Edit Menu Actions
        undo.addActionListener(e -> {
            String allText = textArea.getText();
            StringBuilder lastWordR = new StringBuilder();
            for(int i=allText.length()-1; i>=0; i--) {
                if(allText.charAt(i) == ' ') {
                    break;
                }
                lastWordR.append(allText.charAt(i));
            }
            StringBuilder lastWord = new StringBuilder();
            sStack.push(new String(lastWord));
            for(int i=lastWordR.length()-1; i>=0; i--) {
                lastWord.append(lastWordR.charAt(i));
            }
            // System.out.println(lastWord);
            sStack.push(new String(lastWord));
            textArea.setText("");
            StringBuilder screenTextUpdate = new StringBuilder();
            for(int i=0; i<=(allText.length()-lastWord.length())-1; i++) {
                screenTextUpdate.append(allText.charAt(i));
            }
            String toUpdate = new String(screenTextUpdate);
            textArea.setText(toUpdate.stripTrailing());
            System.out.println("xxxxxxxxxxxxxxxxxxxxxx");
            System.out.println(sStack.peek());
        });

        redo.addActionListener(e -> {
            System.err.println("redo pressed");
            System.err.println("peak element : "+sStack.peek());
            if(sStack.peek().length() == 0) {
                sStack.pop();
                textArea.append(" " + sStack.pop());
            } else {
                textArea.append(" " + sStack.pop());
            }
        });

        // Adding the Menu Bar to the Frame
        frame.setJMenuBar(menuBar);

        // Frame settings (size, close operation, visibility)
        frame.setSize(500, 400); // Set the frame size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the application when the window is closed
        frame.setVisible(true); // Make the frame visible

        return menuBar;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Text Editor");
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);

        MenuBarManager menuManager = new MenuBarManager(textArea);
        menuManager.createMenuBar(frame);
    }
}
