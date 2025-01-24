import { expect } from "chai";
import { describe, it, beforeEach } from "mocha";
import sinon from "sinon";
import { BackendServer } from "./BackendServer";
import { ConnectionService } from "./ConnectionService";
import { Connection } from "../model/Connection";
import { generateUuid } from "@theia/core";

describe("ConnectionService", () => {
  let backendServer: BackendServer;
  let connectionService: ConnectionService;
  let sendWebRequestStub: sinon.SinonStub;

  beforeEach(() => {
    backendServer = new BackendServer("http://localhost:8080");
    connectionService = new ConnectionService(backendServer);
    sendWebRequestStub = sinon.stub(backendServer, "sendWebRequest");
  });

  afterEach(() => {
    sinon.restore();
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
      sendWebRequestStub.resolves(mockConnections);

      const connections = await connectionService.getConnections();
      expect(connections).to.deep.equal(mockConnections);
      expect(sendWebRequestStub.calledOnceWith("/api/v1/connections", "GET")).to
        .be.true;
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
      sendWebRequestStub.resolves(mockConnection);

      const connection = await connectionService.getConnection(
        mockConnection.id,
      );
      expect(connection).to.deep.equal(mockConnection);
      expect(
        sendWebRequestStub.calledOnceWith(
          "/api/v1/connection/" + mockConnection.id,
          "GET",
        ),
      ).to.be.true;
    });

    it("should return null if the connection is not found", async () => {
      sendWebRequestStub.resolves(null);

      const connection = await connectionService.getConnection("1");
      expect(connection).to.be.null;
      expect(sendWebRequestStub.calledOnceWith("/api/v1/connection/1", "GET"))
        .to.be.true;
    });
  });

  describe("deleteConnection", () => {
    it("should delete a connection by uuid", async () => {
      sendWebRequestStub.resolves();

      await connectionService.deleteConnection("1");
      expect(
        sendWebRequestStub.calledOnceWith("/api/v1/connection/1", "DELETE"),
      ).to.be.true;
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
      sendWebRequestStub.resolves(mockConnection);

      const connection = await connectionService.createConnection(
        connectionCreationRequest,
      );
      expect(connection).to.deep.equal(mockConnection);
      expect(
        sendWebRequestStub.calledOnceWith(
          "/api/v1/connection",
          "POST",
          connectionCreationRequest,
        ),
      ).to.be.true;
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
      sendWebRequestStub.resolves(mockConnection);

      const connection = await connectionService.updateConnection(
        "1",
        connectionUpdateRequest,
      );
      expect(connection).to.deep.equal(mockConnection);
      expect(
        sendWebRequestStub.calledOnceWith(
          "/api/v1/connection/1",
          "PUT",
          connectionUpdateRequest,
        ),
      ).to.be.true;
    });
  });
});
