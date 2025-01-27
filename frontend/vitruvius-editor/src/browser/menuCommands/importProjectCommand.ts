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

  /**
   * Register the command to import a new project into the server.
   * @param registry The command registry to register the command.
   */
  registerCommands(registry: CommandRegistry): void {
    registry.registerCommand(ImportProjectCommand, {
      execute: () => {
        // Ask the user for the projects name, description and the URL of the Vitruvius server via the quick input service.
        this.quickInputService
          .input({ title: "Enter the projects name." })
          .then((name) => {
            this.quickInputService
              .input({ title: "Enter a description for the project." })
              .then((description) => {
                this.quickInputService
                  .input({ title: "Enter the URL of the Vitruvius server." })
                  .then((url) => {
                    // Create a new connection with the given parameters and load the project if successful.
                    this.connectionService
                      .createConnection({
                        name: name ?? "",
                        description: description ?? "",
                        url: url ?? "",
                      })
                      .then((connection) => {
                        this.displayViewWidgetContribution.widget.then(
                          (widget) => widget.loadProject(connection),
                        );
                      })
                      .catch((_err) =>
                        this.messageService.error(
                          "Couldn't connect to backend.",
                        ),
                      );
                  });
              });
          });
      },
    });
  }
}
