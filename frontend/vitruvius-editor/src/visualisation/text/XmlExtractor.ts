import { Extractor } from "../Extractor";
import { Widget } from "@theia/core/lib/browser";

export class XmlExtractor implements Extractor {
  extractContent(widget: Widget): string {
    // Implementation of extractContent goes here
    return ""; // Replace with actual logic to extract content from the widget
  }
}
