import { Content } from "../../../model/Content";
import { Visualizer } from "../../Visualizer";
import { VisualisationWidget } from "../../VisualisationWidget";
import {inject, injectable} from "@theia/core/shared/inversify";
import {ApplicationShell, WidgetManager} from "@theia/core/lib/browser";
import {PackageDiagramWidget} from "./PackageDiagramWidget";

@injectable()
export class PackageDiagramVisualizer implements Visualizer {
  @inject(WidgetManager)
  private readonly widgetManager!: WidgetManager;
  @inject(ApplicationShell) protected readonly shell: ApplicationShell;

  async visualizeContent(content: Content): Promise<VisualisationWidget<any>> {
    let contentWindow = content.windows[0];
    return this.widgetManager
      .getOrCreateWidget(PackageDiagramWidget.ID, contentWindow.name)
      .then(widget => {
        (widget as PackageDiagramWidget).updateContent(contentWindow.content);
		this.shell.addWidget(widget, { area: "main" });
		this.shell.activateWidget(widget.id);
        (widget as PackageDiagramWidget).update();
        return widget as VisualisationWidget<string>;
      })
  }
}
