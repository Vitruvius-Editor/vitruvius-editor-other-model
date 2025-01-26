import { Visualizer } from "../Visualizer";
import { BaseWidget, Widget } from "@theia/core/lib/browser";

export class JsonVisualizer implements Visualizer {
	visualizeContent(content: string): Promise<Widget> {
		throw new Error("Method not implemented.");
	}
 }
