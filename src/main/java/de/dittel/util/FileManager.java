package de.dittel.util;

import de.dittel.controller.ResourcesController;
import de.dittel.model.Automaton;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Klasse zum Erzeugen/Compilieren/Speichern von Files
 */
public class FileManager {

    private static final ResourcesController resourcesController = ResourcesController.getResourcesController();

    /**
     * Konstruktor
     * <p>
     * PRIVATE, um den impliziten Default-Konstruktor zu überdecken
     */
    private FileManager() {}

    /**
     * Helfermethode zum Erzeugen einer neuen Automatendatei
     *
     * @param newAutomaton Name des neuen Automaten
     */
    public static File createNewAutomatonFile(String newAutomaton) {
        File newAutomatonFile = null;

        try {
            newAutomatonFile = new File("automata/" + newAutomaton + ".java");
            List<String> lines =  new ArrayList<>(Arrays.asList(Resources.readResourcesFile
                    ("defaultAutomaton/DefaultAutomaton.txt").split(System.lineSeparator())));
            lines.set(5, lines.get(5).replace("DefaultAutomaton", newAutomaton));
            lines.set(12, lines.get(12).replace("DefaultAutomaton", newAutomaton));

            Files.write(Path.of(newAutomatonFile.getPath()), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    resourcesController.getI18nValue("createFileError") + "\n" + e);
            alert.showAndWait();
        }

        return newAutomatonFile;
    }

    /**
     * Lädt eine bereits compilierte Automaton-Datei
     *
     * @param automaton .java Datei des Automaten
     * @return Automaton-Klasse des File oder null bei Fehlschlagen
     */
    public static Automaton loadAutomaton(File automaton) {
        try {
            // Klasse laden
            URL classUrl = automaton.getParentFile().toURI().toURL();
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{classUrl});
            Class<?> newAutomatonClass = Class.forName(automaton.getName().replace
                    (".java", ""), true, classLoader);
            return (Automaton) newAutomatonClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            Alert alert = new  Alert(Alert.AlertType.ERROR);
            alert.setContentText(resourcesController.getI18nValue("loadAutomatonFileError") + "\n" + e);
            alert.show();
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Compiliert eine Automaton-Datei
     *
     * @param automaton File, das compiliert werden soll
     * @return true, falls erfolgreich; sonst false
     */
    public static boolean compile(File automaton) {
        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        CharArrayWriter err = new CharArrayWriter();

        try (StandardJavaFileManager manager = javac.getStandardFileManager
                (null, null, null)) {
            Iterable<? extends JavaFileObject> units = manager.getJavaFileObjectsFromFiles
                    (Collections.singletonList(automaton));
            List<String> options = new ArrayList<>();
            options.add("-parameters");
            JavaCompiler.CompilationTask task = javac.getTask(err, manager,
                    null, options, null, units);

        boolean success = task.call();

        if (!success) {
            String msg = err.toString();
            Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
            alert.setTitle(resourcesController.getI18nValue("compileResult"));
            alert.showAndWait();

            return false;
        } else {
            return true;
        }
        } catch (IOException exc) {
            return false;
        }
    }

    /**
     * Erstellt die Automaten "Default", "Gol" oder "Kruemelmonster", falls diese nicht vorhanden sind
     */
    public static void createDefaultAutomaton(String name) {
        try {
            File defaultFile = new File("automata/" + name + ".java");
            List<String> lines = new ArrayList<>(Arrays.asList(Resources.readResourcesFile
                    ("defaultAutomaton/" + name + ".txt").split(System.lineSeparator())));
            Files.write(Path.of(defaultFile.getPath()), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                      resourcesController.getI18nValue("createDefAutomatonError") + "\n" + e);
            alert.showAndWait();
        }
    }

    /**
     * Erstellt die Properties-Datei, falls diese nicht vorhanden ist
     */
    public static void createCasFile() {
        try {
            File casFile = new File("casimulator.properties");
            List<String> lines = new ArrayList<>(Arrays.asList(Resources.readResourcesFile
                    ("properties/casimulator.txt").split(System.lineSeparator())));
            Files.write(Path.of(casFile.getPath()), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                     resourcesController.getI18nValue("createCasFileError") + "\n" + e);
            alert.showAndWait();
        }
    }

    public static void createReadMe() {
        try {
            File readMe = new File("README.txt");
            List<String> lines = new ArrayList<>(Arrays.asList(Resources.readResourcesFile
                    ("readme/readme.txt").split(System.lineSeparator())));
            Files.write(Path.of(readMe.getPath()), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    resourcesController.getI18nValue("createReadmeError") + "\n" + e);
            alert.showAndWait();
        }
    }
}