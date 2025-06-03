package ShoppingCart;

import java.util.ArrayList;
import java.util.*;

// class Rating{

// }
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
        return priceComponent.getPrice() - taxAmount;
    }
}

class DigitalProduct implements IProductStrategy{
    private IDeliveryStrategy iDeliveryStrategy;
    DigitalProduct(IDeliveryStrategy iDeliveryStrategy){
        this.iDeliveryStrategy = iDeliveryStrategy;
    }
    public double calculatePrice(Product product){
        PriceComponent priceComponent = new BasePrice(product.getPrice());
        priceComponent = new DiscountDecorator(priceComponent, 50);
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
    GiftProduct(IDeliveryStrategy iDeliveryStrategy){
        this.iDeliveryStrategy = iDeliveryStrategy;
    }
    public double calculatePrice(Product product){
        PriceComponent priceComponent = new BasePrice(product.getPrice());
        priceComponent = new DiscountDecorator(priceComponent, 50);
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
    private Integer qty;
    private double price;
    Product(int id,String name,Integer qty){
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.rating = new ArrayList<>();
    }
    void setDescription(Description description) {
        this.description = description;
    }

    void setPrice(double price) {
        this.price = price;
    }
}
// class Item{
//     private int id;
//     private List<Product>products;
// }
// class Cart{
//     private int id;
// }
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
