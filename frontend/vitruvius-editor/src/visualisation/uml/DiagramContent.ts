/**
 * A class representing a UML Component.
 */
class UMLComponent {
    classID: string;
    name: string;
    attributes: string[];
    methods: string[];

    constructor(classId: string, className: string, attributes: string[], methods: string[]) {
        this.classID = classId;
        this.name = className;
        this.attributes = attributes;
        this.methods = methods;
    }
}

/**
 * A class representing a UML Relationship between two Components.
 */
class UMLRelation {
    name: string;
    fromID: string;
    toID: string;

    constructor(from: string, to: string) {
        this.fromID = from;
        this.toID = to;
    }
}

/**
 * A class representing a Full UML Diagram
 */
class UMLDiagram {
    packages :UMLComponent[];
    links :UMLRelation[];

    className1 = "Class Name1";
    attributes1 = ["+attribute11: type", "-attribute12: type", "adsfsfsdfsdfsdf"];
    methods1 = ["+method11: void", "-method12: type"];
    umlPackage1:UMLComponent = new UMLComponent( 'a1', this.className1, this.attributes1, this.methods1 );

    className2 :string = "Class Name2";
    attributes2 :string[] = ["+attribute21: type", "-attribute22: type"];
    methods2 :string[] = ["+method21: void", "-method22: type"];
    umlPackage2:UMLComponent = new UMLComponent( 'b2', this.className2, this.attributes2, this.methods2 );

    constructor() {
        this.packages = [this.umlPackage1, this.umlPackage2];
    }

    getComponents(): UMLComponent[] {
        return this.packages;
    }

    getLinks(): UMLRelation[] {
        return [new UMLRelation('a1', 'b2')];
    }
}