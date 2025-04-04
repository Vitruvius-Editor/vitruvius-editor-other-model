import { test, expect } from "@playwright/test";
import { beforeEach, afterEach } from "../hooks";

test.beforeEach(beforeEach);
test.afterEach(afterEach);

test("Test project editing", async ({ page }) => {
  await page.goto("http://localhost:3000/");
  await page.locator('[id="theia\\:menubar"]').getByText("Vitruvius").click();
  await page.getByText("Vitruvius Import Project").click();
  await page.getByRole("combobox", { name: "input" }).fill("Test Project");
  await page.getByRole("combobox", { name: "input" }).press("Enter");
  await page
    .getByRole("combobox", { name: "input" })
    .fill("Example Description");
  await page.getByRole("combobox", { name: "input" }).press("Enter");
  await page.getByRole("combobox", { name: "input" }).fill("localhost");
  await page.getByRole("combobox", { name: "input" }).press("Enter");
  await page.getByRole("combobox", { name: "input" }).fill("8000");
  await page.getByRole("combobox", { name: "input" }).press("Enter");
  await page.locator('[id="theia\\:menubar"]').getByText("Vitruvius").click();
  await page.getByText("Vitruvius Load Project", { exact: true }).click();
  await expect(
    page.getByLabel("Type to narrow down results.").locator("a"),
  ).toContainText("Test Project");
  await page.locator('[id="theia\\:menubar"]').getByText("Vitruvius").click();
  await page.getByText("Vitruvius Edit Project", { exact: true }).click();
  await page.getByRole("option", { name: "Test Project" }).locator("a").click();
  await page
    .getByRole("combobox", { name: "input" })
    .fill("Edited Test Project");
  await page.getByRole("combobox", { name: "input" }).press("Enter");
  await page
    .getByRole("combobox", { name: "input" })
    .press("ControlOrMeta+ArrowLeft");
  await page.getByRole("combobox", { name: "input" }).press("ArrowLeft");
  await page
    .getByRole("combobox", { name: "input" })
    .fill("Edited Description");
  await page.getByRole("combobox", { name: "input" }).press("Enter");
  await page.getByRole("combobox", { name: "input" }).press("Enter");
  await page.getByRole("combobox", { name: "input" }).press("Enter");
  await page.locator('[id="theia\\:menubar"]').getByText("Vitruvius").click();
  await page.getByText("Vitruvius Load Project", { exact: true }).click();
  await expect(
    page.getByLabel("Type to narrow down results.").locator("a"),
  ).toContainText("Edited Test Project");
});

test("Test invalid project unloading", async ({ page }) => {
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
  await expect(page.locator('[id="widget\\:display-views"]')).toContainText(
    "The following views are avaliable for the loaded project:",
  );
  await page.locator('[id="theia\\:menubar"]').getByText("Vitruvius").click();
  await page.getByText("Vitruvius Edit Project", { exact: true }).click();
  await page
    .getByRole("option", { name: "Example Project" })
    .locator("a")
    .click();
  await page.getByRole("combobox", { name: "input" }).press("Enter");
  await page.getByRole("combobox", { name: "input" }).press("Enter");
  await page.getByRole("combobox", { name: "input" }).press("Enter");
  await page.getByRole("combobox", { name: "input" }).fill("1234");
  await page.getByRole("combobox", { name: "input" }).press("Enter");
  await expect(page.locator('[id="widget\\:display-views"]')).toContainText(
    "Currently no Vitruvius project is loaded.",
  );
});
