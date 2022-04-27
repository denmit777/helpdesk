import axios from 'axios';

const TICKET_API_BASE_URL = "http://localhost:8081/tickets"

class TicketService {

    getAllTickets() {
        return axios.get(TICKET_API_BASE_URL + '/all');
    }

    getAllSortedTickets = (sortField) => {
        return axios.get(TICKET_API_BASE_URL + '/all?sortField=' + sortField);
    }

    getAllDescSortedTickets = (sortField) => {
        return axios.get(TICKET_API_BASE_URL + '/all?sortField=' + sortField + '&sortDirection=desc');
    }

    getAllTicketsSearchedById = (parameter) => {
        return axios.get(TICKET_API_BASE_URL + '/all?searchField=id&parameter=' + parameter);
    }

    getAllTicketsSearchedByName = (parameter) => {
        return axios.get(TICKET_API_BASE_URL + '/all?searchField=name&parameter=' + parameter);
    }

    getAllTicketsSearchedByDesiredDate = (parameter) => {
        return axios.get(TICKET_API_BASE_URL + '/all?searchField=desiredResolutionDate&parameter=' + parameter);
    }

    getAllTicketsSearchedByUrgency = (parameter) => {
        return axios.get(TICKET_API_BASE_URL + '/all?searchField=urgency&parameter=' + parameter);
    }

    getAllTicketsSearchedByStatus = (parameter) => {
        return axios.get(TICKET_API_BASE_URL + '/all?searchField=status&parameter=' + parameter);
    }

    getMyTickets() {
        return axios.get(TICKET_API_BASE_URL + '/my');
    }

    getMySortedTickets = (sortField) => {
        return axios.get(TICKET_API_BASE_URL + '/my?sortField=' + sortField);
    }

    getMyDescSortedTickets = (sortField) => {
        return axios.get(TICKET_API_BASE_URL + '/my?sortField=' + sortField + '&sortDirection=desc');
    }

    getMyTicketsSearchedById = (parameter) => {
        return axios.get(TICKET_API_BASE_URL + '/my?searchField=id&parameter=' + parameter);
    }

    getMyTicketsSearchedByName = (parameter) => {
        return axios.get(TICKET_API_BASE_URL + '/my?searchField=name&parameter=' + parameter);
    }

    getMyTicketsSearchedByDesiredDate = (parameter) => {
        return axios.get(TICKET_API_BASE_URL + '/my?searchField=desiredResolutionDate&parameter=' + parameter);
    }

    getMyTicketsSearchedByUrgency = (parameter) => {
        return axios.get(TICKET_API_BASE_URL + '/my?searchField=urgency&parameter=' + parameter);
    }

    getMyTicketsSearchedByStatus = (parameter) => {
        return axios.get(TICKET_API_BASE_URL + '/my?searchField=status&parameter=' + parameter);
    }

    createTicket(ticket) {
        return axios.post(TICKET_API_BASE_URL, ticket); 
    }

    createTicketAsDraft(ticket) {
        return axios.post(TICKET_API_BASE_URL + '/?buttonValue=SaveAsDraft', ticket);
    }

    getTicketById(ticketId) {
        return axios.get(TICKET_API_BASE_URL + '/' + ticketId);
    }

    updateTicket(ticket, ticketId) {
        return axios.put(TICKET_API_BASE_URL + '/' + ticketId, ticket);
    }

    updateTicketAndSaveAsDraft(ticket, ticketId) {
        return axios.put(TICKET_API_BASE_URL + '/' + ticketId + '/?buttonValue=SaveAsDraft', ticket);
    }

    changeTicketStatusToNew(ticketId) {
        return axios.put(TICKET_API_BASE_URL + '/' + ticketId + '/change-status?newStatus=new');    
    }

    changeTicketStatusToCanceled(ticketId) {
        return axios.put(TICKET_API_BASE_URL + '/' + ticketId + '/change-status?newStatus=canceled');    
    }

    changeTicketStatusToApproved(ticketId) {
        return axios.put(TICKET_API_BASE_URL + '/' + ticketId + '/change-status?newStatus=approved');    
    }

    changeTicketStatusToDeclined(ticketId) {
        return axios.put(TICKET_API_BASE_URL + '/' + ticketId + '/change-status?newStatus=declined');    
    }

    changeTicketStatusToInProgress(ticketId) {
        return axios.put(TICKET_API_BASE_URL + '/' + ticketId + '/change-status?newStatus=in_progress');    
    }

    changeTicketStatusToDone(ticketId) {
        return axios.put(TICKET_API_BASE_URL + '/' + ticketId + '/change-status?newStatus=done');    
    }

    getNewTicketId() {
        return axios.get(TICKET_API_BASE_URL);
    }
}

export default new TicketService() 