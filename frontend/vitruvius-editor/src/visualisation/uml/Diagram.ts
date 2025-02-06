export type Diagram = { 
    nodes: DiagramNode[],
    connections: DiagramConnection[],
};

export type DiagramNode = {
    uuid: string,
    name: string,
    attributes: {
        uuid: string,
        visibility: "PUBLIC" | "PRIVATE" | "PROTECTED",
        name: string,
        type: {
            uuid: string,
            name: string,
        }
    }[],
    methods: {
        uuid: string,
        visibility: "PUBLIC" | "PRIVATE" | "PROTECTED",
        name: string,
        parameters: {
            uuid: string,
            name: string,
            type: {
                uuid: string,
                name: string
            }
        }[],
        returnType: {
            uuid: string,
            name: string,
        }
    }[],
    viewRecommendations: {
        displayViewName: string,
        windowName: string,
    }[],
};

export type DiagramConnection = {
    uuid: string,
    sourceNodeUUID: string,
    targetNodeUUID: string,
    connectionType: string,
    sourceMultiplicity: string,
    targetMultiplicity: string,
    connectionName: string,
};
