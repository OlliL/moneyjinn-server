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
importPackage(Packages.de.willuhn.util);
importPackage(Packages.de.willuhn.logging);
importPackage(Packages.de.willuhn.jameica.gui);
importPackage(Packages.de.willuhn.jameica.messaging);
importPackage(Packages.de.willuhn.jameica.system);
importPackage(Packages.de.willuhn.jameica.hbci);
importPackage(Packages.de.willuhn.jameica.hbci.rmi);
importPackage(Packages.de.willuhn.jameica.hbci.messaging);
importPackage(Packages.de.willuhn.jameica.hbci.synchronize.jobs);

// Import der Packete aus HTMLUnit für den Webseiten-Abruf
importPackage(Packages.com.gargoylesoftware.htmlunit);
importPackage(Packages.com.gargoylesoftware.htmlunit.html);
importPackage(Packages.com.gargoylesoftware.htmlunit.util);
/*******************************************************************************/



/*******************************************************************************
 * Konfiguration und Cache
 *******************************************************************************/

/****** BENUTZER-BEREICH - DIESER KANN NACH DEN VORGABEN EINGESTELLT WERDEN! *******/ 
// Abrufen und Anzeigen von Meldungen der Bank - (Standart ist: Abruf einmal pro Session)
// Mögliche Optionen:	full (= bei jedem Abruf prüfen)
//			soft (= nur einmal pro Sitzung (Session) von Jameica prüfen)
//			off (= es wird nie nach Meldungen der Bank geprüft)
var MoneYou_CheckNewInfo = "soft";



/****** ENTWICKLER-BEREICH - DIESEN NICHT ÄNDERN! *******/ 
// Versions-Nummer setzen
var MoneYou_Script_Version = "2.1.10";
 
// wichtig für neuen Sync, wir setzten die Variable für die Abfrage
var MoneYou_NewSyncActive = false;

// Diese BLZ muss im Hibiscus-Konto hinterlegt sein
var HibiscusScripting_MoneYou_BLZ = "50324040";
 
// Cache für bekannte Umsätze
var HibiscusScripting_MoneYou_HibiscusUmsaetze = [];

// Cache für Passwort
var MoneYou_Benutzer = "";
var HibiscusScripting_MoneYou_lPass = [];

// Cache für Informationsabruf-Check
var MoneYou_NewInfoChecked = [];

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
var ExcIdent = "";
var ExcIdentMoneYou = "[MoneYou] Fehler: ";

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
// setzten des LogIdent für die Initalisierung
LogIdent = LogIdentMoneYou;
ExcIdent = ExcIdentMoneYou;

// Start der Initalisierung ins Log screiben
Logger.info(LogIdent+"Initalisierung des Scripts in der Version " + MoneYou_Script_Version + " ...");
Logger.debug(LogIdent+"verwendetes Betriebssystem: " + OSArtString);
Logger.debug(LogIdent+"verwendete Version der Java Runtime Umgebung: " + JavaVerString);
Logger.debug(LogIdent+"Herausgeber der Java-Umgebung: " + JavaVenString);
Logger.debug(LogIdent+"Codpage (Zeichencodierung) der Javaumgebung in diesem System: " + JavaSysCodePage);

// Die Registrierung auf das Event "hibiscus.sync.function". Damit kann Hibiscus ermitteln, ob und über welche Javascript-Funktion das Konto synchronisiert werden kann
events.add("hibiscus.sync.function", "HibiscusScripting_MoneYou_sync_function");
// Registrierung auf Konto-Sync-Event
events.add("hibiscus.konto.sync", "HibiscusScripting_MoneYou_kontoSync");

// Wir schauen bei der Initialisierung, ob es ein Konto gibt, was MoneYou (ABN AMRO Bank) fähig ist
if (!HibiscusScripting_MoneYou_isAccountDefined()) {
/*******************************************************************************
 * Kontoanlage Assistent ver. 1.3.1
 *******************************************************************************/

	// Falls nicht, bieten wir dem Benutzer an eines zu erzeugen
	Logger.info(LogIdent+"Kein MoneYou-Konto gefunden, Nachfrage ob Anlage erw\u00FCnscht");
	
	var AssiInfoString = new java.lang.String("\n\n\nDas MoneYou-PlugIn f\u00fcr Hibiscus ist installiert ...\n\nZur Zeit ist noch kein Konto f\u00fcr die MoneYou (ABN AMRO Bank) in Hibiscus angelegt\n"
						+ "Wollen Sie jetzt den Assistenten zur automatischen Kontoanlage ausf\u00fchren?\n\n"
						+ "[ INFO: Um diesen Assistenten erneut zu starten, rufen Sie einfach die Jameica-Einstellungen auf\n"
						+ "und klicken auf 'Speichern' ]\n"
						+ "(vorausgesetzt Sie aktivieren nicht den folgenden Haken und w\u00e4hlen zus\u00e4tzlich 'Nein'!)");
								 
	if (Java8upFrame == true) {
		var infoArray = java.lang.reflect.Array.newInstance(java.lang.Class.forName("java.lang.String"), 1);
	} else {
		var infoArray = java.lang.reflect.Array.newInstance(java.lang.String, 1);
	};
	infoArray[0] = AssiInfoString;
	// Abfragefenster beim Benutzer mit Info-Text, einstellbar dass dieses nicht mehr erscheint
	var StartCreateAccount = Application.getCallback().askUser("MoneYou (ABN AMRO Bank) - Kontoanlage Assistent {0}", infoArray);
	
	
	
	if (StartCreateAccount == true) {
		HibiscusScripting_MoneYou_CreateAccountAssistent();
		
	} else {
		Logger.info(LogIdent+"Anlegen des Kontos wurde vom Benutzer mit NEIN beantwortet oder der Assistent ist deaktiviert");
	};	

} else {

  	try { 
		if (Java8upFrame == true) {
			var db = Application.getServiceFactory().lookup(java.lang.Class.forName("de.willuhn.jameica.hbci.HBCI"),"database");
		} else {
			var db = Application.getServiceFactory().lookup(HBCI,"database");
		};
		
	} catch(err) {
		db = false;
	};

	if (db != false) {
		Logger.info(LogIdent+"OK: Mindestens ein Offline-Konto f\u00fcr die MoneYou (ABN AMRO Bank) ist bereits angelegt");

		var AssiInfoNewString = new java.lang.String("\n\n\nZur Zeit ist schon mindestens ein Konto f\u00fcr die MoneYou (ABN AMRO Bank) in Hibiscus angelegt\n"
							   + "Wollen Sie jetzt den Assistenten zur automatischen Kontoanlage ausf\u00fchren\n"
							   + "um ein zus\u00e4tzliches Konto f\u00fcr die MoneYou (ABN AMRO Bank) anzulegen?\n\n"
							   + "[ INFO: Um diesen Assistenten erneut zu starten, rufen Sie einfach die Jameica-Einstellungen auf\n"
							   + "und klicken auf 'Speichern' ]\n"
							   + "(vorausgesetzt Sie aktivieren nicht den folgenden Haken und w\u00e4hlen zus\u00e4tzlich 'Nein'!)\n\n"
							   + "Soll diese Frage zum Anlegen eines zus\u00e4tzlichen Kontos nicht mehr erscheinen,\n"
							   + "aktivieren Sie einfach den folgenden Haken und w\u00e4hlen 'Nein'\n"
							   + "(Um diesen Assisten wieder verf\u00fcbar zu machen folgen Sie der Anleitung von Hibiscus-Scripting)");
									 
		if (Java8upFrame == true) {
			var infoNewArray = java.lang.reflect.Array.newInstance(java.lang.Class.forName("java.lang.String"), 1);
		} else {
			var infoNewArray = java.lang.reflect.Array.newInstance(java.lang.String, 1);
		};
		infoNewArray[0] = AssiInfoNewString;
		// Abfragefenster beim Benutzer mit Info-Text, einstellbar dass dieses nicht mehr erscheint
		var StartCreateNewAccount = Application.getCallback().askUser("MoneYou (ABN AMRO Bank) - Kontoanlage Assistent (Zusatzkonto) {0}", infoNewArray);

			
		if (StartCreateNewAccount == true) {
			HibiscusScripting_MoneYou_CreateAccountAssistent();
			
		} else {
			Logger.info(LogIdent+"Anlegen eines zus\u00e4tzlichen Kontos wurde vom Benutzer mit NEIN beantwortet oder der Assistent ist deaktiviert");
		};		
	};
	
};
/*******************************************************************************/



// Ende der Initalisierung ins Log schreiben
Logger.info(LogIdent+"Initalisierung des Scripts beendet");
/*******************************************************************************/





function HibiscusScripting_MoneYou_CreateAccountAssistent() {
/*******************************************************************************
 * Bentutzerführung des Kontoanlage-Assistenten für ein MoneYou-Konto
 *******************************************************************************/

	try {
		// die notwendigen Variablen für das Anlegen des Kontos
		var AccNumber = "";
		var LoginUsername = "";
		var CustomerFullName = "";
		/***# var CreditCardNr = "";	// wird bei diesem Konto eine Kreditkarte verarbeitet brauchen wir diese Variable #***/
		var CancelMsg = "Kontoanlage ";
	  
		// hier wird nach der Kontonummer gefragt, falls nicht notwendig einfach löschen, ABER DANN AUCH ALLE WEITEREN VARIABLEN ACCNUMBER UND KONTONUMMER Z.B MANUELL MIT 00000000 SETZEN LASSEN
		do {
			var AccNumberSet = false;
			CancelMsg = "Eingabe der Kontonummer ";
			AccNumber = Application.getCallback().askUser("Bitte geben Sie Ihre Kontonummer ein\n\n"
								    + "(diese finden Sie z. B Online als IBAN,\n"
								    + "ohne Zusatz DE50324040, also die letzten 10 Stellen)\n\n"
								    , "Kontonummer:");
			if ((isFinite(AccNumber)) && (AccNumber.length() > 5)) { AccNumberSet = true; };
			      
		} while(AccNumberSet == false); // wenn die Kontonummer nicht aus reinen Zahlen besteht und weniger als fünf Zeichen hat kommt die Abfrage immer wieder
		
		do {
			CancelMsg = "Eingabe der Anmeldekennung ";
			LoginUsername = Application.getCallback().askUser("Bitte geben Sie Ihre Nutzer-ID der MoneYou (ABN AMRO Bank) ein\n\n", "Nutzer-ID:");
			      
		} while(LoginUsername.length() <= 3);
		
		/***# Als weiteres Beispiel könnte als Loginbenutzer eine E-Mail-Adresse benötigt werden, dann die do-while-Schleife hier darüber löschen
		// hier wird eine E-Mail-Adresse abgefragt und nach einem logischem Aufbau geprüft
		do {
			var eMailString = false;
			CancelMsg = "Eingabe der E-Mail-Adresse ";
			LoginUsername = Application.getCallback().askUser("Bitte geben Sie Ihre MoneYou-Login E-Mail-Adresse ein\n\n"
								       , "eMail-Adresse:");
			
			// Es wird geprüft ob die E-Mail-Adresse einem logischem Aufbau entspricht
			if ((LoginUsername.indexOf("@") != -1) && (LoginUsername.lastIndexOf('.') > 4) && (LoginUsername.indexOf(" ") == -1)) {
				eMailString = true;
			
			} else {
				Application.getCallback().notifyUser("\nGeben Sie bitte eine g\u00fcltige E-Mail-Adresse ein  \n\n");
			};
			
		} while(eMailString == false); // wenn die E-Mail-Adresse keinen logischen Aufbau hat kommt die Abfrage immer wieder
		#***/
		
		
		do {
			CancelMsg = "Eingabe des Namens ";
			CustomerFullName = Application.getCallback().askUser("Bitte geben Sie den Namen des Kontoinhabers ein\n" 
									   + "(Vorname Nachname)\n\n"
									   , "Kontoinhaber:");
			      
		} while(CustomerFullName.length() <= 3); // wenn die der Name weniger als drei Zeichen hat kommt die Abfrage immer wieder
		
		/***# //wird bei diesem Konto eine Kreditkarte verarbeitet brauchen wir diesen Abschnitt 
		do {
			var CardNumberSet = false;
			CancelMsg = "Eingabe der Kreditkartennumer ";
			CreditCardNr = Application.getCallback().askUser("Bitte geben Sie Ihre 16-stellige MoneYou (ABN AMRO Bank) Kreditkartennummer ein\n\n", "Kartennummer:");
			if ((isFinite(CreditCardNr)) && (CreditCardNr.length() == 16)) { CardNumberSet = true; };
			      
		} while(CardNumberSet == false); // wenn die Kreditkartennummer nicht aus reinen Zahlen besteht und weniger nicht genau 16 Zeichen hat kommt die Abfrage immer wieder
		#***/
		
		
	} catch(err) {
		Logger.warn(LogIdent + CancelMsg + "wurde vom Benuzter abgebrochen");
		// die letzte Variable leeren um keine falsche Fehlermeldung zu setzten
		CustomerFullName = "";
	
	} finally {	
		try {
			if (AccNumber == "" || CustomerFullName == "" || LoginUsername == ""/***#  || CreditCardNr == "" // wird bei diesem Konto eine Kreditkarte verarbeitet brauchen wir diese Variable #***/) {
				Logger.warn(LogIdent+"Kontodaten nicht vollst\u00E4ndig. Anlegen des Kontos wurde abgebrochen");
				Application.getCallback().notifyUser("Es wurde kein MoneYou-Konto erstellt\n\n"
								   + "Die Kontodaten sind nicht vollst\u00E4ndig!\n"
								   + "("+CancelMsg+"wurde vom Benutzer abgebrochen)\n\n"
								   + "[ INFO: Um diesen Assistenten erneut zu starten, rufen Sie einfach die Jameica-Einstellungen auf\n"
								   + "und klicken auf 'Speichern' ]\n");
								   
			} else {
				// Hibiscus-Konto wird nun angelegt
				HibiscusScripting_MoneYou_createHibiscusAccount(AccNumber, CustomerFullName, LoginUsername/***# ,CreditCardNr // wird bei diesem Konto eine Kreditkarte verarbeitet brauchen wir diese Variable #***/);
				Logger.info(LogIdent+"Hibiscus-Offline-Konto f\u00fcr die MoneYou wurde automatisch erzeugt");
				Application.getCallback().notifyUser("\nHibiscus-Offline-Konto die f\u00fcr MoneYou (ABN AMRO Bank) wurde automatisch erzeugt  \n\n"
								   + "F\u00FCr ein weiteres Konto f\u00fcr die MoneYou (ABN AMRO Bank), rufen Sie einfach die Jameica-Einstellungen auf\n"
								   + "und klicken auf 'Speichern'\n"
								   + "oder lesen Sie bitte die Anleitung unter FAQ auf der Projektseite von Hibiscus-Scripting\n");
			};
			
		} catch(err) {
			Logger.error(LogIdent+"Fehler beim automatischen anlegen des Kontos: Die Kontonummer ist ung\u00FCltig!\nLog-Meldung: " + err);
			Application.getCallback().notifyUser("Fehler beim automatischen anlegen des Kontos\n\n" 
							   + "Die eingegebene Kontonummer ist ung\u00FCltig!\n\n"
							   + "[ INFO: Um diesen Assistenten erneut zu starten, rufen Sie einfach die Jameica-Einstellungen auf\n"
							   + "und klicken auf 'Speichern' ]\n");

		};
	};
	
};





function HibiscusScripting_MoneYou_isAccountDefined() {
/*******************************************************************************
 * Schaut nach, ob bereits ein Konto für den MoneYou-Abruf existiert
 *******************************************************************************/

	try {
		if (Java8upFrame == true) {
			var db = Application.getServiceFactory().lookup(java.lang.Class.forName("de.willuhn.jameica.hbci.HBCI"),"database");
		} else {
			var db = Application.getServiceFactory().lookup(HBCI,"database");
		};
		
	} catch(e) {
		// Beim Start von Jameica steht zum Init-Zeitpunkt noch kein DB-Zugriff zur Verfügung
		Logger.debug(LogIdent+"Noch kein Datenbankzugriff f\u00fcr MoneYou-PlugIn m\u00F6glich. Check auf existierendes MoneYou-Konto wird abgebrochen (siehe Log)\nLog: "+e);
		return true;
	};
	
	if (Java8upFrame == true) {
		var list = db.createList(java.lang.Class.forName("de.willuhn.jameica.hbci.rmi.Konto"));
	} else {
		var list = db.createList(Konto);
	};

	while(list.hasNext()) {
		var konto = list.next();
		
		if (konto.getBLZ() == HibiscusScripting_MoneYou_BLZ) { return true; }; // Ein Offline-Konto für diese Bank existiert bereits
	};

	return false;

};





function HibiscusScripting_MoneYou_createHibiscusAccount(AccNumber, CustomerFullName, LoginUsername /***# ,CreditCardNr // wird bei diesem Konto eine Kreditkarte verarbeitet brauchen wir diese Variable #***/) {
/*******************************************************************************
 * Erzeugen eines Offline-Kontos mit den Einstellungen für die MoneYou (ABN AMRO Bank)
 *******************************************************************************/
	
	if (Java8upFrame == true) {
		var db = Application.getServiceFactory().lookup(java.lang.Class.forName("de.willuhn.jameica.hbci.HBCI"),"database");
		var konto = db.createObject(java.lang.Class.forName("de.willuhn.jameica.hbci.rmi.Konto"), null);

	} else {
		var db = Application.getServiceFactory().lookup(HBCI,"database");
		var konto = db.createObject(Konto, null);
	};

	konto.setBezeichnung("MoneYou - Tagesgeld");
	konto.setKundennummer(LoginUsername);          		// die Kundennummer oder Loginname
	konto.setKontonummer(AccNumber);           		// Ist Konto-Nummer die der Benuter eingegeben hat
	konto.setBLZ(HibiscusScripting_MoneYou_BLZ); 		// BLZ der 'MoneYou'
	/***# konto.setUnterkonto(CreditCardNr);		// Kreditkartennummer des Benutzers #***/
	konto.setKommentar("Automatisch erzeugtes Konto \n\n(dieser Infotext und die Bezeichnung des Kontos k\u00f6nnen nat\u00fcrlich ge\u00e4ndert werden)");
	konto.setName(CustomerFullName);			// Ist der vollständige Name den der Benutzer eingegeben hat
	konto.setFlags(Konto.FLAG_OFFLINE);              	// Muss ein Offline-Konto sein

	konto.store();

	// Keine automatischen Gegenbuchungen
	var sync = new SynchronizeOptions(konto);
	sync.setSyncOffline(false);
	
};





function HibiscusScripting_MoneYou_sync_function(konto, type) {
/*******************************************************************************
 * Sie wird aufgerufen, wenn Hibiscus herauszufinden versucht, ob das Script
 * den angegebenen Geschäftsvorfall für das Konto unterstützt. Falls ja,
 * muss das Script den Namen der Javascript-Funktion zurückliefern, die
 * ausgeführt werden soll, um diesen Geschäftsvorfall auszuführen.
 * Andernfalls NULL.
 * @param konto das betreffende Konto, für welches der Support geprüft werden soll.
 * @param type Name der Klasse des gesuchten Jobs.
 * @return der Name der auszuführenden Javascript-Funktion oder NULL.
 *******************************************************************************/

	// Sync-Aufruf wird hier ignoriert und beendet wenn die BLZ vom Script nicht zum Konto gehört
	// (muss immer an erster Stelle der Sync-Funktion stehen noch vor allen anderen Anweisungen oder Code!)
	if (	(konto.getBLZ() != HibiscusScripting_MoneYou_BLZ)
	     || ((konto.getKundennummer().length() < 6) && (konto.getKundennummer().length() > 20)) // so gegen Festgeld eigentlich nicht mehr effektiv
	     || ((String(konto.getUnterkonto()) == "Festgeld"))
	   ) { return; };



	LogIdent = LogIdentMoneYou;
	ExcIdent = ExcIdentMoneYou;
  


	// Liste aller möglichen Klassen (Gross-Kleinschreibung beachten!):
	// - SynchronizeJobKontoauszug
	// - SynchronizeJobDauerauftragDelete
	// - SynchronizeJobDauerauftragList
	// - SynchronizeJobDauerauftragStore
	// - SynchronizeJobLastschrift
	// - SynchronizeJobSammelLastschrift
	// - SynchronizeJobSammelUeberweisung
	// - SynchronizeJobSepaUeberweisung
	// - SynchronizeJobUeberweisung

	// ACHTUNG: Hibiscus unterstützt beim Scripting-Support derzeit
	// nur "SynchronizeJobKontoauszug". Die anderen könnten in
	// Zukunft jedoch noch folgen. Irgendwann wird also prinzipiell
	// jeder Geschäftsvorfall auch via Scripting möglich sein.

	// Wir liefern hier zurück, dass wir nur den Abruf von Kontoauszügen
	// unterstützen.
	if (Java8upFrame == true) {
		if (type.equals(java.lang.Class.forName("de.willuhn.jameica.hbci.synchronize.jobs.SynchronizeJobKontoauszug"))) { return "HibiscusScripting_MoneYou_Kontoauszug"; };
	} else {
		if (type.equals(SynchronizeJobKontoauszug)) { return "HibiscusScripting_MoneYou_Kontoauszug"; };
	};

	return null;
	
};





function HibiscusScripting_MoneYou_Kontoauszug(job, session) {
/*******************************************************************************
 * MoneYou neuer Sync Kontoauszug (registrierte Hauptfunktion die den Sync startet)
 *******************************************************************************/

	var konto = job.getKonto();
	var monitor = session.getProgressMonitor();

	// Speicher für die Sychronisierungsoptionen die in diesem Konto gesetzt sind
	var options = new SynchronizeOptions(konto);

	var forceSaldo  = job.getContext(SynchronizeJobKontoauszug.CTX_FORCE_SALDO);
	Logger.debug(LogIdent+"HibiscusScripting_MoneYou_Kontoauszug: forceSaldo: " + forceSaldo);
	var forceUmsatz = job.getContext(SynchronizeJobKontoauszug.CTX_FORCE_UMSATZ);
	Logger.debug(LogIdent+"HibiscusScripting_MoneYou_Kontoauszug: forceUmsatz: " + forceUmsatz);
	
	// Wenn "fetchSaldo" true ist, sollte (wenn möglich) der Saldo des Kontos aktualisiert werden
	MoneYou_fetchSaldo = options.getSyncSaldo() || (forceSaldo != null && forceSaldo.booleanValue());
	// Wenn "fetchUmsatz" true ist, sollten neue Umsätze abgerufen werden
	MoneYou_fetchUmsatz = options.getSyncKontoauszuege() || (forceUmsatz != null && forceUmsatz.booleanValue());
	
	// Auslesen der neuen Option ob der Saldo durch Hibiscus berechnet wird oder nicht
	try {
		MoneYou_autoSaldo = options.getAutoSaldo();
		var AutoSaldoExist = true;
	
	} catch(err) {
		if (options.getSyncSaldo() == true) { MoneYou_autoSaldo = false; }
		else if (options.getSyncSaldo() == false) { MoneYou_autoSaldo = true; };
		var AutoSaldoExist = false;
	};
	

	// Über aktiven neuen Sync im Log informieren
	Logger.info(LogIdent+"Neue Synchronisierung wurde erkannt, mit folgenden Einstellungen: ");
	Logger.info(LogIdent+" - Saldo aktualisieren: " + MoneYou_fetchSaldo);
	Logger.info(LogIdent+" - Kontoausz\u00fcge (Ums\u00e4tze) abrufen: " + MoneYou_fetchUmsatz);
	Logger.info(LogIdent+" - Berechnung des Saldo durch Hibiscus: " + MoneYou_autoSaldo);
	
	// wichtig für neuen Sync, wir setzten die Variable dass dieser aktiv ist
	MoneYou_NewSyncActive = true;
	// und setzen dass für dieses Konto kein Saldo durch Hibiscus berechnet wird
	if ((MoneYou_autoSaldo != false) && (AutoSaldoExist == true)) { 
		options.setAutoSaldo(false);
		Logger.info(LogIdent+"Berechnung des Saldo durch Hibiscus wurde nun ausgeschaltet ist nun also: " + options.getAutoSaldo());
	};
	
	
	// Wir leiten den Aufruf einfach an die existierende Funktion weiter
	try {
		HibiscusScripting_MoneYou_kontoSync(konto, monitor);
		
	} catch(err) {
		MoneYou_fetchSaldo = "";
		MoneYou_fetchUmsatz = "";

		Logger.error(ExcIdent + err);
		return new ApplicationException(ExcIdent + err);	
	};
	
};





