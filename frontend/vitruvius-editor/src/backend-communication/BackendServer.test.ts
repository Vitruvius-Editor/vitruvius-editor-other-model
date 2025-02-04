import { BackendServer, ActuatorResponse } from './BackendServer'; // Adjust the import path as necessary

// Mock the global fetch function
global.fetch = jest.fn();

describe('BackendServer', () => {
  const mockUrl = 'http://localhost:3000';
  let backendServer: BackendServer;

  beforeEach(() => {
    backendServer = new BackendServer(mockUrl);
    jest.clearAllMocks(); // Clear previous mocks before each test
  });

  describe('checkHealthy', () => {
    it('should return true when server is healthy', async () => {
      const mockResponse: ActuatorResponse = { status: 'UP' };
      (fetch as jest.Mock).mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce(mockResponse),
      });

      const result = await backendServer.checkHealthy();
      expect(result).toBe(true);
      expect(fetch).toHaveBeenCalledWith(`${mockUrl}/actuator/health`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
      });
    });

    it('should return false when server is not healthy', async () => {
      const mockResponse: ActuatorResponse = { status: 'DOWN' };
      (fetch as jest.Mock).mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce(mockResponse),
      });

      const result = await backendServer.checkHealthy();
      expect(result).toBe(false);
    });

    it('should return false when fetch fails', async () => {
      (fetch as jest.Mock).mockRejectedValueOnce(new Error('Network error'));

      const result = await backendServer.checkHealthy();
      expect(result).toBe(false);
    });
  });

  describe('sendWebRequest', () => {
    it('should return data when request is successful', async () => {
      const mockResponseData = { data: 'some data' };
      (fetch as jest.Mock).mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce(mockResponseData),
      });

      const result = await backendServer.sendWebRequest('/test', 'GET');
      expect(result).toEqual(mockResponseData);
      expect(fetch).toHaveBeenCalledWith(`${mockUrl}/test`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
      });
    });

    it('should throw an error when response is not ok', async () => {
      (fetch as jest.Mock).mockResolvedValueOnce({
        ok: false,
        status: 404,
      });

      await expect(backendServer.sendWebRequest('/test', 'GET')).rejects.toEqual('HTTP error! status: 404');
    });

    it('should throw an error when fetch fails', async () => {
      (fetch as jest.Mock).mockRejectedValueOnce(new Error('Network error'));

      await expect(backendServer.sendWebRequest('/test', 'GET')).rejects.toEqual(new Error('Network error'));
    });

    it('should send a request with a body when provided', async () => {
      const mockResponseData = { data: 'some data' };
      const requestBody = { key: 'value' };
      (fetch as jest.Mock).mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce(mockResponseData),
      });

      const result = await backendServer.sendWebRequest('/test', 'POST', requestBody);
      expect(result).toEqual(mockResponseData);
      expect(fetch).toHaveBeenCalledWith(`${mockUrl}/test`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(requestBody),
      });
    });
  });
});