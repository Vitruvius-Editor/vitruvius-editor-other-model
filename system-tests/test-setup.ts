import Docker from 'dockerode';
const docker = new Docker();
jest.setTimeout(120000);

beforeAll(async () => {
    await pullImages();
})

beforeEach(async () => {
    await startContainers();
    await waitForHealth("http://localhost:8080/actuator/health");
    console.log("Backend is helathy.")
})

afterEach(async () => {
    await stopContainers();
})

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

async function startContainers() {
    const backendContainer = await docker.createContainer({
        Image: 'ghcr.io/vitruvius-editor/vitruvius-editor-backend:latest',
        name: 'vitruvius-editor-backend-test',
        Tty: false,
        ExposedPorts: {
            '8080/tcp': {}
        },
        HostConfig: {
            PortBindings: {
                '8080/tcp': [
                    {
                        HostPort: '8080' // Bind to port 8080 on the host
                    }
                ]
            }
        },
        Env: [
            'HOST=docker.host.internal' // Set the host environment variable
        ]
        });
    await backendContainer.start();
    console.log('Backend container started successfully:', backendContainer.id);

    const frontendContainer = await docker.createContainer({
        Image: 'ghcr.io/vitruvius-editor/vitruvius-editor-frontend:latest',
        name: 'vitruvius-editor-frontend-test',
        Tty: true,
        ExposedPorts: {
            '3000/tcp': {}
        },
        HostConfig: {
            PortBindings: {
                '3000/tcp': [
                    {
                        HostPort: '3000' // Bind to port 3000 on the host
                    }
                ]
            }
        }
    });

    await frontendContainer.start();
    console.log('Frontend container started successfully:', frontendContainer.id);

    const serverContainer = await docker.createContainer({
        Image: 'ghcr.io/vitruvius-editor/vitruvius-server:latest',
        name: 'vitruvius-server-test',
        Tty: true,
        ExposedPorts: {
            '8000/tcp': {}
        },
        HostConfig: {
            PortBindings: {
                '8000/tcp': [
                    {
                        HostPort: '8000' // Bind to port 8000 on the host
                    }
                ]
            }
        }
    });

    await serverContainer.start();
    console.log('Server container started successfully:', serverContainer.id);
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

async function waitForHealth(url: string, timeout: number = 30000, interval: number = 5000) {
    const startTime = Date.now();

    while (Date.now() - startTime < timeout) {
        try {
            const response = await fetch(url);
            if (response.ok) {
                const data = await response.json();
                if (data.status === 'UP') {
                    console.log('Backend is healthy!');
                    return;
                }
            }
        } catch (error) {
            // Ignore errors and continue polling
            console.log('Waiting for backend to be healthy...');
        }

        await new Promise(resolve => setTimeout(resolve, interval));
    }

    throw new Error(`Backend did not become healthy within ${timeout / 1000} seconds.`);
}
