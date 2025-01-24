import { injectable, inject } from '@theia/core/shared/inversify';
import { Command, CommandContribution, CommandRegistry, InputOptions, MAIN_MENU_BAR, MenuContribution, MenuModelRegistry, MenuPath, MessageService, QuickInputService, QuickPickService } from '@theia/core/lib/common';
import {ConnectionService} from '../backend-communication/ConnectionService';
import {DisplayViewWidgetContribution} from './display-view-widget-contribution';

export namespace VitruviusMenus {
    export const VITRUVIUS: MenuPath = MAIN_MENU_BAR.concat("vitruvius");
}

export const VitruviusHelpCommand: Command = {
  id: "VitruviusHelp.command",
  label: "Vitruvius Help",
};

export const VitruviusLoadProject: Command = {
    id: 'VitruviusLoadProject.command',
    label: 'Vitruvius Load Project'
}

export const VitruviusImportProject: Command = {
    id: 'VitruviusImportProject.command',
    label: 'Vitruvius Import Project'
}

export const VitruviusRefreshProject: Command = {
    id: 'VitruviusRefreshProject.command',
    label: 'Vitruvius Refresh Project'
}

export const VitruviusDeleteProject: Command = {
    id: 'VitruviusDeleteProject.command',
    label: 'Vitruvius Delete Project'
}

export const VitruviusEditProject: Command = {
    id: 'VitruviusEditProject.command',
    label: 'Vitruvius Edit Project'
}

@injectable()
export class VitruviusHelpCommandContribution implements CommandContribution {
  @inject(MessageService)
  protected readonly messageService!: MessageService;
  @inject(ConnectionService)
  protected readonly connectionService!: ConnectionService;
  registerCommands(registry: CommandRegistry): void {
    registry.registerCommand(VitruviusHelpCommand, {
      execute: () => this.messageService.info("Vitruvius Help"),
    });
  }
}

@injectable()
export class VitruviusLoadProjectContribution implements CommandContribution {
  @inject(MessageService)
  protected readonly messageService!: MessageService;
  @inject(ConnectionService)
  protected readonly connectionService!: ConnectionService;
  @inject(QuickPickService)
  protected readonly quickPickService!: QuickPickService;
  @inject(DisplayViewWidgetContribution)
  protected readonly displayViewWidgetContribution!: DisplayViewWidgetContribution;
  registerCommands(registry: CommandRegistry): void {
    registry.registerCommand(VitruviusLoadProject, {
      execute: () => this.connectionService.getConnections().then(res => {
		let items = res.map(connection => {
			return {
				label: connection.name,
				execute: () => this.displayViewWidgetContribution.loadProject(connection)
			}
		})
		this.quickPickService.show(items);
	  }).catch(_err => this.messageService.error("Couldn't connect to backend.")),
    });
  }
}

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
    registry.registerCommand(VitruviusImportProject, {
      execute: () => {
		this.quickInputService.input({title: 'Enter the projects name.'}).then(name => {
			this.quickInputService.input({title: 'Enter a description for the project.'}).then(description => {
				this.quickInputService.input({title: 'Enter the URL of the Vitruvius server.'}).then(url => {
					this.connectionService.createConnection({name: name ?? '', description: description ?? '', url: url ?? ''}).then(connection => {
						this.displayViewWidgetContribution.loadProject(connection);
					}).catch(_err => this.messageService.error("Couldn't connect to backend."))
				})
			})
		});
	  },
    });
  }
}

@injectable()
export class VitruviusRefreshProjectContribution
  implements CommandContribution
{
  @inject(MessageService)
  protected readonly messageService!: MessageService;
  @inject(ConnectionService)
  protected readonly connectionService!: ConnectionService;
  registerCommands(registry: CommandRegistry): void {
    registry.registerCommand(VitruviusRefreshProject, {
      execute: () => this.messageService.info("Refresh Project"),
    });
  }
}

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

  registerCommands(registry: CommandRegistry): void {
    registry.registerCommand(VitruviusDeleteProject, {
      execute: () => this.connectionService.getConnections().then(connections => {
		let items = connections.map(connection => {
			return {
				label: connection.name,
				execute: () => {
					this.connectionService.deleteConnection(connection.uuid);
					this.messageService.info("Project deleted.");
					this.displayViewWidgetContribution.widget.then(widget => {
						if (widget.getConnection() == connection) {
							widget.loadProject(null);
						}
					})
				}
			}
		});
		this.quickPickService.show(items);
	  }).catch(_err => this.messageService.error("Couldn't connect to the Backend.")),
    });
  }
}

@injectable()
export class VitruviusEditProjectContribution implements CommandContribution {
  @inject(MessageService)
  protected readonly messageService!: MessageService;
  @inject(ConnectionService)
  protected readonly connectionService!: ConnectionService;
  registerCommands(registry: CommandRegistry): void {
    registry.registerCommand(VitruviusEditProject, {
      execute: () => this.messageService.info("Edit Project"),
    });
  }
}

@injectable()
export class VitruviusMenuContribution implements MenuContribution {

    registerMenus(menus: MenuModelRegistry): void {
        menus.registerSubmenu(VitruviusMenus.VITRUVIUS, 'Vitruvius');

        menus.registerMenuAction(VitruviusMenus.VITRUVIUS, {
            commandId: VitruviusHelpCommand.id,
            label: VitruviusHelpCommand.label
        });
        menus.registerMenuAction(VitruviusMenus.VITRUVIUS, {
            commandId: VitruviusLoadProject.id,
            label: VitruviusLoadProject.label
        });
        menus.registerMenuAction(VitruviusMenus.VITRUVIUS, {
            commandId: VitruviusImportProject.id,
            label: VitruviusImportProject.label
        });
        menus.registerMenuAction(VitruviusMenus.VITRUVIUS, {
            commandId: VitruviusRefreshProject.id,
            label: VitruviusRefreshProject.label
        });
        menus.registerMenuAction(VitruviusMenus.VITRUVIUS, {
            commandId: VitruviusDeleteProject.id,
            label: VitruviusDeleteProject.label
        });
        menus.registerMenuAction(VitruviusMenus.VITRUVIUS, {
            commandId: VitruviusEditProject.id,
            label: VitruviusEditProject.label
        });
    }
}
