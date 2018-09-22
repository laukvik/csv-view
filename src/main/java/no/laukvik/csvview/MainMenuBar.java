package no.laukvik.csvview;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import no.laukvik.csvview.content.ViewMode;
import no.laukvik.csvview.utils.Builder;
import no.laukvik.csvview.utils.RecentFiles;

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;

import static no.laukvik.csvview.utils.Builder.isMac;

/**
 * JavaFX MenuBar for the CSV application.
 */
final class MainMenuBar extends MenuBar {

    /**
     * The Menu for View.
     */
    private final Menu viewMenu;
    /**
     * The Application this MenuBar belongs to.
     */
    private final Main main;
    /**
     * The ResourceBundle.
     */
    private final ResourceBundle bundle;
    /**
     * The Menu for recent files.
     */
    private Menu openRecentMenu;

    /**
     * Creates a new MenuBar for the JavaFX application.
     *
     * @param main the main app
     */
    public MainMenuBar(final Main main) {
        super();
        this.main = main;
        bundle = Builder.getBundle();
        setUseSystemMenuBar(isMac());
        viewMenu = buildViewMenu();
        //
        getMenus().add(buildFileMenu());
        getMenus().add(buildEditMenu());
        getMenus().add(buildQueryMenu());
        getMenus().add(buildInsertMenu());
        getMenus().addAll(viewMenu);
    }
    
    KeyCombination getCtrl(String key){
        if (isMac()){
            return KeyCombination.keyCombination("Meta+" + key);
        } else {
            return KeyCombination.keyCombination("ctrl+" + key);
        }
    }

    KeyCombination getAlt(String key){
        if (isMac()){
            return KeyCombination.keyCombination("Meta+" + key);
        } else {
            return KeyCombination.keyCombination("Alt+" + key);
        }
    }

