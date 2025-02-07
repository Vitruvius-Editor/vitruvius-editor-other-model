import type { Config } from '@jest/types';

export default async (): Promise<Config.InitialOptions> => ({
    preset: 'ts-jest',
    testMatch: ['**.test.ts', '**.test.tsx'],
    rootDir: '../',
    transform: {
        '^.+\\.(ts)$': 'ts-jest',
        '^.+\\.(tsx)$': 'ts-jest',
    },
    moduleNameMapper: {
        "\\.(css|less|scss|sass)$": "identity-obj-proxy",
    },
    setupFilesAfterEnv: ['<rootDir>/test-setup.ts'],
});

