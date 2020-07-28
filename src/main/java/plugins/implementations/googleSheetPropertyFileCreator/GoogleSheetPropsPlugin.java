package plugins.implementations.googleSheetPropertyFileCreator;

import gui.MainPane;
import gui.pages.AbstractFormPage;
import plugins.utils.AbstractPlugin;

/**
 *
 * @author Matt
 */
public class GoogleSheetPropsPlugin implements AbstractPlugin {

    @Override
    public AbstractFormPage getFormPage(MainPane pane) {
        return new GoogleSheetPropsPage(pane);
    }

    @Override
    public String getType() {
        return "create";
    }

    @Override
    public String getName() {
        return "create sheet properties";
    }

    @Override
    public String getDescription() {
        return "creates a new set of Google Sheet properties, which allow the application to locate Google Sheets in your drive";
    }

}