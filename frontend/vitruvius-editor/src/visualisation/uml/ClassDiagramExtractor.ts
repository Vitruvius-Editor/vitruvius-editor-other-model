import {Content} from "../../model/Content";
import { Extractor } from "../Extractor";
import { Widget } from "@theia/core/lib/browser";

export class ClassDiagramExtractor implements Extractor {
	extractContent(widget: Widget): Promise<Content> {
		throw new Error("Method not implemented.");
	}
}
