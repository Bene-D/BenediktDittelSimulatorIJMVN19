package de.dittel.controller;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller-Klasse für die i18n-Ressourcen
 */
public class ResourcesController {

	private static final String PROP_CLASS = "i18n_resources.text";
	private static ResourcesController resourcesController = null;
	private Locale locale;
	private ResourceBundle bundle;
	private final ObservableResourceFactory resourceFactory;

	/**
	 * Getter für die Klasse
	 */
	public static ResourcesController getResourcesController() {
		if (ResourcesController.resourcesController == null) {
			ResourcesController.resourcesController = new ResourcesController();
		}
		return ResourcesController.resourcesController;
	}

	/**
	 * Konstruktor
	 */
	private ResourcesController() {
		try {
			String localeStr = PropertiesController.getPropertiesController().getLanguage();
			if (localeStr == null) {
				this.locale = Locale.getDefault();
			} else if (!localeStr.equals("de") && !localeStr.equals("en")) {
				this.locale = Locale.getDefault();
			} else {
				this.locale = new Locale(localeStr);
			}
		} catch (Exception e) {
			this.locale = Locale.getDefault();
		}
		Locale.setDefault(this.locale);
		this.bundle = ResourceBundle.getBundle(PROP_CLASS, this.locale);
		this.resourceFactory = new ObservableResourceFactory();
		this.resourceFactory.setResources(this.bundle);
	}

	/**
	 * Liefert den im Bundle hinterlegten Wert
	 *
	 * @param key Key für den gesuchten Wert
	 * @return Value der im Bundle hinterlegt ist
	 */
	public String getI18nValue(String key) {
		return this.bundle.getString(key);
	}

	public StringBinding i18n(String value) {
		return this.resourceFactory.getStringBinding(value);
	}

	/**
	 * Getter für locale
	 */
	public Locale getLocale() {
		return this.locale;
	}

	/**
	 * Setter für locale
	 */
	public void setLocale(Locale loc) {
		this.locale = loc;
		Locale.setDefault(this.locale);
		this.bundle = ResourceBundle.getBundle(PROP_CLASS, this.locale);
		this.resourceFactory.setResources(this.bundle);
	}

}

class ObservableResourceFactory {

	private final ObjectProperty<ResourceBundle> resources = new SimpleObjectProperty<>();

	public ObjectProperty<ResourceBundle> resourcesProperty() {
		return resources;
	}

	public final ResourceBundle getResources() {
		return resourcesProperty().get();
	}

	public final void setResources(ResourceBundle resources) {
		resourcesProperty().set(resources);
	}

	public StringBinding getStringBinding(String key) {
		return new StringBinding() {
			{
				bind(resourcesProperty());
			}

			@Override
			public String computeValue() {
				return getResources().getString(key);
			}
		};
	}
}