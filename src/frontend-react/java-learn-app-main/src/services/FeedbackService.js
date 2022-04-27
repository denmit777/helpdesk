import axios from 'axios';

const TICKET_API_BASE_URL = "http://localhost:8081/tickets/";

class FeedbackService {

    getLastFiveFeedbacksByTicketId(ticketId) {
        return axios.get(TICKET_API_BASE_URL + ticketId + "/feedbacks");
    }

    getAllFeedbacksByTicketId(ticketId) {
        return axios.get(TICKET_API_BASE_URL + ticketId + "/feedbacks?buttonValue=Show All");
    }

    createFeedback(feedback, ticketId) {
        return axios.post(TICKET_API_BASE_URL + ticketId + "/feedbacks", feedback);
    }
}

export default new FeedbackService() 