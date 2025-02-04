import {UMLDiagram} from "./DiagramContent";
import {UMLArrowLink, UMLNode} from "./DiagramComponents";

/**
 * A class to parse a UML Diagram from a string.
 * The string should be in JSON format and contain the nodes and links of the diagram.
 * Currently, the parsing is commented out and the nodes and links are hardcoded for testing/demonstration purposes.
 */
export class UMLDiagramParser {

    // The example nodes and links data
    nodesData :[string, string, string[], string[]][] = [
        ['a1', 'class name1', ["+attribute11: type", "-attribute12: type"], ["+method11: void", "-method12: type"]],
        ['b2', 'class name2', ["+attribute21: type", "-attribute22: type"], ["+method21: void", "-method22: type"]]
    ];
    idPairs: [string, string][] = [["a1", "b2"]];

    constructor() {
    }

    parse(content: string): UMLDiagram {
        const nodes: UMLNode[] = [];
        const links: UMLArrowLink[] = [];

        // Uncomment the following lines to parse the content
        // const data = JSON.parse(content);
        // this.nodesData = data.nodes;
        // this.idPairs = data.links;

        this.nodesData.forEach(data => {
            nodes.push(new UMLNode(data[0], data[1], data[2], data[3]));
        });

        this.idPairs.forEach(pair => {
            const fromNode = nodes.find(node => node.getClassID() === pair[0]);
            const toNode = nodes.find(node => node.getClassID() === pair[1]);
            if (fromNode !== undefined && toNode !== undefined) {
                links.push(new UMLArrowLink(fromNode, toNode));
            }
        });

        return new UMLDiagram(nodes, links);
    }
}