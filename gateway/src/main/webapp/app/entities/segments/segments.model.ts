import { BaseEntity } from './../../shared';

export class Segments implements BaseEntity {
    constructor(
        public id?: number,
        public coaId?: number,
        public segmentName?: string,
        public values?: number,
        public segmentLength?: number,
        public createdBy?: number,
        public createdDate?: any,
        public lastUpdatedBy?: number,
        public lastUpdatedDate?: any,
    ) {
    }
}
