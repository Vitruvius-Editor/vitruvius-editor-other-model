import {
  inject,
  injectable,
  postConstruct,
} from "@theia/core/shared/inversify";
import * as React from "react";
import { MessageService } from "@theia/core";
import { VisualisationWidget } from "../VisualisationWidget";
import createEngine, {DiagramModel, CanvasWidget} from '@projectstorm/react-diagrams';
import {ArrowLinkFactory} from "./DiagramComponents";
import {UMLDiagramBuilder} from "./UMLDiagramParser";
import {Diagram} from "./Diagram";

/**
 * A Widget to visualize a UML Package Vitruvius view.
 */
@injectable()
export class DiagramWidget extends VisualisationWidget<Diagram> {
  static readonly ID = "packagediagramwidget:packagediagramwidget";
  static readonly LABEL = "DiagramWidget";

  @inject(MessageService)
  protected readonly messageService!: MessageService;

  /**
   * Initializes the widget with the default id, label and initial content.
   */
  @postConstruct()
  protected init(): void {
    this.doInit(DiagramWidget.ID, DiagramWidget.LABEL, { nodes: [], connections: [] });
  }

  /**
   * Renders the widget containing the content as a UML Package Diagram given from the Backend.
   * Uses the Parser to parse the content and create the diagram.
   */
  render(): React.ReactElement {
    const engine = createEngine();
    engine.getLinkFactories().registerFactory(new ArrowLinkFactory());

    const umlDiagramParser = new UMLDiagramBuilder();
    const umlDiagram = umlDiagramParser.parse(this.content, "Class");
    const model = new DiagramModel();
    umlDiagram.getNodes().forEach(component => model.addNode(component));
    umlDiagram.getLinks().forEach(link => model.addLink(link));
    engine.setModel(model);

    return <CanvasWidget className="diagram-container" engine={engine} />;
  }

  getVisualizerName(): string {
    return "DiagramVisualizer";
  }
}
