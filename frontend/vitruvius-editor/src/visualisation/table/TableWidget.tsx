import {
  injectable,
  postConstruct,
} from "@theia/core/shared/inversify";
import * as React from "react";
import { VisualisationWidget } from "../VisualisationWidget";

/**
* A Widget to visualize a table based Vitruvius view.
*/
@injectable()
export class TableWidget extends VisualisationWidget<Table> {
    getVisualizerName(): string {
        return "TextVisualizer";
    }
	static readonly ID = "tablewidget:tablewidget";
	static readonly LABEL = "TableWidget";

	/**
	* Initializes the widget with the default id, label and initial content.
	*/
	@postConstruct()
	protected init(): void {
		this.doInit(TableWidget.ID, TableWidget.LABEL, {rows: [], columns: []});
	}

	/**
	* Renders the widget containing a text area to edit the content.
	*/
	render(): React.ReactElement {
	return (
	  <div className="editor-container table-widget">
      <table>
        <thead>
          <tr>
            {this.content.columns.filter(column => column.shouldBeDisplayed).map(column => (
              <th>{column.displayName}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {this.content.rows.map(row => (
            <tr>
              {this.content.columns.filter(column => column.shouldBeDisplayed).map(column => {
				  if (column.editable) {
					  return(<td><input className={'hidden-input'} defaultValue={row[column.fieldName as keyof RowEntry].toString()} onChange={(event) => this.handleChange(event, row.uuid, column.fieldName)}></input></td>)
				  } else {
					  return(<td>{row[column.fieldName as keyof RowEntry].toString()}</td>)
				  }
			  })}
            </tr>
          ))}
        </tbody>
      </table>
	  </div>
	);
	}

    getContentString(): string {
        return JSON.stringify(this.content)
    }

	handleChange(event: React.ChangeEvent<HTMLInputElement>, uuid: string, propertyName: string): void {
		(this.content.rows.find(row => row.uuid === uuid)as any)[propertyName]=event.target.value;
	}
}

