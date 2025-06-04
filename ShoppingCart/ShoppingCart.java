package ShoppingCart;

import java.util.ArrayList;
import java.util.*;

class Rating{
    private int id;
}
class Description{
    private int id;
    private String content;
    Description(int id,String content){
        this.id = id;
        this.content = content;
    }
    public Integer getDescriptionId(){
        return this.id;
    }
    public String getContent(){
        return this.content;
    }
}
class ProductBuilder{
    private int id;
    private String name;
    private Integer qty;
    private Description description;
    private double price;
    public ProductBuilder id(int id){
        this.id = id;
        return this;
    }
    public ProductBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder qty(Integer qty) {
        this.qty = qty;
        return this;
    }
    public ProductBuilder description(int descId, String content) {
        this.description = new Description(descId, content);
        return this;
    }

    public ProductBuilder price(double price) {
        this.price = price;
        return this;
    }

    public Product createProduct() {
        Product product = new Product(id, name, qty);
        product.setDescription(description);
        product.setPrice(price);
        return product;
    }

}
interface IProductStrategy{
    double calculatePrice(Product product);
    boolean isDeliverable();
    String getDeliveryMethod();
}
interface PriceComponent{
    double getPrice();
}
interface IDeliveryStrategy{
    String getDeliveryMethod();
    void deliverProduct();
}
interface Offer{
    boolean isApplicable(Product product);
    double applyDiscount(Product product);
}
class OfferEngine{
    public double getBestOfferPrice(Product product, List<Offer> availableOffers) {
        double min = product.getPrice();
        for (Offer o : availableOffers) {
            if (o.isApplicable(product)) {
                min = Math.min(min, o.applyDiscount(product));
            }
        }
        return min;
    }
}
class FlatDiscountOffer implements Offer{
    private double discount;

    public FlatDiscountOffer(double discount) {
        this.discount = discount;
    }

    public boolean isApplicable(Product product) {
        return true;
    }

    public double applyDiscount(Product product) {
        return product.getPrice() - discount;
    }
}
class MailDelivery implements IDeliveryStrategy{
    public String getDeliveryMethod(){
        return "MAIL";
    }
    public void deliverProduct(){

    }
}
class ShipDelivery implements IDeliveryStrategy{
    public String getDeliveryMethod(){
        return "SHIPPING";
    }
    public void deliverProduct(){
        
    }
}
class BasePrice implements PriceComponent{
    private double price;
    public BasePrice(double price) {
        this.price = price;
    }
    public double getPrice() {
        return price;
    }
}
class DiscountDecorator implements PriceComponent{
    private PriceComponent priceComponent;
    private double discountAmount;
    DiscountDecorator(PriceComponent priceComponent,double discountAmount){
        this.priceComponent = priceComponent;
        this.discountAmount = discountAmount;
    }
    public double getPrice(){
        return priceComponent.getPrice() - discountAmount;
    }
}
class TaxDecorator implements PriceComponent{
    private PriceComponent priceComponent;
    private double taxAmount;
    TaxDecorator(PriceComponent priceComponent,double taxAmount){
        this.priceComponent = priceComponent;
        this.taxAmount = taxAmount;
    }
    public double getPrice(){
        return priceComponent.getPrice() + taxAmount;
    }
}

class DigitalProduct implements IProductStrategy{
    private IDeliveryStrategy iDeliveryStrategy;
    private double discountAmount;
    DigitalProduct(IDeliveryStrategy iDeliveryStrategy,double discountAmount){
        this.iDeliveryStrategy = iDeliveryStrategy;
        this.discountAmount = discountAmount;
    }
    public double calculatePrice(Product product){
        PriceComponent priceComponent = new BasePrice(product.getPrice());
        priceComponent = new DiscountDecorator(priceComponent, discountAmount);
        return priceComponent.getPrice();
    }
    public boolean isDeliverable(){
        return false;
    }
    public String getDeliveryMethod(){
        return iDeliveryStrategy.getDeliveryMethod();
    }
}
class GiftProduct implements IProductStrategy{
    private IDeliveryStrategy iDeliveryStrategy;
    private double discountAmount;
    GiftProduct(IDeliveryStrategy iDeliveryStrategy,double discountAmount){
        this.iDeliveryStrategy = iDeliveryStrategy;
        this.discountAmount = discountAmount;
    }
    public double calculatePrice(Product product){
        PriceComponent priceComponent = new BasePrice(product.getPrice());
        priceComponent = new DiscountDecorator(priceComponent, discountAmount);
        return priceComponent.getPrice();
    }
    public boolean isDeliverable(){
        return true;
    }
    public String getDeliveryMethod(){
        return iDeliveryStrategy.getDeliveryMethod();
    }
}
class Product{
    private int id;
    private String name;
    private Description description;
    private List<Rating>rating;
    private double price;
    Product(int id,String name){
        this.id = id;
        this.name = name;
        this.rating = new ArrayList<>();
    }
    void setDescription(Description description) {
        this.description = description;
    }
    void setPrice(double price) {
        this.price = price;
    }
    double getPrice(){
        return this.price;
    }
}
class CartItem{
    private int id;
    private Product product;
    private int qty;
    CartItem(int id,Product product,int qty){
        this.id = id;
        this.product = product;
        this.qty = qty;
    }
}
interface ICartService{
    void addToCart(CartItem item);
    void removeFromCart(CartItem item);
    void emptyCart(Cart cart);
}
class CartService implements ICartService{
    public void addToCart(CartItem item) {

    }
    public void removeFromCart(CartItem item){

    }
    public void emptyCart(Cart cart){

    }
}
interface ISearch{
    
}
class Search implements ISearch{

}
interface ISellerService{
    void addProduct();
    void updateProduct();
}
class SellerService implements ISellerService{
    public void addProduct(){

    }
    public void updateProduct(){

    }
}
class Cart{
    private int id;
    private int user_id;
    private boolean isEmpty = true;
    Cart(int id,int user_id){
        this.id = id;
        this.user_id = user_id;
    }
    public Integer getCartId(){
        return this.id;
    }
    public Integer getUserId(){
        return this.user_id;
    }
    public boolean isCartEmpty(){
        return this.isEmpty == true ? true : false;
    }
    public void updateCartStatus(boolean isEmpty){
        this.isEmpty = isEmpty;
    }
}
public class ShoppingCart {
    private static ShoppingCart shoppingCart = null;
    ShoppingCart(){};
    public static ShoppingCart getShoppingCartInstance(){
        if(shoppingCart == null){
            synchronized(ShoppingCart.class){
                if(shoppingCart == null){
                    shoppingCart = new ShoppingCart();
                }
            }
        }
        return shoppingCart;
    }
    public static void main(String []args){

    }
}

// DigitalProduct and GiftProduct
// Gift Product will have discounts directly
// Give discounts based on offers -> use decorator pattern
