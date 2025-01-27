import {Command, CommandContribution, CommandRegistry, MessageService} from "@theia/core";
import {inject, injectable} from "@theia/core/shared/inversify";
import {ConnectionService} from "../../backend-communication/ConnectionService";

/**
 * Command to refresh the project and synchronise the changes with the Vitruvius server
 */
export const RefreshProjectCommand: Command = {
    id: 'RefreshProjectCommand.command',
    label: 'Vitruvius Refresh Project'
}

/**
 * Command contribution for the refresh project command.
 */
@injectable()
export class VitruviusRefreshProjectContribution
    implements CommandContribution {
    @inject(MessageService)
    protected readonly messageService!: MessageService;
    @inject(ConnectionService)
    protected readonly connectionService!: ConnectionService;

    /**
     * Register the refresh project command.
     * @param registry The command registry to register the command with.
     */
    registerCommands(registry: CommandRegistry): void {
        registry.registerCommand(RefreshProjectCommand, {
            execute: () => this.messageService.info("Refresh Project"),
        });
    }
}