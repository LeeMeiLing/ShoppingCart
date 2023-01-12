package shoppingCartProject;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    
    public static void main(String[] args) {

        String[] userArg = args[0].trim().split("@|:");
        String user = userArg[0];
        String host = userArg[1];
        String port = userArg[2];
        int portNum = Integer.parseInt(port);
        //System.out.printf("%s %s %s",user, server, port); //testing

        Console cons = System.console();
        
    try (Socket socket = new Socket(host,portNum)) {

            System.out.printf("Connected to shopping cart server at " + host + " on " + user + " port " + port + "\n");
        
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            OutputStream os = socket.getOutputStream();
            //OutputStreamWriter osw = new OutputStreamWriter(os); // skip this??
            PrintWriter pw = new PrintWriter(os);

            pw.println(user);
            pw.flush();

            boolean exit = false;
            String consInput = null;

            //while loop here
            while (!exit) {

                String fromServer = br.readLine();
                
                if (fromServer.equals(">")) {

                    consInput = cons.readLine(">"); 
                    pw.println(consInput);
                    pw.flush();
                
                }else{

                    System.out.println(fromServer);

                }
                
                if (null != consInput){
                    
                    if (consInput.trim().equals("exit")) {
                        exit = true;
                    }
                }
                
            }
            
        }catch (IOException e){

            System.out.println("Client error: " + e.getMessage());
            e.printStackTrace();

        }

    }

}
