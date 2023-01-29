package de.dittel.view;

import de.dittel.model.Callable;
import de.dittel.util.ReferenceHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasse zur Darstellung eines ContextMenu des PopulationPanel
 */
public class PopulationPanelContextMenu extends ContextMenu {

    private final List<Method> methods;

    /**
     * Konstruktor
     * <p>
     * Fügt alle validen Methoden dem ContextMenu hinzu
     *
     * @param referenceHandler verwaltet die verwendeten Referenzen
     */
    public PopulationPanelContextMenu(ReferenceHandler referenceHandler) {
        methods = getValidMethods(referenceHandler);


        for (Method method : methods) {
            method.setAccessible(true);
            String functionType = method.getReturnType().getName();
            String functionName = method.getName();
            StringBuilder functionParameter = new StringBuilder();
            int numberOfParameters = method.getParameters().length;

            for (Parameter parameter : method.getParameters()) {
                numberOfParameters--;
                functionParameter.append(parameter.getType().getName()).append(" ");
                functionParameter.append(parameter.getName());
                if (numberOfParameters != 0) {
                    functionParameter.append(", ");
                }
            }

            MenuItem item = new MenuItem(functionType + " " + functionName + "(" + functionParameter + ")");
            getItems().add(item);
        }
    }

    /**
     * Liefert eine Liste mit allen Methoden des Automaten
     *
     * @return Liste mit allen Methoden des Automaten
     */
    public List<Method> getValidMethods() {
        return methods;
    }

    /**
     * Liefert eine Liste mit allen Methoden, die den Kriterien entsprechen
     *
     * @param referenceHandler verwaltet die verwendeten Referenzen
     * @return Liste mit allen validen Methoden
     */
    private List<Method> getValidMethods(ReferenceHandler referenceHandler) {
        List<Method> res = new ArrayList<>();
        for (Method method : referenceHandler.getAutomaton().getClass().getDeclaredMethods()) {
            if (!Modifier.isStatic(method.getModifiers())
                    && !Modifier.isAbstract(method.getModifiers())
                    && method.getParameterCount() == 2
                    && method.getParameterTypes()[0].equals(int.class)
                    && method.getParameterTypes()[1].equals(int.class)
                    && method.isAnnotationPresent(Callable.class)) {
                res.add(method);
            }
        }
        return res;
    }
}
