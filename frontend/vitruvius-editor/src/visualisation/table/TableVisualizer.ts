import {Content} from "../../model/Content";
import { Visualizer } from "../Visualizer";
import { Widget } from "@theia/core/lib/browser";

export class TableVisualizer implements Visualizer {
	visualizeContent(content: Content): Promise<Widget> {
		throw new Error("Method not implemented.");
	}
}
