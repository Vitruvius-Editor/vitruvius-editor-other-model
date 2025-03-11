import { test, expect } from '@playwright/test';
import {beforeEach, afterEach} from '../hooks';

test.beforeEach(beforeEach);
test.afterEach(afterEach);

test('Test DisplayView showing', async ({ page }) => {
  await page.goto('http://localhost:3000/#/home/project');
  await page.locator('[id="theia\\:menubar"]').getByText('Vitruvius').click();
  await page.getByText('Vitruvius Import Project').click();
  await page.getByRole('combobox', { name: 'input' }).fill('Example Project');
  await page.getByRole('combobox', { name: 'input' }).press('Enter');
  await page.getByRole('combobox', { name: 'input' }).fill('Example Description');
  await page.getByRole('combobox', { name: 'input' }).press('Enter');
  await page.getByRole('combobox', { name: 'input' }).fill('localhost');
  await page.getByRole('combobox', { name: 'input' }).press('Enter');
  await page.getByRole('combobox', { name: 'input' }).fill('8000');
  await page.getByRole('combobox', { name: 'input' }).press('Enter');
  await page.locator('[id="shell-tab-widget\\:display-views"] > .theia-tab-icon-label > .p-TabBar-tabIcon').click();
  await expect(page.locator('[id="widget\\:display-views"]')).toContainText('The following views are avaliable for the loaded project:');
  await expect(page.locator('[id="widget\\:display-views"]')).toContainText('SourceCode');
  await expect(page.locator('[id="widget\\:display-views"]')).toContainText('ClassTable');
  await expect(page.locator('[id="widget\\:display-views"]')).toContainText('ClassDiagram');
});

test('Test window displaying', async ({ page }) => {
  await page.goto('http://localhost:3000/#/home/project');
  await page.locator('[id="theia\\:menubar"]').getByText('Vitruvius').click();
  await page.getByText('Vitruvius Import Project').click();
  await page.getByRole('combobox', { name: 'input' }).fill('Example Project');
  await page.getByRole('combobox', { name: 'input' }).press('Enter');
  await page.getByRole('combobox', { name: 'input' }).fill('Example Description');
  await page.getByRole('combobox', { name: 'input' }).press('Enter');
  await page.getByRole('combobox', { name: 'input' }).fill('localhost');
  await page.getByRole('combobox', { name: 'input' }).press('Enter');
  await page.getByRole('combobox', { name: 'input' }).fill('8000');
  await page.getByRole('combobox', { name: 'input' }).press('Enter');
  await page.locator('[id="shell-tab-widget\\:display-views"] > .theia-tab-icon-label > .p-TabBar-tabIcon').click();
  await page.getByText('SourceCode').click();
  await expect(page.locator('[id="widget\\:display-views"]')).toContainText('Class1');
  await expect(page.locator('[id="widget\\:display-views"]')).toContainText('Class2');
  await page.getByText('ClassTable').click();
  await expect(page.locator('[id="widget\\:display-views"]')).toContainText('examplePackage');
  await page.getByText('ClassDiagram').click();
  await expect(page.locator('[id="widget\\:display-views"]')).toContainText('examplePackage');
});
