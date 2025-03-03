import {
  inject,
  injectable,
  postConstruct,
} from "@theia/core/shared/inversify";
import * as React from "react";
import { MessageService } from "@theia/core";
import { VisualisationWidget, VisualisationWidgetState } from "../VisualisationWidget";
import createEngine, {
  DiagramModel,
  CanvasWidget,
  DagreEngine,
  DiagramEngine,
  PathFindingLinkFactory,
} from '@projectstorm/react-diagrams';
import { DefaultDiagramState } from '@projectstorm/react-diagrams';
import { ArrowLinkFactory, DiagramContent, UMLNode, UMLRelation } from "./DiagramComponents";
import { Diagram, visibilitySymbol } from "./Diagram";

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
    this.engine = createEngine({ registerDefaultDeleteItemsAction: false});
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

    const DiagramComponent: React.FC = () => {
      React.useLayoutEffect(() => {
        this.dagre();
      }, [])
      return (
          <div className="editor-container">
            <CanvasWidget className="diagram-container" engine={this.engine} />
          </div>
      )
    }

    return <DiagramComponent/>;
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

    const handleInputChange = (event: React.FormEvent<HTMLSpanElement>, data: any, key: string) => {
      data[key] = event.currentTarget.textContent || "";
    };

    diagram.nodes.forEach((data, nodeIndex) => {
      if (type === 'Class') {
        const text = (
            <div>
              <span contentEditable spellCheck={false} onInput={(e) => handleInputChange(e, data, 'name')}>{data.name}</span> <br />
              <hr />
              {data.attributes.map((attr, index) => (
                  <React.Fragment key={index}>
                    <span contentEditable spellCheck={false} onInput={(e) => handleInputChange(e, attr, 'visibility')}>{visibilitySymbol(attr.visibility)}</span>
                    <span contentEditable spellCheck={false} onInput={(e) => handleInputChange(e, attr, 'name')}>{attr.name}</span>:
                    <span contentEditable spellCheck={false} onInput={(e) => handleInputChange(e, attr.type, 'name')}>{attr.type.name}</span>
                    <span onClick={() => this.handlerDeleteAttribute(nodeIndex, index)} style={{ cursor: 'pointer', color: 'red' }}> - </span><br />
                  </React.Fragment>
              ))}
              <hr />
              {data.methods.map((method, methodIndex) => (
                  <React.Fragment key={methodIndex}>
                    <span contentEditable spellCheck={false} onInput={(e) => handleInputChange(e, method, 'visibility')}>{visibilitySymbol(method.visibility)}</span>
                    <span contentEditable spellCheck={false} onInput={(e) => handleInputChange(e, method, 'name')}>{method.name}</span>(
                    {method.parameters.map((param, index) => (
                        <React.Fragment key={param.uuid}>
                          <span contentEditable spellCheck={false} onInput={(e) => handleInputChange(e, param, 'name')}>{param.name}</span>:
                          <span contentEditable spellCheck={false} onInput={(e) => handleInputChange(e, param.type, 'name')}>{param.type.name}</span>
                          <span onClick={() => this.handleDeleteParameter(nodeIndex, methodIndex, index)} style={{ cursor: 'pointer', color: 'red' }}> - </span>
                          <span>{index != method.parameters.length-1 ? "," : ""}</span>
                        </React.Fragment>
                    ))}
                    ):
                    <span contentEditable spellCheck={false} onInput={(e) => handleInputChange(e, method.returnType, 'name')}>{method.returnType.name}</span>
                    <span onClick={() => this.handlerDeleteMethod(nodeIndex, methodIndex)} style={{ cursor: 'pointer', color: 'red' }}> - </span><br />
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
    return "UmlVisualizer";
  }

  override restoreState(oldState: object): void {
    let typedState = oldState as VisualisationWidgetState<Diagram>;
    this.setLabel(typedState.label);
    this.updateContent(typedState.content);
    this.visualisationWidgetRegistry.registerWidget(this, typedState.displayView, typedState.connection);
    this.dagre()
  }
  handlerDeleteAttribute(node: number, index: number) {
    this.content.nodes[node].attributes.splice(index, 1);
    this.update();
  }

  handlerDeleteMethod(node: number, index: number) {
    this.content.nodes[node].methods.splice(index, 1);
    this.update();
  }

  handleDeleteParameter(node: number, method: number, parameter: number) {
    this.content.nodes[node].methods[method].parameters.splice(parameter, 1);
    this.update();
  }
}

/**
 * Generates a Dagre engine with specific configurations.
 * @returns A configured DagreEngine instance.
 */
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

/**
 * Distributes nodes in the diagram using the Dagre engine.
 * @param engine The DiagramEngine instance.
 */
function autoDistribute(engine: DiagramEngine) {
  const model = engine.getModel();

  const dagreEngine = genDagreEngine();
  dagreEngine.redistribute(model);

  reroute(engine);
  engine.repaintCanvas();
}

/**
 * Refreshes links in the diagram using the Dagre engine.
 * @param engine The DiagramEngine instance.
 */
function autoRefreshLinks(engine: DiagramEngine) {
  const model = engine.getModel();

  const dagreEngine = genDagreEngine();
  dagreEngine.refreshLinks(model);

  reroute(engine);
  engine.repaintCanvas();
}

/**
 * Reroutes links in the diagram.
 * @param engine The DiagramEngine instance.
 */
function reroute(engine: DiagramEngine) {
  engine.getLinkFactories().getFactory<PathFindingLinkFactory>(PathFindingLinkFactory.NAME).calculateRoutingMatrix();
}
