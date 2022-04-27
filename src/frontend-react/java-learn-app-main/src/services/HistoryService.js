import axios from 'axios';

const TICKET_API_BASE_URL = "http://localhost:8081/tickets/";

class HistoryService {

    getLastFiveHistoriesByTicketId(ticketId) {
        return axios.get(TICKET_API_BASE_URL + ticketId + "/histories");
    }

    getAllHistoriesByTicketId(ticketId) {
        return axios.get(TICKET_API_BASE_URL + ticketId + "/histories?buttonValue=Show All");
    }

    createHistory(history, ticketId) {
        return axios.post(TICKET_API_BASE_URL + ticketId + "/histories", history);
    }

    createHistoryAfterChangeStatusToCanceled(history, ticketId, previousStatus) {
        return axios.post(TICKET_API_BASE_URL + ticketId + "/histories/?previousStatus=" + previousStatus + "&newStatus=CANCELED", history);
    }

    createHistoryAfterChangeStatusToNew(history, ticketId, previousStatus) {
        return axios.post(TICKET_API_BASE_URL + ticketId + "/histories/?previousStatus=" + previousStatus + "&newStatus=NEW", history);
    }

    createHistoryAfterChangeStatusToApproved(history, ticketId, previousStatus) {
        return axios.post(TICKET_API_BASE_URL + ticketId + "/histories/?previousStatus=" + previousStatus + "&newStatus=APPROVED", history);
    }

    createHistoryAfterChangeStatusToDeclined(history, ticketId, previousStatus) {
        return axios.post(TICKET_API_BASE_URL + ticketId + "/histories/?previousStatus=" + previousStatus + "&newStatus=DECLINED", history);
    }

    createHistoryAfterChangeStatusToInProgress(history, ticketId, previousStatus) {
        return axios.post(TICKET_API_BASE_URL + ticketId + "/histories/?previousStatus=" + previousStatus + "&newStatus=IN_PROGRESS", history);
    }

    createHistoryAfterChangeStatusToDone(history, ticketId, previousStatus) {
        return axios.post(TICKET_API_BASE_URL + ticketId + "/histories/?previousStatus=" + previousStatus + "&newStatus=DONE", history);
    }
}

export default new HistoryService() 