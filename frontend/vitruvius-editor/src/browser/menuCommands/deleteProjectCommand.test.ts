import {
  CommandRegistry,
  MessageService,
  QuickPickItem,
  QuickPickService,
} from "@theia/core";
import { Container } from "@theia/core/shared/inversify";
import { ConnectionService } from "../../backend-communication/ConnectionService";
import { DisplayViewWidgetContribution } from "../displayViewWidgetContribution";
import {
  VisualisationWidgetRegistry,
  WidgetData,
} from "../../visualisation/VisualisationWidgetRegistry";
import {
  VitruviusDeleteProjectContribution,
  DeleteProjectCommand,
} from "./deleteProjectCommand";
import { VisualisationWidget } from "../../visualisation/VisualisationWidget";

describe("VitruviusDeleteProjectContribution", () => {
  let container: Container;
  let contribution: VitruviusDeleteProjectContribution;
  let messageService: jest.Mocked<MessageService>;
  let connectionService: jest.Mocked<ConnectionService>;
  let quickPickService: jest.Mocked<QuickPickService>;
  let displayViewWidgetContribution: jest.Mocked<DisplayViewWidgetContribution>;
  let visualisationWidgetRegistry: jest.Mocked<VisualisationWidgetRegistry>;
  let commandRegistry: jest.Mocked<CommandRegistry>;

  beforeEach(() => {
    container = new Container();
    messageService = {
      error: jest.fn(),
      info: jest.fn(),
    } as unknown as jest.Mocked<MessageService>;
    connectionService = {
      getConnections: jest.fn(),
      deleteConnection: jest.fn(),
    } as unknown as jest.Mocked<ConnectionService>;
    quickPickService = {
      show: jest.fn(),
    } as unknown as jest.Mocked<QuickPickService>;
    displayViewWidgetContribution = {
      widget: Promise.resolve({
        loadProject: jest.fn(),
        getConnection: jest.fn(),
      }),
    } as unknown as jest.Mocked<DisplayViewWidgetContribution>;
    visualisationWidgetRegistry = {
      getWidgetsByConnection: jest.fn(),
    } as unknown as jest.Mocked<VisualisationWidgetRegistry>;
    commandRegistry = {
      registerCommand: jest.fn(),
    } as unknown as jest.Mocked<CommandRegistry>;

    container.bind(MessageService).toConstantValue(messageService);
    container.bind(ConnectionService).toConstantValue(connectionService);
    container.bind(QuickPickService).toConstantValue(quickPickService);
    container
      .bind(DisplayViewWidgetContribution)
      .toConstantValue(displayViewWidgetContribution);
    container
      .bind(VisualisationWidgetRegistry)
      .toConstantValue(visualisationWidgetRegistry);
    container.bind(VitruviusDeleteProjectContribution).toSelf();
    contribution = container.get(VitruviusDeleteProjectContribution);
  });

  it("should register the DeleteProjectCommand", () => {
    contribution.registerCommands(commandRegistry);
    expect(commandRegistry.registerCommand).toHaveBeenCalledWith(
      DeleteProjectCommand,
      expect.any(Object),
    );
  });

  it("should show quick pick items and delete the project when executing the command", async () => {
    const connections = [
      {
        uuid: "test-uuid",
        name: "TestProject",
        description: "TestDescription",
        url: "http://test.url",
        port: 1245,
      },
    ];
    connectionService.getConnections.mockResolvedValue(connections);
    visualisationWidgetRegistry.getWidgetsByConnection.mockReturnValue([
      {
        widget: { close: jest.fn() } as unknown as VisualisationWidget<any>,
      } as unknown as WidgetData,
    ]);

    contribution.registerCommands(commandRegistry);
    const commandHandler = commandRegistry.registerCommand.mock.calls[0][1] as {
      execute: () => Promise<void>;
    };
    await commandHandler.execute();

    expect(quickPickService.show).toHaveBeenCalledWith(
      expect.arrayContaining([
        expect.objectContaining({ label: "TestProject" }),
      ]),
    );
    const item = quickPickService.show.mock.calls[0][0][0] as QuickPickItem;
    // @ts-ignore
    item.execute();

    expect(connectionService.deleteConnection).toHaveBeenCalledWith(
      "test-uuid",
    );
  });

  it("should show an error message if there is an exception while retrieving connections", async () => {
    connectionService.getConnections.mockRejectedValue(
      new Error("Connection error"),
    );

    contribution.registerCommands(commandRegistry);
    const commandHandler = commandRegistry.registerCommand.mock.calls[0][1] as {
      execute: () => Promise<void>;
    };
    await commandHandler.execute();

    expect(messageService.error).toHaveBeenCalledWith(
      "Couldn't connect to the Backend.",
    );
  });
});
