package shoppingCartProject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ShoppingCart {
    
    private String cartOwner;
    private List <String> itemList = new ArrayList<>();

    // constructor
    public ShoppingCart (String cartOwner) {
        this.cartOwner = cartOwner;
    }

    public ShoppingCart (String cartOwner, List<String> itemList) {
        this.cartOwner = cartOwner;
        this.itemList = itemList;
    }



    // getter   
    public String getCartOwner() {
        return cartOwner;
    }

    public List<String> getItemList() {
        return itemList;
    }

    // list method
    public void listCart() {

        if (itemList.isEmpty()) {

            System.out.printf("%s's cart is empty, please add items to the cart\n",cartOwner);
        
        } else {

            System.out.printf("%d items in the cart\n",itemList.size());
                
            for (int i=0; i< itemList.size(); i++) {
                System.out.printf("%d. %s\n", i+1, itemList.get(i));
            }
        }

    }

    // add method, duplicate items not added
    public void addToCart(String[] items) {

        List<String> added = new LinkedList<>();
        for (String item : items) {

            if(!(item.isBlank())){

                if(! (this.itemList.contains(item))) {
                    this.itemList.add(item);
                    added.add(item);
                }             
            }
        }
        
        if (!(added.isEmpty())) {
            System.out.println(added + " added to cart");
        }
    
    }

    // delete method
    public void deleteItem(String itemNumber) {

        int item = 0;

        try{
            item = Integer.parseInt(itemNumber.trim());
            if ((item > 0) && (item <= itemList.size())) {
                System.out.printf(itemList.remove(item-1) + " removed from cart\n");

            } else {
                System.out.printf("please enter a valid number to delete item\n",item);
            }
        }catch (NumberFormatException e){
            System.out.printf("please enter a valid number to delete item\n",item);
        }
    
    }



}
    
    
    
   
