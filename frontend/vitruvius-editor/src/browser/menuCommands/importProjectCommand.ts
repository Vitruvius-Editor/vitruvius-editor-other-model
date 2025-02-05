import {
  Command,
  CommandContribution,
  CommandRegistry,
  MessageService,
  QuickInputService,
} from "@theia/core";
import { inject, injectable } from "@theia/core/shared/inversify";
import { ConnectionService } from "../../backend-communication/ConnectionService";
import { DisplayViewWidgetContribution } from "../displayViewWidgetContribution";

/**
 * Command to import a new project into the server.
 */
export const ImportProjectCommand: Command = {
  id: "ImportProjectCommand.command",
  label: "Vitruvius Import Project",
};

/**
 * Command contribution to import a new project into the server.
 */
@injectable()
export class VitruviusImportProjectContribution implements CommandContribution {
  @inject(MessageService)
  protected readonly messageService!: MessageService;
  @inject(ConnectionService)
  protected readonly connectionService!: ConnectionService;
  @inject(QuickInputService)
  protected readonly quickInputService!: QuickInputService;
  @inject(DisplayViewWidgetContribution)
  protected readonly displayViewWidgetContribution!: DisplayViewWidgetContribution;

  registerCommands(registry: CommandRegistry): void {
    registry.registerCommand(ImportProjectCommand, {
      execute: async () => {
        try {
          const name = await this.quickInputService.input({ title: "Enter the project's name." });
          const description = await this.quickInputService.input({ title: "Enter a description for the project." });
          const hostname = await this.quickInputService.input({ title: "Enter the hostname of the Vitruvius server." });
          const port = await this.quickInputService.input({ title: "Enter the port of the Vitruvius server" });

          if (name && description && hostname && port) {
            const connection = await this.connectionService.createConnection({
              name,
              description,
              url: hostname,
              port: parseInt(port),
            });

            const widget = await this.displayViewWidgetContribution.widget;
            await widget.loadProject(connection);
          } else {
            await this.messageService.error("All fields are required.");
          }
        } catch (error) {
          await this.messageService.error("Couldn't connect to backend.");
        }
      },
    });
  }
}