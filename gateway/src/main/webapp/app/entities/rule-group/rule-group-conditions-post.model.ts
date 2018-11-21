export class RuleGroupConditionsPost {
    constructor(
        public sequence?: number,
        public sourceColumn?: any,
        public operator?: any,
        public targetColumn?: any
    ) {
    }
}
