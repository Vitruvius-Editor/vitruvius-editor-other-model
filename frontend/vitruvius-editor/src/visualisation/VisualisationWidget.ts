import { ReactWidget } from "@theia/core/lib/browser/widgets/react-widget";

/**
 * Abstract widget that represents a ReactWidget used to visualize a Vitruvius view.
 */
export abstract class VisualisationWidget<T> extends ReactWidget {
  // The content of the widget.
  protected content: T;

  /**
   * Initializes the widget with the given id, label and initial content.
   * @param id The id of the widget.
   * @param initialLabel The label of the widget.
   * @param initialContent The initial content of the widget.
   */
  protected async doInit(
    id: string,
    initialLabel: string,
    initialContent: T,
  ): Promise<void> {
    this.id = id;
    this.title.label = initialLabel;
    this.title.caption = initialLabel;
    this.title.closable = true;
    this.title.iconClass = "fa fa-window-maximize"; // example widget icon.\
    this.content = initialContent;
    this.update();
  }

  // Updates the content of the widget.
  updateContent(content: T): void {
    this.content = content;
    this.update();
  }

  // Returns the content of the widget.
  getContent(): T {
    return this.content;
  }

  // Sets the label of the widget.
  setLabel(label: string): void {
    this.title.label = label;
    this.title.caption = label;
    this.update();
  }
}
