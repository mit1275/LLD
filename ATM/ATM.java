package ATM;

import javax.smartcardio.Card;

interface IATMState{
    void insertCard(Card card);
    void ejectCard(Card card);
    void pressButton();
    void enterPin();
    void authenticateUser();
    void startTransaction();
}
class IdleState implements IATMState{
    private ATM atm;
    IdleState(ATM atm){
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
class HasCardState implements IATMState{
    private ATM atm;
    HasCardState(ATM atm){
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
    private IATMState IdleState;
    private IATMState HasCardState;
    private IATMState AuthenticateUserState;
    private IATMState TransactionState;
    private IATMState curState;
    ATM(){
        IdleState = new IdleState(this);
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
        curState.insertCard(Card card);
    }
    public void ejectCard(Card card){
        curState.ejectCard(Card card);
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