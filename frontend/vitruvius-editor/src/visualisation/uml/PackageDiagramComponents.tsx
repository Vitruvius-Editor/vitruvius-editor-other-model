import { DefaultNodeModel } from '@projectstorm/react-diagrams-defaults';
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
    })
  }
}