function HibiscusScripting_MoneYou_kontoSync(konto, monitor) {
/*******************************************************************************
 * MoneYou (ABN AMRO Bank) Sync (registrierte Hauptfunktion die den Sync startet)
 *******************************************************************************/

	// Sync-Aufruf wird hier ignoriert und beendet wenn die BLZ vom Script nicht zum Konto gehört
	// (muss immer an erster Stelle der Sync-Funktion stehen noch vor allen anderen Anweisungen oder Code!)
	if (	(konto.getBLZ() != HibiscusScripting_MoneYou_BLZ)
	     || ((konto.getKundennummer().length() < 6) && (konto.getKundennummer().length() > 20)) // so gegen Festgeld eigentlich nicht mehr effektiv
	     || ((String(konto.getUnterkonto()) == "Festgeld"))
	   ) { return; };


	
	LogIdent = LogIdentMoneYou;
	ExcIdent = ExcIdentMoneYou;



	monitor.setPercentComplete(0);

	
	
	if (MoneYou_NewSyncActive == false) {
		monitor.log("Synchronisiere Konto: " + konto.getLongName());
	};

	// Ausgabe der Versionsnummer des Scripts, ist oben unter Konfiguration einzustellen
	Logger.info(LogIdent + "Version " + MoneYou_Script_Version + " wurde gestartet ...");
	monitor.log(LogIdent + "Version " + MoneYou_Script_Version + " wurde gestartet ...");
	monitor.log("******************************************************************************************************************\n");

	// hier wird gleich mal geprüft ob nicht beide Optionen deaktiviert sind, dann können wir gleich wieder abbrechen (übernimmt aber auch Jameica)
	if ((MoneYou_NewSyncActive == true) && (MoneYou_fetchSaldo == false) && (MoneYou_fetchUmsatz == false)) {
		Logger.warn(LogIdent+"Neuer Sync wird nicht ausgef\u00fcrt da die Option 'Saldo aktualisieren' und 'Kontoausz\u00fcge (Ums\u00e4tze) abrufen' deaktiviert sind. Nichts zu tun");
		monitor.log("Neuer Sync wird nicht ausgef\u00fcrt da die Option 'Saldo aktualisieren' und 'Kontoausz\u00fcge (Ums\u00e4tze) abrufen' deaktiviert sind. Nichts zu tun");
	};
	
	

	try {
		/*******************************************************************************
		 * Java-Versions- und Codepage-Check ver. 1.2.1
		 * (Prüft ob Java in der richtigen Version verwendet wird, und ob die Codepage stimmt)
		 *******************************************************************************/
		monitor.log("\u00dcberpr\u00FCfe Java-Version und den verwendeten Zeichensatz ...");
		Logger.info(LogIdent+"\u00dcberpr\u00FCfe Java-Version und den verwendeten Zeichensatz ...");
		var minJavaVer = String("1.6"); // hier stellt man die gewünschte Mindestversion der verwendeten Java-Umgebung für Jameica ein (Format 1.6)
		
		if (OSArtString.contains("Mac")) { var toHaveCodepage = String("MacRoman"); }
		else if (OSArtString.contains("Win")) { var toHaveCodepage = String("Cp1252"); }
		else { var toHaveCodepage = String("UTF-8, ISO-8859-15"); };
		var makeCodepageInfo = false;
		
		try {
			var JavaVer = String(java.lang.System.getProperty("java.version")).substring(0,3); // ermittelt die aktuell verwendete Java-Umgebung von Jameica
			
		} catch(err) {
			if (MoneYou_NewSyncActive == false) {
				Logger.error(LogIdent+"Java-Version konnte nicht ermittelt werden. Stellen Sie sicher dass Java mindestens in der Version "+minJavaVer+" installiert ist");
				return new java.lang.Exception("Java-Version konnte nicht ermittelt werden. Stellen Sie sicher dass Java mindestens in der Version "+minJavaVer+" installiert ist");
			
			} else {
				throw ("Java-Version konnte nicht ermittelt werden. Stellen Sie sicher dass Java mindestens in der Version "+minJavaVer+" installiert ist");
			};
		};
		
		Logger.debug(LogIdent+"minJavaVer: " + minJavaVer);
		Logger.debug(LogIdent+"JavaVer: " + JavaVer);
		Logger.debug(LogIdent+"toHaveCodepage: " + toHaveCodepage);
		Logger.debug(LogIdent+"JavaSysCodePage: " + JavaSysCodePage);
		
		// die Werte werden zum geteilt da Nachkommastellen von JavaScript rein matematisch genutzt und somit gekürzt werden (x.10 wird zu x.1)
		var minJavaArray = minJavaVer.split(".");
		Logger.debug(LogIdent+"minJavaArray[0]: " + minJavaArray[0] + " / in parseFloat: " +parseFloat(minJavaArray[0]) + "    und    minJavaArray[1]: " + minJavaArray[1] + " / in parseFloat: " +parseFloat(minJavaArray[1]));
		var JavaVerArray = JavaVer.split(".");
		Logger.debug(LogIdent+"JavaVerArray[0]: " + JavaVerArray[0] + " / in parseFloat: " +parseFloat(JavaVerArray[0]) + "    und    JavaVerArray[1]: " + JavaVerArray[1] + " / in parseFloat: " +parseFloat(JavaVerArray[1]));
		
		// nun starten wir einen logischen Vergleich als Nummern statt String
		if ((parseFloat(JavaVerArray[0]) < parseFloat(minJavaArray[0])) || 
		   ((parseFloat(JavaVerArray[0]) == parseFloat(minJavaArray[0])) && (parseFloat(JavaVerArray[1]) < parseFloat(minJavaArray[1])))) {
			Logger.error(LogIdent+"Java-Version zu niedrig. Mindestes Version "+minJavaVer+" wird ben\u00f6tigt. (aktuell verwendete Umgebung durch Jameica ist "+ JavaVerString +")");
			if (MoneYou_NewSyncActive == false) {
				Logger.error(LogIdent+"Java-Version zu niedrig. Mindestes Version "+minJavaVer+" wird ben\u00f6tigt. (aktuell verwendete Umgebung durch Jameica ist "+ JavaVerString +")");
				return new java.lang.Exception("Java-Version zu niedrig. Mindestes Version "+minJavaVer+" wird ben\u00f6tigt. (aktuell verwendete Umgebung durch Jameica ist "+ JavaVerString +")");
			
			} else {
				throw ("Java-Version zu niedrig. Mindestes Version "+minJavaVer+" wird ben\u00f6tigt. (aktuell verwendete Umgebung durch Jameica ist "+ JavaVerString +")");
			}; 

		} else {
			// Prüfe auf verwendete und somit richtige Codepage
			if ((String(JavaSysCodePage).indexOf(toHaveCodepage) != -1) || (toHaveCodepage.indexOf(String(JavaSysCodePage)) != -1)) {
				monitor.log("OK: Java-Version " + JavaVerString + " von '" + JavaVenString + "' installiert, aktiv und verwendet Zeichensatz '" + JavaSysCodePage + "'");
				Logger.info(LogIdent+"Java-Version " + JavaVerString + " von '" + JavaVenString + "' installiert, aktiv und verwendet Zeichensatz '" + JavaSysCodePage + "'");
			
			} else {
				monitor.log("ACHTUNG: Java-Version " + JavaVerString + " ist zwar OK aber verwendet falschen Zeichensatz '" + JavaSysCodePage + "' (Richtig w\u00e4re '" + toHaveCodepage + "')");
				Logger.warn(LogIdent+"ACHTUNG: Java-Version " + JavaVerString + " ist zwar OK aber verwendet falschen Zeichensatz '" + JavaSysCodePage + "' (Richtig w\u00e4re '" + toHaveCodepage + "')");
				makeCodepageInfo = true;
			};			
			
		};
		/******************************************************************************/



		/*******************************************************************************
		 * HTMLUnit-Versions-Check ver. 1.3.7
		 * (Prüft ob HTMLUnit installiert ist, checkt die Version und gibt diese aus)
		 *******************************************************************************/
		monitor.log("\u00dcberpr\u00FCfe HTMLUnit-Version ...");
		Logger.info(LogIdent+"\u00dcberpr\u00FCfe HTMLUnit-Version ...");
		var minHTMLUnitVer = String("2.13"); // hier stellt man die gewünschte Mindestversion der installierten HTMLUnit ein (Format 2.9 oder 2.10)
		
		try {
			// ermittelt die installierte HTMLUnit-Version
			var HTMLUnitVer = String(com.gargoylesoftware.htmlunit.Version.getProductVersion());
			
		} catch(err) {
			if (MoneYou_NewSyncActive == false) {
				Logger.error(LogIdent+"HTMLUnit-Version konnte nicht ermittelt werden. Stellen Sie sicher dass HTMLUnit mindestens in der Version "+minHTMLUnitVer+" installiert ist");
				return new java.lang.Exception("HTMLUnit-Version konnte nicht ermittelt werden. Stellen Sie sicher dass HTMLUnit mindestens in der Version "+minHTMLUnitVer+" installiert ist");
			
			} else {
				throw ("HTMLUnit-Version konnte nicht ermittelt werden. Stellen Sie sicher dass HTMLUnit mindestens in der Version "+minHTMLUnitVer+" installiert ist");
			};
		};
		
		Logger.debug(LogIdent+"minHTMLUnitVer: " + minHTMLUnitVer);
		Logger.debug(LogIdent+"HTMLUnitVer: " + HTMLUnitVer);
		
		// die Werte werden zum geteilt da Nachkommastellen von JavaScript rein matematisch genutzt und somit gekürzt werden (x.10 wird zu x.1)
		var minHTMLUnitArray = minHTMLUnitVer.split(".");
		Logger.debug(LogIdent+"minHTMLUnitArray[0]: " + minHTMLUnitArray[0] + " / in parseFloat: " +parseFloat(minHTMLUnitArray[0]) + "    und    minHTMLUnitArray[1]: " + minHTMLUnitArray[1] + " / in parseFloat: " +parseFloat(minHTMLUnitArray[1]));
		var HTMLUnitVerArray = HTMLUnitVer.split(".");
		Logger.debug(LogIdent+"HTMLUnitVerArray[0]: " + HTMLUnitVerArray[0] + " / in parseFloat: " +parseFloat(HTMLUnitVerArray[0]) + "    und    HTMLUnitVerArray[1]: " + HTMLUnitVerArray[1] + " / in parseFloat: " +parseFloat(HTMLUnitVerArray[1]));
		
		// nun starten wir einen logischen Vergleich als Nummern statt String
		if ((parseFloat(HTMLUnitVerArray[0]) < parseFloat(minHTMLUnitArray[0])) || 
		   ((parseFloat(HTMLUnitVerArray[0]) == parseFloat(minHTMLUnitArray[0])) && (parseFloat(HTMLUnitVerArray[1]) < parseFloat(minHTMLUnitArray[1])))) {
			Logger.error(LogIdent+"HTMLUnit-Version zu niedrig. Mindestes Version "+minHTMLUnitVer+" wird ben\u00f6tigt. (Ihre Version ist "+ HTMLUnitVer +")");
			if (MoneYou_NewSyncActive == false) {
				Logger.error(LogIdent+"HTMLUnit-Version zu niedrig. Mindestes Version "+minHTMLUnitVer+" wird ben\u00f6tigt. (Ihre Version ist "+ HTMLUnitVer +")");
				return new java.lang.Exception("HTMLUnit-Version zu niedrig. Mindestes Version "+minHTMLUnitVer+" wird ben\u00f6tigt. (Ihre Version ist "+ HTMLUnitVer +")");
			
			} else {
				throw ("HTMLUnit-Version zu niedrig. Mindestes Version "+minHTMLUnitVer+" wird ben\u00f6tigt. (Ihre Version ist "+ HTMLUnitVer +")");
			}; 

		} else {
			monitor.log("OK: HTMLUnit-Version " + HTMLUnitVer + " installiert und aktiv");
			Logger.info(LogIdent+"OK: HTMLUnit-Version " + HTMLUnitVer + " installiert und aktiv");
		};
		/******************************************************************************/
		
		
		
		monitor.setPercentComplete(3);

		
		
		//*******************************************************************************
		// Initialisieren des webClients
		//*******************************************************************************
		try {
			HibiscusScripting_MoneYou_prepareClient(monitor);
			
		} catch(err) {
			Logger.error(LogIdent +err);
			if (MoneYou_NewSyncActive == false) {
				Logger.error(LogIdent+ err);
				return new java.lang.Exception(err);

			} else {
				throw err;
			};
			
		};
		//*******************************************************************************

		
				
		//*******************************************************************************
		// Login, inkl. Passwortabfrage beim User
		//*******************************************************************************
		// Setzen des Login für den aktuellen Abruf
		MoneYou_Benutzer = konto.getKundennummer();
		Hibiscus_Wallet = Packages.de.willuhn.jameica.hbci.Settings.getWallet();
		Hibiscus_cachePins = Packages.de.willuhn.jameica.hbci.Settings.getCachePin();
		Hibiscus_storePins = Packages.de.willuhn.jameica.hbci.Settings.getStorePin();
		Logger.debug(LogIdent+"Einstellung von Hibiscus zum Passwort Zwischenspeichern ist: " +Hibiscus_cachePins);
		Logger.debug(LogIdent+"Einstellung von Hibiscus zu Passwort in Wallet speichern ist: " +Hibiscus_storePins);
		MoneYou_Wallet_Alias = "scripting.MoneYou." + HibiscusScripting_MoneYou_makeHash(konto.getBLZ() + "." + MoneYou_Benutzer);
		

		monitor.log("MoneYou-Login mit " + MoneYou_Benutzer + " ...");

		
		do {
			monitor.setPercentComplete(8);
			
			
			var MoneYou_Passwort = "";
			if (Hibiscus_cachePins == true) { 
				MoneYou_Passwort = HibiscusScripting_MoneYou_lPass[MoneYou_Benutzer]; 
			} else {
				HibiscusScripting_MoneYou_lPass[MoneYou_Benutzer] = null;
			};
			if (Hibiscus_storePins == true) {
				HibiscusScripting_MoneYou_lPass[MoneYou_Benutzer] = Hibiscus_Wallet.get(MoneYou_Wallet_Alias);
				MoneYou_Passwort = HibiscusScripting_MoneYou_lPass[MoneYou_Benutzer]; 
			} else {
				if (Hibiscus_Wallet.get(MoneYou_Wallet_Alias)) { Hibiscus_Wallet.set(MoneYou_Wallet_Alias,null); };
			};
			
			
			try {
				if (!MoneYou_Passwort || (MoneYou_Passwort == "") || (MoneYou_Passwort == null) || (MoneYou_Passwort == "null")) {
					var LogUserString = MoneYou_Benutzer.substring(0, 4) + "*******";
					Logger.info(LogIdent+"MoneYou-Passwort f\u00fcr Anmeldekennung "+LogUserString+" wird abgefragt ...");			

					MoneYou_Passwort = Application.getCallback().askPassword("Bitte geben Sie das 'MoneYou (ABN AMRO Bank)' Passwort\n"
													  + "zum Konto " + konto.getKontonummer() + "\n"
												 	  + "und Anmeldekennung " + MoneYou_Benutzer + "\nein:");
				}; 

			} catch(err) {
				if (MoneYou_NewSyncActive == false) {
					Logger.error(LogIdent+"Login fehlgeschlagen! Passwort-Eingabe vom Benuzter abgebrochen");
					return java.lang.Exception("Login fehlgeschlagen! Passwort-Eingabe vom Benuzter abgebrochen");
				
				} else {
					throw ("Login fehlgeschlagen! Passwort-Eingabe vom Benuzter abgebrochen");
				};
			};


			
			//*********************************************************************************
			// Login: Funktionsaufruf mit Benutzername und Passwort
			//*********************************************************************************
			try {
				var PostLoginPage = HibiscusScripting_MoneYou_HttpsLogin(MoneYou_Benutzer, MoneYou_Passwort, webClient);
				
			} catch(err) {
				try {
					HibiscusScripting_MoneYou_SecLogout("noLogin", monitor, webClient);
				} catch(secerr) {
					Logger.debug(LogIdent+"SecLogout konnte nicht ohne Fehler durchgef\u00fchrt werden. Fehler: " +secerr);
				};
				
				if (MoneYou_NewSyncActive == false) {
					Logger.error(LogIdent+"Login fehlgeschlagen! " +err);
					return java.lang.Exception("Login fehlgeschlagen! " +err);
				
				} else {
					throw ("Login fehlgeschlagen! " +err);
				};
				
			};
			//*********************************************************************************

			
		} while(!PostLoginPage);

		// Der Login sollte funktioniert haben also speichern wir das Passwort wie eingestellt
		if (Hibiscus_cachePins == true) { HibiscusScripting_MoneYou_lPass[MoneYou_Benutzer] = MoneYou_Passwort; };
		if (Hibiscus_storePins == true) { Hibiscus_Wallet.set(MoneYou_Wallet_Alias,MoneYou_Passwort); };
		//******************************************************************************


	  
		monitor.setPercentComplete(15); 

		Logger.info(LogIdent+"MoneYou-Login war erfolgreich");
		monitor.log("Login war erfolgreich");

		
		
		//*********************************************************************************
		// Check ob die Kontoeröffnung abgeschlossen ist
		//*********************************************************************************
		var PostLoginXML = PostLoginPage.asXml();
		//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_AfterLogin PostLoginXML: \n" +PostLoginXML);
		
		if (PostLoginXML.contains('Ihren Antrag') && PostLoginXML.contains('ist erfolgreich abgeschlossen, wenn alle Punkte abgehakt sind')) {
			
			// Anzahl der noch zu erledigenden Punkte zählen
			var openPointsCount = String(PostLoginXML).split("untickbox").length - 1;
			Logger.debug(LogIdent+"HibiscusScripting_MoneYou_AfterLogin: openPointsCount: " +openPointsCount);
			
			// Anzahl der schon erledigten Punkte zählen
			var donePointsCount = String(PostLoginXML).split('alt="tickbox"').length - 1;
			Logger.debug(LogIdent+"HibiscusScripting_MoneYou_AfterLogin: donePointsCount: " +donePointsCount);
			
			// die einzelnen Punkte auslesen
			var openPoints = "";
			var donePoints = "";
			var SearchPos = 0;
			for (var i = 0; i < openPointsCount; i++) {
				var NrIDXstart = PostLoginXML.indexOf("untickbox",SearchPos); // Ermittle die Position (Index) des Anfangs der Information
				var NrIDXstart = PostLoginXML.indexOf(">", NrIDXstart)+1; // Ermittle die Position (Index) des Anfangs der Information
				Logger.debug(LogIdent+"NrIDXstart: " +NrIDXstart);
				var NrIDXend = PostLoginXML.indexOf("</tr>", NrIDXstart); // Ermittle die Position (Index) des Endes der Information
				Logger.debug(LogIdent+"NrIDXend: " +NrIDXend);
				var RAWopenPoint = PostLoginXML.substring(NrIDXstart, NrIDXend); // Hole den String vom Index1 bis Index2
				//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_AfterLogin RAWopenPoint: " +RAWopenPoint);
				// formatieren des Punktes
				var openPoint = String(org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4(RAWopenPoint)); // in einen Unicode-String umgewandeln (d. h. die HTML Entitys (decimal) sollen richtig dargestellt werden)
				openPoint = HibiscusScripting_MoneYou_stripHTML(openPoint);
				openPoint = openPoint.split("\n").join("").split("\t").join("").split("\r").join("");
				openPoint = HibiscusScripting_MoneYou_removeWhitespace(openPoint);
				Logger.debug(LogIdent+"HibiscusScripting_MoneYou_AfterLogin openPoint gefunden: " +openPoint);
				// zur Liste hinzufügen
				openPoints = openPoints + " - " + openPoint + "\n";
				// und weiter suchen
				SearchPos = NrIDXend;
				
			};
			var SearchPos = 0;
			for (var i = 0; i < donePointsCount; i++) {
				var NrIDXstart = PostLoginXML.indexOf('alt="tickbox"',SearchPos); // Ermittle die Position (Index) des Anfangs der Information
				var NrIDXstart = PostLoginXML.indexOf(">", NrIDXstart)+1; // Ermittle die Position (Index) des Anfangs der Information
				Logger.debug(LogIdent+"NrIDXstart: " +NrIDXstart);
				var NrIDXend = PostLoginXML.indexOf("</tr>", NrIDXstart); // Ermittle die Position (Index) des Endes der Information
				Logger.debug(LogIdent+"NrIDXend: " +NrIDXend);
				var RAWdonePoint = PostLoginXML.substring(NrIDXstart, NrIDXend); // Hole den String vom Index1 bis Index2
				//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_AfterLogin RAWdonePoint: " +RAWdonePoint);
				// formatieren des Punktes
				var donePoint = String(org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4(RAWdonePoint)); // in einen Unicode-String umgewandeln (d. h. die HTML Entitys (decimal) sollen richtig dargestellt werden)
				donePoint = HibiscusScripting_MoneYou_stripHTML(donePoint);
				donePoint = donePoint.split("\n").join("").split("\t").join("").split("\r").join("");
				donePoint = donePoint.split("Ihre Kontonummer ist oben angegeben.").join("");
				donePoint = HibiscusScripting_MoneYou_removeWhitespace(donePoint);
				Logger.debug(LogIdent+"HibiscusScripting_MoneYou_AfterLogin donePoint gefunden: " +donePoint);
				// zur Liste hinzufügen
				donePoints = donePoints + " - " + donePoint + "\n";
				// und weiter suchen
				SearchPos = NrIDXend;
				
			};
			
			// Ausgeben der Informationen an den Benutzer
			Application.getCallback().notifyUser("\nMeldung der MoneYou:\n\n\nUm die Kontoer\u00f6ffnung abzuschlie\u00dfen, sehen Sie hier, was noch zu tun ist:\n\n\n" 
							   + "Was m\u00fcssen Sie oder Wir noch tun?\n\n" + openPoints + "\n\n"
							   + "Wie aktiviert MoneYou Ihr Konto? Was ist erledigt?\n\n" + donePoints + "\n\n");
			
			throw "Die Kontoer\u00f6ffnung ist noch nicht abgeschlossen. Es m\u00fcssen alle angegebenen Punkte erledigt sein ...";
		};
		//*********************************************************************************



		//*********************************************************************************
		// Kontoübersicht: Funktionsaufruf für den Abruf von Informationen der Bank
		//*********************************************************************************
		if (!MoneYou_NewInfoChecked[MoneYou_Benutzer]) { MoneYou_NewInfoChecked[MoneYou_Benutzer] = false; };
		
		if (MoneYou_CheckNewInfo == "full" || (MoneYou_CheckNewInfo == "soft" && MoneYou_NewInfoChecked[MoneYou_Benutzer] == false) || MoneYou_CheckNewInfo == undefined || MoneYou_CheckNewInfo == "") {

			// Hole Briefkasten und dort enthaltene Info-Nachrichten	
			Logger.info(LogIdent+"Pr\u00fcfe auf neue Informationen der Bank und rufe diese nach Bedarf ab ...");
			monitor.log("Pr\u00fcfe auf neue Informationen der Bank und rufe diese nach Bedarf ab ...");
			try {
				var PostLoginPage = HibiscusScripting_MoneYou_getInfo(konto, webClient, monitor);
				
			} catch(err) {
				try {
					HibiscusScripting_MoneYou_SecLogout("Login", monitor, webClient);
				} catch(secerr) {
					Logger.debug(LogIdent+"SecLogout konnte nicht ohne Fehler durchgef\u00fchrt werden. Fehler: " +secerr);
				};
				
				if (MoneYou_NewSyncActive == false) {
					Logger.error(LogIdent+"Konto: " +err);
					return new java.lang.Exception("Konto: " +err);
				
				} else {
					throw ("Konto: " +err);
				};
			} finally {
				MoneYou_NewInfoChecked[MoneYou_Benutzer] = true;
			};
			
		} else if (MoneYou_CheckNewInfo == "off") {
			Logger.info(LogIdent+"Pr\u00fcfung auf neue Informationen der Bank wurde in der Benutzerkonfiguration des Scripts deaktiviert ...");
		};
		//*********************************************************************************


		
		//*********************************************************************************
		// Kontodetailansicht: Funktionsaufruf für den Abruf von Details zum Konto
		//*********************************************************************************
		try {
			var PostLoginPage = HibiscusScripting_MoneYou_getAccInfo(konto, webClient, monitor);
			
		} catch(err) {
			try {
				HibiscusScripting_MoneYou_SecLogout("Login", monitor, webClient);
			} catch(secerr) {
				Logger.debug(LogIdent+"SecLogout konnte nicht ohne Fehler durchgef\u00fchrt werden. Fehler: " +secerr);
			};
			
			if (MoneYou_NewSyncActive == false) {
				Logger.error(LogIdent+"Konto: " +err);
				return new java.lang.Exception("Konto: " +err);
			
			} else {
				throw ("Konto: " +err);
			};
		};
		//*********************************************************************************
		
		
		
		//*********************************************************************************
		// Funktionsaufruf für den Check nach einem Festgeldkonto
		//*********************************************************************************
		try {
			var PostLoginPage = HibiscusScripting_MoneYou_getFixMoney(konto, webClient, monitor);
			
		} catch(err) {
			try {
				HibiscusScripting_MoneYou_SecLogout("Login", monitor, webClient);
			} catch(secerr) {
				Logger.debug(LogIdent+"SecLogout konnte nicht ohne Fehler durchgef\u00fchrt werden. Fehler: " +secerr);
			};
			
			if (MoneYou_NewSyncActive == false) {
				Logger.error(LogIdent+"Konto: " +err);
				return new java.lang.Exception("Konto: " +err);
			
			} else {
				throw ("Konto: " +err);
			};
		};
		//*********************************************************************************



		monitor.setPercentComplete(25);
		
		
		
		//*******************************************************************************
		// Kontoauszug
		//*******************************************************************************
		Logger.info(LogIdent+"Rufe Umsatz\u00fcbersicht auf und starte Abruf des Kontoauszuges ...");
		monitor.log("Rufe Umsatz\u00fcbersicht auf und starte Abruf des Kontoauszuges ...");
		try {
			var ResponseDataArray = HibiscusScripting_MoneYou_getTransData(konto, webClient, monitor);
			var ResponseData = ResponseDataArray[0];
			var Amount = ResponseDataArray[1];
		
		} catch(err) {
			try {
				HibiscusScripting_MoneYou_SecLogout("Login", monitor, webClient);
			} catch(secerr) {
				Logger.debug(LogIdent+"SecLogout konnte nicht ohne Fehler durchgef\u00fchrt werden. Fehler: " +secerr);
			};
			
			if (MoneYou_NewSyncActive == false) {
				Logger.error(LogIdent + "Kontoauszug fehlerhaft: " +err);
				return new java.lang.Exception("Kontoauszug fehlerhaft: " +err);
			
			} else {
				throw ("Kontoauszug fehlerhaft: " +err);
			};
			
		};
		
		// Überprüfen ob es sich womöglich nicht um einen richtigen Kontoauszug handelt
		if (ResponseData.indexOf("Keine (neuen) Ums\u00e4tze vorhanden") == -1) {
			
			// Überprüfen ob es sich womöglich nicht um einen richtigen Kontoauszug handelt
			if (ResponseData.contains("<html") || ResponseData.contains("<head")) {
				try {
					HibiscusScripting_MoneYou_SecLogout("Login", monitor, webClient);
				} catch(secerr) {
					Logger.debug(LogIdent+"SecLogout konnte nicht ohne Fehler durchgef\u00fchrt werden. Fehler: " +secerr);
				};

				throw ("Kontoauszug abholen fehlgeschlagen! Beinhaltet falsche Daten. Bitte neu versuchen oder \u00fcberpr\u00fcfen Sie dies mit einem manuellen Download auf der Bank-Homepage");
			};
		};
		//******************************************************************************


		
		monitor.setPercentComplete(50);
		
		Logger.info(LogIdent+"Kontoauszug erfolgreich. Importiere Daten ...");
		monitor.log("Kontoauszug erfolgreich. Importiere Daten ...");

		
		
		//*********************************************************************************
		// Kontoauszug verarbeiten: Funktionsaufruf für die Datenverarbeitung
		//*********************************************************************************
		try {
			HibiscusScripting_MoneYou_syncDataAndAccount(ResponseData, Amount, konto, monitor);
			
		} catch(err) {
			try {
				HibiscusScripting_MoneYou_SecLogout("Login", monitor, webClient);
			} catch(secerr) {
				Logger.debug(LogIdent+"SecLogout konnte nicht ohne Fehler durchgef\u00fchrt werden. Fehler: " +secerr);
			};
			
			if (MoneYou_NewSyncActive == false) {	
				Logger.error(LogIdent+"Umsatzverarbeitung: " +err);
				return new java.lang.Exception("Umsatzverarbeitung: " +err);
			
			} else {
				throw ("Umsatzverarbeitung: " +err);
			};
		};
		//*********************************************************************************
		
		
		
		monitor.addPercentComplete(99); 
		
		// Logout von der Homepage
		Logger.info(LogIdent+"MoneYou-Logout ...");
		monitor.log("MoneYou-Logout ...");
		try {
			HibiscusScripting_MoneYou_SecLogout("Logout", monitor, webClient);
		} catch(secerr) {
			Logger.debug(LogIdent+"SecLogout konnte nicht ohne Fehler durchgef\u00fchrt werden. Fehler: " +secerr);
		};
		
		
		if (makeCodepageInfo == true) {
			monitor.log("ACHTUNG: Ihre Java-Umgebung verwendet einen falschen Zeichensatz '" + JavaSysCodePage + "' (Richtig w\u00e4re '" + toHaveCodepage + "'). Dadurch k\u00f6nnen Umsatzdoppler entstehen!");
			Logger.warn(LogIdent+"ACHTUNG: Ihre Java-Umgebung verwendet einen falschen Zeichensatz '" + JavaSysCodePage + "' (Richtig w\u00e4re '" + toHaveCodepage + "'). Dadurch k\u00f6nnen Umsatzdoppler entstehen!");
		};


		monitor.log("******************************************************************************************************************\n\n\n");
		monitor.addPercentComplete(100); 
				
				
				
	} catch(err) {
		throw (err);
		
	} finally {
		webClient = null;
	};

};





