import {FrontendApplicationContribution, WidgetFactory, bindViewContribution} from '@theia/core/lib/browser';
import { VitruviusMenuContribution } from './vitruviusMenuContribution';
import { CommandContribution, MenuContribution } from '@theia/core/lib/common';
import { ContainerModule, Container } from '@theia/core/shared/inversify';
import {DisplayViewWidgetContribution} from './displayViewWidgetContribution';
import {DisplayViewWidget} from './displayViewWidget';
import {BackendServer} from '../backend-communication/BackendServer';
import {DisplayViewService} from '../backend-communication/DisplayViewService';
import {ConnectionService} from '../backend-communication/ConnectionService';
import {DisplayViewResolver} from '../visualisation/DisplayViewResolver';
import {SourceCodeVisualizer} from '../visualisation/text/SourceCodeVisualizer';
import {SourceCodeExtractor} from '../visualisation/text/SourceCodeExtractor';
import {TextWidget } from '../visualisation/text/TextWidget';
import {VitruviusHelpCommandContribution} from "./menuCommands/helpCommand";
import {VitruviusLoadProjectContribution} from "./menuCommands/loadProjectCommand";
import {VitruviusImportProjectContribution} from "./menuCommands/importProjectCommand";
import {VitruviusRefreshProjectContribution} from "./menuCommands/refreshProjectCommand";
import {VitruviusDeleteProjectContribution} from "./menuCommands/deleteProjectCommand";
import {VitruviusEditProjectContribution} from "./menuCommands/editProjectCommand";

/**
 * This ContainerModule binds the services and contributions of the frontend part of the application.
 */
export default new ContainerModule(bind => {
	// All bindings for the backend communication
	bind("Url").toConstantValue('http://localhost:8080'); //TODO: Don't hardcode this
	bind(BackendServer).toSelf().inSingletonScope();
	bind(ConnectionService).toSelf().inSingletonScope();
	bind(DisplayViewService).toSelf().inSingletonScope();

	// All bindings for the visualisation of DisplayViews
    bind(SourceCodeVisualizer).toSelf().inSingletonScope();
    bind(SourceCodeExtractor).toSelf().inSingletonScope();
    bind(DisplayViewResolver).toDynamicValue(ctx => {
        let displayViewResolver = new DisplayViewResolver();
        displayViewResolver.registerDisplayView("TextVisualizer", ctx.container.get(SourceCodeVisualizer), ctx.container.get(SourceCodeExtractor));
        return displayViewResolver;
    }).inSingletonScope();

    // Factory for creating a TextWidget
    bind(WidgetFactory).toDynamicValue(ctx => ({
        id: TextWidget.ID,
        createWidget: (widgetLabel: string) => {
            const child = new Container({defaultScope: 'Singleton'});
            child.parent = ctx.container;
            child.bind(TextWidget).toSelf();
            child.get(TextWidget).setLabel(widgetLabel);
            return child.get(TextWidget)
        }
    }))

	// All bindings for various UI contributions
    bind(CommandContribution).to(VitruviusHelpCommandContribution);
    bind(CommandContribution).to(VitruviusEditProjectContribution);
    bind(CommandContribution).to(VitruviusDeleteProjectContribution);
    bind(CommandContribution).to(VitruviusImportProjectContribution);
    bind(CommandContribution).to(VitruviusLoadProjectContribution);
    bind(CommandContribution).to(VitruviusRefreshProjectContribution);
    bind(MenuContribution).to(VitruviusMenuContribution);

    // All bindings for the widget that shows the DisplayViews
	bindViewContribution(bind, DisplayViewWidgetContribution);
    bind(FrontendApplicationContribution).toService(DisplayViewWidgetContribution);
    bind(DisplayViewWidget).toSelf();
    bind(WidgetFactory).toDynamicValue(ctx => ({
        id: DisplayViewWidget.ID,
        createWidget: () => ctx.container.get<DisplayViewWidget>(DisplayViewWidget)
    })).inSingletonScope();

});
