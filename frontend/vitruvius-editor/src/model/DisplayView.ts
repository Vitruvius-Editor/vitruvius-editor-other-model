/**
 * Represents a display view with various selector and mapper names.
 */
export type DisplayView = {
  /**
   * The name of the display view.
   */
  name: string;

  /**
   * The name of the view type.
   */
  viewTypeName: string;

  /**
   * The name of the view mapper.
   */
  viewMapperName: string;

  /**
   * The name of the window selector.
   */
  windowSelectorName: string;

  /**
   * The name of the content selector.
   */
  contentSelectorName: string;
};
