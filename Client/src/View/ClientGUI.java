package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import App.Client;

public class ClientGUI extends JFrame {
    private Client client;

    // Components
    private JPanel usernamePanel;
    private JTextField usernameField;
    private JButton connectButton;
    private JButton leaveButton;
    private JTextField textField;
    private JButton sendButton;
    private JButton cancelButton;
    private JTextArea resultArea;
    private JPanel textPanel;
    private JPanel buttonsPanel;
    private JPanel resultPanel;

    public ClientGUI(Client client) {
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
        connectButton = new JButton();
        connectButton.setText("Connect");
        leaveButton = new JButton();
        leaveButton.setText("Leave");
        leaveButton.setEnabled(false);

        usernamePanel.add(usernameField);
        usernamePanel.add(connectButton);
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
        textField.setEditable(false);
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
                        client.send(client.getUsername() + ": " + text);
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

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (usernameField.getText().equals("")) {
                    resultArea.append("Please enter your username.\n");
                } else {
                    client.setUsername(usernameField.getText());
                    usernameField.setEditable(false);
                    if (client.connect()) {
                        resultArea.append("Connect to server successfully. Start chat now.\n");
                        // client.send("enter " + client.getUsername());

                        connectButton.setEnabled(false);
                        leaveButton.setEnabled(true);
                        textField.setEditable(true);
                    } else {
                        resultArea.append("Cannot connect to server. Try again.\n");
                    }
                }
            }
        });

        leaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.send("exit " + client.getUsername());
                // try {
                // client.socket.shutdownOutput();
                // } catch (IOException e1) {
                // System.err.println("Cannot shutdown output.");
                // e1.printStackTrace();
                // }
            }
        });
    }

    public void ReceiveChat() {
        while (true) {
            if (client.isConnected()) {
                String res = client.receive();
                if (res.equals(client.getUsername() + " leave the conversation.")) {
                    client.stop();
                    dispose();
                } else
                    resultArea.append(res + "\n");
            } else {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }
    }
}
