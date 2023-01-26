package shoppingCartProject;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Path;


public class MultiThreadedMain {

    public static void main(String[] args) {

        if (args.length < 2) {
                System.out.println("please re-run the program. Indicate the directory and port number");
                return;
        } 

        // read directory
        Path directoryPath = FileSystems.getDefault().getPath(args[0].trim());
        //File directory = directoryPath.toFile();
        //File[] allFiles = directory.listFiles();

        // read port
        int port = Integer.parseInt(args[1].trim());

        try (ServerSocket serverSocket = new ServerSocket(port);) {

            while(true){

                System.out.println("Waiting for client connection");
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                multiThreadedServer mts = new multiThreadedServer(socket,directoryPath);
                Thread thread = new Thread(mts);
                thread.start();


            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
