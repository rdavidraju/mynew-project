import { BaseEntity } from './../../shared';

export class ApplicationPrograms implements BaseEntity {
    constructor(
        public id?: number,
        public prgmName?: string,
        public tenantId?: number,
        public prgmDescription?: string,
        public generatedPath?: string,
        public targetPath?: string,
        public prgmPath?: string,
        public prgmOrClassName?: string,
        public prgmType?: string,
        public enable?: boolean,
        public startDate?: any,
        public endDate?: any,
        public creationDate?: any,
        public createdBy?: number,
        public lastUpdationDate?: any,
        public lastUpdatedBy?: number,
    ) {
        this.enable = false;
    }
}
