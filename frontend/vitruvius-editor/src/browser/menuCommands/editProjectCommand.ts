import {
    Command,
    CommandContribution,
    CommandRegistry,
    MessageService,
    QuickInputService,
    QuickPickService,
} from "@theia/core";
import { inject, injectable } from "@theia/core/shared/inversify";
import { ConnectionService } from "../../backend-communication/ConnectionService";
import { DisplayViewWidgetContribution } from "../displayViewWidgetContribution";

/**
 * Command to edit a project.
 */
export const EditProjectCommand: Command = {
    id: "EditProjectCommand.command",
    label: "Vitruvius Edit Project",
};

/**
 * Command contribution to edit a project.
 */
@injectable()
export class VitruviusEditProjectContribution implements CommandContribution {
    @inject(MessageService)
    protected readonly messageService!: MessageService;
    @inject(ConnectionService)
    protected readonly connectionService!: ConnectionService;
    @inject(QuickInputService)
    protected readonly quickInputService!: QuickInputService;
    @inject(QuickPickService)
    protected readonly quickPickService!: QuickPickService;
    @inject(DisplayViewWidgetContribution)
    protected readonly displayViewWidgetContribution!: DisplayViewWidgetContribution;

    /**
     * Register the command to edit a project.
     * @param registry The command registry to register the command.
     */
    registerCommands(registry: CommandRegistry): void {
        registry.registerCommand(EditProjectCommand, {
            execute: async () => {
                try {
                    const connections = await this.connectionService.getConnections();
                    const items = connections.map((connection) => ({
                        label: connection.name,
                        execute: async () => {
                            try {
                                const name = await this.quickInputService.input({
                                    title: "Edit the project's name.",
                                    value: connection.name,
                                });
                                const description = await this.quickInputService.input({
                                    title: "Edit the project's description.",
                                    value: connection.description,
                                });
                                const url = await this.quickInputService.input({
                                    title: "Edit the project's URL",
                                    value: connection.url,
                                });

                                const port = parseInt(await this.quickInputService.input({
                                    title: "Edit the project's port",
                                    value: connection.port.toString(),
                                }) as string);

                                const newConnection = await this.connectionService.updateConnection(connection.uuid, {
                                    name,
                                    description,
                                    url,
                                    port,
                                });

                                await this.messageService.info("Project successfully updated.");

                                const widget = await this.displayViewWidgetContribution.widget;
                                if (widget.getConnection()?.uuid === newConnection.uuid) {
                                    await widget.loadProject(newConnection);
                                }
                            } catch (error) {
                                await this.messageService.error("Couldn't connect to Backend server.");
                            }
                        },
                    }));
                    await this.quickPickService.show(items);
                } catch (error) {
                    await this.messageService.error("Couldn't retrieve connections.");
                }
            },
        });
    }
}
