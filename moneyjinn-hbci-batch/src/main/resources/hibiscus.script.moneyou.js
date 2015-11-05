/******************************************************************************************************
 * Script zum PlugIn für die Scripting-Funktionen von Jameica/Hibiscus
/******************************************************************************************************
* Hibiscus-Script für MoneYou (Tagesgeld, Festgeld) [Germany]
* Original:
* http://hibiscus-scripting.derrichter.de
*
* Creation Date: 03.01.2014 [GMT+1]
* @author Sebastian Richter
*
* @version Revision: 2.1.10
*          Date: 18.12.2014 19:58 [GMT+1]
*
* Copyright (c) Sebastian Richter / http://hibiscus-scripting.derrichter.de / All rights reserved
*
* HINWEIS:
* SOLLTE DIESES SCRIPT ODER TEILE DARAUS KOPIERT ODER FÜR EIGENE ZWECKE WEITERVERWENDET WERDEN
* IST DER AUTOR SEBASTIAN RICHTER DARÜBER ZU INFORMIEREN UND SEINE EINWILLIGUNG ZU BESTÄTIGEN!
*
* LIZENZ:
* Hibiscus Scripting-PlugIns (alle Varianten) von Sebastian Richter (HIBISCUS-SCRIPITNG PROJECT)
* sind lizenziert unter:
* CC BY-NC-ND 4.0 - http://creativecommons.org/licenses/by-nc-nd/4.0/deed.de
*
* ZUWIEDERHANDLUNGEN WERDEN JE NACH BEDARF
* - GEMELDET
* - ZUR ANZEIGE GEBRACHT
* - MIT EINER UNTERLASSUNGSKLAGE VERBOTEN
* - MIT EINER SCHADENERSATZFORDERUNG VERHÄNGT
*
* IN JEDEM FALL IST ALS QUELLENVERWEIS UND AUTOR SEBASTIAN RICHTER / HIBISCUS-SCRIPTING-PROJEKT ANZUGEBEN!
*
* GEISTIGES EIGENTUM VON SEBASTIAN RICHTER - HIBISCUS-SCRIPTING-PROJECT
* UND EVENTUELL IN TEILEN DEM URSPRÜNGLICHEN AUTOR WIE ANGEGEBEN (WENN ZUTREFFEND)
*
/*****************************************************************************************************/



/* VersionsHistory
*
* ver. 2.1.10
*
* NEW [141217]: Offizielle Freigabe und Unterstützung für Java Runtime 8 (empfohlen ab JRE 8 Update 25); Hierdurch Sicherheitsgewinn und erheblicher Geschwindigkeitszuwachs beim Laden der PlugIns
* CHG [141121]: Vorbereitung für: Festgeldkonten werden nun nur noch einmal pro Jameica-Sitzung aktualisiert; Somit schnellerer Abruf bei mehreren Tagesgeldkonten
* BUG [141121]: Nach automatischer Verlängerung eines Festgeld-Kontos wird nun der Salo und der Zwischensaldo des neun Umsatzes richtig gesetzt und nicht mit dem alten Guthaben addiert
* BUG [140709]: Die Funktion "SecureLogout" wird nun auch bei z. B. einem falschem Passwort wieder vollständig fehlerfrei durchgeführt; Logout wird hierbei auch wieder richtig ausgeführt
* CHG [140709]: Überprüfung auf Festgeld-Konto abgeändert unter Berücksichtigung dass die Nutzer-ID geändert werden kann und zwischen 6 und 20 Zeichen lang darf (Danke an RedBull800 aus dem Forum)
* NEW [140709]: Kennwort-Änderungsassistent: sollte das Kennwort auf der Homepage der MoneYou geändert werden müssen bietet das PlugIn automatisch die gleiche Funktion an
* CHG [140616]: Die aktuelle Webseite wird nun nicht mehr an die nächste Funktion weitergegeben; Diese liest stattdessen die aktuelle Seite des aktuellen Browserfensters aus
* CHG [140609]: Der Identifier in der Log-Datei (jameica.log) trägt nun passender Weise den Zusatz "-PlugIn"
*
*
*
* ver. 2.1.2
*
* NEW [140521]: Vollständige Verwaltung von MoneYou Festgeld-Anlagen (Danke an Fabian für die Daten)
*		- neue Festgeld-Anlagen werden immer erkannt (Anglegt und Anlage-Umsatz eingetragen) insofern niemals der Assistent hierfür deaktiviert wurde, dieser muss also in der Konfiguration fehlen
*		- die Konten für Festgeld-Anlage werden nun auch automatisch aktualisiert und die aktuell "aufgelaufenen Zinsen" eingetragen
*		- bei Auslauf einer Festgeld-Anlage wird das entsprechende Offline-Konto in Hibiscus um den Abbuchungsumsatz erweitert und automatisch deaktiviert (nicht einfach gelöscht)
*		- bei automatischer Verlängerung einer Festgeld-Anlage wird im Falle dass die Zinsen inklusive im Anlagebetrag sein sollen, automatisch ein Umsatz für die Zinsgutschrift angelegt (ungetestet)
* CHG [140517]: Aufgelaufene Zinsen werden im Kommentarfeld nun gleich in der ersten Zeile angegeben; Unter Windows und Mac hat man in der Kontoübersicht somit eine Notiz auf den ersten Blick
* BUG [140516]: Es wird nun auch wieder im Log-Modus "Info" und "Warn" die Fehlermeldung ins Systemprotokoll (jameica.log) geschrieben wie Sie erzeugt wurde
* BUG [140506]: Fehlendes Deklarieren der Datenbankverbindung vor dem Setzen von Systemnachrichten eingefügt
* BUG [140414]: Es werden nun auch mehrere laufende Festgelder automatisch als Offline-Konto in Hibiscus angelegt
*
*
*
* ver. 2.1.1
*
* CHG [140409]: Die neue Anlagefunktion für Festgeldkonten wurde an die neue Struktur der MoneYou-Homepage angepasst; seit 07.04.2014 Unterteilung in laufende und geschlossene Festgeld-Anlagen
*		Hinweise:	- Die automatische Kontoanlage für Festgeld legt im Moment erst mal nur "laufende Festgelder" an, geschlossene folgen (eventuell Vorschläge was dann geschehen soll)
*				- Die aktuell laufenden Zinsen (neu seit 07.04.2014) werden nicht mit ins Kommentarfeld eingetragen da diese (im Moment) auch nicht aktualisiert werden
*				- Das Scripting-PlugIn reagiert nicht auf solche Konten für die Festgeld-Anlage da diese ja auch keine Kontoverbindung und Buchungsvorgänge aufweisen
*				- momentan werden nur einmal Konten hierzu angelegt; Festgeld-Anlagen die folgen müssen noch umgesetzt werden
* BUG [140408]: Der Secure-LogOut fürt nun ohne interne Fehler einen LogOut auf der MoneYou-Seite durch und lässt somit Fehlermeldungen weiter
* BUG [140408]: Aktuell aufgelaufene Zinsen die in das Feld Unterkonto eingetragen werden verursachen keinen Fehler mehr aufgrund der Länge
* BUG [140407]: Der Kontoanlage-Assistent unterstützt nun auch das Anlegen eines Kontos unter einer Java 8 Umgebung
* NEW [140407]: Festgeld-Konten: Sollten im MoneYou-Account Festgeld-Anlagen existieren wird der Benutzer nach einer automatischen Anlage eines Offline-Kontos für diese gefragt; somit sind auch diese Beträge in Hibiscus erfasst
* BUG [140403]: Anpassung des Setzen des Java-Systemproxy für Java 8
* BUG [140402]: Bei der Javacodepage-Prüfung wird nun kompatibel zu den neuen JavaScript-Strings nach Übereinstimmungen gesucht
* BUG [140331]: In der Java- und HTMLUnit-Versionsprüfung werden nun alle Zeichenketten als JavaScript-Strings erstellt; Funktionalität unter allen Java- bzw. JavaScript-Umgebungen
* CHG [140325]: Konvertieren aller Date.Funktionen in ein Java-Datum immer über reine Integer-Werte; höchste Kompatibilität unter den verschiedensten Systemen und Installationen
* NEW [140325]: Unterstützung von Java 8 aka 1.8; Laden der "nashorn:mozilla_compat.js" für die Funktion von "importPackage" und Angabe des statischen Klassenpfads bei Übergabe als Attribut
* CHG [140314]: Die Kontonummer wird nicht mehr mit in den Hash eingerechnet; Dies bedeutet ein Passwort-Eintrag pro Anmeldekennung, also einmal pro Account mit allen darin enthaltenen Konten
* CHG [140314]: Information im Kontoanlage-Assistenten für die Kontonummer hinzugefügt; Sind die letzten 10 Stellen der IBAN die man Online findet
* CHG [140204]: Erweiterung der Anforderung des Zeichensatz der unter Linux verwendeten Java-Umgebung um "ISO-8859-15"; Nicht alle Distributionen haben auf UTF-8 gewechselt (Java-Versionsprüfung Version 1.2.1)
* CHG [140308]: Anpassung an HTMLUnit Version 2.14; Mindestanforderung bleibt bei 2.13
*
*
*
* Release 2.1.0 (aka ver. 1.2.0)
* ==============================
*
* Neuerungen und Features:
* 	NEW [140203]: Einführung und Angleichung der Passwort(PIN)-Funktionalität von Hibiscus. Je nach Einstellung in Hibiscus werden PINs nun für die Sitzung zwischengespeichert oder permanent gespeichert
* 	NEW [140131]: neue Post-Eingangsbenachrichtigung: Es werden "neue Nachrichten" der Bank ermittelt und in das Informationsfenster für neue Systemachrichten der Bank auf der Startseite eingetragen
*		      [in Jameica/Hibiscus 2.4.x wird der Benutzer detailliert über ein Infofenster informiert welches bestätigt werden muss] (Funktion einstellbar)
*		      -- Diese Funktion ist im Moment leider nicht abgeschlossen da es der MoneYou auch nach mehrmaliger Aufforderung nicht möglich war eine Nachricht in die Postbox zu stellen --
*
* Änderungen und Anpassungen:
* 	CHG [140131]: Verwendungszweck um zwei weitere Zeilen erweitert damit die mindestens 140 Zeichen eines SEPA-Verwendungszweck alle erfasst werden
*
* Bugfixes (Fehlerbehebungen und Anpassungen):
* 	BUG [140203]: Formatierung der Kontodetail-Informationen im Kommentarfeld nochmals an die verschiedenen Betriebssysteme angepasst
* 	BUG [140203]: Abruf der Kontodetailinformationen wird nun bei jedem Konto immer durchgeführt und nicht nur einmal pro Sitzung für den Account
*
*
*
*
*
* ver. 1.1.0 (Beta)
*
* CHG [140131]: Sollte der Verwendungszweck leer sein wird hier nun als Ausgleich die Umsatzart eingetragen um einen bessern ersten Blick zu haben
* BUG [140131]: Neue oder alte Umsätze werden nun nicht mehr doppelt gezählt
* NEW [140130]: Erweiterung um den Abruf von Informationen der Bank um die Details des Konto (hier wird bei der Formatierung das verwendete Betriebssystem berücksichtigt)
*		(ist das Feld Notiz (Kommentar) frei (leer) werden hier die Informationen gespeichert; hier wird bei der Formatierung das verwendete Betriebssystem berücksichtigt)
* NEW [140114]: Erweiterung der Funktionen für den Fall dass im gewählten Abrufzeitraum keine Umsätze vorhanden sind
* BUG [140114]: Fehlerhafter Abbruch wenn die Kontonummer in der Auswahl auf der Homepage nicht gefunden wurde
*
*
*
* ver. 1.0.0 (Beta)
* =================
*
* Features:
* - Umsetzung der neuen Scripting-Synchronisierung (ab Jameica/Hibiscus 2.6): es wurden die derzeit möglichen Geschäftsvorfälle von Jameica/Hibiscus umgesetzt
*   (der derzeitige Versionsstand unterstützt dabei voll und ganz (im Moment noch) die Programmversion 2.4.x von Jameica/Hibiscus)
*   > Konto erscheint nun in der Liste "Konten synchronisieren" und wird bei einer Komplett-Synchronisierung aller Konten mit eingeschlossen
*   > Umsätze werden dabei nun gleich in der Liste "Neue Umsätze" angezeigt
*   > Für das Konto kann "Saldo aktualisieren" und "Kontoauszüge (Umsätze abrufen) abrufen" aktiviert/deaktiviert werden 
*     (das Script überspringt dies dann, gilt aber nur für den Abruf über die Startseite mit 'Synchronisierung starten',
*     bei Abruf über das Kontextmenü oder die Detailansicht des Kontos wird immer beides abgerufen)
* - HTMLUnit-Versions-Check (interne Version 1.3.7): diese prüft ob und welche HTMLUnit installiert ist
* - Java-Versions- und Codepage-Check (interne Version 1.2.0): die prüft die Java-Umgebung auf die notwendige Mindestversion und auf die verwendete Codepage (Zeichencodierung)
* - Sicherheits-Logout: wird automatisch bei Fehlern ausgeführt (dieser setzt unter anderem das Passwort zurück)
* - Assistent zur automatischen Konto-Anlage (1.3.1): Dieser unterstützt auch das Anlegen zusätzlicher Konten 
*   INFO: um diesen Assistenten zu starten: rufen Sie über das Hauptmenü die Jameica-Einstellungen auf und klicken dort einfach auf 'Speichern'
*   Dieser Assistent kann auch dauerhaft deaktiviert werden indem man "Diese Frage nicht mehr anzeigen" aktiviert und zusätzlich 'Nein' wählt
*   (Um dies rückgängig zu machen muss in der Datei "<Jameica-Profilpfad>\cfg\de.willuhn.jameica.system.ApplicationCallback.properties" die entsprechende Zeile gelöscht werden)
* - Erweiterte Fehlerbehandlung der Release Version 2.1 (mit automatischer Formatierung)
* - Routine falls der "Kontostand im Kontoauszug nicht aktuell" ist oder der "Kontostand im Kontoauszug aktualisiert wurde obwohl die Buchungen dazu noch fehlen"
* 	- Umfangreich verbesserte Erkennung von "nicht aktuelles Kontosaldo" und erweitert auf "fehlenden Buchungen", welche bis unter zwei neuen Buchungen 100% genau ist und danach die Berechnung wenn notwendig anders ausführt
*   	(diese Funktion merkt sich nun auch den letzten Status und orientiert sich daran und gibt diesen weiter aus bis er sich ändert)
*   	Die Zwischensalden werden dann je nach Situation nur für neue Umsätze (nach letztem Umsatz) berechnet. 
*   	Zur besseren Erkennung werden alte Umsätze (vor letztem Umsatz), in solch einer Situation, mit Zwischensaldo "0,00" gespeichert und können selbst gesetzt werden
* 	- Zusätzlich wird bei scheinbar richtigem Gleichnis zwischen Konto und Auszug bei Bedarf die letzten 30 Zwischensalden automatisch neu berechnet (sozusagen automatische Korrektur von falschem Zwischensaldo)
*   	(dies bedeutet dass selbst wenn der letzte Zwischensaldo wirklich mal falsch gesetzt werden sollte, werden bei einem erneutem Abruf (bei dem zu diesem Zeitpunkt auf dem Konto alles aktuell ist) die letzten 30 Zwischensummen automatisch korrigiert)
*	- Bei einem Erstabruf (noch keine Umsätze im Konto) muss das Kontosaldo aktuell sein und es dürfen keine Umsätze fehlen, ansonten stimmen die berechneten Zwischensalden nicht
* - Proxy-Unterstützung: vollständige Unterstützung der Proxy-Funktionalität von Jameica (interne Version 1.4.2)
*   ("Systemproxy benutzen" wird dann wegen Java nur funktionieren wenn in den Java-Einstellungen dies auch so konfiguriert ist 
*   und dort kein eigener Proxy eingestellt ist.) Eine eventuell notwendige Proxy-Authentifizierung müsste im Moment im System hinterlegt werden 
*   und/oder von diesen geregelt werden da dies Jameica nicht unterstützt. (z. B. Benutzername/Passwort)
*   (eine Integration einer Authentifizierung mit statischen Benutzer/Passwort erfolgt in einer späteren Version, falls gewünscht eventuell früher)
*
* Zusätzliche Besonderheiten und Features für das Script der MoneYou:
* - Eine Funktion erkennt und prüft wie im Online-Portal auch offene Punkte zum Abschluss der Kontoeröffnung und gibt diese übersichtlich aus
*   Somit kann über Hibiscus der Status der Kontoeröffnung geprüft werden
*
*/




