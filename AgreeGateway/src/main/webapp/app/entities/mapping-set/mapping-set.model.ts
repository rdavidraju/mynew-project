import { BaseEntity } from './../../shared';

export class MappingSet implements BaseEntity {
    constructor(
        public id?: number,
        public tenantId?: number,
        public name?: string,
        public description?: string,
        public createdBy?: number,
        public lastUpdatedBy?: number,
        public createdDate?: any,
        public lastUpdatedDate?: any,
        public purpose?: any,
        public lookUppurpose?: any,
        public mappingSetValues?: any,
        public enabledFlag?: any,
        public startDate?: any,
        public endDate?: any
    ) {
    }
}

export const MappingSetBreadCrumbTitles = {
    mappingSetList: 'Mapping Set List',
    mappingSetDetails: 'Mapping Set Details',
    mappingSetNew: 'Create New Mapping Set',
    mappingSetEdit: 'Edit Mapping Set',
}