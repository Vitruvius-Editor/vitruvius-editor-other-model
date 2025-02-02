import { Container } from "@theia/core/shared/inversify";
import { ApplicationShell, WidgetManager } from "@theia/core/lib/browser";
import { TableVisualizer } from "./TableVisualizer";
import { TableWidget } from "./TableWidget";
import { Content } from "../../model/Content";
import { TableEntry } from "./TableEntry";

describe("TableVisualizer", () => {
    let container: Container;
    let visualizer: TableVisualizer;
    let widgetManager: jest.Mocked<WidgetManager>;
    let shell: jest.Mocked<ApplicationShell>;
    let content: Content;

    beforeEach(() => {
        container = new Container();
        widgetManager = { getOrCreateWidget: jest.fn() } as unknown as jest.Mocked<WidgetManager>;
        shell = { addWidget: jest.fn(), activateWidget: jest.fn() } as unknown as jest.Mocked<ApplicationShell>;

        container.bind(WidgetManager).toConstantValue(widgetManager);
        container.bind(ApplicationShell).toConstantValue(shell);
        container.bind(TableVisualizer).toSelf();
        visualizer = container.get(TableVisualizer);

        content = {
            visualizerName: "TableVisualizer",
            windows: [{ name: "TestWindow", content: JSON.stringify({ entries: [] }) }]
        };
    });

    it("should visualize content and add widget to shell", async () => {
        const mockWidget = { updateContent: jest.fn() } as unknown as TableWidget;
        widgetManager.getOrCreateWidget.mockResolvedValue(mockWidget);

        const result = await visualizer.visualizeContent(content);

        expect(widgetManager.getOrCreateWidget).toHaveBeenCalledWith(TableWidget.ID, "TestWindow");
        expect(mockWidget.updateContent).toHaveBeenCalledWith([]);
        expect(shell.addWidget).toHaveBeenCalledWith(mockWidget, { area: "main" });
        expect(shell.activateWidget).toHaveBeenCalledWith(mockWidget.id);
        expect(result).toBe(mockWidget);
    });

    it("should parse and update content correctly", async () => {
        const entries: TableEntry[] = [
            { uuid: 'uuid', name: 'name', visibility: 'visibility', isAbstract: true, isFinal: true, superClassName: 'superClassName', interfaces: ['interfaces'], attributeCount: 1, methodCount: 1, linesOfCode: 1 }
        ];
        content.windows[0].content = JSON.stringify({ entries });
        const mockWidget = { updateContent: jest.fn() } as unknown as TableWidget;
        widgetManager.getOrCreateWidget.mockResolvedValue(mockWidget);

        await visualizer.visualizeContent(content);

        expect(mockWidget.updateContent).toHaveBeenCalledWith(entries);
    });
});