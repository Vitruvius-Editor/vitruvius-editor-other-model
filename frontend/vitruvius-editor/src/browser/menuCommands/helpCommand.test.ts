import { CommandRegistry } from "@theia/core";
import { Container } from "@theia/core/shared/inversify";
import { HelpWidgetContribution } from "../helpWidgetContribution";
import { VitruviusHelpCommandContribution, HelpCommand } from "./helpCommand";

describe("VitruviusHelpCommandContribution", () => {
    let container: Container;
    let contribution: VitruviusHelpCommandContribution;
    let helpWidgetContribution: jest.Mocked<HelpWidgetContribution>;
    let commandRegistry: jest.Mocked<CommandRegistry>;

    beforeEach(() => {
        container = new Container();
        helpWidgetContribution = { openView: jest.fn() } as unknown as jest.Mocked<HelpWidgetContribution>;
        commandRegistry = { registerCommand: jest.fn() } as unknown as jest.Mocked<CommandRegistry>;

        container.bind(HelpWidgetContribution).toConstantValue(helpWidgetContribution);
        container.bind(VitruviusHelpCommandContribution).toSelf();
        contribution = container.get(VitruviusHelpCommandContribution);
    });

    it("should register the HelpCommand", () => {
        contribution.registerCommands(commandRegistry);
        expect(commandRegistry.registerCommand).toHaveBeenCalledWith(HelpCommand, expect.any(Object));
    });

    it("should open the help widget when executing the command", async () => {
        contribution.registerCommands(commandRegistry);
        const commandHandler = commandRegistry.registerCommand.mock.calls[0][1] as { execute: () => Promise<void> };
        await commandHandler.execute();

        expect(helpWidgetContribution.openView).toHaveBeenCalled();
    });
});