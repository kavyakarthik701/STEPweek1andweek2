import java.util.*;

public class RateLimiter {

    class TokenBucket{
        int tokens,maxTokens;
        long lastRefill;
        TokenBucket(int max){maxTokens=max; tokens=max; lastRefill=System.currentTimeMillis();}
    }

    HashMap<String, TokenBucket> clients = new HashMap<>();
    int limit=1000;
    long refillTime=3600_000;

    boolean checkRateLimit(String clientId){
        clients.putIfAbsent(clientId,new TokenBucket(limit));
        TokenBucket b=clients.get(clientId);
        long now=System.currentTimeMillis();
        if(now-b.lastRefill>=refillTime){b.tokens=b.maxTokens;b.lastRefill=now;}
        if(b.tokens>0){b.tokens--;
            System.out.println("checkRateLimit(clientId=\""+clientId+"\") → Allowed ("+b.tokens+" requests remaining)"); return true;}
        else{long retry=(refillTime-(now-b.lastRefill))/1000;
            System.out.println("checkRateLimit(clientId=\""+clientId+"\") → Denied (0 requests remaining, retry after "+retry+"s)"); return false;}
    }

    void getRateLimitStatus(String clientId){
        TokenBucket b=clients.get(clientId);
        long reset=(b.lastRefill+refillTime)/1000;
        int used=limit-b.tokens;
        System.out.println("getRateLimitStatus(\""+clientId+"\") → {used: "+used+", limit: "+limit+", reset: "+reset+"}");
    }

    public static void main(String[] args){
        RateLimiter r=new RateLimiter();
        r.checkRateLimit("abc123");
        r.checkRateLimit("abc123");
        for(int i=0;i<998;i++) r.checkRateLimit("abc123");
        r.checkRateLimit("abc123");
        r.getRateLimitStatus("abc123");
    }
}
