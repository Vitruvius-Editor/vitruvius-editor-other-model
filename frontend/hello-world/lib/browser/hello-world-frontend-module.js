"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
/**
 * Generated using theia-extension-generator
 */
const hello_world_contribution_1 = require("./hello-world-contribution");
const common_1 = require("@theia/core/lib/common");
const inversify_1 = require("@theia/core/shared/inversify");
exports.default = new inversify_1.ContainerModule(bind => {
    // add your contribution bindings here
    bind(common_1.CommandContribution).to(hello_world_contribution_1.HelloWorldCommandContribution);
    bind(common_1.MenuContribution).to(hello_world_contribution_1.HelloWorldMenuContribution);
});
//# sourceMappingURL=hello-world-frontend-module.js.map