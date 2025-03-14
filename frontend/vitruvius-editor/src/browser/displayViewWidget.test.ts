import { DisplayViewWidget } from "./displayViewWidget";
import { Container } from "@theia/core/shared/inversify";
import { MessageService } from "@theia/core";
import { DisplayViewService } from "../backend-communication/DisplayViewService";
import { DisplayViewResolver } from "../visualisation/DisplayViewResolver";
import { VisualisationWidgetRegistry } from "../visualisation/VisualisationWidgetRegistry";
import { Connection } from "../model/Connection";
import { DisplayView } from "../model/DisplayView";
import { render, fireEvent, waitFor } from "@testing-library/react";
import "@testing-library/jest-dom";

describe("DisplayViewWidget", () => {
  let widget: DisplayViewWidget;
  let messageService: jest.Mocked<MessageService>;
  let displayViewService: jest.Mocked<DisplayViewService>;
  let displayViewResolver: jest.Mocked<DisplayViewResolver>;
  let visualisationWidgetRegistry: jest.Mocked<VisualisationWidgetRegistry>;
  let connection: Connection;
  let displayView: DisplayView;

  beforeEach(() => {
    const container = new Container();
    messageService = {
      error: jest.fn(),
    } as unknown as jest.Mocked<MessageService>;
    displayViewService = {
      getDisplayViews: jest.fn(),
      getDisplayViewWindows: jest.fn(),
      getDisplayViewContent: jest.fn(),
    } as unknown as jest.Mocked<DisplayViewService>;
    displayViewResolver = {
      getWidget: jest.fn(),
    } as unknown as jest.Mocked<DisplayViewResolver>;
    visualisationWidgetRegistry = {
      registerWidget: jest.fn(),
    } as unknown as jest.Mocked<VisualisationWidgetRegistry>;

    container.bind(MessageService).toConstantValue(messageService);
    container.bind(DisplayViewService).toConstantValue(displayViewService);
    container.bind(DisplayViewResolver).toConstantValue(displayViewResolver);
    container
      .bind(VisualisationWidgetRegistry)
      .toConstantValue(visualisationWidgetRegistry);
    container.bind(DisplayViewWidget).toSelf();
    widget = container.get(DisplayViewWidget);

    connection = { uuid: "test-uuid" } as Connection;
    displayView = { name: "TestView" } as DisplayView;
  });

  it("should initialize with default id, label, and empty content", () => {
    expect(widget.id).toBe(DisplayViewWidget.ID);
    expect(widget.title.label).toBe(DisplayViewWidget.LABEL);
    expect(widget.getConnection()).toBeNull();
    expect(widget["widgetItems"]).toEqual([]);
  });

  it("should load project and update widget items", async () => {
    displayViewService.getDisplayViews.mockResolvedValue([displayView]);
    await widget.loadProject(connection);
    expect(widget.getConnection()).toBe(connection);
    expect(widget["widgetItems"]).toEqual([{ displayView, windows: null }]);
  });

  it("should handle project load failure", async () => {
    displayViewService.getDisplayViews.mockRejectedValue(
      new Error("Connection error"),
    );
    await widget.loadProject(connection);
    await waitFor(() =>
      expect(displayViewService.getDisplayViews).toHaveBeenCalled(),
    );
    expect(messageService.error).toHaveBeenCalledWith(
      "Couldn't connect to the given Vitruvius server.",
    );
    expect(widget.getConnection()).toBeNull();
    expect(widget["widgetItems"]).toEqual([]);
  });

  it("should render the widget with no project loaded", () => {
    const { getByText } = render(widget.render());
    expect(
      getByText("Currently no Vitruvius project is loaded."),
    ).toBeInTheDocument();
  });

  it("should handle loading a project with null value", async () => {
    await widget.loadProject(null);
    expect(widget.getConnection()).toBeNull();
    expect(widget["widgetItems"]).toEqual([]);
  });

  it("should render the widget with project loaded", async () => {
    displayViewService.getDisplayViews.mockResolvedValue([displayView]);
    await widget.loadProject(connection);
    const { getByText } = render(widget.render());
    expect(
      getByText("The following views are avaliable for the loaded project:"),
    ).toBeInTheDocument();
    expect(getByText(displayView.name)).toBeInTheDocument();
  });

  it("should handle widget item click and load windows", async () => {
    displayViewService.getDisplayViews.mockResolvedValue([displayView]);
    displayViewService.getDisplayViewWindows.mockResolvedValue(["Window1"]);
    await widget.loadProject(connection);
    const { getByText } = render(widget.render());
    fireEvent.click(getByText(displayView.name));
    expect(displayViewService.getDisplayViewWindows).toHaveBeenCalledWith(
      connection.uuid,
      displayView.name,
    );
  });

  it("should handle window click and show content in new widget", async () => {
    displayViewService.getDisplayViews.mockResolvedValue([displayView]);
    displayViewService.getDisplayViewWindows.mockResolvedValue(["Window1"]);
    displayViewService.getDisplayViewContent.mockResolvedValue({
      visualizerName: "TextVisualizer",
      windows: [{ name: "Window1", content: "Content" }],
    });
    const mockWidget = { show: jest.fn() } as any;
    displayViewResolver.getWidget.mockResolvedValue(mockWidget);
    await widget.loadProject(connection);
    let { getByText } = render(widget.render());
    fireEvent.click(getByText(displayView.name));
    await waitFor(() =>
      expect(widget["widgetItems"][0].windows).toEqual(["Window1"]),
    );
    getByText = render(widget.render()).getByText;
    fireEvent.click(getByText("Window1"));
    expect(displayViewService.getDisplayViewContent).toHaveBeenCalledWith(
      connection.uuid,
      displayView.name,
      { windows: ["Window1"] },
    );
    await waitFor(() =>
      expect(displayViewResolver.getWidget).toHaveBeenCalled(),
    );
    expect(visualisationWidgetRegistry.registerWidget).toHaveBeenCalledWith(
      mockWidget,
      displayView,
      connection,
    );
    expect(mockWidget.show).toHaveBeenCalled();
  });

  it("should close windows when clicked again on the DisplayView name", async () => {
    displayViewService.getDisplayViews.mockResolvedValue([displayView]);
    displayViewService.getDisplayViewWindows.mockResolvedValue(["Window1"]);
    await widget.loadProject(connection);
    const { getByText } = render(widget.render());

    // Click to open windows
    fireEvent.click(getByText(displayView.name));
    await waitFor(() =>
      expect(widget["widgetItems"][0].windows).toEqual(["Window1"]),
    );

    // Click again to close windows
    fireEvent.click(getByText(displayView.name));
    await waitFor(() => expect(widget["widgetItems"][0].windows).toBeNull());
  });

  it("should store and restore state", () => {
    widget["connection"] = connection;
    widget["widgetItems"] = [{ displayView, windows: ["Window1"] }];
    const state = widget.storeState();
    expect(state).toEqual({
      connection,
      widgetItems: [{ displayView, windows: ["Window1"] }],
    });

    const newWidget = new DisplayViewWidget();
    newWidget.restoreState(state);
    expect(newWidget.getConnection()).toEqual(connection);
    expect(newWidget["widgetItems"]).toEqual([
      { displayView, windows: ["Window1"] },
    ]);
  });
});