/*******************************************************************************
 * Crossover-Unterstützung von Java6/7-Code unter Java 8 aka 1.8
 *******************************************************************************/
// Laden des Kompatibilitäts-Scripts für die Mozilla JavaScript-Engine
try {
	load("nashorn:mozilla_compat.js");
	var Java8upFrame = true;

} catch(e) {
	// es konnte kein Kompatibilitäts-Script geladen werden
	// somit ist eine Java-Umgebung älter als Java 8 aktiv oder fehlt
	var Java8upFrame = false;
};
/*******************************************************************************/



/*******************************************************************************
 * Importieren der Standard-Javapackete für das Hibiscus Banking Scripting
 *******************************************************************************/
// Import der Packete aus Jameica für Logging, System, HBCI, und Sync-Funktionen
importPackage(Packages.java.lang);

// Import der Packete aus HTMLUnit für den Webseiten-Abruf
importPackage(Packages.com.gargoylesoftware.htmlunit);
importPackage(Packages.com.gargoylesoftware.htmlunit.html);
importPackage(Packages.com.gargoylesoftware.htmlunit.util);
/*******************************************************************************/



/*******************************************************************************
 * Konfiguration und Cache
 *******************************************************************************/

/****** BENUTZER-BEREICH - DIESER KANN NACH DEN VORGABEN EINGESTELLT WERDEN! *******/ 

/****** ENTWICKLER-BEREICH - DIESEN NICHT ÄNDERN! *******/ 
// Versions-Nummer setzen
var MoneYou_Script_Version = "2.1.10";
 
// Diese BLZ muss im Hibiscus-Konto hinterlegt sein
var HibiscusScripting_MoneYou_BLZ = "50324040";
 
// Cache für Passwort
var MoneYou_Benutzer = "";

// aktuelle URL-Adresse
var HibiscusScripting_MoneYou_URL = "https://secure.moneyou.de";

// aktuelle URL-Adresse für den Login
var HibiscusScripting_MoneYou_LoginURL = "https://secure.moneyou.de/thc/policyenforcer/pages/loginB2C.jsf";

// aktuelle URL-Adresse für den Mailbox-Status
var HibiscusScripting_MoneYou_UnreadMailCheck = "https://banking.MoneYou.de/PostboxStatus";

// aktuelle URL-Adresse für die Übersicht auf (z. B. Umsatzübersicht oder Übersicht der Rechnungsdownload oder Kreditkartenübersicht)
var HibiscusScripting_MoneYou_Select = "https://banking.MoneYou.de/TransactionBanking";

// aktuelle URL-Adresse für den CSV-Abruf
var HibiscusScripting_MoneYou_getCsvURL = "https://banking.MoneYou.de/TransactionSearch";

// aktuelle URL-Adresse für den Logout
var HibiscusScripting_MoneYou_LogoutURL = "https://banking.MoneYou.de/logout";

// Ident für Log-Einträge und für Exceptions (Aufbau LogIdent/ExcIdent ist identisch mit anderen Scripts um Code einfach mehrfach zu verwenden)
var LogIdent = "";
var LogIdentMoneYou = "MoneYou-PlugIn: ";

// String für Infofenster-Überschrift "Fehlermeldung der Bank" in Dialogen
var MoneYouErrorTitle = new java.lang.String("\nFehlermeldung der MoneYou (ABN AMRO Bank):\n\n\n");

// Variable für den Check von Festgeldkonten
if (!MoneYou_FixMoney_Checked) { var MoneYou_FixMoney_Checked = false; };

// verwendetes Betriebssystem
var OSArtString = java.lang.System.getProperty("os.name");

// verwendete Version der Java Runtime Umgebung
var JavaVerString = java.lang.System.getProperty("java.version");

// Herausgeber der Java Umgebung
var JavaVenString = java.lang.System.getProperty("java.vendor");

// Codpage (Zeichencodierung) der Javaumgebung in diesem System
var JavaSysCodePage = java.lang.System.getProperty("file.encoding");
/*******************************************************************************/



/*******************************************************************************
 * Initialisierung
 *******************************************************************************/
LogIdent = LogIdentMoneYou;




