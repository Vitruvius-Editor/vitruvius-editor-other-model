import {ReactWidget} from '@theia/core/lib/browser';
import {inject, injectable, postConstruct} from '@theia/core/shared/inversify';
import * as React from 'react';
import { MessageService } from '@theia/core';


@injectable()
export class TextWidget extends ReactWidget {
   static readonly ID = 'textwidget:textwidget';
    static readonly LABEL = 'TextWidget';
	private content = 'penis';

    @inject(MessageService)
    protected readonly messageService!: MessageService;

    @postConstruct()
    protected init(): void {
        this.doInit()
    }

    protected async doInit(): Promise <void> {
        this.id = TextWidget.ID;
        this.title.label = TextWidget.LABEL;
        this.title.caption = TextWidget.LABEL;
        this.title.closable = true;
        this.title.iconClass = 'fa fa-window-maximize'; // example widget icon.
        this.update();
    }

    render(): React.ReactElement {
    	return <div>
			<textarea defaultValue={this.content}></textarea>
		</div>
	}

    
	updateContent(content: string): void {
		this.content = content;
		this.update();
	}

    setLabel(label: string): void {
        this.title.label = label;
        this.title.caption = label;
        this.update();
    }
}
