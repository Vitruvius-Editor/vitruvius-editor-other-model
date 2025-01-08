import {Visualizer} from "./Visualizer";
import {DisplayView} from "../model/DisplayView";
import {BaseWidget, Widget} from "@theia/core/lib/browser";
import {Extractor} from "./Extractor";

/**
 * The DisplayViewResolver class is responsible for managing the registration and resolution
 * of display views with their corresponding visualizers and extractors.
 */
export class DisplayViewResolver {
    readonly mappings: Map<string, [Visualizer, Extractor]>

    constructor() {
       this.mappings = new Map();
    }

    /**
     * Registers a display view to be used with a specific visualizer and extractor.
     *
     * @param {DisplayView} displayView - The display view instance to be registered.
     * @param {Visualizer} visualizer - The visualizer associated with the display view.
     * @param {Extractor} extractor - The extractor used to provide data for the display view.
     * @return {void} This method does not return a value.
     */
    registerDisplayView(displayView: DisplayView, visualizer: Visualizer, extractor: Extractor): void {}

    /**
     * Retrieves a widget based on the provided display view and its content.
     *
     * @param {DisplayView} displayView - The display view object that determines the context of the widget.
     * @param {string} displayViewContent - The content associated with the display view.
     * @return {Widget} The widget instance created based on the provided display view and content.
     */
    getWidget(displayView: DisplayView, displayViewContent: string): Widget {
        return new BaseWidget()
    }

    /**
     * Retrieves the content associated with a given display view and its widget.
     *
     * @param {DisplayView} displayView - The display view for which the content is being retrieved.
     * @param {Widget} displayViewWidget - The widget associated with the display view.
     * @return {string} The content as a string.
     */
    getContent(displayView: DisplayView, displayViewWidget: Widget): string {
        return ""
    }
}