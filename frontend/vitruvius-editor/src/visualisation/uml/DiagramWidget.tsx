import {
  inject,
  injectable,
  postConstruct,
} from "@theia/core/shared/inversify";
import * as React from "react";
import { MessageService } from "@theia/core";
import { VisualisationWidget } from "../VisualisationWidget";
import createEngine, {DiagramModel, CanvasWidget} from '@projectstorm/react-diagrams';
import {AdvancedLinkFactory, PackageImportLink, PackageNode} from "./DiagramComponents";

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

    const componentFetcher = new ClassesFetcher();
    const umlPackageDiagram = componentFetcher.parse(this.getContent());

    const packageNodeComponents: PackageNode[] = [];
    const packageImportLinks: PackageImportLink[] = [];

    umlPackageDiagram.getPackages().forEach(umlClass => {
      const packageNode = new PackageNode(umlClass.classID, umlClass.name, umlClass.attributes, umlClass.methods);
      packageNodeComponents.push(packageNode);
    });

    umlPackageDiagram.getLinks().forEach(link => {
      const fromPackage = packageNodeComponents.find(node => node.getClassID() === link.fromID);
      const toPackage = packageNodeComponents.find(node => node.getClassID() === link.toID);
      if (fromPackage !== undefined && toPackage !== undefined) {
        packageImportLinks.push(new PackageImportLink(fromPackage, toPackage));

      }
    });

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

/**
 * A class used to fetch the Classes from the Backend.
 * Currently the classes are hardcoded for demonstration purposes.
 */
class ClassesFetcher {
  packages: UMLPackage[];

  className1 = "Class Name1";
  attributes1 = ["+attribute11: type", "-attribute12: type", "adsfsfsdfsdfsdf"];
  methods1 = ["+method11: void", "-method12: type"];
  umlPackage1:UMLPackage = new UMLPackage( 'a1', this.className1, this.attributes1, this.methods1 );

  className2 :string = "Class Name2";
  attributes2 :string[] = ["+attribute21: type", "-attribute22: type"];
  methods2 :string[] = ["+method21: void", "-method22: type"];
  umlPackage2:UMLPackage = new UMLPackage( 'b2', this.className2, this.attributes2, this.methods2 );

  constructor() {
    this.packages = [this.umlPackage1, this.umlPackage2];
  }

  parse(content: string): UMLPackageDiagram {
    return new UMLPackageDiagram();
  }

  getPackages(): UMLPackage[] {
    return this.packages;
  }

  getLinks(): UMLPackageLink[] {
    return [new UMLPackageLink('a1', 'b2')];
  }
}