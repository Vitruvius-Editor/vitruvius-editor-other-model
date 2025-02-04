import {DefaultLinkFactory, DefaultLinkModel, DefaultNodeModel, DefaultPortModel } from '@projectstorm/react-diagrams-defaults';
import React from 'react';
// import {PortModelAlignment} from "@projectstorm/react-diagrams";
import { DiagramEngine, LinkWidget, PointModel } from '@projectstorm/react-diagrams-core';
import { DefaultLinkPointWidget, DefaultLinkSegmentWidget } from '@projectstorm/react-diagrams-defaults';

/**
 * The UMLNode class represents a UML class node in the React diagram.
 */
export class UMLNode extends DefaultNodeModel {
  classID: string

  constructor(classID :string, className: string, attributes: string[], methods: string[]) {
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
    this.classID = classID;
    // this.addPort(new AdvancedPortModel(true, 'a'));
  }

  getClassID() {
    return this.classID;
  }

  getPort() :DefaultPortModel {
    return this.ports[0];
  }
}

/**
 * The UMLArrowLink class represents a UML arrow link in the React diagram.
 */
export class UMLArrowLink extends DefaultLinkModel {
  constructor(From : UMLNode, To : UMLNode) {
    super({
      type: 'advanced',
      curvyness: 0,
      color: 'black',
      selectedColor: 'black'
    });

    const fromPort:DefaultPortModel = From.addPort(new AdvancedPortModel(false, 'out'));
    const toPort:DefaultPortModel = To.addPort(new AdvancedPortModel(true, 'in'));
    fromPort.link(toPort, new AdvancedLinkFactory());

    this.setLocked(true);
    this.setSourcePort(fromPort);
    this.setTargetPort(toPort);
    this.addLabel('Imports');
  }
}

export class AdvancedLinkModel extends DefaultLinkModel {
  constructor() {
    super({
      type: 'advanced',
      width: 4
    });
  }
}

export class AdvancedPortModel extends DefaultPortModel {
  createLinkModel(): AdvancedLinkModel | null {
    return new AdvancedLinkModel();
  }
}

const CustomLinkArrowWidget = (props) => {
  const { point, previousPoint } = props;

  const angle =
      90 +
      (Math.atan2(
              point.getPosition().y - previousPoint.getPosition().y,
              point.getPosition().x - previousPoint.getPosition().x
          ) *
          180) /
      Math.PI;

  //translate(50, -10),
  return (
      <g className="arrow" transform={'translate(' + point.getPosition().x + ', ' + point.getPosition().y + ')'}>
        <g style={{ transform: 'rotate(' + angle + 'deg)' }}>
          <g transform={'translate(0, -3)'}>
            <polygon
                points="0,10 8,30 -8,30"
                fill={props.color}
                data-id={point.getID()}
                data-linkid={point.getLink().getID()}
            />
          </g>
        </g>
      </g>
  );
};

export interface AdvancedLinkWidgetProps {
  link: DefaultLinkModel;
  diagramEngine: DiagramEngine;
  pointAdded?: (point: PointModel, event: MouseEvent) => any;
  renderPoints?: boolean;
  selected?: (event: MouseEvent) => any;
}

export class AdvancedLinkWidget extends React.Component<AdvancedLinkWidgetProps> {
  generatePoint = (point: PointModel): JSX.Element => {
    return (
        <DefaultLinkPointWidget
            key={point.getID()}
            point={point as any}
            colorSelected={this.props.link.getOptions().selectedColor ?? ''}
            color={this.props.link.getOptions().color}
        />
    );
  };

  generateLink = (path: string, extraProps: any, id: string | number): JSX.Element => {
    return (
        <DefaultLinkSegmentWidget
            key={`link-${id}`}
            path={path}
            diagramEngine={this.props.diagramEngine}
            factory={this.props.diagramEngine.getFactoryForLink(this.props.link)}
            link={this.props.link}
            extras={extraProps}
        />
    );
  };

  addPointToLink = (event: MouseEvent, index: number) => {
    if (
        !event.shiftKey &&
        !this.props.link.isLocked() &&
        this.props.link.getPoints().length - 1 <= this.props.diagramEngine.getMaxNumberPointsPerLink()
    ) {
      const position = this.props.diagramEngine.getRelativeMousePoint(event);
      const point = this.props.link.point(position.x, position.y, index);
      event.persist();
      event.stopPropagation();
      this.props.diagramEngine.getActionEventBus().fireAction({
        event,
        model: point
      });
    }
  };

  generateArrow(point: PointModel, previousPoint: PointModel): JSX.Element {
    return (
        <CustomLinkArrowWidget
            key={point.getID()}
            point={point as any}
            previousPoint={previousPoint as any}
            colorSelected={this.props.link.getOptions().selectedColor}
            color={this.props.link.getOptions().color}
        />
    );
  }

  render() {
    //ensure id is present for all points on the path
    var points = this.props.link.getPoints();
    var paths = [];

    //draw the multiple anchors and complex line instead
    for (let j = 0; j < points.length - 1; j++) {
      paths.push(
          this.generateLink(
              LinkWidget.generateLinePath(points[j], points[j + 1]),
              {
                'data-linkid': this.props.link.getID(),
                'data-point': j,
                onMouseDown: (event: MouseEvent) => {
                  this.addPointToLink(event, j + 1);
                }
              },
              j
          )
      );
    }

    //render the circles
    for (let i = 1; i < points.length - 1; i++) {
      paths.push(this.generatePoint(points[i]));
    }

    if (this.props.link.getTargetPort() !== null) {
      paths.push(this.generateArrow(points[points.length - 1], points[points.length - 2]));
    } else {
      paths.push(this.generatePoint(points[points.length - 1]));
    }

    return <g data-default-link-test={this.props.link.getOptions().testName}>{paths}</g>;
  }
}

export class AdvancedLinkFactory extends DefaultLinkFactory {
  constructor() {
    super('advanced');
  }

  generateModel(): AdvancedLinkModel {
    return new AdvancedLinkModel();
  }

  generateReactWidget(event): JSX.Element {
    return <AdvancedLinkWidget link={event.model} diagramEngine={this.engine} />;
  }
}