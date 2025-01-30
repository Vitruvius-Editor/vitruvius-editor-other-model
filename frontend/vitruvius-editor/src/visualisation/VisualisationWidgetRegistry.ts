import { injectable } from "@theia/core/shared/inversify";
import {VisualisationWidget} from "./VisualisationWidget";

@injectable()
export class VisualisationWidgetRegsitry{
    private widgets: VisualisationWidget<any>[] = [];

    registerWidget(widget: VisualisationWidget<any>): void {
        this.widgets.push(widget)
    }

    unregisterWidget(widget: VisualisationWidget<any>): void {
        this.widgets = this.widgets.filter(w => w.getLabel() !== widget.getLabel());
    }
}
