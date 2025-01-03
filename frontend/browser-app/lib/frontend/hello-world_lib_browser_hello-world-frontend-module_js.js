"use strict";
(self["webpackChunkbrowser_app"] = self["webpackChunkbrowser_app"] || []).push([["hello-world_lib_browser_hello-world-frontend-module_js"],{

/***/ "../hello-world/lib/browser/hello-world-contribution.js":
/*!**************************************************************!*\
  !*** ../hello-world/lib/browser/hello-world-contribution.js ***!
  \**************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
exports.HelloWorldMenuContribution = exports.HelloWorldCommandContribution = exports.HelloWorldCommand = void 0;
const inversify_1 = __webpack_require__(/*! @theia/core/shared/inversify */ "../node_modules/@theia/core/shared/inversify/index.js");
const common_1 = __webpack_require__(/*! @theia/core/lib/common */ "../node_modules/@theia/core/lib/common/index.js");
const browser_1 = __webpack_require__(/*! @theia/core/lib/browser */ "../node_modules/@theia/core/lib/browser/index.js");
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


/***/ }),

/***/ "../hello-world/lib/browser/hello-world-frontend-module.js":
/*!*****************************************************************!*\
  !*** ../hello-world/lib/browser/hello-world-frontend-module.js ***!
  \*****************************************************************/
/***/ ((__unused_webpack_module, exports, __webpack_require__) => {


Object.defineProperty(exports, "__esModule", ({ value: true }));
/**
 * Generated using theia-extension-generator
 */
const hello_world_contribution_1 = __webpack_require__(/*! ./hello-world-contribution */ "../hello-world/lib/browser/hello-world-contribution.js");
const common_1 = __webpack_require__(/*! @theia/core/lib/common */ "../node_modules/@theia/core/lib/common/index.js");
const inversify_1 = __webpack_require__(/*! @theia/core/shared/inversify */ "../node_modules/@theia/core/shared/inversify/index.js");
exports["default"] = new inversify_1.ContainerModule(bind => {
    // add your contribution bindings here
    bind(common_1.CommandContribution).to(hello_world_contribution_1.HelloWorldCommandContribution);
    bind(common_1.MenuContribution).to(hello_world_contribution_1.HelloWorldMenuContribution);
});


/***/ })

}]);
//# sourceMappingURL=hello-world_lib_browser_hello-world-frontend-module_js.js.map