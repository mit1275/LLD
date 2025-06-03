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
    private Map<Integer,List<File>>fileData;
    FolderService(Map<Integer,List<File>>fileData){
        this.fileData = fileData;
    }
    public List<File>addFile(int parent_id,File f){
        List<File>f2;
        if(fileData.get(f.getId())!=null){
            f2 = fileData.get(f.getId());
            f2.add(f);
            fileData.put(f.getId(), f2);
        } else {
            f2 = new ArrayList<>();
            f2.add(f);
            fileData.putIfAbsent(f.getId(), f2);
        }
        return f2;
    }
    public List<File>removeFile(int parent_id,File f){
        
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
    private int versionId;
    File(int id,String name,List<Content>contents){
        this.id = id;
        this.name = name;
        this.contents = contents;
        this.parent_id = -1;
        this.versionId = 1;
        isFileExist = true;
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
    public int getParentId(){
        return this.parent_id;
    }
    public String getName(){
        return this.name;
    }
    public Integer getVersionId(){
        return this.versionId;
    }
}
interface IFileRepository{
    void updateVersion(File f,int version_id);
    void updateParent(File f,int parent_id);
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
interface IFileUploaderService{
    File uploadFile(String data,String fileName,int parent_id);
}
interface IFileDownloaderService{
    File downloadFile(int file_id,int version_id);
}
interface IFileVersioning{
    void assignVersion(File f,int version_id);
}
class FileVersionService implements IFileVersioning{
    private Map<Integer,List<File>>files;
    private IFileRepository fileRepository;
    FileVersionService(Map<Integer,List<File>>files,IFileRepository fileRepository){
        this.files = files;
        this.fileRepository = fileRepository;
    }
    public void assignVersion(File f,int last_version){
        fileRepository.updateVersion(f,last_version+1);
    }
}
interface IFileValidateService{
    File isDuplicateFile(String fileName,int parent_id);
}
class FileValidateService implements IFileValidateService{
    private List<File>files;
    public File isDuplicateFile(String fileName,int parent_id){
        for(int i=0;i<files.size();i++){
            int cur_file_par = files.get(i).getParentId();
            String file_name = files.get(i).getName();
            if(fileName == file_name && (cur_file_par)==parent_id){
                return files.get(i);
            }
        }
        return null;
    }
}
interface IFileBuilder{
    File createFile(int id, String name, String data);
}
class FileBuilder implements IFileBuilder{
    public File createFile(int id, String name, String data) {
        List<Content> chunks = chunkData(data);
        File file = new File(id, name, chunks);
        return file;
    }
    private List<Content> chunkData(String data) {
        List<Content> contents = new ArrayList<>();
        for (int i = 0; i < data.length(); i += 5) {
            int end = Math.min(data.length(), i + 5);
            contents.add(new Content(i + 1, data.substring(i, end), i + 1));
        }
        return contents;
    }
}
class FileUploadService implements IFileUploaderService{
    private Map<Integer,List<File>>fileData;
    private IFileValidateService fileValidateService;
    private IFileVersioning fileVersioning;
    private IFileRepository fileRepository;
    private IFolderService folderService;
    private IFileBuilder fileBuilder;
    FileUploadService(Map<Integer,List<File>>fileData,IFileValidateService fileValidateService,IFileVersioning fileVersioning,IFileRepository fileRepository,IFolderService folderService,IFileBuilder fileBuilder){
        this.fileData = fileData;
        this.fileValidateService = fileValidateService;
        this.fileVersioning = fileVersioning;
        this.fileRepository = fileRepository;
        this.folderService = folderService;
        this.fileBuilder = fileBuilder;
    }
    public File uploadFile(String data,String fileName,int parent_id){
        File f2 = fileValidateService.isDuplicateFile(fileName, parent_id);
        if(f2!=null){
            File f = fileBuilder.createFile(f2.getId(), fileName, data);
            fileVersioning.assignVersion(f,f2.getVersionId());
            fileRepository.updateParent(f, parent_id);
            folderService.addFile(parent_id, f);
            return f;
        }else{
            File f = fileBuilder.createFile(1, fileName, data);
            fileVersioning.assignVersion(f,0);
            fileRepository.updateParent(f, parent_id);
            folderService.addFile(parent_id, f);
            return f;
        }
    }
}
class FileDownloadService implements IFileDownloaderService{
    private Map<Integer,List<File>>fileData;
    FileDownloadService(Map<Integer,List<File>>fileData){
        this.fileData = fileData;
    }
    public File downloadFile(int file_id,int version_id){
        List<File>f = fileData.get(file_id);
        for(int i=0;i<f.size();i++){
            if(f.get(i).getVersionId() == version_id){
                return f.get(i);
            }
        }
        return null;
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