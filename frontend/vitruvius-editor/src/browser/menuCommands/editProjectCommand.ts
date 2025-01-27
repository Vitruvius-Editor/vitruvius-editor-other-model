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
      // Request all available connections from the server.
      execute: () =>
        this.connectionService.getConnections().then((connections) => {
          // Convert the connections to quick pick items.
          let items = connections.map((connection) => {
            return {
              label: connection.name,
              execute: () => {
                // Ask the user for the new name, description and URL of the project via the quick input service.
                this.quickInputService
                  .input({
                    title: "Edit the projects name.",
                    value: connection.name,
                  })
                  .then((name) => {
                    this.quickInputService
                      .input({
                        title: "Edit the projects description.",
                        value: connection.description,
                      })
                      .then((description) => {
                        this.quickInputService
                          .input({
                            title: "Edit the projects URL",
                            value: connection.url,
                          })
                          .then((url) => {
                            // Update the connection with the new parameters and load the project if successful.
                            this.connectionService
                              .updateConnection(connection.uuid, {
                                name,
                                description,
                                url,
                              })
                              .then((newConnection) => {
                                this.messageService.info(
                                  "Project sucessfully updated.",
                                );
                                // Reload the project if the widget is currently displaying the updated connection.
                                this.displayViewWidgetContribution.widget.then(
                                  (widget) => {
                                    if (
                                      widget.getConnection()?.uuid ==
                                      newConnection.uuid
                                    ) {
                                      widget.loadProject(newConnection);
                                    }
                                  },
                                );
                              })
                              .catch((_err) =>
                                this.messageService.error(
                                  "Couldn't connect to Backend server.",
                                ),
                              );
                          });
                      });
                  });
              },
            };
          });
          // Show the quick pick items.
          this.quickPickService.show(items);
        }),
    });
  }
}
