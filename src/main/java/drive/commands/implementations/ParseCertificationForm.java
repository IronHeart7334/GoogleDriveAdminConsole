package drive.commands.implementations;

import drive.commands.utils.AbstractDriveCommand;
import fileUtils.FileList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import start.ServiceAccess;
import structs.DetailedFileInfo;
import structs.UserListProperties;
import structs.FileListProperties;
import structs.DetailedUserInfo;
import structs.UserToFileMapping;

/**
 * 
 * @author Matt
 */
public class ParseCertificationForm extends AbstractDriveCommand<List<UserToFileMapping>>{
    private final UserListProperties certFormInfo;
    private final FileListProperties fileListInfo;
    private final String accessListId;
    private final boolean isTest;
    
    public ParseCertificationForm(ServiceAccess service, UserListProperties source, FileListProperties fileList, String accessListFileId, boolean thisIsATest) {
        super(service);
        certFormInfo = source;
        fileListInfo = fileList;
        accessListId = accessListFileId;
        isTest = thisIsATest;
    }

    @Override
    public List<UserToFileMapping> execute() throws IOException {
        // first, extract the campers from the form responses
        ArrayList<DetailedUserInfo> newCampers = new ReadUserList(getServiceAccess(), certFormInfo).execute();
        System.out.println("Contents of certification form:");
        newCampers.forEach(System.out::println);
        
        
        // next, get the list of files campers will get access to
        FileList files = new ReadFileList(getServiceAccess(), fileListInfo).execute();
        System.out.println("Files they will get:");
        files.forEach(System.out::println);
        
        // construct the list of requests to make
        ArrayList<UserToFileMapping> whoGetsWhat = UserToFileMapping.constructUserFileList(newCampers, files);
        
        // construct the command
        AbstractDriveCommand cmd = new GiveViewAccess(getServiceAccess(), whoGetsWhat);
        
        System.out.println(cmd);
        
        if(!isTest){
            try{
                cmd.execute();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // add people to the Minecraft user list
            String[] newMcUsers = newCampers.stream().map((userData)->{
                return userData.getMinecraftUsername();
            }).toArray((size)->new String[size]);

            new AddToAccessList(getServiceAccess(), accessListId, newMcUsers).execute();
        }
        return whoGetsWhat;
    }
}
