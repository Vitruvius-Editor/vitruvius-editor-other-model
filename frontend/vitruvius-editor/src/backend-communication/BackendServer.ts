import { inject, injectable } from "@theia/core/shared/inversify";

@injectable()
export class BackendServer {
  readonly url: string;

  constructor(@inject("Url") url: string) {
    this.url = url;
  }

  /**
   * Checks whether the server is alive.
   */
  async checkHealthy(): Promise<boolean> {
    return new Promise<boolean>((resolve) => {
      this.sendWebRequest<ActuatorResponse>("/actuator/health", "GET")
        .then((response) => resolve(response.status == "UP"))
        .catch((_exception) => resolve(false));
    });
  }

  /**
   * Sends a web request to the backend server and parses the result into a data type/
   * @param path The path on the server
   * @param method The request type
   * @param body The request body if present
   */
  async sendWebRequest<T>(
    path: string,
    method: "GET" | "POST" | "PUT" | "DELETE",
    body?: any,
  ): Promise<T> {
    const url = `${this.url}${path}`;
    let options: RequestInit = {
      method,
      headers: {
        "Content-Type": "application/json",
      },
    };

    if (body) {
      options.body = JSON.stringify(body);
    }

    return fetch(url, options)
      .then((response) => {
        if (!response.ok) {
          return Promise.reject(`HTTP error! status: ${response.status}`);
        }
        return response.json();
      })
      .then((data) => data as T)
      .catch((exception) => {
        return Promise.reject(exception);
      });
  }
}

export type ActuatorResponse = { status: string };
