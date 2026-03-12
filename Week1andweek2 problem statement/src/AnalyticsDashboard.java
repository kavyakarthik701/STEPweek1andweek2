import java.util.*;

public class AnalyticsDashboard {

    HashMap<String,Integer> pageViews = new HashMap<>();
    HashMap<String,Set<String>> uniqueUsers = new HashMap<>();
    HashMap<String,Integer> sources = new HashMap<>();

    void processEvent(String url,String userId,String source){

        pageViews.put(url,pageViews.getOrDefault(url,0)+1);

        uniqueUsers.putIfAbsent(url,new HashSet<>());
        uniqueUsers.get(url).add(userId);

        sources.put(source,sources.getOrDefault(source,0)+1);
    }

    void getDashboard(){

        List<Map.Entry<String,Integer>> list = new ArrayList<>(pageViews.entrySet());
        list.sort((a,b)->b.getValue()-a.getValue());

        System.out.println("Top Pages:");

        for(int i=0;i<Math.min(10,list.size());i++){
            String url=list.get(i).getKey();
            int views=list.get(i).getValue();
            int unique=uniqueUsers.get(url).size();

            System.out.println((i+1)+". "+url+" - "+views+" views ("+unique+" unique)");
        }

        int total=0;
        for(int v:sources.values()) total+=v;

        System.out.println("\nTraffic Sources:");

        for(String s:sources.keySet()){
            double p=(sources.get(s)*100.0)/total;
            System.out.println(s+": "+String.format("%.1f",p)+"%");
        }
    }

    public static void main(String[] args){

        AnalyticsDashboard a = new AnalyticsDashboard();

        a.processEvent("/article/breaking-news","user1","google");
        a.processEvent("/article/breaking-news","user2","facebook");
        a.processEvent("/sports/championship","user3","direct");
        a.processEvent("/article/breaking-news","user1","google");

        a.getDashboard();
    }
}
