import { test, expect } from "@playwright/test";
import { beforeEach, afterEach } from "../hooks";

test.beforeEach(beforeEach);
test.afterEach(afterEach);

test("Test class table view", async ({ page }) => {
  await page.goto("http://localhost:3000/#/home/project");
  await page.locator('[id="theia\\:menubar"]').getByText("Vitruvius").click();
  await page.getByText("Vitruvius Import Project").click();
  await page.getByRole("combobox", { name: "input" }).fill("Example Project");
  await page.getByRole("combobox", { name: "input" }).press("Enter");
  await page
    .getByRole("combobox", { name: "input" })
    .fill("Example Description");
  await page.getByRole("combobox", { name: "input" }).press("Enter");
  await page.getByRole("combobox", { name: "input" }).fill("localhost");
  await page.getByRole("combobox", { name: "input" }).press("Enter");
  await page.getByRole("combobox", { name: "input" }).fill("8000");
  await page.getByRole("combobox", { name: "input" }).press("Enter");
  await page
    .locator(
      '[id="shell-tab-widget\\:display-views"] > .theia-tab-icon-label > .p-TabBar-tabIcon',
    )
    .click();
  await page.getByText("ClassTable").click();
  await page.getByText("examplePackage").click();
  await expect(page.locator("thead")).toContainText("Attributes");
  await expect(page.locator("thead")).toContainText("Interfaces");
  await expect(page.locator("thead")).toContainText("Abstract");
  await expect(page.locator("thead")).toContainText("Final");
  await expect(page.locator("thead")).toContainText("Lines of Code");
  await expect(page.locator("thead")).toContainText("Methods");
  await expect(page.locator("thead")).toContainText("Name");
  await expect(page.locator("thead")).toContainText("Superclass");
  await expect(page.locator("thead")).toContainText("Visibility");
  await expect(page.locator("tbody")).toContainText("2");
  await expect(page.locator("tbody")).toContainText("1");
  await expect(page.locator("tbody")).toContainText("false");
  await expect(page.locator("tbody")).toContainText("true");
  await expect(page.locator("tbody")).toContainText("true");
  await expect(page.locator("tbody")).toContainText("false");
  await expect(page.locator("tbody")).toContainText("1");
  await expect(page.locator("tbody")).toContainText("1");
  await expect(page.locator("tbody")).toContainText("Class2");
  await expect(
    page.locator('[id="shell-tab-tablewidget\\:tablewidget"]'),
  ).toContainText("examplePackage");
  await expect(
    page.getByRole("cell", { name: "Class1" }).getByRole("textbox"),
  ).toBeVisible();
  await expect(page.getByRole("textbox").nth(2)).toBeVisible();
  await expect(page.getByRole("textbox").nth(1)).toBeVisible();
  await expect(page.getByRole("textbox").nth(3)).toBeVisible();
});

test("Test class table editing", async ({ page }) => {
  await page.goto("http://localhost:3000/#/home/project");
  await page.locator('[id="theia\\:menubar"]').getByText("Vitruvius").click();
  await page.getByText("Vitruvius Import Project").click();
  await page.getByRole("combobox", { name: "input" }).fill("Example Project");
  await page.getByRole("combobox", { name: "input" }).press("Enter");
  await page
    .getByRole("combobox", { name: "input" })
    .fill("Example Description");
  await page.getByRole("combobox", { name: "input" }).press("Enter");
  await page.getByRole("combobox", { name: "input" }).fill("localhost");
  await page.getByRole("combobox", { name: "input" }).press("Enter");
  await page.getByRole("combobox", { name: "input" }).fill("8000");
  await page.getByRole("combobox", { name: "input" }).press("Enter");
  await page
    .locator(
      '[id="shell-tab-widget\\:display-views"] > .theia-tab-icon-label > .p-TabBar-tabIcon',
    )
    .click();
  await page.getByText("ClassTable").click();
  await page.getByText("examplePackage").click();
  await page.getByRole("cell", { name: "Class1" }).getByRole("textbox").click();
  await page
    .getByRole("cell", { name: "Class1" })
    .getByRole("textbox")
    .fill("ClassFoo");
  await page.getByRole("textbox").nth(2).click();
  await page.getByRole("textbox").nth(2).fill("ClassBar");
  await page.locator('[id="theia\\:menubar"]').getByText("Vitruvius").click();
  await page.getByText("Vitruvius Refresh Project", { exact: true }).click();
  await page
    .getByRole("option", { name: "ClassTable examplePackage" })
    .locator("a")
    .click();
  await expect(page.locator("tbody")).toContainText("ClassBar");
  await page.getByRole("textbox").nth(1).click();
  await page.getByRole("textbox").nth(1).fill("protected");
  await page.locator('[id="theia\\:menubar"]').getByText("Vitruvius").click();
  await page.getByText("Vitruvius Refresh Project", { exact: true }).click();
  await page
    .getByRole("option", { name: "ClassTable examplePackage" })
    .locator("a")
    .click();
  await expect(
    page.getByRole("cell", { name: "protected" }).getByRole("textbox"),
  ).toBeVisible();
});
