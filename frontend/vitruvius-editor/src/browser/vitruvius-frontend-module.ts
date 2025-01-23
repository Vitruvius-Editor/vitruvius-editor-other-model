/*
 * Generated using theia-extension-generator
 */
import {FrontendApplicationContribution, WidgetFactory, bindViewContribution} from '@theia/core/lib/browser';
import { VitruviusEditProjectContribution, VitruviusDeleteProjectContribution, VitruviusHelpCommandContribution, VitruviusImportProjectContribution, VitruviusLoadProjectContribution, VitruviusRefreshProjectContribution, VitruviusMenuContribution } from './vitruvius-contribution';
import { CommandContribution, MenuContribution } from '@theia/core/lib/common';
import { ContainerModule } from '@theia/core/shared/inversify';
import {WidgetContribution} from './display-view-widget-contribution';
import {DisplayViewWidget} from './display-view-widget';

export default new ContainerModule(bind => {
    // add your contribution bindings here
    bind(CommandContribution).to(VitruviusHelpCommandContribution);
    bind(CommandContribution).to(VitruviusEditProjectContribution);
    bind(CommandContribution).to(VitruviusDeleteProjectContribution);
    bind(CommandContribution).to(VitruviusImportProjectContribution);
    bind(CommandContribution).to(VitruviusLoadProjectContribution);
    bind(CommandContribution).to(VitruviusRefreshProjectContribution);
    bind(MenuContribution).to(VitruviusMenuContribution);

	bindViewContribution(bind, WidgetContribution);
    bind(FrontendApplicationContribution).toService(WidgetContribution);
    bind(DisplayViewWidget).toSelf();
    bind(WidgetFactory).toDynamicValue(ctx => ({
        id: DisplayViewWidget.ID,
        createWidget: () => ctx.container.get<DisplayViewWidget>(DisplayViewWidget)
    })).inSingletonScope();

});
