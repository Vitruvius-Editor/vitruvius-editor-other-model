import { MenuModelRegistry } from "@theia/core/lib/common";
import { Container } from "@theia/core/shared/inversify";
import {
  VitruviusMenuContribution,
  VitruviusMenu,
} from "./vitruviusMenuContribution";
import { HelpCommand } from "./menuCommands/helpCommand";
import { LoadProjectCommand } from "./menuCommands/loadProjectCommand";
import { ImportProjectCommand } from "./menuCommands/importProjectCommand";
import { RefreshProjectCommand } from "./menuCommands/refreshProjectCommand";
import { DeleteProjectCommand } from "./menuCommands/deleteProjectCommand";
import { EditProjectCommand } from "./menuCommands/editProjectCommand";

describe("VitruviusMenuContribution", () => {
  let container: Container;
  let contribution: VitruviusMenuContribution;
  let menuRegistry: jest.Mocked<MenuModelRegistry>;

  beforeEach(() => {
    container = new Container();
    menuRegistry = {
      registerSubmenu: jest.fn(),
      registerMenuAction: jest.fn(),
    } as unknown as jest.Mocked<MenuModelRegistry>;

    container.bind(MenuModelRegistry).toConstantValue(menuRegistry);
    container.bind(VitruviusMenuContribution).toSelf();
    contribution = container.get(VitruviusMenuContribution);
  });

  it("should register the Vitruvius submenu", () => {
    contribution.registerMenus(menuRegistry);
    expect(menuRegistry.registerSubmenu).toHaveBeenCalledWith(
      VitruviusMenu.VITRUVIUS,
      "Vitruvius",
    );
  });

  it("should register the HelpCommand", () => {
    contribution.registerMenus(menuRegistry);
    expect(menuRegistry.registerMenuAction).toHaveBeenCalledWith(
      VitruviusMenu.VITRUVIUS,
      {
        commandId: HelpCommand.id,
        label: HelpCommand.label,
        order: "0",
      },
    );
  });

  it("should register the LoadProjectCommand", () => {
    contribution.registerMenus(menuRegistry);
    expect(menuRegistry.registerMenuAction).toHaveBeenCalledWith(
      VitruviusMenu.VITRUVIUS,
      {
        commandId: LoadProjectCommand.id,
        label: LoadProjectCommand.label,
        order: "1",
      },
    );
  });

  it("should register the ImportProjectCommand", () => {
    contribution.registerMenus(menuRegistry);
    expect(menuRegistry.registerMenuAction).toHaveBeenCalledWith(
      VitruviusMenu.VITRUVIUS,
      {
        commandId: ImportProjectCommand.id,
        label: ImportProjectCommand.label,
        order: "2",
      },
    );
  });

  it("should register the RefreshProjectCommand", () => {
    contribution.registerMenus(menuRegistry);
    expect(menuRegistry.registerMenuAction).toHaveBeenCalledWith(
      VitruviusMenu.VITRUVIUS,
      {
        commandId: RefreshProjectCommand.id,
        label: RefreshProjectCommand.label,
        order: "3",
      },
    );
  });

  it("should register the DeleteProjectCommand", () => {
    contribution.registerMenus(menuRegistry);
    expect(menuRegistry.registerMenuAction).toHaveBeenCalledWith(
      VitruviusMenu.VITRUVIUS,
      {
        commandId: DeleteProjectCommand.id,
        label: DeleteProjectCommand.label,
        order: "4",
      },
    );
  });

  it("should register the EditProjectCommand", () => {
    contribution.registerMenus(menuRegistry);
    expect(menuRegistry.registerMenuAction).toHaveBeenCalledWith(
      VitruviusMenu.VITRUVIUS,
      {
        commandId: EditProjectCommand.id,
        label: EditProjectCommand.label,
        order: "5",
      },
    );
  });
});
