import { Extractor } from "../Extractor";
import { Widget } from "@theia/core/lib/browser";

export class SourceCodeExtractor implements Extractor {
  extractContent(widget: Widget): string {
    // Implementation goes here, for now returning an empty string
    return "";
  }
}
