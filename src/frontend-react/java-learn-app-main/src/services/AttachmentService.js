import axios from 'axios';

const TICKET_API_BASE_URL = "http://localhost:8081/tickets/";

class AttachmentService {

    getAllAttachmentsByTicketId(ticketId) {
        return axios.get(TICKET_API_BASE_URL + ticketId + "/attachments");
    }

    createAttachment(attachment, ticketId, fileName) {
        return axios.post(TICKET_API_BASE_URL + ticketId + "/attachments?fileName=" + fileName, attachment);
    }

    getAttachmentById(attachmentId, ticketId) {
        return axios.get(TICKET_API_BASE_URL + ticketId + "/attachments/" + attachmentId);
    }

    deleteAttachment(attachmentId, ticketId) {
        return axios.delete(TICKET_API_BASE_URL + ticketId + "/attachments/" + attachmentId);
    }
}

export default new AttachmentService() 