function HibiscusScripting_MoneYou_kontoSync(iban, kundennummer, password) {
/*******************************************************************************
 * MoneYou (ABN AMRO Bank) Sync (registrierte Hauptfunktion die den Sync startet)
 *******************************************************************************/

	LogIdent = LogIdentMoneYou;

	// Ausgabe der Versionsnummer des Scripts, ist oben unter Konfiguration einzustellen
	// System.out.println(LogIdent + "Version " + MoneYou_Script_Version + " wurde gestartet ...");

	try {
		/*******************************************************************************
		 * Java-Versions- und Codepage-Check ver. 1.2.1
		 * (Prüft ob Java in der richtigen Version verwendet wird, und ob die Codepage stimmt)
		 *******************************************************************************/
		// System.out.println(LogIdent+"\u00dcberpr\u00FCfe Java-Version und den verwendeten Zeichensatz ...");
		var minJavaVer = String("1.6"); // hier stellt man die gewünschte Mindestversion der verwendeten Java-Umgebung für Jameica ein (Format 1.6)
		
		if (OSArtString.contains("Mac")) { var toHaveCodepage = String("MacRoman"); }
		else if (OSArtString.contains("Win")) { var toHaveCodepage = String("Cp1252"); }
		else { var toHaveCodepage = String("UTF-8, ISO-8859-15"); };
		var makeCodepageInfo = false;
		
		try {
			var JavaVer = String(java.lang.System.getProperty("java.version")).substring(0,3); // ermittelt die aktuell verwendete Java-Umgebung von Jameica
			
		} catch(err) {
			throw ("Java-Version konnte nicht ermittelt werden. Stellen Sie sicher dass Java mindestens in der Version "+minJavaVer+" installiert ist");
		};
		
		// System.out.println(LogIdent+"minJavaVer: " + minJavaVer);
		// System.out.println(LogIdent+"JavaVer: " + JavaVer);
		// System.out.println(LogIdent+"toHaveCodepage: " + toHaveCodepage);
		// System.out.println(LogIdent+"JavaSysCodePage: " + JavaSysCodePage);
		
		// die Werte werden zum geteilt da Nachkommastellen von JavaScript rein matematisch genutzt und somit gekürzt werden (x.10 wird zu x.1)
		var minJavaArray = minJavaVer.split(".");
		// System.out.println(LogIdent+"minJavaArray[0]: " + minJavaArray[0] + " / in parseFloat: " +parseFloat(minJavaArray[0]) + "    und    minJavaArray[1]: " + minJavaArray[1] + " / in parseFloat: " +parseFloat(minJavaArray[1]));
		var JavaVerArray = JavaVer.split(".");
		// System.out.println(LogIdent+"JavaVerArray[0]: " + JavaVerArray[0] + " / in parseFloat: " +parseFloat(JavaVerArray[0]) + "    und    JavaVerArray[1]: " + JavaVerArray[1] + " / in parseFloat: " +parseFloat(JavaVerArray[1]));
		
		// nun starten wir einen logischen Vergleich als Nummern statt String
		if ((parseFloat(JavaVerArray[0]) < parseFloat(minJavaArray[0])) || 
		   ((parseFloat(JavaVerArray[0]) == parseFloat(minJavaArray[0])) && (parseFloat(JavaVerArray[1]) < parseFloat(minJavaArray[1])))) {
			// System.out.println(LogIdent+"Java-Version zu niedrig. Mindestes Version "+minJavaVer+" wird ben\u00f6tigt. (aktuell verwendete Umgebung durch Jameica ist "+ JavaVerString +")");
			throw ("Java-Version zu niedrig. Mindestes Version "+minJavaVer+" wird ben\u00f6tigt. (aktuell verwendete Umgebung durch Jameica ist "+ JavaVerString +")");

		} else {
			// Prüfe auf verwendete und somit richtige Codepage
			if ((String(JavaSysCodePage).indexOf(toHaveCodepage) != -1) || (toHaveCodepage.indexOf(String(JavaSysCodePage)) != -1)) {
				// System.out.println(LogIdent+"Java-Version " + JavaVerString + " von '" + JavaVenString + "' installiert, aktiv und verwendet Zeichensatz '" + JavaSysCodePage + "'");
			
			} else {
				// System.out.println(LogIdent+"ACHTUNG: Java-Version " + JavaVerString + " ist zwar OK aber verwendet falschen Zeichensatz '" + JavaSysCodePage + "' (Richtig w\u00e4re '" + toHaveCodepage + "')");
				makeCodepageInfo = true;
			};			
			
		};
		/******************************************************************************/



		/*******************************************************************************
		 * HTMLUnit-Versions-Check ver. 1.3.7
		 * (Prüft ob HTMLUnit installiert ist, checkt die Version und gibt diese aus)
		 *******************************************************************************/
		// System.out.println(LogIdent+"\u00dcberpr\u00FCfe HTMLUnit-Version ...");
		var minHTMLUnitVer = String("2.13"); // hier stellt man die gewünschte Mindestversion der installierten HTMLUnit ein (Format 2.9 oder 2.10)
		
		try {
			// ermittelt die installierte HTMLUnit-Version
			var HTMLUnitVer = String(com.gargoylesoftware.htmlunit.Version.getProductVersion());
			
		} catch(err) {
			throw ("HTMLUnit-Version konnte nicht ermittelt werden. Stellen Sie sicher dass HTMLUnit mindestens in der Version "+minHTMLUnitVer+" installiert ist");
		};
		
		// System.out.println(LogIdent+"minHTMLUnitVer: " + minHTMLUnitVer);
		// System.out.println(LogIdent+"HTMLUnitVer: " + HTMLUnitVer);
		
		// die Werte werden zum geteilt da Nachkommastellen von JavaScript rein matematisch genutzt und somit gekürzt werden (x.10 wird zu x.1)
		var minHTMLUnitArray = minHTMLUnitVer.split(".");
		// System.out.println(LogIdent+"minHTMLUnitArray[0]: " + minHTMLUnitArray[0] + " / in parseFloat: " +parseFloat(minHTMLUnitArray[0]) + "    und    minHTMLUnitArray[1]: " + minHTMLUnitArray[1] + " / in parseFloat: " +parseFloat(minHTMLUnitArray[1]));
		var HTMLUnitVerArray = HTMLUnitVer.split(".");
		// System.out.println(LogIdent+"HTMLUnitVerArray[0]: " + HTMLUnitVerArray[0] + " / in parseFloat: " +parseFloat(HTMLUnitVerArray[0]) + "    und    HTMLUnitVerArray[1]: " + HTMLUnitVerArray[1] + " / in parseFloat: " +parseFloat(HTMLUnitVerArray[1]));
		
		// nun starten wir einen logischen Vergleich als Nummern statt String
		if ((parseFloat(HTMLUnitVerArray[0]) < parseFloat(minHTMLUnitArray[0])) || 
		   ((parseFloat(HTMLUnitVerArray[0]) == parseFloat(minHTMLUnitArray[0])) && (parseFloat(HTMLUnitVerArray[1]) < parseFloat(minHTMLUnitArray[1])))) {
			// System.out.println(LogIdent+"HTMLUnit-Version zu niedrig. Mindestes Version "+minHTMLUnitVer+" wird ben\u00f6tigt. (Ihre Version ist "+ HTMLUnitVer +")");
			throw ("HTMLUnit-Version zu niedrig. Mindestes Version "+minHTMLUnitVer+" wird ben\u00f6tigt. (Ihre Version ist "+ HTMLUnitVer +")");

		} else {
			// System.out.println(LogIdent+"OK: HTMLUnit-Version " + HTMLUnitVer + " installiert und aktiv");
		};
		/******************************************************************************/
		
		
		
		//*******************************************************************************
		// Initialisieren des webClients
		//*******************************************************************************
		try {
			HibiscusScripting_MoneYou_prepareClient();
			
		} catch(err) {
			// System.out.println(LogIdent +err);
			throw err;
			
		};
		//*******************************************************************************

		
				
		//*******************************************************************************
		// Login, inkl. Passwortabfrage beim User
		//*******************************************************************************
		// Setzen des Login für den aktuellen Abruf
		MoneYou_Benutzer = kundennummer;
		MoneYou_Passwort = password; 
			//*********************************************************************************
			// Login: Funktionsaufruf mit Benutzername und Passwort
			//*********************************************************************************
			try {
				var PostLoginPage = HibiscusScripting_MoneYou_HttpsLogin(MoneYou_Benutzer, MoneYou_Passwort, webClient);
				
			} catch(err) {
				try {
					HibiscusScripting_MoneYou_SecLogout("noLogin", webClient);
				} catch(secerr) {
					// System.out.println(LogIdent+"SecLogout konnte nicht ohne Fehler durchgef\u00fchrt werden. Fehler: " +secerr);
				};
				throw ("Login fehlgeschlagen! " +err);
			}

		// Der Login sollte funktioniert haben also speichern wir das Passwort wie eingestellt
		//******************************************************************************
  
		// System.out.println(LogIdent+"MoneYou-Login war erfolgreich");
		
		
		//*********************************************************************************
		// Kontodetailansicht: Funktionsaufruf für den Abruf von Details zum Konto
		//*********************************************************************************
		try {
			var PostLoginPage = HibiscusScripting_MoneYou_getAccInfo(iban, webClient);
			
		} catch(err) {
			try {
				HibiscusScripting_MoneYou_SecLogout("Login", webClient);
			} catch(secerr) {
				// System.out.println(LogIdent+"SecLogout konnte nicht ohne Fehler durchgef\u00fchrt werden. Fehler: " +secerr);
			};
			
			throw ("Konto: " +err);
		};
		//*********************************************************************************
		
			
		//*******************************************************************************
		// Kontoauszug
		//*******************************************************************************
		// System.out.println(LogIdent+"Rufe Umsatz\u00fcbersicht auf und starte Abruf des Kontoauszuges ...");
		try {
			var ResponseDataArray = HibiscusScripting_MoneYou_getTransData(iban, webClient);
			var ResponseData = ResponseDataArray[0];
			var Amount = ResponseDataArray[1];
		
		} catch(err) {
			try {
				HibiscusScripting_MoneYou_SecLogout("Login", webClient);
			} catch(secerr) {
				// System.out.println(LogIdent+"SecLogout konnte nicht ohne Fehler durchgef\u00fchrt werden. Fehler: " +secerr);
			};
			
				throw ("Kontoauszug fehlerhaft: " +err);
			
		};
		
		// Überprüfen ob es sich womöglich nicht um einen richtigen Kontoauszug handelt
		if (ResponseData.indexOf("Keine (neuen) Ums\u00e4tze vorhanden") == -1) {
			
			// Überprüfen ob es sich womöglich nicht um einen richtigen Kontoauszug handelt
			if (ResponseData.contains("<html") || ResponseData.contains("<head")) {
				try {
					HibiscusScripting_MoneYou_SecLogout("Login", webClient);
				} catch(secerr) {
					// System.out.println(LogIdent+"SecLogout konnte nicht ohne Fehler durchgef\u00fchrt werden. Fehler: " +secerr);
				};

				throw ("Kontoauszug abholen fehlgeschlagen! Beinhaltet falsche Daten. Bitte neu versuchen oder \u00fcberpr\u00fcfen Sie dies mit einem manuellen Download auf der Bank-Homepage");
			};
		};
		//******************************************************************************


		
		
		// System.out.println(LogIdent+"Kontoauszug erfolgreich. Importiere Daten ...");

		
		
		//*********************************************************************************
		// Kontoauszug verarbeiten: Funktionsaufruf für die Datenverarbeitung
		//*********************************************************************************
		try {
			var kontoauszug = HibiscusScripting_MoneYou_syncDataAndAccount(ResponseData, Amount, iban);
			
		} catch(err) {
			try {
				HibiscusScripting_MoneYou_SecLogout("Login", webClient);
			} catch(secerr) {
				// System.out.println(LogIdent+"SecLogout konnte nicht ohne Fehler durchgef\u00fchrt werden. Fehler: " +secerr);
			};
			
				throw ("Umsatzverarbeitung: " +err);
		};
		//*********************************************************************************
		
		
		
		
		// Logout von der Homepage
		// System.out.println(LogIdent+"MoneYou-Logout ...");
		try {
			HibiscusScripting_MoneYou_SecLogout("Logout", webClient);
		} catch(secerr) {
			// System.out.println(LogIdent+"SecLogout konnte nicht ohne Fehler durchgef\u00fchrt werden. Fehler: " +secerr);
		};
		
		
		if (makeCodepageInfo == true) {
			// System.out.println(LogIdent+"ACHTUNG: Ihre Java-Umgebung verwendet einen falschen Zeichensatz '" + JavaSysCodePage + "' (Richtig w\u00e4re '" + toHaveCodepage + "'). Dadurch k\u00f6nnen Umsatzdoppler entstehen!");
		};


				
	} catch(err) {
		throw (err);
		
	} finally {
		webClient = null;
	};

	return kontoauszug;
};





