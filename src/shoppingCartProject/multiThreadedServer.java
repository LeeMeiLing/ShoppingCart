package shoppingCartProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class multiThreadedServer implements Runnable{

    final Socket socket; // use final Socket socket; dont assign null if use final
    final Path directoryPath;

    public multiThreadedServer(Socket socket, Path directoryPath) {
        this.socket = socket;
        this.directoryPath = directoryPath;
    }

    @Override
    public void run() {
        
      
        try{

            System.out.println(Thread.currentThread().getName()); // debug
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            OutputStream os = socket.getOutputStream();
            //OutputStreamWriter osw = new OutputStreamWriter(os); // skip this??
            PrintWriter pw = new PrintWriter(os);

            String clientInput = br.readLine();
            //System.out.println(clientInput);// testing

            pw.println("Using " + directoryPath.getFileName() + " directory for persistence");
            pw.flush();
            // pw.printf("There " + (allFiles.length == 1? "is" : "are") +" %d " + (allFiles.length == 1? "cart" : "carts") + " in %s directory\n", 
            // allFiles.length, args[0].trim());
            // pw.flush();

            
            File file = null;
            ShoppingCart cart = null;
            Reader reader = null;
            BufferedReader fileBR = null;
            Writer writer = null;
            BufferedWriter bw = null;
            Boolean exit = false;

            //Path path = Paths.get(".\\shoppingcart",clientInput+".cart"); // defined directory instead of using arg[0] to find directory
            Path path = Paths.get(directoryPath.toString(),clientInput+".cart");
            //System.out.printf("path name is " + path.getFileName()+"\n"); // testing 
            file = path.toFile();
            //System.out.printf("file name is " + file.getName()+"\n"); // testing
                
            if (file.exists()) {

                // System.out.println("File exist"); // testing
                // pw.println("File exist"); //testing
                // pw.flush(); //testing

                try {
                    reader = new FileReader(file);
                    fileBR = new BufferedReader(reader);
                        
                    String cartOwner = clientInput;
                    String readfile;
                    List<String> itemList = new ArrayList<>();

                    while (null != (readfile = fileBR.readLine())) {
                            itemList.add(readfile);
                    }

                    fileBR.close();
                    reader.close();

                    cart = new ShoppingCart(cartOwner,itemList);

                    pw.printf("%s's shopping cart loaded\n",clientInput);
                    pw.flush();

                } catch (IOException e){
                    e.printStackTrace();
                }
                    

            }else {

                pw.println("Cart does not exist in shoppingcart directory");
                pw.flush();

                // create new cart
                try {
                    file.createNewFile();

                    if (file.exists()) {
                            pw.printf("New cart created for %s\n",clientInput);
                            pw.flush();
                    }

                    if (!(clientInput.isBlank())) {
                            String cartOwner = clientInput;
                            cart = new ShoppingCart(cartOwner);
                    }

                } catch (IOException e){
                        pw.println("Fail to create file for " + clientInput);
                        pw.flush();
                        e.printStackTrace();
                }

            }

            while (!exit) {

                pw.println(">"); // 
                pw.flush();
                String userInput = br.readLine(); // read from inputstream
                String[] input = userInput.trim().split(" ");
                int inputLength = input.length;
                //System.out.printf("input length is %d\n",inputLength); // testing

                
                // testing
                // for (String s : input)
                // System.out.printf("%s\n",s);


                switch (input[0]) {

                    case "list":
                    {
                        cart.listCart(pw);
                        break;
                    }

                    case "add":
                    {
                        String[] items = new String[inputLength-1];
                        for (int i=0; i < (inputLength-1) ; i++){
                            items[i]= input[i+1];
                        }
                            
                        cart.addToCart(items,pw);
                        break;
                    }

                    case "delete":
                    {
                        cart.deleteItem(input[1],pw);
                        break;
                    }

                    case "save": 
                    {
                        try {
                            writer = new FileWriter(file);
                            bw = new BufferedWriter(writer);
                            for (String s : cart.getItemList()){
                                bw.write(s + "\n");
                                bw.flush();
                            }

                            pw.println("Cart contents saved to " + cart.getCartOwner());

                        } catch (IOException e){
                            e.printStackTrace();
                        } finally {
                            if (bw!= null) bw.close();
                            if (writer!= null) writer.close();
                        }

                        break;
                    }

                    case "exit":
                    {
                        exit = true;
                        break;
                    }
                }
            }


            if (br!= null) br.close();
            if (isr!= null) isr.close();
            if (is!= null) is.close();
            if (pw!= null) br.close();
            if (os!= null) isr.close();
            socket.close();

        } catch (IOException e){

            System.out.println("Server error: " + e.getMessage());
            e.printStackTrace();

        }
        
    }


    
}
