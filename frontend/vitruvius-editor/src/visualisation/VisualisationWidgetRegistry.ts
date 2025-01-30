import { injectable } from "@theia/core/shared/inversify";
import {VisualisationWidget} from "./VisualisationWidget";
import {Connection} from "../model/Connection";
import {DisplayView} from "../model/DisplayView";

@injectable()
export class VisualisationWidgetRegistry{
    private widgets: WidgetData[] = [];

    registerWidget(widget: VisualisationWidget<any>, displayView: DisplayView, connection: Connection): void {
        this.widgets.push({connection, displayView, widget})
    }

    unregisterWidget(widget: VisualisationWidget<any>): void {
        this.widgets = this.widgets.filter(widgetData => widgetData.widget.getLabel() !== widget.getLabel());
    }

    getWidgets(): Readonly<WidgetData[]> {
        return this.widgets;
    }
}

type WidgetData = {connection: Connection, displayView: DisplayView, widget: VisualisationWidget<any>}