    /**
     * Builds the file menu.
     *
     * @return the menu
     */
    private Menu buildFileMenu() {
        final Menu fileMenu = new Menu(bundle.getString("file"));
        MenuItem newItem = new MenuItem(bundle.getString("file.new"));
        newItem.setAccelerator(getCtrl("n"));
        newItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                main.handleNewFile();
            }
        });

        MenuItem openItem = new MenuItem(bundle.getString("file.open"));
        openItem.setAccelerator(getCtrl("o"));
        openItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                main.handleFileOpen();
            }
        });

        // ------ RecentFiles files ------
        openRecentMenu = new Menu(bundle.getString("file.recent"));

        MenuItem saveItem = new MenuItem(bundle.getString("file.save"));
        saveItem.setAccelerator(getCtrl("s"));
        MenuItem saveAsItem = new MenuItem(bundle.getString("file.saveas"));
        saveAsItem.setAccelerator(getCtrl("s+shift"));
        saveAsItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                main.handleSaveAsAction();
            }
        });

        MenuItem importItem = new MenuItem(bundle.getString("file.import"));
        importItem.setAccelerator(getCtrl("i"));
        importItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                main.handleOpenFileWithOptions();
            }
        });


        final Menu exportMenu = new Menu(bundle.getString("file.export"));

        MenuItem exportJsonItem = new MenuItem(bundle.getString("file.export.json"));
        MenuItem exportXmlItem = new MenuItem(bundle.getString("file.export.xml"));
        MenuItem exportHtmlItem = new MenuItem(bundle.getString("file.export.html"));
        MenuItem resourceBundleItem = new MenuItem(bundle.getString("file.export.resourcebundle"));
        exportJsonItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                main.handleExportJsonAction();
            }
        });
        exportXmlItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                main.handleExportXmlAction();
            }
        });
        exportHtmlItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                main.handleExportHtmlAction();
            }
        });
        exportMenu.getItems().addAll(exportJsonItem, exportXmlItem, exportHtmlItem, resourceBundleItem);

        MenuItem printItem = new MenuItem(bundle.getString("file.print"));
        printItem.setAccelerator(getCtrl("p"));
        printItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                main.handlePrintAction();
            }
        });
        fileMenu.getItems().addAll(newItem, openItem, openRecentMenu, saveItem, saveAsItem, new SeparatorMenuItem(),
                importItem, exportMenu, new SeparatorMenuItem(), printItem);

        List.of(saveItem, saveAsItem, importItem, exportMenu, printItem).forEach(menuItem -> menuItem.setDisable(true));

        return fileMenu;
    }

    /**
     * Builds the edit menu.
     *
     * @return the menu
     */
    private Menu buildEditMenu() {

        // ----- Edit ------
        final Menu edit = new Menu(bundle.getString("edit"));
        MenuItem cutItem = new MenuItem(bundle.getString("edit.cut"));
        cutItem.setAccelerator(getCtrl("x"));
        cutItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                main.handleCutAction();
            }
        });

        MenuItem copyItem = new MenuItem(bundle.getString("edit.copy"));
        copyItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                main.handleCopyAction();
            }
        });
        copyItem.setAccelerator(getCtrl("c"));
        MenuItem pasteItem = new MenuItem(bundle.getString("edit.paste"));
        pasteItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                main.handlePasteAction();
            }
        });
        pasteItem.setAccelerator(getCtrl("v"));
        MenuItem deleteItem = new MenuItem(bundle.getString("edit.delete"));
        deleteItem.setAccelerator(KeyCombination.keyCombination("delete"));
        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                main.handleDeleteAction();
            }
        });

        MenuItem moveUpItem = new MenuItem(bundle.getString("edit.moveup"));
        moveUpItem.setAccelerator(getCtrl("" + KeyCode.UP));
        moveUpItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                main.handleMoveUpAction();
            }
        });
        MenuItem moveDownItem = new MenuItem(bundle.getString("edit.movedown"));
        moveDownItem.setAccelerator(getCtrl("" + KeyCode.DOWN));
        moveDownItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                main.handleMoveDownAction();
            }
        });
        edit.getItems().addAll(cutItem, copyItem, pasteItem, deleteItem,
                new SeparatorMenuItem(), moveUpItem, moveDownItem);

        edit.getItems().forEach(menuItem -> menuItem.setDisable(true));
        return edit;
    }

    /**
     * Builds the query menu.
     *
     * @return the menu
     */
    private Menu buildQueryMenu() {
        // ----- QueryModel ------
        final Menu queryMenu = new Menu(bundle.getString("query"));  // Clear query
        MenuItem newQueryMenuItem = new MenuItem(bundle.getString("query.new"));
        newQueryMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                main.handleNewQuery();
            }
        });
        queryMenu.getItems().addAll(newQueryMenuItem);
        return queryMenu;
    }

    /**
     * Builds the insert menu.
     *
     * @return the menu
     */
    private Menu buildInsertMenu() {
        // ----- Insert ------
        final Menu insert = new Menu(bundle.getString("insert"));
        MenuItem newColumnItem = new MenuItem(bundle.getString("insert.column"));
        newColumnItem.setAccelerator(getCtrl("i"));
        newColumnItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                main.handleNewColumnAction();
            }
        });
        MenuItem newRowItem = new MenuItem(bundle.getString("insert.row"));
        newRowItem.setAccelerator(getCtrl("R"));
        newRowItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                main.handleNewRowAction();
            }
        });
        MenuItem headersRowItem = new MenuItem(bundle.getString("insert.headers"));
        headersRowItem.setAccelerator(getCtrl("H"));
        headersRowItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                main.handleNewHeaders();
            }
        });
        insert.getItems().addAll(newColumnItem, newRowItem, headersRowItem);

        insert.getItems().forEach(menuItem -> menuItem.setDisable(true));

        return insert;
    }

    /**
     * Builds the view menu.
     *
     * @return the menu
     */
    private Menu buildViewMenu() {
        Menu menu = new Menu(bundle.getString("view"));
        CheckMenuItem viewResultsMenuItem = new CheckMenuItem(bundle.getString("view.results"));
        viewResultsMenuItem.setAccelerator(getAlt("1"));
        viewResultsMenuItem.setSelected(true);
        viewResultsMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                setSelectedMode(ViewMode.Results);
                main.handleViewResultsAction();
            }
        });

        CheckMenuItem viewChartMenuItem = new CheckMenuItem(bundle.getString("view.piechart"));
        viewChartMenuItem.setAccelerator(getAlt("2"));
        viewChartMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                setSelectedMode(ViewMode.Chart);
                main.handleViewChartAction();

            }
        });

        CheckMenuItem previewMenuItem = new CheckMenuItem(bundle.getString("view.preview"));
        previewMenuItem.setAccelerator(getAlt("3"));
        previewMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                setSelectedMode(ViewMode.Preview);
                main.handleViewPreviewAction();

            }
        });

        CheckMenuItem wikipediaMenuItem = new CheckMenuItem(bundle.getString("view.wikipedia"));
        wikipediaMenuItem.setAccelerator(getAlt("4"));
        wikipediaMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                setSelectedMode(ViewMode.Wikipedia);
                main.handleViewWikipediaAction();

            }
        });

        CheckMenuItem googleMapsMenuItem = new CheckMenuItem(bundle.getString("view.googlemaps"));
        googleMapsMenuItem.setAccelerator(getAlt("5"));
        googleMapsMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                setSelectedMode(ViewMode.Maps);
                main.handleViewGoogleMapsAction();

            }
        });

        CheckMenuItem googleSearchMenuItem = new CheckMenuItem(bundle.getString("view.google"));
        googleSearchMenuItem.setAccelerator(getAlt("6"));
        googleSearchMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                setSelectedMode(ViewMode.Search);
                main.handleViewGoogleSearchAction();

            }
        });

        menu.getItems().addAll(viewResultsMenuItem, viewChartMenuItem, previewMenuItem,
                wikipediaMenuItem, googleMapsMenuItem, googleSearchMenuItem);

        return menu;
    }


    /**
     * Builds a menu with all recently loaded files.
     *
     * @param recentFiles the recentFiles object
     */
    public void buildRecentList(final RecentFiles recentFiles) {
        openRecentMenu.getItems().clear();
        for (File file : recentFiles.getList()) {
            MenuItem openRecentItem = new MenuItem(file.getAbsolutePath());
            openRecentItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(final ActionEvent t) {
                    main.loadFile(file);
                }
            });
            openRecentMenu.getItems().add(openRecentItem);
        }
        openRecentMenu.getItems().add(new SeparatorMenuItem());
        MenuItem openRecentItem = new MenuItem(bundle.getString("file.recent.clear"));
        openRecentItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                recentFiles.clear();
                buildRecentList(recentFiles);
            }
        });
        openRecentMenu.getItems().add(openRecentItem);
    }

    /**
     * Sets the selected view mode.
     *
     * @param viewMode the view mode
     */
    private void setSelectedMode(final ViewMode viewMode) {
        int x = 0;
        for (MenuItem item : viewMenu.getItems()) {
            CheckMenuItem i = (CheckMenuItem) item;
            i.setSelected(x == viewMode.ordinal());
            x++;
        }
    }

    public void setDefault() {
        setSelectedMode(ViewMode.Results);
    }
}
