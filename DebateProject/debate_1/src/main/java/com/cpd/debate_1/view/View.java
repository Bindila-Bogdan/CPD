package com.cpd.debate_1.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class View {
    private final JFrame frame;
    private JPanel primaryPanel, secondaryPanel;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JTextField textField;
    private JButton sendButton;

    public View(String frameName) {
        frame = new JFrame(frameName);
        frame.setResizable(false);
        setupFrame(480, 480);
    }

    public void setupFrame(int width, int height) {
        primaryPanel = new JPanel();
        primaryPanel.setLayout(new BoxLayout(primaryPanel, BoxLayout.Y_AXIS));

        textArea = new JTextArea();
        textArea.setFont(new Font("Sans", Font.BOLD, 14));
        scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        primaryPanel.add(scrollPane);

        secondaryPanel = new JPanel();
        secondaryPanel.setLayout(new BoxLayout(secondaryPanel, BoxLayout.X_AXIS));

        textField = new JTextField();
        textField.setMaximumSize(new Dimension(400, 36));
        textField.setFont(new Font("Sans", Font.BOLD, 14));
        secondaryPanel.add(textField);

        sendButton = new JButton("Send message");
        sendButton.setBackground(Color.WHITE);

        secondaryPanel.add(sendButton);
        secondaryPanel.setBorder(new EmptyBorder(4, 0, 4, 0));

        primaryPanel.add(secondaryPanel);
        primaryPanel.setBorder(new EmptyBorder(4, 4, 0, 4));

        frame.setContentPane(primaryPanel);
        primaryPanel.setVisible(true);
        frame.setSize(width, height);
    }

    public JFrame getFrame() {
        return frame;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public JTextField getTextField() {
        return textField;
    }

    public JButton getSendButton() {
        return sendButton;
    }
}
