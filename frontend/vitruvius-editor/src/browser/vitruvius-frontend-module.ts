/**
 * Generated using theia-extension-generator
 */
import {FrontendApplicationContribution, WidgetFactory, bindViewContribution} from '@theia/core/lib/browser';
import { VitruviusEditProjectContribution, VitruviusDeleteProjectContribution, VitruviusHelpCommandContribution, VitruviusImportProjectContribution, VitruviusLoadProjectContribution, VitruviusRefreshProjectContribution, VitruviusMenuContribution } from './vitruvius-contribution';
import { CommandContribution, MenuContribution } from '@theia/core/lib/common';
import { ContainerModule } from '@theia/core/shared/inversify';
import {WidgetContribution} from './widget-contribution';
import {WidgetWidget} from './widget-widget';

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
    bind(WidgetWidget).toSelf();
    bind(WidgetFactory).toDynamicValue(ctx => ({
        id: WidgetWidget.ID,
        createWidget: () => ctx.container.get<WidgetWidget>(WidgetWidget)
    })).inSingletonScope();

});
