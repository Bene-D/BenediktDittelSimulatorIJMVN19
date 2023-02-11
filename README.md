# Simulator für zellulaere Automaten

#### Dieses Projekt wurde im Wintersemester 2022/23 im Rahmen der Veranstaltung Java-Praktikum durchgeführt.
Es wurde ein Simulator für zelluläre Automaten - wie bsp. "Game of Life" entwickelt.
Siehe auch: https://de.wikipedia.org/wiki/Conways_Spiel_des_Lebens.

Das Programm erlaubt das Erstellen von neuen zellulären Automaten.
Dazu ist es erforderlich, dass die Transform-Methode des Automaten sowie die Anzahl der möglichen Zustände angepasst werden.
Als Grundlage ist ein DefaultAutomaton implementiert, der alle benötigten Funktionen bereitstellt.

Zusätzlich stehen die Automaten "Game of Life" und "Kruemelmonster" als Beispiele zur Verfügung.


## Benutzung:

Um das Programm mit benutzerdefinierten Einstellungen zu starten, können der _**casimulator.properties**_-Datei folgende Parameter hinzugefügt werden:

#### language=x
#### speed=y
- x kann "de" oder "en" sein (ohne " ")
- y kann ein Wert zwischen 200 - 3400 sein (200=sehr schnell, 1000=schnell, 1800=standard, 2600=langsam, 3400=sehr langsam)