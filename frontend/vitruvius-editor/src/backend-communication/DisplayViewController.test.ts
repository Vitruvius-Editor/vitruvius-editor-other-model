import { BackendServer } from "./BackendServer";
import { DisplayViewService } from "./DisplayViewService";
import { DisplayView } from "../model/DisplayView";
import { Selector } from "../model/Selector";
import { generateUuid } from "@theia/core";

describe("DisplayViewService", () => {
    let backendServer: BackendServer;
    let displayViewService: DisplayViewService;
    let sendWebRequestStub: jest.SpyInstance;
    const connectionId = generateUuid();

    beforeEach(() => {
        backendServer = new BackendServer("http://localhost:8080");
        displayViewService = new DisplayViewService(backendServer);
        sendWebRequestStub = jest.spyOn(backendServer, "sendWebRequest");
    });

    afterEach(() => {
        jest.restoreAllMocks();
    });

    describe("getDisplayViews", () => {
        it("should return a list of display views", async () => {
            const mockDisplayViews: DisplayView[] = [
                {
                    name: "ClassDiagramDisplayView",
                    viewTypeName: "ClassDiagram",
                    viewMapperName: "ClassDiagramMapper",
                    contentSelectorName: "All",
                    windowSelectorName: "All",
                },
            ];
            sendWebRequestStub.mockResolvedValue(mockDisplayViews);

            const displayViews = await displayViewService.getDisplayViews(connectionId);
            expect(displayViews).toEqual(mockDisplayViews);
            expect(sendWebRequestStub).toHaveBeenCalledWith(
                `/api/v1/connection/${connectionId}/displayViews`,
                "GET"
            );
        });
    });

    describe("getDisplayViewWindows", () => {
        it("should return a list of windows for a display view", async () => {
            const mockWindows = { windows: ["Window 1"] };
            sendWebRequestStub.mockResolvedValue(mockWindows);

            const windows = await displayViewService.getDisplayViewWindows(connectionId, "DisplayView 1");
            expect(windows).toEqual(mockWindows.windows);
            expect(sendWebRequestStub).toHaveBeenCalledWith(
                `/api/v1/connection/${connectionId}/displayView/DisplayView 1`,
                "GET"
            );
        });

        it("should return null if the display view is not found", async () => {
            sendWebRequestStub.mockResolvedValue(Promise.reject("Not Found"));

            const windows = await displayViewService.getDisplayViewWindows(connectionId, "NonExistentDisplayView");
            expect(windows).toBeNull();
            expect(sendWebRequestStub).toHaveBeenCalledWith(
                `/api/v1/connection/${connectionId}/displayView/NonExistentDisplayView`,
                "GET"
            );
        });
    });

    describe("getDisplayViewContent", () => {
        it("should return the content of a display view", async () => {
            const mockContent = { content: "DisplayView Content" };
            const selector: Selector = { windows: ["Window1", "Window2"] };
            sendWebRequestStub.mockResolvedValue(mockContent);

            const content = await displayViewService.getDisplayViewContent(connectionId, "DisplayView 1", selector);
            expect(content).toEqual(mockContent);
            expect(sendWebRequestStub).toHaveBeenCalledWith(
                `/api/v1/connection/${connectionId}/displayView/DisplayView 1`,
                "POST",
                selector
            );
        });

        it("should return null if the display view content is not found", async () => {
            const selector: Selector = { windows: ["Window1", "Window2"] };
            sendWebRequestStub.mockResolvedValue(null);

            const content = await displayViewService.getDisplayViewContent(connectionId, "NonExistentDisplayView", selector);
            expect(content).toBeNull();
            expect(sendWebRequestStub).toHaveBeenCalledWith(
                `/api/v1/connection/${connectionId}/displayView/NonExistentDisplayView`,
                "POST",
                selector
            );
        });
    });

    describe("updateDisplayViewContent", () => {
        it("should update the content of a display view", async () => {
            const mockUpdatedContent = { visualizerName: "TextVisualizer", content: "Updated DisplayView Content", windows: [{name: "Window1", content: "foo"}] };
            sendWebRequestStub.mockResolvedValue(mockUpdatedContent);

            const updatedContent = await displayViewService.updateDisplayViewContent(
                connectionId,
                "DisplayView 1",
                mockUpdatedContent
            );
            expect(updatedContent).toEqual(mockUpdatedContent);
            expect(sendWebRequestStub).toHaveBeenCalledWith(
                `/api/v1/connection/${connectionId}/displayView/DisplayView 1`,
                "PUT",
                mockUpdatedContent
            );
        });

        it("should return null if the display view content update fails", async () => {
            const mockUpdatedContent = { visualizerName: "TextVisualizer", content: "Updated DisplayView Content", windows: [{name: "Window1", content: "foo"}] };
            sendWebRequestStub.mockResolvedValue(null);

            const updatedContent = await displayViewService.updateDisplayViewContent(
                connectionId,
                "NonExistentDisplayView",
                mockUpdatedContent
            );
            expect(updatedContent).toBeNull();
            expect(sendWebRequestStub).toHaveBeenCalledWith(
                `/api/v1/connection/${connectionId}/displayView/NonExistentDisplayView`,
                "PUT",
                mockUpdatedContent
            );
        });
    });
});