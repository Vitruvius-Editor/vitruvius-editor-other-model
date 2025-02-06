import { Container } from "@theia/core/shared/inversify";
import { ApplicationShell, WidgetManager } from "@theia/core/lib/browser";
import { TableVisualizer } from "./TableVisualizer";
import { TableWidget } from "./TableWidget";
import { Content } from "../../model/Content";
import { Table } from "./Table";

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
            windows: [{ name: "TestWindow", content: JSON.stringify({ rows: [], columns: [] }) }]
        };
    });

    it("should parse and update content correctly", async () => {
        const table: Table = {
            rows: [
                { uuid: 'uuid1', name: 'name1', visibility: 'public', isAbstract: false, isFinal: false, superClassName: 'superClass1', interfaces: ['interface1'], attributeCount: 1, methodCount: 1, linesOfCode: 10 },
                { uuid: 'uuid2', name: 'name2', visibility: 'private', isAbstract: true, isFinal: true, superClassName: 'superClass2', interfaces: ['interface2'], attributeCount: 2, methodCount: 2, linesOfCode: 20 }
            ],
            columns: [
                { fieldName: "name", displayName: "Name", shouldBeDisplayed: true, editable: true, fieldType: "string" },
                { fieldName: "visibility", displayName: "Visibility", shouldBeDisplayed: true, editable: false, fieldType: "string" },
                { fieldName: "isAbstract", displayName: "Abstract", shouldBeDisplayed: true, editable: false, fieldType: "boolean" },
                { fieldName: "isFinal", displayName: "Final", shouldBeDisplayed: true, editable: false, fieldType: "boolean" },
                { fieldName: "superClassName", displayName: "Super Class", shouldBeDisplayed: true, editable: false, fieldType: "string" },
                { fieldName: "interfaces", displayName: "Interfaces", shouldBeDisplayed: true, editable: false, fieldType: "array" },
                { fieldName: "attributeCount", displayName: "Attributes", shouldBeDisplayed: true, editable: false, fieldType: "number" },
                { fieldName: "methodCount", displayName: "Methods", shouldBeDisplayed: true, editable: false, fieldType: "number" },
                { fieldName: "linesOfCode", displayName: "Lines of Code", shouldBeDisplayed: true, editable: false, fieldType: "number" }
            ]
        };
        content.windows[0].content = JSON.stringify(table);
        const mockWidget = { updateContent: jest.fn() } as unknown as TableWidget;
        widgetManager.getOrCreateWidget.mockResolvedValue(mockWidget);

        await visualizer.visualizeContent(content);

        expect(mockWidget.updateContent).toHaveBeenCalledWith(table);
    });
});