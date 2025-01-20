import { injectable, inject } from '@theia/core/shared/inversify';
import { Command, CommandContribution, CommandRegistry, MAIN_MENU_BAR, MenuContribution, MenuModelRegistry, MenuPath, MessageService } from '@theia/core/lib/common';
import { CommonMenus } from '@theia/core/lib/browser';

// export namespace DiagramCommands {
//     export const CENTER = 'diagram:center';
//     export const FIT = 'diagram:fit';
//     export const EXPORT = 'diagram:export';
//     export const SELECT_ALL = 'diagram.selectAll';
//     export const OPEN_IN_DIAGRAM = 'diagram.open';
//     export const DELETE = 'diagram.delete';
//     export const LAYOUT = 'diagram.layout';
// }

export namespace VitruviusMenus {
    export const VITRUVIUS: MenuPath = MAIN_MENU_BAR.concat("vitruvius");
}

export const VitruviusHelpCommand: Command = {
    id: 'VitruviusHelp.command',
    label: 'Vitruvius Help'
};

export const VitruviusLoadProject: Command = {
    id: 'VitruviusLoadProject.command',
    label: 'Load Project'
}

export const VitruviusImportProject: Command = {
    id: 'VitruviusImportProject.command',
    label: 'Import Project'
}

export const VitruviusRefreshProject: Command = {
    id: 'VitruviusRefreshProject.command',
    label: 'Refresh Project'
}

export const VitruviusDeleteProject: Command = {
    id: 'VitruviusDeleteProject.command',
    label: 'Delete Project'
}

export const VitruviusChangeProject: Command = {
    id: 'VitruviusChangeProject.command',
    label: 'Change Project'
}

@injectable()
export class VitruviusHelpCommandContribution implements CommandContribution {
    
    @inject(MessageService)
    protected readonly messageService!: MessageService;
    registerCommands(registry: CommandRegistry): void {
        registry.registerCommand(VitruviusHelpCommand, {
            execute: () => this.messageService.info('Vitruvius Help')
        });
    }
}

@injectable()
export class VitruviusLoadProjectContribution implements CommandContribution {
    
    @inject(MessageService)
    protected readonly messageService!: MessageService;
    registerCommands(registry: CommandRegistry): void {
        registry.registerCommand(VitruviusLoadProject, {
            execute: () => this.messageService.info('Load Project')
        });
    }
}

@injectable()
export class VitruviusImportProjectContribution implements CommandContribution {
    
    @inject(MessageService)
    protected readonly messageService!: MessageService;
    registerCommands(registry: CommandRegistry): void {
        registry.registerCommand(VitruviusImportProject, {
            execute: () => this.messageService.info('Import Project')
        });
    }
}

@injectable()
export class VitruviusRefreshProjectContribution implements CommandContribution {
    
    @inject(MessageService)
    protected readonly messageService!: MessageService;
    registerCommands(registry: CommandRegistry): void {
        registry.registerCommand(VitruviusRefreshProject, {
            execute: () => this.messageService.info('Refresh Project')
        });
    }
}

@injectable()
export class VitruviusDeleteProjectContribution implements CommandContribution {
    
    @inject(MessageService)
    protected readonly messageService!: MessageService;
    registerCommands(registry: CommandRegistry): void {
        registry.registerCommand(VitruviusDeleteProject, {
            execute: () => this.messageService.info('Delete Project')
        });
    }
}

@injectable()
export class VitruviusChangeProjectContribution implements CommandContribution {
    
    @inject(MessageService)
    protected readonly messageService!: MessageService;
    registerCommands(registry: CommandRegistry): void {
        registry.registerCommand(VitruviusChangeProject, {
            execute: () => this.messageService.info('Change Project')
        });
    }
}

@injectable()
export class VitruviusSubmenuContribution implements MenuContribution {

    registerMenus(menus: MenuModelRegistry): void {
        menus.registerSubmenu(VitruviusMenus.VITRUVIUS, 'Vitruvius');

        menus.registerMenuAction(VitruviusMenus.VITRUVIUS, {
            commandId: VitruviusHelpCommand.id,
            label: VitruviusHelpCommand.label
        });
    }
}