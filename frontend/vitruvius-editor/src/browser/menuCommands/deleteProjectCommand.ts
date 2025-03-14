import {
  Command,
  CommandContribution,
  CommandRegistry,
  MessageService,
  QuickPickService,
} from "@theia/core";
import { inject, injectable } from "@theia/core/shared/inversify";
import { DisplayViewWidgetContribution } from "../displayViewWidgetContribution";
import { ConnectionService } from "../../backend-communication/ConnectionService";
import { VisualisationWidgetRegistry } from "../../visualisation/VisualisationWidgetRegistry";

/**
 * Command to delete a project.
 */
export const DeleteProjectCommand: Command = {
  id: "DeleteProjectCommand.command",
  label: "Vitruvius Delete Project",
};

/**
 * Command contribution to delete a project.
 */
@injectable()
export class VitruviusDeleteProjectContribution implements CommandContribution {
  @inject(MessageService)
  protected readonly messageService!: MessageService;
  @inject(QuickPickService)
  protected readonly quickPickService!: QuickPickService;
  @inject(DisplayViewWidgetContribution)
  protected readonly displayViewWidgetContribution!: DisplayViewWidgetContribution;
  @inject(ConnectionService)
  protected readonly connectionService!: ConnectionService;
  @inject(VisualisationWidgetRegistry)
  protected readonly visualisationWidgetRegistry!: VisualisationWidgetRegistry;

  /**
   * Register the command to delete a project.
   * @param registry The command registry to register the command.
   */
  registerCommands(registry: CommandRegistry): void {
    registry.registerCommand(DeleteProjectCommand, {
      execute: async () => {
        try {
          const connections = await this.connectionService.getConnections();
          const items = connections.map((connection) => ({
            label: connection.name,
            execute: async () => {
              try {
                await this.connectionService.deleteConnection(connection.uuid);
                const widget = await this.displayViewWidgetContribution.widget;
                if (widget.getConnection()?.uuid === connection.uuid) {
                  this.visualisationWidgetRegistry
                    .getWidgetsByConnection(connection)
                    .forEach((widgetData) => widgetData.widget.close());
                  await widget.loadProject(null);
                }
                await this.messageService.info("Project deleted.");
              } catch (error) {
                await this.messageService.error(
                  "Couldn't connect to the Backend server.",
                );
              }
            },
          }));
          await this.quickPickService.show(items);
        } catch (error) {
          await this.messageService.error("Couldn't connect to the Backend.");
        }
      },
    });
  }
}
