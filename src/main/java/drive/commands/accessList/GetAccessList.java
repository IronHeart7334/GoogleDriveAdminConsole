package drive.commands.accessList;

import drive.commands.AbstractDriveCommand;
import fileUtils.FileReadWriteUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import services.ServiceAccess;

/**
 *
 * @author Matt
 */
public class GetAccessList extends AbstractDriveCommand<String[]>{
    private final String fileId;
    
    public GetAccessList(ServiceAccess service, String fileId){
        super(service);
        this.fileId = fileId;
    }
    
    @Override
    public String[] execute() throws IOException{        
        InputStream content = getDrive().files().get(fileId).executeMediaAsInputStream();
        String textContents = FileReadWriteUtil.readStream(content);
        String[] array = Arrays.stream(textContents.split(System.lineSeparator())).filter((String userName)->!"".equals(userName.trim())).toArray((int size)->new String[size]);
        return array;
    }

}
