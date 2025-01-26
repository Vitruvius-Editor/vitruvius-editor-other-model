import { Widget } from "@theia/core/lib/browser";

/**
 * Interface representing an Extractor, which is responsible for extracting content from a given widget.
 *
 * This interface defines a method that takes a Widget as an argument and returns a string representing the extracted content.
 */
export interface Extractor {
  /**
   * Extracts and returns the textual content from the given widget.
   *
   * @param {Widget} widget - The widget instance from which content is to be extracted.
   * @return {string} The extracted textual content of the widget.
   */
  extractContent(widget: Widget): Promise<string>;
}
