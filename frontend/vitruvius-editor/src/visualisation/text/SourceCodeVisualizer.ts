import { Visualizer } from "../Visualizer";
import {Widget, WidgetManager} from "@theia/core/lib/browser";
import {inject, injectable} from "@theia/core/shared/inversify";
import {TextWidget} from "./TextWidget";
import { ApplicationShell } from "@theia/core/lib/browser/shell/application-shell";
import {Content} from "../../model/Content";

@injectable()
export class SourceCodeVisualizer implements Visualizer {
	@inject(WidgetManager)
	private readonly widgetManager!: WidgetManager;
	@inject(ApplicationShell) protected readonly shell: ApplicationShell;
	async visualizeContent(content: Content): Promise<Widget> {
		let contentWindow = content.windows[0];
		return this.widgetManager.getOrCreateWidget(TextWidget.ID, contentWindow.name).then(widget => {
			(widget as TextWidget).updateContent(contentWindow.content);
			this.shell.addWidget(widget, {area: 'main'});
			return widget;
		});
	}
  
}
