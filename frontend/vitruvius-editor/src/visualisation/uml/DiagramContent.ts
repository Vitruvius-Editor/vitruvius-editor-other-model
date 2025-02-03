import {UMLArrowLink, UMLNode} from "./DiagramComponents";

/**
 * A class representing a Full UML Diagram
 */
export class UMLDiagram {
    nodes :UMLNode[];
    links :UMLArrowLink[];

    className1 = "Class Name1";
    attributes1 = ["+attribute11: type", "-attribute12: type", "adsfsfsdfsdfsdf"];
    methods1 = ["+method11: void", "-method12: type"];
    umlNode1:UMLNode = new UMLNode( 'a1', this.className1, this.attributes1, this.methods1 );

    className2 :string = "Class Name2";
    attributes2 :string[] = ["+attribute21: type", "-attribute22: type"];
    methods2 :string[] = ["+method21: void", "-method22: type"];
    umlNode2:UMLNode = new UMLNode( 'b2', this.className2, this.attributes2, this.methods2 );

    constructor() {
        this.nodes = [this.umlNode1, this.umlNode2];
        this.links = [new UMLArrowLink(this.umlNode1, this.umlNode2)];
    }

    getNodes(): UMLNode[] {
        return this.nodes;
    }

    getLinks(): UMLArrowLink[] {
        return this.links;
    }
}