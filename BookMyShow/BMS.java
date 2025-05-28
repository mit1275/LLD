package BookMyShow;
import java.util.*;
import java.time.Instant;
enum SeatType{
    STANDARD,
    DELUX
}
enum SeatStatus{
    RESERVED,
    AVAILABLE,
    NOT_AVAILABLE
}
enum Language{
    ENGLISH,
    HINDI,
    TELGU
}
enum MovieType{
    ACTION,
    THRILLER,
    COMEDY
}
enum StarCount{
    FIVE,
    FOUR,
    THREE,
    TWO
}
class Comment{
    private int id;
}
class Rating{
    private int id;
    private StarCount starCount;
    private Comment comment;
    private User user;
    private Movie movie;
}
class Account{

}
class RatingService{

}
class User{
    private Search searchObj;
    private Address address;
}
class SystemUser extends User{
    private Account account;
    private int id;
    private UserService userService;
}
class Admin extends SystemUser{
    public void addMovies(){

    }
    public void addShow(){

    }
}
class UserService{
    private BookingService bookingService;
    private PaymentService paymentService;
    private RatingService ratingService;
    UserService(BookingService bookingService,PaymentService paymentService,RatingService ratingService){
        this.bookingService = bookingService;
        this.paymentService = paymentService;
        this.ratingService = ratingService;
    }
}
interface ISeatStrategy{
    int getSeatPrice();
    void getSeatFeatures();
}
class StandardSeatStrategy implements ISeatStrategy{
    private int price;
    StandardSeatStrategy(int price){
        this.price = price;
    }
    public int getSeatPrice(){
        return this.price;
    }
    public void getSeatFeatures(){
        System.out.println("Standard Features");
    }
}
class DeluxSeatStrategy implements ISeatStrategy{
    private int price;
    DeluxSeatStrategy(int price){
        this.price = price;
    }
    public int getSeatPrice(){
        return this.price;
    }
    public void getSeatFeatures(){
        System.out.println("Delux Features");
    }
}
class Seat{
    private int id;
    private SeatType seatType;
    private SeatStatus seatStatus;
    private ISeatStrategy seatStrategy;
    // private double cost;
    Seat(int id,SeatType seatType,ISeatStrategy seatStrategy){
        this.id = id;
        this.seatType = seatType;
        this.seatStatus = SeatStatus.AVAILABLE;
        this.seatStrategy = seatStrategy;
        // this.cost = cost;
    }
    public SeatType getSeatType(){
        return this.seatType;
    }
    public SeatStatus getSeatStatus(){
        return this.seatStatus;
    }
    public void updateSeatStatus(SeatStatus seatStatus){
        this.seatStatus = seatStatus;
    }
    public int getCost() {
        return seatStrategy.getSeatPrice();
    }
    public void printFeatures() {
        seatStrategy.getSeatFeatures();
    }
}
class Address{
    private String pincode,city;
}
class CinemaHall{
    private int id;
    private Address address;
    private List<Auditorium>auditorium;
}
class Auditorium{
    private int id;
    private CinemaHall cinemaHall;
    private List<Show>listOfShows;
}
class Show{
    private int id;
    private Auditorium auditorium;
    private Movie movie;
    private Date startTime;
    private int durationMins;
    private Map<SeatType,List<Seat>>seats;
    private Language language;
    public Movie getMovie(){
        return this.movie;
    }
    public int getShowId(){
        return this.id;
    }
}
class Movie{
    private int id;
    private List<Cast>movieCast;
    private Date releaseDate;
    private int duration;
    private List<Language>languagesAvailable;
    private String movieName;
    private String thumbnil;
    private MovieType movieType;
    private List<Rating>rating;
    Movie(int id){

    }
    public String getMovieName(){
        return this.movieName;
    }
}
interface ISearchStrategy{
    List<Show>getShowsByMovieName(String movieName);
    List<CinemaHall> getCinemaHallsByMovieName(String movieName);
}
class SimpleSearchStrategy implements ISearchStrategy{
    private Map<Movie,List<Show>>listOfAllShows;
    private Map<CinemaHall,List<Show>>allShowsInCinemaHall;
    SimpleSearchStrategy(Map<Movie,List<Show>>listOfAllShows, Map<CinemaHall,List<Show>>allShowsInCinemaHall){
        this.listOfAllShows = listOfAllShows;
        this.allShowsInCinemaHall = allShowsInCinemaHall;
    }
    public List<Show>getShowsByMovieName(String movieName){
        for(Map.Entry<Movie,List<Show>>entry:listOfAllShows.entrySet()){
            if(entry.getKey().getMovieName().equals(movieName)){
                return entry.getValue();
            }
        }
        return null;
    }
    public List<CinemaHall> getCinemaHallsByMovieName(String movieName) {
        List<CinemaHall> result = new ArrayList<>();
        for (Map.Entry<CinemaHall, List<Show>> entry : allShowsInCinemaHall.entrySet()) {
            for (Show show : entry.getValue()) {
                if (show.getMovie().getMovieName().equalsIgnoreCase(movieName)) {
                    result.add(entry.getKey());
                    break;
                }
            }
        }
        return result;
    }
}
class Search{
    private ISearchStrategy searchStrategy;
    Search(ISearchStrategy searchStrategy){
        this.searchStrategy = searchStrategy;
    }
    public List<Show>getShowsByMovieName(String movieName){
        return searchStrategy.getShowsByMovieName(movieName);
    }
    public List<CinemaHall> getCinemaHallsByMovieName(String movieName){
        return searchStrategy.getCinemaHallsByMovieName(movieName);
    }
}
class Booking{
    private int id;
    private Show show;
    private List<Seat>seats;
    private int totCost;
    private Ticket ticket;
    Booking(int id,Show show,List<Seat>seats,int totCost,Ticket t){
        this.id = id;
        this.show = show;
        this.totCost = totCost;
        this.seats = seats;
        this.ticket = t;
    }
}
interface ISeatAllocator{
    List<Seat>assignSeats(Request r);
}
class RandomSeatAllocator implements ISeatAllocator{
    private Map<Show,List<Seat>>showObj;
    RandomSeatAllocator(Map<Show,List<Seat>>showObj){
        this.showObj = showObj;
    }
    public List<Seat>assignSeats(Request r){
        List<Seat>s = showObj.get(r.getShow());
        Map<SeatType,Integer>mp = new HashMap<>();
        for(int i=0;i<s.size();i++){
            if(s.get(i).getSeatStatus()==SeatStatus.AVAILABLE){
               if(!mp.containsKey(s.get(i).getSeatType())){
                mp.putIfAbsent(s.get(i).getSeatType(),1);
               }else{
                 int val = mp.get(s.get(i).getSeatType());
                 mp.putIfAbsent(s.get(i).getSeatType(),val+1);
               }
            }
        }
        List<Seat>ans = new ArrayList<>();
        for(Map.Entry<SeatType,Integer>entry:r.getSeatTypes().entrySet()){
            SeatType s1 = entry.getKey();
            int count = entry.getValue();
            if(mp.containsKey(s1)){
                List<Seat>s2 = showObj.get(r.getShow());
                for(int i=0;i<s2.size();i++){
                    
                }
            }
        }
        return ans;
    }
}
class UserPreferenceSeatAllocator implements ISeatAllocator{
    private Map<Show,List<Seat>>showObj;
    UserPreferenceSeatAllocator(Map<Show,List<Seat>>showObj){
        this.showObj = showObj;
    }
    public List<Seat>assignSeats(Request r){

    }
}
interface ICostCalculator{
    int getTotCost(List<Seat>s);
}
class SimpleCostCalculatorStrategy implements ICostCalculator{
    public int getTotCost(List<Seat>s){
        int ans = 0;
        for(int i=0;i<s.size();i++){
            ans+=(s.get(i).getCost());
        }
        return ans;
    }
}
class BookingService{
    private RequestValidatorService requestValidatorService;
    private Map<Show,List<Seat>>showObj;
    private ISeatAllocator seatAllocator;
    private TicketService ticketService;
    private ICostCalculator costCalculator;
    private SeatService seatService;
    BookingService(RequestValidatorService requestValidatorService,Map<Show,List<Seat>>showObj,ISeatAllocator seatAllocator,ICostCalculator costCalculator,TicketService ticketService,SeatService seatService){
        this.requestValidatorService = requestValidatorService;
        this.showObj = showObj;
        this.seatAllocator = seatAllocator;
        this.costCalculator = costCalculator;
        this.ticketService = ticketService;
        this.seatService = seatService;
    }
    public Booking reserve(Show show,int countUser,Map<SeatType,Integer>seatTypes){
        Request r = new Request(show, countUser, seatTypes);
        if(requestValidatorService.canServeUserRequest(showObj,r)){
            List<Seat>ans = seatAllocator.assignSeats(r);
            seatService.assignSeats(ans);
            int cost = costCalculator.getTotCost(ans);
            Ticket t = ticketService.issueTicket(show,ans,cost);
            return new Booking(1,show,ans,cost,t);
        }
        return null;
    }
    // public void unBook(){

