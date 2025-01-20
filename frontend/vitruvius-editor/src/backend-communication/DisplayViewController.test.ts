import { expect } from 'chai';
import { describe, it, beforeEach, afterEach } from 'mocha';
import sinon from 'sinon';
import { BackendServer } from './BackendServer';
import { DisplayViewService } from './DisplayViewService';
import { DisplayView } from '../model/DisplayView';
import { Selector } from '../model/Selector';

describe('DisplayViewService', () => {
    let backendServer: BackendServer;
    let displayViewService: DisplayViewService;
    let sendWebRequestStub: sinon.SinonStub;
    const connectionId = 'test-connection-id';

    beforeEach(() => {
        backendServer = new BackendServer('http://localhost:8080');
        displayViewService = new DisplayViewService(backendServer, connectionId);
        sendWebRequestStub = sinon.stub(backendServer, 'sendWebRequest');
    });

    afterEach(() => {
        sinon.restore();
    });

    describe('getDisplayViews', () => {
        it('should return a list of display views', async () => {
            const mockDisplayViews: DisplayView[] = [
                { name: 'ClassDiagramDisplayView',
                    viewTypeName: 'ClassDiagram',
                    viewMapperName: 'ClassDiagramMapper',
                    contentSelectorName: 'All',
                    windowSelectorName: 'All' },];
            sendWebRequestStub.resolves(mockDisplayViews);

            const displayViews = await displayViewService.getDisplayViews();
            expect(displayViews).to.deep.equal(mockDisplayViews);
            expect(sendWebRequestStub.calledOnceWith(`/api/v1/connection/${connectionId}/displayViews`, 'GET')).to.be.true;
        });
    });

    describe('getDisplayViewWindows', () => {
        it('should return a list of windows for a display view', async () => {
            const mockWindows = [{ name: 'Window 1' }];
            sendWebRequestStub.resolves(mockWindows);

            const windows = await displayViewService.getDisplayViewWindows('DisplayView 1');
            expect(windows).to.deep.equal(mockWindows);
            expect(sendWebRequestStub.calledOnceWith(`/api/v1/connection/${connectionId}/displayView/DisplayView 1`, 'GET')).to.be.true;
        });

        it('should return null if the display view is not found', async () => {
            sendWebRequestStub.resolves(null);

            const windows = await displayViewService.getDisplayViewWindows('NonExistentDisplayView');
            expect(windows).to.be.null;
            expect(sendWebRequestStub.calledOnceWith(`/api/v1/connection/${connectionId}/displayView/NonExistentDisplayView`, 'GET')).to.be.true;
        });
    });

    describe('getDisplayViewContent', () => {
        it('should return the content of a display view', async () => {
            const mockContent = 'DisplayView Content';
            const selector: Selector = {windows: ["Window1", "Window2"]};
            sendWebRequestStub.resolves(mockContent);

            const content = await displayViewService.getDisplayViewContent('DisplayView 1', selector);
            expect(content).to.equal(mockContent);
            expect(sendWebRequestStub.calledOnceWith(`/api/v1/connection/${connectionId}/displayView/DisplayView 1/content`, 'POST', selector)).to.be.true;
        });

        it('should return null if the display view content is not found', async () => {
            const selector: Selector = {windows: ["Window1", "Window2"]};
            sendWebRequestStub.resolves(null);

            const content = await displayViewService.getDisplayViewContent('NonExistentDisplayView', selector);
            expect(content).to.be.null;
            expect(sendWebRequestStub.calledOnceWith(`/api/v1/connection/${connectionId}/displayView/NonExistentDisplayView/content`, 'POST', selector)).to.be.true;
        });
    });

    describe('updateDisplayViewContent', () => {
        it('should update the content of a display view', async () => {
            const mockUpdatedContent = 'Updated DisplayView Content';
            sendWebRequestStub.resolves(mockUpdatedContent);

            const updatedContent = await displayViewService.updateDisplayViewContent('DisplayView 1', mockUpdatedContent);
            expect(updatedContent).to.equal(mockUpdatedContent);
            expect(sendWebRequestStub.calledOnceWith(`/api/v1/connection/${connectionId}/displayView/DisplayView 1`, 'PUT', mockUpdatedContent)).to.be.true;
        });

        it('should return null if the display view content update fails', async () => {
            const mockUpdatedContent = 'Updated DisplayView Content';
            sendWebRequestStub.resolves(null);

            const updatedContent = await displayViewService.updateDisplayViewContent('NonExistentDisplayView', mockUpdatedContent);
            expect(updatedContent).to.be.null;
            expect(sendWebRequestStub.calledOnceWith(`/api/v1/connection/${connectionId}/displayView/NonExistentDisplayView`, 'PUT', mockUpdatedContent)).to.be.true;
        });
    });
});
