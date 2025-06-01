package RideSharing;
import java.util.*;
enum AccountStatus{
    ACTIVE,
    INACTIVE
}
enum AccountType{
    DRIVER,
    RIDER
}
enum VehicleType{
    TW,
    FW
}
enum RideStatus{
    RIDE_PENDING,
    RIDE_ACCEPTED,
    RIDE_ONGOING,
    RIDE_COMPLETED,
    RIDE_CANCELLED
}
class Location{
    String address;
}
class Account{
    private String userName;
    private String password;
    private AccountStatus accountStatus;
    private AccountType accountType;
    private String createdAt;
}
interface IRegisterUserStrategy{
    UserProfile registerUser();
    UserProfile getUser();
}
class RegisterRider implements IRegisterUserStrategy{
    private UserProfile u;
    RegisterRider(UserProfile u){
        this.u = u;
    }
    public UserProfile registerUser(){
        return new Rider(u);
    }
    public UserProfile getUser(){
        return this.u;
    }
}
class RegisterDriver implements IRegisterUserStrategy{
    private UserProfile u;
    private List<Vehicle>v;
    private String licenseNo;
    RegisterDriver(UserProfile u,List<Vehicle>v,String licenseNo){
        this.u = u;
        this.v = v;
        this.licenseNo = licenseNo;
    }
    public UserProfile registerUser(){
        return new Driver(u,v,licenseNo);
    }
    public UserProfile getUser(){
        return this.u;
    }
}
class UserProfile{
    private int id;
    private Account account;
    UserProfile(int id,Account account){
        this.id = id;
        this.account = account;
    }
    public int getUserId(){
        return this.id;
    }
    public Account getAccount(){
        return this.account;
    }
}
class Rider extends UserProfile{
    Rider(UserProfile u){
        super(u.getUserId(), u.getAccount());
    }
}
class Driver extends UserProfile{
    private List<Vehicle>v;
    private String licenseNo;
    Driver(UserProfile u,List<Vehicle>v,String licenseNo){
        super(u.getUserId(), u.getAccount());
        this.v = v;
        this.licenseNo = licenseNo;
    }
}
abstract class Vehicle{
    private String regNo;
    public Vehicle(String regNo) {
        this.regNo = regNo;
    }
    public abstract VehicleType getType();
}
abstract class FWVehicle extends Vehicle{
    FWVehicle(String regNo) {
        super(regNo);
    }
    public VehicleType getType() {
        return VehicleType.FW;
    }
}
class SUV extends FWVehicle{
    SUV(String regNo) {
        super(regNo);
    }
}
class SUDAN extends FWVehicle{
    SUDAN(String regNo){
        super(regNo);
    }
}
class XL extends FWVehicle{
    XL(String regNo){
        super(regNo);
    }
}
abstract class TWVehicle extends Vehicle{
    TWVehicle(String regNo) {
        super(regNo);
    }
    public VehicleType getType() {
        return VehicleType.TW;
    }
}
class BIKE extends TWVehicle{
    BIKE(String regNo){
        super(regNo);
    }
}
class Ride{
    private int id;
    private RideStatus rideStatus;
    private Vehicle v;
    private int initialCost;
    private Date expectedTimeOfArrival;
    private Location dst;
    private Location src;
    Ride(int id,Vehicle v,Location src,Location dst,int initialCost,Date expectedTimeOfArrival){
        this.id = id;
        this.rideStatus = RideStatus.RIDE_PENDING;
        this.v = v;
        this.src = src;
        this.dst = dst;
        this.initialCost = initialCost;
        this.expectedTimeOfArrival = expectedTimeOfArrival;
    }
    public RideStatus getRideStatus(){
        return this.rideStatus;
    }
}
interface ISearchRideStrategy{
    List<Driver>findMeRide(Location src,Location dst,Vehicle v);
}
class InsideCityRideStrategy implements ISearchRideStrategy{
    private List<Driver>drivers;
    private ILocationService locationService;
    InsideCityRideStrategy(List<Driver>drivers,ILocationService locationService){
        this.drivers = drivers;
        this.locationService = locationService;
    }
    public List<Driver>findMeRide(Location src,Location dst,Vehicle v){
        
    }
}
class OutSideCityRideStrategy implements ISearchRideStrategy{
    private List<Driver>drivers;
    private ILocationService locationService;
    OutSideCityRideStrategy(List<Driver>drivers,ILocationService locationService){
        this.drivers = drivers;
        this.locationService = locationService;
    }
    public List<Driver>findMeRide(Location src,Location dst,Vehicle v){

    }
}
interface IRiderActions {
    Ride requestRide(Location src, Location dst, Vehicle v);
    void cancelRide(int rideId);
}
interface IDriverActions {
    Ride acceptRide(int rideId);
    void rejectRide(int rideId);
}
class RiderActions implements IRiderActions {
    private IRideService rideService;
    private int riderId;

    RiderActions(IRideService rideService, int riderId) {
        this.rideService = rideService;
        this.riderId = riderId;
    }

    public Ride requestRide(Location src, Location dst, Vehicle v) {
        return rideService.createRide(src, dst, v, riderId);
    }

    public void cancelRide(int rideId) {
        Ride r = rideService.getRideById(rideId);
        r.setRideStatus(RideStatus.RIDE_CANCELLED);
    }
}
interface IRideService {
    Ride createRide(Location src, Location dst, Vehicle v, int riderId);
    Ride getRideById(int rideId);
}
class RideService implements IRideService{
    private ISearchRideStrategy searchStrategy;
    private Map<Integer, Ride> rideMap = new HashMap<>();
    private int rideIdCounter = 0;
    public RideService(ISearchRideStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }
    public Ride createRide(Location src, Location dst, Vehicle v, int riderId) {
        List<Driver> availableDrivers = searchStrategy.findMeRide(src, dst, v);

        if (availableDrivers.isEmpty()) return null;
        Driver selectedDriver = availableDrivers.get(0);

        Ride newRide = new Ride(++rideIdCounter, v, src, dst, 100, new Date());
        rideMap.put(newRide.getId(), newRide);

        return newRide;
    }
    public void cancelRide(){

    }
    public RideStatus getRideStatus(){

    }
    public Boolean updateRideStatus(){

    }
}
interface ILocationService {
    void updateLocation(int userId, Location location);
    Location getLocation(int userId);
}
class InMemoryLocationService implements ILocationService {
    private Map<Integer, Location> userLocationMap = new HashMap<>();

    public void updateLocation(int userId, Location location) {
        userLocationMap.put(userId, location);
    }

    public Location getLocation(int userId) {
        return userLocationMap.get(userId);
    }
}
// interface IDriverRideAction{
//     Ride acceptRide();
//     void rejectRide();
// }
// class RideAction implements IDriverRideAction{
//     public Ride acceptRide(){

//     }
//     public void rejectRide(){

//     }
// }
public class RideSharing {
    private static RideSharing rideSharing = null;
    private RideSharing(){}
    public static RideSharing getRideSharingInstance(){
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
// users - request a ride,cancel ride, rate driver,get ride status
// driver - set availabilty status,accept or reject ride,update ride status
// admin - view total rides,blacklist user/rider
// system - find best ride,maintain ride status,compute cost,concurrent