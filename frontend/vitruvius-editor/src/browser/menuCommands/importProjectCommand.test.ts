import { CommandRegistry, MessageService, QuickInputService } from "@theia/core";
import { Container } from "@theia/core/shared/inversify";
import { ConnectionService } from "../../backend-communication/ConnectionService";
import { DisplayViewWidgetContribution } from "../displayViewWidgetContribution";
import { VitruviusImportProjectContribution, ImportProjectCommand } from "./importProjectCommand";

describe("VitruviusImportProjectContribution", () => {
    let container: Container;
    let contribution: VitruviusImportProjectContribution;
    let messageService: jest.Mocked<MessageService>;
    let connectionService: jest.Mocked<ConnectionService>;
    let quickInputService: jest.Mocked<QuickInputService>;
    let displayViewWidgetContribution: jest.Mocked<DisplayViewWidgetContribution>;
    let commandRegistry: jest.Mocked<CommandRegistry>;

    beforeEach(() => {
        container = new Container();
        messageService = { error: jest.fn() } as unknown as jest.Mocked<MessageService>;
        connectionService = { createConnection: jest.fn() } as unknown as jest.Mocked<ConnectionService>;
        quickInputService = { input: jest.fn() } as unknown as jest.Mocked<QuickInputService>;
        displayViewWidgetContribution = { widget: Promise.resolve({ loadProject: jest.fn() }) } as unknown as jest.Mocked<DisplayViewWidgetContribution>;
        commandRegistry = { registerCommand: jest.fn() } as unknown as jest.Mocked<CommandRegistry>;

        container.bind(MessageService).toConstantValue(messageService);
        container.bind(ConnectionService).toConstantValue(connectionService);
        container.bind(QuickInputService).toConstantValue(quickInputService);
        container.bind(DisplayViewWidgetContribution).toConstantValue(displayViewWidgetContribution);
        container.bind(VitruviusImportProjectContribution).toSelf();
        contribution = container.get(VitruviusImportProjectContribution);
    });

    it("should register the ImportProjectCommand", () => {
        contribution.registerCommands(commandRegistry);
        expect(commandRegistry.registerCommand).toHaveBeenCalledWith(ImportProjectCommand, expect.any(Object));
    });

    it("should show input prompts and create a connection when executing the command", async () => {
        quickInputService.input
            .mockResolvedValueOnce("TestProject")
            .mockResolvedValueOnce("TestDescription")
            .mockResolvedValueOnce("http://test.url")
            .mockResolvedValueOnce("1234");

        const connection = { name: "TestProject", description: "TestDescription", url: "http://test.url", port: 1234, uuid: "test-uuid" };
        connectionService.createConnection.mockResolvedValue(connection);

        contribution.registerCommands(commandRegistry);
        const commandHandler = commandRegistry.registerCommand.mock.calls[0][1] as { execute: () => Promise<void> };
        await commandHandler.execute();

        expect(quickInputService.input).toHaveBeenCalledTimes(4);
        expect(connectionService.createConnection).toHaveBeenCalledWith({name: "TestProject", description: "TestDescription", url: "http://test.url", port: 1234});
        const widget = await displayViewWidgetContribution.widget;
        expect(widget.loadProject).toHaveBeenCalledWith(connection);
    });

    it("should show an error message if any input is missing", async () => {
        quickInputService.input.mockResolvedValueOnce("TestProject").mockResolvedValueOnce("").mockResolvedValueOnce("http://test.url").mockResolvedValueOnce("1234");

        contribution.registerCommands(commandRegistry);
        const commandHandler = commandRegistry.registerCommand.mock.calls[0][1] as { execute: () => Promise<void> };
        await commandHandler.execute();

        expect(messageService.error).toHaveBeenCalledWith("All fields are required.");
    });

    it("should show an error message if there is an exception", async () => {
        quickInputService.input.mockRejectedValue(new Error("Input error"));

        contribution.registerCommands(commandRegistry);
        const commandHandler = commandRegistry.registerCommand.mock.calls[0][1] as { execute: () => Promise<void> };
        await commandHandler.execute();

        expect(messageService.error).toHaveBeenCalledWith("Couldn't connect to backend.");
    });
});