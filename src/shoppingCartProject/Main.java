package shoppingCartProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main{

    public static void main(String[] args) {

        if (args.length == 0) {
                System.out.println("please re-run the program and indicate the directory");
                return;
        } 

        Path directoryPath = FileSystems.getDefault().getPath(args[0].trim());
        File directory = directoryPath.toFile();
        File[] allFiles = directory.listFiles();
        System.out.printf("There are %d carts in %s directory\n", allFiles.length, args[0].trim());

        for (File f:allFiles){
            System.out.printf("%s\n", f.getName());
        }
        
        
        Console cons = System.console();
        Boolean exit = false;
        File file = null;
        ShoppingCart cart = null;

        while (!exit) {

            String userInput = cons.readLine(">");
            String[] input = userInput.trim().split(" ");
            int inputLength = input.length;
            //System.out.printf("input length is %d\n",inputLength); // testing

            Reader reader;
            BufferedReader br;
            Writer writer;
            BufferedWriter bw;

            // for testing
            // for (String s : input)
            // System.out.printf("%s\n",s);

            switch (input[0]) {

                case "load":
                {
                    Path path = Paths.get(".\\shoppingcart",input[1]+".cart");
                    //System.out.printf("the path is %s\n",path.toAbsolutePath()); // testing
                    file = path.toFile();
                    
                    if (file.exists()) {

                        //System.out.println("File exist"); // testing

                        try {
                            reader = new FileReader(file);
                            br = new BufferedReader(reader);
                            
                            String cartOwner = input[1];
                            String readfile;
                            List<String> itemList = new ArrayList<>();

                            while (null != (readfile = br.readLine())) {
                                itemList.add(readfile);
                            }

                            cart = new ShoppingCart(cartOwner,itemList);

                            System.out.printf("%s's shopping cart loaded\n",input[1]);
  
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                        

                    }else{

                        System.out.println("Cart does not exist in shoppingcart directory");

                        // create new cart
                        try {
                            file.createNewFile();

                            if (file.exists()) {
                                System.out.printf("New cart created for %s\n",input[1]);
                            }

                            if (!(input[1].isBlank())) {
                                String cartOwner = input[1];
                                cart = new ShoppingCart(cartOwner);
                                // System.out.println("New cart object instantiated"); //testing
                            }

                        } catch (IOException e){
                            e.printStackTrace();
                        }

                    }

                    break;
                }


                case "list":
                {
                    cart.listCart();
                    break;
                }

                case "add":
                {
                    String[] items = new String[inputLength-1];
                    for (int i=0; i < (inputLength-1) ; i++){
                        items[i]= input[i+1];
                    }
                        
                    cart.addToCart(items);
                    break;
                }

                case "delete":
                {
                    cart.deleteItem(input[1]);
                    break;
                }

                case "save": 
                {
                    try {
                        writer = new FileWriter(file);
                        bw = new BufferedWriter(writer);
                        for (String s : cart.getItemList()){
                            bw.write(s + "\n");
                        }
                        bw.flush();
                        bw.close();
                        System.out.println("Cart contents saved to " + cart.getCartOwner());

                    } catch (IOException e){
                        e.printStackTrace();
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

        


        
        

    
        // ShoppingCart mycart = new ShoppingCart("Mei Ling");
        // String[] items = {"toy", "vege"," ", "apples"};
        // mycart.addToCart(items);
        // mycart.listCart();
        // mycart.deleteItem("     hello testing   ");

    }

    
}