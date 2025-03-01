import Docker from 'dockerode';
const docker = new Docker();

export default async () => {
    stopContainers();
}

async function stopContainers() {
    const containerNames = [
        'vitruvius-editor-backend-test',
        'vitruvius-editor-frontend-test',
        'vitruvius-server-test'
    ];

    for (const name of containerNames) {
        try {
            const container = docker.getContainer(name);
            await container.stop();
            console.log(`Container ${name} stopped successfully.`);

            await container.remove();
            console.log(`Container ${name} removed successfully.`);
        } catch (error) {
            console.error(`Error stopping or removing container ${name}:`, error);
        }
    }
}
