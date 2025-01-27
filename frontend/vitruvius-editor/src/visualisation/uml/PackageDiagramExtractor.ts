import {Content} from "../../model/Content";
import { Extractor } from "../Extractor";
import {VisualisationWidget} from "../VisualisationWidget";

export class PackageDiagramExtractor implements Extractor {
	extractContent(widget: VisualisationWidget<any>): Promise<Content> {
		throw new Error("Method not implemented.");
	}
}
