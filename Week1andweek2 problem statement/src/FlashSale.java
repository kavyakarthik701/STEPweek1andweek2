import java.util.*;

public class FlashSale {

    HashMap<String,Integer> stock = new HashMap<>();
    HashMap<String,Queue<Integer>> waitlist = new HashMap<>();

    void addProduct(String id,int qty){
        stock.put(id,qty);
        waitlist.put(id,new LinkedList<>());
    }

    void checkStock(String id){
        System.out.println(stock.getOrDefault(id,0)+" units available");
    }

    void purchaseItem(String id,int userId){
        int s = stock.getOrDefault(id,0);

        if(s>0){
            stock.put(id,s-1);
            System.out.println("Success, "+(s-1)+" units remaining");
        }else{
            Queue<Integer> q = waitlist.get(id);
            q.add(userId);
            System.out.println("Added to waiting list, position #"+q.size());
        }
    }

    public static void main(String[] args){

        FlashSale f = new FlashSale();

        f.addProduct("IPHONE15_256GB",100);

        f.checkStock("IPHONE15_256GB");

        f.purchaseItem("IPHONE15_256GB",12345);
        f.purchaseItem("IPHONE15_256GB",67890);

        for(int i=0;i<100;i++)
            f.purchaseItem("IPHONE15_256GB",i);

        f.purchaseItem("IPHONE15_256GB",99999);
    }
}
