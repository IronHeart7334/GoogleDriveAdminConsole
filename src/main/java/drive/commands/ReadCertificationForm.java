package drive.commands;

import com.google.api.services.drive.Drive;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import drive.DriveAccess;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import users.UserData;

/**
 *
 * @author Matt
 */
public class ReadCertificationForm extends AbstractDriveCommand<ArrayList<UserData>>{
    private final String spreadsheetFileId;
    
    public ReadCertificationForm(Drive d, String fileId) {
        super(d);
        spreadsheetFileId = fileId;
    }

    @Override
    public ArrayList<UserData> execute() throws IOException {
        ArrayList<UserData> users = new ArrayList<>();
        try {
            Sheets service = DriveAccess.getInstance().getSheets();
            ValueRange values = service.spreadsheets().values().get(spreadsheetFileId, "Form Responses 1").execute();
            List<List<Object>> data = values.getValues();
            for(List<Object> row : data){
                for(Object cell : row){
                    System.out.print(cell.toString() + "\t");
                }
                System.out.println();
            }
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
            System.exit(ex.hashCode());
        }
        return users;
    }

}
