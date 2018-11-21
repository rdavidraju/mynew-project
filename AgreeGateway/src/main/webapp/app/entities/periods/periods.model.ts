import { BaseEntity } from './../../shared';

export class Periods implements BaseEntity {
    constructor(
        public id?: number,
        public calId?: number,
        public periodName?: string,
        public fromDate?: any,
        public toDate?: any,
        public periodNum?: number,
        public quarter?: number,
        public year?: number,
        public adjustment?: boolean,
        public status?: string,
        public createdBy?: number,
        public createdDate?: any,
        public lastUpdatedBy?: number,
        public lastUpdatedDate?: any,
    ) {
        this.adjustment = false;
    }
}
