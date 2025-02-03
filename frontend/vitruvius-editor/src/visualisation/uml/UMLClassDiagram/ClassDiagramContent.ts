/**
 * A class representing a UML Class.
 */
class UMLClass {
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
 * A class representing a UML Link between two classes.
 */
class UMLClassLink {
    name: string;
    fromID: string;
    toID: string;

    constructor(name: string, fromID: string, toID: string) {
        this.name = name;
        this.fromID = from;
        this.toID = to;
    }
}

/**
 * A class representing a Full UML Package Diagram
 */