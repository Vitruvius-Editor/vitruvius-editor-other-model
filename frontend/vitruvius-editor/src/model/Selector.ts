export type Selector =
  | { type: "All" }
  | { type: "Name"; name: string; exact: boolean };
