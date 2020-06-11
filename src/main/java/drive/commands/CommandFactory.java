package drive.commands;

import drive.commands.camp.ReadFileList;
import drive.commands.camp.ReadCertificationForm;
import drive.commands.accessList.SetAccessListContent;
import drive.commands.accessList.GetAccessList;
import drive.commands.accessList.CreateAccessList;
import drive.commands.accessList.AddToAccessList;
import drive.commands.basic.GiveAccess;
import drive.commands.basic.Copy;
import structs.AccessType;
import drive.commands.camp.ParseCertificationForm;
import services.ServiceAccess;
import structs.CertificationFormInfo;
import structs.FileListInfo;

/**
 *
 * @author Matt
 */
public class CommandFactory {
    private final ServiceAccess services;
    
    public CommandFactory(ServiceAccess service){
        services = service;
    }
    
    public final CreateAccessList createAccessListCmd(String folderId){
        return new CreateAccessList(services, folderId);
    }
    
    public final GetAccessList getAccessListCmd(String accessListId){
        return new GetAccessList(services, accessListId);
    }
    
    public final SetAccessListContent setAccessListCmd(String accessListId, String[] userNames){
        return new SetAccessListContent(services, accessListId, userNames);
    }
    
    public final AddToAccessList addToAccessListCmd(String accessListId, String[] newUsers){
        return new AddToAccessList(services, accessListId, newUsers);
    }
    
    
    
    public final GiveAccess giveViewAccess(String fileId, String email){
        return new GiveAccess(services, fileId, email, AccessType.VIEW);
    }
    public final Copy makeCopyFor(String fileId, String toDir, String email, String name){
        return new Copy(services, fileId, toDir, name, email);
    }
    
    public final ReadCertificationForm readCertForm(CertificationFormInfo info){
        return new ReadCertificationForm(services, info);
    }
    public final ReadFileList readFileList(FileListInfo info){
        return new ReadFileList(services, info);
    }
    
    public final ParseCertificationForm parseCertificationForm(CertificationFormInfo formInfo, FileListInfo fileInfo, String campRootId, String accessListId){
        return new ParseCertificationForm(services, formInfo, fileInfo, campRootId, accessListId);
    }
}
