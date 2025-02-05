import {UMLNode, UMLRelation} from "./DiagramComponents";

/**
 * A class to represent a UML Diagram.
 */
export class UMLDiagram {
    nodes :UMLNode[];
    links :UMLRelation[];

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
