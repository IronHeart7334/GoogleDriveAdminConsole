package plugins.implementations.shareFiles;

import plugins.implementations.userListReader.ReadUserList;
import plugins.implementations.fileListReader.ReadFileList;
import drive.AbstractDriveCommand;
import fileUtils.FileList;
import fileUtils.UserList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import structs.GoogleSheetProperties;
import structs.UserToFileMapping;
import sysUtils.Logger;

/**
 * 
 * @author Matt
 */
public class ShareFiles extends AbstractDriveCommand<List<UserToFileMapping>>{
    private final GoogleSheetProperties userListProps;
    private final GoogleSheetProperties fileListProps;
    private final boolean isTest;
    
    public ShareFiles(GoogleSheetProperties source, GoogleSheetProperties fileList, boolean thisIsATest) {
        super();
        userListProps = source;
        fileListProps = fileList;
        isTest = thisIsATest;
    }

    @Override
    public List<UserToFileMapping> execute() throws IOException {
        StringBuilder msg = new StringBuilder();
        
        // first, extract the users from the form responses
        UserList newUsers = new ReadUserList(userListProps).execute();
        
        msg.append("Contents of user list:");
        newUsers.forEach((user)->msg.append("\n").append(user.toString()));
        
        
        // next, get the list of files users will get access to
        FileList files = new ReadFileList(fileListProps).execute();
        msg.append("\nFiles they will get:");
        files.forEach((file)->msg.append("\n").append(file.toString()));
        
        // construct the list of requests to make
        ArrayList<UserToFileMapping> whoGetsWhat = UserToFileMapping.constructUserFileList(newUsers, files);
        
        msg.append("\nCreating the following mappings:");
        whoGetsWhat.forEach((mapping)->msg.append("\n").append(mapping.toString()));
        Logger.log(msg.toString());
        
        // construct the command
        AbstractDriveCommand cmd = new GiveViewAccess(whoGetsWhat);
        
        Logger.log(cmd.toString());
        
        if(!isTest){
            try{
                cmd.execute();
            } catch (IOException ex) {
                Logger.logError(ex);
            }
        }
        return whoGetsWhat;
    }
}
