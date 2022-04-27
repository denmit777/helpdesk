import "./App.css";
import LoginPage from "./components/LoginPage";
import RegisterPage from "./components/RegisterPage";
import MainPageWithRouter from "./components/MainPage";
import TicketInfo from "./components/TicketInfo";
import AttachmentWithRouter from "./components/Attachment";
import TicketCreationPageWithRouter from "./components/TicketCreationPage";
import TicketEditPageWithRouter from "./components/TicketEditPage";
import AuthenticatedRoute from './components/AuthenticatedRoute';
import FeedbackPage from "./components/FeedbackPage";

import {
  BrowserRouter as Router,
  Route,
  Switch,
} from "react-router-dom";

function App() {
  return (
    <Router>
      <Switch>
        <Route path="/" exact component={LoginPage} />
//        <AuthenticatedRoute path="/register" exact component={RegisterPage} />
        <Route path="/register" exact component={RegisterPage} />
        <AuthenticatedRoute path="/tickets" exact component={MainPageWithRouter} />
        <Route path="/tickets/:id" component={TicketInfo} />
        <Route path="/tickets/:id/attachments/:attachmentId" component={AttachmentWithRouter} />
        <Route path="/update-ticket/:id" component={TicketEditPageWithRouter} />
        <Route path="/add-ticket/:id">
          <TicketCreationPageWithRouter />
        </Route>
        <Route path="/feedbacks/:ticketId">
          <FeedbackPage />
        </Route>
      </Switch>
    </Router>
  );
}

export default App;
