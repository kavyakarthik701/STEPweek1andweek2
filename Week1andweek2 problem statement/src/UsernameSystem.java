import java.util.*;

public class UsernameSystem {

    HashMap<String,Integer> users = new HashMap<>();
    HashMap<String,Integer> attempts = new HashMap<>();

    boolean checkAvailability(String u){
        attempts.put(u, attempts.getOrDefault(u,0)+1);
        return !users.containsKey(u);
    }

    void register(String u,int id){
        if(!users.containsKey(u)) users.put(u,id);
    }

    List<String> suggest(String u){
        List<String> s = new ArrayList<>();
        for(int i=1;i<=3;i++){
            if(!users.containsKey(u+i)) s.add(u+i);
        }
        String d = u.replace("_",".");
        if(!users.containsKey(d)) s.add(d);
        return s;
    }

    String getMostAttempted(){
        String r=""; int m=0;
        for(String k:attempts.keySet()){
            if(attempts.get(k)>m){
                m=attempts.get(k);
                r=k;
            }
        }
        return r;
    }

    public static void main(String[] args){
        UsernameSystem s = new UsernameSystem();
        s.register("john_doe",1);

        System.out.println(s.checkAvailability("john_doe"));
        System.out.println(s.checkAvailability("jane_smith"));
        System.out.println(s.suggest("john_doe"));
        System.out.println(s.getMostAttempted());
    }
}