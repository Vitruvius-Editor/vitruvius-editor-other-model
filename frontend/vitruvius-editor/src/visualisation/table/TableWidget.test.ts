import { TableWidget } from "./TableWidget";
import { Container } from "@theia/core/shared/inversify";
import '@testing-library/jest-dom';
import {VisualisationWidgetRegistry} from "../VisualisationWidgetRegistry";

describe("TableWidget", () => {
    let widget: TableWidget;
    let content: TableEntry[];
    let visualisationWidgetRegistry: jest.Mocked<VisualisationWidgetRegistry>;

    beforeEach(() => {
        const container = new Container();
        visualisationWidgetRegistry = { registerWidget: jest.fn() } as unknown as jest.Mocked<VisualisationWidgetRegistry>;
        container.bind(VisualisationWidgetRegistry).toConstantValue(visualisationWidgetRegistry);
        container.bind(TableWidget).toSelf();
        widget = container.get(TableWidget);
        content = [
            { uuid: 'uuid', name: 'name', visibility: 'visibility', isAbstract: true, isFinal: true, superClassName: 'superClassName', interfaces: ['interfaces'], attributeCount: 1, methodCount: 1, linesOfCode: 1 },
            { uuid: 'uuid2', name: 'name2', visibility: 'visibility', isAbstract: false, isFinal: true, superClassName: 'superClassName', interfaces: ['interfaces'], attributeCount: 1, methodCount: 1, linesOfCode: 1 },
        ];
    });

    it("should initialize with default id, label, and empty content", () => {
        expect(widget.id).toBe(TableWidget.ID);
        expect(widget.title.label).toBe(TableWidget.LABEL);
        expect(widget.getContent()).toEqual([]);
    });

    it("should return the correct visualizer name", () => {
        expect(widget.getVisualizerName()).toBe("TextVisualizer");
    });

    it("should return the content as a string", () => {
        widget.updateContent(content);

        expect(widget.getContentString()).toBe(
            JSON.stringify({ entries: widget.getContent() })
        );
    });
});