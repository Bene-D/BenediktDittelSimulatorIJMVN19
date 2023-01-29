package de.dittel.controller;

import de.dittel.util.ReferenceHandler;

import java.util.Locale;

/**
 * Controller-Klasse für die Sprache
 */
public class LanguageController {

	/**
	 * Konstruktor
	 *
	 * @param referenceHandler verwaltet die verwendeten Referenzen
	 */
	public LanguageController(ReferenceHandler referenceHandler) {
		referenceHandler.getMainController().getLanguageToggleGroup().selectedToggleProperty()
				.addListener((observable, oldValue, newValue) -> {
					if (newValue == null) {
						return;
					}
					if (newValue == referenceHandler.getMainController().getLanguageEnglishMenuItem()) {
						ResourcesController.getResourcesController().setLocale(Locale.ENGLISH);
					} else if (newValue == referenceHandler.getMainController().getLanguageGermanMenuItem()) {
						ResourcesController.getResourcesController().setLocale(Locale.GERMAN);
					}
				});
	}
}
