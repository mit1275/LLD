package ATM;

enum CardType{
    DEBIT_CARD,
    CREDIT_CARD
}
class Card{
    private String cardNo;
    private String name;
    private String cvv;
    private Date issuedAt;
    private Date issuedTill;
    private CardType cardType;
}
interface IATMState{
    void insertCard(Card card);
    void ejectCard(Card card);
    void pressButton();
    void enterPin();
    void authenticateUser();
    void startTransaction();
}
interface ICardReader{
    void readCard(Card card);
}
class CardReaderFetchIssuerBankService implements ICardReader{
    public void readCard(Card card){

    }
}
class CardReaderFetchBinNumberService implements ICardReader{
    public void readCard(Card card){

    }
}
class CardReaderFetchCardTypeService implements ICardReader{
    public void readCard(Card card){

    }
}
class IdleState implements IATMState{
    private ATM atm;
    private ICardReader cardReader;
    IdleState(ATM atm,ICardReader cardReader){
        this.atm = atm;
        this.cardReader = cardReader;
    }
    public void insertCard(Card card){
        System.out.println("Insert card"+card);
        atm.setState(atm.getHasCardState());
        cardReader.readCard(card);
    }
    public void ejectCard(Card card){
        System.out.println("Please insert card first");
    }
    public void pressButton(){
        System.out.println("Please insert card first");
    }
    public void enterPin(){
        System.out.println("Please insert card first");
    }
    public void authenticateUser(){
        System.out.println("Please insert card first");
    }
    public void startTransaction(){
        System.out.println("Please insert card first");
    }
}
class HasCardState implements IATMState{
    private ATM atm;
    HasCardState(ATM atm){
        this.atm = atm;
    }
    public void insertCard(Card card){
        System.out.println("Card is already inserted");
        return ;
    }
    public void ejectCard(Card card){
        System.out.println("Card is ejected");
        atm.setState(atm.getIdleState());
    }
    public void pressButton(){
        System.out.println("Button is pressed");
        // atm.setState(null);
    }
    public void enterPin(){

    }
    public void authenticateUser(){

    }
    public void startTransaction(){

    }
}
class AuthenticateUserState implements IATMState{
    private ATM atm;
    AuthenticateUserState(ATM atm){
        this.atm = atm;
    }
    public void insertCard(Card card){

    }
    public void ejectCard(Card card){

    }
    public void pressButton(){

    }
    public void enterPin(){

    }
    public void authenticateUser(){

    }
    public void startTransaction(){

    }
}
class TransactionState implements IATMState{
    private ATM atm;
    TransactionState(ATM atm){
        this.atm = atm;
    }
    public void insertCard(Card card){

    }
    public void ejectCard(Card card){

    }
    public void pressButton(){

    }
    public void enterPin(){

    }
    public void authenticateUser(){

    }
    public void startTransaction(){

    }
}
interface ITransactionOptions{
    void withdrawCash();
    void checkBalance();
    void transferCash();
}
interface IAuthentication{
    void authenticateUser(Card card);
}
class ATM{
    private ICardReader cardReader;
    private IATMState IdleState;
    private IATMState HasCardState;
    private IATMState AuthenticateUserState;
    private IATMState TransactionState;
    private IATMState curState;
    ATM(){
        IdleState = new IdleState(this,cardReader);
        HasCardState = new HasCardState(this);
        AuthenticateUserState = new AuthenticateUserState(this);
        TransactionState = new TransactionState(this);
        curState = IdleState;
    }
    public void setState(IATMState state) {
        curState = state;
    }
    public IATMState getIdleState(){
        return IdleState;
    }
    public IATMState getHasCardState(){
        return HasCardState;
    }
    public IATMState getAuthenticateUserState(){
        return AuthenticateUserState;
    }
    public IATMState getTransactionState(){
        return TransactionState;
    }
    public IATMState getCurState(){
        return curState;
    }
    public void insertCard(Card card){
        curState.insertCard(card);
    }
    public void ejectCard(Card card){
        curState.ejectCard(card);
    }
    public void pressButton(){
        curState.pressButton();
    }
    public void enterPin(){
        curState.enterPin();
    }
    public void authenticateUser(){
        curState.authenticateUser();
    }
    public void startTransaction(){
        curState.startTransaction();
    }
    public static void main(String []args){

    }
}