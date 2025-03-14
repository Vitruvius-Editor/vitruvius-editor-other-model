import * as React from "react";
import { injectable, postConstruct } from "@theia/core/shared/inversify";
import { ReactWidget } from "@theia/core/lib/browser/widgets/react-widget";

@injectable()
export class HelpWidget extends ReactWidget {
  static readonly ID = "widget:display-help";
  static readonly LABEL = "Vitruvius Help";

  @postConstruct()
  protected init(): void {
    this.doInit();
  }

  protected async doInit(): Promise<void> {
    this.id = HelpWidget.ID;
    this.title.label = HelpWidget.LABEL;
    this.title.caption = HelpWidget.LABEL;
    this.title.closable = true;
    this.title.iconClass = "fa fa-window-maximize"; // example widget icon.
    this.update();
  }

  render(): React.ReactElement {
    return <div className="vitruvius-help">{this.getHtmlContent()}</div>;
  }

  protected getHtmlContent(): React.ReactElement {
    // Replace this with your actual HTML content
    return (
      <div className="vitruvius-help">
        <h1>Vitruvius Hilfe</h1>
        <p>
          Diese Anleitung beschreibt die Nutzung des Vitruvius-Editor-Plugins in
          Eclipse Theia.
        </p>

        <h2>Benutzeroberfläche (GUI)</h2>
        <p>
          In der oberen Toolbar befindet sich ganz rechts ein neues Menü mit dem
          Titel &quot;Vitruvius&quot;. Darüber sind alle hier beschriebenen
          Befehle schnell erreichbar.
        </p>
        <p>
          Auf der linken Seite gibt es einen neuen Tab namens
          &quot;Vitruvius&quot;. Über diesen Tab kann das Fenster geöffnet
          werden, in dem alle verfügbaren Views angezeigt werden.
        </p>

        <h2>Vitruvius-Editor starten</h2>
        <p>Mit Dockerfile starten.</p>

        <h2>Vitruvius-Projekt öffnen</h2>
        <ol>
          <li>1. Das Vitruvius-Projekt importieren.</li>
          <li>2. Name, Beschreibung, Host und Port angeben.</li>
          <li>
            3. Falls ein Server auf dem angegebenen Port existiert, wird das
            Projekt automatisch geladen.
          </li>
          <li>
            4. Falls bereits mehrere Server eingebunden sind, können sie über
            den Befehl &quot;Vitruvius Load Project&quot; im Vitruvius-Menü
            ausgewählt und geladen werden.
          </li>
        </ol>

        <h2>Views öffnen</h2>
        <ol>
          <li>
            1. Das Fenster &quot;Vitruvius&quot; in der linken Taskbar öffnen.
          </li>
          <li>2. Im Fenster werden alle verfügbaren View-Typen angezeigt.</li>
          <li>
            3. Einen View-Typ anklicken – dieser klappt sich aus und zeigt alle
            zugehörigen Views an.
          </li>
          <li>
            4. Eine gewünschte View auswählen, um sie zu öffnen. Diese wird dann
            in der Main Area der Theia IDE dargestellt.
          </li>
        </ol>

        <h2>Vitruvius Views editieren</h2>
        <ol>
          <li>1. Gewünschte View öffnen</li>
          <li>
            2. Änderungen vornehmen, z.B. Code-Änderungen am Source Code
            vornehmen oder editierbare Kacheln einer Tabelle anpassen.
          </li>
          <li>
            3. Änderungen auf das Model mit dem Vitruvius-Server synchronisieren
            mit dem &quot;Vitruvius Refresh Project&quot; Command.
          </li>
          <li>4. Die zu synchronisierende Datei auswählen.</li>
        </ol>

        <h2>Vitruvius-Projekt löschen</h2>
        <ol>
          <li>
            1. Den Befehl &quot;Vitruvius Delete Project&quot; im Vitruvius-Menü
            auswählen.
          </li>
          <li>2. Das zu löschende Projekt auswählen.</li>
        </ol>
        <p>
          Dies entfernt lediglich die Verbindung zum Vitruvius-Server, das
          eigentliche Projekt bleibt bestehen.
        </p>

        <h2>Name und Beschreibung eines Vitruvius-Projekts ändern</h2>
        <ol>
          <li>1. Den Befehl &quot;Vitruvius Edit Project&quot; auswählen.</li>
          <li>2. Das gewünschte Projekt auswählen.</li>
          <li>3. Name und Beschreibung nach Wunsch anpassen.</li>
        </ol>
      </div>
    );
  }
}
