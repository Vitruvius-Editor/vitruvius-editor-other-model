import {injectable} from "@theia/core/shared/inversify";
import { Content } from "../../model/Content";
import { Extractor } from "../Extractor";
import { VisualisationWidget } from "../VisualisationWidget";

/**
 * The DiagramExtractor class is responsible for extracting content from a VisualisationWidget.
 * It implements the Extractor interface.
 */
@injectable()
export class DiagramExtractor implements Extractor {
    /**
     * Extracts content from the given VisualisationWidget.
     *
     * @param widget - The VisualisationWidget to extract content from.
     * @returns A promise that resolves to a Content object containing the visualizer name and windows.
     */
    extractContent(widget: VisualisationWidget<any>): Promise<Content> {
        return new Promise((resolve, _refuse) => resolve({
            visualizerName: 'UmlVisualizer',
            windows: [
                {
                    name: widget.getLabel(),
                    content: widget.getContent()
                }
            ]
        }));
    }
}
