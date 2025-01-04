# Vitruvius-Editor
The empty base Framework of Theia with a (currently) empty Hello-World plugin and Sprotty.

## Getting started

Please install all necessary [prerequisites](https://github.com/eclipse-theia/theia/blob/master/doc/Developing.md#prerequisites).

In Short:
node version ^18, for easy node version managment use NVM
some errors require python to be downgraded to python 3.10 if python 3.11 was previously installed

## Running the browser example

    yarn install
    yarn build:browser
    yarn start:browser

*or:*

    yarn build:browser
    cd browser-app
    yarn start

*or:* launch `Start Browser Backend` configuration from VS code.

Open http://localhost:3000 in the browser.

## Troubleshooting

If port 3000 shows a blank webpage or an infinite Theia loading screen, try running npm install and then the steps for running the Browser example as explained above again.

## Developing with the browser example

Start watching all packages, including `browser-app`, of your application with

    yarn watch:browser

*or* watch only specific packages with

    cd hello-world
    yarn watch

and the browser example.

    cd browser-app
    yarn watch

Run the example as [described above](#Running-the-browser-example)

## Publishing

Create a npm user and login to the npm registry, [more on npm publishing](https://docs.npmjs.com/getting-started/publishing-npm-packages).

    npm login

Publish packages with lerna to update versions properly across local packages, [more on publishing with lerna](https://github.com/lerna/lerna#publish).

    npx lerna publish
