import {UMLDiagram} from "./DiagramContent";
import {UMLNode, UMLRelation} from "./DiagramComponents";
import React from "react";
import {Diagram} from "./Diagram";

/**
* A class to build the JSX Elements of a Diagram from the Diagram type.
*/
export class UMLDiagramBuilder {

    // The example nodes and links data
    nodesData :[string, string, string, string[], string[]][] = [
        ['Package', 'a1', 'class name1', ["+attribute11: type", "-attribute12: type"], ["+method11: void", "-method12: type"]],
        ['Class', 'b2', 'class name2', ["+attribute21: type", "-attribute22: type"], ["+method21: void", "-method22: type"]]
    ];
    linkData: [string, string, string, string][] = [["advanced", "LabelName", "a1", "b2"]];

    constructor() {
    }

    parse(diagram: Diagram, type: "Class" | "Package"): UMLDiagram {
        const nodes: UMLNode[] = [];
        const links: UMLRelation[] = [];

        diagram.nodes.forEach(data => {
            if (type === 'Class') {
                const text = (
                <div>
                    {data.name} <br />
                    <hr /*width="100%" size="2" color="black" noshade*/></hr>
                {data.attributes.map((attr, index) => (
                    <React.Fragment key={index}>
                        {attr.visibility} {attr.name}: {attr.type.name} <br />
                        </React.Fragment>
                ))}
                <hr /*width="100%" size="2" color="black" noshade*/></hr>
                {data.methods.map((method, index) => (
                    <React.Fragment key={index}>
                        {method.visibility} {method.name}({method.parameters.map(param => `${param.name}: ${param.type.name}`).reduce((prev, curr) => `${prev}, ${curr}`, "").slice(2)}): {method.returnType.name} <br />
                        </React.Fragment>
                ))}
                </div>
            )//@ts-ignore
                nodes.push(new UMLNode(data.uuid, text));
            } else if (type === 'Package') {
                nodes.push(new UMLNode(data.uuid, data.name));
            }

        });

        diagram.connections.forEach(link => {
            const fromNode = nodes.find(node => node.getClassID() === link.sourceNodeUUID);
            const toNode = nodes.find(node => node.getClassID() === link.targetNodeUUID);
            if (fromNode !== undefined && toNode !== undefined) {
                links.push(new UMLRelation("advanced", link.uuid, fromNode, toNode));
            }
        });

        return new UMLDiagram(nodes, links);
    }
}
