import { Visualizer } from "../Visualizer";
import { WidgetManager } from "@theia/core/lib/browser";
import { inject, injectable } from "@theia/core/shared/inversify";
import { TextWidget } from "./TextWidget";
import { ApplicationShell } from "@theia/core/lib/browser/shell/application-shell";
import { Content } from "../../model/Content";
import { VisualisationWidget } from "../VisualisationWidget";

/**
 * A Visualizer to display source code based DisplayViews.
 */
@injectable()
export class SourceCodeVisualizer implements Visualizer {
  @inject(WidgetManager)
  private readonly widgetManager!: WidgetManager;
  @inject(ApplicationShell) protected readonly shell: ApplicationShell;

  async visualizeContent(content: Content): Promise<VisualisationWidget<any>> {
    // TODO: Allow multiple windows to be displayed at once.
    let contentWindow = content.windows[0];
    // Create a new TextWidget through the widgetManager and add it to the main area of the shell.
    return this.widgetManager
      .getOrCreateWidget(TextWidget.ID, contentWindow.name)
      .then((widget) => {
        // At the content to the window.
        (widget as TextWidget).updateContent(contentWindow.content);
        // At the window to the editor.
        this.shell.addWidget(widget, { area: "main" });
        return widget as VisualisationWidget<string>;
      });
  }
}
