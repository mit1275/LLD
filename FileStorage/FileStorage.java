package FileStorage;
import java.time.Instant;
import java.util.*;
class Folder{
    private int id;
    private String name;
    private List<File>files;
    private int parent_id;
    Folder(int id,String name,List<File>files,int parent_id){
        this.id = id;
        this.name = name;
        this.files = files;
        this.parent_id = parent_id;
    }
}
interface IFolderService{
    List<File>addFile();
    List<File>removeFile();
    void displayFolderStructure();
    void deleteFolder();
}
class FolderService implements IFolderService{
    private Map<Integer,List<File>>files;
    FolderService(Map<Integer,List<File>>files){
        this.files = files;
    }
    public List<File>addFile(int parent_id,File f){
        if(files.get(parent_id)!=null){
            List<File>f2 = files.get(parent_id);
            f2.add(f);
            files.put(parent_id, f2);
            return f2;
        }else{
            List<File>f2 = new ArrayList<>();
            f2.add(f);
            files.put(parent_id, f2);
            return f2;
        }
    }
    public List<File>removeFile(int parent_id,File f){
        if(files.get(parent_id)!=null){
            List<File>f2 = files.get(parent_id);
            for(int i=0;i<f2.size();i++){
                if(f2.get(i).getId() == (f.getId())){
                    f2.get(i).updateFileStatus(false);
                }
            }
            return f2;
        }
        return null;
    }
    public void displayFolderStructure(){
        
    }
    public void deleteFolder(){

    }
}
class Content{
    private int id;
    private String data;
    private int sequenceNo;
    private long createdAt;
    Content(int id,String data,int sequenceNo){
        this.id = id;
        this.createdAt = Instant.EPOCH.toEpochMilli();
        this.data = data;
        this.sequenceNo = sequenceNo;
    }
    public Content getContent(){
        return this;
    }
}
class File{
    private int id;
    private int user_id;
    private String name;
    private List<Content>contents;
    private int parent_id;
    private boolean isFileExist;
    private List<Integer>user_idAccess;
    private Version version;
    File(int id,String name,List<Content>contents,int parent_id,Version version){
        this.id = id;
        this.name = name;
        this.contents = contents;
        this.parent_id = parent_id;
        isFileExist = true;
        user_idAccess = new ArrayList<>();
        user_idAccess.add(user_id);
        this.version = version;
    }
    public int getId(){
        return this.id;
    }
    public boolean updateFileStatus(boolean isFileExist){
        this.isFileExist = isFileExist;
        return this.isFileExist;
    }
    public boolean getFileStatus(){
        return this.isFileExist;
    }
}
class Version{
    private int id;
    private int file_id;
    private long createdAt;
    Version(int id,int file_id){
        this.id = id;
        this.file_id = file_id;
        this.createdAt = Instant.EPOCH.toEpochMilli();
    }
}
interface IFileUploaderService{
    File uploadFile(String data,String fileName);
}
interface IFileDownloaderService{
    File downloadFile(int file_id);
}
class FileUploadService implements IFileUploaderService{
    private Map<Integer,File>fileData;
    public File uploadFile(String data,String fileName){
        List<Content>cn = new ArrayList<>();
        for (int i = 0; i < data.length(); i += 5){
            int end = Math.min(data.length(), i + 5);
            String part = data.substring(i, end);
            Content content = new Content(i+1, part,i+1);
            cn.add(content);
        }
        File f = new File(1, fileName, cn, -1, new Version(1, 1)); // check if file is already uploaded with same parent
        fileData.putIfAbsent(f.getId(), f);
        return f;
    }
}
class FileDownloadService implements IFileDownloaderService{
    private Map<Integer,File>fileData;
    FileDownloadService(Map<Integer,File>fileData){
        this.fileData = fileData;
    }
    public File downloadFile(int file_id){
        return fileData.get(file_id);
    }
}
public class FileStorage {
    public static FileStorage fileStorage = null;
    private FileStorage(){};
    public static FileStorage getFileStorage(){
        if(fileStorage == null){
            synchronized(FileStorage.class){
                if(fileStorage == null){
                    fileStorage = new FileStorage();
                }
            }
        }
        return fileStorage;
    }
    public static void main(String []args){

    }
}