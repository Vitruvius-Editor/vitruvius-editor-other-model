import * as React from "react";
import { injectable, postConstruct } from "@theia/core/shared/inversify";
import { ReactWidget } from "@theia/core/lib/browser/widgets/react-widget";

@injectable()
export class HelpWidget extends ReactWidget {
  static readonly ID = "help:widget";
  static readonly LABEL = "Help";

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
      <div>
        <h1>Help</h1>
        <div dangerouslySetInnerHTML={{ __html: this.getHtmlContent() }} />
      </div>
    );
  }

  protected getHtmlContent(): string {
    // Replace this with your actual HTML content
    return `
      <p>This is the help content.</p>
    `;
  }
}