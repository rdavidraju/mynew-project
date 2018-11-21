import { BaseEntity } from './../../shared';

export const DashboardV2BreadCrumbTitles = {
    dashboard: 'Dashboard'
}


export class UserPreference {
    constructor(
        public processesId?: any,
        /* public processesList?: any, */
        public dateRange?: string,
        /* public dateRanges?: any, */
        public reconViolationPeriod?: any,
        public accountingViolationPeriod?: any,
        public drillThruSeconds?: any,
        public savePreference?: boolean,
        public loadsavedPreference?: boolean,
    ) {
        this.savePreference = false;
        this.loadsavedPreference = false;
    }
}

export interface PeriodicElement {
    name: string;
    position: number;
    weight: number;
    symbol: string;
  }