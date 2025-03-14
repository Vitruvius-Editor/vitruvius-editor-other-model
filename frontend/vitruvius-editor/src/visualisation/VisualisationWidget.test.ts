import { Container } from "@theia/core/shared/inversify";
import { VisualisationWidget } from "./VisualisationWidget";
import { VisualisationWidgetRegistry } from "./VisualisationWidgetRegistry";
import { DisplayView } from "../model/DisplayView";
import { Connection } from "../model/Connection";
import React from "react";

class TestVisualisationWidget extends VisualisationWidget<string> {
  getVisualizerName(): string {
    return "TestVisualizer";
  }

  protected render(): React.ReactNode {
    return undefined;
  }
}

describe("VisualisationWidget", () => {
  let widget: TestVisualisationWidget;
  let visualisationWidgetRegistry: jest.Mocked<VisualisationWidgetRegistry>;

  beforeEach(async () => {
    const container = new Container();
    visualisationWidgetRegistry = {
      registerWidget: jest.fn(),
      unregisterWidget: jest.fn(),
      getWidgets: jest.fn().mockReturnValue([]),
    } as unknown as jest.Mocked<VisualisationWidgetRegistry>;
    container
      .bind(VisualisationWidgetRegistry)
      .toConstantValue(visualisationWidgetRegistry);
    container.bind(TestVisualisationWidget).toSelf();
    widget = container.get(TestVisualisationWidget);
    await (widget as any).doInit("test-id", "Test Label", "Initial Content");
  });

  it("should initialize with given id, label, and content", () => {
    expect(widget.id).toBe("test-id");
    expect(widget.getLabel()).toBe("Test Label");
    expect(widget.getContent()).toBe("Initial Content");
  });

  it("should update content", () => {
    widget.updateContent("Updated Content");
    expect(widget.getContent()).toBe("Updated Content");
  });

  it("should set and get label", () => {
    widget.setLabel("New Label");
    expect(widget.getLabel()).toBe("New Label");
  });

  it("should register and unregister widget on close", () => {
    widget.close();
    expect(visualisationWidgetRegistry.unregisterWidget).toHaveBeenCalledWith(
      widget,
    );
  });

  it("should store and restore state", () => {
    const displayView: DisplayView = {
      name: "display-view",
      viewTypeName: "view-type",
      viewMapperName: "view-mapper",
      windowSelectorName: "window-selector",
      contentSelectorName: "content-selector",
    };
    const connection: Connection = {
      name: "connection",
      description: "connection-description",
      url: "http://localhost:8080",
      uuid: "connection-uuid",
      port: 8080,
    };
    visualisationWidgetRegistry.getWidgets.mockReturnValue([
      { widget, displayView, connection },
    ]);

    const state = widget.storeState();
    expect(state).toEqual({
      content: "Initial Content",
      label: "Test Label",
      displayView,
      connection,
    });

    widget.restoreState(state);
    expect(widget.getLabel()).toBe("Test Label");
    expect(widget.getContent()).toBe("Initial Content");
    expect(visualisationWidgetRegistry.registerWidget).toHaveBeenCalledWith(
      widget,
      displayView,
      connection,
    );
  });
});
