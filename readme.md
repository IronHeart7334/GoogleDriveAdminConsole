# Google Drive Administration Console
A small, extendable application for sharing Google Drive Files.

## Project Purpose ##
    Over the Summer of 2020, I worked as an independent contractor for the California Aerospace Museum.
I joined a team working on a STEM (Science, Technology, Engineering, and Math) camp, where campers
would use Minecraft as a tool to build a Mars colony, learning the science behind the various structures
they were told to build. Campers were given Google documents with instructions and information they would
need in their build process. However, manually distributing many files to many campers is not very scalable,
and making these documents public wasn't a preferred option. As such, I developed this administration console
to join tables of camper and file information, and distribute the files accordingly.

## Requirements ##
(*) This application uses Java to run. If you do not yet have Java installed, you'll need to download and install Java [here](https://www.java.com/en/).

## Installing the Application ##
1. First, go to the [release tab](https://github.com/IronHeart7334/AerospaceCampAdminConsole/releases) for the latest stable release, 
locate the most recent version, and download the source code under the 'assets' tab.
2. Once the application is done downloading, unzip it.
3. You will find the executable JAR file under build/libs. You can move this JAR file wherever you want. Feel free to delete the other files, as you only need this JAR file.
4. Double-click the JAR file to run it.
5. Any files the application creates are stored under [HOME]/Google Drive Administration Console, where HOME is your home folder.
For PC users, this is usually '''C:\Users\NAME\Google Drive Administration Console'''. The credentials will go in the "credentials" folder here.
6. The first time you launch the application, it will ask you for some Google credentials. Follow the on-screen instructions to finish setting up the application.
7. Once you've put the credentials in the appropriate folder and closed the popup, you'll be asked to sign in to Google.
8. After signing in, the application will launch. 

## Information on User and File Lists ##
* [general information](https://docs.google.com/document/d/1rEV5si3BlKcu4RbHDmYYkPtk12aHa35u9U_VfO1Bpwk/edit?usp=sharing)
* [example spreadsheet] (https://docs.google.com/spreadsheets/d/1mdyHStF7EvLQCkS1avJ_JaD9t7HvAXGEW8X0Zqr81cI/edit?usp=sharing)
* [property file for example user list] (https://drive.google.com/file/d/1CBpe0JTFOinSSxf9cuS0Rzeq0N2NxKBP/view?usp=sharing)
* [property file for example file list] (https://drive.google.com/file/d/17_WSCA6TOYb6GTL3oYdFlsJY6falgyZV/view?usp=sharing)

## Helpful Links for Developers ##
* [CSV Package](https://commons.apache.org/proper/commons-csv/apidocs/index.html)
* [The Google API console](https://console.developers.google.com/apis/credentials)
* [Getting started with Drive API Java](https://developers.google.com/drive/api/v3/quickstart/java)
* [Getting started with Sheets API Java](https://developers.google.com/sheets/api/quickstart/java)
* [Drive Javadoc](https://developers.google.com/resources/api-libraries/documentation/drive/v3/java/latest/overview-summary.html)
* [Sheets Javadoc](https://developers.google.com/resources/api-libraries/documentation/sheets/v4/java/latest/)
* Use this to download a file's contents: https://drive.google.com/uc?export=download&id=ID
* https://cloud.google.com/compute/docs/api-rate-limits
* https://console.developers.google.com/apis/api/drive.googleapis.com/quotas
To check if something is a folder, check if its MIME type is "application/vnd.google-apps.folder"

## TODO ##
* Different name for application
* Implement drive.commands.utils.DriveCommandResult
* Make PluginLoader scour the classpath for plugins
* Automate
* Make GoogleDriveURL class which extracts file ID from a file sharing URL (note that URLs are formatted differently for some file types)