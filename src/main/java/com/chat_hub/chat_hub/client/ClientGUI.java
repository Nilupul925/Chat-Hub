package com.chat_hub.chat_hub.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ExecutionException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.chat_hub.chat_hub.model.Message;

public class ClientGUI extends JFrame{
    private JPanel connectedUsersPanel, messagePanel;
    private StompClient stompClient;
    private String userName;

    public ClientGUI(String userName) throws InterruptedException, ExecutionException {
        super("User: " + userName);
        this.userName = userName;
        stompClient = new StompClient(userName);

        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(ClientGUI.this, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
                
                if (option == JOptionPane.YES_OPTION) {
                    stompClient.disconnectUser(userName);
                    ClientGUI.this.dispose();
                }
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

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(Utilities.SECONDARY_COLOR);

        JTextField messageInputField = new JTextField();
        messageInputField.addKeyListener(new KeyAdapter() {

            @Override 
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == KeyEvent.VK_ENTER) {
                    String message = messageInputField.getText();
                    if(!message.isEmpty()) {
                        messagePanel.add(createChatMessageComponent(new Message("User_1", message)));
                        messageInputField.setText("");
                        
                        //Refresh the fields
                        revalidate();
                        repaint();

                    }
                }
            }
        });
        messageInputField.setBackground(Utilities.SECONDARY_COLOR);
        messageInputField.setForeground(Utilities.TEXT_COLOR);
        messageInputField.setFont(new Font("Inter", Font.PLAIN, 16));
        messageInputField.setPreferredSize(new Dimension(getWidth(), 40));

        inputPanel.add(messageInputField, BorderLayout.CENTER);

        chatPanel.add(messagePanel, BorderLayout.CENTER);
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

        JLabel messageLable = new JLabel(message.getMessage());
        messageLable.setFont(new Font("Inter", Font.PLAIN, 18));
        messageLable.setForeground(Utilities.TEXT_COLOR);
        chatMessage.add(messageLable);

        return chatMessage;
    }
}
