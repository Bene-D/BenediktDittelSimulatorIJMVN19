package de.dittel.controller;

import javafx.scene.control.Alert;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Controller-Klasse zum Laden der benutzerdefinierten Einstellungen
 */
public class PropertiesController {

	private static final String PROPFILE = "casimulator.properties";
	private static final String PROP_LANGUAGE = "language";
	private static final String DEF_LANGUAGE = "de";
	private static PropertiesController propertiesController = null;
	private final Properties properties;

	/**
	 * Getter für die Klasse
	 */
	public static PropertiesController getPropertiesController() {
		if (PropertiesController.propertiesController == null) {
			PropertiesController.propertiesController = new PropertiesController();
		}
		return PropertiesController.propertiesController;
	}
	private static final String PROP_SPEED = "speed";
	private static final int DEF_SPEED = SimulationController.DEF_SPEED;

	/**
	 * Konstruktor
	 */
	private PropertiesController() {
		this.properties = new Properties();
		try (FileInputStream propertiesFile = new FileInputStream(PropertiesController.PROPFILE)) {
			this.properties.load(propertiesFile);
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR, "Sorry, Fehler beim Laden der casimulator.properties Datei!");
			alert.showAndWait();
		}
	}

	/**
	 * Getter für language
	 */
	public String getLanguage() {
		String language = this.properties.getProperty(PropertiesController.PROP_LANGUAGE);
		return language == null ? DEF_LANGUAGE : language;
	}

	/**
	 * Getter für speed
	 */
	public int getSpeed() {
		String speed = this.properties.getProperty(PropertiesController.PROP_SPEED);
		if (speed == null) {
			return DEF_SPEED;
		}
		try {
			return Integer.parseInt(speed);
		} catch (NumberFormatException exc) {
			return DEF_SPEED;
		}
	}
}