function HibiscusScripting_MoneYou_prepareClient() {
/*******************************************************************************
 * Bereitet den WebClient von HTMLUnit vor, richtet diesen ein
 *******************************************************************************/
    
    try {
		// neuen WebClient initalisieren ...
		webClient = new WebClient(BrowserVersion.FIREFOX_24);	// hier könnte man auch den verwendeten Browser einstellen

		//*********************************************************************************
		// Bereich für die Einstellungen des WebClient, insbesondere ob JavaScript genutzt wird
		//*********************************************************************************		
		webClient.getOptions().setUseInsecureSSL(false);				// hier könnte man einstellen dass auch Verbindungen akzeptiert werden bei denen das Zertifikat nicht gültig ist, also nicht zu empfehlen
		webClient.getOptions().setRedirectEnabled(true);				// legt fest dass wenn vom Server ein Umleitungs-Befehl kommt, dass dieser gefolgt wird

		webClient.getOptions().setJavaScriptEnabled(true);				// sollte JavaScript auf der Seite benötigt werden, kann man dies hier einschalten, vorher aber mal mit ausgeschaltetem JavaScript im Browser (z. B. Opera) testen, denn nicht alle brauchen dies und dann ist der Abruf wesentlich schneller
		webClient.getOptions().setThrowExceptionOnScriptError(false); 			// bei eingeschaltetem Javascript notwendig, um Fehlermeldungen zu reduzieren (obwohl dies nicht gut funktioniert, deswegen das deaktiviern des Loggin im Script)
		//webClient.setAjaxController(new NicelyResynchronizingAjaxController()); 	// sollten z. B. gewisse JavaScript-Meldungen (z. B. Fehlermeldung "Passwort falsch" bei Ikano-Bank) im Quellcode nicht angezeigt werden

		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);		// ignoriert z. B. 404-Meldungen um Fehlermeldungen zu reduzieren

		webClient.getOptions().setCssEnabled(false);					// schaltet das Verarbeiten von CSS aus, dieses wird hier meist nicht benötigt, reduziert Fehlermeldungen im Log und arbeitet schneller
		webClient.getOptions().setPrintContentOnFailingStatusCode(true);
		//*********************************************************************************		

		// System.out.println(LogIdent+"Verbindung vorbereitet");
		/***************************************************************************************/	
		
	} catch(err) {
		throw err;
	};
	
};





function HibiscusScripting_MoneYou_HttpsLogin(ResponseLogin, ResponsePasswort, webClient) {
/*******************************************************************************
 * Login MoneYou, liefert die Ergebnisseite oder dessen Fehlermeldung zurück
 *******************************************************************************/

	if (String(ResponsePasswort).length > 12) {
		// System.out.println("  Das MoneYou-Passwort darf nicht mehr als 12-stellig sein  ");
		return;
	};
	
		
	
	// Variable für spezifische Error-Meldungen
	var InputError = 0;

	
	
	try {
		//*********************************************************************************
		// Login-Seite aufrufen, liefert die Ergebnisseite oder dessen Fehlermeldung zurück
		//*********************************************************************************
		// System.out.println(LogIdent+"MoneYou-Login aufrufen ... (GET " +HibiscusScripting_MoneYou_LoginURL+")");
		System.out.println(LogIdent+"Login aufrufen");
		try {		
			var pageLogin = webClient.getPage(HibiscusScripting_MoneYou_LoginURL);
			// System.out.println(LogIdent+"HibiscusScripting_MoneYou_HttpsLogin: pageLogin: " +pageLogin);
			
		} catch(err) {
			InputError = 2;
			throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
		};
		if (!pageLogin) { throw "Die Login-Seite konnte nicht aufgerufen werden!"; };
		var pageLoginXML = pageLogin.asXml();
		//// System.out.println(LogIdent+"HibiscusScripting_MoneYou_HttpsLogin: pageLoginXML: \n" +pageLoginXML); // gibt die ganze Seite aus, also ganz schön viel
		
		// Es wird noch der Response auf Fehlernachrichten überprüft
		try {
			HibiscusScripting_MoneYou_checkResponse(pageLoginXML, pageLogin);
			
		} catch(err) {
			InputError = 2;
			throw "Fehlermeldung der Bank: " +err;
		};
		//*********************************************************************************
		
		
		
		//*********************************************************************************
		// Setzen des Formulars für den Login und Werte eintragen
		//*********************************************************************************			
		try {
			var formLogin = pageLogin.getFormByName("loginForm");
			// System.out.println(LogIdent+"HibiscusScripting_MoneYou_HttpsLogin: formLogin: " +formLogin);
			formLogin.getInputByName("j_username_pwd").setValueAttribute(ResponseLogin);
			formLogin.getInputByName("j_password_pwd").setValueAttribute(ResponsePasswort);
			var submitLogin = pageLogin.getElementById("btnNext");
			
		} catch(err) {
			throw "Fehler beim setzen des Login-Formulars oder der Felder (siehe Log - Bitte den Entwickler im Forum informieren)\nLog-Eintrag: " +err;

		};
		//*********************************************************************************
		

		
		//*********************************************************************************
		// Login abschicken, liefert die Ergebnisseite oder dessen Fehlermeldung zurück
		//*********************************************************************************
		// System.out.println(LogIdent+"Login-Form wird abgesendet ...");
		try {
			var PostLoginPage = submitLogin.click();
			// System.out.println(LogIdent+"HibiscusScripting_MoneYou_HttpsLogin: PostLoginPage: " +PostLoginPage);
			
		} catch(err) {
			InputError = 2;
			throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
		};
		if (!PostLoginPage) { throw "Die Login-Folgeseite konnte nicht aufgerufen werden!"; };
		var PostLoginXML = PostLoginPage.asXml();
		//// System.out.println(LogIdent+"HibiscusScripting_MoneYou_HttpsLogin: PostLoginXML: \n" +PostLoginXML); // gibt die ganze Seite aus, also ganz schön viel
		
		// Es wird noch der Response auf Fehlernachrichten überprüft
		try {
			HibiscusScripting_MoneYou_checkResponse(PostLoginXML, PostLoginPage);
			
		} catch(err) {
			if (err.equals("MoneYouAccountUnlocked")) {
				// passt anscheinend, dann machen wir mal weiter
				return;
				
			} else {
				InputError = 2;
				throw "Fehlermeldung der Bank: " +err;
			};
		};
		//*********************************************************************************


		//*********************************************************************************
		// Jetzt überprüfen wir mal noch ob das Kennwort gändert werden muss
		//*********************************************************************************
		if (PostLoginXML.contains("MoneYou findet den Schutz Ihrer Daten wichtig") && PostLoginXML.contains("Ihr Kennwort muss aus")) {
			throw ("Passwort muss geaendert werden");
		};
		//*********************************************************************************



		//*********************************************************************************
		// Check zur Sicherheit ob der Login denn funktioniert hat oder der Login noch angezeigt wird
		//*********************************************************************************
		if (PostLoginXML.contains('action="/thc/policyenforcer/pages/loginB2C.jsf')) {
			InputError = 2;
			throw "Die Loginseite wird trotz keinem bekannten Fehler noch immer angezeigt. Informieren Sie bitte den Script-Entwickler";
		};
		//*********************************************************************************

		return PostLoginPage;
		
	} catch(err) {

		if (InputError == 2) {
			throw err;
			
		} else {
			throw "Fehlermeldung von Jameica: " +err;
		
		};
	};

};





