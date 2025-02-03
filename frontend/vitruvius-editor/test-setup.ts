import {FrontendApplicationConfigProvider} from "@theia/core/lib/browser/frontend-application-config-provider";
FrontendApplicationConfigProvider.set({});

Object.defineProperty(document, 'queryCommandSupported', {
	value: jest.fn((command: string) => {
		// Customize the behavior based on the command
		if (command === 'copy') {
			return true; // Mocking that the 'copy' command is supported
		}
		return false; // Other commands are not supported
	}),
	writable: true, // Allow overwriting if needed
});


