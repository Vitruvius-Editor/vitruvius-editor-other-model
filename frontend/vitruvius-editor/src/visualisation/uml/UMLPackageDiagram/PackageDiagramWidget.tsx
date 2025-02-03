import {
  inject,
  injectable,
  postConstruct,
} from "@theia/core/shared/inversify";
import * as React from "react";
import { MessageService } from "@theia/core";
import { VisualisationWidget } from "../../VisualisationWidget";
import createEngine, {DiagramModel, CanvasWidget} from '@projectstorm/react-diagrams';
import {AdvancedLinkFactory, AdvancedPortModel, PackageImportLink, PackageNode} from "./PackageDiagramComponents";

/**
 * A Widget to visualize a UML Package Vitruvius view.
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
   * Renders the widget containing the content as a UML Package Diagram given from the Backend.
   */
  render(): React.ReactElement {
    const engine = createEngine();
    engine.getLinkFactories().registerFactory(new AdvancedLinkFactory());

    const packageNodeComponents: PackageNode[] = [];
    const packageImportLinks: PackageImportLink[] = [];

    const className1 = "Class Name1";
    const attributes1 = ["+attribute11: type", "-attribute12: type"];
    const methods1 = ["+method11: void", "-method12: type"];

    const packageNode1 = new PackageNode( className1, attributes1, methods1 )
    packageNode1.setPosition(100, 100);
    let port1 = packageNode1.addPort(new AdvancedPortModel(false, 'out'));

    const className2 = "Class Name2";
    const attributes2 :string[] = ["+attribute21: type", "-attribute22: type"];
    const methods2 :string[] = ["+method21: void", "-method22: type"];

    const packageNode2 = new PackageNode( className2, attributes2, methods2 )
    packageNode2.setPosition(600, 100);
    let port2 = packageNode2.addPort(new AdvancedPortModel(true, 'in'));

    packageNodeComponents.push(packageNode1);
    packageNodeComponents.push(packageNode2);

    // link
    const customLink = port1.link(port2);
    // const customLink = packageNode1.getPort().link(packageNode2.getPort());
    // const customLink = new PackageImportLink(packageNode1, packageNode2);
    packageImportLinks.push(customLink);

    //model
    const model = new DiagramModel();
    packageNodeComponents.forEach(component => model.addNode(component));
    packageImportLinks.forEach(link => model.addLink(link));
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


