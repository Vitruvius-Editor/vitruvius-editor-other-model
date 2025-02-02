import { BackendServer } from "./BackendServer";
import { ConnectionService } from "./ConnectionService";
import { Connection } from "../model/Connection";
import { generateUuid } from "@theia/core";

describe("ConnectionService", () => {
  let backendServer: BackendServer;
  let connectionService: ConnectionService;
  let sendWebRequestStub: jest.SpyInstance;

  beforeEach(() => {
    backendServer = new BackendServer("http://localhost:8080");
    connectionService = new ConnectionService(backendServer);
    sendWebRequestStub = jest.spyOn(backendServer, "sendWebRequest");
  });

  afterEach(() => {
    jest.restoreAllMocks();
  });

  describe("getConnections", () => {
    it("should return a list of connections", async () => {
      const mockConnections: Connection[] = [
        {
          uuid: generateUuid(),
          name: "Connection 1",
          description: "Description",
          url: "http://example.com",
        },
        {
          uuid: generateUuid(),
          name: "Connection 2",
          description: "Description",
          url: "http://example.com",
        },
      ];
      sendWebRequestStub.mockResolvedValue(mockConnections);

      const connections = await connectionService.getConnections();
      expect(connections).toEqual(mockConnections);
      expect(sendWebRequestStub).toHaveBeenCalledWith("/api/v1/connections", "GET");
    });
  });

  describe("getConnection", () => {
    it("should return a connection by uuid", async () => {
      const mockConnection: Connection = {
        uuid: generateUuid(),
        name: "Connection 1",
        description: "Description",
        url: "http://example.com",
      };
      sendWebRequestStub.mockResolvedValue(mockConnection);

      const connection = await connectionService.getConnection(mockConnection.uuid);
      expect(connection).toEqual(mockConnection);
      expect(sendWebRequestStub).toHaveBeenCalledWith(`/api/v1/connection/${mockConnection.uuid}`, "GET");
    });

    it("should return null if the connection is not found", async () => {
      sendWebRequestStub.mockResolvedValue(null);

      const connection = await connectionService.getConnection("1");
      expect(connection).toBeNull();
      expect(sendWebRequestStub).toHaveBeenCalledWith("/api/v1/connection/1", "GET");
    });
  });

  describe("deleteConnection", () => {
    it("should delete a connection by uuid", async () => {
      sendWebRequestStub.mockResolvedValue(null);

      await connectionService.deleteConnection("1");
      expect(sendWebRequestStub).toHaveBeenCalledWith("/api/v1/connection/1", "DELETE");
    });
  });

  describe("createConnection", () => {
    it("should create a new connection", async () => {
      const mockConnection: Connection = {
        uuid: generateUuid(),
        name: "Connection 1",
        description: "Description",
        url: "http://example.com",
      };
      const connectionCreationRequest = {
        name: "Connection 1",
        description: "Description",
        url: "http://example.com",
      };
      sendWebRequestStub.mockResolvedValue(mockConnection);

      const connection = await connectionService.createConnection(connectionCreationRequest);
      expect(connection).toEqual(mockConnection);
      expect(sendWebRequestStub).toHaveBeenCalledWith("/api/v1/connection", "POST", connectionCreationRequest);
    });
  });

  describe("updateConnection", () => {
    it("should update an existing connection", async () => {
      const mockConnection: Connection = {
        uuid: generateUuid(),
        name: "Connection",
        description: "Description",
        url: "http://example.com",
      };
      const connectionUpdateRequest = { name: "Updated Connection" };
      sendWebRequestStub.mockResolvedValue(mockConnection);

      const connection = await connectionService.updateConnection("1", connectionUpdateRequest);
      expect(connection).toEqual(mockConnection);
      expect(sendWebRequestStub).toHaveBeenCalledWith("/api/v1/connection/1", "PUT", connectionUpdateRequest);
    });
  });
});
