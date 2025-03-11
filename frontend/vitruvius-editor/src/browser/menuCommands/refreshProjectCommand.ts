import {
  Command,
  CommandContribution,
  CommandRegistry,
  MessageService,
  QuickPickService,
} from "@theia/core";
import { inject, injectable } from "@theia/core/shared/inversify";
import { ConnectionService } from "../../backend-communication/ConnectionService";
import {VisualisationWidgetRegistry} from "../../visualisation/VisualisationWidgetRegistry";
import {DisplayViewService} from "../../backend-communication/DisplayViewService";
import {DisplayViewResolver} from "../../visualisation/DisplayViewResolver";
import {VisualisationWidget} from "../../visualisation/VisualisationWidget";

/**
 * Command to refresh the project and synchronise the changes with the Vitruvius server
 */
export const RefreshProjectCommand: Command = {
  id: "RefreshProjectCommand.command",
  label: "Vitruvius Refresh Project",
};

/**
 * Command contribution for the refresh project command.
 */
@injectable()
export class VitruviusRefreshProjectContribution implements CommandContribution {
  @inject(MessageService)
  protected readonly messageService!: MessageService;
  @inject(ConnectionService)
  protected readonly connectionService!: ConnectionService;
  @inject(QuickPickService)
  protected readonly quickPickService!: QuickPickService;
  @inject(VisualisationWidgetRegistry)
  protected readonly visualisationWidgetRegistry!: VisualisationWidgetRegistry;
  @inject(DisplayViewService)
  protected readonly displayViewService!: DisplayViewService;
  @inject(DisplayViewResolver)
  protected readonly displayViewResolver!: DisplayViewResolver;

  /**
   * Register the refresh project command.
   * @param registry The command registry to register the command with.
   */
  registerCommands(registry: CommandRegistry): void {
    registry.registerCommand(RefreshProjectCommand, {
      execute: async () => {
        const items = this.visualisationWidgetRegistry.getWidgets().map(widgetData => {
          return {
            label: `${widgetData.widget.getLabel()}`,
            execute: async () => {
              try {
                const content = await this.displayViewResolver.getContent(widgetData.widget);
                if (content) {
                  const res = await this.displayViewService.updateDisplayViewContent(widgetData.connection.uuid, widgetData.displayView.name, content);
                  if (res !== null) {
                    widgetData.widget.close();
                    const widget = await this.displayViewResolver.getWidget(res) as VisualisationWidget<any>;
                    this.visualisationWidgetRegistry.registerWidget(widget, widgetData.displayView, widgetData.connection);
                    widget.show();
                  } else {
                    await this.messageService.error("Invalid update!");
                  }
                }
              } catch (error) {
                this.messageService.error("Error updating the window.");
              }
            }
          };
        });
        this.quickPickService.show(items);
      }
    });
  }
}
