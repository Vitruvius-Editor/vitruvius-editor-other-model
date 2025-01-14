import {BackendServer} from "./BackendServer";
import {DisplayView} from "../model/DisplayView";
import {Selector} from "../model/Selector";

export class DisplayViewService {
    // @ts-ignore
    private backendServer: BackendServer;
    readonly connectionId: string;

    constructor(backendServer: BackendServer, connectionId: string) {
        this.backendServer = backendServer;
        this.connectionId = connectionId;
    }

    async getDisplayViews(): Promise<DisplayView[]> {
        return new Promise<DisplayView[]>((resolve, reject) => {})
    }

    async getDisplayViewWindows(displayViewName: string): Promise<Window[] | null> {
        return new Promise<Window[]>((resolve, reject) => {})
    }

    async getDisplayViewContent(displayViewName: string, selector: Selector): Promise<string | null> {
        return new Promise<string>((resolve, reject) => {})
    }

    async updateDisplayViewContent(displayViewName: string, updatedContent: string): Promise<string | null> {
        return new Promise<string>((resolve, reject) => {})
    }
}
