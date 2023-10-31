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

public class ServerGUI extends JFrame {
    // private DataTransfer dataTransfer;
    private Server server;

    private JPanel operationPanel;
    private JButton startButton;
    private JButton stopButton;
    private JTextArea logArea;
    private JPanel logPanel;

    public ServerGUI(Server server) {
        this.server = server;
        CreateView();
        stopButton.setEnabled(false);
        Show();
        AttachEventHandler();
        // ReceiveChat();
    }

    public void Show() {
        this.setVisible(true);
    }

    private void CreateView() {

        this.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        this.setMinimumSize(new Dimension(400, 500));
        this.setPreferredSize(new Dimension(400, 500));

        operationPanel = new JPanel();
        startButton = new JButton();
        startButton.setText("Start server");
        stopButton = new JButton();
        stopButton.setText("Stop server");
        operationPanel.add(startButton);
        operationPanel.add(stopButton);

        logPanel = new JPanel();
        logPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        logPanel.setMinimumSize(new Dimension(400, 300));
        logPanel.setPreferredSize(new Dimension(400, 300));

        logArea = new JTextArea(16, 32);
        logArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        logArea.setEditable(false);
        logArea.setWrapStyleWord(true);
        JScrollPane sp = new JScrollPane(logArea);
        logPanel.add(sp);

        this.add(operationPanel);
        this.add(logPanel);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void AttachEventHandler() {

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                try {
                    server.start();
                } catch (Exception e1) {
                    logArea.append("Cannot start server: " + e1.getMessage() + "\n");
                }

                logArea.append("Server is running at port " + server.getPortNumber() + "\n");
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    server.stopServer();
                } catch (Exception e1) {
                    logArea.append("Cannot stop server: " + e1.getMessage() + "\n");
                }

                logArea.append("Server is stopped\n");
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
            }
        });
    }

    public void log(String str) {
        logArea.append(str + "\n");
    }
}
