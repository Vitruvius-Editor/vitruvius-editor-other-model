import { VisualisationWidgetRegistry } from "./VisualisationWidgetRegistry";
import { VisualisationWidget } from "./VisualisationWidget";
import { DisplayView } from "../model/DisplayView";
import { Connection } from "../model/Connection";
import { generateUuid } from "@theia/core";
import * as React from "react";
import 'reflect-metadata';

class MockVisualisationWidget extends VisualisationWidget<any> {
    getVisualizerName(): string {
        return "MockVisualizer";
    }

    protected render(): React.ReactNode {
        return undefined;
    }
}

describe("VisualisationWidgetRegistry", () => {
    let registry: VisualisationWidgetRegistry;
    let mockWidget: MockVisualisationWidget;
    let displayView: DisplayView;
    let connection: Connection;

    beforeEach(() => {
        registry = new VisualisationWidgetRegistry();
        mockWidget = new MockVisualisationWidget();
        displayView = { name: "TestView", viewTypeName: "TestType", viewMapperName: "TestMapper", contentSelectorName: "TestSelector", windowSelectorName: "TestWindow" };
        connection = { uuid: generateUuid(), name: "TestConnection", description: "TestDescription", url: "http://test.com" };
    });

    it("should register a widget", () => {
        registry.registerWidget(mockWidget, displayView, connection);
        const widgets = registry.getWidgets();
        expect(widgets.length).toBe(1);
        expect(widgets[0].widget).toBe(mockWidget);
        expect(widgets[0].displayView).toBe(displayView);
        expect(widgets[0].connection).toBe(connection);
    });

    it("should not register a widget with the same label and constructor name", () => {
        registry.registerWidget(mockWidget, displayView, connection);
        const anotherWidget = new MockVisualisationWidget();
        registry.registerWidget(anotherWidget, displayView, connection);
        const widgets = registry.getWidgets();
        expect(widgets.length).toBe(1);
    });

    it("should unregister a widget", () => {
        registry.registerWidget(mockWidget, displayView, connection);
        registry.unregisterWidget(mockWidget);
        const widgets = registry.getWidgets();
        expect(widgets.length).toBe(0);
    });

    it("should get widgets by connection", () => {
        registry.registerWidget(mockWidget, displayView, connection);
        const anotherConnection = { uuid: generateUuid(), name: "AnotherConnection", description: "AnotherDescription", url: "http://another.com" };
        const anotherWidget = new MockVisualisationWidget();
        registry.registerWidget(anotherWidget, displayView, anotherConnection);
        const widgetsByConnection = registry.getWidgetsByConnection(connection);
        expect(widgetsByConnection.length).toBe(1);
        expect(widgetsByConnection[0].widget).toBe(mockWidget);
    });
});