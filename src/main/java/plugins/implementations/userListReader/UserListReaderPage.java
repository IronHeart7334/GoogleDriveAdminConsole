package plugins.implementations.userListReader;

import plugins.implementations.AbstractReaderPage;
import fileUtils.UserList;
import gui.MainPane;
import structs.GoogleSheetProperties;

/**
 *
 * @author Matt
 */
public class UserListReaderPage extends AbstractReaderPage {

    public UserListReaderPage(MainPane inPane) {
        super(inPane, "User List Properties", "select the user sheet properties");
    }

    @Override
    public void parse(GoogleSheetProperties props) throws Exception {
        MainPane parent = getPaneParent();
        UserList users = new ReadUserList(props).execute();
        parent.addText(String.format("%s's sheet named '%s' contains the following users:", props.getFileId(), props.getSheetName()));
        parent.addText(users.toString());
    }
}
