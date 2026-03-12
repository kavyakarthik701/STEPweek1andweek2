import java.util.*;

class Transaction{
    int id; double amount; String merchant; long time; String account;
    Transaction(int id,double amount,String merchant,long time,String account){this.id=id;this.amount=amount;this.merchant=merchant;this.time=time;this.account=account;}
}

public class TransactionAnalyzer {
    List<Transaction> txs = new ArrayList<>();
    void addTransaction(Transaction t){txs.add(t);}

    List<int[]> findTwoSum(double target){
        HashMap<Double,Integer> map = new HashMap<>();
        List<int[]> res = new ArrayList<>();
        for(Transaction t:txs){
            double c=target-t.amount;
            if(map.containsKey(c)) res.add(new int[]{map.get(c), t.id});
            map.put(t.amount,t.id);
        }
        return res;
    }

    List<List<Integer>> findKSum(int k,double target){
        List<List<Integer>> res=new ArrayList<>();
        kSumHelper(0,k,target,new ArrayList<>(),res);
        return res;
    }

    void kSumHelper(int idx,int k,double target,List<Integer> path,List<List<Integer>> res){
        if(k==0 && target==0){res.add(new ArrayList<>(path)); return;}
        if(idx>=txs.size()) return;
        if(txs.get(idx).amount<=target){
            path.add(txs.get(idx).id);
            kSumHelper(idx+1,k-1,target-txs.get(idx).amount,path,res);
            path.remove(path.size()-1);
        }
        kSumHelper(idx+1,k,target,path,res);
    }

    List<Map<String,Object>> detectDuplicates(){
        HashMap<String,Set<String>> map=new HashMap<>();
        for(Transaction t:txs){
            String key=t.amount+"|"+t.merchant;
            map.putIfAbsent(key,new HashSet<>());
            map.get(key).add(t.account);
        }
        List<Map<String,Object>> res=new ArrayList<>();
        for(String k:map.keySet()){
            if(map.get(k).size()>1){
                Map<String,Object> m=new HashMap<>();
                String[] p=k.split("\\|");
                m.put("amount",Double.parseDouble(p[0]));
                m.put("merchant",p[1]);
                m.put("accounts",map.get(k));
                res.add(m);
            }
        }
        return res;
    }

    public static void main(String[] args){
        TransactionAnalyzer ta=new TransactionAnalyzer();
        ta.addTransaction(new Transaction(1,500,"Store A",1000,"acc1"));
        ta.addTransaction(new Transaction(2,300,"Store B",1015,"acc2"));
        ta.addTransaction(new Transaction(3,200,"Store C",1030,"acc3"));
        ta.addTransaction(new Transaction(4,500,"Store A",1045,"acc2"));

        List<int[]> twoSum=ta.findTwoSum(500);
        System.out.print("findTwoSum(target=500) → [");
        for(int i=0;i<twoSum.size();i++){System.out.print(Arrays.toString(twoSum.get(i))); if(i<twoSum.size()-1) System.out.print(", ");}
        System.out.println("]");

        List<List<Integer>> kSum=ta.findKSum(3,1000);
        System.out.print("findKSum(k=3, target=1000) → [");
        for(int i=0;i<kSum.size();i++){System.out.print(kSum.get(i)); if(i<kSum.size()-1) System.out.print(", ");}
        System.out.println("]");

        System.out.println("detectDuplicates() → "+ta.detectDuplicates());
    }
}