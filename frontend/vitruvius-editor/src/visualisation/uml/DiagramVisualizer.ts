import { Content } from "../../model/Content";
import { Visualizer } from "../Visualizer";
import { VisualisationWidget } from "../VisualisationWidget";
import {inject, injectable} from "@theia/core/shared/inversify";
import {ApplicationShell, WidgetManager} from "@theia/core/lib/browser";
import {DiagramWidget} from "./DiagramWidget";

@injectable()
export class DiagramVisualizer implements Visualizer {
  @inject(WidgetManager)
  private readonly widgetManager!: WidgetManager;
  @inject(ApplicationShell) protected readonly shell: ApplicationShell;

  async visualizeContent(content: Content): Promise<VisualisationWidget<any>> {
    let contentWindow = content.windows[0];
    return this.widgetManager
      .getOrCreateWidget(DiagramWidget.ID, contentWindow.name)
      .then(widget => {
        let diagram = JSON.parse(contentWindow.content);
        (widget as DiagramWidget).updateContent(diagram);
        (widget as DiagramWidget).update();
        return widget as VisualisationWidget<string>;
      })
  }
}
