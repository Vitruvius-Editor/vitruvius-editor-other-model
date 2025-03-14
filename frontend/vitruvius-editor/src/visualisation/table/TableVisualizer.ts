import { Content } from "../../model/Content";
import { Visualizer } from "../Visualizer";
import { VisualisationWidget } from "../VisualisationWidget";
import { inject, injectable } from "@theia/core/shared/inversify";
import { ApplicationShell, WidgetManager } from "@theia/core/lib/browser";
import { TableWidget } from "./TableWidget";
import { Table } from "./Table";

@injectable()
export class TableVisualizer implements Visualizer {
  @inject(WidgetManager)
  private readonly widgetManager!: WidgetManager;
  @inject(ApplicationShell) protected readonly shell: ApplicationShell;

  async visualizeContent(content: Content): Promise<VisualisationWidget<any>> {
    let contentWindow = content.windows[0];
    let parsed: Table = JSON.parse(contentWindow.content);
    return this.widgetManager
      .getOrCreateWidget(TableWidget.ID, "ClassTable " + contentWindow.name)
      .then((widget) => {
        (widget as TableWidget).updateContent(parsed);
        return widget as VisualisationWidget<string>;
      });
  }
}
