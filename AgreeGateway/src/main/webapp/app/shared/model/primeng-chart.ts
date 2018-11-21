export class ChartData {
    constructor(
    public labels?: string[],
    public datasets?: ChartDataSet[]
    ){}
}

export class ChartDataSet {
    constructor(
    public label?: string,
    public data?: any[],
    public fill?: boolean,
    public borderColor?: string,
    public backgroundColor?: string
    ){}
}