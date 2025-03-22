# Vitruvius Editor

Vitruvius Editor is a web-based editor designed for developers to create and manage views of a remote project in the Vitruvius framework. It provides a modern and intuitive interface for writing, editing, and managing text-based and UML-based views. This repo contains both the frontend and the backend of the project. See our Github Wiki for more information on how to use this editor.

## Build Instructions

### Using Docker Compose

To build and run the Vitruvius Editor using Docker Compose, follow these steps:

1. Clone the repository:

   ```
   git clone https://github.com/Vitruvius-Editor/vitruvius-editor.git
   ```

2. Navigate to the project directory:

   ```
   cd vitruvius-editor
   ```

3. Build and run the Docker Compose setup:

   ```
   docker-compose up -d
   ```

   This will build the Docker image for the Vitruvius Editor and start the container. The editor will be exposed on port 3000 of your local machine.

   > **Note for Production**: When running the Vitruvius Editor in a production environment, you should update the database credentials in the `docker-compose.yml` file to use your own secure credentials.

4. Open your web browser and navigate to `http://localhost:3000` to access the Vitruvius Editor.

### Manual Build

If you prefer not to use Docker, you can build and run the Vitruvius Editor manually. The Vitruvius Editor is composed of a frontend and a backend component, each with their own build instructions.

1. Clone the repository:

   ```
   git clone https://github.com/Vitruvius-Editor/vitruvius-editor.git
   ```

2. Navigate to the frontend and backend directories:

   ```
   cd vitruvius-editor/frontend
   ```

   ```
   cd vitruvius-editor/backend
   ```

3. Follow the build instructions provided in the respective `README.md` files for the frontend and backend components.

   - [Frontend Build Instructions](./frontend/README.md)
   - [Backend Build Instructions](./backend/README.md)

4. Once you have built both the frontend and backend components, you can start the Vitruvius Editor server and access it at `http://localhost:3000`.

## Demo Project

To run the demo project, you can use the `compose-demo.yml` file. This file contains the necessary configuration to set up and run the demo with an example Vitruvius server using Docker Compose.

### Running the Demo

To run the demo, execute the following command:

```sh
sudo docker-compose -f compose-demo.yml up
```

**Notice:** Root privileges are required to run the demo compose because it uses Docker's host networking feature.

### Loading the demo project

After starting the containers navigate to [http://localhost:3000](http://localhost:3000) and select the import option in the Vitruvius menu. Select any name and description and set the hostname to **localhost** and the port to **8000**.

## License

Vitruvius Editor is licensed under the [Eclipse Public License 2.0](LICENSE).
