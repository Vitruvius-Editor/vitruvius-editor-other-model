import {
  inject,
  injectable,
  postConstruct,
} from "@theia/core/shared/inversify";
import * as React from "react";
import { MessageService } from "@theia/core";
import { VisualisationWidget } from "../VisualisationWidget";
import createEngine, { DiagramModel, CanvasWidget, DefaultLinkModel } from '@projectstorm/react-diagrams';
import { DefaultNodeModel } from '@projectstorm/react-diagrams-defaults';
import { PackageNode } from "./PackageDiagramComponents";

/**
 * A Widget to visualize a text based Vitruvius view.
 */
@injectable()
export class PackageDiagramWidget extends VisualisationWidget<string> {
  static readonly ID = "packagediagramwidget:packagediagramwidget";
  static readonly LABEL = "PackageDiagramWidget";

  @inject(MessageService)
  protected readonly messageService!: MessageService;

  /**
   * Initializes the widget with the default id, label and initial content.
   */
  @postConstruct()
  protected init(): void {
    this.doInit(PackageDiagramWidget.ID, PackageDiagramWidget.LABEL, "/*Initial Content*/");
  }

  /**
   * Renders the widget containing a text area to edit the content.
   */
  render(): React.ReactElement {
    const engine = createEngine();

    const className = "Class Name";
    const attributes = ["+attribute1: type", "-attribute2: type"];
    const methods = ["+method1: void", "-method2: type"];
    const packageNode1 = new PackageNode( className, attributes, methods )

    const node1 = new DefaultNodeModel({
      name: (
        <>
          {className} <br />
          <hr width="100%" size="2" color="black" noshade></hr>
          +attribute1: type <br />
          -attribute2: type <br />
          <hr width="100%" size="2" color="black" noshade></hr>
          +method1: void <br />
          -method2: type <br />
        </>
      ),
      color: 'rgb(145, 145, 145)',
    });
    node1.setPosition(100, 100);
    let port1 = node1.addOutPort('Out');
    let portin1 = node1.addInPort('In');

    // node 2
    const node2 = new DefaultNodeModel({
      name: 'Node 2',
      color: 'rgb(0,192,255)',
    });
    node2.setPosition(100, 100);
    let port2 = node2.addOutPort('Out');

    // link
    const link = port1.link<DefaultLinkModel>(port2);
    link.addLabel('Hello World!');

    //model
    const model = new DiagramModel();
    model.addAll(node1, node2, link, packageNode1);
    engine.setModel(model);

    return <CanvasWidget className="diagram-container" engine={engine} />;
  }


  /**
   * Handles the change event of the text area and updates the content.
   * @param event
   */
  handleChange(event: React.ChangeEvent<HTMLTextAreaElement>): void {
    this.content = event.target.value;
  }
}