function HibiscusScripting_MoneYou_SecLogout(func,  webClient) {
/*******************************************************************************
 * Logout MoneYou: wird auch zur Sicherheit bei auftreten eines Fehler ausgeführt
 *******************************************************************************/

	if (func != "Logout") {
		// System.out.println(LogIdent + "f\u00fcr die Sicherheit wird noch der Logout durchgef\u00fchrt und der Passwortspeicher zur\u00fcckgesetzt ...");
	};
	
	if (func != "noLogin") {

		var page = webClient.getCurrentWindow().getEnclosedPage();

		var LogoutURL = page.getAnchorByText("Ausloggen"); // HibiscusScripting_MoneYou_LogoutURL
		// System.out.println(LogIdent+"LogoutURL: " +LogoutURL);
		
		try {
			var PostLogoutPage = LogoutURL.click();
		
		} catch(err) {
			throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
		};
		// System.out.println(LogIdent+"PostLogoutPage: " +PostLogoutPage);
		//// System.out.println(LogIdent+"PostLogoutPageXML: \n" +PostLogoutPage.asXml()); //gibt die ganze Logout-Seite im Log aus

	};
	
	// alle Fenster schließen
	webClient.closeAllWindows();
	
	// sensible Daten löschen
	MoneYou_Benutzer = "";
	MoneYou_Wallet_Alias = "";

	// und noch wichtig: ein paar Variablen zurück setzen!
	MoneYou_fetchSaldo = "";
	MoneYou_fetchUmsatz = "";

};





function HibiscusScripting_MoneYou_getAccInfo(iban, webClient) {
/*******************************************************************************
 * Ruft die Kontoübersicht oder die Detailansicht für das ausgewählte Konto auf,
 * und liest dort Details auf um diese ins Kommentarfeld zu schreiben
 *******************************************************************************/

	System.out.println(LogIdent+"Abholen der Kontoinformationen");
	var page = webClient.getCurrentWindow().getEnclosedPage();


	// Vorbereiten und prüfen ob Detailinformationen überhaupt abgerufen werden sollten
	//*********************************************************************************
	// Übersicht aufrufen, liefert die Ergebnisseite oder dessen Fehlermeldung zurück
	//*********************************************************************************
	var AccountsListURL = page.getAnchorByText("Mein Tagesgeld");
	// System.out.println(LogIdent+"Click " +AccountsListURL);
	try {		
		var DataPage = AccountsListURL.click();
		// System.out.println(LogIdent+"HibiscusScripting_MoneYou_getAccInfo: AccountsListURL DataPage (Seite): " +DataPage);
		
	} catch(err) {
		InputError = 2;
		throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
	};
	if (!DataPage) { throw "Die Tagesgeld-Konten\u00fcbersicht konnte nicht aufgerufen werden!"; };
	var DataPageXML = DataPage.asXml();	
	//// System.out.println(LogIdent+"HibiscusScripting_MoneYou_getAccInfo: DataPageXML (Seite): \n" +DataPageXML); // gibt die ganze Seite aus, also ganz schön viel
	var AccountDetailResponse = DataPage.getWebResponse().getContentAsString();;	
	// System.out.println(LogIdent+"HibiscusScripting_MoneYou_getAccInfo: AccountDetailResponse (Seite): \n" +AccountDetailResponse); // gibt die ganze Seite aus, also ganz schön viel		
	
	// Es wird noch der Response auf Fehlernachrichten überprüft
	try {
		HibiscusScripting_MoneYou_checkResponse(DataPageXML, DataPage);
		
	} catch(err) {
		InputError = 2;
		throw "Fehlermeldung der Bank: " +err;
	};
	//*********************************************************************************

	
	// Die Texte auslesen und formatieren
	var IBANsub = iban.substring(0,4) + " " + iban.substring(4,8) + " " + iban.substring(8,10);
	var DataStart = AccountDetailResponse.indexOf(IBANsub); // Ermittle die Position (Index) des Anfangs der Daten

	// nun den aktuellen Zinsatz auslesen
	var DataIDX = AccountDetailResponse.indexOf('interestRate', DataStart); // Ermittle die Position (Index) des Anfangs der Daten
	var DataIDX = AccountDetailResponse.indexOf('value', DataIDX); // Ermittle die Position (Index) des Anfangs der Daten
	// System.out.println(LogIdent+"DataIDX: " +DataIDX);
	var DataInterestPaIDXstart = AccountDetailResponse.indexOf('>', DataIDX)+1; // Ermittle die Position (Index) des Anfangs der Daten
	// System.out.println(LogIdent+"DataInterestPaIDXstart: " +DataInterestPaIDXstart);
	var DataInterestPaIDXend = AccountDetailResponse.indexOf("<", DataInterestPaIDXstart); // Ermittle die Position (Index) des Endes der Daten
	// System.out.println(LogIdent+"DataInterestPaIDXend: " +DataInterestPaIDXend);
	var DataInterestPaText = AccountDetailResponse.substring(DataInterestPaIDXstart, DataInterestPaIDXend); // Hole den String vom Index1 bis Index2
	//// System.out.println(LogIdent+"DataInterestPaText: " +DataInterestPaText);

	// nun die Kontoart auslesen
	var DataIDX = AccountDetailResponse.indexOf('bankProductInList', DataInterestPaIDXend); // Ermittle die Position (Index) des Anfangs der Daten
	var DataIDX = AccountDetailResponse.indexOf('value', DataIDX); // Ermittle die Position (Index) des Anfangs der Daten
	// System.out.println(LogIdent+"DataIDX: " +DataIDX);
	var DataAccountArtIDXstart = AccountDetailResponse.indexOf('>', DataIDX)+1; // Ermittle die Position (Index) des Anfangs der Daten
	// System.out.println(LogIdent+"DataAccountArtIDXstart: " +DataAccountArtIDXstart);
	var DataAccountArtIDXend = AccountDetailResponse.indexOf("<", DataAccountArtIDXstart); // Ermittle die Position (Index) des Endes der Daten
	// System.out.println(LogIdent+"DataAccountArtIDXend: " +DataAccountArtIDXend);
	var DataAccountArtText = AccountDetailResponse.substring(DataAccountArtIDXstart, DataAccountArtIDXend); // Hole den String vom Index1 bis Index2
	//// System.out.println(LogIdent+"DataAccountArtText: " +DataAccountArtText);

	// nun die errechneten Zinsen bis heute
	var DataIDX = AccountDetailResponse.indexOf('accruedInterestInList', DataAccountArtIDXend); // Ermittle die Position (Index) des Anfangs der Daten
	var DataIDX = AccountDetailResponse.indexOf('value', DataIDX); // Ermittle die Position (Index) des Anfangs der Daten
	// System.out.println(LogIdent+"DataIDX: " +DataIDX);
	var DataInterestIDXstart = AccountDetailResponse.indexOf('>', DataIDX)+1; // Ermittle die Position (Index) des Anfangs der Daten
	// System.out.println(LogIdent+"DataInterestIDXstart: " +DataInterestIDXstart);
	var DataInterestIDXend = AccountDetailResponse.indexOf("<", DataInterestIDXstart); // Ermittle die Position (Index) des Endes der Daten
	// System.out.println(LogIdent+"DataInterestIDXend: " +DataInterestIDXend);
	var DataInterestText = AccountDetailResponse.substring(DataInterestIDXstart, DataInterestIDXend); // Hole den String vom Index1 bis Index2
	//// System.out.println(LogIdent+"DataInterestText: " +DataInterestText);
	
	// nun noch alles formatieren
	var AccArt = DataAccountArtText.split(" ").join("").split("\r").join("").split("\n").join("").split("\t").join("");
	// System.out.println(LogIdent+"Kontodetails-Informationen (Kontoart): " +DataAccountArtText);
	var InterestPa = DataInterestPaText.split("  ").join("").split("\r").join("").split("\n").join("").split("\t").join("");
	// System.out.println(LogIdent+"Kontodetails-Informationen (Zinssatz in % p.a.): " +DataInterestPaText);
	var Interest = DataInterestText.split("  ").join("").split("\r").join("").split("\n").join("").split("\t").join("");
	// System.out.println(LogIdent+"Kontodetails-Informationen (Errechnete Zinsen bis heute): " +Interest);
	
	// Speichern der Informationen im Konto
//	 System.out.println("Aufgelaufene Zinsen: " + Interest + "  \n\n"
//					 + "Kontodetail-Informationen der Bank:\n"
//					 + "-----------------------------------\n"
//					 + "Kontoart:\t\t\t\t" + AccArt + "\n"
//					 + "Zinssatz in % p.a.:\t\t" + InterestPa);

	return DataPage;
		
};



