export class BackendServer {
    readonly url: string;

    constructor(url: string) {
        this.url = url;
    }

    /**
     * Checks whether the server is alive.
     */
    async checkHealthy(): Promise<boolean> {
        return new Promise<boolean>(async (resolve) => {})
    }

    /**
     * Sends a web request to the backend server and parses the result into a data type/
     * @param path The path on the server
     * @param method The request type
     * @param body The request body if present
     */
    async sendWebRequest<T>(path: string, method: 'GET' | 'POST' | 'PUT' | 'DELETE', body?: any): Promise<T> {
        return new Promise<T>((resolve, reject) => {})
    }
}