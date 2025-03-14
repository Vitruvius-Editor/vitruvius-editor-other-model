import { Command, CommandContribution, CommandRegistry } from "@theia/core";
import { inject, injectable } from "@theia/core/shared/inversify";
import { HelpWidgetContribution } from "../helpWidgetContribution";

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
  @inject(HelpWidgetContribution)
  protected readonly helpWidgetContribution!: HelpWidgetContribution;

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
    await this.helpWidgetContribution.openView();
  }
}
