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
class Account{
    private String username,password;
    private String mobileNumber;
    private AccountStatus accountStatus;
    private AccountMode accountMode;
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
    void searchRide(Location currentLocation,Location dstLocation,VehicleModel vehicleModel);
}
interface IRideAssignmentStrategy{
    void getMeBestRide(Location currentLocation,Location dstLocation,VehicleModel vehicleModel);
}
class ShortestTimeRideStrategy implements IRideAssignmentStrategy{
    public void getMeBestRide(Location currentLocation,Location dstLocation,VehicleModel vehicleModel){
        
    }
}
class SearchRide implements ISearchRide{
    public void searchRide(Location currentLocation,Location dstLocation,VehicleModel vehicleModel){

    }
}
interface IRiderModeService{
    void searchRide();
    void cancelRide();
    void completePaymentForRide();
    // void rideHistory();
}
interface IDriverModeService{
    void acceptRide();
    void cancelRide();
}
class RiderService implements IRiderModeService{
    private ISearchRide iSearchRide;
    RiderService(ISearchRide iSearchRide){
        this.iSearchRide = iSearchRide;
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