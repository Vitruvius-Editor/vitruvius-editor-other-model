# Vitruvius System Tests README

This folder contains the system tests for the Vitruvius Editor written with playwright.

## Prerequisites

Before you begin, ensure you have the following installed:

- [Node.js](https://nodejs.org/) (version 12 or later)
- [Yarn](https://yarnpkg.com/) (version 1.22 or later)
- [Playwright](https://playwright.dev/) (installed as a dependency in your project)
- [Docker](https://www.docker.com/) (latest version)
- [Docker Compose](https://docs.docker.com/compose/) (latest version)

## Installation

1. **Clone the repository** (if you haven't already):

   ```bash
   git clone https://github.com/Vitruvius-Editor/vitruvius-editor.git
   cd Vitruvius-Editor/system-tests
   ```

2. **Install dependencies**:

   Run the following command to install the necessary dependencies, including Playwright:

   ```bash
   yarn install
   ```

3. **Install Playwright browsers**:

   After installing Playwright, you need to install the required browsers. Run:

   ```bash
   npx playwright install
   ```

## Running System Tests

At first make sure that you are using the latest docker images of the editor for testing:

```bash
sudo docker-compose -f compose-tests.yml pull
```

To run the system tests, use the following command:

```bash
yarn playwright test
```

This command will execute the tests defined in your Playwright configuration.

### Running Specific Tests

If you want to run a specific test file, you can do so by specifying the path:

```bash
yarn playwright test path/to/your/test.spec.ts
```

### Running Tests in Headless Mode

By default, Playwright runs tests in headless mode. If you want to run tests in a visible browser window, you can set the `HEADLESS` environment variable to `false`:

```bash
HEADLESS=false yarn playwright test
```

## Additional Commands

- **Run tests with a specific browser**:

  You can specify which browser to use by setting the `BROWSER` environment variable:

  ```bash
  BROWSER=firefox yarn playwright test
  ```

  Supported browsers: `chromium`, `firefox`, `webkit`.

- **Run tests with a specific configuration**:

  If you have multiple configurations, you can specify which one to use:

  ```bash
  yarn playwright test --config=path/to/your/config.js
  ```

## Conclusion

You are now set up to run system tests using Playwright and Yarn. For more information on writing tests and using Playwright features, refer to the [Playwright documentation](https://playwright.dev/docs/intro).

If you encounter any issues, please check the project's issue tracker or reach out for help. Happy testing!
