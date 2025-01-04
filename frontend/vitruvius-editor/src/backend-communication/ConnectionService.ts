import {BackendServer} from "./BackendServer";
import {Connection} from "../model/Connection";

export class ConnectionService {
    private backendServer: BackendServer;

    constructor(backendServer: BackendServer) {
        this.backendServer = backendServer;
    }

    async getConnections(): Promise<Connection[]> {
        return new Promise<Connection[]>(async (resolve) => {})
    }

    async getConnection(uuid: string): Promise<Connection | null> {
        return new Promise<Connection>((resolve) => {})
    }

    async deleteConnection(uuid: string): Promise<void> {
        return new Promise<void>((resolve) => {})
    }

    async createConnection(connectionCreationRequest: {name: string, description: string, url: string}): Promise<Connection> {
        return new Promise<Connection>((resolve) => {})
    }

    async updateConnection(connectionUpdateRequest: Partial<{name: string, description: string, url: string}>): Promise<Connection> {
        return new Promise<Connection>((resolve) => {})
    }
}