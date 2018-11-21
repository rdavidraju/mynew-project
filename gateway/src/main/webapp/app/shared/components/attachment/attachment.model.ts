export class AttachmentModel {
    public id: any;
    public attachmentKey: string;
    public attachmentCategory: string;
    public createdBy: number;
    public creationDate: string;
    public lastUpdatedBy: number;
    public lastUpdatedDate: string;
    public attachmentFilesList: Array<AttachmentFile>
}

export class AttachmentFile {
    public attachmentId: any;
    public createdBy: number;
    public creationDate: string;
    public fileContent: string;
    public fileName: string;
    public fileType: string;
    public id: any;
    public lastUpdatedBy: number;
    public lastUpdatedDate: string;
    public url: string;
}

export const blobTypes = { 'csv': 'text/csv', 'xls': 'application/vnd.ms-excel', 'xlsx': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', 'pdf': 'application/pdf' };