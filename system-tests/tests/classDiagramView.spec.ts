import { test, expect } from "@playwright/test";
import { beforeEach, afterEach } from "../hooks";

test.beforeEach(beforeEach);
test.afterEach(afterEach);

test("Test class diagram view", async ({ page }) => {
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
  await page.getByText("ClassDiagram").click();
  await page.getByText("examplePackage").click();
  await expect(page.locator(".css-qcxco2").first()).toBeVisible();
  await expect(
    page.locator("div:nth-child(2) > .css-frtd22 > .css-lldskh > .css-qcxco2"),
  ).toBeVisible();
  await expect(page.locator("path").nth(1)).toBeVisible();
  await expect(
    page.locator('[id="packagediagramwidget\\:packagediagramwidget"]'),
  ).toContainText(
    "Class1 +myIntAttribute:int - +myIntAttribute:Object - +myOperation(param1:int - ,param2:int - ):int - Class2 +myIntAttribute:Object - +myOperation():Object -",
  );
  await expect(
    page.locator('[id="packagediagramwidget\\:packagediagramwidget"]'),
  ).toContainText("Class2 +myIntAttribute:Object - +myOperation():Object -");
});

test("Test class diagram editing", async ({ page }) => {
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
  await page.getByText("ClassDiagram").click();
  await page.getByText("examplePackage").click();
  await page
    .locator('[id="packagediagramwidget\\:packagediagramwidget"]')
    .getByText("Class1", { exact: true })
    .fill("ClassFoo");
  await page
    .locator('[id="packagediagramwidget\\:packagediagramwidget"]')
    .getByText("Class2", { exact: true })
    .fill("ClassBar");
  await page.getByText("myIntAttribute").nth(2).fill("myIntAttributeTest");
  await page.getByText("myOperation").first().fill("myOperationTest");
  await page.locator('[id="theia\\:menubar"]').getByText("Vitruvius").click();
  await page.getByText("Vitruvius Refresh Project", { exact: true }).click();
  await page
    .getByRole("option", { name: "ClassDiagram examplePackage" })
    .locator("a")
    .click();
  await expect(
    page.locator('[id="packagediagramwidget\\:packagediagramwidget"]'),
  ).toContainText(
    "ClassFoo +myIntAttribute:int - +myIntAttribute:Object - +myOperationTest(param1:int - ,param2:int - ):int - ClassBar +myIntAttributeTest:Object - +myOperation():Object -",
  );
  await expect(
    page.locator('[id="packagediagramwidget\\:packagediagramwidget"]'),
  ).toContainText(
    "ClassBar +myIntAttributeTest:Object - +myOperation():Object -",
  );
});

test("Test class diagram deleting", async ({ page }) => {
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
  await page.getByText("ClassDiagram").click();
  await page.getByText("examplePackage").click();
  await page.locator("span:nth-child(23)").click();
  await page.locator("span:nth-child(13)").click();
  await page.getByText("-", { exact: true }).nth(1).click();
  await page.locator('[id="theia\\:menubar"]').getByText("Vitruvius").click();
  await page.getByText("Vitruvius Refresh Project", { exact: true }).click();
  await page
    .getByRole("option", { name: "ClassDiagram examplePackage" })
    .locator("a")
    .click();
  await expect(
    page.locator('[id="packagediagramwidget\\:packagediagramwidget"]'),
  ).toContainText(
    "Class1 +myIntAttribute:int - +myOperation(param1:int - ):int -",
  );
  await expect(
    page.locator('[id="packagediagramwidget\\:packagediagramwidget"]'),
  ).toContainText("Class2 +myIntAttribute:Object -");
});
