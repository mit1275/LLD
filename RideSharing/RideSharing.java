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
enum Availability{
    AVAILABLE,
    NOT_AVAILABLE
}
enum RequestStatus{
    CREATED,
    PENDING,
    COMPLETED,
    CANCELLED
}
interface IRideCostStrategy{
    double getPrice();
}
interface PriceComponent{
    double getPrice();
}
class BaseCharge{
    private double price;
    BaseCharge(double price){
        this.price = price;
    }
    public double getCost(){
        return this.price;
    }
}
class DistanceBasedPricing implements PriceComponent {
    private PriceComponent component;
    private double distance;
    private double ratePerKm;

    DistanceBasedPricing(PriceComponent component, double distance, double ratePerKm) {
        this.component = component;
        this.distance = distance;
        this.ratePerKm = ratePerKm;
    }

    public double getPrice() {
        return component.getPrice() + (distance * ratePerKm);
    }
}
class TimeBasedPricing implements PriceComponent {
    private PriceComponent component;
    private double timeInMinutes;
    private double ratePerMinute;

    TimeBasedPricing(PriceComponent component, double timeInMinutes, double ratePerMinute) {
        this.component = component;
        this.timeInMinutes = timeInMinutes;
        this.ratePerMinute = ratePerMinute;
    }

    public double getPrice() {
        return component.getPrice() + (timeInMinutes * ratePerMinute);
    }
}
class SurgePricing implements PriceComponent {
    private PriceComponent component;
    private double surgeMultiplier;

    SurgePricing(PriceComponent component, double surgeMultiplier) {
        this.component = component;
        this.surgeMultiplier = surgeMultiplier;
    }

