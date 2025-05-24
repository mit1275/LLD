import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
enum ElevatorStatus{
    MOVING,
    STOP
}
enum MovingDirection{
    UP,
    DOWN
}
interface Button { }

interface PressableButton extends Button {
    void pressButton();
}
class ElevatorButton implements PressableButton{
    private int destinationFloor;
    private ElevatorCar car;
    private RequestScheduler requestScheduler;
    public ElevatorButton(int destinationFloor, ElevatorCar car, RequestScheduler scheduler) {
        this.destinationFloor = destinationFloor;
        this.car = car;
        this.requestScheduler = scheduler;
    }
    public void pressButton(){
        MovingDirection direction = (destinationFloor > car.getCurrentFloor()) 
            ? MovingDirection.UP 
            : MovingDirection.DOWN;

        Request request = new Request(car.getCurrentFloor(), destinationFloor, direction);
        requestScheduler.addRequest(car, request);
        requestScheduler.serveRequest(car);
    }
}
interface DirectionalPressableButton extends Button {
    void pressButton(MovingDirection direction);
}
class HallButton implements DirectionalPressableButton{
    private int floor;
    private ElevatorScheduler scheduler;
    public HallButton(int floor, ElevatorScheduler scheduler) {
        this.floor = floor;
        this.scheduler = scheduler;
    }
    public void pressButton(MovingDirection direction) {
        Request request = new Request(floor, floor, direction);
        scheduler.scheduleElevator(request);
    }
}
interface ElevatorScheduler{
    void scheduleElevator(Request r);
}
class ScheduleElevatorByDistance implements ElevatorScheduler{
    private List<ElevatorCar>elevatorCars;
    private RequestScheduler requestScheduler;
    ScheduleElevatorByDistance(List<ElevatorCar>elevatorCars,RequestScheduler requestScheduler){
        this.elevatorCars = elevatorCars;
        this.requestScheduler = requestScheduler;
    }
    public void scheduleElevator(Request r){
        ElevatorCar el = this.findBestElevatorCar(this.elevatorCars,r);
        if(el!=null){
            requestScheduler.addRequest(el, r);
            requestScheduler.serveRequest(el);
        }
    }
    public ElevatorCar findBestElevatorCar(List<ElevatorCar>elevatorCars,Request r){
        int ans=100;
        ElevatorCar z=null;
        for(int i=0;i<elevatorCars.size();i++){
            ElevatorCar e = elevatorCars.get(i);
            if(e.getMovingStatus() == ElevatorStatus.STOP){
                int x=Math.abs(e.getCurrentFloor()-r.getDestinationFloor());
                if(ans>x){
                    ans = x;
                    z=e;
                }
            }
            else if(r.getMovingDirection() == e.getMovingDirection()){
                if(r.getMovingDirection() == MovingDirection.UP && e.getCurrentFloor() <= r.getDestinationFloor()){
                    int x=(e.getCurrentFloor()-r.getDestinationFloor());
                        if(ans>x){
                            ans = x;
                            z=e;
                        }
                }
                else if(r.getMovingDirection() == MovingDirection.DOWN && e.getCurrentFloor()>=r.getDestinationFloor()){
                    int x=Math.abs(e.getCurrentFloor()-r.getDestinationFloor());
                        if(ans>x){
                            ans = x;
                            z=e;
                        }
                }
            }
        }
        return z;
    }
}
interface RequestScheduler{
    void addRequest(ElevatorCar car,Request request);
    void serveRequest(ElevatorCar car);
}
class ServeRequestQueue implements RequestScheduler{
    private Map<ElevatorCar, PriorityQueue<Integer>> pq1;
    private Map<ElevatorCar, PriorityQueue<Integer>> pq2;
    ServeRequestQueue(){
        pq1 = new HashMap<>();
        pq2= new HashMap<>();
    }
    public void serveRequest(ElevatorCar car){
        while(!pq1.get(car).isEmpty() || !pq2.get(car).isEmpty()){
            if(car.getMovingStatus() == ElevatorStatus.STOP){
                if (!pq1.get(car).isEmpty()) {
                    car.setMovingDirection(MovingDirection.UP);
                    car.setElevatorStatus(ElevatorStatus.MOVING);
                } else if (!pq2.get(car).isEmpty()) {
                    car.setMovingDirection(MovingDirection.DOWN);
                    car.setElevatorStatus(ElevatorStatus.MOVING);
                } else {
                    break;
                }
            }
            else if(car.getMovingDirection() == MovingDirection.UP){
                while(!pq1.get(car).isEmpty() && (car.getCurrentFloor()<=pq1.get(car).peek())) {
                    car.setCurrentFloor(pq1.get(car).poll());
                }
                car.setMovingDirection(MovingDirection.DOWN);
                car.setElevatorStatus(ElevatorStatus.STOP);
            }
            else if(car.getMovingDirection() == MovingDirection.DOWN){
                while(!pq2.get(car).isEmpty() && (car.getCurrentFloor()>=pq2.get(car).peek())) {
                    car.setCurrentFloor(pq2.get(car).poll());
                }
                car.setMovingDirection(MovingDirection.UP);
                car.setElevatorStatus(ElevatorStatus.STOP);
            }
        }
    }
    public void addRequest(ElevatorCar car,Request request){
        pq1.putIfAbsent(car, new PriorityQueue<>());
        pq2.putIfAbsent(car, new PriorityQueue<>(Collections.reverseOrder()));
        if(car.getCurrentFloor()<request.getDestinationFloor()){
            pq1.get(car).add(request.getDestinationFloor());
        }else{
            pq2.get(car).add(request.getDestinationFloor());
        }
    }
}
class ElevatorCar{
    private int id;
    private ElevatorStatus elevatorStatus;
    private List<ElevatorButton>sideButtons;
    private MovingDirection movingDirection;
    private int curFloor;
    ElevatorCar(int id){
        this.id = id;
        this.elevatorStatus = ElevatorStatus.STOP;
        this.curFloor = 0;
        this.movingDirection = null;
    }
    public ElevatorStatus getMovingStatus(){
        return this.elevatorStatus;
    }
    public MovingDirection getMovingDirection(){
        return movingDirection;
    }
    public int getCurrentFloor(){
        return curFloor;
    }
    public void setMovingDirection(MovingDirection movingDirection){
        this.movingDirection = movingDirection;
    }
    public void setElevatorStatus(ElevatorStatus elevatorStatus){
        this.elevatorStatus = elevatorStatus;
    }
    public void setCurrentFloor(int curFloor){
        this.curFloor = curFloor;
    }
    @Override
    public String toString(){
        return "ElevatorCar "+this.id;
    }
}
class Request{
    private int id;
    private int dstFloor;
    private MovingDirection movingDirection;
    Request(int id,int dstFloor,MovingDirection movingDirection){
        this.id = id;
        this.dstFloor = dstFloor;
        this.movingDirection = movingDirection;
    }
    public Request getRequestObj(){
        return this;
    }
    public int getDestinationFloor(){
        return this.dstFloor;
    }
    public MovingDirection getMovingDirection(){
        return this.movingDirection;
    }
}
class Building{
    private List<ElevatorCar>elevatorCars;
    Building(List<ElevatorCar>elevatorCars){
        this.elevatorCars = elevatorCars;
    }
}
public class Elevator {
    private static Elevator elevator = null;
    private Elevator(){};
    private List<ElevatorCar>elevatorCar;
    private int numFloors;
    private Elevator(List<ElevatorCar>elevatorCar,int numFloors){
        this.elevatorCar = elevatorCar;
        this.numFloors = numFloors;
    }
    public static Elevator getElevatorInstance(List<ElevatorCar>elevatorCar,int numFloors){
        if(elevator == null){
            synchronized(Elevator.class){
                if(elevator == null){
                    elevator = new Elevator(elevatorCar,numFloors);
                }
            }
        }
        return elevator;
    }
    public static void main(String []args){
        ElevatorCar elevator1 = new ElevatorCar(1);
        ElevatorCar elevator2 = new ElevatorCar(2);
        List<ElevatorCar> elevators = Arrays.asList(elevator1, elevator2);
        RequestScheduler requestScheduler = new ServeRequestQueue();

        // Elevator scheduler
        ElevatorScheduler elevatorScheduler = new ScheduleElevatorByDistance(elevators, requestScheduler);

        // Step 2: Press hall button on floor 0 to go UP
        HallButton hallButton = new HallButton(0, elevatorScheduler);
        System.out.println("User on Floor 0 presses UP hall button");
        hallButton.pressButton(MovingDirection.UP);

        ElevatorButton buttonInsideElevator = new ElevatorButton(5, elevator1, requestScheduler);
        System.out.println("User inside elevator presses button for Floor 5");
        buttonInsideElevator.pressButton();

        // Step 4: Print final state
        System.out.println("Elevator final floor: " + elevator1.getCurrentFloor());
        System.out.println("Elevator status: " + elevator1.getMovingStatus());
        System.out.println("Elevator direction: " + elevator1.getMovingDirection());
    }
}
