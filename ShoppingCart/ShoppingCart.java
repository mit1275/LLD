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
interface PriceCalculator{
    double calculateFinalPrice(Cart cart);
}
interface TaxAndExtraDiscounts{
    double calculateFinalPrice();
}
interface CostCalculator{
    double getFinalPrice(Cart cart);
}
class CostCalculatorService implements CostCalculator{
    private PriceCalculator priceCalculator;
    private TaxAndExtraDiscounts taxAndExtraDiscounts;
    CostCalculatorService(PriceCalculator priceCalculator,TaxAndExtraDiscounts taxAndExtraDiscounts){
        this.priceCalculator = priceCalculator;
        this.taxAndExtraDiscounts = taxAndExtraDiscounts;
    }
    public double getFinalPrice(Cart cart){
        double price1 = priceCalculator.calculateFinalPrice(cart);
        price1 = taxAndExtraDiscounts.calculateFinalPrice();
        return price1;
    }
}
class CalculatePriceWithOffers implements PriceCalculator{
    private OfferEngine offerEngine;
    private List<Offer>offers;
    CalculatePriceWithOffers(OfferEngine offerEngine,List<Offer>offers){
        this.offerEngine = offerEngine;
        this.offers = offers;
    }
    public double calculateFinalPrice(Cart cart){
        double totCost = 0;
        List<CartItem>cartItems = cart.getCartItems();
        for(int i=0;i<cartItems.size();i++){
            Product product = cartItems.get(i).getProduct();
            totCost+=offerEngine.getBestOfferPrice(product, offers);
        }
        return totCost;
    }
}
class TaxAndExtra implements TaxAndExtraDiscounts{
    private PriceComponent priceComponent;
    TaxAndExtra(PriceComponent priceComponent){
        this.priceComponent = priceComponent;
    }
    public double calculateFinalPrice(){
        return priceComponent.getPrice();
    }
}
class ProductBuilder{
    private int id;
    private String name;
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
    public ProductBuilder description(int descId, String content) {
        this.description = new Description(descId, content);
        return this;
    }
    public ProductBuilder price(double price) {
        this.price = price;
        return this;
    }
    public Product createProduct() {
        Product product = new Product(id, name);
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
interface ICartRepository {
    Cart getCartByUserId(int userId);
    void saveCart(int userId, Cart cart);
}
class CartSystemRepository implements ICartRepository{
    private Map<Integer,Cart>user_cart_mapping;
    private List<Product>products;
    CartSystemRepository(Map<Integer,Cart>user_cart_mapping,List<Product>products){
        this.user_cart_mapping = user_cart_mapping;
        this.products = products;
    }
    public Cart getCartByUserId(int userId){
        Cart cart = user_cart_mapping.get(userId);
        return cart;
    }
    public void saveCart(int userId, Cart cart){
        user_cart_mapping.put(userId, cart);
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
    public Product getProduct(){
        return this.product;
    }
    public Integer getQty(){
        return this.qty;
    }
}
interface ICartService{
    void addToCart(int user_id,CartItem item);
    void removeFromCart(int userId,CartItem item);
    void emptyCart(Cart cart);
}
interface ICheckout{
    void proceedToCheckout(Cart cart);
}
class CheckoutService implements ICheckout{
    private CostCalculatorService costCalculatorService;
    CheckoutService(CostCalculatorService costCalculatorService){
        this.costCalculatorService = costCalculatorService;
    }
    public void proceedToCheckout(Cart cart){
        costCalculatorService.getFinalPrice(cart);
    }
}
class CartService implements ICartService{
    private ICartRepository cartRepository;
    CartService(ICartRepository cartRepository){
        this.cartRepository = cartRepository;
    }
    public void addToCart(int userId,CartItem item) {
        Cart cart = cartRepository.getCartByUserId(userId);
        cart.addToCart(item);
        cartRepository.saveCart(userId, cart);
    }
    public void removeFromCart(int userId,CartItem item){
        Cart cart = cartRepository.getCartByUserId(userId);
        cart.removeFormCart(item);
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
interface ISellerRepository{
    void addProduct();
    void updateProduct();
}
class SellerRepository implements ISellerRepository{
    private Map<Integer,Cart>user_cart_mapping;
    private List<Product>products;
    SellerRepository(Map<Integer,Cart>user_cart_mapping,List<Product>products){
        this.user_cart_mapping = user_cart_mapping;
        this.products = products;
    }
    public Cart getCartByUserId(int userId){
        Cart cart = user_cart_mapping.get(userId);
        return cart;
    }
    public void saveCart(int userId, Cart cart){
        user_cart_mapping.put(userId, cart);
    }
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
    private List<CartItem>listOfCartItems;
    Cart(int id,int user_id){
        this.id = id;
        this.user_id = user_id;
        this.listOfCartItems = new ArrayList<>();
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
    public List<CartItem>getCartItems(){
        return this.listOfCartItems;
    }
    public List<CartItem>addToCart(CartItem cartItem){
        listOfCartItems.add(cartItem);
        return listOfCartItems;
    }
    public List<CartItem>removeFormCart(CartItem cartItem){
        listOfCartItems.remove(cartItem);
        return listOfCartItems;
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