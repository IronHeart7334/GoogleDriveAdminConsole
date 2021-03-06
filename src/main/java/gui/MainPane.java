package gui;

import gui.components.HidableDecorator;
import gui.components.MessagePopup;
import gui.components.TextScroller;
import gui.pages.AbstractFormPage;
import gui.pages.OutputPage;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import plugins.utils.PluginLoader;
import drive.GoogleDriveService;
import plugins.utils.AbstractPlugin;
import sysUtils.FileSystem;

/**
 * 
 * @author Matt
 */
public class MainPane extends JPanel{
    private final JMenuBar menu;
    private final JPanel bodyPane;
    
    private final OutputPage outputPage;
    private final HidableDecorator outputWrapper;
    
    private final JPanel cmdPane;
    private final HidableDecorator cmdWrapper;
    
    private final TextScroller helpText;
    private final HidableDecorator helpWrapper;
    
    public MainPane(GoogleDriveService service){
        super();
        
        setLayout(new BorderLayout());
        
        // construct the menu bar
        menu = new JMenuBar();
        loadServices();
        
        JButton saveLogButton = new JButton("Save logs");
        saveLogButton.addActionListener((e)->{
            try {
                String path = FileSystem.getInstance().saveLog();
                new MessagePopup("Successfully saved logs to " + path);
            } catch (IOException ex) {
                new MessagePopup("Failed to save logs");
                ex.printStackTrace();
            }
        });
        menu.add(saveLogButton);
        
        JButton logoutButton = new JButton("Delete Saved Login");
        logoutButton.addActionListener((e)->{
            service.logOut();
            new MessagePopup("Logged out successfully", ()->{
                // I don't particularly like this. Wish I could just do "Application.close()"
                Container root = getParent();
                while(root != null && !(root instanceof MainWindow)){
                    root = root.getParent();
                }
                if(root == null){
                    throw new UnsupportedOperationException();
                } else {
                    ((MainWindow)root).dispose();
                }
            });
        });
        menu.add(logoutButton);
        
        add(menu, BorderLayout.PAGE_START);
        
        // construct the page content area
        bodyPane = new JPanel(); // do I need this?
        bodyPane.setLayout(new GridLayout(1, 1));
        
        // left side
        outputPage = new OutputPage(this);
        outputWrapper = new HidableDecorator("Output", outputPage);
        
        // middle
        cmdPane = new JPanel();
        cmdPane.setLayout(new GridLayout(1, 1));
        cmdWrapper = new HidableDecorator("Command", cmdPane);
        
        // right
        helpText = new TextScroller();
        helpWrapper = new HidableDecorator("Help", helpText);
        
        // combine left and middle into one split pane
        JSplitPane split1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, outputWrapper, cmdWrapper);
        split1.setResizeWeight(0.0); // devotes extra space to the right pane (cmdWrapper)
        split1.resetToPreferredSizes();
        //combine split1 and right into one split pane so we have 3 side-by-side panels
        JSplitPane split2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, split1, helpWrapper);
        split2.setResizeWeight(1.0); // devotes extra space to the left pane (split1)
        split2.resetToPreferredSizes();
        // add split2, while now contains left, middle, and right
        /*
        split2
        \_split1
          \_left
          \_middle
        \_right
        */
        bodyPane.add(split2, BorderLayout.CENTER);
        
        // make split panes resize themselves when the user toggles the wrappers
        outputWrapper.addHideListener((isHidden)->{
            if(isHidden){
                split1.resetToPreferredSizes();
            }
        });
        
        cmdWrapper.addHideListener((isHidden)->{
            if(isHidden){
                split1.setResizeWeight(1.0); // give extra space to output
                split2.setResizeWeight(0.5); // divide evenly between split1 and help
            } else {
                split1.setResizeWeight(0.0); // give extra space to cmd
                split2.setResizeWeight(1.0); // give extra space to split1
            }
            split1.resetToPreferredSizes();
            split2.resetToPreferredSizes();
        });
        helpWrapper.addHideListener((isHidden)->{
            if(isHidden){
                split2.resetToPreferredSizes();
            }
        });
        
        add(bodyPane, BorderLayout.CENTER);
    }
    
    /**
     * Takes each AbstractPlugin from the PluginLoader,
     * and adds menus for each of them.
     */
    private void loadServices(){
        HashMap<String, JMenu> menus = new HashMap<>();
        String type;
        for(AbstractPlugin plugin : PluginLoader.getInstance().getAllPlugins()){
            type = plugin.getType().toLowerCase();
            if(!menus.containsKey(type)){
                menus.put(type, new JMenu(type));
            }
            addMenuItem(menus.get(type), plugin.getName(), ()->openTab(plugin));
        }
        menus.values().forEach((subMenu)->menu.add(subMenu));
    }
    
    private void openTab(AbstractPlugin plugin){
        AbstractFormPage page = plugin.getFormPage(this);
        cmdPane.removeAll();
        cmdPane.add(page);
        cmdWrapper.setHidden(false);
        helpText.setText(plugin.getHelp());
        revalidate();
        repaint();
    }
    
    private JMenuItem addMenuItem(JMenu addTo, String text, Runnable r){
        JMenuItem newItem = new JMenuItem(text);
        newItem.addActionListener((e)->r.run());
        addTo.add(newItem);
        return newItem;
    }
    
    public final void switchToOutputTab(){
        outputWrapper.setHidden(false);
        cmdWrapper.setHidden(true);
        repaint();
    }
    
    public final void setTabSwitchingEnabled(boolean allowSwitching){
        cmdWrapper.setEnabled(allowSwitching);
    }
    
    public final void addText(String appendMe){
        outputPage.addText(appendMe);
    }
}
