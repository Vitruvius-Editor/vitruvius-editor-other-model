export type RowEntry = {
  uuid: string;
  name: string;
  visibility: string;
  isAbstract: boolean;
  isFinal: boolean;
  superClassName: string;
  interfaces: string[];
  attributeCount: number;
  methodCount: number;
  linesOfCode: number;
};
