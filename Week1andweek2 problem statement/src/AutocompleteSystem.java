import java.util.*;

public class AutocompleteSystem {

    class TrieNode{
        Map<Character,TrieNode> children = new HashMap<>();
        boolean isWord=false;
    }

    TrieNode root = new TrieNode();
    HashMap<String,Integer> freq = new HashMap<>();

    void insert(String word){
        TrieNode node=root;
        for(char c:word.toCharArray()){
            node.children.putIfAbsent(c,new TrieNode());
            node=node.children.get(c);
        }
        node.isWord=true;
        freq.put(word,freq.getOrDefault(word,0)+1);
    }

    List<String> search(String prefix){
        TrieNode node=root;
        for(char c:prefix.toCharArray()){
            if(!node.children.containsKey(c)) return new ArrayList<>();
            node=node.children.get(c);
        }
        PriorityQueue<String> pq = new PriorityQueue<>((a,b)->freq.get(a)-freq.get(b));
        dfs(node,prefix,pq);
        List<String> res=new ArrayList<>();
        while(!pq.isEmpty()) res.add(0,pq.poll());
        return res;
    }

    void dfs(TrieNode node,String path,PriorityQueue<String> pq){
        if(node.isWord){
            pq.offer(path);
            if(pq.size()>10) pq.poll();
        }
        for(char c:node.children.keySet()){
            dfs(node.children.get(c),path+c,pq);
        }
    }

    void updateFrequency(String query){
        insert(query);
        System.out.println("Frequency of \""+query+"\" → "+freq.get(query));
    }

    public static void main(String[] args){
        AutocompleteSystem ac = new AutocompleteSystem();
        ac.insert("java tutorial");
        ac.insert("javascript");
        ac.insert("java download");
        ac.insert("java 21 features");

        System.out.println(ac.search("jav"));
        ac.updateFrequency("java 21 features");
        ac.updateFrequency("java 21 features");
    }
}