import { BackendServer } from "./BackendServer";
import { DisplayView } from "../model/DisplayView";
import { Selector } from "../model/Selector";

export class DisplayViewService {
  private backendServer: BackendServer;
  readonly connectionId: string;

  constructor(backendServer: BackendServer, connectionId: string) {
    this.backendServer = backendServer;
    this.connectionId = connectionId;
  }

  async getDisplayViews(): Promise<DisplayView[]> {
    return this.backendServer.sendWebRequest(
      `/api/v1/connection/${this.connectionId}/displayViews`,
      "GET",
    );
  }

  async getDisplayViewWindows(
    displayViewName: string,
  ): Promise<Window[] | null> {
    return this.backendServer.sendWebRequest(
      `/api/v1/connection/${this.connectionId}/displayView/${displayViewName}`,
      "GET",
    );
  }

  async getDisplayViewContent(
    displayViewName: string,
    selector: Selector,
  ): Promise<string | null> {
    return this.backendServer.sendWebRequest(
      `/api/v1/connection/${this.connectionId}/displayView/${displayViewName}/content`,
      "POST",
      selector,
    );
  }

  async updateDisplayViewContent(
    displayViewName: string,
    updatedContent: string,
  ): Promise<string | null> {
    return this.backendServer.sendWebRequest(
      `/api/v1/connection/${this.connectionId}/displayView/${displayViewName}`,
      "PUT",
      updatedContent,
    );
  }
}
