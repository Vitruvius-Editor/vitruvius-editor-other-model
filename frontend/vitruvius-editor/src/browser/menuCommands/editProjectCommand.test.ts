import { CommandRegistry, MessageService, QuickInputService, QuickPickService } from "@theia/core";
import { Container } from "@theia/core/shared/inversify";
import { ConnectionService } from "../../backend-communication/ConnectionService";
import { DisplayViewWidgetContribution } from "../displayViewWidgetContribution";
import { VitruviusEditProjectContribution, EditProjectCommand } from "./editProjectCommand";

describe("VitruviusEditProjectContribution", () => {
    let container: Container;
    let contribution: VitruviusEditProjectContribution;
    let messageService: jest.Mocked<MessageService>;
    let connectionService: jest.Mocked<ConnectionService>;
    let quickInputService: jest.Mocked<QuickInputService>;
    let quickPickService: jest.Mocked<QuickPickService>;
    let displayViewWidgetContribution: jest.Mocked<DisplayViewWidgetContribution>;
    let commandRegistry: jest.Mocked<CommandRegistry>;

    beforeEach(() => {
        container = new Container();
        messageService = { error: jest.fn(), info: jest.fn() } as unknown as jest.Mocked<MessageService>;
        connectionService = { getConnections: jest.fn(), updateConnection: jest.fn() } as unknown as jest.Mocked<ConnectionService>;
        quickInputService = { input: jest.fn() } as unknown as jest.Mocked<QuickInputService>;
        quickPickService = { show: jest.fn() } as unknown as jest.Mocked<QuickPickService>;
        displayViewWidgetContribution = { widget: Promise.resolve({ loadProject: jest.fn(), getConnection: jest.fn() }) } as unknown as jest.Mocked<DisplayViewWidgetContribution>;
        commandRegistry = { registerCommand: jest.fn() } as unknown as jest.Mocked<CommandRegistry>;

        container.bind(MessageService).toConstantValue(messageService);
        container.bind(ConnectionService).toConstantValue(connectionService);
        container.bind(QuickInputService).toConstantValue(quickInputService);
        container.bind(QuickPickService).toConstantValue(quickPickService);
        container.bind(DisplayViewWidgetContribution).toConstantValue(displayViewWidgetContribution);
        container.bind(VitruviusEditProjectContribution).toSelf();
        contribution = container.get(VitruviusEditProjectContribution);
    });

    it("should register the EditProjectCommand", () => {
        contribution.registerCommands(commandRegistry);
        expect(commandRegistry.registerCommand).toHaveBeenCalledWith(EditProjectCommand, expect.any(Object));
    });

    it("should show quick pick items and update the project when executing the command", async () => {
        const connections = [{ uuid: "test-uuid", name: "TestProject", description: "TestDescription", url: "http://test.url", port: 1245 }];
        connectionService.getConnections.mockResolvedValue(connections);
        quickInputService.input
            .mockResolvedValueOnce("NewTestProject")
            .mockResolvedValueOnce("NewTestDescription")
            .mockResolvedValueOnce("http://newtest.url")
            .mockResolvedValueOnce("1245");
        connectionService.updateConnection.mockResolvedValue({ uuid: "test-uuid", name: "NewTestProject", description: "NewTestDescription", url: "http://newtest.url", port: 1245 });

        contribution.registerCommands(commandRegistry);
        const commandHandler = commandRegistry.registerCommand.mock.calls[0][1] as { execute: () => Promise<void> };
        await commandHandler.execute();

        expect(quickPickService.show).toHaveBeenCalledWith(expect.arrayContaining([
            expect.objectContaining({ label: "TestProject" })
        ]));
    });

    it("should show an error message if there is an exception when retrieving connections", async () => {
        connectionService.getConnections.mockRejectedValue(new Error("Connection error"));

        contribution.registerCommands(commandRegistry);
        const commandHandler = commandRegistry.registerCommand.mock.calls[0][1] as { execute: () => Promise<void> };
        await commandHandler.execute();

        expect(messageService.error).toHaveBeenCalledWith("Couldn't retrieve connections.");
    });

});