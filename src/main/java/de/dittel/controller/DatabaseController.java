package de.dittel.controller;

import de.dittel.util.ReferenceHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Controller-Klasse für die Datenbank
 */
public class DatabaseController {

    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String DB_NAME = "stageDB";
    private static final String DB_URL_PREFIX = "jdbc:derby:" + DB_NAME;
    private static final String DB_URL = DB_URL_PREFIX + ";create=false";
    private static final String DB_URL_CREATE = DB_URL_PREFIX + ";create=true";

    private static final String TABLENAME = "STAGE";
    private static final String CREATE_TABLE_STATEMENT = "CREATE TABLE " + TABLENAME + " (" +
            "name VARCHAR(20) PRIMARY KEY, stageX DOUBLE NOT NULL, stageY DOUBLE NOT NULL, stageWidth DOUBLE NOT NULL, " +
            "stageHeight DOUBLE NOT NULL, populationWidth DOUBLE NOT NULL, " +
            "populationHeight DOUBLE NOT NULL NOT NULL, slider DOUBLE NOT NULL) ";
    private static final String INSERT_STATEMENT = "INSERT INTO " + TABLENAME + " (name, stageX, stageY, stageWidth, " +
            "stageHeight, populationWidth, populationHeight, slider) values (?, ?, ?, ?, ?, ?, ?, ?) ";
    private static final String UPDATE_STATEMENT = "UPDATE " + TABLENAME + " SET stageX = ?, stageY = ?, " +
            "stageWidth = ?, stageHeight = ?, populationWidth = ?, populationHeight = ?, slider = ? WHERE name = ? ";
    private static final String SELECT_STATEMENT = "SELECT stageX, stageY, stageWidth, stageHeight, populationWidth, " +
            "populationHeight, slider FROM " + TABLENAME + " WHERE name = ?";
    private static final String SELECT_NAMES_STATEMENT = "SELECT name FROM " + TABLENAME;
    private static final String DELETE_STATEMENT = "DELETE FROM " + TABLENAME + " WHERE name = ?";
    private Connection connection = null;
    private final ReferenceHandler referenceHandler;

    /**
     * Konstruktor
     *
     * @param referenceHandler verwaltet die verwendeten Referenzen
     */
    public DatabaseController(ReferenceHandler referenceHandler) {
        this.referenceHandler = referenceHandler;
    }

