package de.dittel.controller;

import de.dittel.model.Automaton;
import de.dittel.model.Cell;
import de.dittel.util.ReferenceHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import javax.xml.stream.*;
import java.io.*;

/**
 * Controller-Klasse für den XML-Serilization Vorgang
 */
public class XMLSerializationController {

    private static final FileChooser fileChooser;
    private final ResourcesController resourcesController = ResourcesController.getResourcesController();


    static {
        fileChooser = new FileChooser();
        File dir = new File(".");
        fileChooser.setInitialDirectory(dir);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("*.xml", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
    }

    /**
     * Konstruktor
     *
     * @param referenceHandler verwaltet die verwendeten Referenzen
     */
    public XMLSerializationController(ReferenceHandler referenceHandler) {
        referenceHandler.getMainController().getXmlSerializationMenuItem().setOnAction(e -> savePopulation(referenceHandler));
        referenceHandler.getMainController().getXmlDeserializationMenuItem().setOnAction(e -> loadPopulation(referenceHandler));
    }

    /**
     * Speichert eine Population als XML-Datei ab
     *
     * @param referenceHandler verwaltet die verwendeten Referenzen
     */
    private void savePopulation(ReferenceHandler referenceHandler) {
        fileChooser.setTitle(resourcesController.getI18nValue("xmlSerializationFileChooserTitle"));
        File file = fileChooser.showSaveDialog(referenceHandler.getMainStage());

        if (file == null) {
            return;
        }

        if (!file.getName().endsWith(".xml")) {
            file = new File(file.getAbsolutePath().concat(".xml"));
        }

        try (FileOutputStream os = new FileOutputStream(file)) {
            writeXML(referenceHandler, os);
        } catch (Exception exc) {
            exc.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    resourcesController.getI18nValue("xmlSerializationError"), ButtonType.OK);
            alert.showAndWait();
        }
    }

    // XML-Format
    // <?xml version="1.0" ?>
    // <automaton numberOfStates="2" numberOfRows="3" numberOfColumns="2">
    // <cell row="0" column="0" state="0"></cell>
    // <cell row="0" column="1" state="1"></cell> ...
    // </automaton>

    /**
     * Erzeugt eine XMLOutputfactory und schreibt alle Zellen und deren Zustand in den OutputStream
     *
     * @param referenceHandler verwaltet die verwendeten Referenzen
     * @param os FileOutputStream
     * @throws Exception kann eine Reihe von Fehler werfen
     */
    private static void writeXML(ReferenceHandler referenceHandler, OutputStream os) throws Exception {
        Automaton automaton = referenceHandler.getAutomaton();
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = factory.createXMLStreamWriter(os); // not Closable :-(

        try {
            final String NEWLINE = System.lineSeparator();
            synchronized (automaton) {
                writer.writeStartDocument();
                writer.writeCharacters(NEWLINE);
                writer.writeStartElement("automaton");
                writer.writeAttribute("numberOfStates", String.valueOf(automaton.getNumberOfStates()));
                writer.writeAttribute("numberOfRows", String.valueOf(automaton.getNumberOfRows()));
                writer.writeAttribute("numberOfColumns", String.valueOf(automaton.getNumberOfColumns()));
                writer.writeCharacters(NEWLINE);

                for (int r = 0; r < automaton.getNumberOfRows(); r++) {
                    for (int c = 0; c < automaton.getNumberOfColumns(); c++) {
                        writer.writeStartElement("cell");
                        writer.writeAttribute("row", String.valueOf(r));
                        writer.writeAttribute("column", String.valueOf(c));
                        writer.writeAttribute("state", String.valueOf(automaton.getCell(r, c).getState()));
                        writer.writeEndElement(); // cell
                        writer.writeCharacters(NEWLINE);
                    }
                }
                writer.writeEndElement(); // automaton
                writer.writeCharacters(NEWLINE);
                writer.writeEndDocument();
            }
        } finally {
            writer.close();
        }
    }

    /**
     * Lädt eine Population aus einer XML-Datei und lädt diese in den aktuellen Automaten
     *
     * @param referenceHandler verwaltet die verwendeten Referenzen
     */
    private void loadPopulation(ReferenceHandler referenceHandler) {
        fileChooser.setTitle(resourcesController.getI18nValue("xmlDeserializationFileChooserTitle"));
        File file = fileChooser.showOpenDialog(referenceHandler.getMainStage());

        if (file != null) {
            try (FileInputStream is = new FileInputStream(file)) {
                loadXML(referenceHandler, is);
            } catch (Exception exc) {
                exc.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        resourcesController.getI18nValue("xmlDeserializationError"), ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    /**
     * Liest den InputStream der XML-Datei und erzeugt daraus eine neue Population
     *
     * @param referenceHandler verwaltet die verwendeten Referenzen
     * @param is InputStream
     */
    private void loadXML(ReferenceHandler referenceHandler, InputStream is) throws Exception {
        Automaton automaton = referenceHandler.getAutomaton();
        XMLStreamReader parser = null;

        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
            parser = factory.createXMLStreamReader(is);
            Cell[][] population = null;

            while (parser.hasNext()) {
                if (parser.getEventType() == XMLStreamConstants.START_ELEMENT) {
                    String element = parser.getLocalName();
                    if ("automaton".equals(element)) {
                        int numberOfStates = Integer.parseInt(parser.getAttributeValue(null,
                                "numberOfStates"));

                        if (numberOfStates > automaton.getNumberOfStates()) {
                            throw new Exception();
                        }

                        int numberOfRows = Integer.parseInt(parser.getAttributeValue(null,
                                "numberOfRows"));
                        int numberOfColumns = Integer.parseInt(parser.getAttributeValue(null,
                                "numberOfColumns"));
                        population = new Cell[numberOfRows][numberOfColumns];
                    } else if ("cell".equals(element)) {
                        if (population == null) {
                            throw new Exception();
                        }

                        int row = Integer.parseInt(parser.getAttributeValue(null, "row"));
                        int column = Integer.parseInt(parser.getAttributeValue(null, "column"));
                        int state = Integer.parseInt(parser.getAttributeValue(null, "state"));

                        population[row][column] = new Cell(state);
                    }
                }
                parser.next();
            }

            if (!automaton.changeCells(population)) {
                throw new Exception();
            }
        } finally {
            if (parser != null) {
                parser.close();
            }
        }
    }
}