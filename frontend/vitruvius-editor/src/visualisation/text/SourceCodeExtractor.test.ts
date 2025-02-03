import { SourceCodeExtractor } from './SourceCodeExtractor';
import { TextWidget } from './TextWidget';
import { Content } from '../../model/Content';

describe('SourceCodeExtractor', () => {
    let extractor: SourceCodeExtractor;
    let widget: jest.Mocked<TextWidget>;

    beforeEach(() => {
        extractor = new SourceCodeExtractor();
        widget = {
            getLabel: jest.fn(),
            getContent: jest.fn()
        } as unknown as jest.Mocked<TextWidget>;
    });

    it('should extract content from widget', async () => {
        widget.getLabel.mockReturnValue('TestWindow');
        widget.getContent.mockReturnValue('Test Content');

        const content: Content = await extractor.extractContent(widget);

        expect(content).toEqual({
            visualizerName: 'TextVisualizer',
            windows: [{
                name: 'TestWindow',
                content: 'Test Content'
            }]
        });
    });

    it('should handle empty content from widget', async () => {
        widget.getLabel.mockReturnValue('TestWindow');
        widget.getContent.mockReturnValue('');

        const content: Content = await extractor.extractContent(widget);

        expect(content).toEqual({
            visualizerName: 'TextVisualizer',
            windows: [{
                name: 'TestWindow',
                content: ''
            }]
        });
    });
});