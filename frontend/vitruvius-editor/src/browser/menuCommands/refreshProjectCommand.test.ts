import { CommandRegistry, MessageService, QuickPickService } from "@theia/core";
import { Container } from "@theia/core/shared/inversify";
import { ConnectionService } from "../../backend-communication/ConnectionService";
import {VisualisationWidgetRegistry, WidgetData} from "../../visualisation/VisualisationWidgetRegistry";
import { DisplayViewService } from "../../backend-communication/DisplayViewService";
import { DisplayViewResolver } from "../../visualisation/DisplayViewResolver";
import { VitruviusRefreshProjectContribution, RefreshProjectCommand } from "./refreshProjectCommand";
import {TextWidget} from "../../visualisation/text/TextWidget";

describe("VitruviusRefreshProjectContribution", () => {
    let container: Container;
    let contribution: VitruviusRefreshProjectContribution;
    let messageService: jest.Mocked<MessageService>;
    let connectionService: jest.Mocked<ConnectionService>;
    let quickPickService: jest.Mocked<QuickPickService>;
    let visualisationWidgetRegistry: jest.Mocked<VisualisationWidgetRegistry>;
    let displayViewService: jest.Mocked<DisplayViewService>;
    let displayViewResolver: jest.Mocked<DisplayViewResolver>;
    let commandRegistry: jest.Mocked<CommandRegistry>;

    beforeEach(() => {
        container = new Container();
        messageService = { error: jest.fn() } as unknown as jest.Mocked<MessageService>;
        connectionService = {} as unknown as jest.Mocked<ConnectionService>;
        quickPickService = { show: jest.fn() } as unknown as jest.Mocked<QuickPickService>;
        visualisationWidgetRegistry = { getWidgets: jest.fn() } as unknown as jest.Mocked<VisualisationWidgetRegistry>;
        displayViewService = { updateDisplayViewContent: jest.fn() } as unknown as jest.Mocked<DisplayViewService>;
        displayViewResolver = { getContent: jest.fn(), getWidget: jest.fn() } as unknown as jest.Mocked<DisplayViewResolver>;
        commandRegistry = { registerCommand: jest.fn() } as unknown as jest.Mocked<CommandRegistry>;

        container.bind(MessageService).toConstantValue(messageService);
        container.bind(ConnectionService).toConstantValue(connectionService);
        container.bind(QuickPickService).toConstantValue(quickPickService);
        container.bind(VisualisationWidgetRegistry).toConstantValue(visualisationWidgetRegistry);
        container.bind(DisplayViewService).toConstantValue(displayViewService);
        container.bind(DisplayViewResolver).toConstantValue(displayViewResolver);
        container.bind(VitruviusRefreshProjectContribution).toSelf();
        contribution = container.get(VitruviusRefreshProjectContribution);
    });

    it("should register the RefreshProjectCommand", () => {
        contribution.registerCommands(commandRegistry);
        expect(commandRegistry.registerCommand).toHaveBeenCalledWith(RefreshProjectCommand, expect.any(Object));
    });

    it("should show quick pick items when executing the command", async () => {
        const widgetData: WidgetData = {
            displayView: { name: "TestView" },
            widget: { getLabel: jest.fn().mockReturnValue("TestWidget"), close: jest.fn() },
            connection: { uuid: "test-uuid" }
        } as unknown as WidgetData;
        visualisationWidgetRegistry.getWidgets.mockReturnValue([widgetData]);
        displayViewResolver.getContent.mockResolvedValue({visualizerName: "TestVisualizer", windows: []});
        displayViewService.updateDisplayViewContent.mockResolvedValue({visualizerName: "TestVisualizer", windows: []});
        displayViewResolver.getWidget.mockResolvedValue(new TextWidget());

        contribution.registerCommands(commandRegistry);
        const commandHandler = commandRegistry.registerCommand.mock.calls[0][1] as { execute: () => Promise<void> };
        await commandHandler.execute();

        expect(quickPickService.show).toHaveBeenCalledWith(expect.arrayContaining([
            expect.objectContaining({ label: "TestWidget" })
        ]));
    });

});
