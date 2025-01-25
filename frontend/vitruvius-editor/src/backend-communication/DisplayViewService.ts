import { BackendServer } from "./BackendServer";
import { DisplayView } from "../model/DisplayView";
import { Selector } from "../model/Selector";
import {inject, injectable} from "@theia/core/shared/inversify";

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
    return this.backendServer.sendWebRequest<WindowResponse>(
      `/api/v1/connection/${connectionId}/displayView/${displayViewName}`,
      "GET",
    ).then(windowResponse => windowResponse.windows);
  }

  async getDisplayViewContent(
	connectionId: string,
    displayViewName: string,
    selector: Selector,
  ): Promise<string | null> {
    return this.backendServer.sendWebRequest(
      `/api/v1/connection/${connectionId}/displayView/${displayViewName}/content`,
      "POST",
      selector,
    );
  }

  async updateDisplayViewContent(
	connectionId: string,
    displayViewName: string,
    updatedContent: string,
  ): Promise<string | null> {
    return this.backendServer.sendWebRequest(
      `/api/v1/connection/${connectionId}/displayView/${displayViewName}`,
      "PUT",
      updatedContent,
    );
  }
}

type WindowResponse = {windows: string[]};
