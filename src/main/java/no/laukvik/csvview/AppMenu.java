package no.laukvik.csvview;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import no.laukvik.csv.Row;
import no.laukvik.csv.columns.Column;
import no.laukvik.csvview.column.ColumnListener;
import no.laukvik.csvview.content.ViewMode;
import no.laukvik.csvview.table.ResultsTableListener;
import no.laukvik.csvview.utils.Builder;
import no.laukvik.csvview.utils.RecentFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static no.laukvik.csvview.utils.Builder.isMac;

/**
 * JavaFX MenuBar for the CSV application.
 */
final class AppMenu extends MenuBar implements ResultsTableListener, ColumnListener {

    /**
     * The Menu for View.
     */
    private final Menu viewMenu;
    /**
     * The Application this MenuBar belongs to.
     */
    private final App app;
    /**
     * The ResourceBundle.
     */
    private final ResourceBundle bundle;
    /**
     * The Menu for recent files.
     */
    private Menu openRecentMenu;
    private MenuItem importItem;
    private Menu exportMenu;
    private Menu editMenu;

    /**
     * Creates a new MenuBar for the JavaFX application.
     *
     * @param app the app app
     */
    public AppMenu(final App app) {
        super();
        this.app = app;
        bundle = Builder.getBundle();
        setUseSystemMenuBar(isMac());
        viewMenu = buildViewMenu();
        getMenus().add(buildFileMenu());
        getMenus().add(buildEditRowsMenu());
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
    
    private String getString(String key){
        return bundle.getString("appmenu." + key);
    }

    /**
     * Builds the file menu.
     *
     * @return the menu
     */
    private Menu buildFileMenu() {
        final Menu fileMenu = new Menu(getString("file"));
        MenuItem newItem = new MenuItem(getString("file.new"));
        newItem.setAccelerator(getCtrl("n"));
        newItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                app.handleNewFile();
            }
        });

        MenuItem openItem = new MenuItem(getString("file.open"));
        openItem.setAccelerator(getCtrl("o"));
        openItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                app.handleFileOpen();
            }
        });

        MenuItem openWith = new MenuItem(getString("file.open.wizard"));
        openWith.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                app.handleOpenFileWithOptions();
            }
        });

        // ------ RecentFiles files ------
        openRecentMenu = new Menu(getString("file.recent"));

        MenuItem saveItem = new MenuItem(getString("file.save"));
        saveItem.setAccelerator(getCtrl("s"));
        MenuItem saveAsItem = new MenuItem(getString("file.saveas"));
        saveAsItem.setAccelerator(getCtrl("s+shift"));
        saveAsItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                app.handleSaveAsAction();
            }
        });

        importItem = new MenuItem(getString("file.import"));
        importItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                app.handleImport();
            }
        });


        exportMenu = new Menu(getString("file.export"));

        MenuItem exportCsvItem = new MenuItem(getString("file.export.csv"));
        MenuItem exportJsonItem = new MenuItem(getString("file.export.json"));
        MenuItem exportXmlItem = new MenuItem(getString("file.export.xml"));
        MenuItem exportHtmlItem = new MenuItem(getString("file.export.html"));
        exportJsonItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                app.handleExportJsonAction();
            }
        });
        exportXmlItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                app.handleExportXmlAction();
            }
        });
        exportHtmlItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                app.handleExportHtmlAction();
            }
        });
        exportMenu.getItems().addAll(exportCsvItem, exportJsonItem, exportXmlItem, exportHtmlItem);

        MenuItem printItem = new MenuItem(getString("file.print"));
        printItem.setAccelerator(getCtrl("p"));
        printItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                app.handlePrintAction();
            }
        });
        fileMenu.getItems().addAll(newItem, openItem, openRecentMenu, openWith, saveItem, saveAsItem, new SeparatorMenuItem(),
                importItem, exportMenu);
        return fileMenu;
    }

    /**
     * Builds the editMenu menu.
     *
     * @return the menu
     */
    private Menu buildEditRowsMenu() {

        // ----- Edit ------
        editMenu = new Menu(getString("edit"));
        MenuItem cutItem = new MenuItem(getString("edit.cut.rows"));
        cutItem.setAccelerator(getCtrl("x"));
        cutItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                app.handleCutRowsAction();
            }
        });

        MenuItem copyItem = new MenuItem(getString("edit.copy.rows"));
        copyItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                app.handleCopyRowsAction();
            }
        });
        copyItem.setAccelerator(getCtrl("c"));
        MenuItem pasteItem = new MenuItem(getString("edit.paste.rows"));
        pasteItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                app.handlePasteRowsAction();
            }
        });
        pasteItem.setAccelerator(getCtrl("v"));
        MenuItem deleteItem = new MenuItem(getString("edit.delete.rows"));
        deleteItem.setAccelerator(KeyCombination.keyCombination("delete"));
        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                app.handleDeleteRowsAction();
            }
        });

        MenuItem deleteColumnItem = new MenuItem(getString("edit.delete.column"));
        deleteColumnItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                app.handleDeleteColumnAction();
            }
        });

        editMenu.getItems().addAll(
                cutItem, copyItem, pasteItem, deleteItem,
                new SeparatorMenuItem(),
                deleteColumnItem
        );
        return editMenu;
    }

    /**
     * Builds the query menu.
     *
     * @return the menu
     */
    private Menu buildQueryMenu() {
        // ----- QueryModel ------
        final Menu queryMenu = new Menu(getString("query"));  // Clear query
        MenuItem newQueryMenuItem = new MenuItem(getString("query.new"));
        newQueryMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                app.handleNewQuery();
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
        final Menu insert = new Menu(getString("insert"));
        MenuItem newColumnItem = new MenuItem(getString("insert.column"));
        newColumnItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                app.handleNewColumnAction();
            }
        });
        MenuItem newRowItem = new MenuItem(getString("insert.row"));
        newRowItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                app.handleNewRowAction();
            }
        });
        insert.getItems().addAll(newColumnItem, newRowItem);
        return insert;
    }

    /**
     * Builds the view menu.
     *
     * @return the menu
     */
    private Menu buildViewMenu() {
        Menu menu = new Menu(getString("view"));
        CheckMenuItem viewResultsMenuItem = new CheckMenuItem(getString("view.results"));
        viewResultsMenuItem.setAccelerator(getAlt("1"));
        viewResultsMenuItem.setSelected(true);
        viewResultsMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                setSelectedMode(ViewMode.Results);
                app.handleViewResultsAction();
            }
        });

        CheckMenuItem viewChartMenuItem = new CheckMenuItem(getString("view.piechart"));
        viewChartMenuItem.setAccelerator(getAlt("2"));
        viewChartMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                setSelectedMode(ViewMode.Chart);
                app.handleViewChartAction();

            }
        });

        CheckMenuItem previewMenuItem = new CheckMenuItem(getString("view.preview"));
        previewMenuItem.setAccelerator(getAlt("3"));
        previewMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                setSelectedMode(ViewMode.Preview);
                app.handleViewPreviewAction();

            }
        });

        CheckMenuItem wikipediaMenuItem = new CheckMenuItem(getString("view.wikipedia"));
        wikipediaMenuItem.setAccelerator(getAlt("4"));
        wikipediaMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                setSelectedMode(ViewMode.Wikipedia);
                app.handleViewWikipediaAction();

            }
        });

        CheckMenuItem googleMapsMenuItem = new CheckMenuItem(getString("view.googlemaps"));
        googleMapsMenuItem.setAccelerator(getAlt("5"));
        googleMapsMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                setSelectedMode(ViewMode.Maps);
                app.handleViewGoogleMapsAction();

            }
        });

        CheckMenuItem googleSearchMenuItem = new CheckMenuItem(getString("view.google"));
        googleSearchMenuItem.setAccelerator(getAlt("6"));
        googleSearchMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                setSelectedMode(ViewMode.Search);
                app.handleViewGoogleSearchAction();

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
                    app.loadFile(file);
                }
            });
            openRecentMenu.getItems().add(openRecentItem);
        }
        openRecentMenu.getItems().add(new SeparatorMenuItem());
        MenuItem openRecentItem = new MenuItem(getString("file.recent.clear"));
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

    public void rowsSelected(List<Row> rows){
        boolean disable = rows.isEmpty();
        editMenu.getItems().get(0).setDisable(disable);
        editMenu.getItems().get(1).setDisable(disable);
        editMenu.getItems().get(2).setDisable(disable);
        editMenu.getItems().get(3).setDisable(disable);
    }

    public void columnSelected(Column column){
        editMenu.getItems().get(5).setDisable(column == null);
    }

    @Override
    public void columnChanged(Column column) {
    }

    @Override
    public void columnVisibilityChanged(Column column, boolean isVisible) {
    }

}
