import { DisplayViewResolver } from "./DisplayViewResolver";
import { Visualizer } from "./Visualizer";
import { Extractor } from "./Extractor";
import { Content } from "../model/Content";
import { VisualisationWidget } from "./VisualisationWidget";

class MockVisualizer implements Visualizer {
    visualizeContent(content: Content): Promise<VisualisationWidget<any>> {
        return Promise.resolve(new MockVisualisationWidget());
    }
}

class MockExtractor implements Extractor {
    extractContent(widget: VisualisationWidget<any>): Promise<Content> {
        return Promise.resolve({ visualizerName: "MockVisualizer" } as Content);
    }
}

class MockVisualisationWidget extends VisualisationWidget<any> {
    getVisualizerName(): string {
        return "MockVisualizer";
    }

    protected render(): React.ReactNode {
        return undefined;
    }
}

describe("DisplayViewResolver", () => {
    let resolver: DisplayViewResolver;
    let mockVisualizer: MockVisualizer;
    let mockExtractor: MockExtractor;
    let content: Content;
    let widget: MockVisualisationWidget;

    beforeEach(() => {
        resolver = new DisplayViewResolver();
        mockVisualizer = new MockVisualizer();
        mockExtractor = new MockExtractor();
        content = { visualizerName: "MockVisualizer" } as Content;
        widget = new MockVisualisationWidget();
    });

    it("should register a display view", () => {
        resolver.registerDisplayView("TestView", mockVisualizer, mockExtractor);
        expect(resolver.mappings.size).toBe(1);
        expect(resolver.mappings.get("TestView")).toEqual([mockVisualizer, mockExtractor]);
    });

    it("should retrieve the widget associated with a given content", async () => {
        resolver.registerDisplayView("MockVisualizer", mockVisualizer, mockExtractor);
        const result = await resolver.getWidget(content);
        expect(result).toBeInstanceOf(MockVisualisationWidget);
    });

    it("should return null if no visualizer is found for the given content", async () => {
        const result = await resolver.getWidget(content);
        expect(result).toBeNull();
    });

    it("should retrieve the content associated with a given widget", async () => {
        resolver.registerDisplayView("MockVisualizer", mockVisualizer, mockExtractor);
        const result = await resolver.getContent(widget);
        expect(result).toEqual(content);
    });

    it("should return null if no extractor is found for the given widget", async () => {
        const result = await resolver.getContent(widget);
        expect(result).toBeNull();
    });
});