import { BackendServer } from "./BackendServer";
import { Connection } from "../model/Connection";
import {inject, injectable} from "@theia/core/shared/inversify";

@injectable()
export class ConnectionService {
  private backendServer: BackendServer;

  constructor(@inject(BackendServer) backendServer: BackendServer) {
    this.backendServer = backendServer;
  }

  async getConnections(): Promise<Connection[]> {
    return this.backendServer.sendWebRequest("/api/v1/connections", "GET");
  }

  async getConnection(uuid: string): Promise<Connection | null> {
    return this.backendServer.sendWebRequest(
      `/api/v1/connection/${uuid}`,
      "GET",
    );
  }

  async deleteConnection(uuid: string): Promise<void> {
    return this.backendServer.sendWebRequest(
      `/api/v1/connection/${uuid}`,
      "DELETE",
    );
  }

  async createConnection(connectionCreationRequest: {
    name: string;
    description: string;
    url: string;
  }): Promise<Connection> {
    return this.backendServer.sendWebRequest(
      "/api/v1/connection",
      "POST",
      connectionCreationRequest,
    );
  }

  async updateConnection(
    uuid: string,
    connectionUpdateRequest: Partial<{
      name: string;
      description: string;
      url: string;
    }>,
  ): Promise<Connection> {
    return this.backendServer.sendWebRequest(
      `/api/v1/connection/${uuid}`,
      "PUT",
      connectionUpdateRequest,
    );
  }
}