function HibiscusScripting_MoneYou_prepareClient(monitor) {
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

		
		
		// Ausschalten des Loggin für HTMLUnit
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF); 
		Logger.debug(LogIdent+"Loggin-Einstellung von 'com.gargoylesoftware' ist "+java.util.logging.Logger.getLogger("com.gargoylesoftware").getLevel());



		/****************************************************************************************
		 * ProxyCheckSet ver. 1.4.2
		 * (Prüfen ob ein Proxy, wie in Jameica eingestellt, benötigt wird und stellt diesen ein)
		 *****************************************************************************************/		
		Logger.debug(LogIdent+"es wird auf eine Proxy-Konfiguration gepr\u00FCft ...");
		var JameicaSysProxyUse = Application.getConfig().getUseSystemProxy();
		var JameicaProxyHost = Application.getConfig().getProxyHost();
		var JameicaProxyPort = Application.getConfig().getProxyPort();
		var JameicaHttpsProxyHost = Application.getConfig().getHttpsProxyHost();
		var JameicaHttpsProxyPort = Application.getConfig().getHttpsProxyPort();
		var ProxyConfigSet;

		if ((JameicaSysProxyUse == true) || 
			((JameicaProxyHost != null) && (JameicaProxyPort != -1) && (JameicaSysProxyUse == false)) || 
			((JameicaHttpsProxyHost != null) && (JameicaHttpsProxyPort != -1) && (JameicaSysProxyUse == false))) {

			Logger.info(LogIdent+"Proxy Einstellungen setzten ...");
			monitor.log("Proxy Einstellungen setzten ...");
			
			Logger.debug(LogIdent+"Jameica nutzt den System-Proxy: " +JameicaSysProxyUse);
			Logger.debug(LogIdent+"HTTP-Proxy Host von Jameica ist: " +JameicaProxyHost);
			Logger.debug(LogIdent+"HTTP-Proxy Port von Jameica ist: " +JameicaProxyPort);
			Logger.debug(LogIdent+"HTTPS-Proxy Host von Jameica ist: " +JameicaHttpsProxyHost);
			Logger.debug(LogIdent+"HTTPS-Proxy Port von Jameica ist: " +JameicaHttpsProxyPort);

			if (JameicaSysProxyUse == true) {
				//den Systemproxy in Java übernehmen (dies muss in den Java-Einstellungen auch so konfiguriert sein und kein eigener Proxy! sonst müsste dies hier noch weiter ausgebaut werden ...)
				java.lang.System.setProperty("java.net.useSystemProxies", "true");
				//Werte der System-Proxykonfiguration welche Java nun verwendet
				var SysProxyInfoHTTP = new java.lang.String(java.net.ProxySelector.getDefault().select(new java.net.URI("http://www.java.de")).get(0));
				var SysProxyInfoHTTPS = new java.lang.String(java.net.ProxySelector.getDefault().select(new java.net.URI("https://www.mydrive.ch")).get(0)); 
				var SysProxyFehler = 0;
				Logger.debug(LogIdent+"HTTP Proxy-Einstellung des Systems ist f\u00FCr Java: " +SysProxyInfoHTTP);
				Logger.debug(LogIdent+"HTTPS Proxy-Einstellung des Systems ist f\u00FCr Java: " +SysProxyInfoHTTPS);
				
				if ((SysProxyInfoHTTP == "DIRECT") && (SysProxyInfoHTTPS == "DIRECT"))
				{
						monitor.log("Info-Warnung: Systemproxy-Einstellungen verwenden ist in Jameica eingestellt, es ist aber kein Proxy im System eingetragen!");
						Logger.info(LogIdent+"Systemproxy-Einstellungen verwenden ist in Jameica eingestellt, es ist aber kein Proxy im System eingetragen!");
						ProxyConfigSet = new ProxyConfig();
						
				} else {

					if (SysProxyInfoHTTP != "DIRECT") {
						// HTTP-Proxywerte lesen (so ist es am sichersten,da wir nicht die Einstellung von Java selbst wollen, sondern was Java als Systemproxy ausgelesen hat)
						var SysProxyValuesString = SysProxyInfoHTTP.split(" @ "); //eg.: HTTP @ 10.96.3.105:8080
						Logger.debug(LogIdent+"SysProxyValuesString: " +SysProxyValuesString);
						var SysProxyProtokol = SysProxyValuesString[0];
						Logger.debug(LogIdent+"SysProxyProtokol: " +SysProxyProtokol);
						var SysProxySetting = SysProxyValuesString[1];
						Logger.debug(LogIdent+"SysProxySetting: " +SysProxySetting);
						var SysProxyString = SysProxySetting.split(":"); //eg.: 10.96.3.105:8080
						Logger.debug(LogIdent+"SysProxyString: " +SysProxyString);
						var SysProxyHost = SysProxyString[0];
						Logger.debug(LogIdent+"HTTP-Proxy Host des Systems ist f\u00FCr Java: " +SysProxyHost);
						var SysProxyPort = SysProxyString[1];
						Logger.debug(LogIdent+"HTTP-Proxy Port des Systems ist f\u00FCr Java: " +SysProxyPort);
					} else { 
						monitor.log("Warnung: Systemproxy-Einstellungen verwenden ist in Jameica eingestellt, es ist aber kein Proxy f\u00FCr HTTP im System eingetragen!");
						Logger.warn(LogIdent+"Systemproxy-Einstellungen verwenden ist in Jameica eingestellt, es ist aber kein Proxy f\u00FCr HTTP im System eingetragen!");
						var SysProxyFehler = SysProxyFehler + 1;
					};
					
					if (SysProxyInfoHTTPS != "DIRECT") {
						// HTTPS-Proxywerte lesen (so ist es am sichersten,da wir nicht die Einstellung von Java selbst wollen, sondern was Java als Systemproxy ausgelesen hat)
						var SysHttpsProxyValuesString = SysProxyInfoHTTPS.split(" @ "); //eg.: HTTP @ 10.96.3.107:7070
						Logger.debug(LogIdent+"SysHttpsProxyValuesString: " +SysHttpsProxyValuesString);
						var SysHttpsProxyProtokol = SysHttpsProxyValuesString[0];
						Logger.debug(LogIdent+"SysHttpsProxyProtokol: " +SysHttpsProxyProtokol); // HTTP ist hier auch für HTTPS normal (es ist ja das HTTP-Protokoll)
						var SysHttpsProxySetting = SysHttpsProxyValuesString[1];
						Logger.debug(LogIdent+"SysHttpsProxySetting: " +SysHttpsProxySetting);
						var SysHttpsProxyString = SysHttpsProxySetting.split(":"); //eg.: 10.96.3.105:8080
						Logger.debug(LogIdent+"SysHttpsProxyString: " +SysHttpsProxyString);
						var SysHttpsProxyHost = SysHttpsProxyString[0];
						Logger.debug(LogIdent+"HTTPS-Proxy Host des Systems ist f\u00FCr Java: " +SysHttpsProxyHost);
						var SysHttpsProxyPort = SysHttpsProxyString[1];
						Logger.debug(LogIdent+"HTTPS-Proxy Port des Systems ist f\u00FCr Java: " +SysHttpsProxyPort);
					} else { 
						monitor.log("Warnung: Systemproxy-Einstellungen verwenden ist in Jameica eingestellt, es ist aber kein Proxy f\u00FCr HTTPS im System eingetragen!");
						Logger.warn(LogIdent+"Systemproxy-Einstellungen verwenden ist in Jameica eingestellt, es ist aber kein Proxy f\u00FCr HTTPS im System eingetragen!");
						var SysProxyFehler = SysProxyFehler + 1;
					};
					
					if ((SysHttpsProxyHost != null) && (SysHttpsProxyPort != -1)) {
						ProxyConfigSet = new ProxyConfig(SysHttpsProxyHost, SysHttpsProxyPort, false);
						monitor.log("OK: Es wird der HTTPS-Proxy vom System benutzt");
						Logger.info(LogIdent+"Es wird der HTTPS-Proxy vom System benutzt");
					} else if ((SysProxyHost != null) && (SysProxyPort != -1)) {
						ProxyConfigSet = new ProxyConfig(SysProxyHost, SysProxyPort, false);
						monitor.log("Warnung: Es wird der HTTP-Proxy vom System benutzt. Sollte dieser kein HTTPS unterst\u00FCzen gibt es Fehler!");
						Logger.warn(LogIdent+"Es wird der HTTP-Proxy vom System benutzt. Sollte dieser kein HTTPS unterst\u00FCzen gibt es Fehler!");
					} else { 
						var SysProxyFehler = SysProxyFehler + 1;
						if (SysProxyFehler == 3) {
							monitor.log("Warnungs-INFO: Es sieht so aus als w\u00FCrden Sie eigentlich keinen Proxy verwenden ...");
							monitor.log("Warnungs-INFO: ... entfernen Sie daher wom\u00f6glich einfach den Hacken der Proxykonfiguration ...");
							monitor.log("Warnungs-INFO: ... 'System-Einstellungen verwenden' in den Jameica Einstellungen, um diesen Fehler zu beheben");
						};
						throw "Systemproxy-Einstellungen verwenden ist gew\u00E4hlt: aber bei diesen fehlt offensichtlich ein Eintrag!";
					};
					
				};
				
			} else if ((JameicaHttpsProxyHost != null) && (JameicaHttpsProxyPort != -1) && (JameicaSysProxyUse == false)) {
				ProxyConfigSet = new ProxyConfig(JameicaHttpsProxyHost, JameicaHttpsProxyPort, false);
				monitor.log("OK: Es wird der HTTPS-Proxy von Jameica benutzt");
				Logger.info(LogIdent+"Es wird der HTTPS-Proxy von Jameica benutzt");
			} else if ((JameicaProxyHost != null) && (JameicaProxyPort != -1) && (JameicaSysProxyUse == false)) {
				ProxyConfigSet = new ProxyConfig(JameicaProxyHost, JameicaProxyPort, false);
				monitor.log("Warnung: Es wird der HTTP-Proxy von Jameica benutzt. Sollte dieser kein HTTPS unterst\u00FCzen gibt es Fehler!");
				Logger.warn(LogIdent+"Es wird der HTTP-Proxy von Jameica benutzt. Sollte dieser kein HTTPS unterst\u00FCzen gibt es Fehler!");
			};
			
			
			
			// WebClient mit den den Proxy-Einstellungen anlegen
			webClient.getOptions().setProxyConfig(ProxyConfigSet);
			Logger.debug(LogIdent+"ProxyConfigSet-Configstring ergibt: " + ProxyConfigSet);
			Logger.debug(LogIdent+"WebClient-Proxy-Configstring ergibt: " + webClient.getOptions().getProxyConfig());
			
			
			
		} else if ((JameicaProxyHost != null) && (JameicaProxyPort == -1) && (JameicaSysProxyUse == false)) {
			throw "Es ist ein HTTP-Proxy eingetragen aber die Port-Einstellung fehlt!";
				
		} else if ((JameicaProxyHost == null) && (JameicaProxyPort != -1) && (JameicaSysProxyUse == false)) {
			throw "Es ist ein HTTP-Proxy-Port eingetragen aber die Host-Einstellung fehlt!";
		
		} else if ((JameicaHttpsProxyHost != null) && (JameicaHttpsProxyPort == -1) && (JameicaSysProxyUse == false)) {
			throw "Es ist ein HTTPS-Proxy eingetragen aber die Port-Einstellung fehlt!";
		
		} else if ((JameicaHttpsProxyHost == null) && (JameicaHttpsProxyPort != -1) && (JameicaSysProxyUse == false)) {
			throw "Es ist ein HTTPS-Proxy-Port eingetragen aber die Host-Einstellung fehlt!";
		
		} else {
			Logger.debug(LogIdent+"... es ist auf keine aktive Weise ein Proxy eingestellt");
		};
			
		Logger.info(LogIdent+"Verbindung vorbereitet");
		/***************************************************************************************/	
		
	} catch(err) {
		monitor.log("Warnung: " +err);
		Logger.warn(LogIdent+"" +err);
		throw "Setzen der webClient Verbindungsparameter fehlgeschlagen! (siehe Warnungen im Log)";
	};
	
};





