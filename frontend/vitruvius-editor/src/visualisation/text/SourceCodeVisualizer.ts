import { Visualizer } from "../Visualizer";
import { Widget } from "@theia/core/lib/browser";
import {inject, injectable} from "@theia/core/shared/inversify";
import { EditorManager } from "@theia/editor/lib/browser"
import { URI } from "@theia/core";

@injectable()
export class SourceCodeVisualizer implements Visualizer {
	@inject(EditorManager)
	protected readonly editorManager: EditorManager;
	visualizeContent(content: string): Promise<Widget> {
	  let uri = new URI('data:text/plain;charset=utf-8,' + encodeURIComponent(content));
	  return this.editorManager.getOrCreateByUri(uri);
	}
  
}
