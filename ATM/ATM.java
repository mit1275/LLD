package ATM;
import java.util.*;
class Card{
    private Integer uniqueNo;
    private Integer binNumber;
    private Date issuedAt;
    private Date expiry;
    private String cardIssuer;
    private String cardType;
}
interface IState{
    void insertCard(Card card);
    void pressButton();
    void enterPin();
    void authenticateUser();
    void transactionManagement();
    void dispenser();
}
class InsertCardState implements IState{
    private ATM atm;
    InsertCardState(ATM atm){
        this.atm = atm;
    }
    public void insertCard(Card card){
        System.out.println("Card Inserted");
        atm.setCurrentState(atm.getPressButtonState());
    }
    public void pressButton(){
        System.out.println("Insert card first");
    }
    public void enterPin(){
        System.out.println("Insert card first");
    }
    public void authenticateUser(){
        System.out.println("Insert card first");
    }
    public void transactionManagement(){
        System.out.println("Insert card first");
    }
    public void dispenser(){
        System.out.println("Insert card first");
    }
}
class PressButtonState implements IState{
    private ATM atm;
    PressButtonState(ATM atm){
        this.atm = atm;
    }
    public void insertCard(Card card){
        System.out.println("Card Already Inserted");
        return ;
    }
    public void pressButton(){
        System.out.println("Button clicked");
        atm.setCurrentState(atm.getEnterPinState());
    }
    public void enterPin(){
        System.out.println("Select options");
    }
    public void authenticateUser(){
        System.out.println("Select options");
    }
    public void transactionManagement(){
        System.out.println("Insert card first");
    }
    public void dispenser(){
        System.out.println("Insert card first");
    }
}
class EnterPinState implements IState{
    private ATM atm;
    EnterPinState(ATM atm){
        this.atm = atm;
    }
    public void insertCard(Card card){
        System.out.println("Card Already Inserted");
        return ;
    }
    public void pressButton(){
        System.out.println("Button clicked");
    }
    public void enterPin(){
        System.out.println("Pin entered");
        atm.setCurrentState(atm.getAuthenticatedState());
    }
    public void authenticateUser(){
        System.out.println("Please enter pin");
    }
    public void transactionManagement(){
        System.out.println("Insert card first");
    }
    public void dispenser(){
        System.out.println("Insert card first");
    }
}
class AuthenticatedState implements IState{
    private ATM atm;
    AuthenticatedState(ATM atm){
        this.atm = atm;
    }
    public void insertCard(Card card){
        System.out.println("Card Already Inserted");
        return ;
    }
    public void pressButton(){
        System.out.println("Button clicked");
    }
    public void enterPin(){
        System.out.println("Pin entered");
        
    }
    public void authenticateUser(){
        System.out.println("User Authenticated");
        atm.setCurrentState(atm.getTransactionManagementState());
    }
    public void transactionManagement(){
        System.out.println("Authenticate User");
    }
    public void dispenser(){
        System.out.println("Authenticate User");
    }
}
class TransactionManagementState implements IState{
    private ATM atm;
    TransactionManagementState(ATM atm){
        this.atm = atm;
    }
    public void insertCard(Card card){
        System.out.println("Card Already Inserted");
        return ;
    }
    public void pressButton(){
        System.out.println("Button clicked");
    }
    public void enterPin(){
        System.out.println("Pin entered");
    }
    public void authenticateUser(){
        System.out.println("User Authenticated");
        
    }
    public void transactionManagement(){
        System.out.println("Transaction complete");
        atm.setCurrentState(atm.getDispenserState());
    }
    public void dispenser(){
        System.out.println("Enter amount details");
    }
}
class DispenserState implements IState{
    private ATM atm;
    DispenserState(ATM atm){
        this.atm = atm;
    }
    public void insertCard(Card card){
        System.out.println("Card Already Inserted");
        return ;
    }
    public void pressButton(){
        System.out.println("Button clicked");
    }
    public void enterPin(){
        System.out.println("Pin entered");
        
    }
    public void authenticateUser(){
        System.out.println("User Authenticated");
        atm.setCurrentState(atm.getAuthenticatedState());
    }
    public void transactionManagement(){
        System.out.println("Insert card first");
    }
    public void dispenser(){
        System.out.println("Insert card first");
    }
}
public class ATM {
    private IState currentState;
    private IState insertCardState;
    private IState pressButtonState;
    private IState enterPinState;
    private IState authenticatedState;
    private IState transactionManagementState;
    private IState dispenserState;
    ATM(){
        insertCardState = new InsertCardState(this);
        pressButtonState = new PressButtonState(this);
        enterPinState = new EnterPinState(this);
        authenticatedState = new AuthenticatedState(this);
        transactionManagementState = new TransactionManagementState(this);
        dispenserState = new DispenserState(this);
        currentState = insertCardState;
    }
    public void setCurrentState(IState state) {
        this.currentState = state;
    }
    public void insertCard(Card card){
        currentState.insertCard(card);
    }
    public void pressButton(){
        currentState.pressButton();
    }
    public void enterPin(){
        currentState.enterPin();
    }
    public void authenticateUser(){
        currentState.authenticateUser();
    }
    public void transactionManagement(){
        currentState.transactionManagement();
    }
    public void dispenser(){
        currentState.dispenser();
    }
    public IState getInsertCardState() {
        return insertCardState;
    }

    public IState getPressButtonState() {
        return pressButtonState;
    }

    public IState getEnterPinState() {
        return enterPinState;
    }

    public IState getAuthenticatedState() {
        return authenticatedState;
    }
    public IState getTransactionManagementState() {
        return transactionManagementState;
    }
    public IState getDispenserState() {
        return dispenserState;
    }
    public static void main(String[]args){
        
    }
}
// authenticate user, check balance, withdraw balance,
