import { TextWidget } from "./TextWidget";
import { MessageService } from "@theia/core";
import {render} from "@testing-library/react";
import {Container} from "@theia/core/shared/inversify";
import '@testing-library/jest-dom';
import {VisualisationWidgetRegistry} from "../VisualisationWidgetRegistry";

describe("TextWidget", () => {
    let widget: TextWidget;
    let messageService: jest.Mocked<MessageService>;
    let visualisationWidgetRegistry: jest.Mocked<VisualisationWidgetRegistry>;

    beforeEach(() => {
        const container = new Container();
        messageService = { info: jest.fn() } as unknown as jest.Mocked<MessageService>;
        visualisationWidgetRegistry = { registerWidget: jest.fn() } as unknown as jest.Mocked<VisualisationWidgetRegistry>;
        container.bind(MessageService).toConstantValue(messageService);
        container.bind(VisualisationWidgetRegistry).toConstantValue(visualisationWidgetRegistry);
        container.bind(TextWidget).toSelf()
        widget = container.get(TextWidget);
    });

    it("should initialize with default id, label, and content", () => {
        expect(widget.id).toBe(TextWidget.ID);
        expect(widget.title.label).toBe(TextWidget.LABEL);
        expect(widget.getContent()).toBe("/*Initial Content*/");
    });

    it("should render a text area with initial content", () => {
        const { getByDisplayValue } = render(widget.render());
        expect(getByDisplayValue("/*Initial Content*/")).toBeInTheDocument();
    });

    it("should return the correct visualizer name", () => {
        expect(widget.getVisualizerName()).toBe("TextVisualizer");
    });
});