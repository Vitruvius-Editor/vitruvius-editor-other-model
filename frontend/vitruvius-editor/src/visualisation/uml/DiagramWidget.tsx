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
import {UMLDiagramParser} from "./UMLDiagramParser";

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
    const model = new DiagramModel();
    umlDiagram.getNodes().forEach(component => model.addNode(component));
    umlDiagram.getLinks().forEach(link => model.addLink(link));
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