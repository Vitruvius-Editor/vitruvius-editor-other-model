/**
 * Generated using theia-extension-generator
 */
import { VitruviusChangeProjectContribution, VitruviusDeleteProjectContribution, VitruviusHelpCommandContribution, VitruviusImportProjectContribution, VitruviusLoadProjectContribution, VitruviusRefreshProjectContribution, VitruviusMenuContribution } from './vitruvius-contribution';
import { CommandContribution, MenuContribution } from '@theia/core/lib/common';
import { ContainerModule } from '@theia/core/shared/inversify';

export default new ContainerModule(bind => {
    // add your contribution bindings here
    bind(CommandContribution).to(VitruviusHelpCommandContribution);
    bind(CommandContribution).to(VitruviusChangeProjectContribution);
    bind(CommandContribution).to(VitruviusDeleteProjectContribution);
    bind(CommandContribution).to(VitruviusImportProjectContribution);
    bind(CommandContribution).to(VitruviusLoadProjectContribution);
    bind(CommandContribution).to(VitruviusRefreshProjectContribution);
    bind(MenuContribution).to(VitruviusMenuContribution);
});