function HibiscusScripting_MoneYou_HttpsLogin(ResponseLogin, ResponsePasswort, webClient) {
/*******************************************************************************
 * Login MoneYou, liefert die Ergebnisseite oder dessen Fehlermeldung zurück
 *******************************************************************************/

	if (String(ResponsePasswort).length > 12) {
		Application.getCallback().notifyUser("  Das MoneYou-Passwort darf nicht mehr als 12-stellig sein  ");
		return;
	};
	
		
	
	// Variable für spezifische Error-Meldungen
	var InputError = 0;

	
	
	try {
		//*********************************************************************************
		// Login-Seite aufrufen, liefert die Ergebnisseite oder dessen Fehlermeldung zurück
		//*********************************************************************************
		Logger.info(LogIdent+"MoneYou-Login aufrufen ... (GET " +HibiscusScripting_MoneYou_LoginURL+")");
		try {		
			var pageLogin = webClient.getPage(HibiscusScripting_MoneYou_LoginURL);
			Logger.debug(LogIdent+"HibiscusScripting_MoneYou_HttpsLogin: pageLogin: " +pageLogin);
			
		} catch(err) {
			InputError = 2;
			throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
		};
		if (!pageLogin) { throw "Die Login-Seite konnte nicht aufgerufen werden!"; };
		var pageLoginXML = pageLogin.asXml();
		//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_HttpsLogin: pageLoginXML: \n" +pageLoginXML); // gibt die ganze Seite aus, also ganz schön viel
		
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
			Logger.debug(LogIdent+"HibiscusScripting_MoneYou_HttpsLogin: formLogin: " +formLogin);
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
		Logger.info(LogIdent+"Login-Form wird abgesendet ...");
		try {
			var PostLoginPage = submitLogin.click();
			Logger.debug(LogIdent+"HibiscusScripting_MoneYou_HttpsLogin: PostLoginPage: " +PostLoginPage);
			
		} catch(err) {
			InputError = 2;
			throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
		};
		if (!PostLoginPage) { throw "Die Login-Folgeseite konnte nicht aufgerufen werden!"; };
		var PostLoginXML = PostLoginPage.asXml();
		//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_HttpsLogin: PostLoginXML: \n" +PostLoginXML); // gibt die ganze Seite aus, also ganz schön viel
		
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

			// Das erste Mal nach dem neuen Kennwort fragen
			try {
				do {	
					var newPINCheck = false;
					do {
						var newPIN1Set = false;
						var newPIN1 = Application.getCallback().askPassword("Login - Kennwort \u00e4ndern\n\n\n"
												  + "MoneYou findet den Schutz Ihrer Daten wichtig.\n"
												  + "Darum bitten wir Sie, Ihr Kennwort einige Male pro Jahr\n"
												  + "beim Einloggen automatisch zu \u00e4ndern. Mit der Kennwort\u00e4nderung\n"
												  + "tragen Sie zu einem sicheren Onlinebanking bei.\n\n"
												  + "Ihr Kennwort muss aus 8 bis 12 Zeichen bestehen und\n"
												  + "mindestens drei der folgenden vier Gruppen enthalten:\n"
												  + "- Gro\u00dfbuchstaben\n"
												  + "- Kleinbuchstaben\n"
												  +" - Zahlen\n"
												  + "- ausschlie\u00dflich die folgenden Sonderzeichen: # ? * + - .\n\n"
												  + "Verwenden Sie KEINE h\u00e4ufig vorkommenden oder einfach\n"
												  + "zu erratenden W\u00f6rter (z. B. Ihren Vor- und Nachnamen).\n"
												  + "Ihr neues Kennwort darf nicht mit den 5 zuletzt verwendeten\n"
												  + "Kennw\u00f6rtern \u00fcbereinstimmen.\n\n"
												  + "neue Kennwort (8 bis 12-stellig)");
						if ((newPIN1.search(/^[0-9A-Za-z]+$/) != -1) && (newPIN1.length() >= 8) && (newPIN1.length() <= 12)) { newPIN1Set = true; };
				      
					} while(newPIN1Set == false);

					do {
						var newPIN2Set = false;
						var newPIN2 = Application.getCallback().askPassword("Login - Kennwort \u00e4ndern\n\n\n"
												  + "Bitte best\u00e4tigen Sie das neue Kennwort noch einmal\n\n"
												  + "Ihr Kennwort muss aus 8 bis 12 Zeichen bestehen und\n"
												  + "mindestens drei der folgenden vier Gruppen enthalten:\n"
												  + "- Gro\u00dfbuchstaben\n"
												  + "- Kleinbuchstaben\n"
												  +" - Zahlen\n"
												  + "- ausschlie\u00dflich die folgenden Sonderzeichen: # ? * + - .\n\n"
														  + "Kennwort-Best\u00e4tigung (8 bis 12-stellig)");
						if ((newPIN2.search(/^[0-9A-Za-z]+$/) != -1) && (newPIN2.length() >= 8) && (newPIN2.length() <= 12)) { newPIN2Set = true; };
				      
					} while(newPIN2Set == false);

					if (String(newPIN1) == String(newPIN2)) { newPINCheck = true; }
					else { Application.getCallback().notifyUser("Kennwort-Vergabe fehlgeschlagen\n\nPIN und Kennwort-Best\u00e4tigung sind nicht identisch\n"); };

				} while(newPINCheck == false);


			} catch(err) {
				InputError = 2;
				throw ("Vergabe eines neuen Kennwort vom Benuzter abgebrochen");
			};


			//*********************************************************************************
			// Setzen des Formulars für die PIN-Änderung
			//*********************************************************************************			
			try {
				var formchangePIN = PostLoginPage.getFormByName("pwdUpdateForm");
				Logger.debug(LogIdent+"HibiscusScripting_MoneYou_HttpsLogin: formchangePIN: " +formchangePIN);

				var AnswerField8 = PostLoginPage.getFirstByXPath("//input[@name='oldPassword']");
				AnswerField8.setValueAttribute(ResponsePasswort);
				var AnswerField9 = PostLoginPage.getFirstByXPath("//input[@name='newPassword']");
				AnswerField9.setValueAttribute(newPIN1);
				var AnswerField7 = PostLoginPage.getFirstByXPath("//input[@name='newPasswordConf']");
				AnswerField7.setValueAttribute(newPIN2);

				var submitchangePIN = PostLoginPage.getElementById("btnNext");
				
			} catch(err) {
				InputError = 2;
				throw "Fehler beim setzen des changePIN-Formulars oder der Felder f\u00fcr die PIN-\u00c4nderung (siehe Log - Bitte den Entwickler im Forum informieren)\nLog-Eintrag: " +err;
			};
			//*********************************************************************************


			//*********************************************************************************
			// Aktivierung abschicken, liefert die Ergebnisseite oder dessen Fehlermeldung zurück
			//*********************************************************************************
			Logger.info(LogIdent+"changePIN-Form wird abgesendet ...");
			try {
				var changePINPage = submitchangePIN.click();
				webClient.waitForBackgroundJavaScript(5000);
				Logger.debug(LogIdent+"HibiscusScripting_MoneYou_HttpsLogin: changePINPage (changePIN): " +changePINPage);
				
			} catch(err) {
				InputError = 2;
				throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
			};
			var changePINXML = changePINPage.asXml();
			//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_HttpsLogin: changePINXML (changePIN): \n" +changePINXML); // gibt die ganze Seite aus, also ganz schön viel
			var changePINResponse = changePINPage.getWebResponse().getContentAsString();
			//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_HttpsLogin: changePINResponse (changePIN): \n" +changePINResponse); // gibt die ganze Seite aus, also ganz schön viel
			
			if (!changePINPage) { throw "Die Aktivierungs-Folgeseite konnte nicht aufgerufen werden!"; };
		
			// Es wird noch der Response auf Fehlernachrichten überprüft
			try {
				HibiscusScripting_MoneYou_checkResponse(changePINResponse, changePINPage);
				
			} catch(err) {
				InputError = 2;
				throw "Fehlermeldung der Bank: " +err;
			};
			//*********************************************************************************


			// nun sollte die Eingabe bestätigt werden
			if (	(changePINResponse.contains("Letzter Login: "))
			     || (changePINResponse.contains("Ausloggen"))
			   ) {
				
				Application.getCallback().notifyUser("Login - Kennwort \u00e4ndern\n\n\n"
								   + "Ihr Kennwort wurde erfolgreich ge\u00e4ndert.\n\n"
								   + "Bitte verwenden Sie ab jetzt nur noch Ihr neu gew\u00e4hltes Kennwort\n\n");

				return PostLoginPage;

			} else {
				Logger.info(LogIdent+"Nach der Eingabe der neuen PIN wird keine Erfolgsseite angezeigt, daher ist hier wahrscheinlich ein Fehler aufgetreten (siehe Seite)");
				Logger.debug(LogIdent+"HibiscusScripting_MoneYou_HttpsLogin: \n" +changePINResponse); // gibt die ganze Seite aus, also ganz schön viel
				throw "Nach der Eingabe der neuen PIN wird keine Erfolgsseite angezeigt, daher ist hier wahrscheinlich ein Fehler aufgetreten";
			};

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





function HibiscusScripting_MoneYou_SecLogout(func, monitor, webClient) {
/*******************************************************************************
 * Logout MoneYou: wird auch zur Sicherheit bei auftreten eines Fehler ausgeführt
 *******************************************************************************/

	if (func != "Logout") {
		Logger.info(LogIdent + "f\u00fcr die Sicherheit wird noch der Logout durchgef\u00fchrt und der Passwortspeicher zur\u00fcckgesetzt ...");
		monitor.log("Pre-Fehler: f\u00fcr die Sicherheit wird noch der Logout durchgef\u00fchrt und der Passwortspeicher zur\u00fcckgesetzt ...");
	};
	
	if (func != "noLogin") {

		var page = webClient.getCurrentWindow().getEnclosedPage();

		var LogoutURL = page.getAnchorByText("Ausloggen"); // HibiscusScripting_MoneYou_LogoutURL
		Logger.debug(LogIdent+"LogoutURL: " +LogoutURL);
		
		try {
			var PostLogoutPage = LogoutURL.click();
		
		} catch(err) {
			throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
		};
		Logger.debug(LogIdent+"PostLogoutPage: " +PostLogoutPage);
		//Logger.debug(LogIdent+"PostLogoutPageXML: \n" +PostLogoutPage.asXml()); //gibt die ganze Logout-Seite im Log aus

	};
	
	// alle Fenster schließen
	webClient.closeAllWindows();
	
	// sensible Daten löschen
	if (func != "Logout") { 
		HibiscusScripting_MoneYou_lPass[MoneYou_Benutzer] = null; // zur Sicherheit wird hier nun das zwischengespeicherte Passwort auf Null gesetzt
		if (Hibiscus_Wallet.get(MoneYou_Wallet_Alias)) { Hibiscus_Wallet.set(MoneYou_Wallet_Alias,null); }; // zur Sicherheit wird hier nun das permanet gespeicherte Passwort gelöscht
	};
	MoneYou_Benutzer = "";
	MoneYou_Wallet_Alias = "";

	// und noch wichtig: ein paar Variablen zurück setzen!
	MoneYou_fetchSaldo = "";
	MoneYou_fetchUmsatz = "";

};





function HibiscusScripting_MoneYou_makeHash(identString) {
/*******************************************************************************
 * Kodiert den übergebenen String in einen MD5-Hashschlüssel
 *******************************************************************************/

		var origString = new java.lang.String(identString);
		
		var HashAlgorithm = java.security.MessageDigest.getInstance("MD5");
		HashAlgorithm["update(byte[])"](origString.getBytes());
		
		var digest = HashAlgorithm.digest();
		
		var stringBuffer = new java.lang.StringBuffer();
		
		for (var i = 0; i < digest.length; i++) {
			stringBuffer.append(java.lang.Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
		};

		//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_makeHash original: " + origString);
		Logger.debug(LogIdent+"HibiscusScripting_MoneYou_makeHash digested: " + digest);
		Logger.debug(LogIdent+"HibiscusScripting_MoneYou_makeHash digested(hex): " + stringBuffer.toString());
		
		var MD5hash = stringBuffer.toString();
		
		return MD5hash;

};





function HibiscusScripting_MoneYou_getInfo(konto, webClient, monitor) {
/*******************************************************************************
 * Information MoneYou, liefert die Information oder eine Fehlermeldung zurück
 *******************************************************************************/
	
	
	var page = webClient.getCurrentWindow().getEnclosedPage();


	//*********************************************************************************
	// Mailboxinfo aufrufen, liefert die Ergebnisseite
	//*********************************************************************************
	var PostBoxStatusURL = page.getAnchorByText("E-Mail-Postfach");
	Logger.info(LogIdent+"Briefkasten checken ... (Click " +PostBoxStatusURL+")");
	try {		
		var PostBoxStatus = PostBoxStatusURL.click();
		Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getInfo: PostBoxStatus: " +PostBoxStatus);
		
	} catch(err) {
		InputError = 2;
		throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
	};	
	if (!PostBoxStatus) { throw "Der Briefkasten-Status konnte nicht aufgerufen werden!"; };
	var PostBoxStatusResponse = PostBoxStatus.getWebResponse().getContentAsString();
	//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getInfo: PostBoxStatusResponse: \n" +PostBoxStatusResponse); // gibt die ganze Seite aus, also ganz schön viel
	//*********************************************************************************

	
	// den Text erzeugen
	var InfoText = " neue Nachricht(en)";

	
	if (PostBoxStatusResponse.contains("Ergebnisse 0 - 0")) {
		var PostCount = 0;
	
	} else if (PostBoxStatusResponse.contains("eingegangen")) {
		/*
		// die Anzahl holen
		var NrIDXstart = PostBoxStatusResponse.indexOf("<value>")+7; // Ermittle die Position (Index) des Anfangs der Information
		Logger.debug(LogIdent+"NrIDXstart: " +NrIDXstart);
		var NrIDXend = PostBoxStatusResponse.indexOf("</value>", NrIDXstart); // Ermittle die Position (Index) des Endes der Information
		Logger.debug(LogIdent+"NrIDXend: " +NrIDXend);
		var PostCount = PostBoxStatusResponse.substring(NrIDXstart, NrIDXend); // Hole den String vom Index1 bis Index2
		Logger.debug(LogIdent+"PostCount: " +PostCount);

		// den Text holen
		var InfoIDXstart = PostBoxStatusResponse.indexOf("<text>")+6; // Ermittle die Position (Index) des Anfangs der Information
		Logger.debug(LogIdent+"InfoIDXstart: " +InfoIDXstart);
		var InfoIDXend = PostBoxStatusResponse.indexOf("</text>", InfoIDXstart); // Ermittle die Position (Index) des Endes der Information
		Logger.debug(LogIdent+"InfoIDXend: " +InfoIDXend);
		var InfoText = PostBoxStatusResponse.substring(InfoIDXstart, InfoIDXend); // Hole den String vom Index1 bis Index2
		Logger.debug(LogIdent+"InfoText: " +InfoText);
		*/

		var PostCount = 99999;
		
		// Check zur Sicherheit ob der PostCount eine Zahl ist
		if (!isFinite(PostCount)) { Logger.warn(LogIdent+"Die Angabe der Anzahl von Postnachrichten ist keine richtige Zahl, vermutlich ein Fehler beim Auslesen"); };
	
	} else {
		var PostCount = 0;
	};
	

	if (PostCount > 0) {
		if (MoneYou_NewSyncActive == false) {
			// Hier wird nun also die perfekt formatierte Nachricht in einem Infofenster ausgegeben
			Application.getCallback().notifyUser("\nMeldung der MoneYou:\n\n\nIhr Briefkasten enth\u00e4lt " + InfoText + "\n\n");
		
		} else {
			// Datenbank-Verbindung holen
			if (Java8upFrame == true) {
				var db = Application.getServiceFactory().lookup(java.lang.Class.forName("de.willuhn.jameica.hbci.HBCI"),"database");
			} else {
				var db = Application.getServiceFactory().lookup(HBCI,"database");
			};

			//for (var v = 0; v < MsgArray.length; v++) {
				//if (v == 5) { break; }; // bei der 5ten machen wir mal Schluss
				if (Java8upFrame == true) {
					var SystemMsg = db.createObject(java.lang.Class.forName("de.willuhn.jameica.hbci.rmi.Nachricht"),null);
				} else {
					var SystemMsg = db.createObject(Nachricht,null);
				};
				SystemMsg.setBLZ(konto.getBLZ());
				SystemMsg.setNachricht("Ihr Briefkasten enth\u00e4lt " + InfoText);
				SystemMsg.setDatum(new java.util.Date());
				SystemMsg.store();
			//};
			
			Logger.info(LogIdent+"Es wurden '"+PostCount+"' Kontoausz\u00fcge und/oder Mitteilung(en) ausgelesen und eingetragen");
			monitor.log("Es wurden '"+PostCount+"' Kontoausz\u00fcge und/oder Mitteilung(en) ausgelesen und eingetragen");

		};
		
		return PostBoxStatus;
	} else {
		Logger.info(LogIdent+"Keine ungelesenen Kontoausz\u00fcge und/oder Mitteilung(en) vorhanden");
		monitor.log("Keine ungelesenen Kontoausz\u00fcge und/oder Mitteilung(en) vorhanden");
		
		return PostBoxStatus;
	};
	
};





function HibiscusScripting_MoneYou_getAccInfo(konto, webClient, monitor) {
/*******************************************************************************
 * Ruft die Kontoübersicht oder die Detailansicht für das ausgewählte Konto auf,
 * und liest dort Details auf um diese ins Kommentarfeld zu schreiben
 *******************************************************************************/

	var page = webClient.getCurrentWindow().getEnclosedPage();


	// Vorbereiten und prüfen ob Detailinformationen überhaupt abgerufen werden sollten
	var Kommentar = konto.getKommentar();
	var Unterkonto = konto.getUnterkonto();
	if (Kommentar == null) { Kommentar = new java.lang.String(""); };
	if (Unterkonto == null) { Unterkonto = new java.lang.String(""); };
	Logger.debug(LogIdent+"Kontodetails - Anzahl der Zeichen in der Notiz: " +Kommentar.length());
	Logger.debug(LogIdent+"Kontodetails - Anzahl der Zeichen im Feld Unterkonto: " +Unterkonto.length());
	if (((Kommentar.length() > 0) && (!Kommentar.contains("Kontodetail-Informationen der Bank")) && (!Kommentar.contains("Automatisch erzeugtes Konto"))) 
	 && ((Unterkonto.length() > 0) && (!Unterkonto.contains("Zinsen")))) { return; };


	//*********************************************************************************
	// Übersicht aufrufen, liefert die Ergebnisseite oder dessen Fehlermeldung zurück
	//*********************************************************************************
	var AccountsListURL = page.getAnchorByText("Mein Tagesgeld");
	Logger.debug(LogIdent+"Click " +AccountsListURL);
	try {		
		var DataPage = AccountsListURL.click();
		Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getAccInfo: AccountsListURL DataPage (Seite): " +DataPage);
		
	} catch(err) {
		InputError = 2;
		throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
	};
	if (!DataPage) { throw "Die Tagesgeld-Konten\u00fcbersicht konnte nicht aufgerufen werden!"; };
	var DataPageXML = DataPage.asXml();	
	//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getAccInfo: DataPageXML (Seite): \n" +DataPageXML); // gibt die ganze Seite aus, also ganz schön viel
	var AccountDetailResponse = DataPage.getWebResponse().getContentAsString();;	
	//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getAccInfo: AccountDetailResponse (Seite): \n" +AccountDetailResponse); // gibt die ganze Seite aus, also ganz schön viel		
	
	// Es wird noch der Response auf Fehlernachrichten überprüft
	try {
		HibiscusScripting_MoneYou_checkResponse(DataPageXML, DataPage);
		
	} catch(err) {
		InputError = 2;
		throw "Fehlermeldung der Bank: " +err;
	};
	//*********************************************************************************

	
	// Die Texte auslesen und formatieren
	var IBANsub = konto.getKontonummer().substring(0,4) + " " + konto.getKontonummer().substring(4,8) + " " + konto.getKontonummer().substring(8,10);
	var DataStart = AccountDetailResponse.indexOf(IBANsub); // Ermittle die Position (Index) des Anfangs der Daten

	// nun den aktuellen Zinsatz auslesen
	var DataIDX = AccountDetailResponse.indexOf('interestRate', DataStart); // Ermittle die Position (Index) des Anfangs der Daten
	var DataIDX = AccountDetailResponse.indexOf('value', DataIDX); // Ermittle die Position (Index) des Anfangs der Daten
	Logger.debug(LogIdent+"DataIDX: " +DataIDX);
	var DataInterestPaIDXstart = AccountDetailResponse.indexOf('>', DataIDX)+1; // Ermittle die Position (Index) des Anfangs der Daten
	Logger.debug(LogIdent+"DataInterestPaIDXstart: " +DataInterestPaIDXstart);
	var DataInterestPaIDXend = AccountDetailResponse.indexOf("<", DataInterestPaIDXstart); // Ermittle die Position (Index) des Endes der Daten
	Logger.debug(LogIdent+"DataInterestPaIDXend: " +DataInterestPaIDXend);
	var DataInterestPaText = AccountDetailResponse.substring(DataInterestPaIDXstart, DataInterestPaIDXend); // Hole den String vom Index1 bis Index2
	//Logger.debug(LogIdent+"DataInterestPaText: " +DataInterestPaText);

	// nun die Kontoart auslesen
	var DataIDX = AccountDetailResponse.indexOf('bankProductInList', DataInterestPaIDXend); // Ermittle die Position (Index) des Anfangs der Daten
	var DataIDX = AccountDetailResponse.indexOf('value', DataIDX); // Ermittle die Position (Index) des Anfangs der Daten
	Logger.debug(LogIdent+"DataIDX: " +DataIDX);
	var DataAccountArtIDXstart = AccountDetailResponse.indexOf('>', DataIDX)+1; // Ermittle die Position (Index) des Anfangs der Daten
	Logger.debug(LogIdent+"DataAccountArtIDXstart: " +DataAccountArtIDXstart);
	var DataAccountArtIDXend = AccountDetailResponse.indexOf("<", DataAccountArtIDXstart); // Ermittle die Position (Index) des Endes der Daten
	Logger.debug(LogIdent+"DataAccountArtIDXend: " +DataAccountArtIDXend);
	var DataAccountArtText = AccountDetailResponse.substring(DataAccountArtIDXstart, DataAccountArtIDXend); // Hole den String vom Index1 bis Index2
	//Logger.debug(LogIdent+"DataAccountArtText: " +DataAccountArtText);

	// nun die errechneten Zinsen bis heute
	var DataIDX = AccountDetailResponse.indexOf('accruedInterestInList', DataAccountArtIDXend); // Ermittle die Position (Index) des Anfangs der Daten
	var DataIDX = AccountDetailResponse.indexOf('value', DataIDX); // Ermittle die Position (Index) des Anfangs der Daten
	Logger.debug(LogIdent+"DataIDX: " +DataIDX);
	var DataInterestIDXstart = AccountDetailResponse.indexOf('>', DataIDX)+1; // Ermittle die Position (Index) des Anfangs der Daten
	Logger.debug(LogIdent+"DataInterestIDXstart: " +DataInterestIDXstart);
	var DataInterestIDXend = AccountDetailResponse.indexOf("<", DataInterestIDXstart); // Ermittle die Position (Index) des Endes der Daten
	Logger.debug(LogIdent+"DataInterestIDXend: " +DataInterestIDXend);
	var DataInterestText = AccountDetailResponse.substring(DataInterestIDXstart, DataInterestIDXend); // Hole den String vom Index1 bis Index2
	//Logger.debug(LogIdent+"DataInterestText: " +DataInterestText);
	
	// nun noch alles formatieren
	var AccArt = DataAccountArtText.split(" ").join("").split("\r").join("").split("\n").join("").split("\t").join("");
	Logger.debug(LogIdent+"Kontodetails-Informationen (Kontoart): " +DataAccountArtText);
	var InterestPa = DataInterestPaText.split("  ").join("").split("\r").join("").split("\n").join("").split("\t").join("");
	Logger.debug(LogIdent+"Kontodetails-Informationen (Zinssatz in % p.a.): " +DataInterestPaText);
	var Interest = DataInterestText.split("  ").join("").split("\r").join("").split("\n").join("").split("\t").join("");
	Logger.debug(LogIdent+"Kontodetails-Informationen (Errechnete Zinsen bis heute): " +Interest);
	
	// Speichern der Informationen im Konto
	if (Kommentar.length() < 1 || Kommentar.contains("Kontodetail-Informationen der Bank") || Kommentar.contains("Automatisch erzeugtes Konto")) {
		if (OSArtString.contains("Linux")) { 
			konto.setKommentar("Aufgelaufene Zinsen: " + Interest + "  \n\n"
					 + "Kontodetail-Informationen der Bank:\n"
					 + "-----------------------------------\n"
					 + "Kontoart:\t\t\t\t" + AccArt + "\n"
					 + "Zinssatz in % p.a.:\t\t" + InterestPa);
		} else if (OSArtString.contains("Mac")) {
			konto.setKommentar("Aufgelaufene Zinsen: " + Interest + "  \n\n"
					 + "Kontodetail-Informationen der Bank:\n"
					 + "-----------------------------------\n"
					 + "Kontoart:\t\t\t\t" + AccArt + "\n"
					 + "Zinssatz in % p.a.:\t\t" + InterestPa);
		
		} else {
			konto.setKommentar("Aufgelaufene Zinsen: " + Interest + "  \n\n"
					 + "Kontodetail-Informationen der Bank:\n"
					 + "-----------------------------------\n"
					 + "Kontoart:\t\t\t\t\t" + AccArt + "\n"
					 + "Zinssatz in % p.a.:\t\t\t" + InterestPa);
		};
	
	} else if (Unterkonto.length() < 1 || Unterkonto.contains("Zinsen")) {
		konto.setUnterkonto("laufende Zinsen: "+Interest);
	};
	
	// und das ganze speichern
	konto.store();
	
	// jetzt noch gleich die Anzeige aktualisieren
	/***** hierzu muss erst noch das Multi-Threading angepasst werden *****/
	//var view = GUI.getCurrentView();
	//GUI.startView(view.getClass(),view.getCurrentObject());
	
	return DataPage;
		
};





function HibiscusScripting_MoneYou_getFixMoney(konto, webClient, monitor) {
/*******************************************************************************
 * Ruft die Kontoübersicht und wenn vorhanden ein Festgeldkonto auf,
 * und liest dort dessen Details aus um diese ins Kommentarfeld zu schreiben
 *******************************************************************************/

	var page = webClient.getCurrentWindow().getEnclosedPage(); 


	if (page.getWebResponse().getContentAsString().contains("Mein Festgeld")/* && (KontoExist == false)*/ && (MoneYou_FixMoney_Checked != true)) {

		try {
			if (Java8upFrame == true) {
				var db = Application.getServiceFactory().lookup(java.lang.Class.forName("de.willuhn.jameica.hbci.HBCI"),"database");
			} else {
				var db = Application.getServiceFactory().lookup(HBCI,"database");
			};
			
		} catch(e) {
			// Beim Start von Jameica steht zum Init-Zeitpunkt noch kein DB-Zugriff zur Verfügung
			Logger.debug(LogIdent+"Kein Datenbankzugriff f\u00fcr MoneYou-PlugIn m\u00F6glich. Check auf existierendes MoneYou-Konto wird abgebrochen (siehe Log)\nLog: "+e);
			return page;
		};
		
		if (Java8upFrame == true) {
			var list = db.createList(java.lang.Class.forName("de.willuhn.jameica.hbci.rmi.Konto"));
		} else {
			var list = db.createList(Konto);
		};

		var KontoExist = false;
		
		while(list.hasNext()) {
			var festkonto = list.next();
			
			if ((festkonto.getBLZ() == HibiscusScripting_MoneYou_BLZ) && (festkonto.getKundennummer().length() > 12) && ((String(festkonto.getUnterkonto()) == "Festgeld"))) { 
				var KontoExist = true; // Ein Offline-Festgeldkonto für die MoneYou existiert bereits
				break;
			}; 
		};

		if (KontoExist == false) {

			// Vorbereiten und prüfen ob nach Festgeld überhaupt geprüft werden soll
			// Falls ja, bieten wir dem Benutzer an eines zu erzeugen
			Logger.info(LogIdent+"Nachfrage ob Anlage eines Festgeld-Offlinekonto erw\u00FCnscht ...");

			var AssiInfoString = new java.lang.String("\n\n\nIn Ihrem MoneYou-Account befindet sich mindestens ein Festgeldkonto ...\n\nZur Zeit ist noch kein Festgeldkonto f\u00fcr die MoneYou (ABN AMRO Bank) in Hibiscus angelegt\n"
								+ "Wollen Sie jetzt den Assistenten zur automatischen Kontoanlage ausf\u00fchren?\n\n"
								+ "[ INFO: Wenn Sie dies sp\u00e4ter erledigen wollen w\u00e4hlen Sie 'Nein'\n"
								+ "um diesen Assistenten beim n\u00e4chsten Abruf der neuen Session erneut zu starten.\n"
								+ "(vorausgesetzt Sie aktivieren nicht den folgenden Haken und w\u00e4hlen zus\u00e4tzlich 'Nein'!)");
										 
			if (Java8upFrame == true) {
				var infoArray = java.lang.reflect.Array.newInstance(java.lang.Class.forName("java.lang.String"), 1);
			} else {
				var infoArray = java.lang.reflect.Array.newInstance(java.lang.String, 1);
			};
			infoArray[0] = AssiInfoString;
			// Abfragefenster beim Benutzer mit Info-Text, einstellbar dass dieses nicht mehr erscheint
			var StartUpdateAccount = Application.getCallback().askUser("MoneYou (ABN AMRO Bank) - Kontoanlage Assistent - Festgeld {0}", infoArray);

		} else {
			var StartUpdateAccount = true;
		};



		if (StartUpdateAccount == true) {

			var DataPage = page;
			var RunArt = "active";
			var fCount = 0;
			var AccountList = new Array();
			
			do {
				//*********************************************************************************
				// Übersicht aufrufen, liefert die Ergebnisseite oder dessen Fehlermeldung zurück
				//*********************************************************************************
				var AccountsListURL = DataPage.getAnchorByText("Mein Festgeld");
				Logger.debug(LogIdent+"Click " +AccountsListURL);
				try {		
					var DataPage = AccountsListURL.click();
					Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getFixMoney: AccountsListURL DataPage (Seite): " +DataPage);
					
				} catch(err) {
					InputError = 2;
					throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
				};
				if (!DataPage) { throw "Die Festgeld-Konten\u00fcbersicht konnte nicht aufgerufen werden!"; };
				var DataPageXML = DataPage.asXml();	
				//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getFixMoney: DataPageXML (Seite): \n" +DataPageXML); // gibt die ganze Seite aus, also ganz schön viel
				var AccountDetailResponse = DataPage.getWebResponse().getContentAsString();;	
				//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getFixMoney: AccountDetailResponse (Seite): \n" +AccountDetailResponse); // gibt die ganze Seite aus, also ganz schön viel		
				
				// Es wird noch der Response auf Fehlernachrichten überprüft
				try {
					HibiscusScripting_MoneYou_checkResponse(DataPageXML, DataPage);
					
				} catch(err) {
					InputError = 2;
					throw "Fehlermeldung der Bank: " +err;
				};
				//*********************************************************************************

				
				//*********************************************************************************
				// Kontostatus einstellen, liefert die Ergebnisseite oder dessen Fehlermeldung zurück
				//*********************************************************************************
				if (RunArt == "active") {
					DataPage.getFirstByXPath("//input[@id='activeContractSwitch']").setChecked(true);
					DataPage.getFirstByXPath("//input[@id='closedContractSwitch']").setChecked(false);

				} else if (RunArt == "inactive") {
					DataPage.getFirstByXPath("//input[@id='activeContractSwitch']").setChecked(false);
					DataPage.getFirstByXPath("//input[@id='closedContractSwitch']").setChecked(true);	
				};


				var NextButton = DataPage.getFirstByXPath("//input[@id='btnNext']");
				Logger.debug(LogIdent+"Click " +NextButton);
				try {		
					var DataPage = NextButton.click();
					Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getFixMoney: NextButton DataPage (Seite): " +DataPage);
					
				} catch(err) {
					InputError = 2;
					throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
				};
				if (!DataPage) { throw "Die Festgeld-Konten\u00fcbersicht konnte nicht aufgerufen werden!"; };
				var DataPageXML = DataPage.asXml();	
				//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getFixMoney: DataPageXML (Seite): \n" +DataPageXML); // gibt die ganze Seite aus, also ganz schön viel
				var AccountDetailResponse = DataPage.getWebResponse().getContentAsString();;	
				//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getFixMoney: AccountDetailResponse (Seite): \n" +AccountDetailResponse); // gibt die ganze Seite aus, also ganz schön viel		
				
				// Es wird noch der Response auf Fehlernachrichten überprüft
				try {
					HibiscusScripting_MoneYou_checkResponse(DataPageXML, DataPage);
					
				} catch(err) {
					InputError = 2;
					throw "Fehlermeldung der Bank: " +err;
				};
				//*********************************************************************************


				// Zählen der Festgeldkonten und auslesen der Links zur Detailansicht
				var AccLinks = DataPage.getByXPath("//span[contains(@id,'contractEndDateIn')]/parent::a");
				Logger.debug(LogIdent+"Es sind '"+AccLinks.size()+"' '"+RunArt+"' Festgeld-Anlagen vorhanden ...");

				if (RunArt == "active") { var ArtActive = AccLinks.size(); }
				else if (RunArt == "inactive") { var ArtInactive = AccLinks.size(); };

				
				for (var i = 0; i < AccLinks.size(); i++) {

					if (i > 0) {
						
						//*********************************************************************************
						// Übersicht aufrufen, liefert die Ergebnisseite oder dessen Fehlermeldung zurück
						//*********************************************************************************
						var AccountsURL = DataPage.getAnchorByText("Mein Festgeld");
						Logger.debug(LogIdent+"Click " +AccountsURL);
						try {		
							var DataPage = AccountsURL.click();
							Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getFixMoney: AccountsURL DataPage (Seite): " +DataPage);
							
						} catch(err) {
							InputError = 2;
							throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
						};
						if (!DataPage) { throw "Die Festgeld-Konten\u00fcbersicht konnte nicht aufgerufen werden!"; };
						var DataPageXML = DataPage.asXml();	
						//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getFixMoney: DataPageXML (Seite): \n" +DataPageXML); // gibt die ganze Seite aus, also ganz schön viel
						var AccountDetailResponse = DataPage.getWebResponse().getContentAsString();;	
						//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getFixMoney: AccountDetailResponse (Seite): \n" +AccountDetailResponse); // gibt die ganze Seite aus, also ganz schön viel		
						
						// Es wird noch der Response auf Fehlernachrichten überprüft
						try {
							HibiscusScripting_MoneYou_checkResponse(DataPageXML, DataPage);
							
						} catch(err) {
							InputError = 2;
							throw "Fehlermeldung der Bank: " +err;
						};
						//*********************************************************************************
							
						//*********************************************************************************
						// Kontostatus einstellen, liefert die Ergebnisseite oder dessen Fehlermeldung zurück
						//*********************************************************************************
						if (RunArt == "active") {
							DataPage.getFirstByXPath("//input[@id='activeContractSwitch']").setChecked(true);
							DataPage.getFirstByXPath("//input[@id='closedContractSwitch']").setChecked(false);

						} else if (RunArt == "inactive") {
							DataPage.getFirstByXPath("//input[@id='activeContractSwitch']").setChecked(false);
							DataPage.getFirstByXPath("//input[@id='closedContractSwitch']").setChecked(true);	
						};

						var NextButton = DataPage.getFirstByXPath("//input[@id='btnNext']");
						Logger.debug(LogIdent+"Click " +NextButton);
						try {		
							var DataPage = NextButton.click();
							Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getFixMoney: NextButton DataPage (Seite): " +DataPage);
							
						} catch(err) {
							InputError = 2;
							throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
						};
						if (!DataPage) { throw "Die Festgeld-Konten\u00fcbersicht konnte nicht aufgerufen werden!"; };
						var DataPageXML = DataPage.asXml();	
						//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getFixMoney: DataPageXML (Seite): \n" +DataPageXML); // gibt die ganze Seite aus, also ganz schön viel
						var AccountDetailResponse = DataPage.getWebResponse().getContentAsString();;	
						//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getFixMoney: AccountDetailResponse (Seite): \n" +AccountDetailResponse); // gibt die ganze Seite aus, also ganz schön viel		
						
						// Es wird noch der Response auf Fehlernachrichten überprüft
						try {
							HibiscusScripting_MoneYou_checkResponse(DataPageXML, DataPage);
							
						} catch(err) {
							InputError = 2;
							throw "Fehlermeldung der Bank: " +err;
						};
						//*********************************************************************************

						// Auslesen der Links zur Detailansicht
						var AccLinks = DataPage.getByXPath("//span[contains(@id,'contractEndDateIn')]/parent::a");
					};
					
					
					//*********************************************************************************
					// Detailansicht aufrufen, liefert die Ergebnisseite oder dessen Fehlermeldung zurück
					//*********************************************************************************
					var AccountURL = AccLinks.get(i);
					Logger.debug(LogIdent+"Click " +AccountURL);
					try {		
						var DataPage = AccountURL.click();
						Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getFixMoney: AccountURL DataPage (Seite): " +DataPage);
						
					} catch(err) {
						InputError = 2;
						throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
					};
					if (!DataPage) { throw "Die Detailansicht des Festgeld-Konto konnte nicht aufgerufen werden!"; };
					var DataPageXML = DataPage.asXml();	
					//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getFixMoney: DataPageXML (Seite): \n" +DataPageXML); // gibt die ganze Seite aus, also ganz schön viel
					var AccountDetailResponse = DataPage.getWebResponse().getContentAsString();;	
					//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getFixMoney: AccountDetailResponse (Seite): \n" +AccountDetailResponse); // gibt die ganze Seite aus, also ganz schön viel		
					
					// Es wird noch der Response auf Fehlernachrichten überprüft
					try {
						HibiscusScripting_MoneYou_checkResponse(DataPageXML, DataPage);
						
					} catch(err) {
						InputError = 2;
						throw "Fehlermeldung der Bank: " +err;
					};
					//*********************************************************************************
					
				

					// Beginndatum auslesen
					var BeginndatumIDXstart = AccountDetailResponse.indexOf('Beginndatum:'); // Ermittle die Position (Index) des Anfangs der Daten
					var BeginndatumIDXstart = AccountDetailResponse.indexOf('<span',BeginndatumIDXstart); // Ermittle die Position (Index) des Anfangs der Daten
					Logger.debug(LogIdent+"BeginndatumIDXstart: " +BeginndatumIDXstart);
					var BeginndatumIDXend = AccountDetailResponse.indexOf("</span>", BeginndatumIDXstart)+7; // Ermittle die Position (Index) des Endes der Daten
					Logger.debug(LogIdent+"BeginndatumIDXend: " +BeginndatumIDXend);
					var BeginndatumText = AccountDetailResponse.substring(BeginndatumIDXstart, BeginndatumIDXend); // Hole den String vom Index1 bis Index2
					// Logger.debug(LogIdent+"BeginndatumText: " +BeginndatumText);

					// Enddatum auslesen
					var EnddatumIDXstart = AccountDetailResponse.indexOf('Enddatum:'); // Ermittle die Position (Index) des Anfangs der Daten
					var EnddatumIDXstart = AccountDetailResponse.indexOf('<span',EnddatumIDXstart); // Ermittle die Position (Index) des Anfangs der Daten
					Logger.debug(LogIdent+"EnddatumIDXstart: " +EnddatumIDXstart);
					var EnddatumIDXend = AccountDetailResponse.indexOf("</span>", EnddatumIDXstart)+7; // Ermittle die Position (Index) des Endes der Daten
					Logger.debug(LogIdent+"EnddatumIDXend: " +EnddatumIDXend);
					var EnddatumText = AccountDetailResponse.substring(EnddatumIDXstart, EnddatumIDXend); // Hole den String vom Index1 bis Index2
					// Logger.debug(LogIdent+"EnddatumText: " +EnddatumText);

					if (RunArt == "inactive") { 
						// Kündigungsdatum auslesen
						var CanceldatumIDXstart = AccountDetailResponse.indexOf('ndigungsdatum:'); // Ermittle die Position (Index) des Anfangs der Daten
						var CanceldatumIDXstart = AccountDetailResponse.indexOf('<span',CanceldatumIDXstart); // Ermittle die Position (Index) des Anfangs der Daten
						Logger.debug(LogIdent+"CanceldatumIDXstart: " +CanceldatumIDXstart);
						var CanceldatumIDXend = AccountDetailResponse.indexOf("</span>", CanceldatumIDXstart)+7; // Ermittle die Position (Index) des Ende der Daten
						Logger.debug(LogIdent+"CanceldatumIDXend: " +CanceldatumIDXend);
						var CanceldatumText = AccountDetailResponse.substring(CanceldatumIDXstart, CanceldatumIDXend); // Hole den String vom Index1 bis Index2
						// Logger.debug(LogIdent+"CanceldatumText: " +CanceldatumText);
					};

					// Zinssatz auslesen
					var ZinssatzIDXstart = AccountDetailResponse.indexOf('Zinssatz:'); // Ermittle die Position (Index) des Anfangs der Daten
					var ZinssatzIDXstart = AccountDetailResponse.indexOf('<span',ZinssatzIDXstart); // Ermittle die Position (Index) des Anfangs der Daten
					Logger.debug(LogIdent+"ZinssatzIDXstart: " +ZinssatzIDXstart);
					var ZinssatzIDXend = AccountDetailResponse.indexOf("</span>", ZinssatzIDXstart)+7; // Ermittle die Position (Index) des Endes der Daten
					Logger.debug(LogIdent+"ZinssatzIDXend: " +ZinssatzIDXend);
					var ZinssatzText = AccountDetailResponse.substring(ZinssatzIDXstart, ZinssatzIDXend); // Hole den String vom Index1 bis Index2
					// Logger.debug(LogIdent+"ZinssatzText: " +ZinssatzText);

					// Betrag auslesen
					var BetragIDXstart = AccountDetailResponse.indexOf('Betrag:'); // Ermittle die Position (Index) des Anfangs der Daten
					var BetragIDXstart = AccountDetailResponse.indexOf('<span',BetragIDXstart); // Ermittle die Position (Index) des Anfangs der Daten
					Logger.debug(LogIdent+"BetragIDXstart: " +BetragIDXstart);
					var BetragIDXend = AccountDetailResponse.indexOf("</span>", BetragIDXstart)+7; // Ermittle die Position (Index) des Endes der Daten
					Logger.debug(LogIdent+"BetragIDXend: " +BetragIDXend);
					var BetragText = AccountDetailResponse.substring(BetragIDXstart, BetragIDXend); // Hole den String vom Index1 bis Index2
					// Logger.debug(LogIdent+"BetragText: " +BetragText);

					// Zinsen auslesen
					var ZinsenIDXstart = AccountDetailResponse.indexOf('Zinsen:'); // Ermittle die Position (Index) des Anfangs der Daten
					var ZinsenIDXstart = AccountDetailResponse.indexOf('<span',ZinsenIDXstart); // Ermittle die Position (Index) des Anfangs der Daten
					Logger.debug(LogIdent+"ZinsenIDXstart: " +ZinsenIDXstart);
					var ZinsenIDXend = AccountDetailResponse.indexOf("</span>", ZinsenIDXstart)+7; // Ermittle die Position (Index) des Endes der Daten
					Logger.debug(LogIdent+"ZinsenIDXend: " +ZinsenIDXend);
					var ZinsenText = AccountDetailResponse.substring(ZinsenIDXstart, ZinsenIDXend); // Hole den String vom Index1 bis Index2
					Logger.debug(LogIdent+"ZinsenText: " +ZinsenText);

					// Abrechnung auslesen
					var AbrechnungIDXstart = AccountDetailResponse.indexOf('Abrechnung zum Laufzeitende:'); // Ermittle die Position (Index) des Anfangs der Daten
					var AbrechnungIDXstart = AccountDetailResponse.indexOf('<span',AbrechnungIDXstart); // Ermittle die Position (Index) des Anfangs der Daten
					Logger.debug(LogIdent+"AbrechnungIDXstart: " +AbrechnungIDXstart);
					var AbrechnungIDXend = AccountDetailResponse.indexOf("</span>", AbrechnungIDXstart)+7; // Ermittle die Position (Index) des Endes der Daten
					Logger.debug(LogIdent+"AbrechnungIDXend: " +AbrechnungIDXend);
					var AbrechnungText = AccountDetailResponse.substring(AbrechnungIDXstart, AbrechnungIDXend); // Hole den String vom Index1 bis Index2
					// Logger.debug(LogIdent+"AbrechnungText: " +AbrechnungText);

					// Referenznr auslesen
					var ReferenznrIDXstart = AccountDetailResponse.indexOf('Referenznr.:'); // Ermittle die Position (Index) des Anfangs der Daten
					var ReferenznrIDXstart = AccountDetailResponse.indexOf('<span',ReferenznrIDXstart); // Ermittle die Position (Index) des Anfangs der Daten
					Logger.debug(LogIdent+"ReferenznrIDXstart: " +ReferenznrIDXstart);
					var ReferenznrIDXend = AccountDetailResponse.indexOf("</span>", ReferenznrIDXstart)+7; // Ermittle die Position (Index) des Endes der Daten
					Logger.debug(LogIdent+"ReferenznrIDXend: " +ReferenznrIDXend);
					var ReferenznrText = AccountDetailResponse.substring(ReferenznrIDXstart, ReferenznrIDXend); // Hole den String vom Index1 bis Index2
					// Logger.debug(LogIdent+"ReferenznrText: " +ReferenznrText);

					// Tagesgeldkonto auslesen
					var TagesgeldkontoIDXstart = AccountDetailResponse.indexOf('bertrag auf Tagesgeldkonto:'); // Ermittle die Position (Index) des Anfangs der Daten
					var TagesgeldkontoIDXstart = AccountDetailResponse.indexOf('<span',TagesgeldkontoIDXstart); // Ermittle die Position (Index) des Anfangs der Daten
					Logger.debug(LogIdent+"TagesgeldkontoIDXstart: " +TagesgeldkontoIDXstart);
					var TagesgeldkontoIDXend = AccountDetailResponse.indexOf("</span>", TagesgeldkontoIDXstart)+7; // Ermittle die Position (Index) des Endes der Daten
					Logger.debug(LogIdent+"TagesgeldkontoIDXend: " +TagesgeldkontoIDXend);
					var TagesgeldkontoText = AccountDetailResponse.substring(TagesgeldkontoIDXstart, TagesgeldkontoIDXend); // Hole den String vom Index1 bis Index2
					// Logger.debug(LogIdent+"TagesgeldkontoText: " +TagesgeldkontoText);

					
					// nun noch alles formatieren
					var Beginndatum = HibiscusScripting_MoneYou_stripHTML(String(BeginndatumText));
					Logger.debug(LogIdent+"Festgeld-Kontodetails-Informationen (Beginndatum): " +Beginndatum);
					var Enddatum = HibiscusScripting_MoneYou_stripHTML(String(EnddatumText));
					Logger.debug(LogIdent+"Festgeld-Kontodetails-Informationen (Enddatum): " +Enddatum);
					if (RunArt == "inactive") { 
						var Canceldatum = HibiscusScripting_MoneYou_stripHTML(String(CanceldatumText));
						Logger.debug(LogIdent+"Festgeld-Kontodetails-Informationen (Canceldatum): " +Canceldatum);
					};
					var Zinssatz = HibiscusScripting_MoneYou_stripHTML(String(ZinssatzText));
					Logger.debug(LogIdent+"Festgeld-Kontodetails-Informationen (Zinssatz): " +Zinssatz);
					var Betrag = HibiscusScripting_MoneYou_stripHTML(String(BetragText));
					Logger.debug(LogIdent+"Festgeld-Kontodetails-Informationen (Betrag): " +Betrag);
					var Zinsen = HibiscusScripting_MoneYou_stripHTML(String(ZinsenText));
					Logger.debug(LogIdent+"Festgeld-Kontodetails-Informationen (Zinsen): " +Zinsen);
					var Abrechnung = String(org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4(new java.lang.String(HibiscusScripting_MoneYou_stripHTML(String(AbrechnungText)))));
					Logger.debug(LogIdent+"Festgeld-Kontodetails-Informationen (Abrechnung): " +Abrechnung);
					var Referenznr = HibiscusScripting_MoneYou_stripHTML(String(ReferenznrText));
					Logger.debug(LogIdent+"Festgeld-Kontodetails-Informationen (Referenznr): " +Referenznr);
					var Tagesgeldkonto = HibiscusScripting_MoneYou_stripHTML(String(TagesgeldkontoText));
					Logger.debug(LogIdent+"Festgeld-Kontodetails-Informationen (Tagesgeldkonto): " +Tagesgeldkonto);


					// Ermittelte Daten in ein Array schreiben
					AccountList[fCount] = new Array();
					AccountList[fCount][0] = Beginndatum;
					AccountList[fCount][1] = Enddatum;
					AccountList[fCount][2] = Zinssatz;
					AccountList[fCount][3] = Betrag;
					AccountList[fCount][4] = Zinsen;
					AccountList[fCount][5] = Abrechnung;
					AccountList[fCount][6] = Referenznr;
					AccountList[fCount][7] = Tagesgeldkonto;
					AccountList[fCount][8] = RunArt;
					if (RunArt == "inactive") { AccountList[fCount][9] = Canceldatum; };

					var fCount = fCount + 1;
				};

				if (RunArt == "active") { var RunArt = "inactive"; }
				else if (RunArt == "inactive") { var RunArt = "done"; };


			} while (RunArt != "done");



			Logger.debug(LogIdent+"Daten von '"+AccountList.length+"' Festgeld-Anlagen werden nun verarbeitet ...");
			var newKontos = 0;
			var oldKontos = 0;
			var dataActive = 0;
			var dataInactive = 0;

			for (var i = 0; i < AccountList.length; i++) {

				var KontoExist = false;
				var KontoChange = false;

				// Daten für das Konto aufbereiten
				var arraydatum = AccountList[i][0].split(".");
				var Datum = new java.util.Date((parseInt(arraydatum[2],10) - 1900),(parseInt(arraydatum[1],10) - 1),parseInt(arraydatum[0],10));
				if (AccountList[i][8] == "inactive") {
					var arraydatum = AccountList[i][9].split(".");
					var endDatum = new java.util.Date((parseInt(arraydatum[2],10) - 1900),(parseInt(arraydatum[1],10) - 1),parseInt(arraydatum[0],10));
				};
				var betragvalue = parseFloat(AccountList[i][3].replace(/\./, "").replace(/,/, ".").replace(/ EUR/, ""));
				var kontonr = String(AccountList[i][7].split(" ").join("")).substring(12,22);


				if (Java8upFrame == true) {
					var list = db.createList(java.lang.Class.forName("de.willuhn.jameica.hbci.rmi.Konto"));
				} else {
					var list = db.createList(Konto);
				};

				while(list.hasNext()) {
					var festkonto = list.next();
					
					if (	(String(festkonto.getBLZ()) == HibiscusScripting_MoneYou_BLZ)
					     && (String(festkonto.getKundennummer()) == String(AccountList[i][6]))
					     && (String(festkonto.getUnterkonto()) == "Festgeld")
					   ) {
						var KontoExist = true; // Ein Offline-Festgeldkonto für die MoneYou existiert bereits

						var EndeCanelDateString = "Enddatum: " + AccountList[i][1] + "\n";
						if (AccountList[i][8] == "inactive") { var EndeCanelDateString = EndeCanelDateString + "\nEnddatum: " + AccountList[i][9] + "\n"; };

						if (festkonto.getFlags() == 2) {
							
							var firstSaldo = festkonto.getSaldo();
							
							festkonto.setSaldo(betragvalue);		// Der Betrag der Anlage
							festkonto.setSaldoAvailable(betragvalue);	// Verfügbarer Betrag der Anlage
							festkonto.setKommentar("Aufgelaufene Zinsen: " + AccountList[i][4] + "  \n\n"
									     + "Kontodetail-Informationen Festgeld:\n"
									     + "-----------------------------------\n"
									     + "Beginndatum: " + AccountList[i][0] + "\n"
									     + EndeCanelDateString
									     + "Zinssatz: " + AccountList[i][2] + "\n"
									     + "Betrag: " + AccountList[i][3] + "\n"
									     + "Abrechnung zum Laufzeitende : " + AccountList[i][5] + "\n"
									     + "Referenznr.: " + AccountList[i][6] + "\n"
									     + "\u00dcbertrag auf Tagesgeld: " + AccountList[i][7]);
							festkonto.store();
							
							// Wenn das Festgeld bei der MoneYou automatisch verlängert wurde ist dann im Konto eventuell ein Umsatz für eine Zinsgutschrift anzulegen
							if (firstSaldo < betragvalue) { 
								
								// Umsatz anlegen
								if (Java8upFrame == true) {
									var umsatz = db.createObject(java.lang.Class.forName("de.willuhn.jameica.hbci.rmi.Umsatz"),null);
								} else {
									var umsatz = db.createObject(Umsatz,null);
								};
								umsatz.setKonto(festkonto);

								umsatz.setDatum(Datum); // Datum
								umsatz.setValuta(Datum); // Valuta
								umsatz.setBetrag(betragvalue - firstSaldo); // Betrag
								umsatz.setSaldo(betragvalue); // Saldo
								umsatz.setArt("Zinszahlung Festgeld"); // Umsatzart
								umsatz.setZweck("Zinszahlung Festgeld"); // Verwendungszweck
								umsatz.setPrimanota(AccountList[i][6]); // Primanota
								umsatz.setGegenkontoName(konto.getName()); // GegenkontoName
								umsatz.setGegenkontoNummer(kontonr); // GegenkontoNummer
								umsatz.setGegenkontoBLZ(HibiscusScripting_MoneYou_BLZ); // GegenkontoBLZ

								// Umsatz speichern
								Logger.info(LogIdent+"Speichere Umsatz f\u00fcr aufgelaufene Zinsen im Festgeld ...");
								umsatz.store();

								// Live-Aktualisierung der Umsatz-Liste
								if ((MoneYou_NewSyncActive == true) && (MoneYou_fetchUmsatz == true)) { Application.getMessagingFactory().sendMessage(new ImportMessage(umsatz)); };

								// Saldo des Konto aktualisieren
								festkonto.setSaldo(betragvalue);		// Der Betrag der Anlage
								festkonto.setSaldoAvailable(betragvalue);	// Verfügbarer Betrag der Anlage

								festkonto.store();
							};
							
							if (AccountList[i][8] == "active") { var dataActive = dataActive + 1; }
							else if (AccountList[i][8] == "inactive") { var dataInactive = dataInactive + 1; };
							
						};
						// Keine automatischen Gegenbuchungen
						var sync = new SynchronizeOptions(festkonto);
						sync.setSyncOffline(false);

						Logger.debug(LogIdent+"Konto '"+String(festkonto.getKundennummer())+"' hat die Flags: '"+festkonto.getFlags()+"'");
						
						// Wenn das Festgeld bei der MoneYou nun ausgelaufen ist dann im Konto nun den Abschlussumsatz anlegen und das Konto deaktivieren
						if ((festkonto.getFlags() == 2) && (AccountList[i][8] == "inactive")) {
							
							// Umsatz anlegen
							if (Java8upFrame == true) {
								var umsatz = db.createObject(java.lang.Class.forName("de.willuhn.jameica.hbci.rmi.Umsatz"),null);
							} else {
								var umsatz = db.createObject(Umsatz,null);
							};
							umsatz.setKonto(festkonto);

							umsatz.setDatum(endDatum); // Datum
							umsatz.setValuta(endDatum); // Valuta
							umsatz.setBetrag(betragvalue * -1); // Betrag
							umsatz.setSaldo(0); // Saldo
							umsatz.setArt("R\u00fcckzahlung Festgeldbetrag/ Zinsen"); // Umsatzart
							umsatz.setZweck("R\u00fcckzahlung Festgeldbetrag/ Zinsen"); // Verwendungszweck
							umsatz.setPrimanota(AccountList[i][6]); // Primanota
							umsatz.setGegenkontoName(konto.getName()); // GegenkontoName
							umsatz.setGegenkontoNummer(kontonr); // GegenkontoNummer
							umsatz.setGegenkontoBLZ(HibiscusScripting_MoneYou_BLZ); // GegenkontoBLZ

							// Umsatz speichern
							Logger.info(LogIdent+"Speichere Umsatz wegen ausgelaufenen Festgeld (R\u00fcckzahlung Festgeldbetrag/ Zinsen) ...");
							umsatz.store();

							// Live-Aktualisierung der Umsatz-Liste
							if ((MoneYou_NewSyncActive == true) && (MoneYou_fetchUmsatz == true)) { Application.getMessagingFactory().sendMessage(new ImportMessage(umsatz)); };

							// Saldo des Konto auf Null setzen
							festkonto.setSaldo(0);		// Der Betrag der Anlage
							festkonto.setSaldoAvailable(0);	// Verfügbarer Betrag der Anlage

							// Das Konto ist bei MoneYou abgelaufen und wird somit in Hibiscus deaktiviert
							festkonto.setFlags(Konto.FLAG_OFFLINE+Konto.FLAG_DISABLED);

							festkonto.store();

							var oldKontos = oldKontos + 1;
						};
					}; 
				};


				if ((KontoExist == false) && (AccountList[i][8] != "inactive")) {

					//*******************************************************************************
					// Erzeugen eines Offline-Kontos für Festgeld für die MoneYou (ABN AMRO Bank)
					//*******************************************************************************
					if (Java8upFrame == true) {
						var festkonto = db.createObject(java.lang.Class.forName("de.willuhn.jameica.hbci.rmi.Konto"), null);
					} else {
						var festkonto = db.createObject(Konto, null);
					};
					festkonto.setBezeichnung("MoneYou - Festgeld");
					festkonto.setKundennummer(AccountList[i][6]);			// die Referenznummer der Gegenkonto
					festkonto.setKontonummer(kontonr);  				// Ist Konto-Nummer des Rückzahlungskontos
					festkonto.setBLZ(HibiscusScripting_MoneYou_BLZ); 		// BLZ der 'MoneYou'
					festkonto.setUnterkonto("Festgeld");				// Als Merker
					festkonto.setName(konto.getName());				// Ist der vollständige Name den der Benutzer eingegeben hat
					festkonto.setSaldo(betragvalue);				// Der Betrag der Anlage
					festkonto.setSaldoAvailable(betragvalue);			// Verfügbarer Betrag der Anlage
					festkonto.setFlags(Konto.FLAG_OFFLINE);              		// Sollte ein Offline-Konto sein
					festkonto.setKommentar("Aufgelaufene Zinsen: " + AccountList[i][4] + "  \n\n"
							     + "Kontodetail-Informationen Festgeld:\n"
							     + "-----------------------------------\n"
							     + "Beginndatum: " + AccountList[i][0] + "\n"
							     + "Enddatum: " + AccountList[i][1] + "\n"
							     + "Zinssatz: " + AccountList[i][2] + "\n"
							     + "Betrag: " + AccountList[i][3] + "\n"
							     + "Abrechnung zum Laufzeitende : " + AccountList[i][5] + "\n"
							     + "Referenznr.: " + AccountList[i][6] + "\n"
							     + "\u00dcbertrag auf Tagesgeld: " + AccountList[i][7]);
					festkonto.store();

					// Keine automatischen Gegenbuchungen
					var sync = new SynchronizeOptions(festkonto);
					sync.setSyncOffline(false);
					
					// Auslesen der neuen Option ob der Saldo durch Hibiscus berechnet wird oder nicht
					try {
						MoneYou_autoSaldo = sync.getAutoSaldo();
						var AutoSaldoExist = true;
					
					} catch(err) {
						if (sync.getSyncSaldo() == true) { MoneYou_autoSaldo = false; }
						else if (sync.getSyncSaldo() == false) { MoneYou_autoSaldo = true; };
						var AutoSaldoExist = false;
					};
					// und setzen dass für dieses Konto kein Saldo durch Hibiscus berechnet wird
					if ((MoneYou_autoSaldo != false) && (AutoSaldoExist == true)) { 
						sync.setAutoSaldo(false);
						Logger.info(LogIdent+"Berechnung des Saldo durch Hibiscus wurde nun ausgeschaltet ist nun also: " + sync.getAutoSaldo());
					};
					

					// Umsatz anlegen
					if (Java8upFrame == true) {
						var umsatz = db.createObject(java.lang.Class.forName("de.willuhn.jameica.hbci.rmi.Umsatz"),null);
					} else {
						var umsatz = db.createObject(Umsatz,null);
					};
					umsatz.setKonto(festkonto);

					umsatz.setDatum(Datum); // Datum
					umsatz.setValuta(Datum); // Valuta
					umsatz.setBetrag(betragvalue); // Betrag
					umsatz.setSaldo(betragvalue); // Saldo
					umsatz.setArt("Abschluss eines Festgeldes"); // Umsatzart
					umsatz.setZweck("Abschluss eines Festgeldes"); // Verwendungszweck
					umsatz.setPrimanota(AccountList[i][6]); // Primanota
					umsatz.setGegenkontoName(konto.getName()); // GegenkontoName
					umsatz.setGegenkontoNummer(kontonr); // GegenkontoNummer
					umsatz.setGegenkontoBLZ(HibiscusScripting_MoneYou_BLZ); // GegenkontoBLZ

					// Umsatz speichern
					Logger.info(LogIdent+"Speichere Umsatz aus der Festgeld-Detailansicht ...");
					umsatz.store();

					// Live-Aktualisierung der Umsatz-Liste
					if ((MoneYou_NewSyncActive == true) && (MoneYou_fetchUmsatz == true)) { Application.getMessagingFactory().sendMessage(new ImportMessage(umsatz)); };					
					//*******************************************************************************

					var newKontos = newKontos + 1;
					var dataActive = dataActive + 1;
				
				};
			};


			Logger.info(LogIdent+"Daten von '"+ArtActive+"' aktive und '"+ArtInactive+"' inaktive Festgeld der MoneYou wurden verarbeitet");
			Logger.info(LogIdent+"Hibiscus Offline-Konten f\u00fcr '"+dataActive+"' aktive und '"+dataInactive+"' inaktive Festgeld der MoneYou wurden aktualisiert");

			if (newKontos > 0) {
				Logger.info(LogIdent+"Dabei/Davon wurden '"+newKontos+"' neue aktive Festgeld der MoneYou als Offline-Konto angelegt");
				Application.getCallback().notifyUser("\nHibiscus-Offline-Konten f\u00fcr '"+newKontos+"' neue aktive Festgeld der MoneYou wurden angelegt  \n\n");
			};
			if (oldKontos > 0) {
				Logger.info(LogIdent+"Es wurden '"+oldKontos+"' Offline-Konten wegen ausgelaufene Festgeld-Anlagen der MoneYou in Hibiscus deaktiviert");
				Application.getCallback().notifyUser("\nEs wurden '"+oldKontos+"' Offline-Konten wegen ausgelaufene Festgeld-Anlagen der MoneYou in Hibiscus deaktiviert  \n\n");
			};

			
			return DataPage;

			
		} else {
			Logger.info(LogIdent+"Routine zum Anlegen von Festgeldkonten wurde vom Benutzer mit NEIN beantwortet oder die Routine wurde deaktiviert");

			return page;
		};


		MoneYou_FixMoney_Checked = true;	

	} else {

		return page;
	};
		
};





function HibiscusScripting_MoneYou_getTransData(konto, webClient, monitor) {
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
		Logger.debug(LogIdent+"GET " +AccountsListURL);
		try {		
			var DataPage = AccountsListURL.click();
			Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getTransData: AccountsListURL DataPage (Seite): " +DataPage);
			
		} catch(err) {
			InputError = 2;
			throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
		};
		if (!DataPage) { throw "Die Umsatz\u00fcbersicht konnte nicht aufgerufen werden!"; };
		var DataPageXML = DataPage.asXml();	
		//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getTransData: DataPageXML (Seite): \n" +DataPageXML); // gibt die ganze Seite aus, also ganz schön viel
		
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
			Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getTransData: form: " +form);
			var Anzeigen = form.getInputByName("btnNext");
			
		} catch(err) {
			InputError = 2;
			throw "Fehler beim setzen des Formulars zum CSV-Abruf (siehe Log - Bitte den Entwickler im Forum informieren)\nLog-Eintrag: " +err;
		};	
		//*********************************************************************************			

		//*********************************************************************************
		// Übersicht erweitern, liefert die Ergebnisseite oder dessen Fehlermeldung zurück
		//*********************************************************************************
		Logger.debug(LogIdent+"GET " +Anzeigen);
		try {		
			var DataPage = Anzeigen.click();
			Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getTransData: Anzeigen DataPage (Seite): " +DataPage);
			
		} catch(err) {
			InputError = 2;
			throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
		};
		if (!DataPage) { throw "Die Umsatz\u00fcbersicht konnte nicht erweitert werden!"; };
		var DataPageXML = DataPage.asXml();
		//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getTransData: DataPageXML (Seite): \n" +DataPageXML); // gibt die ganze Seite aus, also ganz schön viel
		
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
			Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getTransData: form: " +form);
			var Buchungen = form.getInputByName("downloadStatementsLinkButton");
			
		} catch(err) {
			InputError = 2;
			throw "Fehler beim setzen des Formulars zum CSV-Abruf (siehe Log - Bitte den Entwickler im Forum informieren)\nLog-Eintrag: " +err;
		};	
		//*********************************************************************************			

		//*********************************************************************************
		// Übersicht erweitern, liefert die Ergebnisseite oder dessen Fehlermeldung zurück
		//*********************************************************************************
		Logger.debug(LogIdent+"GET " +Buchungen);
		try {		
			var DataPage = Buchungen.click();
			Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getTransData: Buchungen DataPage (Seite): " +DataPage);
			
		} catch(err) {
			InputError = 2;
			throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
		};
		if (!DataPage) { throw "Die Umsatz-Downloadseite konnte nicht aufgerufen werden!"; };
		var DataPageXML = DataPage.asXml();
		//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getTransData: DataPageXML (Seite): \n" +DataPageXML); // gibt die ganze Seite aus, also ganz schön viel
		
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
			Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getTransData: form: " +form);
			var kontoselect = form.getSelectByName("accountNumber");
			Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getTransData: kontoselect: " +kontoselect);
			
		} catch(err) {
			InputError = 2;
			throw "Fehler beim setzen der Kontoauswahl in der Umsatz\u00fcbersicht (siehe Log - Bitte den Entwickler im Forum informieren)\nLog-Eintrag: " +err;
		};	
		//*********************************************************************************			

		//*********************************************************************************
		// Auswahl treffen für das Konto in der Umsatzübersicht
		//*********************************************************************************			
		// die notwendige Kontonummer
		var kontonr = String(konto.getKontonummer());
		var selectKonto = false;

		// Konto auswählen ...
		var list = kontoselect.getOptions();
		for (var i = 0; i < list.size(); i++) {
			var selectOption = list.get(i);
			//Logger.debug(LogIdent+"Listeneintrag der Kontoauswahl gefunden: " +selectOption);
			//Logger.debug(LogIdent+"dieser Eintrag als reiner Text: " +String(selectOption.asText()));
			if (selectOption.getValueAttribute().contains(kontonr)) {
				//Logger.debug(LogIdent+"Kontoauswahl auf "+selectOption);
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
		//Logger.debug(LogIdent+"Die Kontoauswahl aufgeteilt nach Leerzeichen: "+AccountInfos);
		var SaldoFound = false;

		for (var i = 0; i < AccountInfos.length; i++) {
			if (AccountInfos[i].indexOf(",") != -1) {
				//Logger.debug(LogIdent+"Saldo gefunden: "+AccountInfos[i]);
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
		var lastCall = konto.getSaldoDatum();		// letzte Kontoaktualisierung
		var fromDate = new java.util.Date();         	// Abrufdatum
		var DateNow  = new java.util.Date();         	// Aktuelles Datum
		
		var umsaetze = konto.getUmsaetze();
		var UmsatzZahl = umsaetze.size();

		if (!lastCall || (UmsatzZahl == 0)) {
			// Noch kein Kontenabruf, bestimmte Zeitspanne in die Vergangenheit
			fromDate = new java.util.Date((DateNow.getTime()-31104000000)); // 1 Jahr ist z. B. 31536000000; 12 Wochen sind z. B. 7257600000
			monitor.log("Sie benutzen dieses MoneYou-Konto das erste Mal in Hibiscus oder der Saldo und Datum wurde zur\u00fcckgesetzt ...");
			monitor.log("... es werden nun alle Ums\u00E4tze abgeholt ...");
			Logger.info(LogIdent+"Sie benutzen dieses MoneYou-Konto das erste Mal in Hibiscus oder der Saldo und Datum wurde zur\u00fcckgesetzt ...");
			Logger.info(LogIdent+"... es werden nun alle Ums\u00E4tze abgeholt ...");

		} else {
			Logger.debug(LogIdent+"Letztes Abrufdatum: " + lastCall.getDate()+"."+(lastCall.getMonth() + 1)+"."+(lastCall.getYear()+1900));
			fromDate = new java.util.Date((lastCall.getTime()-1209600000)); // 14 Tage mehr abrufen, Überschneidungen findet der Doppel-Check
		};

		// Umsatz-Cache aktualisieren für den späteren Abgleich, zur Sicherheit ein paar Tage mehr als nötig...
		HibiscusScripting_MoneYou_refreshHibiscusUmsaetze(konto, (parseInt(((DateNow.getTime() - fromDate.getTime()) / 86400000),10) + 300));

		// Tag und Monat muss 2-stellig sein
		var fromDateDay = String (fromDate.getDate()); fromDateDay = ((fromDateDay < 10) ? "0" + fromDateDay : fromDateDay);
		var fromDateMonth = String ((fromDate.getMonth() + 1)); fromDateMonth = ((fromDateMonth < 10) ? "0" + fromDateMonth : fromDateMonth);
		var DateNowDay  = String (DateNow.getDate()); DateNowDay = ((DateNowDay < 10) ? "0" + DateNowDay : DateNowDay);
		var DateNowMonth  = String ((DateNow.getMonth() + 1)); DateNowMonth = ((DateNowMonth < 10) ? "0" + DateNowMonth : DateNowMonth);

		Logger.debug(LogIdent+"Rufe Ums\u00e4tze vom "+fromDateDay+"."+fromDateMonth+"."+(fromDate.getYear()+1900)+" bis "+DateNowDay+"."+DateNowMonth+"."+(DateNow.getYear()+1900)+" ab ...");
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
			Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getTransData: DataPage (Suche): " +DataPage);
			
		} catch(err) {
			InputError = 2;
			throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
		};
		if (!DataPage) { throw "Die Suche-Folgeseite konnte nicht aufgerufen werden!"; };
		var DataPageXML = DataPage.asXml();
		//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getTransData: DataPageXML (Suche): \n" +DataPageXML); // gibt die ganze Seite aus, also ganz schön viel
		
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
		//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getTransData: DataPageResponse (Suche): \n" +DataPageResponse); // gibt die ganze Seite aus, also ganz schön viel

		if (DataPageResponse.contains('totalMovements">0<')) {
			var csvResponse = String("Keine (neuen) Ums\u00e4tze vorhanden (da drei Platzhalter im Download-Formular)");
			
		} else {
		
			//*********************************************************************************
			// Setzen des Formulars für den CSV-Abruf
			//*********************************************************************************			
			try {
				var form = DataPage.getFormByName("DownloadMovementForm");			
				Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getTransData: form: " +form);
				var CSVButton = form.getInputByValue("herunterladen");
				
			} catch(err) {
				InputError = 2;
				throw "Fehler beim setzen des Formulars zum CSV-Abruf (siehe Log - Bitte den Entwickler im Forum informieren)\nLog-Eintrag: " +err;
			};	
			//*********************************************************************************			

			//*********************************************************************************
			// CSV-Export holen, liefert die Ergebnisseite oder dessen Fehlermeldung zurück
			//*********************************************************************************
			Logger.debug(LogIdent+"CSV laden: Click "+CSVButton);
			try {		
				csv = CSVButton.click();
				
			} catch(err) {
				InputError = 2;
				throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
			};
			if (!csv) { throw "Der Kontoauszug (CSV) konnte nicht aufgerufen werden!"; };
			Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getTransData: csv: " +csv);
			var csvResponse = csv.getWebResponse().getContentAsString();
			//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_getTransData: csvResponse: \n" +csvResponse); // gibt die ganze Seite aus, also ganz schön viel
			
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





function HibiscusScripting_MoneYou_syncDataAndAccount(data, amount, konto, monitor) {
/*******************************************************************************
 * Erzeugen von Umsätzen aus dem Kontoauszug, Prüfung und Speicherung
 *******************************************************************************/

	Logger.debug(LogIdent+"Funktion HibiscusScripting_MoneYou_syncDataAndAccount wurde aufgerufen ...");
	
	
	// CSV-Ergebnisliste in ein Array umwandeln
	var CSVDataRow = HibiscusScripting_MoneYou_Data2Array(data, ";");
	//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_syncDataAndAccount Ergebnis:\n" +CSVDataRow);
	
	// Datenbank-Verbindung holen
	if (Java8upFrame == true) {
		var db = Application.getServiceFactory().lookup(java.lang.Class.forName("de.willuhn.jameica.hbci.HBCI"),"database");
	} else {
		var db = Application.getServiceFactory().lookup(HBCI,"database");
	};
	
	// Variablen für Checks der Umsatzverarbeitung
	var saldo = amount;
	var UmsatzAnzahl = 0;
	var NewUmsatzCount = 0;
	var OldUmsatzCount = 0;
	var SaldoInfo = false;
	var newSaldo = false;
	var givenSaldoGet = false;
	var givenKontoSaldo = Math.round(konto.getSaldo() * 100) / 100;
	var lastCall = konto.getSaldoDatum(); 	// letzte Kontoaktualisierung
	var LastUmsatzDatum = new java.util.Date((1931 - 1900),0,1); // Variable setzen für den Fall dass noch keine Umsätze vorhanden sind
	var MetaIdentifier = HibiscusScripting_MoneYou_makeHash(konto.getKontonummer()+"-"+konto.getKundennummer()+"-"+konto.getUnterkonto());



	//*******************************************************************************
	// Auslesen des Kontoauszug-Saldos (wird mindestens von einem der beiden Fälle benötigt)
	//*******************************************************************************
	var givenSaldo = Math.round(saldo * 100) / 100;
	Logger.debug(LogIdent+"Saldo der Homepage: " + givenSaldo);
	givenSaldoGet = true;
	//*******************************************************************************
	

	
	//*******************************************************************************
	// Informationen über bestehende Umsätze lesen
	//*******************************************************************************
	// letzen Umsatz lesen um zwischen alten und neuen Umsätzen zu unterscheiden (und benötigt für eventuelle erweiterte Zwischensaldo-Berechnung)
	var lastumsaetze = konto.getUmsaetze();
	var lastUmsatzZahl = parseInt(lastumsaetze.size(),10);
	if (lastUmsatzZahl != 0) {
		var lastumsatz = lastumsaetze.next();
		var lastUmsatzDateArray = String(lastumsatz.getDatum()).split("-");
		var LastUmsatzDatum = new java.util.Date((parseInt(lastUmsatzDateArray[0],10) - 1900),(parseInt(lastUmsatzDateArray[1],10) - 1),parseInt(lastUmsatzDateArray[2],10));
		Logger.debug(LogIdent+"Datum des letzten Umsatzes: " + LastUmsatzDatum);
		var LastUmsatzBetrag = Math.round(lastumsatz.getBetrag() * 100) / 100;
		Logger.debug(LogIdent+"Betrag des letzten Umsatzes: " + LastUmsatzBetrag);
		var LastUmsatzSaldo = Math.round(lastumsatz.getSaldo() * 100) / 100;
		Logger.debug(LogIdent+"Saldo des letzten Umsatzes: " + LastUmsatzSaldo);
	};
	//*******************************************************************************



	//*******************************************************************************
	// Verarbeiten der Umsätze aus dem Kontoauszug
	//*******************************************************************************
	// Abfrage ob neuer Sync aktiv und ob dort "Kontoauszüge (Umsätze) abrufen" aktiviert ist wegen Kontoauszugsverarbeitung
	if ((MoneYou_NewSyncActive == false) || ((MoneYou_NewSyncActive == true) && (MoneYou_fetchUmsatz == true))) {

		// Auslesen aller Buchungen und zu einem sauberen Array zusammenbauen dass nur fertig formatierte Umsätze enhält für die spätere Verarbeitung und Nutzung
		var TransactionData = new Array();
		var ArrayCount = 0;
		
		for (var i = 0; i < CSVDataRow.length; i++) {
			
			//Logger.debug(LogIdent+"Umsatzverarbeitungsschleife: CSVDataRow["+i+"][0]: " + CSVDataRow[i][0]);
			// Prüfen ob die Zeile leer ist dann weiter mit der nächsten
			if ((CSVDataRow[i] == undefined) || (CSVDataRow[i][0] == undefined) || (!CSVDataRow[i][0])) { continue; };
			//Logger.debug(LogIdent+"Umsatzverarbeitungsschleife: wurde beachtet ...");
			
			// Buchungen fangen mit einer laufenden Nummer an:
			if (isFinite(CSVDataRow[i][0])) {
				
				TransactionData[ArrayCount] = new Array();
				
				/////// Buchungsdatum [0] ///////
				var arraydatum = CSVDataRow[i][1].split(".");
				TransactionData[ArrayCount][0] = new java.util.Date((parseInt(arraydatum[2],10) - 1900),(parseInt(arraydatum[1],10) - 1),parseInt(arraydatum[0],10));
				//Logger.debug(LogIdent+"TransactionData["+ArrayCount+"][0] hat folgenden Wert: (Datum): "+TransactionData[ArrayCount][0]);

				/////// Valuta-Datum [1] ///////
				var arrayvaluta = CSVDataRow[i][5].split(".");
				TransactionData[ArrayCount][1] = new java.util.Date((parseInt(arrayvaluta[2],10) - 1900),(parseInt(arrayvaluta[1],10) - 1),parseInt(arrayvaluta[0],10));
				//Logger.debug(LogIdent+"TransactionData["+ArrayCount+"][1] hat folgenden Wert: (Valuta): "+TransactionData[ArrayCount][1]);

				/////// Betrag [2] ///////
				TransactionData[ArrayCount][2] = parseFloat(CSVDataRow[i][3].replace(/\./g,"").replace(/\,/, "."));
				//Logger.debug(LogIdent+"TransactionData["+ArrayCount+"][2] hat folgenden Wert: (Betrag): "+TransactionData[ArrayCount][2]);

				/////// Saldo [3] ///////
				TransactionData[ArrayCount][3] = 0; // noch nicht bekannt, muss erst noch berechnet werden
				//Logger.debug(LogIdent+"TransactionData["+ArrayCount+"][3] hat folgenden Wert: (Saldo): "+TransactionData[ArrayCount][3]);

				/////// Umsatzart [4] ///////
				TransactionData[ArrayCount][4] = CSVDataRow[i][2];
				//Logger.debug(LogIdent+"TransactionData["+ArrayCount+"][4] hat folgenden Wert: (Art): "+TransactionData[ArrayCount][4]);

				/////// Verwendungszweck [5] ///////
				// Verwendungstext-String zusammenbauen
				var PurpRawString = String(CSVDataRow[i][8]) + " " + String(CSVDataRow[i][9]);
				var PurpString = PurpRawString; //.split("-").join("\u00c4").split("_").join("\u00dc").split("+").join("\u00d6");
				TransactionData[ArrayCount][5] = HibiscusScripting_MoneYou_removeWhitespace(PurpString);
				if (!TransactionData[ArrayCount][5] || (TransactionData[ArrayCount][5] == null) || (TransactionData[ArrayCount][5] == "") || (TransactionData[ArrayCount][5] == " ") || (TransactionData[ArrayCount][5] == "undefined")) {
					TransactionData[ArrayCount][5] = TransactionData[ArrayCount][4];
				};
				//Logger.debug(LogIdent+"TransactionData["+ArrayCount+"][5] hat folgenden Wert: (Zweck): "+TransactionData[ArrayCount][5]);

				/////// Primanotakennzeichen [6] ///////
				TransactionData[ArrayCount][6] = CSVDataRow[i][10];
				//Logger.debug(LogIdent+"TransactionData["+ArrayCount+"][6] hat folgenden Wert: (Primanota): "+TransactionData[ArrayCount][6]);

				/////// GegenkontoName [7], GegenkontoNummer [8], GegenkontoBLZ [9] ///////
				TransactionData[ArrayCount][7] = CSVDataRow[i][7]; //.split("-").join("\u00c4").split("_").join("\u00dc").split("+").join("\u00d6");
				if (new java.lang.String(CSVDataRow[i][6]).contains(" ")) {
					GegenkontoArray = CSVDataRow[i][6].split(" ");
					TransactionData[ArrayCount][8] = GegenkontoArray[1];
					TransactionData[ArrayCount][9] = GegenkontoArray[0];
					
				};/* else if (new java.lang.String(CSVDataRow[i][6]).contains(" ")) { 
					var IBAN = CSVDataRow[i][6].split(" ").join("");
					//Logger.debug(LogIdent+"CSVDataRow["+i+"][6] hat folgenden Wert ohne Leerzeichen: (IBAN): "+IBAN);
					TransactionData[ArrayCount][8] = IBAN.substring(12,22);
					TransactionData[ArrayCount][9] = IBAN.substring(4,12);
				};*/
				//Logger.debug(LogIdent+"TransactionData["+ArrayCount+"][7] hat folgenden Wert: (GegenkontoName): "+TransactionData[ArrayCount][7]);
				//Logger.debug(LogIdent+"TransactionData["+ArrayCount+"][8] hat folgenden Wert: (GegenkontoNummer): "+TransactionData[ArrayCount][8]);
				//Logger.debug(LogIdent+"TransactionData["+ArrayCount+"][9] hat folgenden Wert: (GegenkontoBLZ): "+TransactionData[ArrayCount][9]);

				/////// Kommentar Notiz [10] ///////
				if (CSVDataRow[i][4] != "EUR") { TransactionData[ArrayCount][10] = "W\u00e4hrung: " + CSVDataRow[i][4]; };
				//Logger.debug(LogIdent+"TransactionData["+ArrayCount+"][10] hat folgenden Wert: (Notiz): "+TransactionData[ArrayCount][10]);

				/////// Zeilennummer [11] ///////
				TransactionData[ArrayCount][11] = i + 1;
				//Logger.debug(LogIdent+"TransactionData["+ArrayCount+"][11] hat folgenden Wert: (Zeilennummer): "+TransactionData[ArrayCount][11]);

				// ausgeben was hier für ein Buchungs-Array zusammengesetzt wurde
				//Logger.debug(LogIdent+"TransactionData["+ArrayCount+"] hat folgende Werte: "+TransactionData[ArrayCount]);
				
				// Den Zähler für das nächste Buchungs-Array hochsetzen
				ArrayCount = ArrayCount +1;
			};
		};

		// umkehren der Reihenfolge der Datensätze im Array da im Auszug oben nicht die neuesten sind
		TransactionData.reverse();

		
		
		// zur Sicherheit gegen Änderungen das ganze Array in ein Sort-Array kopieren
		var UnsortedArray = TransactionData.slice();
		// sortieren der Umsätze nach dem Belegdatum, falls diese im Kontoauszug nicht richtig angeordnet sind
		var SortedArray = UnsortedArray.sort(	function(a, b) { return b[0].getTime() - a[0].getTime(); }	); // und immer dass neueste Datum zuerst, für die Berechnung
		
		// auslesen aller eindeutigen Datum
		var AllDataDates = new Array();
		for (var i = 0; i < SortedArray.length; i++) {
			if (i == 0) { var Next_Date = AllDataDates.push(SortedArray[i][0]); }
			else if (SortedArray[i][0].getTime() !== SortedArray[i-1][0].getTime()) { var Next_Date = AllDataDates.push(SortedArray[i][0]); };
		};
		//Logger.debug(LogIdent+"Liste eindeutiger Beleg-Datum im Auszug:");
		//for (var i = 0; i < AllDataDates.length; i++) { Logger.debug(LogIdent + "Nr.: " + i + "; " + AllDataDates[i]); };
		
		// erzeugen von Array-Blöcken für jedes einzelene Datum, nach einander aus dem Kontoauszug (dadurch entsteht eine sehr sichere Sortierung)
		// Somit ist dass Haupt-Array nach Buchungsdatum sortiert, hat aber innerhalb eines Tages die gleiche Reichenfolge wie im Auszug
		// (sort-Funktion war hier zu fehleranfällig und hat gerne gemischt, d. h. sort und reverse arbeiten innerhalb eines Tages zu unterschiedlich auf verschiedenen Systemen)
		var DayArray = new Array();
		var NewFullArray = new Array();
		for (var i = 0; i < AllDataDates.length; i++) {
			DayArray[i] = new Array();
			for (var ArrayNr = 0; ArrayNr < TransactionData.length; ArrayNr++) {
				if (TransactionData[ArrayNr][0].getTime() === AllDataDates[i].getTime()) {
					//Logger.debug(LogIdent+"Transaktion "+ArrayNr+"; "+TransactionData[ArrayNr][0].getTime()+" = "+AllDataDates[i].getTime()+"; Pusche Umsatz "+TransactionData[ArrayNr][2]);
					var Insert_DataSet = DayArray[i].push(TransactionData[ArrayNr]);
				};
			};
			NewFullArray = NewFullArray.concat(DayArray[i]);
		};
		
		// ursprüngliches Haupt-Array durch neu zusammengesetztes Haupt-Array ersetzen als modularer Aufbau für die Weiterverarbeitung
		TransactionData = [].concat(NewFullArray);
		
		// ausgeben der vollständigen Liste aller Buchungen in sortierter Reihenfolge
		//Logger.debug(LogIdent+"TransactionData hat folgende Werte: "+TransactionData);
		Logger.debug(LogIdent+"Liste aller abgeholten Ums\u00E4tze: (schon fertig formatiert und sortiert, bereit zur Verarbeitung) [falls nicht auskommentiert]");
		for (var i = 0; i < TransactionData.length; i++) {
			//Logger.debug(LogIdent+"\n"+TransactionData[i][0]+"\t"+TransactionData[i][1]+"\t"+TransactionData[i][2]+"\t"+TransactionData[i][3]+"\t"+TransactionData[i][4]+"\t"+TransactionData[i][5]+"\t"+TransactionData[i][6]+"\t"+TransactionData[i][7]+"\t"+TransactionData[i][8]+"\t"+TransactionData[i][9]+"\t"+TransactionData[i][10]);
		};
		

		
		// Berechnung der Summe von Beträgen der neuen Umsätze für die Selbstberechnung falls der Kontoauszugssaldo nicht aktuell ist
		var SumSaldo = 0;
		var TeilSaldo = 0;

		for (var i = 0; i < TransactionData.length; i++) {
			// Um zu prüfen ob es ein neuer checkumsatz ist müssen wir wohl einen Umsatzdatensatz zusammenbauen und mit den vorhandenen vergleichen
			if (Java8upFrame == true) {
				var checkumsatz = db.createObject(java.lang.Class.forName("de.willuhn.jameica.hbci.rmi.Umsatz"),null);
			} else {
				var checkumsatz = db.createObject(Umsatz,null);
			};
			checkumsatz.setKonto(konto);
			
			checkumsatz.setDatum(TransactionData[i][0]); // Datum
			checkumsatz.setValuta(TransactionData[i][1]); // Valuta
			checkumsatz.setBetrag(TransactionData[i][2]); // Betrag
			checkumsatz.setSaldo(TransactionData[i][3]); // Saldo
			checkumsatz.setArt(TransactionData[i][4]); // Umsatzart
			checkumsatz.setZweck(TransactionData[i][5].substr(0,27)); // Verwendungszweck
			if (TransactionData[i][5].length > 27) { checkumsatz.setZweck2(String(TransactionData[i][5].substr(27,27))); };
			// weitere Zeilen
			var purplines = new Array();
			if (TransactionData[i][5].length > 54) { purplines.push(String(TransactionData[i][5].substr(54,27))); };
			if (TransactionData[i][5].length > 81) { purplines.push(String(TransactionData[i][5].substr(81,27))); };
			if (TransactionData[i][5].length > 108) { purplines.push(String(TransactionData[i][5].substr(108,27))); };
			if (TransactionData[i][5].length > 135) { purplines.push(String(TransactionData[i][5].substr(135,27))); };
			if (TransactionData[i][5].length > 162) { purplines.push(String(TransactionData[i][5].substr(126,27))); };
			if (TransactionData[i][5].length > 54) { checkumsatz.setWeitereVerwendungszwecke(purplines); };
			if (TransactionData[i][6]) { checkumsatz.setPrimanota(TransactionData[i][6]); }; // Primanota
			if (TransactionData[i][7]) { checkumsatz.setGegenkontoName(TransactionData[i][7]); }; // GegenkontoName
			if (TransactionData[i][8]) { checkumsatz.setGegenkontoNummer(TransactionData[i][8]); }; // GegenkontoNummer
			if (TransactionData[i][9]) { checkumsatz.setGegenkontoBLZ(TransactionData[i][9]); }; // GegenkontoBLZ
			if (TransactionData[i][10]) { checkumsatz.setKommentar(TransactionData[i][10]); }; // Kommentar


			if (HibiscusScripting_MoneYou_HibiscusUmsaetze && HibiscusScripting_MoneYou_HibiscusUmsaetze.contains(checkumsatz) != null) {
				Logger.debug(LogIdent+"checkumsatz aus der "+TransactionData[i][11]+". Zeile fliest nicht in die checkumsatz-Summenberechnung mit ein da bereits vorhanden ...");
				TransactionData[i][77] = true; // war der Umsatz schon einmal vorhanden? Benötigt für die alternative Zwischensaldo-Berechnung
				continue; // haben wir schon
				
			} else {
				// falls Umsätze absichtlich gelöscht wurden gibt es neue Umsätze aber es ist richtig dass der Kontosaldo dem Auszugssaldo entspricht: dann keine mit Einbeziehung
				if (TransactionData[i][0].getTime() < LastUmsatzDatum.getTime()) {
					Logger.debug(LogIdent+"checkumsatz aus der "+TransactionData[i][9]+". Zeile fliest nicht in die checkumsatz-Summenberechnung mit ein da \u00E4lter als letzter Umsatz ...");
					OldUmsatzCount = OldUmsatzCount + 1;
					continue; // haben wir schon
				
				} else {
					TeilSaldo = TransactionData[i][2]; // Betrag
					SumSaldo = SumSaldo + TeilSaldo;
					SumSaldo = Math.round(SumSaldo * 100) / 100;
					newSaldo = true;
					NewUmsatzCount = NewUmsatzCount + 1;
				};
			};
		};
		Logger.debug(LogIdent+"Es sind neue Ums\u00E4tze seit dem letzen Umsatz vorhanden: " +newSaldo);
		Logger.debug(LogIdent+"Die Summe aller neuen Ums\u00E4tze seit letztem Umsatz betr\u00E4gt: " +SumSaldo);

		if (lastUmsatzZahl != 0) {
			var CheckSaldo_LU_SS = LastUmsatzSaldo + SumSaldo;
			CheckSaldo_LU_SS = Math.round(CheckSaldo_LU_SS * 100) / 100;
			Logger.debug(LogIdent+"Summe des Saldo vom letzen Umsatz und der Summe aller neuen Ums\u00E4tze seit letztem Umsatz: " +CheckSaldo_LU_SS);
			var CheckSaldo_KS_SS = konto.getSaldo() + SumSaldo;
			CheckSaldo_KS_SS = Math.round(CheckSaldo_KS_SS * 100) / 100;
			Logger.debug(LogIdent+"Summe des Saldo vom Konto und der Summe aller neuen Ums\u00E4tze seit letztem Umsatz: " +CheckSaldo_KS_SS);
		};
		
		
		if (lastUmsatzZahl != 0) {

			Logger.debug(LogIdent+"Entwickler-Info: Vorbereitung darauf falls Buchungen fehlen oder Kontosaldo nicht aktuell ist ...");
			var MoneYou_TransIsComming = konto.getMeta("MoneYou_TransIsComming."+MetaIdentifier,"FALSE");
			Logger.debug(LogIdent+"Entwickler-Info: aktueller Stand der Meta-Notiz von MoneYou_TransIsComming zu Anfang: " +MoneYou_TransIsComming);
			var MoneYou_RightSaldo = konto.getMeta("MoneYou_RightSaldo."+MetaIdentifier,"TRUE");
			Logger.debug(LogIdent+"Entwickler-Info: aktueller Stand der Meta-Notiz von MoneYou_RightSaldo zu Anfang: "+MoneYou_RightSaldo);
			var AccountLastUmsatzFehler = false;

			// wir setzen in der Datenbank, wenn scheinbar alles OK ist und keine Indiferenz besteht, zur Sicherheit die Variablen zurück
			if ((givenSaldoGet == true) && (givenKontoSaldo == LastUmsatzSaldo)) {
				Logger.debug(LogIdent+"Entwickler-Info: Kontoauszug hat Saldo UND Kontosaldo ist gleich letztes Zwischensaldo ...");
				konto.setMeta("MoneYou_TransIsComming."+MetaIdentifier, "FALSE");
				var MoneYou_TransIsComming = konto.getMeta("MoneYou_TransIsComming."+MetaIdentifier,"FALSE");
				Logger.debug(LogIdent+"Entwickler-Info: setze somit erstmal auf MoneYou_TransIsComming: "+MoneYou_TransIsComming);
				
				konto.setMeta("MoneYou_RightSaldo."+MetaIdentifier, "TRUE");
				var MoneYou_RightSaldo = konto.getMeta("MoneYou_RightSaldo."+MetaIdentifier,"TRUE");
				Logger.debug(LogIdent+"Entwickler-Info: setze somit erstmal auf MoneYou_RightSaldo: "+MoneYou_RightSaldo);
			
			} else if ((MoneYou_TransIsComming != "FALSE") && (NewUmsatzCount > 0)) {
				Logger.debug(LogIdent+"Entwickler-Info: Es wurden neue Buchungen erwartet, und nun sind neue eingetroffen ...");
				konto.setMeta("MoneYou_TransIsComming."+MetaIdentifier, "TRUE");
				var MoneYou_TransIsComming = konto.getMeta("MoneYou_TransIsComming."+MetaIdentifier,"FALSE");
				Logger.debug(LogIdent+"Entwickler-Info: setze somit erstmal auf MoneYou_TransIsComming: "+MoneYou_TransIsComming);
			};
				

			// jetzt erstmal rausfinden was der Fall sein könnte
			if (	(NewUmsatzCount == 0)
			     && (givenKontoSaldo != givenSaldo)
			     && (MoneYou_RightSaldo == "TRUE")
			   ) {  // In diesem Fall sollten Buchungen fehlen
			   	var sumKontoNewCount = givenKontoSaldo + SumSaldo;
				Logger.debug(LogIdent+"Entwickler-Info: keine Ums\u00e4tze; Kontosaldo["+givenKontoSaldo+"] != Auszugssaldo["+givenSaldo+"]; Summe(Kontosaldo + gesamt neue Umsätze)["+sumKontoNewCount+"] != Auszugssaldo["+givenSaldo+"]");				
				konto.setMeta("MoneYou_TransIsComming."+MetaIdentifier, new java.util.Date());
				var MoneYou_TransIsComming = konto.getMeta("MoneYou_TransIsComming."+MetaIdentifier,"FALSE");
				Logger.debug(LogIdent+"Entwickler-Info: somit fehlen anscheinend Buchungen; setze MoneYou_TransIsComming: "+MoneYou_TransIsComming);
			};

			if (	(NewUmsatzCount > 0) // >=1
			     && (givenKontoSaldo == givenSaldo)
			     && ((Math.round((givenKontoSaldo + SumSaldo) * 100) / 100) != givenSaldo)
			   ) {  // In diesem Fall sollte der Kontosaldo nicht aktuell sein
			   	var sumKontoNewCount = givenKontoSaldo + SumSaldo;
				Logger.debug(LogIdent+"Entwickler-Info: min. ein Umsatz; Kontosaldo["+givenKontoSaldo+"] == Auszugssaldo["+givenSaldo+"]; Summe(Kontosaldo + gesamt neue Umsätze)["+sumKontoNewCount+"] != Auszugssaldo["+givenSaldo+"]");				
				konto.setMeta("MoneYou_RightSaldo."+MetaIdentifier, "FALSE");
				var MoneYou_RightSaldo = konto.getMeta("MoneYou_RightSaldo."+MetaIdentifier,"TRUE");
				Logger.debug(LogIdent+"Entwickler-Info: somit ist anscheined der Auszugssaldo nicht aktuell; setze MoneYou_RightSaldo: "+MoneYou_RightSaldo);
			};

			if (	(NewUmsatzCount == 1)
			     && (givenKontoSaldo != givenSaldo)
			     && ((Math.round((givenKontoSaldo + SumSaldo) * 100) / 100) != givenSaldo)
			     && (MoneYou_TransIsComming != "TRUE")
			   ) {  // In diesem Fall sollten Buchungen fehlen
			   	var sumKontoNewCount = givenKontoSaldo + SumSaldo;
				Logger.debug(LogIdent+"Entwickler-Info: ein Umsatz; Kontosaldo["+givenKontoSaldo+"] != Auszugssaldo["+givenSaldo+"]; Summe(Kontosaldo + gesamt neue Umsätze)["+sumKontoNewCount+"] != Auszugssaldo["+givenSaldo+"]");				
				konto.setMeta("MoneYou_TransIsComming."+MetaIdentifier, new java.util.Date());
				var MoneYou_TransIsComming = konto.getMeta("MoneYou_TransIsComming."+MetaIdentifier,"FALSE");
				Logger.debug(LogIdent+"Entwickler-Info: somit fehlen anscheinend Buchungen; setze MoneYou_TransIsComming: "+MoneYou_TransIsComming);
			};

			if (	(NewUmsatzCount > 1) // >=2
			     && (givenKontoSaldo != givenSaldo)
			     && ((Math.round((givenKontoSaldo + SumSaldo) * 100) / 100) != givenSaldo)
			   ) {  // In diesem Fall sollte der Kontosaldo nicht aktuell sein oder es fehlen Buchungen (wer weiß dass schon) // wer nen tollen Algorithmus für dass hier hat, bitte bei mir melden ;)
			   	var sumKontoNewCount = givenKontoSaldo + SumSaldo;
				Logger.debug(LogIdent+"Entwickler-Info: zwei oder mehr Ums\u00e4tze; Kontosaldo["+givenKontoSaldo+"] != Auszugssaldo["+givenSaldo+"]; Summe(Kontosaldo + gesamt neue Umsätze)["+sumKontoNewCount+"] != Auszugssaldo["+givenSaldo+"]");				
				konto.setMeta("MoneYou_TransIsComming."+MetaIdentifier, new java.util.Date());
				konto.setMeta("MoneYou_RightSaldo."+MetaIdentifier, "FALSE");
				var MoneYou_TransIsComming = konto.getMeta("MoneYou_TransIsComming."+MetaIdentifier,"FALSE");
				Logger.debug(LogIdent+"Entwickler-Info: somit fehlen anscheinend Buchungen; setze MoneYou_TransIsComming: "+MoneYou_TransIsComming);
				var MoneYou_RightSaldo = konto.getMeta("MoneYou_RightSaldo."+MetaIdentifier,"TRUE");
				Logger.debug(LogIdent+"Entwickler-Info: und/oder somit ist anscheined der Auszugssaldo nicht aktuell; setze MoneYou_RightSaldo: "+MoneYou_RightSaldo);
			};


		} else {
				Logger.debug(LogIdent+"Entwickler-Info: Keine Ums\u00e4tze im Konto vorhanden, setze daher den Fehlende-Buchungen-Check auf FALSE und Saldo-Richtig-Check auf TRUE");
				konto.setMeta("MoneYou_TransIsComming."+MetaIdentifier, "FALSE");
				var MoneYou_TransIsComming = konto.getMeta("MoneYou_TransIsComming."+MetaIdentifier,"FALSE");
				Logger.debug(LogIdent+"Entwickler-Info: setze somit erstmal auf MoneYou_TransIsComming: "+MoneYou_TransIsComming);
				
				konto.setMeta("MoneYou_RightSaldo."+MetaIdentifier, "TRUE");
				var MoneYou_RightSaldo = konto.getMeta("MoneYou_RightSaldo."+MetaIdentifier,"TRUE");
				Logger.debug(LogIdent+"Entwickler-Info: setze somit erstmal auf MoneYou_RightSaldo: "+MoneYou_RightSaldo);

		};


		// Berechnung der Zwischensalden der Umsätze
		for (var i = 0; i < TransactionData.length; i++) {
			
			// Prüfung ob der Kontoauszugssaldo noch nicht aktuell ist
			if (   ((givenKontoSaldo != 0) && (lastUmsatzZahl > 0)) 
			     && ((givenSaldo == konto.getSaldo()) || (CheckSaldo_KS_SS != givenSaldo) || (givenSaldoGet == false)) 
			     && (SaldoInfo == false) 
			     && (newSaldo == true) 
			     && (CheckSaldo_LU_SS != givenKontoSaldo) 
			     && (lastUmsatzZahl != 0) 
			     && (MoneYou_RightSaldo != "TRUE")
			     && (MoneYou_TransIsComming == "FALSE")
			   ) {
				Logger.info(LogIdent+"Der Saldo des Kontoauszuges ist nicht auf dem aktuellen Stand. Berechne Zwischensalden daher mit Summe(Kontosaldo, neuer Umsatz) ...");
				monitor.log("Der Saldo des Kontoauszuges ist nicht auf dem aktuellen Stand. Berechne Zwischensalden daher mit Summe(Kontosaldo, neuer Umsatz) ...");
				SaldoInfo = true;
				
				if (konto.getSaldo() != LastUmsatzSaldo) {
					var SaldoInfoString = new java.lang.String("\n   - gesetztes Kontosaldo ist:  "+konto.getSaldo()
										 + "\n   - letztes Zwischensaldo ist: "+LastUmsatzSaldo
										 + "\n\nwurden von Ihnen manuell Ums\u00e4tze gel\u00f6scht?\n\n"
										 + "Wenn JA dann best\u00e4tigen Sie dies hier bitte ...\n"
										 + "(setze dann f\u00fcr die Berechnung den Kontosaldo gleich dem letztem Zwischensaldo (Annahme Kontosaldo ist falsch))\n\n"
										 + "Falls NEIN dann bitte 'Nein' dr\u00fccken ...\n"
										 + "(in diesem Fall wird der Zwischensaldo des letzten Umsatzes gleich Kontosaldo gesetzt (Annahme Kontosaldo ist richtig))\n")
										 + "\nABER ACHTUNG!\n"
										 + "In jedem Fall gilt und hat Vorang:\n"
										 + "   - Wissen Sie garantiert dass der Kontosaldo richtig war bzw. ist, dr\u00fccken Sie auf 'Nein'\n"
										 + "   - Wissen Sie garantiert dass der letzte Zwischensaldo richtig war bzw. ist, dr\u00fccken Sie auf 'Ja'\n\n";
										 
					if (Java8upFrame == true) {
						var infoArray = java.lang.reflect.Array.newInstance(java.lang.Class.forName("java.lang.String"), 1);
					} else {
						var infoArray = java.lang.reflect.Array.newInstance(java.lang.String, 1);
					};
					infoArray[0] = SaldoInfoString;
					// Abfragefenster beim Benutzer mit Info-Text, einstellbar dass dieses nicht mehr erscheint
					if (konto.getUnterkonto().contains("Anlage") || konto.getUnterkonto().contains("Spar") || konto.getUnterkonto().contains("Kredit") || konto.getUnterkonto().contains("Tages") || konto.getUnterkonto().contains("Giro")) {
						var Kontobezeichnung = konto.getBezeichnung() + " - (" + konto.getUnterkonto() + ")";
					} else { var Kontobezeichnung = konto.getBezeichnung(); };
					var UserDelUmsatz = Application.getCallback().askUser(Kontobezeichnung+"\nKontosaldo oder letztes Zwischensaldo nicht korrekt?! {0}", infoArray);
					if (UserDelUmsatz == false) {
						Logger.info(LogIdent+"Info-Warnung: Der Zwischensaldo des letzten Umsatzes entspricht nicht dem Kontosaldo. Wird daher angeglichen. Bitte auf Richtigkeit pr\u00fcfen");
						monitor.log("Info-Warnung: Der Zwischensaldo des letzten Umsatzes entspricht nicht dem Kontosaldo. Wird daher angeglichen. Bitte auf Richtigkeit pr\u00fcfen");
						if (givenSaldoGet == true) { 
							Logger.info(LogIdent+"Letztes Zwischensaldo neu gesetzt mit Auszugssaldo-SumSaldo");
							lastumsatz.setSaldo(givenSaldo - SumSaldo); 
						} else { 
							Logger.info(LogIdent+"Letztes Zwischensaldo neu gesetzt mit Kontosaldo (da Auszugssaldo fehlt)");
							lastumsatz.setSaldo(konto.getSaldo());
						};
						lastumsatz.store();
					
					} else if (UserDelUmsatz == true) {
						konto.setSaldo(Math.round(LastUmsatzSaldo * 100) / 100);
						konto.store();

						// Live-Aktualisierung des Konto-Saldos
						Application.getMessagingFactory().sendMessage(new SaldoMessage(konto));
						monitor.log("Saldo aktualisiert von Konto: " + konto.getBezeichnung());
						
						var givenKontoSaldo = Math.round(konto.getSaldo() * 100) / 100;
					};
					
				};				
				
				// wir simulieren hier also dass der Kontoauszugssaldo schon auf dem richtigem Stand ist
				saldo = givenKontoSaldo + SumSaldo;
				saldo = Math.round(saldo * 100) / 100;
				//givenSaldo = saldo;

				
			} else if ((lastUmsatzZahl == 0) && (newSaldo == true) && (givenSaldoGet == false) && (SaldoInfo == false)) {
				Logger.info(LogIdent+"Der Saldo des Kontoauszuges ist nicht angegeben. Berechne Zwischensalden daher mit Summe(neue Ums\u00e4tze) ...");
				monitor.log("Der Saldo des Kontoauszuges ist nicht angegeben. Berechne Zwischensalden daher mit Summe(neue Ums\u00e4tze) ...");
				SaldoInfo = true;
				
				konto.setSaldo(Math.round(SumSaldo * 100) / 100);

				// wir simulieren hier also dass der Kontoauszugssaldo schon auf dem richtigem Stand ist
				saldo = konto.getSaldo();
				saldo = Math.round(saldo * 100) / 100;
				givenSaldo = saldo;
			};

			if ((MoneYou_TransIsComming != "FALSE") && (MoneYou_TransIsComming != "TRUE") && (lastUmsatzZahl != 0) && (NewUmsatzCount > 0)) {
				Logger.debug(LogIdent+"Da nun also anscheinend das Kontoauszugsaldo nicht aktuell ist ...");
				Logger.debug(LogIdent+" ... und/oder eine Buchung oder mehrere Buchungen fehlen ...");
				Logger.debug(LogIdent+" ... und/oder das Konto-Abrufdatum zur\u00fcck gesetzt wurde, kommt gleich die alternative Zwischensummenberechnung");
				var MakeAlternativeSaldos = true;
				break;
				
			} else {
				var MakeAlternativeSaldos = false;
				//Logger.debug(LogIdent+"Zwischensaldo-Berechnung Schleife: saldo: " + saldo);
				TransactionData[i][3] = saldo;
				saldo = saldo - TransactionData[i][2];
				saldo = Math.round(saldo * 100) / 100;
			};

		};
		if (MakeAlternativeSaldos == true) {
			//if ((MoneYou_TransIsComming != "FALSE") && (MoneYou_RightSaldo == "TRUE")) {
				Logger.info(LogIdent+"Es existieren ungebuchte Transaktionen ODER der Saldo des Kontoauszuges ist nicht aktuell ODER das Konto-Abrufdatum wurde zur\u00fcck gesetzt ...");
				Logger.info(LogIdent+" ... berechne Zwischensalden daher nun mit Summen(letztes Zwischensaldo, neuer Umsatz) ...");
				monitor.log("Es existieren ungebuchte Transaktionen ODER der Saldo des Kontoauszuges ist nicht aktuell ODER das Konto-Abrufdatum wurde zur\u00fcck gesetzt ...");
				monitor.log(" ... berechne Zwischensalden daher nun mit Summen(letztes Zwischensaldo, neuer Umsatz) ...");
			
				var saldo = LastUmsatzSaldo;
			
				for (var i = TransactionData.length; i--;) {
					// Logger.debug(LogIdent+"Zwischensaldo-Berechnung fehlende Buchungen oder Kontoauszugsaldo nicht aktuell - Schleife: aktuelles lastUmsatzDatum inTime: " +LastUmsatzDatum.getTime());
					// Logger.debug(LogIdent+"Zwischensaldo-Berechnung fehlende Buchungen oder Kontoauszugsaldo nicht aktuell - Schleife: aktuelles Belegdatum inTime:      " +TransactionData[i][0].getTime());
					// Logger.debug(LogIdent+"Zwischensaldo-Berechnung fehlende Buchungen oder Kontoauszugsaldo nicht aktuell - Schleife: aktueller lastUmsatzBetrag: " +LastUmsatzBetrag);
					// Logger.debug(LogIdent+"Zwischensaldo-Berechnung fehlende Buchungen oder Kontoauszugsaldo nicht aktuell - Schleife: aktueller Betrag          : " +TransactionData[i][2]);
					if ((TransactionData[i][0].getTime() < LastUmsatzDatum.getTime()) || (TransactionData[i][77] == true)) {
						continue;
					
					} else {
						Logger.debug(LogIdent+"Zwischensaldo-Berechnung fehlende Buchungen oder Kontoauszugsaldo nicht aktuell - Schleife: Umsatz ist dran: " +TransactionData[i][0]+"\t"+TransactionData[i][2]+"\t"+TransactionData[i][5]);
						saldo = saldo + TransactionData[i][2]; 
						saldo = Math.round(saldo * 100) / 100;
						Logger.debug(LogIdent+"Zwischensaldo-Berechnung fehlende Buchungen oder Kontoauszugsaldo nicht aktuell - Schleife: saldo beim setzen: " +saldo);
						TransactionData[i][3] = saldo;
					};
				};
			//};
		};




		// Daten jetzt nochmal andersrum durchlaufen, damit die ältesten Daten zuerst in der DB landen. Diesmal haben wir auch den Saldo nach Buchung
		for (var i = TransactionData.length; i--;) {
			
			// Fortschrittsanzeige auf 50% (Stand nach Kontoabruf) bis max. 99%; Zeilen ohne Umsätze werden abgezogen
			monitor.setPercentComplete(parseInt((50 + (49/(TransactionData.length) * ((TransactionData.length) - i))),10));

			if (Java8upFrame == true) {
				var umsatz = db.createObject(java.lang.Class.forName("de.willuhn.jameica.hbci.rmi.Umsatz"),null);
			} else {
				var umsatz = db.createObject(Umsatz,null);
			};
			umsatz.setKonto(konto);


			umsatz.setDatum(TransactionData[i][0]); // Datum
			umsatz.setValuta(TransactionData[i][1]); // Valuta
			umsatz.setBetrag(TransactionData[i][2]); // Betrag
			umsatz.setSaldo(TransactionData[i][3]); // Saldo
			umsatz.setArt(TransactionData[i][4]); // Umsatzart
			umsatz.setZweck(TransactionData[i][5].substr(0,27)); // Verwendungszweck
			if (TransactionData[i][5].length > 27) { umsatz.setZweck2(String(TransactionData[i][5].substr(27,27))); };
			// weitere Zeilen
			var purplines = new Array();
			if (TransactionData[i][5].length > 54) { purplines.push(String(TransactionData[i][5].substr(54,27))); };
			if (TransactionData[i][5].length > 81) { purplines.push(String(TransactionData[i][5].substr(81,27))); };
			if (TransactionData[i][5].length > 108) { purplines.push(String(TransactionData[i][5].substr(108,27))); };
			if (TransactionData[i][5].length > 135) { purplines.push(String(TransactionData[i][5].substr(135,27))); };
			if (TransactionData[i][5].length > 162) { purplines.push(String(TransactionData[i][5].substr(126,27))); };
			if (TransactionData[i][5].length > 54) { umsatz.setWeitereVerwendungszwecke(purplines); };
			if (TransactionData[i][6]) { umsatz.setPrimanota(TransactionData[i][6]); }; // Primanota
			if (TransactionData[i][7]) { umsatz.setGegenkontoName(TransactionData[i][7]); }; // GegenkontoName
			if (TransactionData[i][8]) { umsatz.setGegenkontoNummer(TransactionData[i][8]); }; // GegenkontoNummer
			if (TransactionData[i][9]) { umsatz.setGegenkontoBLZ(TransactionData[i][9]); }; // GegenkontoBLZ
			if (TransactionData[i][10]) { umsatz.setKommentar(TransactionData[i][10]); }; // Kommentar

			if (HibiscusScripting_MoneYou_HibiscusUmsaetze && HibiscusScripting_MoneYou_HibiscusUmsaetze.contains(umsatz) != null) {
				Logger.debug(LogIdent+"Umsatz aus der "+TransactionData[i][11]+". Zeile des CSV-Kontoauszuges ist bereits gepeichert");
				continue; // haben wir schon
			
			} else {
				//Zählen der neuen Umsätze
				UmsatzAnzahl = UmsatzAnzahl + 1;
				//if (TransactionData[i][0].getTime() < LastUmsatzDatum.getTime()) { OldUmsatzCount = OldUmsatzCount + 1; }
				//else { NewUmsatzCount = NewUmsatzCount + 1; };
			};

			// Umsatz speichern
			Logger.info(LogIdent+"Speichere Umsatz aus der "+TransactionData[i][11]+". Zeile des CSV-Kontoauszuges ...");
			umsatz.store();
			
			// Live-Aktualisierung der Umsatz-Liste
			if ((MoneYou_NewSyncActive == true) && (MoneYou_fetchUmsatz == true)) { Application.getMessagingFactory().sendMessage(new ImportMessage(umsatz)); };

		};
		
		
		// Sollte UmsatzAnzahl == 0 sein ist also kein Umsatz als neu eingestuft worden
		if (UmsatzAnzahl == 0) {
			monitor.log("Ergebnis des Sync: Keine neuen Ums\u00E4tze vorhanden"); 
			Logger.info(LogIdent+"Ergebnis des Sync: Keine neuen Ums\u00E4tze vorhanden");
			
		} else if (UmsatzAnzahl == 1) {
			if (NewUmsatzCount == 1) {
				monitor.log("Ergebnis des Sync: Es wurde ein neuer Umsatz \u00FCbernommen");
				Logger.info(LogIdent+"Ergebnis des Sync: Es wurde ein neuer Umsatz \u00FCbernommen");
			
			} else if (OldUmsatzCount == 1) {
				monitor.log("Ergebnis des Sync: Es wurde ein alter Umsatz \u00FCbernommen");
				Logger.info(LogIdent+"Ergebnis des Sync: Es wurde ein alter Umsatz \u00FCbernommen");
			
			} else {
				monitor.log("Ergebnis des Sync: Es wurde ein Umsatz \u00FCbernommen");
				Logger.info(LogIdent+"Ergebnis des Sync: Es wurde ein Umsatz \u00FCbernommen");
			};

		} else {
			if (UmsatzAnzahl == 2) { var UmsatzCountText = "zwei"; };
			if (UmsatzAnzahl == 3) { var UmsatzCountText = "drei"; };
			if (UmsatzAnzahl == 4) { var UmsatzCountText = "vier"; };
			if (UmsatzAnzahl == 5) { var UmsatzCountText = "f\u00FCnf"; };
			if (UmsatzAnzahl == 6) { var UmsatzCountText = "sechs"; };
			if (UmsatzAnzahl == 7) { var UmsatzCountText = "sieben"; };
			if (UmsatzAnzahl == 8) { var UmsatzCountText = "acht"; };
			if (UmsatzAnzahl == 9) { var UmsatzCountText = "neun"; };
			if (UmsatzAnzahl == 10) { var UmsatzCountText = "zehn"; };
			if (UmsatzAnzahl == 11) { var UmsatzCountText = "elf"; };
			if (UmsatzAnzahl == 12) { var UmsatzCountText = "zw\u00f6lf"; };
			if (UmsatzAnzahl > 12) { var UmsatzCountText = "'"+UmsatzAnzahl+"'"; };	
			monitor.log("Ergebnis des Sync: Es wurden "+UmsatzCountText+" Ums\u00E4tze \u00FCbernommen (davon neue: "+NewUmsatzCount+"  davon alte: "+OldUmsatzCount+")");
			Logger.info(LogIdent+"Ergebnis des Sync: Es wurden "+UmsatzCountText+" Ums\u00E4tze \u00FCbernommen (davon neue: "+NewUmsatzCount+"  davon alte: "+OldUmsatzCount+")");
		};

		
	} else {
		Logger.info(LogIdent+"Verarbeitung der Ums\u00E4tze wird ausgelassen da dies deaktiviert wurde");
		monitor.log("Verarbeitung der Ums\u00E4tze wird ausgelassen da dies deaktiviert wurde");
	};
	//*******************************************************************************
	
	
	
	//*******************************************************************************
	// Verarbeiten des Saldos aus dem Kontoauszug
	//*******************************************************************************
	// Abfrage ob neuer Sync aktiv und ob dort "Saldo aktualisieren" aktiviert ist wegen Speicherung des Konto-Saldos
	if ((MoneYou_NewSyncActive == false) || ((MoneYou_NewSyncActive == true) && (MoneYou_fetchSaldo == true))) {


		Logger.info(LogIdent+"Setze Saldo des Kontos gleich dem Saldo vom Kontoauszug ...");
		Logger.debug(LogIdent+"Saldo der als letztes im Konto gesetzt wird: " +givenSaldo);
		if (typeof(givenSaldo) != "undefined") { konto.setSaldo(Math.round(givenSaldo * 100) / 100); };

		// Abschließendes Konto-Speichern
		konto.store();
		
		// Live-Aktualisierung des Konto-Saldos
		Application.getMessagingFactory().sendMessage(new SaldoMessage(konto));
		monitor.log("Saldo aktualisiert von Konto: " + konto.getBezeichnung());

		
		// Variablen mit dem aktuellen Stand aktualisieren
		Logger.debug(LogIdent+"Entwickler-Info: Abschlie\u00dfender Stand der Kontodaten zum Ende ist nun:");
		givenKontoSaldo = konto.getSaldo();
		Logger.debug(LogIdent+"Konto-Saldo wie soeben gesetzt: " +givenKontoSaldo);
		var lastumsaetze = konto.getUmsaetze();
		var lastUmsatzZahl = parseInt(lastumsaetze.size(),10);
		if (lastUmsatzZahl != 0) {
			var lastumsatz = lastumsaetze.next();
			var lastUmsatzDateArray = String(lastumsatz.getDatum()).split("-");
			var LastUmsatzDatum = new java.util.Date((parseInt(lastUmsatzDateArray[0],10) - 1900),(parseInt(lastUmsatzDateArray[1],10) - 1),parseInt(lastUmsatzDateArray[2],10));
			Logger.debug(LogIdent+"Datum des letzten Umsatzes: " + LastUmsatzDatum);
			var LastUmsatzSaldo = lastumsatz.getSaldo();
			Logger.debug(LogIdent+"Saldo des letzten Umsatzes: " + LastUmsatzSaldo);
		};		
		
		
		
		// Überprüfen des Standes der Beziehung zwischen Kontosaldo und letztem Umsatz
		var MoneYou_RightSaldo = konto.getMeta("MoneYou_RightSaldo."+MetaIdentifier,"TRUE");
		
		if (((SaldoInfo == true) && (givenSaldoGet == true)) || ((givenKontoSaldo != LastUmsatzSaldo) && (MoneYou_RightSaldo == "FALSE") && (MoneYou_TransIsComming == "FALSE"))) {
			Logger.info(LogIdent+"Info-Warnung: Kontoauszugssaldo entspricht nicht dem aktuellen Stand, wird von der Bank sp\u00E4ter aktualisiert! Letztes Zwischensaldo ist daher nicht gleich Kontosaldo");
			monitor.log("Info-Warnung: Kontoauszugssaldo entspricht nicht dem aktuellen Stand, wird von der Bank sp\u00E4ter aktualisiert! Letztes Zwischensaldo ist daher nicht gleich Kontosaldo");
			// Diese Information speichern wir mal
			konto.setMeta("MoneYou_RightSaldo."+MetaIdentifier, "FALSE");
			var MoneYou_RightSaldo = konto.getMeta("MoneYou_RightSaldo."+MetaIdentifier,"TRUE");
			Logger.debug(LogIdent+"Entwickler-Info: der Auszugssaldo ist also im Moment NICHT aktuell; daher MoneYou_RightSaldo: "+MoneYou_RightSaldo);

		} else if (MoneYou_TransIsComming == "FALSE") {
			// es ist anscheined der Saldo vom Auszug aktuell, diese Information speichern wir mal
			konto.setMeta("MoneYou_RightSaldo."+MetaIdentifier, "TRUE");
			var MoneYou_RightSaldo = konto.getMeta("MoneYou_RightSaldo."+MetaIdentifier,"TRUE");
			Logger.debug(LogIdent+"Entwickler-Info: der Auszugssaldo ist also im Moment oder wieder aktuell; daher MoneYou_RightSaldo: "+MoneYou_RightSaldo);

		};
		
		if ((givenSaldoGet == true) && (givenSaldo == givenKontoSaldo) && (givenSaldo == LastUmsatzSaldo) && (givenKontoSaldo == LastUmsatzSaldo)) {
			// es werden im Moment anscheinend keine Buchungen erwartet, diese Information speichern wir mal
			konto.setMeta("MoneYou_TransIsComming."+MetaIdentifier, "FALSE");
			var MoneYou_TransIsComming = konto.getMeta("MoneYou_TransIsComming."+MetaIdentifier,"FALSE");
			Logger.debug(LogIdent+"Entwickler-Info: es werden im Moment anscheinend keine Buchungen erwartet; daher MoneYou_TransIsComming: "+MoneYou_TransIsComming);
		};			
		
		if ((MoneYou_TransIsComming != "FALSE") && (MoneYou_TransIsComming != "TRUE") && (MoneYou_RightSaldo == "TRUE")) {
			Logger.info(LogIdent+"Info-Warnung: der Kontoauszugssaldo ist aktuell doch es werden noch Buchungen erwartet! Letztes Zwischensaldo ist daher nicht gleich Kontosaldo");
			monitor.log("Info-Warnung: der Kontoauszugssaldo ist aktuell doch es werden noch Buchungen erwartet! Letztes Zwischensaldo ist daher nicht gleich Kontosaldo");
		
		} else if (MoneYou_TransIsComming == "TRUE") {
			// es sind anscheined die fehlenden Buchungen eingetroffen, diese Information speichern wir mal
			konto.setMeta("MoneYou_TransIsComming."+MetaIdentifier, "FALSE");
			var MoneYou_TransIsComming = konto.getMeta("MoneYou_TransIsComming."+MetaIdentifier,"FALSE");
			Logger.debug(LogIdent+"Entwickler-Info: es sind anscheined die fehlenden Buchungen eingetroffen; daher MoneYou_TransIsComming: "+MoneYou_TransIsComming);
		};

		if ((MoneYou_TransIsComming != "FALSE") && (MoneYou_TransIsComming != "TRUE") && (MoneYou_RightSaldo == "FALSE")) {
			Logger.info(LogIdent+"Info-Warnung: Kontoauszugssaldo aktuell, Buchungen werden erwartet ODER Kontoauszugssaldo nicht aktuell, wird sp\u00E4ter aktualisiert! Letztes Zwischensaldo ist daher nicht gleich Kontosaldo");
			monitor.log("Info-Warnung: Kontoauszugssaldo aktuell, Buchungen werden erwartet ODER Kontoauszugssaldo nicht aktuell, wird sp\u00E4ter aktualisiert! Letztes Zwischensaldo ist daher nicht gleich Kontosaldo");
		};
		
		// Abschließende Prüfung ob der letzte Zwischensaldo korrekt ist
		if (	((MoneYou_TransIsComming == "FALSE") || (MoneYou_TransIsComming == "TRUE"))
		     && (givenSaldoGet == true)
		     && (givenKontoSaldo == givenSaldo)
		     && (givenKontoSaldo != LastUmsatzSaldo)
		     && (lastUmsatzZahl != 0)
		     && (UmsatzAnzahl == 0)
		     && (MoneYou_RightSaldo == "TRUE")
		   ) {
			Logger.info(LogIdent+"Info-Warnung: der Kontosaldo ist aktuell und keine Buchungen fehlen aber letztes Zwischensaldo ist nicht richtig, setze die letzen 30 neu ...");
			monitor.log("Info-Warnung: der Kontosaldo ist aktuell und keine Buchungen fehlen aber letztes Zwischensaldo ist nicht richtig, setze die letzen 30 neu ...");

			var lastumsaetzecheck = konto.getUmsaetze();
			var lastUmsatzZahl = lastumsaetzecheck.size();
			var saldo = Math.round(givenSaldo * 100) / 100;

			// Berechnung der Zwischensalden der Umsätze
			for (var i = 0; i < lastUmsatzZahl; i++) {
				
				var lastumsatz = lastumsaetzecheck.next();
				// Logger.debug(LogIdent+"aktueller Umsatz der gerade neu Berechnet wird: "+lastumsatz.getDatum()+" // "+lastumsatz.getBetrag()+" // "+lastumsatz.getZweck());
				// Logger.debug(LogIdent+"bekommt der Zwischensaldo: "+saldo);
				lastumsatz.setSaldo(saldo);
				saldo = saldo - lastumsatz.getBetrag();
				saldo = Math.round(saldo * 100) / 100;
				lastumsatz.store();
				
				if (i == 29) { break; };
			};
				
			// Live-Aktualisierung der Umsatz-Liste
			if ((MoneYou_NewSyncActive == true) && (MoneYou_fetchUmsatz == true)) { Application.getMessagingFactory().sendMessage(new ImportMessage(umsatz)); };
		};


	} else {
		Logger.info(LogIdent+"Setzen des Saldo wird ausgelassen da dies deaktiviert wurde");
		monitor.log("Setzen des Saldo wird ausgelassen da dies deaktiviert wurde");
	};
	//*******************************************************************************

	var MoneYou_TransIsComming = konto.getMeta("MoneYou_TransIsComming."+MetaIdentifier,"FALSE");
	Logger.debug(LogIdent+"Entwickler-Info: aktueller Stand der Meta-Notiz von MoneYou_TransIsComming zum Ende: " +MoneYou_TransIsComming);
	var MoneYou_RightSaldo = konto.getMeta("MoneYou_RightSaldo."+MetaIdentifier,"TRUE");
	Logger.debug(LogIdent+"Entwickler-Info: aktueller Stand der Meta-Notiz von MoneYou_RightSaldo zum Ende: "+MoneYou_RightSaldo);
	Logger.debug(LogIdent+"Entwickler-Info: aktueller Stand des Kontosaldo: "+givenKontoSaldo);

};





function HibiscusScripting_MoneYou_refreshHibiscusUmsaetze(konto, d) {
/*******************************************************************************
 * Aktualisiert den Cache der bekannten Umsätze
 *******************************************************************************/

	Logger.debug(LogIdent+"Ums\u00e4tze der letzten "+d+" Tage von Hibiscus f\u00fcr Doppelbuchung-Checks holen ...");
	HibiscusScripting_MoneYou_HibiscusUmsaetze = konto.getUmsaetze(d);
	
};





function HibiscusScripting_MoneYou_removeWhitespace(item) {
/*******************************************************************************
 * Überzählige Leerzeichen am Anfang, Ende oder auch innerhalb einer Zeichenkette
 * entfernen (Quelle: http://www.javascriptsource.com/forms/remove_xs_whitespace.html)
 *******************************************************************************/

	//Logger.debug(LogIdent+"Entwickler-Info: Funktion _removeWhitespace wurde aufgerufen ...");

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





function HibiscusScripting_MoneYou_stripHTML(str) {
/*******************************************************************************
 * Alle HTML-Tags innerhalb einer Zeichenkette entfernen 
 * (Quelle: http://javascript.jstruebig.de/javascript/35)
 *******************************************************************************/

	// Entfernen aller Strings in den Tags
	var tmp = str.replace(/(<.*['"])([^'"]*)(['"]>)/g, 
		function(x, p1, p2, p3) { return  p1 + p3; }
	);

	// Nun die Tags entfernen und das Ergebnis zurück liefern
	return tmp.replace(/<\/?[^>]+>/gi, '');

};





function HibiscusScripting_MoneYou_wordWrap(str, maxWidth) {
/*******************************************************************************
 * Bricht einen langen String nach der maxWidht-Anzahl von Zeichen um ohne Wörter zu trennen
 * (Quelle: http://stackoverflow.com/questions/14484787/wrap-text-in-javascript)
 *******************************************************************************/

	var newLineStr = "\n"; 
	var done = false; 
	var res = '';

	do {                    
		var found = false;

		// Fügt neue Zeile am ersten Leerzeichen der Zeile ein
		for (var i = maxWidth - 1; i >= 0; i--) {
			if (HibiscusScripting_MoneYou_testWhite(str.charAt(i))) {
				res = res + [str.slice(0, i), newLineStr].join('');
				str = str.slice(i + 1);
				found = true;
				break;
			};
		};

		// Fügt neue einen Zeilenumbruch an maxWidth Position ein, ist das Wort zu lang
		if (!found) {
			res += [str.slice(0, maxWidth), newLineStr].join('');
			str = str.slice(maxWidth);
		};

		if (str.length < maxWidth) { done = true; };

	} while(!done);

	return res + str;

};





function HibiscusScripting_MoneYou_testWhite(x) {
/*******************************************************************************
 * Gibt zurück an welcher Stelle eines Strings sich ein Leerzeichen befindet
 * (Quelle: http://stackoverflow.com/questions/14484787/wrap-text-in-javascript)
 *******************************************************************************/

	var white = new RegExp(/^\s$/);

	return white.test(x.charAt(0));

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
		
		//Logger.debug(LogIdent+"Funktion checkResponse wurde aufgerufen mit dem Content: " +ResponseContent); // sollte die ganze HTML-Seite ausgeben also sehr viel
		
		if (ResponseContent.contains("Online-Sperre aufheben")) {
			try {
				HibiscusScripting_MoneYou_unlockAccount(ResponseForm);
			
			} catch(msg) {
				throw msg;
			};

		} else if (ResponseContent.contains('class="msgs error"')) {
			ErrorResponse = ResponseForm.getWebResponse().getContentAsString();
			//Logger.debug(LogIdent+"Funktion checkResponse erstellt ErrorResponse: " +ErrorResponse); // sollte die ganze HTML-Seite ausgeben also sehr viel

			var ErrorMessage = HibiscusScripting_MoneYou_formErrorMessage(ErrorResponse);
		
			// Hier wird nun also die perfekt formatierte Fehlernachricht in einem Infofenster ausgegeben
			Application.getCallback().notifyUser(MoneYouErrorTitle  + ErrorMessage + "\n\n");

			// Für die Monitor-Log Ausgabe werden noch alle Zeilenumbrüche entfernt
			ErrorMessage = ErrorMessage.split("\n").join(" ").split("\r").join(" ");
			
			throw ErrorMessage;
			
		} else if (ResponseContent.contains("wurden automatisch vom System abgemeldet")) {
			throw "Die Sitzung wurde von der Bank beendet. Bitte melden Sie sich erneut an";
		
		} else {
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
		Logger.debug(LogIdent+"ErrorIDXstart: " +ErrorIDXstart);
		var ErrorTextIDXStart = MessagePage.indexOf("<li>", ErrorIDXstart)+4; // Ermittle die Position (Index) des Anfangs des Textes
		Logger.debug(LogIdent+"ErrorTextIDXStart: " +ErrorTextIDXStart);
		var ErrorTextIDXend = MessagePage.indexOf("</li>", ErrorTextIDXStart); // Ermittle die Position (Index) des Endes des Textes
		Logger.debug(LogIdent+"ErrorTextIDXend: " +ErrorTextIDXend);
		var ErrorText = MessagePage.substring(ErrorTextIDXStart, ErrorTextIDXend); // Hole den String vom Index1 bis Index2
		Logger.debug(LogIdent+"ErrorText (unformatiert): " +ErrorText);

		// Um die Info-Nachricht schön mit einem Infofenster ausgeben zu können wird nun der String nun formatiert und Code entfernt
		var formErrorText = ErrorText.split("<br/> ").join("\n")
					     .split("<br/ >").join("\n")
					     .split("<br>").join("\n")
					     .split("\t").join("")
					     .split("  ").join("");
		formErrorText = HibiscusScripting_MoneYou_removeWhitespace(formErrorText);
		
		Logger.debug(LogIdent+"ErrorText (formatiert): " +formErrorText);

		return formErrorText;
		
};





function HibiscusScripting_MoneYou_unlockAccount(UnlockPage) {
/*******************************************************************************
 * Routine zur Entsperrung des MoneYou-Kontos
 *******************************************************************************/
			
	try {
		// Variable für spezifische Error-Meldungen
		var InputError = 0;
		// und für die Weitergabe des Ergebnis um richtig weiterzumachen
		MoneYouAccountUnlocked = false;


		
		//*********************************************************************************
		// Abfrage der aktuellen PIN (Anmeldepasswort) beim Benutzer
		//*********************************************************************************		
		var enterPIN = false;
		do {
			try {
				var unlockPIN = Application.getCallback().askPassword("Ihr Konto wurde aufgrund dreimaliger PIN-Falscheingabe aus Sicherheitsgr\u00fcnden\n" 
								  + "vor\u00fcbergehend gesperrt.\n\n"
								  + "Zur Aufhebung dieser Online-Sperre ben\u00f6tigen Sie die korrekte PIN * und eine g\u00fcltige TAN.\n\n"
								  + "Bitte beachten Sie Gro\u00df und Kleinschreibung.\n\n"
								  + "PIN * (= 5-stelliges Anmeldepasswort)\n");
				if ((false == unlockPIN.isEmpty()) && (String(unlockPIN).length != 5) /*&& (isFinite(unlockSuperPIN) == true)*/) { enterPIN = true; };
				
			} catch(err) {
				InputError = 2;
				throw "Konto ist gesperrt und Entsperrung ist fehlgeschlagen! Passwort-Eingabe vom Benuzter abgebrochen ...";

			};
		
		} while(enterPIN == false);
		//*********************************************************************************


		
		//*********************************************************************************
		// Setzen des Formulars für die PIN-Eingabe und Wert eintragen
		//*********************************************************************************			
		try {
			var formLockedPage = UnlockPage.getFormByName("form-1207487741_1");
			Logger.debug(LogIdent+"HibiscusScripting_MoneYou_unlockAccount: formLockedPage: " +formLockedPage);
			formLockedPage.getInputByName("pin").setValueAttribute(unlockPIN);
			var submitPIN = formLockedPage.getInputByName("$$event_next");
			
		} catch(err) {
			InputError = 2;
			throw "KontoEntsperrung: Fehler beim setzen des PIN-Formulars oder PIN-Feldes (siehe Log - Bitte den Entwickler im Forum informieren)\nLog-Eintrag: " +err;
			
		};	
		//*********************************************************************************


		
		//*********************************************************************************
		// Abschicken des PINs
		//*********************************************************************************		
		Logger.info(LogIdent+"MoneYou-Kontosperre, PIN-Form wird absenden ...");
		try {
			var PostLockedPage = submitPIN.click();
			Logger.debug(LogIdent+"HibiscusScripting_MoneYou_unlockAccount: PostLockedPage: " +PostLockedPage);
			
		} catch(err) {
			InputError = 2;
			throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
			
		};
		var PostLockedXML = PostLockedPage.asXml();
		//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_unlockAccount: PostLockedXML: \n" +PostLockedXML); // gibt die ganze Seite aus, also ganz schön viel
		//*********************************************************************************		

		
		
		// für den Datenschutz und der Sicherheit wird hier gleich mal die PIN-Variable geleert
		unlockPIN = "";

		

		if (PostLockedXML.contains("Abfrage TAN")) {
		
			HaveToEnterTAN = true;
			do {
				// erst einmal den String mit der Tan-Nummer suchen und speichern
				var startTanString = PostLockedXML.indexOf("TAN Nummer"); // Ermittle die Position (Index) des Anfangs des Tan-Nummern Strings
				var TanString = PostLockedXML.substring(startTanString, startTanString+14); // Hole den String mit der Tan-Nummer
			
				var enterTAN = false;
				do {
					try {
						var unlockTAN = Application.getCallback().askPassword("Bitte best\u00e4tigen Sie die Eingabe mit einer\n" 
												    + "TAN aus Ihrer Liste *\n(diese muss 6-stellig sein)\n\n"
												    + "* " +TanString);
						if (false == unlockTAN.isEmpty()) { enterTAN = true; };
						
					} catch(err) {
						InputError = 2;
						throw "Konto ist gesperrt und Entsperrung ist fehlgeschlagen! TAN-Eingabe vom Benuzter abgebrochen ...";

					};
				
				} while(!enterTAN || String(unlockTAN).length != 6);
				
				
				
				//*********************************************************************************
				// Setzen des Formulars für die TAN-Eingabe und Wert eintragen
				//*********************************************************************************			
				try {
					var formLockedPage2 = PostLockedPage.getFormByName("form-1207484145_1");
					Logger.debug(LogIdent+"HibiscusScripting_MoneYou_unlockAccount: formLockedPage2: " +formLockedPage2);
					formLockedPage2.getInputByName("tan").setValueAttribute(unlockTAN);
					var submitTAN = formLockedPage2.getInputByName("$$event_next");
					
				} catch(err) {
					InputError = 2;
					throw "KontoEntsperrung: Fehler beim setzen des TAN-Formulars oder TAN-Feldes (siehe Log - Bitte den Entwickler im Forum informieren)\nLog-Eintrag: " +err;
					
				};	
				//*********************************************************************************



				//*********************************************************************************
				// Abschicken der TAN
				//*********************************************************************************		
				Logger.info(LogIdent+"MoneYou-Kontosperre, TAN-Form wird absenden ...");
				try {
					var PostLockedPage2 = submitTAN.click();
					Logger.debug(LogIdent+"HibiscusScripting_MoneYou_unlockAccount: PostLockedPage2: " +PostLockedPage2);
					
				} catch(err) {
					InputError = 2;
					throw "Der Server antwortet nicht oder es existiert keine Internertverbindung mit Jameica (siehe Log)\nLog-Eintrag: " +err;
					
				};
				var PostLockedXML2 = PostLockedPage2.asXml();
				//Logger.debug(LogIdent+"HibiscusScripting_MoneYou_unlockAccount: PostLockedXML2: \n" +PostLockedXML2); // gibt die ganze Seite aus, also ganz schön viel
				//*********************************************************************************		
			
			
				
				// für den Datenschutz und der Sicherheit wird hier gleich mal die TAN-Variable geleert
				unlockTAN = "";

				
				
				if (PostLockedXML2.contains("Online-Sperre aufgehoben")) {
					Logger.info(LogIdent+"MoneYou-Kontosperre wurde erfolgreich aufgehoben");
					Application.getCallback().notifyUser("MoneYou-Kontosperre wurde erfolgreich aufgehoben\n");
					HaveToEnterTAN = false;
					MoneYouAccountUnlocked = true;
					throw true;
					
				} else if (PostLockedXML2.contains("Anforderung fehlgeschlagen")) {
					InputError = 2;
					HaveToEnterTAN = false;
					throw "Konto ist gesperrt und Entsperrung ist fehlgeschlagen! (TAN Eingabe fehlgeschlagen, die Sitzung wurde vorzeitig beendet)";
				
				} else if (PostLockedXML2.contains("Abfrage TAN")) {
					PostLockedXML = PostLockedXML2;
					HaveToEnterTAN = true;
					Application.getCallback().notifyUser("\nFehlermeldung:\n\n\nDie eingegebene TAN war nicht korrekt,\nbitte geben Sie eine g\u00fcltige TAN-Nummer an!\n\n");
					
				} else {
					InputError = 2;
					//Logger.debug(LogIdent+"PostLockedXML2: " +PostLockedXML2); // gibt die ganze Seite als XML aus daher lieber auskommentieren
					HaveToEnterTAN = false;
					throw "Konto ist gesperrt und Entsperrung ist fehlgeschlagen! (zweiter Schritt, TAN war nicht korrekt)";
					
				};
				
			} while(HaveToEnterTAN == true);
			
			
		} else if (PostLockedXML.contains("Anforderung fehlgeschlagen")) {
			InputError = 2;
			throw "Konto ist gesperrt und Entsperrung ist fehlgeschlagen! (TAN Anforderung fehlgeschlagen, die Sitzung wurde vorzeitig beendet)";
			
		} else {
			InputError = 2;
			throw "Konto ist gesperrt und Entsperrung ist fehlgeschlagen! (erster Schritt, PIN ist nicht korrekt)";
				
		};
		
	} catch(err) {

		if (InputError == 2) {
			throw err;
			
		} else if (MoneYouAccountUnlocked == true) {
			throw "MoneYouAccountUnlocked";
		
		} else {
			throw "Fehlermeldung von Jameica: " +err;

		};

	};
	
};




