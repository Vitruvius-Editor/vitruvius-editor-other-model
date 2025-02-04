import {UMLArrowLink, UMLNode} from "./DiagramComponents";

/**
 * A class representing a Full UML Diagram
 */
export class UMLDiagram {
    nodes :UMLNode[];
    links :UMLArrowLink[];

    constructor(nodes :UMLNode[], links :UMLArrowLink[]) {
        this.nodes = nodes;
        this.links = links;
    }

    getNodes(): UMLNode[] {
        return this.nodes;
    }

    getLinks(): UMLArrowLink[] {
        return this.links;
    }
}