    /**
     * Initialisiert den Controller und de-/aktviert die MenuItems
     */
    public void init() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            referenceHandler.getMainController().getSaveSettingsMenuItem().setDisable(true);
            referenceHandler.getMainController().getLoadSettingsMenuItem().setDisable(true);
            referenceHandler.getMainController().getDeleteSettingsMenuItem().setDisable(true);
        }

        try (Connection conn = DriverManager.getConnection(DB_URL_CREATE);
             ResultSet resultSet = conn.getMetaData().getTables(null, null, TABLENAME, null)) {
            if (!resultSet.next()) {
                createTable(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            referenceHandler.getMainController().getSaveSettingsMenuItem().setDisable(true);
            referenceHandler.getMainController().getLoadSettingsMenuItem().setDisable(true);
            referenceHandler.getMainController().getDeleteSettingsMenuItem().setDisable(true);
        }

        referenceHandler.getMainController().getSaveSettingsMenuItem().setOnAction(e -> saveStageConfig());
        referenceHandler.getMainController().getLoadSettingsMenuItem().setOnAction(e -> loadStageConfig());
        referenceHandler.getMainController().getDeleteSettingsMenuItem().setOnAction(e -> deleteStageConfig());
    }

    /**
     * Erzeugt eine neue Tabelle in der Datenbank
     *
     * @param connection zur Datenbank
     * @throws SQLException, falls bei Ausführung des Statements ein Fehler auftritt
     */
    private void createTable(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(CREATE_TABLE_STATEMENT);
        } catch (SQLException exc) {
            exc.printStackTrace();
            throw exc;
        }
    }

    /**
     * Getter für die connection
     */
    private Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed() && connection.isValid(0)) {
                return connection;
            }
            if (connection != null) {
                connection.close();
                return getConnection();
            }
            connection = DriverManager.getConnection(DB_URL);
            return connection;
        } catch (SQLException exc) {
            return null;
        }
    }

    /**
     * Schaltet die Datenbank ab und schließt die Verbindung
     */
    public void shutdown() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (DRIVER.equals("org.apache.derby.jdbc.EmbeddedDriver")) {
            try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException ignored) {}
        }
    }

    /**
     * Speichert die aktuellen Einstellungen des Fensters in der Datenbank
     */
    private void saveStageConfig() {
        TextInputDialog textInputDialog = new TextInputDialog("Name...");
        textInputDialog.setTitle("Einstellung speichern");
        textInputDialog.setHeaderText("Name auswählen");
        Optional<String> stringOptional = textInputDialog.showAndWait();

        if (stringOptional.isEmpty()) {
            return;
        }

        String name = stringOptional.get();

        Connection connection = getConnection();
        if (connection == null || name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Sorry, Datenbankfehler!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if (!configExists(name)) {
            createNewTableEntry(connection, name);
        } else {
            updateTableEntry(connection, name);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Einstellungen gespeichert!");
        alert.showAndWait();
    }

    /**
     * Überprüft ob der Name einer Einstellung bereits vorhanden ist
     *
     * @param name der überprüft werden soll
     * @return true, fall name bereits existiert; sonst false
     */
    private boolean configExists(String name) {
        ArrayList<String> allNames = selectAllNames();
        return allNames.contains(name);
    }

    /**
     * Erzeugt einen neuen Eintrag in der unter TABLENAME angelegten Tabelle
     *
     * @param connection zur Datenbank
     * @param name des neuen Eintrags
     */
    private void createNewTableEntry(Connection connection, String name) {
        try (PreparedStatement insertStatement = connection.prepareStatement(INSERT_STATEMENT)) {
            Stage stage = referenceHandler.getMainStage();
            connection.setAutoCommit(false);

            insertStatement.setString(1, name);
            insertStatement.setDouble(2, stage.getX());
            insertStatement.setDouble(3, stage.getY());
            insertStatement.setDouble(4, stage.getWidth());
            insertStatement.setDouble(5, stage.getHeight());
            insertStatement.setDouble(6, referenceHandler.getPopulationPanel().getPopulationWidth());
            insertStatement.setDouble(7, referenceHandler.getPopulationPanel().getPopulationHeight());
            insertStatement.setDouble(8, referenceHandler.getMainController().getSimulationSpeedSlider().getValue());

            insertStatement.execute();
            connection.commit();
        } catch (SQLException exc) {
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Sorry, Datenbankfehler beim Speichern!", ButtonType.OK);
                alert.showAndWait();
                connection.rollback();
                exc.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updatet einen bereits vorhandenen Datenbankeintrag
     *
     * @param connection zur Datenbank
     * @param name des Eintrags
     */
    private void updateTableEntry(Connection connection, String name) {
        try (PreparedStatement updateStmt = connection.prepareStatement(UPDATE_STATEMENT)) {
            Stage stage = referenceHandler.getMainStage();
            connection.setAutoCommit(false);

            updateStmt.setDouble(1, stage.getX());
            updateStmt.setDouble(2, stage.getY());
            updateStmt.setDouble(3, stage.getWidth());
            updateStmt.setDouble(4, stage.getHeight());
            updateStmt.setDouble(5, referenceHandler.getPopulationPanel().getPopulationWidth());
            updateStmt.setDouble(6, referenceHandler.getPopulationPanel().getPopulationHeight());
            updateStmt.setDouble(7, referenceHandler.getMainController().getSimulationSpeedSlider().getValue());
            updateStmt.setString(8, name);

            updateStmt.execute();
            connection.commit();
        } catch (SQLException exc) {
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Sorry, Datenbankfehler beim Speichern!", ButtonType.OK);
                alert.showAndWait();
                connection.rollback();
                exc.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Lädt eine Fenster-Einstellung aus der Datenbank und wendet sie an
     */
    private void loadStageConfig() {
        ArrayList<String> allNames = selectAllNames();
        if (allNames.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Sorry, keine Einstellungen vorhanden!");
            alert.showAndWait();
            return;
        }

        Optional<String> name = generateChoiceBox(allNames, "Einstellung laden");
        if (name.isEmpty()) {
            return;
        }

        Connection connection = getConnection();
        if (connection == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Sorry, Datenbankfehler!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        try (PreparedStatement selectStmt = connection.prepareStatement(SELECT_STATEMENT)) {
            connection.setAutoCommit(false);
            selectStmt.setString(1, name.get());
            ResultSet resultSet = selectStmt.executeQuery();

            while (resultSet.next()) {
                double stageX = resultSet.getDouble("stageX");
                double stageY = resultSet.getDouble("stageY");
                double stageWidth = resultSet.getDouble("stageWidth");
                double stageHeight = resultSet.getDouble("stageHeight");
                double populationWidth = resultSet.getDouble("populationWidth");
                double populationHeight = resultSet.getDouble("populationHeight");
                double slider = resultSet.getDouble("slider");

                referenceHandler.getMainStage().setX(stageX);
                referenceHandler.getMainStage().setY(stageY);
                referenceHandler.getMainStage().setWidth(stageWidth);
                referenceHandler.getMainStage().setHeight(stageHeight);
                referenceHandler.getPopulationPanel().setPopulationSize(populationWidth, populationHeight);
                referenceHandler.getMainController().getSimulationSpeedSlider().setValue(slider);
            }
            synchronized (referenceHandler) {
                referenceHandler.notifyAll();
            }
        } catch (Exception exc) {
            try {
                exc.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Sorry, Datenbankfehler beim Lesen!", ButtonType.OK);
                alert.showAndWait();
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Generiert eine ChoiceBox mit allen Einstellungen aus der Datebank und liefert den Input des Benutzers
     *
     * @param allNames alle verfügbaren Einstellungen in der Datebank
     * @param title der ChoiceBox
     * @return Auswahl des Benutzers (kann auch leer sein!)
     */
    private Optional<String> generateChoiceBox(ArrayList<String> allNames, String title) {
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>();
        choiceDialog.setTitle(title);
        choiceDialog.setHeaderText("Einstellung wählen: ");
        choiceDialog.setSelectedItem(allNames.get(0));
        allNames.forEach(config -> choiceDialog.getItems().add(config));
        return choiceDialog.showAndWait();
    }

    /**
     * Löscht einen Eintrag aus der Datenbank
     */
    private void deleteStageConfig() {
        ArrayList<String> allNames = selectAllNames();
        if (allNames.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Sorry, keine Einstellungen vorhanden!");
            alert.showAndWait();
            return;
        }

        Optional<String> name = generateChoiceBox(allNames, "Einstellung löschen");
        if (name.isEmpty()) {
            return;
        }

        Connection connection = getConnection();
        if (connection == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Sorry, Datenbankfehler!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        try (PreparedStatement deleteStmt = connection.prepareStatement(DELETE_STATEMENT)) {
            connection.setAutoCommit(false);
            deleteStmt.setString(1, name.get());
            deleteStmt.execute();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Löschen erfolgreich!");
            alert.showAndWait();

        } catch (Exception exc) {
            try {
                exc.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Sorry, Datenbankfehler beim Löschen!", ButtonType.OK);
                alert.showAndWait();
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gibt eine Liste mit den Namen aller Einträge aus der Datenbank zurück
     *
     * @return Liste mit Namen aller Einträge
     */
    private ArrayList<String> selectAllNames() {
        Connection connection = getConnection();
        ArrayList<String> result = new ArrayList<>();

        if (connection == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Sorry, Datenbankfehler!", ButtonType.OK);
            alert.showAndWait();
            return result;
        }

        try (Statement selectStmt = this.connection.createStatement();
             ResultSet resultSet = selectStmt.executeQuery(SELECT_NAMES_STATEMENT)) {

            while (resultSet.next()) {
                result.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Sorry, Datenbankfehler beim Lesen!", ButtonType.OK);
            alert.showAndWait();
        }
        return result;
    }
}
