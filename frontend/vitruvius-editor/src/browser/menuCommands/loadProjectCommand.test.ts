import {CommandHandler, CommandRegistry, MessageService, QuickPickService} from "@theia/core";
import { Container } from "@theia/core/shared/inversify";
import { ConnectionService } from "../../backend-communication/ConnectionService";
import { DisplayViewWidgetContribution } from "../displayViewWidgetContribution";
import { VisualisationWidgetRegistry } from "../../visualisation/VisualisationWidgetRegistry";
import { VitruviusLoadProjectContribution, LoadProjectCommand } from "./loadProjectCommand";
import { Connection } from "../../model/Connection";

describe("VitruviusLoadProjectContribution", () => {
    let container: Container;
    let contribution: VitruviusLoadProjectContribution;
    let messageService: jest.Mocked<MessageService>;
    let connectionService: jest.Mocked<ConnectionService>;
    let quickPickService: jest.Mocked<QuickPickService>;
    let displayViewWidgetContribution: jest.Mocked<DisplayViewWidgetContribution>;
    let visualisationWidgetRegistry: jest.Mocked<VisualisationWidgetRegistry>;
    let commandRegistry: jest.Mocked<CommandRegistry>;

    beforeEach(() => {
        container = new Container();
        messageService = { error: jest.fn() } as unknown as jest.Mocked<MessageService>;
        connectionService = { getConnections: jest.fn() } as unknown as jest.Mocked<ConnectionService>;
        quickPickService = { show: jest.fn() } as unknown as jest.Mocked<QuickPickService>;
        displayViewWidgetContribution = { widget: Promise.resolve({ getConnection: jest.fn(), loadProject: jest.fn() }) } as unknown as jest.Mocked<DisplayViewWidgetContribution>;
        visualisationWidgetRegistry = { getWidgetsByConnection: jest.fn() } as unknown as jest.Mocked<VisualisationWidgetRegistry>;
        commandRegistry = { registerCommand: jest.fn() } as unknown as jest.Mocked<CommandRegistry>;

        container.bind(MessageService).toConstantValue(messageService);
        container.bind(ConnectionService).toConstantValue(connectionService);
        container.bind(QuickPickService).toConstantValue(quickPickService);
        container.bind(DisplayViewWidgetContribution).toConstantValue(displayViewWidgetContribution);
        container.bind(VisualisationWidgetRegistry).toConstantValue(visualisationWidgetRegistry);
        container.bind(VitruviusLoadProjectContribution).toSelf();
        contribution = container.get(VitruviusLoadProjectContribution);
    });

    it("should register the LoadProjectCommand", () => {
        contribution.registerCommands(commandRegistry);
        expect(commandRegistry.registerCommand).toHaveBeenCalledWith(LoadProjectCommand, expect.any(Object));
    });

    it("should show quick pick items when executing the command", async () => {
        const connections: Connection[] = [{ name: "TestConnection", description: "TestDescription", url: "http://test.url", uuid: "test-uuid", port: 1234 }];
        connectionService.getConnections.mockResolvedValue(connections);

        contribution.registerCommands(commandRegistry);
        const commandHandler = commandRegistry.registerCommand.mock.calls[0][1] as CommandHandler;
        await commandHandler.execute();

        expect(connectionService.getConnections).toHaveBeenCalled();
        expect(quickPickService.show).toHaveBeenCalledWith(expect.arrayContaining([
            expect.objectContaining({ label: "TestConnection" })
        ]));
    });

    it("should show an error message if connection fails", async () => {
        connectionService.getConnections.mockRejectedValue(new Error("Connection error"));

        contribution.registerCommands(commandRegistry);
        const commandHandler = commandRegistry.registerCommand.mock.calls[0][1] as CommandHandler;
        await commandHandler.execute();

        expect(connectionService.getConnections).toHaveBeenCalled();
        expect(messageService.error).toHaveBeenCalledWith("Couldn't connect to backend.");
    });
});