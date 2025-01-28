/**
 * Represents the content structure with a visualizer name and an array of windows.
 */
export type Content = {
  /**
   * The name of the visualizer.
   */
  visualizerName: string;

  /**
   * An array of windows, each containing a name and content.
   */
  windows: {
    /**
     * The name of the window.
     */
    name: string;

    /**
     * The content of the window.
     */
    content: string;
  }[];
};
