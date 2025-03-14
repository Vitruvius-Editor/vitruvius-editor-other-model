import { Content } from "../../model/Content";
import { Visualizer } from "../Visualizer";
import { VisualisationWidget } from "../VisualisationWidget";
import { inject, injectable } from "@theia/core/shared/inversify";
import { ApplicationShell, WidgetManager } from "@theia/core/lib/browser";
import { DiagramWidget } from "./DiagramWidget";

/**
 * The DiagramVisualizer class is responsible for visualizing content in a diagram format.
 * It implements the Visualizer interface.
 */
@injectable()
export class DiagramVisualizer implements Visualizer {
  @inject(WidgetManager)
  private readonly widgetManager!: WidgetManager;

  @inject(ApplicationShell)
  protected readonly shell: ApplicationShell;

  /**
   * Visualizes the given content by creating or retrieving a DiagramWidget and updating it with the content.
   *
   * @param content - The content to visualize.
   * @returns A promise that resolves to a VisualisationWidget containing the visualized content.
   */
  async visualizeContent(content: Content): Promise<VisualisationWidget<any>> {
    let contentWindow = content.windows[0];
    return this.widgetManager
      .getOrCreateWidget(DiagramWidget.ID, "ClassDiagram " + contentWindow.name)
      .then((widget) => {
        let diagram = JSON.parse(contentWindow.content);
        (widget as DiagramWidget).updateContent(diagram);
        return widget as VisualisationWidget<string>;
      });
  }
}
