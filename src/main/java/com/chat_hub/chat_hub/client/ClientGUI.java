package com.chat_hub.chat_hub.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.chat_hub.chat_hub.model.Message;

public class ClientGUI extends JFrame implements MessageListener {
    private JPanel connectedUsersPanel, messagePanel;
    private StompClient stompClient;
    private String userName;
    private JScrollPane messageScrollPane;

    public ClientGUI(String userName) throws InterruptedException, ExecutionException {
        super("User: " + userName);
        this.userName = userName;
        stompClient = new StompClient(this, userName);

        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(ClientGUI.this, "Are you sure you want to exit?", "Exit",
                        JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    stompClient.disconnectUser(userName);
                    ClientGUI.this.dispose();
                }
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateMessageLabelWidths();
            }
        });

        getContentPane().setBackground(Utilities.PRIMARY_COLOR);
        addGuiComponents();
    }

    private void addGuiComponents() {
        addConnectedUsersComponent();
        addChatAreaComponent();
    }

    private void addChatAreaComponent() {
        JPanel chatPanel = new JPanel();
        chatPanel.setBorder(Utilities.addPadding(10, 10, 10, 10));
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setBackground(Utilities.TRANSPARENT_COLOR);

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(Utilities.TRANSPARENT_COLOR);

        messageScrollPane = new JScrollPane(messagePanel);
        messageScrollPane.setBackground(Utilities.TRANSPARENT_COLOR);
        messageScrollPane.setBorder(Utilities.addPadding(10, 10, 10, 10));
        messageScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        messageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        messageScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        messageScrollPane.getViewport().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                revalidate();
                repaint();
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(Utilities.SECONDARY_COLOR);

        JTextField messageInputField = new JTextField();
        messageInputField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    String message = messageInputField.getText();
                    if (!message.isEmpty()) {
                        messageInputField.setText("");

                        stompClient.sendMessage(new Message(userName, message));
                    }
                }
            }
        });

        messageInputField.setBorder(Utilities.addPadding(10, 10, 10, 10));
        messageInputField.setBackground(Utilities.THERID_COLOR);
        messageInputField.setForeground(Utilities.TEXT_COLOR_INPUT);
        messageInputField.setFont(new Font("Inter", Font.PLAIN, 16));
        messageInputField.setPreferredSize(new Dimension(getWidth(), 40));

        inputPanel.add(messageInputField, BorderLayout.CENTER);

        chatPanel.add(messageScrollPane, BorderLayout.CENTER);
        chatPanel.add(inputPanel, BorderLayout.SOUTH);

        add(chatPanel, BorderLayout.CENTER);
    }

    private void addConnectedUsersComponent() {
        connectedUsersPanel = new JPanel();
        connectedUsersPanel.setBorder(Utilities.addPadding(10, 10, 10, 10));
        connectedUsersPanel.setLayout(new BoxLayout(connectedUsersPanel, BoxLayout.Y_AXIS));
        connectedUsersPanel.setBackground(Utilities.SECONDARY_COLOR);
        connectedUsersPanel.setPreferredSize(new Dimension(200, getHeight()));

        JLabel connectedUsersLabel = new JLabel("Connected Users");
        connectedUsersLabel.setFont(new Font("Inter", Font.BOLD, 16));
        connectedUsersLabel.setForeground(Utilities.TEXT_COLOR);

        connectedUsersPanel.add(connectedUsersLabel);

        add(connectedUsersPanel, BorderLayout.WEST);
    }

    private JPanel createChatMessageComponent(Message message) {
        JPanel chatMessage = new JPanel();
        chatMessage.setBackground(Utilities.TRANSPARENT_COLOR);
        chatMessage.setLayout(new BoxLayout(chatMessage, BoxLayout.Y_AXIS));
        chatMessage.setBorder(Utilities.addPadding(10, 10, 10, 10));

        JLabel userNameLable = new JLabel(message.getUser());
        userNameLable.setFont(new Font("Inter", Font.BOLD, 18));
        userNameLable.setForeground(Utilities.TEXT_COLOR);
        chatMessage.add(userNameLable);

        JLabel messageLable = new JLabel();
        messageLable.putClientProperty("originalMessage", message.getMessage());
        // This line sets the text of the message label to the message content, wrapped
        // in HTML to allow for word wrapping and a maximum width of 400 pixels.
        messageLable.setText(formatWrappedMessageHtml(message.getMessage()));
        messageLable.setFont(new Font("Inter", Font.PLAIN, 18));
        messageLable.setForeground(Utilities.TEXT_COLOR);
        chatMessage.add(messageLable);

        return chatMessage;
    }

    private String formatWrappedMessageHtml(String rawMessage) {
        int wrapWidth = Math.max(150, (int) (0.50 * getWidth()));
        int maxWordChars = Math.max(10, wrapWidth / 10);
        String safeMessage = rawMessage == null ? "" : rawMessage
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replaceAll("(\\S{" + maxWordChars + "})(?=\\S)", "$1<br>");
        return "<html><body style='width:" + wrapWidth + "px'>" + safeMessage + "</body></html>";
    }

    @Override
    public void onMessageReceived(Message message) {
        messagePanel.add(createChatMessageComponent(message));
        revalidate();
        repaint();

        messageScrollPane.getVerticalScrollBar().setValue(Integer.MAX_VALUE);
    }

    @Override
    public void onActiveUsersUpdated(ArrayList<String> activeUsers) {
        if (connectedUsersPanel.getComponentCount() > 3) {
            connectedUsersPanel.remove(1);
        }

        JPanel connectedUsersLabelPanel = new JPanel();
        connectedUsersLabelPanel.setBackground(Utilities.TRANSPARENT_COLOR);
        connectedUsersLabelPanel.setLayout(new BoxLayout(connectedUsersLabelPanel, BoxLayout.Y_AXIS));

        for (String user : activeUsers) {
            JLabel userLabel = new JLabel();
            userLabel.setText(user);
            userLabel.setFont(new Font("Inter", Font.PLAIN, 16));
            userLabel.setForeground(Utilities.TEXT_COLOR);
            connectedUsersLabelPanel.add(userLabel);
        }
        connectedUsersPanel.add(connectedUsersLabelPanel);

        revalidate();
        repaint();
    }

    private void updateMessageLabelWidths() {
        for (int i = 0; i < messagePanel.getComponents().length; i++) {
            Component component = messagePanel.getComponent(i);

            if (component instanceof JPanel) {

                JPanel chatMessage = (JPanel) component;

                if (chatMessage.getComponentCount() > 1 && chatMessage.getComponent(1) instanceof JLabel) {
                    JLabel messageLabel = (JLabel) chatMessage.getComponent(1);
                    Object originalMessage = messageLabel.getClientProperty("originalMessage");
                    String rawText = originalMessage != null
                            ? originalMessage.toString()
                            : messageLabel.getText().replaceAll("<html><body style='width:.*?px'>", "").replaceAll("</body></html>", "");
                    messageLabel.setText(formatWrappedMessageHtml(rawText));
                }
            }
        }
        messagePanel.revalidate();
        messagePanel.repaint();
    }
}
