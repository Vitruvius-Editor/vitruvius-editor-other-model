import { BackendServer } from "./BackendServer";
import fetchMock from "fetch-mock";

describe("BackendServer", () => {
  const serverUrl = "http://localhost:3000";
  const backendServer = new BackendServer(serverUrl);

  beforeEach(() => {
    fetchMock.mockGlobal();
  });

  afterEach(() => {
    // Reset the fetch mock after each test
    fetchMock.unmockGlobal();
  });

  describe("checkHealthy", () => {
    it("should return true if the server is healthy", async () => {
      fetchMock.getOnce(serverUrl + "/actuator/health", {
        status: 200,
        body: { status: "UP" },
        headers: { "Content-Type": "application/json" },
      });
      const result = await backendServer.checkHealthy();
      expect(result).toBe(true);
    });

    it("should return false if the server is not healthy", async () => {
      fetchMock.getOnce(serverUrl + "/actuator/health", {
        status: 200,
        body: { status: "DOWN" },
        headers: { "Content-Type": "application/json" },
      });

      const result1 = await backendServer.checkHealthy();
      expect(result1).toBe(false);

      fetchMock.getOnce(serverUrl + "/actuator/health", {
        status: 404,
        headers: { "Content-Type": "application/json" },
      });
      const result2 = await backendServer.checkHealthy();
      expect(result2).toBe(false);
    });

    it("should return false if there is an error", async () => {
      fetchMock.getOnce(serverUrl, { throws: new Error("Network error") });

      const result = await backendServer.checkHealthy();
      expect(result).toBe(false);
    });
  });

  describe("sendWebRequest", () => {
    it("should send a GET request and return the parsed response", async () => {
      const mockResponse = { data: "test" };
      fetchMock.getOnce(serverUrl + "/test", {
        body: mockResponse,
        headers: { "Content-Type": "application/json" },
      });

      const result = await backendServer.sendWebRequest<{ data: string }>(
          "/test",
          "GET",
      );
      expect(result).toEqual(mockResponse);
    });

    it("should send a POST request with a body and return the parsed response", async () => {
      const mockResponse = { data: "test" };
      const requestBody = { key: "value" };
      fetchMock.postOnce(serverUrl + "/test", {
        body: mockResponse,
        headers: { "Content-Type": "application/json" },
      });

      const result = await backendServer.sendWebRequest<{ data: string }>(
          "/test",
          "POST",
          requestBody,
      );
      expect(result).toEqual(mockResponse);
    });

    it("should handle a non-ok response and reject the promise", async () => {
      fetchMock.getOnce(serverUrl + "/test", 404);

      await expect(
          backendServer.sendWebRequest<{ data: string }>("/test", "GET")
      ).rejects.toEqual("HTTP error! status: 404");
    });

    it("should handle a network error and reject the promise", async () => {
      fetchMock.getOnce(serverUrl + "/test", {
        throws: new Error("Network Error"),
      });

      await expect(
          backendServer.sendWebRequest<{ data: string }>("/test", "GET")
      ).rejects.toThrow("Network Error");
    });
  });
});