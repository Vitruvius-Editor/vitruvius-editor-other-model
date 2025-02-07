import {
  inject,
  injectable,
  postConstruct,
} from "@theia/core/shared/inversify";
import * as React from "react";
import { useLayoutEffect } from "react";
import { MessageService } from "@theia/core";
import { VisualisationWidget } from "../VisualisationWidget";
import createEngine, {
  DiagramModel,
  CanvasWidget,
  DagreEngine,
  DiagramEngine,
  PathFindingLinkFactory,
} from '@projectstorm/react-diagrams';
import { DefaultDiagramState } from '@projectstorm/react-diagrams';
import { ArrowLinkFactory, DiagramContent, UMLNode, UMLRelation } from "./DiagramComponents";
import { Diagram } from "./Diagram";

/**
 * A Widget to visualize a UML Package Vitruvius view.
 */
@injectable()
export class DiagramWidget extends VisualisationWidget<Diagram> {
  static readonly ID = "packagediagramwidget:packagediagramwidget";
  static readonly LABEL = "DiagramWidget";

  protected engine: DiagramEngine;

  @inject(MessageService)
  protected readonly messageService!: MessageService;

  /**
   * Initializes the widget with the default id, label and initial content.
   */
  @postConstruct()
  protected init(): void {
    this.doInit(DiagramWidget.ID, DiagramWidget.LABEL, { nodes: [], connections: [] });
  }

  constructor(props: any) {
    super(props);
    this.engine = createEngine();
  }

  disableDrag = () => {
    const state = this.engine.getStateMachine().getCurrentState();
    if (state instanceof DefaultDiagramState) {
      state.dragCanvas.config.allowDrag = false;
    }
  };

  /**
   * Renders the widget containing the content as a UML Package Diagram given from the Backend.
   * Uses the Parser to parse the content and create the diagram.
   */
  render(): React.ReactElement {
    this.engine.getLinkFactories().registerFactory(new ArrowLinkFactory());

    const umlDiagram = this.createDiagramContent(this.content, "Class");
    const model = new DiagramModel();
    umlDiagram.nodes.forEach(component => model.addNode(component));
    umlDiagram.links.forEach(link => model.addLink(link));
    this.engine.setModel(model);
    this.disableDrag();

    // useLayoutEffect is supposed to be called after the first render, but it doesn't work here yet

    // useLayoutEffect(() => {
    //   this.dagre();
    // }, []);

    // this.dagre();

    return (
        <div className="editor-container">
          <CanvasWidget className="diagram-container" engine={this.engine} />
        </div>
    );
  }

  dagre() {
    autoDistribute(this.engine);
    autoRefreshLinks(this.engine);
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
                    {attr.visibility} {attr.name}: {attr.type.name} <br />
                  </React.Fragment>
              ))}
              <hr />
              {data.methods.map((method, index) => (
                  <React.Fragment key={index}>
                    {method.visibility} {method.name}({method.parameters.map(param => `${param.name}: ${param.type.name}`).reduce((prev, curr) => `${prev}, ${curr}`, "").slice(2)}): {method.returnType.name} <br />
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
        links.push(new UMLRelation("advanced", link.uuid, fromNode, toNode));
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

function genDagreEngine() {
  return new DagreEngine({
    graph: {
      rankdir: 'RL',
      ranker: 'longest-path',
      marginx: 25,
      marginy: 25
    },
    includeLinks: true,
    nodeMargin: 25
  });
}

function autoDistribute(engine: DiagramEngine) {
  const model = engine.getModel();

  const dagreEngine = genDagreEngine();
  dagreEngine.redistribute(model);

  reroute(engine);
  engine.repaintCanvas();
}

function autoRefreshLinks(engine: DiagramEngine) {
  const model = engine.getModel();

  const dagreEngine = genDagreEngine();
  dagreEngine.refreshLinks(model);

  reroute(engine);
  engine.repaintCanvas();
}

function reroute(engine: DiagramEngine) {
  engine.getLinkFactories().getFactory<PathFindingLinkFactory>(PathFindingLinkFactory.NAME).calculateRoutingMatrix();
}