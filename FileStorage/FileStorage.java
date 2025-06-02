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
    List<File>addFile(int parent_id,File f);
    List<File>removeFile(int parent_id,File f);
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
    private int versionId;
    File(int id,String name,List<Content>contents){
        this.id = id;
        this.name = name;
        this.contents = contents;
        this.parent_id = -1;
        this.versionId = 1;
        isFileExist = true;
        user_idAccess = new ArrayList<>();
        user_idAccess.add(user_id);
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
    public void updateVersion(int version_id){
        this.versionId = version_id;
    }
    public void updateParent(int parent_id){
        this.parent_id = parent_id;
    }
}
interface IFileRepository{
    void updateVersion(File f,int version_id);
    void updateParent(File f,int version_id);
}
class FileRepository implements IFileRepository{
    public void updateVersion(File f,int version_id){
        f.updateVersion(version_id);
    }
    public void updateParent(File f,int parent_id){
        f.updateParent(parent_id);
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
interface IVersion{
    Version createVersion();
    Version getVersion();
}
interface IFileUploaderService{
    File uploadFile(String data,String fileName);
}
interface IFileDownloaderService{
    File downloadFile(int file_id);
}
class IFileVersioning{
    void assignVersion(File f);
    File getFile(File f,int version_id);
}
class FileVersionService implements IFileVersioning{
    private Map<Integer,List<File>>files;
    FileVersionService(Map<Integer,List<File>>files){
        this.files = files;
    }
    public void assignVersion(File f){
        int file_id=f.getId();
        if(files.get(file_id)!=null){
            List<File>f2 = files.get(file_id);
            f2.add(f);
        }
    }
    public File getFile(File f,int version_id){

    }
}
class IFileValidateService{
    boolean isDuplicateFile(String fileName);
}
class FileValidateService implements IFileValidateService{
    private List<File>files;
    public boolean isDuplicateFile(String fileName,int parent_id){

    }
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
        File f = new File(1, fileName, cn);
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