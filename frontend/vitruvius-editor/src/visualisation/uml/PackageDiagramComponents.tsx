import { DefaultLinkModel, DefaultNodeModel } from '@projectstorm/react-diagrams-defaults';
import React from 'react';

export class PackageNode extends DefaultNodeModel {
  constructor(className: string, attributes: string[], methods: string[]) {
    super({
      name: (
        <>
          {className} <br />
          <hr width="100%" size="2" color="black" noshade></hr>
          {attributes.map((attr, index) => (
            <React.Fragment key={index}>
              {attr} <br />
            </React.Fragment>
          ))}

          <hr width="100%" size="2" color="black" noshade></hr>
          {methods.map((method, index) => (
            <React.Fragment key={index}>
              {method} <br />
            </React.Fragment>
          ))}
        </>
      ),
      color: 'rgb(145, 145, 145)'
    });
  }
}

export class PackageImportLink extends DefaultLinkModel {
  constructor(From : PackageNode, To : PackageNode) {
    super({
      type: 'default'
    });
    
    const fromPort = From.addOutPort('OUT');
    const toPort = To.addInPort('IN');
    const link = fromPort.link<DefaultLinkModel>(toPort);

    this.setLocked(true);
    this.getOptions().curvyness = 0;
    this.getOptions().color = 'black';
    this.getOptions().selectedColor = 'black';

    this.addLabel('Imports');
    this.setSourcePort(fromPort);
    this.setTargetPort(toPort);
  }
}