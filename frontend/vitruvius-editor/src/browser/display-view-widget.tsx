import * as React from 'react';
import { injectable, postConstruct, inject } from '@theia/core/shared/inversify';
import { ReactWidget } from '@theia/core/lib/browser/widgets/react-widget';
import { MessageService } from '@theia/core';
import { Connection } from '../model/Connection';
import {DisplayView} from '../model/DisplayView';
import {DisplayViewService} from '../backend-communication/DisplayViewService';
import {DisplayViewResolver} from '../visualisation/DisplayViewResolver';

@injectable()
export class DisplayViewWidget extends ReactWidget {

    static readonly ID = 'widget:display-views';
    static readonly LABEL = 'Vitruvius Views';

    @inject(MessageService)
    protected readonly messageService!: MessageService;

	@inject(DisplayViewService)
	protected readonly displayViewService: DisplayViewService;

	@inject(DisplayViewResolver)
	protected readonly displayViewResolver: DisplayViewResolver;

	private connection: Connection | null;
	private widgetItems: WidgetItem[];

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
		this.widgetItems = [];
        this.update();
    }

    render(): React.ReactElement {
		if (this.connection == null) {
			return <div>
				<p>Currently no Vitruvius project is loaded.</p>
				</div>
		} else {
			return <div>
				<p>The following views are avaliable for the loaded project:</p>
				<div>
					<ul>
						{this.widgetItems.map(widgetItem => {
							return <div>
								<li onClick={() => this.widgetItemClickHandler(widgetItem)}>{widgetItem.displayView.name}</li>
								{this.Windows(widgetItem)}
							</div>
						} )
						}	
					</ul>
				</div>
			</div>
		}
    }

	Windows(widgetItem: WidgetItem): React.ReactElement {
		let windowsNonNull = widgetItem.windows ? widgetItem.windows : [];
		if (windowsNonNull.length != 0) {
			return <div>
				<ul>
					{windowsNonNull.map(window => <div><li onClick={() => this.windowClickHandler(widgetItem, window)}>{window}</li></div>)}
				</ul>
			</div>
		} else {
			return <div></div>;
		}
	}

	async loadProject(connection: Connection | null) {
		if(connection == null) {
			this.connection = null;
			this.widgetItems = [];
			this.update();
		} else {
			this.displayViewService.getDisplayViews(connection.uuid).then(displayViews => {
				this.connection = connection;
				this.widgetItems = displayViews.map(displayView => {
					return {displayView, windows: null};
				});
			}).catch(_err => {
				this.messageService.error("Couldn't connect to the given Vitruvius server.");
				this.connection = null;
				this.widgetItems = [];
			}).finally(() => this.update())
		}
	}

	getConnection(): Connection | null {
		return this.connection;
	}

	private async widgetItemClickHandler(widgetItem: WidgetItem) {
		if(widgetItem.windows == null) {
			this.displayViewService.getDisplayViewWindows((this.connection as Connection).uuid, widgetItem.displayView.name).then(windows => {
				widgetItem.windows = windows ? windows : [];
				this.update();
			});
		} else {
			widgetItem.windows = null;
			this.update();
		}
	}

	private async windowClickHandler(widgetItem: WidgetItem, window: string) {
		this.displayViewService.getDisplayViewContent((this.connection as Connection).uuid, widgetItem.displayView.name, {windows: [window]})
			.then(content => {
				this.displayViewResolver.getWidget(widgetItem.displayView, content ?? "")?.then(widget => widget.show());
			})
	}
}

type WidgetItem = {displayView: DisplayView, windows: string[] | null};
