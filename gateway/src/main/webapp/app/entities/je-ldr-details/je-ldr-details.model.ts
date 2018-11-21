import { BaseEntity } from './../../shared';

export class JeLdrDetails implements BaseEntity {
    constructor(
        public id?: number,
        public jeTempId?: number,
        public colType?: string,
        public colValue?: number,
        public createdBy?: number,
        public createdDate?: any,
        public lastUpdatedBy?: number,
        public lastUpdatedDate?: any,
    ) {
    }
}
