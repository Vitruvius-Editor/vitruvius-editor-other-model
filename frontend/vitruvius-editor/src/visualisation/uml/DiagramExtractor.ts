import {injectable} from "@theia/core/shared/inversify";
import { Content } from "../../model/Content";
import { Extractor } from "../Extractor";
import { VisualisationWidget } from "../VisualisationWidget";

@injectable()
export class DiagramExtractor implements Extractor {
  extractContent(widget: VisualisationWidget<any>): Promise<Content> {
    throw new Error("Method not implemented.");
  }
}
