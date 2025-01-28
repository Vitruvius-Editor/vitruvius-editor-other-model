import { Content } from "../model/Content";
import { VisualisationWidget } from "./VisualisationWidget";

/**
 * The Visualizer interface represents a component responsible for visualizing a given content
 * and rendering it as a Widget.
 */
export interface Visualizer {
  /**
   * Visualizes the given content and returns it as a Widget object.
   *
   * @param {string} content - The content to be visualized.
   * @return {Widget} The visual representation of the provided content as a Widget.
   */
  visualizeContent(content: Content): Promise<VisualisationWidget<any>>;
}
