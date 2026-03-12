import java.util.*;

public class PlagiarismDetector {

    int N = 5;
    HashMap<String, Set<String>> index = new HashMap<>();
    HashMap<String, List<String>> docs = new HashMap<>();

    List<String> ngrams(String text){
        String[] w = text.toLowerCase().split("\\s+");
        List<String> g = new ArrayList<>();
        for(int i=0;i<=w.length-N;i++){
            String s="";
            for(int j=0;j<N;j++) s+=w[i+j]+" ";
            g.add(s.trim());
        }
        return g;
    }

    void addDocument(String id,String text){
        List<String> grams = ngrams(text);
        docs.put(id,grams);

        for(String g:grams){
            index.putIfAbsent(g,new HashSet<>());
            index.get(g).add(id);
        }
    }

    void analyze(String id){
        List<String> grams = docs.get(id);
        HashMap<String,Integer> match = new HashMap<>();

        for(String g:grams){
            if(index.containsKey(g)){
                for(String d:index.get(g)){
                    if(!d.equals(id))
                        match.put(d,match.getOrDefault(d,0)+1);
                }
            }
        }

        for(String d:match.keySet()){
            int m = match.get(d);
            double sim = (m*100.0)/grams.size();
            System.out.println(d+" → "+m+" matches → "+sim+"%");
        }
    }

    public static void main(String[] args){

        PlagiarismDetector p = new PlagiarismDetector();

        p.addDocument("essay_089",
                "machine learning improves prediction using data and algorithms");

        p.addDocument("essay_092",
                "machine learning improves prediction using data and algorithms widely");

        p.addDocument("essay_123",
                "machine learning improves prediction using data and algorithms widely in research");

        p.analyze("essay_123");
    }
}