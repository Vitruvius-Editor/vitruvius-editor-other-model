import { Visualizer } from "../Visualizer";
import {Widget, WidgetManager} from "@theia/core/lib/browser";
import {inject, injectable} from "@theia/core/shared/inversify";
import {TextWidget} from "./TextWidget";
import { ApplicationShell } from "@theia/core/lib/browser/shell/application-shell";

@injectable()
export class SourceCodeVisualizer implements Visualizer {
	@inject(WidgetManager)
	private readonly widgetManager!: WidgetManager;
	@inject(ApplicationShell) protected readonly shell: ApplicationShell;
	async visualizeContent(content: string): Promise<Widget> {
		return this.widgetManager.getOrCreateWidget(TextWidget.ID, "foo").then(widget => {
			(widget as TextWidget).updateContent(content);
			widget.show();
			this.shell.addWidget(widget, {area: 'main'});
			return widget;
		});
	}
  
}
