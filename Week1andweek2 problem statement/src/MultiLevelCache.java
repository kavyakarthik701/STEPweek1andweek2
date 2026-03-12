import java.util.*;

class VideoData{ String id; VideoData(String id){this.id=id;} }

public class MultiLevelCache {
    LinkedHashMap<String,VideoData> L1 = new LinkedHashMap<>(10000,0.75f,true){
        protected boolean removeEldestEntry(Map.Entry<String,VideoData> e){return size()>10000;}
    };
    LinkedHashMap<String,String> L2 = new LinkedHashMap<>(100000,0.75f,true){
        protected boolean removeEldestEntry(Map.Entry<String,String> e){return size()>100000;}
    };
    Map<String,VideoData> L3 = new HashMap<>();
    Map<String,Integer> accessCount = new HashMap<>();
    int promoteThreshold = 5;
    int L1hits=0,L2hits=0,L3hits=0,total=0;
    double L1time=0,L2time=0,L3time=0;

    void addToDB(VideoData v){ L3.put(v.id,v); L2.put(v.id,v.id); }

    VideoData getVideo(String id){
        total++;
        if(L1.containsKey(id)){ L1hits++; L1time+=0.5; accessCount.put(id,accessCount.getOrDefault(id,0)+1);
            System.out.println("L1 Cache HIT (0.5ms)"); return L1.get(id);}
        if(L2.containsKey(id)){ L2hits++; L2time+=5; accessCount.put(id,accessCount.getOrDefault(id,0)+1);
            System.out.println("L1 Cache MISS\nL2 Cache HIT (5ms)");
            if(accessCount.get(id)>=promoteThreshold) L1.put(id,L3.get(id)); return L3.get(id);}
        if(L3.containsKey(id)){ L3hits++; L3time+=150; accessCount.put(id,1);
            System.out.println("L1 Cache MISS\nL2 Cache MISS\nL3 Database HIT (150ms)");
            L2.put(id,L3.get(id).id); return L3.get(id);}
        System.out.println("Video not found"); return null;
    }

    void getStatistics(){
        double L1rate=L1hits*100.0/total, L2rate=L2hits*100.0/total, L3rate=L3hits*100.0/total;
        double overallRate=(L1hits+L2hits+L3hits)*100.0/total;
        double avgTime=(L1time+L2time+L3time)/total;
        System.out.printf("getStatistics() →\nL1: Hit Rate %.0f%%, Avg Time: %.1fms\n",L1rate,L1time/L1hits);
        System.out.printf("L2: Hit Rate %.0f%%, Avg Time: %.1fms\n",L2rate,L2time/L2hits);
        System.out.printf("L3: Hit Rate %.0f%%, Avg Time: %.1fms\n",L3rate,L3time/L3hits);
        System.out.printf("Overall: Hit Rate %.0f%%, Avg Time: %.1fms\n",overallRate,avgTime);
    }

    public static void main(String[] args){
        MultiLevelCache cache = new MultiLevelCache();
        for(int i=1;i<=200;i++) cache.addToDB(new VideoData("video_"+i));

        cache.getVideo("video_123");
        cache.getVideo("video_123");
        cache.getVideo("video_999");
        cache.getStatistics();
    }
}
