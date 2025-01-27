import {Command, CommandContribution, CommandRegistry, MessageService} from "@theia/core";
import {inject, injectable} from "@theia/core/shared/inversify";
import {ConnectionService} from "../../backend-communication/ConnectionService";

/**
 * Command to show the help dialog.
 */
export const HelpCommand: Command = {
    id: "VitruviusHelp.command",
    label: "Vitruvius Help",
};

/**
 * Command contribution to show the help dialog.
 */
@injectable()
export class VitruviusHelpCommandContribution implements CommandContribution {
    @inject(MessageService)
    protected readonly messageService!: MessageService;
    @inject(ConnectionService)
    protected readonly connectionService!: ConnectionService;

    /**
     * Register the command to show the help dialog.
     * @param registry The command registry to register the command.
     */
    registerCommands(registry: CommandRegistry): void {
        registry.registerCommand(HelpCommand, {
            // TODO: Implement the help dialog.
            execute: () => this.messageService.info("Vitruvius Help"),
        });
    }
}