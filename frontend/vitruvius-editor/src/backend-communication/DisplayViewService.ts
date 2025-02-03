import { BackendServer } from "./BackendServer";
import { DisplayView } from "../model/DisplayView";
import { Selector } from "../model/Selector";
import { inject, injectable } from "@theia/core/shared/inversify";
import { Content } from "../model/Content";

@injectable()
export class DisplayViewService {
  private backendServer: BackendServer;

  constructor(@inject(BackendServer) backendServer: BackendServer) {
    this.backendServer = backendServer;
  }

  async getDisplayViews(connectionId: string): Promise<DisplayView[]> {
    return this.backendServer.sendWebRequest(
      `/api/v1/connection/${connectionId}/displayViews`,
      "GET",
    );
  }

  async getDisplayViewWindows(
    connectionId: string,
    displayViewName: string,
  ): Promise<string[] | null> {
    return this.backendServer
      .sendWebRequest<WindowResponse>(
        `/api/v1/connection/${connectionId}/displayView/${displayViewName}`,
        "GET",
      )
      .then((windowResponse) => windowResponse.windows)
        .catch(() => null);
  }

  async getDisplayViewContent(
    connectionId: string,
    displayViewName: string,
    selector: Selector,
  ): Promise<Content | null> {
    return this.backendServer.sendWebRequest<Content>(
      `/api/v1/connection/${connectionId}/displayView/${displayViewName}`,
      "POST",
      selector,
    );
  }

  async updateDisplayViewContent(
    connectionId: string,
    displayViewName: string,
    updatedContent: Content,
  ): Promise<Content | null> {
    return this.backendServer.sendWebRequest(
      `/api/v1/connection/${connectionId}/displayView/${displayViewName}`,
      "PUT",
      updatedContent,
    );
  }
}

type WindowResponse = { windows: string[] };
