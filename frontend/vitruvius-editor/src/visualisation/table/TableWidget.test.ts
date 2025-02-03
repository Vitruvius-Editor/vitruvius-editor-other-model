import { TableWidget } from "./TableWidget";
import { Container } from "@theia/core/shared/inversify";
import '@testing-library/jest-dom';
import {VisualisationWidgetRegistry} from "../VisualisationWidgetRegistry";
import {Table} from "./Table";

describe("TableWidget", () => {
    let widget: TableWidget;
    let content: Table;
    let visualisationWidgetRegistry: jest.Mocked<VisualisationWidgetRegistry>;

    beforeEach(() => {
        const container = new Container();
        visualisationWidgetRegistry = { registerWidget: jest.fn() } as unknown as jest.Mocked<VisualisationWidgetRegistry>;
        container.bind(VisualisationWidgetRegistry).toConstantValue(visualisationWidgetRegistry);
        container.bind(TableWidget).toSelf();
        widget = container.get(TableWidget);
        content = {
            rows: [
                {
                    uuid: "1",
                    name: "Row1",
                    visibility: "public",
                    isAbstract: false,
                    isFinal: false,
                    superClassName: "SuperClass1",
                    interfaces: ["Interface1"],
                    attributeCount: 5,
                    methodCount: 3,
                    linesOfCode: 100,
                },
                {
                    uuid: "2",
                    name: "Row2",
                    visibility: "private",
                    isAbstract: true,
                    isFinal: true,
                    superClassName: "SuperClass2",
                    interfaces: ["Interface2", "Interface3"],
                    attributeCount: 10,
                    methodCount: 6,
                    linesOfCode: 200,
                },
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
                { fieldName: "linesOfCode", displayName: "Lines of Code", shouldBeDisplayed: true, editable: false, fieldType: "number" },
            ],
        };
    });

    it("should initialize with default id, label, and empty content", () => {
        expect(widget.id).toBe(TableWidget.ID);
        expect(widget.title.label).toBe(TableWidget.LABEL);
        expect(widget.getContent()).toEqual({ rows: [], columns: [] });
    });

    it("should return the correct visualizer name", () => {
        expect(widget.getVisualizerName()).toBe("TextVisualizer");
    });

    it("should return the content as a string", () => {
        widget.updateContent(content);

        expect(widget.getContentString()).toBe(
            JSON.stringify(widget.getContent())
        );
    });
});