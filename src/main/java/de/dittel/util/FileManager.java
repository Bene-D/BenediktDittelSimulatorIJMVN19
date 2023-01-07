package de.dittel.util;

import de.dittel.model.Automaton;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Klasse zum Erzeugen/Compilieren/Speichern von Files
 */
public class FileManager {

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
     * @throws IOException Exception, die geworfen werden kann
     */
    public static File createNewAutomatonFile(String newAutomaton) throws IOException {
        File newAutomatonFile = new File("automata/" + newAutomaton + ".java");
        Path dummyData = Paths.get("automata/DefaultAutomaton.java");
        Charset charset = StandardCharsets.UTF_8;

        String content = Files.readString(dummyData, charset);
        content = content.replace("DefaultAutomaton", newAutomaton);
        Files.writeString(Path.of(newAutomatonFile.getPath()), content, charset);
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
            Class<?> newAutomatonClass = Class.forName(automaton.getName().replace(".java", ""), true, classLoader);
            return (Automaton) newAutomatonClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            Alert alert = new  Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ups, da ist was schief gelaufen:\n" + e);
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
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        boolean success = javac.run(null, null, err, automaton.getPath()) == 0;

        if (!success) {
            String msg = err.toString();
            Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
            alert.setTitle("Compilierergebnis");
            alert.showAndWait();
            return false;
        } else {
            return true;
        }
    }
}
