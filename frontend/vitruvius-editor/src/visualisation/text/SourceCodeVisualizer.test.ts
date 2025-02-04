import { Container } from "@theia/core/shared/inversify";
import { WidgetManager } from "@theia/core/lib/browser";
import { ApplicationShell } from "@theia/core/lib/browser/shell/application-shell";
import { SourceCodeVisualizer } from "./SourceCodeVisualizer";
import { TextWidget } from "./TextWidget";
import { Content } from "../../model/Content";

describe("SourceCodeVisualizer", () => {
    let container: Container;
    let visualizer: SourceCodeVisualizer;
    let widgetManager: jest.Mocked<WidgetManager>;
    let shell: jest.Mocked<ApplicationShell>;
    let content: Content;

    beforeEach(() => {
        container = new Container();
        widgetManager = { getOrCreateWidget: jest.fn() } as unknown as jest.Mocked<WidgetManager>;
        shell = { addWidget: jest.fn().mockResolvedValue(null), activateWidget: jest.fn() } as unknown as jest.Mocked<ApplicationShell>;

        container.bind(WidgetManager).toConstantValue(widgetManager);
        container.bind(ApplicationShell).toConstantValue(shell);
        container.bind(SourceCodeVisualizer).toSelf();
        visualizer = container.get(SourceCodeVisualizer);

        content = {
            visualizerName: "SourceCodeVisualizer",
            windows: [{ name: "TestWindow", content: "Test Content" }]
        };
    });

    it("should visualize content and add widget to shell", async () => {
        const mockWidget = { updateContent: jest.fn(), id: "test-id" } as unknown as TextWidget;
        widgetManager.getOrCreateWidget.mockResolvedValue(mockWidget);

        const result = await visualizer.visualizeContent(content);

        expect(widgetManager.getOrCreateWidget).toHaveBeenCalledWith(TextWidget.ID, "TestWindow");
        expect(mockWidget.updateContent).toHaveBeenCalledWith("Test Content");
        expect(shell.addWidget).toHaveBeenCalledWith(mockWidget, { area: "main", mode: "tab-before" });
        expect(shell.activateWidget).toHaveBeenCalledWith(mockWidget.id);
        expect(result).toBe(mockWidget);
    });

});