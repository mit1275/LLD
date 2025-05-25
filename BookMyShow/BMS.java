package BookMyShow;
import java.util.*;
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
    HINDU,
    TELGU
}
enum MovieType{
    ACTION,
    THRILLER,
    COMEDY
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
class RatingService{

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
    Seat(int id,SeatType seatType,ISeatStrategy seatStrategy){
        this.id = id;
        this.seatType = seatType;
        this.seatStatus = SeatStatus.AVAILABLE;
        this.seatStrategy = seatStrategy;
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
class Booking{
    private int id;
}
class BookingService{

}
class Ticket{

}
class TicketService{

}
class Payment{

}
// booking
//  - Book ticket, it will reserve a particular seat
//  - unbook ticket, based on ticket id
// rating implementatio
// booking and unbooking implementation
