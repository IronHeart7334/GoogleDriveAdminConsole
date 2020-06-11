package drive.commands.camp;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import drive.commands.AbstractDriveCommand;
import drive.commands.accessList.AddToAccessList;
import drive.commands.basic.Copy;
import drive.commands.basic.CreateFolder;
import drive.commands.basic.GiveAccess;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import structs.AccessType;
import structs.CamperFile;
import structs.CertificationFormInfo;
import structs.FileListInfo;
import structs.UserData;
import structs.UserToFileMapping;

/**
 * make this add multiple people to multiple files using threads
 * @author Matt
 */
public class ParseCertificationForm extends AbstractDriveCommand<File>{
    private final CertificationFormInfo certFormInfo;
    private final FileListInfo fileListInfo;
    private final String campRootId;
    private final String accessListId;
    
    public ParseCertificationForm(Drive d, CertificationFormInfo source, FileListInfo fileList, String campFolderRootId, String accessListFileId) {
        super(d);
        certFormInfo = source;
        fileListInfo = fileList;
        campRootId = campFolderRootId;
        accessListId = accessListFileId;
    }

    @Override
    public File execute() throws IOException {
        // first, create the camp folder. TODO: make this check if a folder for the week already exist
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String timeStr = LocalDateTime.now().format(formatter);
        
        File createdFolder = new CreateFolder(getDrive(), String.format("%s (test)", timeStr), campRootId).execute();
        
        // second, extract the campers from the form responses
        ArrayList<UserData> newCampers = new ReadCertificationForm(getDrive(), certFormInfo).execute();
        //newCampers.forEach(System.out::println);
        
        
        // next, get the list of files campers will get access to
        ArrayList<CamperFile> files = new ReadFileList(getDrive(), fileListInfo).execute();
        //files.forEach(System.out::println);
        
        // construct the list of requests to make
        ArrayList<UserToFileMapping> whoGetsWhat = UserToFileMapping.constructUserFileList(newCampers, files);
        List<AbstractDriveCommand> commands = whoGetsWhat.stream().map((UserToFileMapping mapping)->{
            AbstractDriveCommand ret = null;
            switch(mapping.getFile().getAccessType()){
                case VIEW:
                    ret = new GiveAccess(getDrive(), mapping.getFile().getFileId(), mapping.getUser().getEmail(), AccessType.VIEW);
                    break;
                case EDIT:
                    ret = new Copy(getDrive(), mapping.getFile().getFileId(), createdFolder.getId(), mapping.getUser().getName(), mapping.getUser().getEmail());
                    break;
                default:
                    throw new RuntimeException("Unsupported access type: " + mapping.getFile().getAccessType());
            }
            return ret;
        }).collect(Collectors.toList());
        
        commands.stream().forEach(System.out::println);
        
        commands.parallelStream().forEach((cmd)->{
            try{
                cmd.execute();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
        String[] newMcUsers = newCampers.stream().map((userData)->{
            return userData.getMinecraftUsername();
        }).toArray((size)->new String[size]);
        
        new AddToAccessList(getDrive(), accessListId, newMcUsers).execute();
        return createdFolder;
    }
}
