package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import App.Client;
import DataTransfer.DataTransfer;

public class ClientGUI extends JFrame {
    private DataTransfer dataTransfer;
    private Client client;

    private JPanel usernamePanel;
    private JTextField usernameField;
    private JButton usernameButton;
    private JButton leaveButton;
    private JTextField textField;
    private JButton sendButton;
    private JButton cancelButton;
    private JTextArea resultArea;
    private JPanel textPanel;
    private JPanel buttonsPanel;
    private JPanel resultPanel;

    public ClientGUI(DataTransfer dataTransfer, Client client) {
        this.dataTransfer = dataTransfer;
        this.client = client;
        CreateView();
        Show();
        AttachEventHandler();
        ReceiveChat();
    }

    public void Show() {
        this.setVisible(true);
    }

    private void CreateView() {

        this.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        this.setMinimumSize(new Dimension(400, 500));
        this.setPreferredSize(new Dimension(400, 500));

        usernamePanel = new JPanel();
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(120, 30));
        usernameButton = new JButton();
        usernameButton.setText("Enter username");
        leaveButton = new JButton();
        leaveButton.setText("Leave");

        usernamePanel.add(usernameField);
        usernamePanel.add(usernameButton);
        usernamePanel.add(leaveButton);

        resultPanel = new JPanel();
        resultPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        resultPanel.setMinimumSize(new Dimension(400, 300));
        resultPanel.setPreferredSize(new Dimension(400, 300));

        resultArea = new JTextArea(16, 32);
        resultArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        resultArea.setEditable(false);
        resultArea.setWrapStyleWord(true);
        JScrollPane sp = new JScrollPane(resultArea);
        resultPanel.add(sp);

        textPanel = new JPanel();
        textPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        textPanel.setPreferredSize(new Dimension(400, 40));

        textField = new JTextField();
        textField.setPreferredSize(new Dimension(320, 30));
        textPanel.add(textField);

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonsPanel.setPreferredSize(new Dimension(180, 40));
        sendButton = new JButton();
        sendButton.setText("Send");
        buttonsPanel.add(sendButton);

        cancelButton = new JButton();
        cancelButton.setText("Clear chat");
        buttonsPanel.add(cancelButton);

        this.add(usernamePanel);
        this.add(resultPanel);
        this.add(textPanel);
        this.add(buttonsPanel);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void AttachEventHandler() {
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (client.getUsername() != null) {
                    String text = textField.getText();
                    try {
                        dataTransfer.send(client.getUsername() + ": " + text);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    textField.setText("");
                } else {
                    resultArea.append("Please enter your username.\n");
                }

            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultArea.setText("");
            }
        });

        usernameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.setUsername(usernameField.getText());
                usernameField.setEditable(false);
            }
        });

        leaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataTransfer.send("exit " + client.getUsername());
                try {
                    dataTransfer.socket.shutdownOutput();
                } catch (IOException e1) {
                    System.err.println("Cannot shutdown output.");
                    e1.printStackTrace();
                }
            }
        });
    }

    public void ReceiveChat() {
        while (true) {
            String res = dataTransfer.receive();
            if (res.equals(client.getUsername() + " leave the conversation.")
                    && dataTransfer.socket.isOutputShutdown()) {
                dataTransfer.close();
                dispose();
            } else
                resultArea.append(res + "\n");
        }
    }
}