function HibiscusScripting_MoneYou_getTransData(iban, webClient) {
/*******************************************************************************
 * Ruft Umsatzsuche für das ausgewählte Konto auf, setzt die Abrufperiode
 * und liefert den Export zurück
 *******************************************************************************/

	// Variable für spezifische Error-Meldungen
	var InputError = 0;
	
	
	var page = webClient.getCurrentWindow().getEnclosedPage();


	try {
		//*********************************************************************************
		// Übersicht aufrufen, liefert die Ergebnisseite oder dessen Fehlermeldung zurück
		//*********************************************************************************
		var AccountsListURL = page.getAnchorByText("Buchungen");
		// System.out.println(LogIdent+"GET " +AccountsListURL);
		try {		
			var DataPage = AccountsListURL.click();
			// System.out.println(LogIdent+"HibiscusScripting_MoneYou_getTransData: AccountsListURL DataPage (Seite): " +DataPage);
			
		} catch(err) {
			InputError = 2;
			throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
		};
		if (!DataPage) { throw "Die Umsatz\u00fcbersicht konnte nicht aufgerufen werden!"; };
		var DataPageXML = DataPage.asXml();	
		//// System.out.println(LogIdent+"HibiscusScripting_MoneYou_getTransData: DataPageXML (Seite): \n" +DataPageXML); // gibt die ganze Seite aus, also ganz schön viel
		
		// Es wird noch der Response auf Fehlernachrichten überprüft
		try {
			HibiscusScripting_MoneYou_checkResponse(DataPageXML, DataPage);
			
		} catch(err) {
			InputError = 2;
			throw "Fehlermeldung der Bank: " +err;
		};
		//*********************************************************************************

		//*********************************************************************************
		// Setzen des Formulars für die erweiterte Ansicht
		//*********************************************************************************			
		try {
			var form = DataPage.getFormByName("AccountingMovementListForm");			
			// System.out.println(LogIdent+"HibiscusScripting_MoneYou_getTransData: form: " +form);
			var Anzeigen = form.getInputByName("btnNext");
			
		} catch(err) {
			InputError = 2;
			throw "Fehler beim setzen des Formulars zum CSV-Abruf (siehe Log - Bitte den Entwickler im Forum informieren)\nLog-Eintrag: " +err;
		};	
		//*********************************************************************************			

		//*********************************************************************************
		// Übersicht erweitern, liefert die Ergebnisseite oder dessen Fehlermeldung zurück
		//*********************************************************************************
		// System.out.println(LogIdent+"GET " +Anzeigen);
		try {		
			var DataPage = Anzeigen.click();
			// System.out.println(LogIdent+"HibiscusScripting_MoneYou_getTransData: Anzeigen DataPage (Seite): " +DataPage);
			
		} catch(err) {
			InputError = 2;
			throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
		};
		if (!DataPage) { throw "Die Umsatz\u00fcbersicht konnte nicht erweitert werden!"; };
		var DataPageXML = DataPage.asXml();
		//// System.out.println(LogIdent+"HibiscusScripting_MoneYou_getTransData: DataPageXML (Seite): \n" +DataPageXML); // gibt die ganze Seite aus, also ganz schön viel
		
		// Es wird noch der Response auf Fehlernachrichten überprüft
		try {
			HibiscusScripting_MoneYou_checkResponse(DataPageXML, DataPage);
			
		} catch(err) {
			InputError = 2;
			throw "Fehlermeldung der Bank: " +err;
		};
		//*********************************************************************************
		
		//*********************************************************************************
		// Setzen des Formulars für die Buchungen herunterladen
		//*********************************************************************************			
		try {
			var form = DataPage.getFormByName("AccountingMovementForm");			
			// System.out.println(LogIdent+"HibiscusScripting_MoneYou_getTransData: form: " +form);
			var Buchungen = form.getInputByName("downloadStatementsLinkButton");
			
		} catch(err) {
			InputError = 2;
			throw "Fehler beim setzen des Formulars zum CSV-Abruf (siehe Log - Bitte den Entwickler im Forum informieren)\nLog-Eintrag: " +err;
		};	
		//*********************************************************************************			

		//*********************************************************************************
		// Übersicht erweitern, liefert die Ergebnisseite oder dessen Fehlermeldung zurück
		//*********************************************************************************
		// System.out.println(LogIdent+"GET " +Buchungen);
		try {		
			var DataPage = Buchungen.click();
			// System.out.println(LogIdent+"HibiscusScripting_MoneYou_getTransData: Buchungen DataPage (Seite): " +DataPage);
			
		} catch(err) {
			InputError = 2;
			throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
		};
		if (!DataPage) { throw "Die Umsatz-Downloadseite konnte nicht aufgerufen werden!"; };
		var DataPageXML = DataPage.asXml();
		//// System.out.println(LogIdent+"HibiscusScripting_MoneYou_getTransData: DataPageXML (Seite): \n" +DataPageXML); // gibt die ganze Seite aus, also ganz schön viel
		
		// Es wird noch der Response auf Fehlernachrichten überprüft
		try {
			HibiscusScripting_MoneYou_checkResponse(DataPageXML, DataPage);
			
		} catch(err) {
			InputError = 2;
			throw "Fehlermeldung der Bank: " +err;
		};
		//*********************************************************************************
		

		
		//*********************************************************************************
		// Setzen des Formulars für die Kontoauswahl
		//*********************************************************************************			
		try {
			var form = DataPage.getFormByName("DownloadMovementForm");
			// System.out.println(LogIdent+"HibiscusScripting_MoneYou_getTransData: form: " +form);
			var kontoselect = form.getSelectByName("accountNumber");
			// System.out.println(LogIdent+"HibiscusScripting_MoneYou_getTransData: kontoselect: " +kontoselect);
			
		} catch(err) {
			InputError = 2;
			throw "Fehler beim setzen der Kontoauswahl in der Umsatz\u00fcbersicht (siehe Log - Bitte den Entwickler im Forum informieren)\nLog-Eintrag: " +err;
		};	
		//*********************************************************************************			

		//*********************************************************************************
		// Auswahl treffen für das Konto in der Umsatzübersicht
		//*********************************************************************************			
		// die notwendige Kontonummer
		var kontonr = String(iban.substr(4));
		var selectKonto = false;

		// Konto auswählen ...
		var list = kontoselect.getOptions();
		for (var i = 0; i < list.size(); i++) {
			var selectOption = list.get(i);
			//// System.out.println(LogIdent+"Listeneintrag der Kontoauswahl gefunden: " +selectOption);
			//// System.out.println(LogIdent+"dieser Eintrag als reiner Text: " +String(selectOption.asText()));
			// System.out.println("=========================> " + selectOption.getValueAttribute());
			if (selectOption.getValueAttribute().contains(kontonr)) {
				//// System.out.println(LogIdent+"Kontoauswahl auf "+selectOption);
				selectOption.setSelected(true);
				selectKonto = true;
				var SelectedOptionText = selectOption.asText();
			};
		};
		
		// wenn die Variable selectKonto nicht true wurde, war die Kontonummer nicht in der Auswahl dabei
		if (selectKonto != true) {
			InputError = 2;
			throw ("Konto "+kontonr+" in der Auswahl nicht gefunden! Kontrollieren Sie bitte Ihre Angaben im Konto");
		};
		//*********************************************************************************
		
		
		
		//*********************************************************************************
		// Auslesen des Saldo des gewählten Kontos
		//*********************************************************************************
		var AccountInfos = String(SelectedOptionText).split(" ");
		//// System.out.println(LogIdent+"Die Kontoauswahl aufgeteilt nach Leerzeichen: "+AccountInfos);
		var SaldoFound = false;

		for (var i = 0; i < AccountInfos.length; i++) {
			if (AccountInfos[i].indexOf(",") != -1) {
				//// System.out.println(LogIdent+"Saldo gefunden: "+AccountInfos[i]);
				SaldoFound = true;
				var Amount = parseFloat(AccountInfos[i].replace(/\./g, "").replace(/,/, "."));
			};
		};

		// Prüfen ob der Saldo ausgelesen werden konnte
		if (SaldoFound == false) {
			throw ("Der Saldo konnte nicht aus dem selektiertem Konto ausgelesen werden (Bitte den Entwickler im Forum informieren)");
		};
		//*********************************************************************************



		//*********************************************************************************
		// Ermitteln der Zeitspanne für den Datenabruf
		//*********************************************************************************
		var DateNow  = new java.util.Date();         	// Aktuelles Datum
		var fromDate = new java.util.Date((DateNow.getTime()-31104000000)); // 1 Jahr ist z. B. 31536000000; 12 Wochen sind z. B. 7257600000
		
		// Tag und Monat muss 2-stellig sein
		var fromDateDay = String (fromDate.getDate()); fromDateDay = ((fromDateDay < 10) ? "0" + fromDateDay : fromDateDay);
		var fromDateMonth = String ((fromDate.getMonth() + 1)); fromDateMonth = ((fromDateMonth < 10) ? "0" + fromDateMonth : fromDateMonth);
		var DateNowDay  = String (DateNow.getDate()); DateNowDay = ((DateNowDay < 10) ? "0" + DateNowDay : DateNowDay);
		var DateNowMonth  = String ((DateNow.getMonth() + 1)); DateNowMonth = ((DateNowMonth < 10) ? "0" + DateNowMonth : DateNowMonth);

		System.out.println(LogIdent+"Rufe Ums\u00e4tze vom "+fromDateDay+"."+fromDateMonth+"."+(fromDate.getYear()+1900)+" bis "+DateNowDay+"."+DateNowMonth+"."+(DateNow.getYear()+1900)+" ab ...");
		//*********************************************************************************			



		//*********************************************************************************
		// Setzen der Werte für den Abrufzeitraum und des Suchen-Buttons
		//*********************************************************************************			
		try {
			form.getInputByName("minDate").setValueAttribute(fromDateDay+"."+fromDateMonth+"."+(fromDate.getYear()+1900));
			form.getInputByName("maxDate").setValueAttribute(DateNowDay+"."+DateNowMonth+"."+(DateNow.getYear()+1900));
			var SearchSubmit = form.getInputByName("btnNext");
			
		} catch(err) {
			InputError = 2;
			throw "Fehler beim setzen der Abrufzeitraum-Werte oder des Suchen-Button (siehe Log - Bitte den Entwickler im Forum informieren)\nLog-Eintrag: " +err;
			
		};
		//*********************************************************************************

		
		
		//*********************************************************************************
		// Suche abschicken, liefert die Ergebnisseite oder dessen Fehlermeldung zurück
		//*********************************************************************************
		try {
			var DataPage = SearchSubmit.click();
			// System.out.println(LogIdent+"HibiscusScripting_MoneYou_getTransData: DataPage (Suche): " +DataPage);
			
		} catch(err) {
			InputError = 2;
			throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
		};
		if (!DataPage) { throw "Die Suche-Folgeseite konnte nicht aufgerufen werden!"; };
		var DataPageXML = DataPage.asXml();
		//// System.out.println(LogIdent+"HibiscusScripting_MoneYou_getTransData: DataPageXML (Suche): \n" +DataPageXML); // gibt die ganze Seite aus, also ganz schön viel
		
		// Es wird noch der Response auf Fehlernachrichten überprüft
		try {
			HibiscusScripting_MoneYou_checkResponse(DataPageXML, DataPage);
			
		} catch(err) {
			InputError = 2;
			throw "Fehlermeldung der Bank: " +err;
		};
		//*********************************************************************************


		//*********************************************************************************
		// Prüfen ob in den gewählten Zeitraum überhaupt Buchungen vorhanden sind
		//*********************************************************************************			
		var DataPageResponse = DataPage.getWebResponse().getContentAsString();
		//// System.out.println(LogIdent+"HibiscusScripting_MoneYou_getTransData: DataPageResponse (Suche): \n" +DataPageResponse); // gibt die ganze Seite aus, also ganz schön viel

		if (DataPageResponse.contains('totalMovements">0<')) {
			var csvResponse = String("Keine (neuen) Ums\u00e4tze vorhanden (da drei Platzhalter im Download-Formular)");
			
		} else {
		
			//*********************************************************************************
			// Setzen des Formulars für den CSV-Abruf
			//*********************************************************************************			
			try {
				var form = DataPage.getFormByName("DownloadMovementForm");			
				// System.out.println(LogIdent+"HibiscusScripting_MoneYou_getTransData: form: " +form);
				var CSVButton = form.getInputByValue("herunterladen");
				
			} catch(err) {
				InputError = 2;
				throw "Fehler beim setzen des Formulars zum CSV-Abruf (siehe Log - Bitte den Entwickler im Forum informieren)\nLog-Eintrag: " +err;
			};	
			//*********************************************************************************			

			//*********************************************************************************
			// CSV-Export holen, liefert die Ergebnisseite oder dessen Fehlermeldung zurück
			//*********************************************************************************
			// System.out.println(LogIdent+"CSV laden: Click "+CSVButton);
			try {		
				csv = CSVButton.click();
				
			} catch(err) {
				InputError = 2;
				throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
			};
			if (!csv) { throw "Der Kontoauszug (CSV) konnte nicht aufgerufen werden!"; };
			// System.out.println(LogIdent+"HibiscusScripting_MoneYou_getTransData: csv: " +csv);
			var csvResponse = csv.getWebResponse().getContentAsString();
			//// System.out.println(LogIdent+"HibiscusScripting_MoneYou_getTransData: csvResponse: \n" +csvResponse); // gibt die ganze Seite aus, also ganz schön viel
			
			// Es wird noch der Response auf Fehlernachrichten überprüft
			try {
				HibiscusScripting_MoneYou_checkResponse(csvResponse, csv);
				
			} catch(err) {
				InputError = 2;
				throw "Fehlermeldung der Bank: " +err;
			};
			//*********************************************************************************
		
		};

		
		// Seite für den späteren LogOut setzen
		var page = webClient.getCurrentWindow().setEnclosedPage(DataPage);

		
		var DataResponse = new Array();
		DataResponse[0] = csvResponse;
		DataResponse[1] = Amount;


		return DataResponse;
		
	} catch(err) {

		if (InputError == 2) {
			throw err;
			
		} else {
			throw "Fehlermeldung von Jameica: " +err;
		
		};
	};

};





