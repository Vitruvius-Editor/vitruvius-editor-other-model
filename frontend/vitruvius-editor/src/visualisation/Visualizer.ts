import { Widget } from "@theia/core/lib/browser";

/**
 * The Visualizer interface represents a component responsible for visualizing a given content string
 * and rendering it as a Widget.
 */
export interface Visualizer {
  /**
   * Visualizes the given content and returns it as a Widget object.
   *
   * @param {string} content - The content to be visualized.
   * @return {Widget} The visual representation of the provided content as a Widget.
   */
  visualizeContent(content: string): Widget;
}
