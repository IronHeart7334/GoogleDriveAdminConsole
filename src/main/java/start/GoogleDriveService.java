package start;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

/**
 * The GoogleDriveService is a Singleton
 * class used to interface with Google Drive and Sheets.
 * 
 * Future versions may get rid of the Google Sheets
 * interface in favor of using CSV files to maximize
 * versatility.
 * 
 * @author Matt Crow
 */
public class GoogleDriveService {
    private final NetHttpTransport httpTransport;
    private final JsonFactory jsonFactory;
    private final String tokenDirPath;
    private final String credentialFilePath;
    private final List<String> scopes;
    
    //maybe subclass these
    private final Drive driveService;
    private final Sheets sheetService;
    
    private static GoogleDriveService instance;
    
    private GoogleDriveService() throws GeneralSecurityException, IOException{
        if(instance != null){
            throw new ExceptionInInitializerError("DriveAccess is a singleton");
        }
        jsonFactory = JacksonFactory.getDefaultInstance();
        tokenDirPath = "tokens";
        credentialFilePath = "/credentials.json";
        
        //If modifying these scopes, delete your previously saved tokens/ folder.
        scopes = new ArrayList<>();
        scopes.add(DriveScopes.DRIVE);
        scopes.add(DriveScopes.DRIVE_FILE); // not sure which I need
        scopes.add(SheetsScopes.SPREADSHEETS_READONLY);
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        
        Credential credentials = createCredentials();
        driveService = new Drive.Builder(httpTransport, jsonFactory, credentials).setApplicationName(Main.APPLICATION_NAME).build();
        sheetService = new Sheets.Builder(httpTransport, jsonFactory, credentials).setApplicationName(Main.APPLICATION_NAME).build();
    }
    
    private Credential createCredentials() throws FileNotFoundException, IOException{
        //Load client secret
        InputStream in = GoogleDriveService.class.getResourceAsStream(credentialFilePath);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + credentialFilePath);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(tokenDirPath)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
    
    /**
     * Use this method to gain access to the GoogleDriveService.
     * If the drive service has not yet been initialized, calls
     * the constructor.
     * 
     * @return the GoogleDriveService instance.
     * @throws GeneralSecurityException if Google fails to authenticate the user
     * @throws IOException if the user hasn't saved their credentials
     */
    public static final GoogleDriveService getInstance() throws GeneralSecurityException, IOException{
        if(instance == null){
            instance = new GoogleDriveService();
        }
        return instance;
    }
    
    public final Drive getDrive(){
        return driveService;
    }
    
    public final Sheets getSheets(){
        return sheetService;
    }
}