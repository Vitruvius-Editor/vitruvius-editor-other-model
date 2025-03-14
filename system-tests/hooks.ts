import { spawn } from "child_process";

export const beforeEach = async (): Promise<void> => {
  await runSudoCommand(
    "docker-compose -f compose-tests.yml up -d --force-recreate",
  );
  await waitForHealth("http://localhost:8080/actuator/health");
};

export const afterEach = async (): Promise<void> => {
  await runSudoCommand("docker-compose -f compose-tests.yml down -t 0");
};

async function runSudoCommand(command: string): Promise<void> {
  return new Promise((resolve, reject) => {
    const child = spawn("sudo", command.split(" "), { stdio: "pipe" });
    child.on("error", (err) => {
      reject(new Error(`Failed to start subprocess: ${err.message}`));
    });
    child.on("exit", (code) => {
      if (code !== 0) {
        reject(new Error(`Command failed with exit code ${code}`));
      } else {
        resolve();
      }
    });
  });
}

async function waitForHealth(
  url: string,
  timeout: number = 30000,
  interval: number = 5000,
): Promise<void> {
  const startTime = Date.now();

  while (Date.now() - startTime < timeout) {
    try {
      const response = await fetch(url);
      if (response.ok) {
        const data = await response.json();
        if (data.status === "UP") {
          console.log("Backend is healthy!");
          return;
        }
      }
    } catch (error) {
      console.log("Waiting for backend to be healthy...");
    }

    await new Promise((resolve) => setTimeout(resolve, interval));
  }

  throw new Error(
    `Backend did not become healthy within ${timeout / 1000} seconds.`,
  );
}
