import {UMLDiagram} from "./DiagramContent";
import {UMLNode, UMLRelation} from "./DiagramComponents";
import React from "react";

/**
 * A class to parse a UML Diagram from a string.
 * The string should be in JSON format and contain the nodes and links of the diagram.
 * Currently, the parsing is commented out and the nodes and links are hardcoded for testing/demonstration purposes.
 */
export class UMLDiagramParser {

    // The example nodes and links data
    nodesData :[string, string, string, string[], string[]][] = [
        ['Package', 'a1', 'class name1', ["+attribute11: type", "-attribute12: type"], ["+method11: void", "-method12: type"]],
        ['Class', 'b2', 'class name2', ["+attribute21: type", "-attribute22: type"], ["+method21: void", "-method22: type"]]
    ];
    linkData: [string, string, string, string][] = [["advanced", "LabelName", "a1", "b2"]];

    constructor() {
    }

    parse(content: string): UMLDiagram {
        const nodes: UMLNode[] = [];
        const links: UMLRelation[] = [];

        // Uncomment the following lines to parse the content
        // const data = JSON.parse(content);
        // this.nodesData = data.nodes;
        // this.idPairs = data.links;

        this.nodesData.forEach(data => {
            if (data[0] === 'Class') {
                const text = (
                <>
                    {data[2]} <br />
                <hr width="100%" size="2" color="black" noshade></hr>
                {data[3].map((attr, index) => (
                    <React.Fragment key={index}>
                        {attr} <br />
                        </React.Fragment>
                ))}

                <hr width="100%" size="2" color="black" noshade></hr>
                {data[4].map((method, index) => (
                    <React.Fragment key={index}>
                        {method} <br />
                        </React.Fragment>
                ))}
                </>
            )
                nodes.push(new UMLNode(data[1], text));
            } else if (data[0] === 'Package') {
                nodes.push(new UMLNode(data[1], data[2]));
            }

        });

        this.linkData.forEach(link => {
            const fromNode = nodes.find(node => node.getClassID() === link[2]);
            const toNode = nodes.find(node => node.getClassID() === link[3]);
            if (fromNode !== undefined && toNode !== undefined) {
                links.push(new UMLRelation(link[0], link[1], fromNode, toNode));
            }
        });

        return new UMLDiagram(nodes, links);
    }
}