import {
  Command,
  CommandContribution,
  CommandRegistry,
} from "@theia/core";
import { inject, injectable } from "@theia/core/shared/inversify";
import { HelpWidget } from "../helpWidget";
import { WidgetManager} from "@theia/core/lib/browser";

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
  @inject(WidgetManager)
  protected readonly widgetManager!: WidgetManager;

  /**
   * Register the command to show the help dialog.
   * @param registry The command registry to register the command.
   */
  registerCommands(registry: CommandRegistry): void {
    registry.registerCommand(HelpCommand, {
      execute: () => this.openHelpWidget(),
    });
  }

  protected async openHelpWidget(): Promise<void> {
    const widget = await this.widgetManager.getOrCreateWidget<HelpWidget>(HelpWidget.ID);
    widget.show();
  }
}
