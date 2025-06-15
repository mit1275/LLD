package RideSharing;
import java.util.*;
enum AccountStatus{
    ACTIVE,
    INACTIVE
}
enum AccountMode{
    RIDER,
    DRIVER
}
enum RideStatus{
    REQUESTED,
    ACCEPTED,
    CANCELLED,
    COMPLETED
}
class Account{
    private String username,password;
    private String mobileNumber;
    private AccountStatus accountStatus;
    private AccountMode accountMode;
}
class Ride{
    private int id;
    private String driverId;
    private String riderId;
    private Location startPoint;
    private Location dstPoint;
    private RideStatus riderStatus;
    private Integer totCost;
    private Date rideCreated;
    private Date rideAssigned;
    private Date rideCompleted;
    private VehicleModel vehicleModel;
    // build audit logs for ride timings
}
class User{
    private String id;
    private String displayName;
    private Account account;
    User(String displayName,Account account){
        this.displayName = displayName;
        this.account = account;
    }
}
interface ISearchRide{
    List<Vehicle>searchRide(Location currentLocation,Location dstLocation,VehicleModel vehicleModel);
}
interface IVehicleRepository {
    void registerVehicle(String driverId, Vehicle vehicle, Location location);
    void updateVehicleLocation(String driverId, Location location);
    List<Vehicle> getAvailableVehicles();
}
class InMemoryVehicleRepository implements IVehicleRepository {
    private final Map<String, Vehicle> vehicleMap = new HashMap<>();
    private final Map<String, Location> locationMap = new HashMap<>();

    public void registerVehicle(String driverId, Vehicle vehicle, Location location) {
        vehicleMap.put(driverId, vehicle);
        locationMap.put(driverId, location);
    }

    public void updateVehicleLocation(String driverId, Location location) {
        locationMap.put(driverId, location);
    }

    public List<Vehicle> getAvailableVehicles() {
        return new ArrayList<>(vehicleMap.values());
    }
    
    public Map<String, Location> getLocationMap() {
        return locationMap;
    }
}
interface IRideAssignmentStrategy{
    List<Vehicle>getMeBestRide(Location currentLocation,Location dstLocation,VehicleModel vehicleModel);
}
class ShortestTimeRideStrategy implements IRideAssignmentStrategy{
    private IVehicleRepository vehicleRepository;

