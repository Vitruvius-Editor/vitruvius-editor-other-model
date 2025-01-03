# my-theia
The base Theia framework to build the Theia-based applications with the vitruvius plugin and sprotty renderer plugin.

## Getting started

Please install all necessary [prerequisites](https://github.com/eclipse-theia/theia/blob/master/doc/Developing.md#prerequisites).

Other important things to try:

Use Node Version 18 (worked the best for me). 

For easy Node Version Management Install [NVM](https://www.freecodecamp.org/news/node-version-manager-nvm-install-guide/)

    nvm install 18
    nvm use 18

## Running the browser example

    yarn build:browser
    yarn start:browser


Open http://localhost:3000 in the browser.



## Developing with the browser example

Start watching all packages, including `browser-app`, of your application with

    yarn watch:browser

*or* watch only specific packages with

    cd hello-world
    yarn watch

and the browser example.

    cd browser-app
    yarn watch