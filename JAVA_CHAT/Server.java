import java.net.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
public class Server extends JFrame{
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    private JLabel heading = new JLabel("Server Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);
    public Server(){
        try{
            server=new ServerSocket(7777);
            System.out.println("Server is ready to accept connections");
            System.out.println("Waiting.....");
            socket=server.accept();
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
            //startWriting();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private void handleEvents(){
        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                if(e.getKeyCode() == 10){
                    String contentToSend=messageInput.getText();
                    messageArea.append("Me : "+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
                
            }
            
        });
    }
    public void createGUI(){
        this.setTitle("Server Messager");
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        this.setLayout(new BorderLayout());
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        //this.add(messageArea,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
        this.setVisible(true);

    }
    public void startReading(){
        Runnable r1=()->{
            System.out.println("Reader Started....");
            try{
                while(true){
                    String msg=br.readLine();
                    if(msg.equals("exit")){
                        System.out.println("Client terminated the chat");
                        JOptionPane.showMessageDialog(this,"Client terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    //System.out.println("Client: "+msg);
                    messageArea.append("Client : "+msg+"\n");
                }
            }
            catch(Exception e){
                //e.printStackTrace();
                System.out.println("Connection is closed");
            }
        };
        new Thread(r1).start();
    }
    public void startWriting(){
        Runnable r2=()->{
            System.out.println("Writer Started....");
            try{
                while(!socket.isClosed()){
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content=br1.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }
                }
            }
            catch(Exception e){
                //e.printStackTrace();
                System.out.println("Connection is Closed");
            }
        };
        new Thread(r2).start();
    }
    public static void main(String[] args){
        System.out.println("This is server..going to start server ");
        new Server();
    }
}
