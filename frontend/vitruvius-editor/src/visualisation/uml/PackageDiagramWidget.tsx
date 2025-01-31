import {
  inject,
  injectable,
  postConstruct,
} from "@theia/core/shared/inversify";
import * as React from "react";
import { MessageService } from "@theia/core";
import { VisualisationWidget } from "../VisualisationWidget";
import createEngine, { DiagramModel, CanvasWidget, DefaultLinkModel } from '@projectstorm/react-diagrams';
import { PackageImportLink, PackageNode } from "./PackageDiagramComponents";

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

    const packageNodeComponents: PackageNode[] = [];
    const packageImportLinks: PackageImportLink[] = [];

    const className1 = "Class Name1";
    const attributes1 = ["+attribute11: type", "-attribute12: type"];
    const methods1 = ["+method11: void", "-method12: type"];

    const packageNode1 = new PackageNode( className1, attributes1, methods1 )
    packageNode1.setPosition(100, 100);

    const className2 = "Class Name2";
    const attributes2 = ["+attribute21: type", "-attribute22: type"];
    const methods2 = ["+method21: void", "-method22: type"];

    const packageNode2 = new PackageNode( className2, attributes2, methods2 )
    packageNode2.setPosition(600, 100);

    packageNodeComponents.push(packageNode1);
    packageNodeComponents.push(packageNode2);

    // link
    const customLink = new PackageImportLink(packageNode1, packageNode2);
    packageImportLinks.push(customLink);

    //model
    const model = new DiagramModel();
    packageNodeComponents.forEach(component => model.addNode(component));
    packageImportLinks.forEach(link => model.addLink(link));
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