    public double getPrice() {
        return component.getPrice() * surgeMultiplier;
    }
}
class TWRideCostStrategy implements IRideCostStrategy{
    private double distance;
    private double timeInMinutes;
    private PriceComponent priceComponent;
    TWRideCostStrategy(double distance, double timeInMinutes) {
        this.distance = distance;
        this.timeInMinutes = timeInMinutes;
    }
    public double getPrice(){

    }
}
class FWRideCostStrategy implements IRideCostStrategy{
    private double distance;
    private double timeInMinutes;
    private PriceComponent priceComponent;
    FWRideCostStrategy(double distance, double timeInMinutes) {
        this.distance = distance;
        this.timeInMinutes = timeInMinutes;
    }
    public double getPrice(){

    }
}
class Request{
    private String userId;
    private Integer id;
    private Location startLocation;
    private Location endLocation;
    private RequestStatus requestStatus;
    Request(String userId,Integer id,Location startLocation,Location endLocation){
        this.userId = userId;
        this.id = id;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.requestStatus = RequestStatus.CREATED;
    }
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
    // new Ride(userId,null,curLocation,dstLocation);
    Ride(String riderId,String driverId,Location startPoint,Location dstPoint){
        this.riderId = riderId;
        this.driverId = driverId;
        this.id=1;
        this.riderStatus = RideStatus.REQUESTED;
        this.startPoint = startPoint;
        this.dstPoint = dstPoint;
    }
    public void updateDriver(String driverId){
        this.driverId = driverId;
    }
    public void updateCost(Integer cost){
        this.totCost = totCost;
    }
    public String getRiderId(){
        return this.riderId;
    }
    public String getDriverId(){
        return this.driverId;
    }
}
class User{
    private String id;
    private String displayName;
    private Account account;
    private Availability availability;
    User(String displayName,Account account){
        this.displayName = displayName;
        this.account = account;
        this.availability = Availability.AVAILABLE;
    }
    public String getId(){
        return this.id;
    }
    public void updateAvailability(Availability availability){
        this.availability = availability;
    }
}
class Driver extends User{
    private VehicleModel vehicleModel;
    private PriorityQueue<Request>requestQueue;
    Driver(VehicleModel vehicleModel,String displayName,Account account){
        super(displayName,account);
        this.vehicleModel = vehicleModel;
        requestQueue = new PriorityQueue<>();
    }
}
interface IUserServiceStrategy{
    void cancelRide(Ride ride);
}
class DriverServiceStrategy implements IUserServiceStrategy{
    private IRequestService requestService;
    private IUserRepository userRepository;
    DriverServiceStrategy(IRequestService requestService,IUserRepository userRepository){
        this.requestService = requestService;
        this.userRepository = userRepository;
    }
    public void acceptRide(String userId,Ride ride){
        User u = userRepository.getUser(userId);
        u.updateAvailability(Availability.NOT_AVAILABLE);
        ride.updateDriver(userId);
    }
    public void cancelRide(Ride ride){
        User u = userRepository.getUser(ride.getDriverId());
        u.updateAvailability(Availability.AVAILABLE);
        ride.updateDriver(null);
    }
    public void swipeRide(String driverId,Request request){
        requestService.removeRequest(driverId,request);
    }
    public void updateAvailability(){

    }
}
class RiderServiceStrategy implements IUserServiceStrategy{
    private final IRequestService requestService;
    RiderServiceStrategy(IRequestService requestService){
        this.requestService = requestService;
    }
    public void requestRide(String userId,Location curLocation,Location dstLocation){
        Request r = new Request(userId,1, curLocation, dstLocation);
        requestService.addRequest(userId, r);
    }
    public void cancelRide(Ride ride){
        
    }
}
interface IUserRepository{
    void registerUser(User u);
    User getUser(String userId);
}
class DriverRepository implements IUserRepository{
    private final Map<String,User>driverRepo;
    private final Map<String,PriorityQueue<Request>>requestQueue;
    DriverRepository(){
        driverRepo = new HashMap<>();
        requestQueue = new HashMap<>();
    }
    public void registerUser(User u){
        driverRepo.put(u.getId(),u);
    }
    public void addRequest(String userId,Request request){
        requestQueue.get(userId).add(request);
    }
    public PriorityQueue<Request>getRequestList(String userId){
        return requestQueue.get(userId);
    }
    public User getUser(String userId){
        return driverRepo.get(userId);
    }
}
class RiderRepository implements IUserRepository{
    private final Map<String,User>userRepo;
    private final Map<String,PriorityQueue<Request>>requestQueue;
    RiderRepository(){
        userRepo = new HashMap<>();
        requestQueue = new HashMap<>();
    }
    public void registerUser(User u){
        userRepo.put(u.getId(),u);
    }
    public User getUser(String userId){
        return userRepo.get(userId);
    }
}
interface IRequestService{
    void addRequest(String driverId,Request request);
    void removeRequest(String driverId,Request request);
}
class RequestService implements IRequestService{
    private IUserRepository userRepository;
    RequestService(IUserRepository userRepository){
        this.userRepository = userRepository;
    }
    public void addRequest(String driverId,Request request){
        userRepository.addRequest(driverId,request);
    }
    public void removeRequest(String driverId,Request request){

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
    List<Vehicle> searchRide(Location cuLocation,Location dstLocation,VehicleModel vehicleModel);
    void cancelRide(Ride ride);
    void completePaymentForRide();
}
interface IDriverModeService{
    void acceptRide();
    void cancelRide();
}
class RiderService implements IRiderModeService{
    private final ISearchRide searchRide;
    private final IUserServiceStrategy userServiceStrategy;
    RiderService(ISearchRide searchRide,IUserServiceStrategy userServiceStrategy){
        this.searchRide = searchRide;
        this.userServiceStrategy = userServiceStrategy;
    }
    public List<Vehicle>searchRide(Location cuLocation,Location dstLocation,VehicleModel vehicleModel){
        return searchRide.searchRide(cuLocation, dstLocation, vehicleModel);
    }
    public void cancelRide(Ride ride){
        userServiceStrategy.cancelRide(ride);
    }
    public void completePaymentForRide(){

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