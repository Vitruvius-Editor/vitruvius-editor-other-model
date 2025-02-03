import { TableExtractor } from './TableExtractor';
import { TableWidget } from './TableWidget';
import { Content } from '../../model/Content';
import { Table } from './Table';

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
        const table: Table = {
            rows: [
                { uuid: 'uuid1', name: 'name1', visibility: 'public', isAbstract: false, isFinal: false, superClassName: 'superClass1', interfaces: ['interface1'], attributeCount: 2, methodCount: 3, linesOfCode: 100 }
            ],
            columns: [
                { fieldName: "name", displayName: "Name", shouldBeDisplayed: true, editable: true, fieldType: "string" },
                { fieldName: "visibility", displayName: "Visibility", shouldBeDisplayed: true, editable: false, fieldType: "string" },
                { fieldName: "isAbstract", displayName: "Abstract", shouldBeDisplayed: true, editable: false, fieldType: "boolean" },
                { fieldName: "isFinal", displayName: "Final", shouldBeDisplayed: true, editable: false, fieldType: "boolean" },
                { fieldName: "superClassName", displayName: "Super Class", shouldBeDisplayed: true, editable: false, fieldType: "string" },
                { fieldName: "interfaces", displayName: "Interfaces", shouldBeDisplayed: true, editable: false, fieldType: "array" },
                { fieldName: "attributeCount", displayName: "Attributes", shouldBeDisplayed: true, editable: false, fieldType: "number" },
                { fieldName: "methodCount", displayName: "Methods", shouldBeDisplayed: true, editable: false, fieldType: "number" },
                { fieldName: "linesOfCode", displayName: "Lines of Code", shouldBeDisplayed: true, editable: false, fieldType: "number" }
            ]
        };
        widget.getLabel.mockReturnValue('TestWindow');
        widget.getContent.mockReturnValue(table);

        const content: Content = await extractor.extractContent(widget);

        expect(content).toEqual({
            visualizerName: 'TableVisualizer',
            windows: [{
                name: 'TestWindow',
                content: JSON.stringify(table)
            }]
        });
    });

    it('should handle empty content from widget', async () => {
        const table: Table = { rows: [], columns: [] };
        widget.getLabel.mockReturnValue('TestWindow');
        widget.getContent.mockReturnValue(table);

        const content: Content = await extractor.extractContent(widget);

        expect(content).toEqual({
            visualizerName: 'TableVisualizer',
            windows: [{
                name: 'TestWindow',
                content: JSON.stringify(table)
            }]
        });
    });
});