    ShortestTimeRideStrategy(IVehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }
    public List<Vehicle>getMeBestRide(Location currentLocation,Location dstLocation,VehicleModel vehicleModel){
        List<Vehicle> candidates = new ArrayList<>();
        for (Vehicle v : vehicleRepository.getAvailableVehicles()) {
            if (v instanceof FW && vehicleModel instanceof FWVehicleModel) {
                candidates.add(v);
            }
            if (v instanceof TW && vehicleModel instanceof TWVehicleModel) {
                candidates.add(v);
            }
        }
        return candidates;
    }
}
class SearchRide implements ISearchRide{
    private IRideAssignmentStrategy rideAssignmentStrategy;
    SearchRide(IRideAssignmentStrategy rideAssignmentStrategy){
        this.rideAssignmentStrategy = rideAssignmentStrategy;
    }
    public List<Vehicle>searchRide(Location currentLocation,Location dstLocation,VehicleModel vehicleModel){
        return rideAssignmentStrategy.getMeBestRide(currentLocation, dstLocation, vehicleModel);
    }
}
interface IRiderModeService{
    void searchRide(Location cuLocation,Location dstLocation,VehicleModel vehicleModel);
    void cancelRide(Ride ride);
    void completePaymentForRide();
    // void rideHistory();
}
interface IDriverModeService{
    void acceptRide();
    void cancelRide();
}
class RiderService implements IRiderModeService{
    private final ISearchRide searchRide;
    RiderService(ISearchRide searchRide){
        this.searchRide = searchRide;
    }
    public List<Vehicle>searchRide(Location cuLocation,Location dstLocation,VehicleModel vehicleModel){
        return searchRide.searchRide(cuLocation, dstLocation, vehicleModel);
    }
}
enum VehicleType{
    TW,
    FW
}
enum EntityType{
    RIDER,
    DRIVER
}
class Location{
    String latitude, longitude;
    String entityId;
    EntityType type;
}
abstract class Vehicle{
    private String regNo;
}
class VehicleFactoryProducer {
    public static VehicleFactory getFactory(VehicleType vehicleType) {
        switch(vehicleType) {
            case TW:
                return new TWVehicleFactory();
            case FW:
                return new FWVehicleFactory();
            default:
                throw new IllegalArgumentException("Unsupported vehicle type");
        }
    }
}
class TW extends Vehicle{
}
class FW extends Vehicle{
    private int numofSeats;
}
interface VehicleFactory{
    Vehicle createVehicle(VehicleType vehicleType,VehicleModel vehicleModel);
}
class TWVehicleFactory implements VehicleFactory{
    TWVehicleFactory(){

    }
    public Vehicle createVehicle(VehicleType vehicleType,VehicleModel vehicleModel){
        TW vehicle = new TW();
        return vehicle;
    }
}
class FWVehicleFactory implements VehicleFactory{
    FWVehicleFactory(){

    }
    public Vehicle createVehicle(VehicleType vehicleType,VehicleModel vehicleModel){
        FW vehicle = new FW();
        return vehicle;
    }
}
abstract class VehicleModel{
    private String name;
    private String features;
    abstract VehicleType getType();
}
class TWVehicleModel extends VehicleModel{
    public VehicleType getType() { return VehicleType.TW; }
}
class FWVehicleModel extends VehicleModel{
    public VehicleType getType() { return VehicleType.FW; }
}
class Bike extends TWVehicleModel{

}
class EVBike extends TWVehicleModel{

}
class SUV extends FWVehicleModel{

}
class Sudan extends FWVehicleModel{

}
interface ILocationService{
    void updateLocation(Location location);
    void assignLocation(String userId,Location location);
}
interface ICacheLocationManager{
    Location getLocation(String userId);
    void setLocation(String userId,Location location);
}
class CacheManager implements ICacheLocationManager{
    private final Map<String,Location>mp;
    CacheManager(){
        mp = new HashMap<>();
    }
    public Location getLocation(String userId){
        return mp.get(userId);
    }
    public void setLocation(String userId,Location location){
        mp.put(userId, location);
    }
}
class LocationService implements ILocationService{
    private INotificationManager iNotificationManager;
    private ICacheLocationManager iCacheLocationManager;
    LocationService(INotificationManager iNotificationManager,ICacheLocationManager iCacheLocationManager){
        this.iNotificationManager = iNotificationManager;
        this.iCacheLocationManager = iCacheLocationManager;
    }
    public void assignLocation(String userId,Location location){
        iCacheLocationManager.setLocation(userId, location);
    }
    public void updateLocation(Location location){
        iNotificationManager.notify(location);
    }
}
interface ILocationObserver{
    void receiveLocation(Location location);
}
class WebSocketLocationObserver implements ILocationObserver{
    public void receiveLocation(Location location){
        System.out.println("Location Received "+location);
    }
    @Override
    public String toString(){
        return ("Location Received at 98");
    }
}
interface INotificationManager{
    void notify(Location location);
    void subscribe(ILocationObserver locationObserver);
    void unsubscribe(ILocationObserver locationObserver);
}
class NotificationManager implements INotificationManager{
    private final List<ILocationObserver> observers = new ArrayList<>();
    public void notify(Location location){
        for(int i=0;i<observers.size();i++){
            ILocationObserver locationObserver = observers.get(i);
            locationObserver.receiveLocation(location);
        }
    }
    public void subscribe(ILocationObserver locationObserver){
        observers.add(locationObserver);
    }
    public void unsubscribe(ILocationObserver locationObserver){
        observers.remove(locationObserver);
    }
}
public class RideSharing{
    private static RideSharing rideSharing = null;
    RideSharing(){}
    public static RideSharing getRideInstance(){
        if(rideSharing == null){
            synchronized(RideSharing.class){
                if(rideSharing == null){
                    rideSharing = new RideSharing();
                }
            }
        }
        return rideSharing;
    }
    public static void main(String []args){

    }
}