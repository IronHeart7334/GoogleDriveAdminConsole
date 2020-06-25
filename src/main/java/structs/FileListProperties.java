package structs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * FileListProperties are used to direct the application
 * to Google Sheets containing information on Google Drive
 * files which should be provided to campers. Sheets located
 * by these properties must conform to the requirement shown
 * in UserList.
 * 
 * @see fileUtils.UserList
 * @author Matt Crow
 */
public class FileListProperties extends Properties{
    private static final String SHEET_ID_KEY = "spreadsheetId";
    private static final String SHEET_NAME_KEY = "sheetName";
    
    public FileListProperties(String spreadsheetFileId, String filesSheetName){
        super();
        setProperty(SHEET_ID_KEY, spreadsheetFileId);
        setProperty(SHEET_NAME_KEY, filesSheetName);
    }
    
    public FileListProperties(){
        this("spreadsheetIdHere", "fileSheetNameHere");
    }
    
    public String getFileId(){
        return getProperty(SHEET_ID_KEY);
    }
    
    public String getSheetName(){
        return getProperty(SHEET_NAME_KEY);
    }
    
    public void save(File f) throws FileNotFoundException, IOException{
        store(new FileOutputStream(f), "This file contains information about which Google Sheet to query for information on a file list");
    }
    
    @Override
    public String toString(){
        StringBuilder bob = new StringBuilder();
        bob.append("FILE LIST INFO:\n");
        forEach((k, v)->bob.append(String.format("* %s : %s \n", k, v)));
        return bob.toString();
    }
}