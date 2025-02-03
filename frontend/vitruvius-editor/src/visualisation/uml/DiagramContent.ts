/**
 * A class representing a UML Package.
 */
class UMLPackage {
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
 * A class representing a UML Link between two Packages.
 */
class UMLPackageLink {
    fromID: string;
    toID: string;

    constructor(from: string, to: string) {
        this.fromID = from;
        this.toID = to;
    }
}

/**
 * A class representing a Full UML Package Diagram
 */
class UMLPackageDiagram {
    packages :UMLPackage[];
    links :UMLPackageLink[];

    getPackages(): UMLPackage[] {
        return this.packages;
    }

    getLinks(): UMLPackageLink[] {
        return [new UMLPackageLink('a1', 'b2')];
    }
}