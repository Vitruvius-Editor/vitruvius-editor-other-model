import { ColumnEntry } from "./ColumnEntry"
import { RowEntry } from "./RowEntry"

export type Table = {
    columns: ColumnEntry[],
    rows: RowEntry[]
}
