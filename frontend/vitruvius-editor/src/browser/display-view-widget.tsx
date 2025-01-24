import * as React from 'react';
import { injectable, postConstruct, inject } from '@theia/core/shared/inversify';
import { ReactWidget } from '@theia/core/lib/browser/widgets/react-widget';
import { MessageService } from '@theia/core';
import { Connection } from '../model/Connection';
import {DisplayView} from '../model/DisplayView';
import {DisplayViewService} from '../backend-communication/DisplayViewService';

@injectable()
export class DisplayViewWidget extends ReactWidget {

    static readonly ID = 'widget:display-views';
    static readonly LABEL = 'Vitruvius Views';

    @inject(MessageService)
    protected readonly messageService!: MessageService;

	@inject(DisplayViewService)
	protected readonly displayViewService: DisplayViewService;

	private connection: Connection | null;
	private displayViews: DisplayView[];

    @postConstruct()
    protected init(): void {
        this.doInit()
    }

    protected async doInit(): Promise <void> {
        this.id = DisplayViewWidget.ID;
        this.title.label = DisplayViewWidget.LABEL;
        this.title.caption = DisplayViewWidget.LABEL;
        this.title.closable = true;
        this.title.iconClass = 'fa fa-window-maximize'; // example widget icon.
		this.connection = null;
		this.displayViews = [];
        this.update();
    }

    render(): React.ReactElement {
		if (this.connection == null) {
			return <div>
				<p>Currently not Vitruvius project is loaded.</p>
				</div>
		} else {
			return <div>
				<p>The following views are avaliable for the loaded project:</p>
				<div>
					{this.displayViews.map(displayView => <button>{displayView.name}</button> )}	
			</div>
			</div>
		}
    }

	async loadProject(connection: Connection | null) {
		if(connection == null) {
			this.connection = null;
			this.displayViews = [];
		} else {
			this.displayViewService.getDisplayViews(connection.uuid).then(displayViews => {
				this.connection = connection;
				this.displayViews = displayViews;
			}).catch(_err => {
				this.messageService.error("Couldn't connect to the given Vitruvius server.");
				this.connection = null;
				this.displayViews = [];
			})
		}
		this.update();
	}

	getConnection(): Connection | null {
		return this.connection;
	}
}
