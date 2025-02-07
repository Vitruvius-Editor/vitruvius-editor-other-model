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
    return (
      <div className="editor-container">
        <h1>Help</h1>
        { this.getHtmlContent() }
      </div>
    );
  }

  protected getHtmlContent(): React.ReactElement {
    // Replace this with your actual HTML content
    return (<p>This is the help content.</p>);
  }
}