function HibiscusScripting_MoneYou_syncDataAndAccount(data, amount, iban) {
/*******************************************************************************
 * Erzeugen von Umsätzen aus dem Kontoauszug, Prüfung und Speicherung
 *******************************************************************************/

	// System.out.println(LogIdent+"Funktion HibiscusScripting_MoneYou_syncDataAndAccount wurde aufgerufen ...");
	
	
	// CSV-Ergebnisliste in ein Array umwandeln
	var CSVDataRow = HibiscusScripting_MoneYou_Data2Array(data, ";");
	//// System.out.println(LogIdent+"HibiscusScripting_MoneYou_syncDataAndAccount Ergebnis:\n" +CSVDataRow);

	// Variablen für Checks der Umsatzverarbeitung
	var saldo = amount;
	var UmsatzAnzahl = 0;
	var NewUmsatzCount = 0;
	var OldUmsatzCount = 0;
	var SaldoInfo = false;
	var newSaldo = false;
	var givenSaldoGet = false;
	var givenKontoSaldo = 0;
	var LastUmsatzDatum = new java.util.Date((1931 - 1900),0,1); // Variable setzen für den Fall dass noch keine Umsätze vorhanden sind
	var MetaIdentifier = 1;

	if (Java8upFrame == true) {
		var BalanceDaily = Java.type("org.laladev.moneyjinn.hbci.core.entity.BalanceDaily");
		var AccountMovement = Java.type("org.laladev.moneyjinn.hbci.core.entity.AccountMovement");
		var AccountData = Java.type("org.laladev.moneyjinn.hbci.batch.entity.AccountData");
		var BigDecimal = Java.type("java.math.BigDecimal");
		var Long = Java.type("java.lang.Long");
		var Integer = Java.type("java.lang.Integer");
	};

	//*******************************************************************************
	// Auslesen des Kontoauszug-Saldos (wird mindestens von einem der beiden Fälle benötigt)
	//*******************************************************************************
	var givenSaldo = Math.round(saldo * 100) / 100;
	// System.out.println(LogIdent+"Saldo der Homepage: " + givenSaldo);
	givenSaldoGet = true;
	//*******************************************************************************
	

	//*******************************************************************************
	// Verarbeiten der Umsätze aus dem Kontoauszug
	//*******************************************************************************

		// Auslesen aller Buchungen und zu einem sauberen Array zusammenbauen dass nur fertig formatierte Umsätze enhält für die spätere Verarbeitung und Nutzung
		var TransactionData = new Array();
		var ArrayCount = 0;
		var anfangsSaldo = new BigDecimal(saldo.toString());
		// System.out.println("==============> " + anfangsSaldo);
		for (var i = 0; i < CSVDataRow.length; i++) {
			
			// Prüfen ob die Zeile leer ist dann weiter mit der nächsten
			if ((CSVDataRow[i] == undefined) || (CSVDataRow[i][0] == undefined) || (!CSVDataRow[i][0])) { continue; };
			
			// Buchungen fangen mit einer laufenden Nummer an:
			if (isFinite(CSVDataRow[i][0])) {
				
				TransactionData[ArrayCount] = new Array();
				
				/////// Buchungsdatum [0] ///////
				var arraydatum = CSVDataRow[i][1].split(".");
				TransactionData[ArrayCount][0] = new java.sql.Date((parseInt(arraydatum[2],10) - 1900),(parseInt(arraydatum[1],10) - 1),parseInt(arraydatum[0],10));
				// // System.out.println(LogIdent+"TransactionData["+ArrayCount+"][0] hat folgenden Wert: (Datum): "+TransactionData[ArrayCount][0]);

				/////// Valuta-Datum [1] ///////
				var arrayvaluta = CSVDataRow[i][5].split(".");
				TransactionData[ArrayCount][1] = new java.sql.Date((parseInt(arrayvaluta[2],10) - 1900),(parseInt(arrayvaluta[1],10) - 1),parseInt(arrayvaluta[0],10));
				//// System.out.println(LogIdent+"TransactionData["+ArrayCount+"][1] hat folgenden Wert: (Valuta): "+TransactionData[ArrayCount][1]);

				/////// Betrag [2] ///////
				TransactionData[ArrayCount][2] = new BigDecimal(parseFloat(CSVDataRow[i][3].replace(/\./g,"").replace(/\,/, ".")).toString());
				//// System.out.println(LogIdent+"TransactionData["+ArrayCount+"][2] hat folgenden Wert: (Betrag): "+TransactionData[ArrayCount][2]);
				anfangsSaldo = anfangsSaldo.subtract(TransactionData[ArrayCount][2]);
				/////// Saldo [3] ///////
				TransactionData[ArrayCount][3] = 0; // noch nicht bekannt, muss erst noch berechnet werden
				//// System.out.println(LogIdent+"TransactionData["+ArrayCount+"][3] hat folgenden Wert: (Saldo): "+TransactionData[ArrayCount][3]);

				/////// Umsatzart [4] ///////
				TransactionData[ArrayCount][4] = CSVDataRow[i][2];
				//// System.out.println(LogIdent+"TransactionData["+ArrayCount+"][4] hat folgenden Wert: (Art): "+TransactionData[ArrayCount][4]);

				/////// Verwendungszweck [5] ///////
				// Verwendungstext-String zusammenbauen
				var PurpRawString = String(CSVDataRow[i][8]) + " " + String(CSVDataRow[i][9]);
				var PurpString = PurpRawString; //.split("-").join("\u00c4").split("_").join("\u00dc").split("+").join("\u00d6");
				TransactionData[ArrayCount][5] = HibiscusScripting_MoneYou_removeWhitespace(PurpString);
				if(TransactionData[ArrayCount][5].length == 0) {
					TransactionData[ArrayCount][5]=TransactionData[ArrayCount][4];
				}
				//// System.out.println(LogIdent+"TransactionData["+ArrayCount+"][5] hat folgenden Wert: (Zweck): "+TransactionData[ArrayCount][5]);

				/////// Primanotakennzeichen [6] ///////
				TransactionData[ArrayCount][6] = CSVDataRow[i][10];
				//// System.out.println(LogIdent+"TransactionData["+ArrayCount+"][6] hat folgenden Wert: (Primanota): "+TransactionData[ArrayCount][6]);

				/////// GegenkontoName [7], GegenkontoNummer [8], GegenkontoBLZ [9] ///////
				TransactionData[ArrayCount][7] = CSVDataRow[i][7]; //.split("-").join("\u00c4").split("_").join("\u00dc").split("+").join("\u00d6");
				if (new java.lang.String(CSVDataRow[i][6]).contains(" ")) {
					GegenkontoArray = CSVDataRow[i][6].split(" ");
					TransactionData[ArrayCount][8] = GegenkontoArray[1];
					TransactionData[ArrayCount][9] = GegenkontoArray[0];
					
				};/* else if (new java.lang.String(CSVDataRow[i][6]).contains(" ")) { 
					var IBAN = CSVDataRow[i][6].split(" ").join("");
					//// System.out.println(LogIdent+"CSVDataRow["+i+"][6] hat folgenden Wert ohne Leerzeichen: (IBAN): "+IBAN);
					TransactionData[ArrayCount][8] = IBAN.substring(12,22);
					TransactionData[ArrayCount][9] = IBAN.substring(4,12);
				};*/
				//// System.out.println(LogIdent+"TransactionData["+ArrayCount+"][7] hat folgenden Wert: (GegenkontoName): "+TransactionData[ArrayCount][7]);
				//// System.out.println(LogIdent+"TransactionData["+ArrayCount+"][8] hat folgenden Wert: (GegenkontoNummer): "+TransactionData[ArrayCount][8]);
				//// System.out.println(LogIdent+"TransactionData["+ArrayCount+"][9] hat folgenden Wert: (GegenkontoBLZ): "+TransactionData[ArrayCount][9]);

				/////// Waehrung [10] ///////
				TransactionData[ArrayCount][10] = CSVDataRow[i][4];
				//// System.out.println(LogIdent+"TransactionData["+ArrayCount+"][10] hat folgenden Wert: (Notiz): "+TransactionData[ArrayCount][10]);

				/////// Zeilennummer [11] ///////
				TransactionData[ArrayCount][11] = i + 1;
				//// System.out.println(LogIdent+"TransactionData["+ArrayCount+"][11] hat folgenden Wert: (Zeilennummer): "+TransactionData[ArrayCount][11]);

				// ausgeben was hier für ein Buchungs-Array zusammengesetzt wurde
				// // System.out.println(LogIdent+"TransactionData["+ArrayCount+"] hat folgende Werte: "+TransactionData[ArrayCount]);
				
				// Den Zähler für das nächste Buchungs-Array hochsetzen
				ArrayCount = ArrayCount +1;
			};
		};

		// ausgeben der vollständigen Liste aller Buchungen in sortierter Reihenfolge
		//// System.out.println(LogIdent+"TransactionData hat folgende Werte: "+TransactionData);
		// System.out.println(LogIdent+"Liste aller abgeholten Ums\u00E4tze: (schon fertig formatiert und sortiert, bereit zur Verarbeitung) [falls nicht auskommentiert]");
		for (var i = 0; i < TransactionData.length; i++) {
			// // System.out.println(LogIdent+"\n"+TransactionData[i][0]+"\t"+TransactionData[i][1]+"\t"+TransactionData[i][2]+"\t"+TransactionData[i][3]+"\t"+TransactionData[i][4]+"\t"+TransactionData[i][5]+"\t"+TransactionData[i][6]+"\t"+TransactionData[i][7]+"\t"+TransactionData[i][8]+"\t"+TransactionData[i][9]+"\t"+TransactionData[i][10]);
		};
		
		
		var zwischensaldo = anfangsSaldo;
		
		var umsatzList = new java.util.ArrayList(); 
		var lastTransactionTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
		var currency = "EUR";

		for (var i = 0; i < TransactionData.length; i++) {

			if (Java8upFrame == true) {
				var umsatz = new AccountMovement();
			} else {
				var umsatz = AccountMovement;
			};
			
			umsatz.setCreationTime(new java.sql.Timestamp(java.lang.System.currentTimeMillis()));

			umsatz.setMyAccountnumber(Long.valueOf(iban.substr(13,9)));
			umsatz.setMyBankcode(Integer.valueOf(iban.substr(4,9)));
			umsatz.setMyIban(iban);
			umsatz.setMyBic("FTSBDEFAMYO");


			umsatz.setBookingDate(TransactionData[i][0]); // Datum
			lastTransactionTimestamp = new java.sql.Timestamp(TransactionData[i][0].getTime());
			umsatz.setValueDate(TransactionData[i][1]); // Valuta
			umsatz.setMovementValue(TransactionData[i][2]); // Betrag
			umsatz.setMovementCurrency(TransactionData[i][10]);

			zwischensaldo = zwischensaldo.add(TransactionData[i][2]);
			umsatz.setBalanceValue(zwischensaldo); // Saldo
			umsatz.setBalanceDate(TransactionData[i][0]); // Datum
			umsatz.setBalanceCurrency(TransactionData[i][10]);
			currency = TransactionData[i][10];
			
			umsatz.setMovementTypeText(TransactionData[i][4]); // Umsatzart
			umsatz.setMovementReason(TransactionData[i][5]); // Verwendungszweck
			if (TransactionData[i][6]) { umsatz.setPrimaNota(TransactionData[i][6]); }; // Primanota
			if (TransactionData[i][7]) { umsatz.setOtherName(TransactionData[i][7]); }; // GegenkontoName
			if (TransactionData[i][8]) { umsatz.setOtherAccountnumber(TransactionData[i][8]); }; // GegenkontoNummer
			if (TransactionData[i][9]) { umsatz.setOtherBankcode(TransactionData[i][9]); }; // GegenkontoBLZ
			
			// not null columns
			umsatz.setCancellation(false);
			umsatz.setCustomerReference("");
			umsatz.setMovementTypeCode(0);
			
//			System.out.println(umsatz);
			umsatzList.add(umsatz);
			
		};
		
		var result = new AccountData();
		var balance = new BalanceDaily();
		
		balance.setMyAccountnumber(Long.valueOf(iban.substr(13,9)));
		balance.setMyBankcode(Integer.valueOf(iban.substr(4,9)));
		balance.setMyIban(iban);
		balance.setMyBic("FTSBDEFAMYO");
		balance.setBalanceDate(new java.sql.Date(System.currentTimeMillis()));
		balance.setLastTransactionDate(lastTransactionTimestamp);
		balance.setBalanceAvailableValue(new BigDecimal(saldo.toString()));
		balance.setBalanceCurrency(currency);
		balance.setLineOfCreditValue(new BigDecimal(0));

		result.setAccountMovements(umsatzList);
		result.setBalanceDaily(balance);

		System.out.println(LogIdent+"Fertig!");
		
		return result;
	//*******************************************************************************
};


