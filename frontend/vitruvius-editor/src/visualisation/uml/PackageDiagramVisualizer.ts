import {Content} from "../../model/Content";
import { Visualizer } from "../Visualizer";
import {VisualisationWidget} from "../VisualisationWidget";

export class PackageDiagramVisualizer implements Visualizer {
	visualizeContent(content: Content): Promise<VisualisationWidget<any>> {
		throw new Error("Method not implemented.");
	}
 }
