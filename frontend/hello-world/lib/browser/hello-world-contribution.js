"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.HelloWorldMenuContribution = exports.HelloWorldCommandContribution = exports.HelloWorldCommand = void 0;
const inversify_1 = require("@theia/core/shared/inversify");
const common_1 = require("@theia/core/lib/common");
const browser_1 = require("@theia/core/lib/browser");
exports.HelloWorldCommand = {
    id: 'HelloWorld.command',
    label: 'Say Hello'
};
let HelloWorldCommandContribution = class HelloWorldCommandContribution {
    registerCommands(registry) {
        registry.registerCommand(exports.HelloWorldCommand, {
            execute: () => this.messageService.info('Hello World!')
        });
    }
};
exports.HelloWorldCommandContribution = HelloWorldCommandContribution;
__decorate([
    (0, inversify_1.inject)(common_1.MessageService),
    __metadata("design:type", common_1.MessageService)
], HelloWorldCommandContribution.prototype, "messageService", void 0);
exports.HelloWorldCommandContribution = HelloWorldCommandContribution = __decorate([
    (0, inversify_1.injectable)()
], HelloWorldCommandContribution);
let HelloWorldMenuContribution = class HelloWorldMenuContribution {
    registerMenus(menus) {
        menus.registerMenuAction(browser_1.CommonMenus.EDIT_FIND, {
            commandId: exports.HelloWorldCommand.id,
            label: exports.HelloWorldCommand.label
        });
    }
};
exports.HelloWorldMenuContribution = HelloWorldMenuContribution;
exports.HelloWorldMenuContribution = HelloWorldMenuContribution = __decorate([
    (0, inversify_1.injectable)()
], HelloWorldMenuContribution);
//# sourceMappingURL=hello-world-contribution.js.map