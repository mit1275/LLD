import java.time.Instant;
import java.util.*;
enum VehicleType{
    TW,
    FW,
    CFV
}
enum ParkingSpotStatus{
    AVAILABLE,
    RESERVED,
    NOT_IN_USE
}
enum VehicleParkingStatus{
    PARKED,
    UN_PARKED
}
enum PaymentStatus{
    LINK_GENERATED,
    PENDING,
    SUCCESS,
    FAILED
}
interface VehicleParkAction{
    public void parkVehicle(Vehicle v);
    public void unparkVehicle(Vehicle v);
}
class TwSpotAction implements VehicleParkAction{
    public void parkVehicle(Vehicle v){
        v.updateParkingStatus(VehicleParkingStatus.PARKED);
    }
    public void unparkVehicle(Vehicle v){
        v.updateParkingStatus(VehicleParkingStatus.UN_PARKED);
    }
}
class FWSpotAction implements VehicleParkAction{
    public void parkVehicle(Vehicle v){
        v.updateParkingStatus(VehicleParkingStatus.PARKED);
    }
    public void unparkVehicle(Vehicle v){
        v.updateParkingStatus(VehicleParkingStatus.UN_PARKED);
    }
}
class ParkingSpot{
    private int id;
    private ParkingSpotStatus parkingSpotStatus;
    private VehicleParkAction vehicleParkAction;
    private long startTime;
    private long endTime;
    private Vehicle v;
    ParkingSpot(int id,VehicleParkAction vehicleParkAction){
        this.id = id;
        this.parkingSpotStatus = ParkingSpotStatus.AVAILABLE;
        this.startTime = 0;
        this.endTime = 0;
        this.vehicleParkAction = vehicleParkAction;
    }
    public void parkVehicle(Vehicle v) {
        // System.out.println("HI MR");
        this.v = v;
        this.vehicleParkAction.parkVehicle(v);
        this.parkingSpotStatus = ParkingSpotStatus.RESERVED;
        this.startTime = Instant.now().toEpochMilli();
        System.out.println("63" + "Parking Status " + this.parkingSpotStatus + " Vehicle "+ this.v + " "+this.startTime);
    }
    public void unparkVehicle(Vehicle v) {
        this.vehicleParkAction.unparkVehicle(v);
        this.parkingSpotStatus = ParkingSpotStatus.AVAILABLE;
        this.endTime = Instant.now().toEpochMilli();
        this.v = null;
    }
    public long getParkingDuration() {
        return (endTime - startTime) / 1000;
    }
    public ParkingSpotStatus getStatus() {
        return parkingSpotStatus;
    }
    public void updateParkingStatus(ParkingSpotStatus p){
        this.parkingSpotStatus = p;
    }
    public long getStartTime(){
        return this.startTime;
    }
    public Vehicle getVehicleDetails(){
        return this.v;
    }
    @Override
    public String toString() {
        return "Parking" + this.id + " "+ this.parkingSpotStatus + " " + this.startTime + " "+this.vehicleParkAction + " "+this.v;
    }
}
class TWParkingSpot extends ParkingSpot{
    TWParkingSpot(int id){
        super(id,new TwSpotAction());
    }
}
class FWParkingSpot extends ParkingSpot{
    FWParkingSpot(int id){
        super(id,new FWSpotAction());
    }
}
class Payment{
    private int id;
    private String transactionId;
    private long amount;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private Ticket ticket;
    Payment(Ticket t,long amount){
        this.ticket = t;
        this.amount = amount;
    }
    public void initiatePayment(){

    }
}
class PaymentMethod{
    private String modeName;
    PaymentMethod(String modeName){
        this.modeName = modeName;
    }
}
class NB extends PaymentMethod{
    NB(){
        super("Net Banking");
    }
}
class UPI extends PaymentMethod{
    UPI(){
        super("UPI");
    }
}
class User{

}
class Vehicle{
    private int id;
    private String regNo;
    private VehicleType vType;
    private User user;
    private VehicleParkingStatus vehicleParkingStatus;
    Vehicle(int id,String regNo,VehicleType vType){
        this.id = id;
        this.regNo = regNo;
        this.vType = vType;
        this.vehicleParkingStatus = VehicleParkingStatus.UN_PARKED;
    }
    public VehicleType getVehicleType(){
        return vType;
    }
    public void updateParkingStatus(VehicleParkingStatus status){
        this.vehicleParkingStatus = status;
    }
    @Override
    public String toString(){
        return ("Vehicle "+ this.id + " "+this.regNo + " "+this.vehicleParkingStatus);
    }
}
class Ticket{
    private int id;
    public ParkingSpot parkingSpot;
    private Vehicle vehicle;
    private long issueTime;
    private long endTime;
    private static int counter = 1;
    public Ticket issueTicket(ParkingSpot parkingSpot,Vehicle v){
        this.id = ++counter;
        this.parkingSpot = parkingSpot;
        this.vehicle = v;
        this.issueTime = Instant.now().toEpochMilli();
        return this;
    }
    public long getIssueTime(){
        return this.issueTime;
    }
    public Ticket updateTicket(ParkingSpot p){
        this.parkingSpot = p;
        return this;
    }
    @Override
    public String toString() {
        return "Ticket{id=" + id +
               ", parkingSpot=" + this.parkingSpot +
               ", vehicle=" + this.vehicle +
               ", issueTime=" + this.issueTime + "}";
    }
}
class ActionPoints{
    private int id;
    private int floorNumber;
    ActionPoints(int id,int floorNumber){
        this.id = id;
        this.floorNumber = floorNumber;
    }
    public int getId(){
        return this.id;
    }
}
class Entry extends ActionPoints{
    private Ticket ticket;
    Entry(int id,int floorNumber){
        super(id, floorNumber);
    }
    public Ticket issueTicket(ParkingSpot p,Vehicle v){
        Ticket ticket = new Ticket().issueTicket(p,v);
        // System.out.println(ticket);
        return ticket;
    }
}
class Exit extends ActionPoints{
    Exit(int id,int floorNumber){
        super(id, floorNumber);
    }
    public void verifyTicket(Ticket t){
        long totCost = this.getCost(t);
        System.out.println(totCost);
        new Payment(t,totCost);
    }
    private long getCost(Ticket t){
        long ans;
        System.out.println("**228 "+t+"\n");
        ans = (Instant.now().toEpochMilli() - t.getIssueTime())*(100);
        return ans;
    }
    
}
class TicketService{
    public Ticket issueTicket(VehicleType v,Vehicle v1,int id){
        ParkingSpot spot = this.findBestPossiblePlace(v);
        if(!spot.equals(null)){
            Ticket t = entryGate.issueTicket(spot,v1);
            System.out.println("**254 "+t+"\n");
            return t;
        }
        return null;
    }
}
class VehicleService{
    private final TicketService t;
    VehicleService(TicketService t){
        this.t = t;
    }
    public void parkVehicle(){
        t.parkingSpot.parkVehicle(this); // parkVehicle
        t.updateTicket(t.parkingSpot);
    }
    public void unparkVehicle(Ticket t){
        t.parkingSpot.unparkVehicle(this); // unParkVehcile
        t.updateTicket(t.parkingSpot);
    }
}
class ParkingService{
    private final Map<VehicleType, List<ParkingSpot>> parkingPositions;
    ParkingService(Map<VehicleType,List<ParkingSpot>>parkingPositions){
        this.parkingPositions = parkingPositions;
    }
    public ParkingSpot findBestPossiblePlace(VehicleType v){
        ParkingSpot ans = null;
        // System.out.println(parkingPositions);
        for(Map.Entry<VehicleType,List<ParkingSpot>>entry:parkingPositions.entrySet()){
            if(entry.getKey().equals(v)){
                List<ParkingSpot>p = entry.getValue();
                for(int i=0;i<p.size();i++){
                    if(p.get(i).getStatus() == ParkingSpotStatus.AVAILABLE){
                        ans = p.get(i);
                        // System.out.println(p.get(i));
                        break;
                    }
                }
            }
        }
        return ans;
    }
    public ParkingSpot getVehicleCurrentPosition(Vehicle v){
        List<ParkingSpot>p=parkingPositions.get(v.getVehicleType());
        if(!p.equals(null)){
            for(ParkingSpot it:p){
                if(it.getVehicleDetails().equals(v)){
                    return it;
                }
            }
        }
        return null;
    }
}
class Server{
    private List<ParkingSpot>parkingSpots;
    private Entry entryGate;
    private Exit exitGate;
    private Map<VehicleType,List<ParkingSpot>>parkingPositions;
    private Server(){}
    private static Server server = null;
    Server(List<ParkingSpot>parkingSpots,Map<VehicleType,List<ParkingSpot>>parkingPositions,Entry entryGate){
        this.parkingPositions = parkingPositions;
        this.parkingSpots = parkingSpots;
        this.entryGate = entryGate;
    }
    public static Server getServerInstance(List<ParkingSpot>parkingSpots,Map<VehicleType,List<ParkingSpot>>parkingPositions,Entry entryGate){
        if(server == null){
            synchronized(Server.class){
                if(server == null){
                    server = new Server(parkingSpots,parkingPositions,entryGate);
                }
            }
        }
        return server;
    }
}
public class ParkingLot{
    private static ParkingLot parkingLot = null;
    private ParkingLot(){}
    private ParkingLot(Map<Integer,List<ParkingSpot>>parkingSpots,Map<VehicleType,List<ParkingSpot>>vehicleTypeParkingSpots){
        this.parkingSpots = parkingSpots;
        this.vehicleTypeParkingSpots = vehicleTypeParkingSpots;
    }{

    }
    private Map<Integer,List<ParkingSpot>>parkingSpots;
    private Map<VehicleType,List<ParkingSpot>>vehicleTypeParkingSpots;
    private List<Vehicle>vehicles;
    public static ParkingLot getParkingLotInstance(Map<Integer,List<ParkingSpot>>parkingSpots,Map<VehicleType,List<ParkingSpot>>vehicleTypeParkingSpots) {
        if (parkingLot == null) {
            synchronized (ParkingLot.class) {
                if (parkingLot == null) {
                    parkingLot = new ParkingLot(parkingSpots,vehicleTypeParkingSpots);
                }
            }
        }
        return parkingLot;
    }
    public static void main(String []args){
        Map<Integer,List<ParkingSpot>>parkingSpots = new HashMap<>();
        Map<VehicleType,List<ParkingSpot>>vehicleTypeParkingSpots = new HashMap<>();
        Server server = null;
        List<ParkingSpot>tw1 = new ArrayList<>();
        List<ParkingSpot>fw1 = new ArrayList<>();
        for(int i=1;i<=5;i++){
            parkingSpots.putIfAbsent(i,new ArrayList<>());
        }
        TWParkingSpot tw = new TWParkingSpot(1);
        parkingSpots.get(1).add(tw);
        tw1.add(tw);
        tw = new TWParkingSpot(2);
        tw1.add(tw);
        parkingSpots.get(1).add(tw);
        FWParkingSpot fw = new FWParkingSpot(3);
        fw1.add(fw);
        parkingSpots.get(2).add(fw);

        vehicleTypeParkingSpots.putIfAbsent(VehicleType.TW,tw1);
        vehicleTypeParkingSpots.putIfAbsent(VehicleType.FW,fw1);

        Entry entry = new Entry(1,1);
        Entry entry2 = new Entry(2,2);

        ParkingLot p = ParkingLot.getParkingLotInstance(parkingSpots, vehicleTypeParkingSpots);
        if(!p.equals(null)){
            Vehicle v1 = new Vehicle(1, "MH01CT", VehicleType.TW);
            Vehicle v2 = new Vehicle(2, "CD590", VehicleType.TW);
            Vehicle v3 = new Vehicle(3, "FOURW2", VehicleType.FW);
            server = Server.getServerInstance(tw1, vehicleTypeParkingSpots, entry);  // make server singleton
            Ticket t= server.issueTicket(VehicleType.TW,v1,1);
            System.out.println("304\n"+t);
            v1.parkVehicle(t);
            System.out.println("333\n"+t);
            List<ParkingSpot>test = parkingSpots.get(1);
            System.out.println(test+"\n");
            List<ParkingSpot>test2 = vehicleTypeParkingSpots.get(VehicleType.TW);
            System.out.println(test2+"\n");
            server.getVehicleCurrentPosition(v1);
            server = Server.getServerInstance(fw1, vehicleTypeParkingSpots, entry2);

            Ticket t1= server.issueTicket(VehicleType.FW,v3,2);
            System.out.println("330\n"+t1);
            v3.parkVehicle(t1);
            List<ParkingSpot>test3 = parkingSpots.get(2);
            System.out.println(test3+"\n");
            List<ParkingSpot>test4 = vehicleTypeParkingSpots.get(VehicleType.FW);
            System.out.println(test4+"\n");
            ParkingSpot pans=server.getVehicleCurrentPosition(v3);
            System.out.println(pans);
            v1.unparkVehicle(t);
            System.out.println("356\n"+t);
            // Exit ex1 = new Exit(1, 1);
            // ex1.verifyTicket(t1);
        }
    }
}



// Actors - Vehicle,User,Security,system

// User
// - park vehicle,unpark,pay
//System - findBestPossiblePlace,calculatecost,getVehiclePosition,generateticket
// Security - verify ticket

// implement strategy pattern for payment methods