function HibiscusScripting_MoneYou_removeWhitespace(item) {
/*******************************************************************************
 * Überzählige Leerzeichen am Anfang, Ende oder auch innerhalb einer Zeichenkette
 * entfernen (Quelle: http://www.javascriptsource.com/forms/remove_xs_whitespace.html)
 *******************************************************************************/

	//// System.out.println(LogIdent+"Entwickler-Info: Funktion _removeWhitespace wurde aufgerufen ...");

	var puffer = "";
	var item_length = item.length;
	var item_length_minus_1 = item.length - 1;
	
	for (var index = 0; index < item_length; index++) {
	
		if (item.charAt(index) != ' ') {
			puffer += item.charAt(index);
		
		} else {
			if (puffer.length > 0) {
				if (item.charAt(index+1) != ' ' && index != item_length_minus_1) {
					puffer += item.charAt(index);
				};
			};
		};
	};
	
	return puffer;

};

function HibiscusScripting_MoneYou_Data2Array(strData, strDelimiter) {
/*******************************************************************************
 * Parsen eines (CSV) Strings mit JavaScript über einen Regular Expression Befehl
 * (Quelle: http://www.bennadel.com/blog/1504-Ask-Ben-Parsing-CSV-Strings-With-JavaScript-Exec-Regular-Expression-Command.htm)
 *******************************************************************************/

	var strDelimiter = (strDelimiter || ",");
	var objPattern = new RegExp(("(\\" + strDelimiter + "|\\r?\\n|\\r|^)" 
				   + "(?:\"([^\"]*(?:\"\"[^\"]*)*)\"|" 
				   + "([^\"\\" + strDelimiter + "\\r\\n]*))")
				   , "gi");
	
	var arrData = [[]];
	var arrMatches = null;
	
	while(arrMatches = objPattern.exec(strData)) {
		var strMatchedDelimiter = arrMatches[1];

		if (strMatchedDelimiter.length && (strMatchedDelimiter != strDelimiter)) {
			arrData.push([]);
		};
	
		if (arrMatches[2]) {
			var strMatchedValue = arrMatches[2].replace(new RegExp("\"\"", "g" ), "\"");
		
		} else {
			var strMatchedValue = arrMatches[3];
		};
		
		arrData[arrData.length - 1].push(strMatchedValue);
	};
	
	return(arrData);
	
};

function HibiscusScripting_MoneYou_checkResponse(ResponseContent, ResponseForm) {
/*******************************************************************************
 * Prüfen ob der Response bekannte Fehlermarker oder Nachrichten enthält
 *******************************************************************************/
		
		//// System.out.println(LogIdent+"Funktion checkResponse wurde aufgerufen mit dem Content: " +ResponseContent); // sollte die ganze HTML-Seite ausgeben also sehr viel
		
		if (ResponseContent.contains("Online-Sperre aufheben")) {
			throw "Account gesperrt";
		} else if (ResponseContent.contains('class="msgs error"')) {
			ErrorResponse = ResponseForm.getWebResponse().getContentAsString();
			//// System.out.println(LogIdent+"Funktion checkResponse erstellt ErrorResponse: " +ErrorResponse); // sollte die ganze HTML-Seite ausgeben also sehr viel

			var ErrorMessage = HibiscusScripting_MoneYou_formErrorMessage(ErrorResponse);
		
			// Hier wird nun also die perfekt formatierte Fehlernachricht in einem Infofenster ausgegeben
			Application.getCallback().notifyUser(MoneYouErrorTitle  + ErrorMessage + "\n\n");

			// Für die Monitor-Log Ausgabe werden noch alle Zeilenumbrüche entfernt
			ErrorMessage = ErrorMessage.split("\n").join(" ").split("\r").join(" ");
			
			throw ErrorMessage;
			
		} else if (ResponseContent.contains("wurden automatisch vom System abgemeldet")) {
			throw "Die Sitzung wurde von der Bank beendet. Bitte melden Sie sich erneut an";
		
		} else {
			// System.out.println("alles OK")
			return;
			
		};
		
};

function HibiscusScripting_MoneYou_formErrorMessage(MessagePage) {
/*******************************************************************************
 * ermittelt in der Seite den ErrorText und bereitet diesen auf
 *******************************************************************************/

		/* Beispiel aus der MoneYou-Webseite 
			
			Der Quelltext:
			
			... <input type="hidden" name="loginForm" value="loginForm" />
			<ul id="loginMessage" class="msgs error" id="loginMessage">
			<li>	Login-Daten werden nicht erkannt. Bitte klicken Sie den Link "Kennwort vergessen?". </li></ul>
			<p id="j_username_pwd_line"><label for="j_username_pwd">Nutzer-ID:</label> ...

			wir wie folgt ausgelesen oder verwertet ...
		*/


		var ErrorIDXstart = MessagePage.indexOf('class="msgs error"'); // Ermittle die Position (Index) des Anfangs der Information
		// System.out.println(LogIdent+"ErrorIDXstart: " +ErrorIDXstart);
		var ErrorTextIDXStart = MessagePage.indexOf("<li>", ErrorIDXstart)+4; // Ermittle die Position (Index) des Anfangs des Textes
		// System.out.println(LogIdent+"ErrorTextIDXStart: " +ErrorTextIDXStart);
		var ErrorTextIDXend = MessagePage.indexOf("</li>", ErrorTextIDXStart); // Ermittle die Position (Index) des Endes des Textes
		// System.out.println(LogIdent+"ErrorTextIDXend: " +ErrorTextIDXend);
		var ErrorText = MessagePage.substring(ErrorTextIDXStart, ErrorTextIDXend); // Hole den String vom Index1 bis Index2
		// System.out.println(LogIdent+"ErrorText (unformatiert): " +ErrorText);

		// Um die Info-Nachricht schön mit einem Infofenster ausgeben zu können wird nun der String nun formatiert und Code entfernt
		var formErrorText = ErrorText.split("<br/> ").join("\n")
					     .split("<br/ >").join("\n")
					     .split("<br>").join("\n")
					     .split("\t").join("")
					     .split("  ").join("");
		formErrorText = HibiscusScripting_MoneYou_removeWhitespace(formErrorText);
		
		// System.out.println(LogIdent+"ErrorText (formatiert): " +formErrorText);

		return formErrorText;
		
};