import {
    FrontendApplicationContribution,
    WidgetFactory,
    bindViewContribution,
} from "@theia/core/lib/browser";
import {VitruviusMenuContribution} from "./vitruviusMenuContribution";
import {CommandContribution, MenuContribution} from "@theia/core/lib/common";
import {ContainerModule, Container} from "@theia/core/shared/inversify";
import {DisplayViewWidgetContribution} from "./displayViewWidgetContribution";
import {DisplayViewWidget} from "./displayViewWidget";
import {BackendServer} from "../backend-communication/BackendServer";
import {DisplayViewService} from "../backend-communication/DisplayViewService";
import {ConnectionService} from "../backend-communication/ConnectionService";
import {DisplayViewResolver} from "../visualisation/DisplayViewResolver";
import {SourceCodeVisualizer} from "../visualisation/text/SourceCodeVisualizer";
import {SourceCodeExtractor} from "../visualisation/text/SourceCodeExtractor";
import {TextWidget} from "../visualisation/text/TextWidget";
import {VitruviusHelpCommandContribution} from "./menuCommands/helpCommand";
import {VitruviusLoadProjectContribution} from "./menuCommands/loadProjectCommand";
import {VitruviusImportProjectContribution} from "./menuCommands/importProjectCommand";
import {VitruviusRefreshProjectContribution} from "./menuCommands/refreshProjectCommand";
import {VitruviusDeleteProjectContribution} from "./menuCommands/deleteProjectCommand";
import {VitruviusEditProjectContribution} from "./menuCommands/editProjectCommand";
import '../../src/browser/style/index.css';
import {TableVisualizer} from "../visualisation/table/TableVisualizer";
import {TableExtractor} from "../visualisation/table/TableExtractor";
import {TableWidget} from "../visualisation/table/TableWidget";
import {DiagramWidget} from "../visualisation/uml/DiagramWidget";
import {DiagramVisualizer} from "../visualisation/uml/DiagramVisualizer";
import {DiagramExtractor} from "../visualisation/uml/DiagramExtractor";

/**
 * This ContainerModule binds the services and contributions of the frontend part of the application.
 */
export default new ContainerModule((bind) => {
    // All bindings for the backend communication
    bind("Url").toConstantValue("http://localhost:8080"); //TODO: Don't hardcode this
    bind(BackendServer).toSelf().inSingletonScope();
    bind(ConnectionService).toSelf().inSingletonScope();
    bind(DisplayViewService).toSelf().inSingletonScope();

    // All bindings for the visualisation of DisplayViews
    bind(SourceCodeVisualizer).toSelf().inSingletonScope();
    bind(SourceCodeExtractor).toSelf().inSingletonScope();
    bind(TableVisualizer).toSelf().inSingletonScope();
    bind(TableExtractor).toSelf().inSingletonScope();
    bind(DiagramVisualizer).toSelf().inSingletonScope();
    bind(DiagramExtractor).toSelf().inSingletonScope();
    bind(DisplayViewResolver)
        .toDynamicValue((ctx) => {
            let displayViewResolver = new DisplayViewResolver();
            displayViewResolver.registerDisplayView(
                "TextVisualizer",
                ctx.container.get(SourceCodeVisualizer),
                ctx.container.get(SourceCodeExtractor));
            displayViewResolver.registerDisplayView(
                "TableVisualizer",
                ctx.container.get(TableVisualizer),
                ctx.container.get(TableExtractor));
            displayViewResolver.registerDisplayView(
                "DiagramVisualizer",
                ctx.container.get(DiagramVisualizer),
                ctx.container.get(DiagramExtractor));
            return displayViewResolver;
        })
        .inSingletonScope();

    // Factory for creating a TextWidget
    bind(WidgetFactory).toDynamicValue((ctx) => ({
        id: TextWidget.ID,
        createWidget: (widgetLabel: string) => {
            const child = new Container({defaultScope: "Singleton"});
            child.parent = ctx.container;
            child.bind(TextWidget).toSelf();
            child.get(TextWidget).setLabel(widgetLabel);
            return child.get(TextWidget);
        },
    }));

    // Factory for creating a TableWidget
    bind(WidgetFactory).toDynamicValue((ctx) => ({
        id: TableWidget.ID,
        createWidget: (widgetLabel: string) => {
            const child = new Container({defaultScope: "Singleton"});
            child.parent = ctx.container;
            child.bind(TableWidget).toSelf();
            child.get(TableWidget).setLabel(widgetLabel);
            return child.get(TableWidget);
        },
    }));

    // Factory for creating a DiagramWidget
    bind(WidgetFactory).toDynamicValue((ctx) => ({
        id: DiagramWidget.ID,
        createWidget: (widgetLabel: string) => {
            const child = new Container({defaultScope: "Singleton"});
            child.parent = ctx.container;
            child.bind(DiagramWidget).toSelf();
            child.get(DiagramWidget).setLabel(widgetLabel);
            return child.get(DiagramWidget);
        },
    }));

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
    bind(FrontendApplicationContribution).toService(
        DisplayViewWidgetContribution,
    );
    bind(DisplayViewWidget).toSelf();
    bind(WidgetFactory)
        .toDynamicValue((ctx) => ({
            id: DisplayViewWidget.ID,
            createWidget: () =>
                ctx.container.get<DisplayViewWidget>(DisplayViewWidget),
        }))
        .inSingletonScope();
});
