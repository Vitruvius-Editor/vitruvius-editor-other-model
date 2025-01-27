/**
 * Represents a connection with a name, description, unique identifier, and URL.
 */
export type Connection = {
  /**
   * The name of the connection.
   */
  name: string;

  /**
   * A brief description of the connection.
   */
  description: string;

  /**
   * The unique identifier for the connection.
   */
  uuid: string;

  /**
   * The URL associated with the connection.
   */
  url: string;
};