package ChatApplication;
import java.util.*;
enum AccountStatus{
    BLOCKED,
    ACTIVE,
    NOT_REGISTERED
}
interface IReadReceipt{
    void markAsRead(int messageId, int readerId);
    boolean isMessageReadByUser(int messageId, int readerId);
}
interface IUserManagerStrategy{
    boolean isUserActive(User u);
}
interface ICacheManager extends IUserManagerStrategy{
    void addUser(User u);
    void removeUser(User u);
}
class CacheManager implements ICacheManager{
    private final List<User>users;
    CacheManager(){
        this.users = new ArrayList<>();
    }
    public void addUser(User u){
        users.add(u);
    }
    public void removeUser(User u){
        users.remove(u);
    }
    public boolean isUserActive(User u){
        for(int i=0;i<users.size();i++){
            if(users.get(i) == u){
                return true;
            }
        }
        return false;
    }
}
class Account{
    private String email,password;
    private String mobNumber;
    private AccountStatus accountStatus;
    private String createdAt;
}
class User{
    private int id;
    private String displayName;
    private Account account;
    private boolean isAdmin;
}
abstract class Message{
    private int id;
    private int user_id_sent_by;
    private Date createdAt;
    private boolean isMessageExist;
    private int parent_msg_id;
    Message(int id,int user_id_sent_by,int parent_msg_id){
        this.id = id;
        this.user_id_sent_by = user_id_sent_by;
        this.parent_msg_id = parent_msg_id;
    }
    public Integer getUserIdSentBy(){
        return this.user_id_sent_by;
    }
}
class GroupMessage extends Message {
    private final int group_id_sent_to;
    public GroupMessage(int id, int user_id_sent_by, int groupId,int parent_msg_id) {
        super(id, user_id_sent_by,parent_msg_id);
        this.group_id_sent_to = groupId;
    }

    public int getGroupIdSentTo() {
        return group_id_sent_to;
    }
}
class SingleUserMessage extends Message {
    private final int user_id_sent_to;

    public SingleUserMessage(int id, int user_id_sent_by, int user_id_sent_to,int parent_msg_id) {
        super(id, user_id_sent_by,parent_msg_id);
        this.user_id_sent_to = user_id_sent_to;
    }

    public int getUserIdSentTo() {
        return user_id_sent_to;
    }
}
interface IMessageService{
    Message sendMessage(Message msg);
}
interface IMessageObserver{
    void onMessageReceived(Message message);
}
class EmailNotifier implements IMessageObserver{
    public void onMessageReceived(Message message){

    }
}
class WebSocketNotifier implements IMessageObserver{
    public void onMessageReceived(Message message){
        
    }
}
interface INotificationManager{
    void subscribe(IMessageObserver iMessageObserver);
    void unsubscribe(IMessageObserver iMessageObserver);
    void notifyUser(Message message);
}
class NotificationManager implements INotificationManager{
    private final List<IMessageObserver> observers = new ArrayList<>();
    public void subscribe(IMessageObserver iMessageObserver){
        observers.add(iMessageObserver);
    }
    public void unsubscribe(IMessageObserver iMessageObserver){
        observers.remove(iMessageObserver);
    }
    public void notifyUser(Message message){
        for(int i=0;i<observers.size();i++){
            IMessageObserver iMessageObserver = observers.get(i);
            iMessageObserver.onMessageReceived(message);
        }
    }
}
interface IMessageRepository {
    Message saveMessage(Message message);
    List<Message> getMessagesSentByUser(int userId);
    List<Message> getMessagesSentToUser(int userId);
    List<Message> getMessagesByGroupId(int groupId);
}
class InMemoryMessageRepository implements IMessageRepository {
    private final List<Message> messages = new ArrayList<>();
    private final Map<Integer, List<GroupMessage>> groupMessages = new HashMap<>();
    public Message saveMessage(Message message) {
        messages.add(message);
        if (message instanceof GroupMessage gm) {
            groupMessages
                .computeIfAbsent(gm.getGroupIdSentTo(), k -> new ArrayList<>())
                .add(gm);
        }
        return message;
    }
    public List<Message> getMessagesSentByUser(int userId) {
        List<Message> result = new ArrayList<>();
        for (Message msg : messages) {
            if (msg.getUserIdSentBy() == userId) {
                result.add(msg);
            }
        }
        return result;
    }
    public List<Message> getMessagesSentToUser(int userId) {
        List<Message> result = new ArrayList<>();
        for (Message msg : messages) {
            if (msg instanceof SingleUserMessage sm) {
                if (sm.getUserIdSentTo() == userId) {
                    result.add(sm);
                }
            }
        }
        return result;
    }
    public List<Message> getMessagesByGroupId(int groupId) {
        return new ArrayList<>(groupMessages.getOrDefault(groupId, Collections.emptyList()));
    }
}
class MessageService implements IMessageService {
    private final IMessageRepository messageRepository;
    private final ISendMessageStrategy sendMessageStrategy;
    MessageService(IMessageRepository messageRepository,ISendMessageStrategy sendMessageStrategy) {
        this.messageRepository = messageRepository;
        this.sendMessageStrategy = sendMessageStrategy;
    }
    public Message sendMessage(Message message){
        Message message2 = messageRepository.saveMessage(message);
        sendMessageStrategy.sendMessage(message2);
        return message2;
    }
}
interface ISendMessageStrategy{
    void sendMessage(Message message);
}
class PushMessageStrategy implements ISendMessageStrategy{
    private final IMessageRepository messageRepository;
    private final INotificationManager notificationManager;
    PushMessageStrategy(IMessageRepository messageRepository,INotificationManager notificationManager){
        this.messageRepository = messageRepository;
        this.notificationManager = notificationManager;
    }
    public void sendMessage(Message message){
        notificationManager.notifyUser(message);
    }
}
class PullMessageStrategy implements ISendMessageStrategy{
    private final IMessageRepository messageRepository;
    PullMessageStrategy(IMessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }
    public void sendMessage(Message message){

    }
}
public class ChatApplication {
    public static void main(String []args){

    }
}