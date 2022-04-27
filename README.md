1.Name of project: helpdesk

2.Launch of project from console:

jar-file: helpdesk>java -jar helpdesk-2.5.2.jar
node modules: helpdesk\src\frontend-react\java-learn-app-main>npm install
frontend part: helpdesk\src\frontend-react\java-learn-app-main>npm start

3.Ports of the project:
    backend: http://localhost:8081
    frontend: http://localhost:3000

4.Start page: http://localhost:3000

5.Sender's email: "tt0188723@gmail.com"

6.Logins (recipients' emails) and passwords of users:

Role 'Employee':
user1_mogilev@yopmail.com/P@ssword1
user2_mogilev@yopmail.com/P@ssword1

Role 'Manager':
manager1_mogilev@yopmail.com/P@ssword1
manager2_mogilev@yopmail.com/P@ssword1

Role 'Engineer':
engineer1_mogilev@yopmail.com/P@ssword1
engineer2_mogilev@yopmail.com/P@ssword1

7.Attachments: resources/attachments

8.Configuration: resources/application.yaml

9.Templates: resources/templates

10.Database scripts: resources/data.sql

11.Launch of all the tests from command line:
    EditConfiguration -> JUnit -> name:mvn test -> All In Directory: helpdesk\src\test ->
    Environment variables : clean test

12.Rest controllers:

AuthController:
registerUser(POST): http://localhost:8081 + body;
authenticationUser(POST): http://localhost:8081/auth + body

TicketController:
save(POST): http://localhost:8081/tickets + body;
getById(GET): http://localhost:8081/tickets/{id};
getAll(GET): http://localhost:8081/tickets/all
getMy(GET): http://localhost:8081/tickets/my
update(PUT): http://localhost:8081/tickets/{id} + body;
changeTicketStatus(PUT): http://localhost:8081/tickets/{id}/change-status + parameters

AttachmentController:
save(POST): http://localhost:8081/tickets/{ticketId}/attachments + body;
getById(GET): http://localhost:8081/tickets/{ticketId}/attachments/{attachmentId};
getAllByTicketId(GET): http://localhost:8081/tickets/{ticketId}/attachments
delete(DELETE): http://localhost:8081/tickets/{ticketId}/attachments/{attachmentId};

CommentController:
save(POST): http://localhost:8081/tickets/{ticketId}/comments + body;
getAllByTicketId(GET): http://localhost:8081/tickets/{ticketId}/comments

FeedbackController:
save(POST): http://localhost:8081/tickets/{ticketId}/feedbacks + body;
getAllByTicketId(GET): http://localhost:8081/tickets/{ticketId}/feedbacks

HistoryController:
save(POST): http://localhost:8081/tickets/{ticketId}/histories + body;
getAllByTicketId(GET): http://localhost:8081/tickets/{ticketId}/histories

