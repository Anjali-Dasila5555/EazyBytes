import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
// import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import javax.swing.*;
// import javax.swing.JTextArea;
// import javax.swing.JTextField;


 class server extends JFrame{

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;


    // component
    private JLabel heading =new JLabel("server area");
    private JTextArea messageArea = new JTextArea();
    private JTextField  messageInput =new JTextField();
    private Font font = new Font("roboto",Font.PLAIN,20);

    // constructor
    public server(){
       try {
        server = new ServerSocket(7777);
        System.out.println("server is ready to accept connection");
        System.out.println("waiting....");
       socket = server.accept();

       br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
       out = new PrintWriter(socket.getOutputStream());


            createGUI();
            headingEvent();
            startReading();
            // startWriting();

       


       } catch (Exception e) {
        e.getStackTrace();
       }
    }

    
    private void headingEvent() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
               
            }

            @Override
            public void keyPressed(KeyEvent e) {
             
            }

            @Override
            public void keyReleased(KeyEvent e) {
            //   System.out.println("key release"+e.getKeyCode());
              if (e.getKeyCode()==10) {
               
                String contentToSend = messageInput.getText();
                messageArea.append("me :"+contentToSend+"\n");
                out.println(contentToSend);
                out.flush();
                messageInput.setText("");
              }
            }
            
        });
    }


    private void createGUI() {
        this.setTitle("server Message");
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        this.setLayout(new BorderLayout());

        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane =new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

    }


    public void startReading(){
        Runnable r1=()->{
            System.out.println("reader started.....");
            try {
            while(true){
               
                    String msg = br.readLine();

                    if (msg.equals("exit")) {
                        System.out.println("client terminated the chat");
                        JOptionPane.showMessageDialog(this, "client terminated the chat");
                        messageInput.setEnabled(false);
                        break;
                    }
                   messageArea.append("client :"+msg+"\n");              
            }
        } catch (Exception e) {
            System.out.println("connection closed");
         }
       
        };
        new Thread(r1).start();
    }
    public void startWriting(){
        Runnable r2=()->{
            try {
            while (!socket.isClosed()) {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }
                  
                  

              
            }
        } catch (Exception e) {
           System.out.println("connection is closed");
           }
           
        };
        new Thread(r2).start();
    }
    public static void main(String[] args) {
        new server();
       
    }
}
