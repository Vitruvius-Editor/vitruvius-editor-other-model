import Docker from 'dockerode';
const docker = new Docker();

export default async () => {
    pullImages();
}

async function pullImages() {
    // Define the images
    const images = [
        'ghcr.io/vitruvius-editor/vitruvius-editor-backend:latest',
        'ghcr.io/vitruvius-editor/vitruvius-editor-frontend:latest',
        'ghcr.io/vitruvius-editor/vitruvius-server:latest'
    ];

    // Pull images if not already present
    for (const image of images) {
        try {
            await pullImage(image);
            console.log(`Image ${image} pulled successfully.`);
        } catch (error) {
            console.error(`Error pulling image ${image}:`, error);
        }
    }
}

async function pullImage(image: string) {
    return new Promise<void>((resolve, reject) => {
        docker.pull(image, (err: any, stream: NodeJS.ReadableStream) => {
            if (err) {
                return reject(err);
            }
            // Stream the progress of the pull
            docker.modem.followProgress(stream, (err) => {
                if (err) {
                    return reject(err);
                }
                resolve();
            });
        });
    });
}


