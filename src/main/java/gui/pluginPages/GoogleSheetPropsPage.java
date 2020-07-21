package gui.pluginPages;

import gui.MainPane;
import gui.pages.AbstractFormPage;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import structs.GoogleSheetProperties;
import sysUtils.FileSystem;
import sysUtils.Logger;

/**
 *
 * @author Matt
 */
public class GoogleSheetPropsPage extends AbstractFormPage {
    private final JTextField propertyFileName;
    private final JTextField spreadsheetId;
    private final JTextField sheetName;
    
    public GoogleSheetPropsPage(MainPane inPane) {
        super(inPane);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        propertyFileName = new JTextField();
        propertyFileName.setToolTipText("enter a name for this property file");
        add(propertyFileName);
        
        spreadsheetId = new JTextField();
        spreadsheetId.setToolTipText("enter the ID of a Google Spreadsheet here");
        add(spreadsheetId);
        
        sheetName = new JTextField();
        sheetName.setToolTipText("enter the sheet name");
        add(sheetName);
        
        JButton run = new JButton("Create");
        run.addActionListener((e)->{
            submit();
        });
        add(run);
    }

    @Override
    public void doSubmit() throws Exception {
        GoogleSheetProperties gsp = new GoogleSheetProperties();
        gsp.setFileId(spreadsheetId.getText());
        gsp.setSheetName(sheetName.getText());
        
        String path = FileSystem.getInstance().saveProperties(propertyFileName.getText(), gsp);
        Logger.log(String.format("Saved properties to %s", path));
    }

}