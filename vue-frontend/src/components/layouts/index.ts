import Center from "./Center.vue"
import Column from "./Column.vue"
import Row from "./Row.vue"
import Expand from "./Expand.vue"

export interface Props {
    mainAxisSize?: "max" | "min",
    mainAxisAlignment?: "start" | "end" | "center" | "space-between" | "space-evenly" | "stretch",
    crossAxisAlignment?: "start" | "end" | "center" | "stretch" | "space-between" | "space-evenly",
    wrap?: boolean
}

export const alignmentMap = {
    "start": "flex-start",
    "end": "flex-end",
    "space-between": "space-between",
    "space-evenly": "space-evenly",
    "space-around": "space-around",
    "stretch": "stretch",
    "center": "center"
}

export {
    Center,
    Column,
    Row,
    Expand
}