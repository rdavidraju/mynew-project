import { Injectable, Component } from '@angular/core';
import { Http } from '@angular/http';

import { AttachmentModel } from './attachment.model';

@Injectable()
export class AttachmentService {

    constructor(
        private http: Http
    ) {}

    /**
     * @author Sameer
     * @description Post Attachment file as base64/blob
     * @param data
     */
    postAttachmentUrl: string = 'agreeapplication/api/postAttchmentAndAttachmentFiles';
    postAttachmentFileUrl: string = 'agreeapplication/api/createAttachmentFiles';
    postAttachment(data: AttachmentModel, isOnlyFile?: boolean) {
        let url = isOnlyFile ? this.postAttachmentFileUrl : this.postAttachmentUrl;
        return this.http.post(url, data).map(res => {
            return res.json();
        });
    }

    /**
     * @author Sameer
     * @description Get Attachment file as base64/blob
     * @param attachmentKey 
     */
    getAttachmentUrl = 'agreeapplication/api/getAttchmentAndAttachmentFilesByAttachmentKey';
    getAttachment(attachmentKey) {
        return this.http.get(`${this.getAttachmentUrl}?attachmentKey=${attachmentKey}`).map(res => {
            return res.json();
        });
    }

    deleteAttachmentUrl = 'agreeapplication/api/attachment-files';
    deleteAttachment(id: number) {
        return this.http.delete(`${this.deleteAttachmentUrl}/${id}`);
    }
}