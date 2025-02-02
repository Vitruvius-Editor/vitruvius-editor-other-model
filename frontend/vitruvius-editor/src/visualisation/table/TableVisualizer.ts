import { Content } from "../../model/Content";
import { Visualizer } from "../Visualizer";
import { VisualisationWidget } from "../VisualisationWidget";
import {inject, injectable} from "@theia/core/shared/inversify";
import {ApplicationShell, WidgetManager} from "@theia/core/lib/browser";
import {TableWidget} from "./TableWidget";

@injectable()
export class TableVisualizer implements Visualizer {
  @inject(WidgetManager)
  private readonly widgetManager!: WidgetManager;
  @inject(ApplicationShell) protected readonly shell: ApplicationShell;

  async visualizeContent(content: Content): Promise<VisualisationWidget<any>> {
		let contentWindow = content.windows[0];
		let parsed: Table = JSON.parse(contentWindow.content);
		return this.widgetManager
				.getOrCreateWidget(TableWidget.ID, contentWindow.name)
				.then(widget => {
						(widget as TableWidget).updateContent(parsed);
						this.shell.addWidget(widget, { area: "main" });
						this.shell.activateWidget(widget.id);
						return widget as VisualisationWidget<string>;
				})
  }
}
