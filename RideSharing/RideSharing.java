package RideSharing;

import java.util.*;

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
    public VehicleType getType() { return VehicleType.TW; }
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
    void assignLocation();
}
class LocationService implements ILocationService{
    private INotificationManager iNotificationManager;
    LocationService(INotificationManager iNotificationManager){
        this.iNotificationManager = iNotificationManager;
    }
    public void assignLocation(){

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