    // }
}
class Ticket{
    private int id;
    private Show show;
    private List<Seat>seats;
    private Long issueTime;
    private int totCost;
    Ticket(int id,Show show,List<Seat>seats,int totCost){
        this.id = id;
        this.show = show;
        this.issueTime = Instant.now().toEpochMilli();
        this.seats = seats;
        this.totCost = totCost;
    }
}
class TicketService{
    public Ticket issueTicket(Show show,List<Seat>seats,int totCost){
        return new Ticket(1, show, seats, totCost);
    }
    // public Boolean cancelTicket(){

    // }
}
class SeatService{
    public void assignSeats(List<Seat> seats) {
        for (Seat seat : seats) {
            seat.updateSeatStatus(SeatStatus.BOOKED);
        }
    }
    public void unBookSeats(List<Seat> seats) {
        for (Seat seat : seats) {
            seat.updateSeatStatus(SeatStatus.AVAILABLE);
        }
    }
}
class Request{
    private int id;
    private Show show;
    private int userCount;
    private Map<SeatType,Integer>seatTypes;
    private static int count = 1;
    Request(Show show,int userCount,Map<SeatType,Integer>seatTypes){
        this.id = ++count;
        this.show = show;
        this.seatTypes = seatTypes;
        this.userCount = userCount;
    }
    public Show getShow(){
        return this.show;
    }
    public Map<SeatType,Integer>getSeatTypes(){
        return this.seatTypes;
    }
    public int getUserCount(){
        return this.userCount;
    }
}
class RequestValidatorService{
    public Boolean canServeUserRequest(Map<Show,List<Seat>>showObj,Request r){
        List<Seat>s = showObj.get(r.getShow());
        Map<SeatType,Integer>mp = new HashMap<>();
        for(int i=0;i<s.size();i++){
            if(s.get(i).getSeatStatus()==SeatStatus.AVAILABLE){
               if(!mp.containsKey(s.get(i).getSeatType())){
                mp.putIfAbsent(s.get(i).getSeatType(),1);
               }else{
                 int val = mp.get(s.get(i).getSeatType());
                 mp.putIfAbsent(s.get(i).getSeatType(),val+1);
               }
            }
        }
        for(Map.Entry<SeatType,Integer>entry:r.getSeatTypes().entrySet()){
            SeatType s1 = entry.getKey();
            int count = entry.getValue();
            if(mp.containsKey(s1)){
                int val=mp.get(s1);
                if(count>val){
                    return false;
                }
            }
        }
        return true;
    }
}
public class BMS {
    private static BMS bms = null;
    private List<CinemaHall>cinemaHall;
    private List<Movie>movies;
    private BMS(){}
    private BMS(List<CinemaHall>cinemaHall,List<Movie>movies){
        this.cinemaHall = cinemaHall;
        this.movies = movies;
    }
    public static BMS getBookMyShowInstance(List<CinemaHall>cinemaHall,List<Movie>movies){
        if(bms == null){
            synchronized(BMS.class){
                if(bms == null){
                    bms = new BMS(cinemaHall,movies);
                }
            }
        }
        return bms;
    }
    public static void main(String []args){

    }
}

class Payment{

}
class PaymentService{
    
}
// booking
//  - Book ticket, it will reserve a particular seat
//  - unbook ticket, based on ticket id
// rating implementatio
// booking and unbooking implementation
