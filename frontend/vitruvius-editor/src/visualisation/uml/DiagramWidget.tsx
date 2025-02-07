import {
  inject,
  injectable,
  postConstruct,
} from "@theia/core/shared/inversify";
import * as React from "react";
import { MessageService } from "@theia/core";
import { VisualisationWidget } from "../VisualisationWidget";
import createEngine, { DiagramModel, CanvasWidget } from '@projectstorm/react-diagrams';
import { ArrowLinkFactory, DiagramContent, UMLNode, UMLRelation } from "./DiagramComponents";
import { Diagram, visibilitySymbol } from "./Diagram";

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

    const umlDiagram = this.createDiagramContent(this.content, "Class");
    const model = new DiagramModel();
    umlDiagram.nodes.forEach(component => model.addNode(component));
    umlDiagram.links.forEach(link => model.addLink(link));
    engine.setModel(model);

    return (
        <div className="editor-container">
          <CanvasWidget className="diagram-container" engine={engine} />
        </div>
    );
  }

  /**
   * Parses the diagram content and creates a DiagramContent object.
   * @param diagram The diagram content to parse.
   * @param type The type of diagram ("Class" or "Package").
   * @returns A DiagramContent object containing the parsed nodes and links.
   */
  createDiagramContent(diagram: Diagram, type: "Class" | "Package"): DiagramContent {
    const nodes: UMLNode[] = [];
    const links: UMLRelation[] = [];

    diagram.nodes.forEach(data => {
      if (type === 'Class') {
        const text = (
            <div>
              {data.name} <br />
              <hr />
              {data.attributes.map((attr, index) => (
                  <React.Fragment key={index}>
                    {visibilitySymbol(attr.visibility)} {attr.name}: {attr.type.name} <br />
                  </React.Fragment>
              ))}
              <hr />
              {data.methods.map((method, index) => (
                  <React.Fragment key={index}>
                    {visibilitySymbol(method.visibility)} {method.name}({method.parameters.map(param => `${param.name}: ${param.type.name}`).reduce((prev, curr) => `${prev}, ${curr}`, "").slice(2)}): {method.returnType.name} <br />
                  </React.Fragment>
              ))}
            </div>
        );
        // @ts-ignore
        nodes.push(new UMLNode(data.uuid, text));
      } else if (type === 'Package') {
        nodes.push(new UMLNode(data.uuid, data.name));
      }
    });

    diagram.connections.forEach(link => {
      const fromNode = nodes.find(node => node.getClassID() === link.sourceNodeUUID);
      const toNode = nodes.find(node => node.getClassID() === link.targetNodeUUID);
      if (fromNode !== undefined && toNode !== undefined) {
        switch(link.connectionType) {
            case "association": links.push(new UMLRelation("default", link.uuid, fromNode, toNode));
            default: links.push(new UMLRelation("advanced", link.uuid, fromNode, toNode));
        } 
      }
    });

    return { nodes, links };
  }

  /**
   * Returns the name of the visualizer.
   * @returns The name of the visualizer.
   */
  getVisualizerName(): string {
    return "DiagramVisualizer";
  }
}
