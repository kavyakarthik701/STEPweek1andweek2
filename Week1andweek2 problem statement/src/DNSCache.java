import java.util.*;

class DNSCache {

    class Entry {
        String ip;
        long expiry;
        Entry(String ip,long ttl){
            this.ip = ip;
            this.expiry = System.currentTimeMillis() + ttl*1000;
        }
    }

    int capacity;
    int hits = 0, misses = 0;

    LinkedHashMap<String,Entry> cache;

    DNSCache(int capacity){
        this.capacity = capacity;

        cache = new LinkedHashMap<>(capacity,0.75f,true){
            protected boolean removeEldestEntry(Map.Entry<String,Entry> e){
                return size() > DNSCache.this.capacity;
            }
        };
    }

    String queryUpstream(String domain){
        return "172.217.14." + new Random().nextInt(255);
    }

    String resolve(String domain,long ttl){

        if(cache.containsKey(domain)){
            Entry e = cache.get(domain);

            if(System.currentTimeMillis() < e.expiry){
                hits++;
                return "Cache HIT → " + e.ip;
            }else{
                cache.remove(domain);
            }
        }

        misses++;
        String ip = queryUpstream(domain);
        cache.put(domain,new Entry(ip,ttl));

        return "Cache MISS → " + ip;
    }

    void getStats(){
        int total = hits + misses;
        double rate = total==0 ? 0 : (hits*100.0/total);
        System.out.println("Hit Rate: "+rate+"%");
    }

    public static void main(String[] args) throws Exception {

        DNSCache dns = new DNSCache(5);

        System.out.println(dns.resolve("google.com",300));
        System.out.println(dns.resolve("google.com",300));
        System.out.println(dns.resolve("openai.com",300));

        dns.getStats();
    }
}