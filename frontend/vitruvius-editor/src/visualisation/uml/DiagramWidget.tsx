import {
  inject,
  injectable,
  postConstruct,
} from "@theia/core/shared/inversify";
import * as React from "react";
import { MessageService } from "@theia/core";
import { VisualisationWidget } from "../VisualisationWidget";
import createEngine, {DiagramModel, CanvasWidget} from '@projectstorm/react-diagrams';
import {AdvancedLinkFactory, UMLArrowLink, UMLNode} from "./DiagramComponents";

/**
 * A Widget to visualize a UML Package Vitruvius view.
 */
@injectable()
export class DiagramWidget extends VisualisationWidget<string> {
  static readonly ID = "packagediagramwidget:packagediagramwidget";
  static readonly LABEL = "DiagramWidget";

  @inject(MessageService)
  protected readonly messageService!: MessageService;

  /**
   * Initializes the widget with the default id, label and initial content.
   */
  @postConstruct()
  protected init(): void {
    this.doInit(DiagramWidget.ID, DiagramWidget.LABEL, "/*Initial Content*/");
  }

  /**
   * Renders the widget containing the content as a UML Package Diagram given from the Backend.
   */
  render(): React.ReactElement {
    const engine = createEngine();
    engine.getLinkFactories().registerFactory(new AdvancedLinkFactory());

    const umlDiagramParser = new UMLDiagramParser();
    const umlDiagram = umlDiagramParser.parse(this.getContent());

    const nodes: UMLNode[] = [];
    umlDiagram.getComponents().forEach(component => {
      const packageNode = new UMLNode(component.classID, component.name, component.attributes, component.methods);
      nodes.push(packageNode);
    });

    const links: UMLArrowLink[] = [];
    umlDiagram.getLinks().forEach(relation => {
      const fromNode = nodes.find(node => node.getClassID() === relation.fromID);
      const toNode = nodes.find(node => node.getClassID() === relation.toID);
      if (fromNode !== undefined && toNode !== undefined) {
        links.push(new UMLArrowLink(fromNode, toNode));
      }
    });

    const model = new DiagramModel();
    nodes.forEach(component => model.addNode(component));
    links.forEach(link => model.addLink(link));
    engine.setModel(model);

    return <CanvasWidget className="diagram-container" engine={engine} />;
  }


  /**
   * Handles the change event of the diagram area and updates the content.
   * @param event
   */
  handleChange(event: React.ChangeEvent<HTMLTextAreaElement>): void {
    this.content = event.target.value;
  }
}

/**
 * A class used to fetch the Classes from the Backend.
 * Currently, the classes are hardcoded for demonstration purposes.
 */
class UMLDiagramParser {

  constructor() {
  }

  parse(content: string): UMLDiagram {
    return new UMLDiagram();
  }
}