import {UMLNode, UMLRelation} from "./DiagramComponents";
import {DefaultLinkModel} from "@projectstorm/react-diagrams-defaults";

/**
 * A class to represent a UML Diagram.
 */
export class UMLDiagram {
    nodes :UMLNode[];
    links :DefaultLinkModel[];

    constructor(nodes :UMLNode[], links :UMLRelation[]) {
        this.nodes = nodes;
        this.links = links;
    }

    getNodes(): UMLNode[] {
        return this.nodes;
    }

    getLinks(): UMLRelation[] {
        return this.links;
    }
}