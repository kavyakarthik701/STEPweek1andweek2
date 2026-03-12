import java.util.*;

public class ParkingLot {

    class Spot {
        String plate; long entryTime;
        Spot(){plate=null; entryTime=0;}
    }

    Spot[] spots;
    int capacity = 500;
    int totalProbes = 0, parkedCount = 0;
    Map<String,Integer> plateToIndex = new HashMap<>();

    ParkingLot(){ spots = new Spot[capacity]; for(int i=0;i<capacity;i++) spots[i]=new Spot(); }

    int hash(String plate){ return Math.abs(plate.hashCode())%capacity; }

    void parkVehicle(String plate){
        int idx = hash(plate), probes=0;
        while(spots[idx].plate!=null){
            probes++; idx = (idx+1)%capacity;
        }
        spots[idx].plate = plate;
        spots[idx].entryTime = System.currentTimeMillis();
        plateToIndex.put(plate,idx);
        totalProbes+=probes; parkedCount++;
        System.out.println("parkVehicle(\""+plate+"\") → Assigned spot #"+idx+" ("+probes+" probes)");
    }

    void exitVehicle(String plate){
        if(!plateToIndex.containsKey(plate)) return;
        int idx = plateToIndex.get(plate);
        long durationMs = System.currentTimeMillis()-spots[idx].entryTime;
        double hours = durationMs/3600000.0;
        double fee = hours*5; // $5 per hour
        System.out.printf("exitVehicle(\"%s\") → Spot #%d freed, Duration: %.2fh, Fee: $%.2f%n",plate,idx,hours,fee);
        spots[idx].plate=null; spots[idx].entryTime=0;
        plateToIndex.remove(plate);
        parkedCount--;
    }

    void getStatistics(){
        double occupancy = parkedCount*100.0/capacity;
        double avgProbes = parkedCount==0?0:(totalProbes*1.0/parkedCount);
        System.out.printf("Occupancy: %.1f%%, Avg Probes: %.2f%n",occupancy,avgProbes);
    }

    public static void main(String[] args){
        ParkingLot p = new ParkingLot();
        p.parkVehicle("ABC-1234");
        p.parkVehicle("ABC-1235");
        p.parkVehicle("XYZ-9999");
        p.exitVehicle("ABC-1234");
        p.getStatistics();
    }
}
