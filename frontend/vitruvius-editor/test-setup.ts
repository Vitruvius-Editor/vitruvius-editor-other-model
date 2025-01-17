import { JSDOM } from 'jsdom';

const { window } = new JSDOM(`<!DOCTYPE html><html><body></body></html>`, {
	url: 'https://example.com',
		includeNodeLocations: true,
  		storageQuota: 10000000,
  		pretendToBeVisual: true
});
global.document = window.document;
// @ts-ignore
global.window = window;
global.Element = window.Element;

