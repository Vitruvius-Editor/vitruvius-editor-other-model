import {injectable} from "@theia/core/shared/inversify";
import { Content } from "../../model/Content";
import { Extractor } from "../Extractor";
import { VisualisationWidget } from "../VisualisationWidget";

@injectable()
export class DiagramExtractor implements Extractor {
  extractContent(widget: VisualisationWidget<any>): Promise<Content> {
    return new Promise((resolve, _refuse) => resolve({visualizerName: 'UmlVisualizer', windows: [
        {
          name: widget.getLabel(),
          content: widget.getContent()
        }
      ]}))
  }
}
