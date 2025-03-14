import { Container } from "@theia/core/shared/inversify";
import { MessageService } from "@theia/core";
import { TextWidget } from "./TextWidget";
import { render, fireEvent } from "@testing-library/react";
import "@testing-library/jest-dom";
import { VisualisationWidgetRegistry } from "../VisualisationWidgetRegistry";

describe("TextWidget", () => {
  let container: Container;
  let widget: TextWidget;
  let messageService: jest.Mocked<MessageService>;

  beforeEach(() => {
    container = new Container();
    messageService = {
      info: jest.fn(),
      error: jest.fn(),
    } as unknown as jest.Mocked<MessageService>;
    container.bind(MessageService).toConstantValue(messageService);
    container
      .bind(VisualisationWidgetRegistry)
      .toConstantValue({} as VisualisationWidgetRegistry);
    container.bind(TextWidget).toSelf();
    widget = container.get(TextWidget);
    (widget as any).init();
  });

  it("should initialize with default id, label, and initial content", () => {
    expect(widget.id).toBe(TextWidget.ID);
    expect(widget.getLabel()).toBe(TextWidget.LABEL);
    expect(widget.getContent()).toBe("/*Initial Content*/");
  });

  it("should render a text area with initial content", () => {
    const { getByText } = render(widget.render());
    expect(getByText("/*Initial Content*/")).toBeInTheDocument();
  });

  it("should update content on text area change", () => {
    const { getByDisplayValue } = render(widget.render());
    const textarea = getByDisplayValue(
      "/*Initial Content*/",
    ) as HTMLTextAreaElement;
    fireEvent.change(textarea, { target: { value: "Updated Content" } });
    expect(widget.getContent()).toBe("Updated Content");
  });
});
