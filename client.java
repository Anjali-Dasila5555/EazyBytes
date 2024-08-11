import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;

import javax.swing.*;
public class client extends JFrame {
   
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    //declare components
    private JLabel heading =new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();

    private Font font = new Font ("Roboto",Font.PLAIN,20);

    //constructor
    public client(){
        try {
            System.out.println("sending to server");
            socket = new Socket("127.0.0.1" , 7777)  ;
            System.out.println("connection done....");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
         
         createGUI();
         handleEvents();
         
            startReading();
            // startWriting();

        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private void createGUI(){
        //gui code......
        this.setTitle("client Message[end]");
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //codint for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
       
        messageArea.setEditable(false);
        //frame layout
        this.setLayout(new BorderLayout());

        //adding the component to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane =new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
        
        this.setVisible(true);
    }
    private void handleEvents(){
       
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
             
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
               System.out.println("key released" + e.getKeyCode());
               if (e.getKeyCode()==10) {
                // System.out.println("you have pressed enter button");
                String contentToSend= messageInput.getText();
                messageArea.append("me:"+contentToSend+"\n");
                out.println(contentToSend);
                out.flush();
                messageInput.setText("");
                messageInput.requestFocus();
               }
            }
            
        });

    }
        public void startReading(){
            Runnable r1=()->{
                System.out.println("reader started.....");
                try{
                while(true){
                   
                        String msg = br.readLine();
    
                        if (msg.equals("exit")) {
                            System.out.println("server terminated the chat");
                            JOptionPane.showMessageDialog(this, "Server Terminated the chat");
                            messageInput.setEnabled(false);
                            socket.close();
                            break;
                        }
                        // System.out.println("server :"+msg);  
                        messageArea.append("Server : "+msg+"\n");
                }
                
            } catch (Exception e) {
                System.out.println("connection closed");
             }
            };
            new Thread(r1).start();
    }
    public void startWriting(){
        Runnable r2=()->{
            try{

            
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
            System.out.println("connection closed");
        } catch (Exception e) {
            e.getStackTrace();
           }
        };
        new Thread(r2).start();
    }
    public static void main(String[] args) {
       new  client();
    }
}
