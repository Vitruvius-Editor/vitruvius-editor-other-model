import { test, expect } from "@playwright/test";
import { beforeEach, afterEach } from "../hooks";

test.beforeEach(beforeEach);
test.afterEach(afterEach);

test("Test class diagram to source code interaction", async ({ page }) => {
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
  await expect(
    page.locator('[id="packagediagramwidget\\:packagediagramwidget"]'),
  ).toContainText(
    "Class1 +myIntAttribute:int - +myIntAttribute:Object - +myOperation(param1:int - ,param2:int - ):int - Class2 +myIntAttribute:Object - +myOperation():Object -",
  );
  await expect(
    page.locator('[id="packagediagramwidget\\:packagediagramwidget"]'),
  ).toContainText("Class2 +myIntAttribute:Object - +myOperation():Object -");
  await page.locator(".css-qcxco2").first().dblclick();
  await expect(page.getByRole("textbox")).toContainText(
    "public class Class1 { int myIntAttribute; boolean myBooleanAttribute = true; public void myMethod() { } public int myMethod(int myParameter) { return 5; } }",
  );
});
