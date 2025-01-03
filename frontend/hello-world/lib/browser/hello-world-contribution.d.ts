import { Command, CommandContribution, CommandRegistry, MenuContribution, MenuModelRegistry, MessageService } from '@theia/core/lib/common';
export declare const HelloWorldCommand: Command;
export declare class HelloWorldCommandContribution implements CommandContribution {
    protected readonly messageService: MessageService;
    registerCommands(registry: CommandRegistry): void;
}
export declare class HelloWorldMenuContribution implements MenuContribution {
    registerMenus(menus: MenuModelRegistry): void;
}
//# sourceMappingURL=hello-world-contribution.d.ts.map