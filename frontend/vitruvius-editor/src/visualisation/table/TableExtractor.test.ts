import { TableExtractor } from './TableExtractor';
import { TableWidget } from './TableWidget';
import { Content } from '../../model/Content';
import { TableEntry } from './TableEntry';

describe('TableExtractor', () => {
    let extractor: TableExtractor;
    let widget: jest.Mocked<TableWidget>;

    beforeEach(() => {
        extractor = new TableExtractor();
        widget = {
            getLabel: jest.fn(),
            getContent: jest.fn()
        } as unknown as jest.Mocked<TableWidget>;
    });

    it('should extract content from widget', async () => {
        const entries: TableEntry[] = [
            { uuid: 'uuid1', name: 'name1', visibility: 'public', isAbstract: false, isFinal: false, superClassName: 'superClass1', interfaces: ['interface1'], attributeCount: 2, methodCount: 3, linesOfCode: 100 }
        ];
        widget.getLabel.mockReturnValue('TestWindow');
        widget.getContent.mockReturnValue(entries);

        const content: Content = await extractor.extractContent(widget);

        expect(content).toEqual({
            visualizerName: 'TableVisualizer',
            windows: [{
                name: 'TestWindow',
                content: JSON.stringify({ entries })
            }]
        });
    });

    it('should handle empty content from widget', async () => {
        widget.getLabel.mockReturnValue('TestWindow');
        widget.getContent.mockReturnValue([]);

        const content: Content = await extractor.extractContent(widget);

        expect(content).toEqual({
            visualizerName: 'TableVisualizer',
            windows: [{
                name: 'TestWindow',
                content: JSON.stringify({ entries: [] })
            }]
        });
    });
});