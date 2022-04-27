import React from "react";
import PropTypes from "prop-types";
import CommentsTable from "./CommentsTable";
import HistoryTable from "./HistoryTable";
import TabPanel from "./TabPanel";
import TicketCreationPageWithRouter from "./TicketCreationPage";
import AttachmentWithRouter from "./Attachment";
import { Link, Route, Switch } from "react-router-dom";
import { withRouter } from "react-router";
import TicketService from '../services/TicketService';
import HistoryService from '../services/HistoryService';
import CommentService from '../services/CommentService';
import AttachmentService from '../services/AttachmentService';

import {
  Button,
  ButtonGroup,
  Paper,
  Tab,
  Tabs,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableRow,
  Typography,
  TextField,
} from "@material-ui/core";

function a11yProps(index) {
  return {
    id: `full-width-tab-${index}`,
    "aria-controls": `full-width-tabpanel-${index}`,
  };
}

class TicketInfo extends React.Component {
  constructor(props) {
    super(props)

    this.state = {
      tabValue: 0,
      ticket: {},
      ticketHistory: [],
      ticketComments: [],
      ticketAttachments: [],
      selectedFile: null,
      comment: '',
      updateTicketAccessError: "",
      createCommentError: [],
      isInvalidComment: false
    }

    this.editTicket = this.editTicket.bind(this);
    this.handleAttachmentChange = this.handleAttachmentChange.bind(this);
    this.handleEnterComment = this.handleEnterComment.bind(this);
    this.addComment = this.addComment.bind(this);
  }

  componentDidMount() {
    const ticketId = this.props.match.params.id;

    TicketService.getTicketById(ticketId).then(res => {
      this.setState({ ticket: res.data });
      sessionStorage.setItem("status", res.data.status)
    })

    AttachmentService.getAllAttachmentsByTicketId(ticketId).then(res => {
      this.setState({ ticketAttachments: res.data });
    });

    HistoryService.getLastFiveHistoriesByTicketId(ticketId).then(res => {
      this.setState({ ticketHistory: res.data });
    });

    CommentService.getLastFiveCommentsByTicketId(ticketId).then(res => {
      this.setState({ ticketComments: res.data });
    });
  }

  editTicket() {
    const userRole = sessionStorage.getItem("userRole");

    if (userRole !== "ROLE_ENGINEER") {
      this.props.history.push(`/update-ticket/${this.state.ticket.id}`);
    } else {
      this.setState({ updateTicketAccessError: "You can't have access to update ticket" });
    }
  }

  handleAttachmentChange = (event) => {
    this.setState({
      selectedFile: event.target.files[0]
    });
  };

  handleEnterComment = (event) => {
    this.setState({
      comment: event.target.value,
    });
  };

  addComment = () => {
    const ticketId = this.props.match.params.id;
    const comment = {
      text: this.state.comment
    }

    CommentService.createComment(comment, ticketId).then(res => {
      this.setState({ createCommentError: [] })
      console.log(res.data);
    }).catch(err => {
      this.setState({ isInvalidComment: true });
      this.setState({ createCommentError: err.response.data })
    })
  }

  handleTabChange = (event, value) => {
    this.setState({
      tabValue: value,
    });
  };

  handleShowAllHistory = (event) => {
    const ticketId = this.props.match.params.id;

    HistoryService.getAllHistoriesByTicketId(ticketId).then((res) => {
      this.setState({ ticketHistory: res.data })
    });
  }

  handleShowAllComments = (event) => {
    const ticketId = this.props.match.params.id;

    CommentService.getAllCommentsByTicketId(ticketId).then((res) => {
      this.setState({ ticketComments: res.data })
    });
  }

  render() {
    const { comment, tabValue, ticketAttachments, ticketComments, ticketHistory } =
      this.state;

    const { url } = this.props.match;

    const { handleCancelTicket, handleEditTicket, handleSubmitTicket, handleShowAllHistory, handleShowAllComments } = this;

    return (
      <Switch>
        <Route exact path={url}>
          <div align='center'>
            {
              this.state.updateTicketAccessError.length > 0 ?
                <div>
                  {this.state.updateTicketAccessError}
                </div>
                :
                ''
            }
          </div>
          <div className="ticket-data-container">
            <div className={"buttons-container"}>
              <Button component={Link} to="/tickets" variant="contained" color="secondary">
                Ticket list
              </Button>
              <Button onClick={() => this.editTicket(this.state.ticket.id)}
                variant="contained"
                color="primary">
                Edit
              </Button>
              <Button component={Link} to={`/feedbacks/${this.state.ticket.id}`} variant="contained" color="secondary">
                Leave Feedback
              </Button>
            </div>
            <div className="ticket-data-container__title">
              <Typography variant="h4">{`Ticket(${this.state.ticket.id}) - ${this.state.ticket.name}`}</Typography>
            </div>
            <div className="ticket-data-container__info">
              <TableContainer className="ticket-table" component={Paper}>
                <Table>
                  <TableBody>
                    <TableRow>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          Created on:
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          {this.state.ticket.createdOn}
                        </Typography>
                      </TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          Category:
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          {this.state.ticket.category}
                        </Typography>
                      </TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          Status:
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          {this.state.ticket.status}
                        </Typography>
                      </TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          Urgency:
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          {this.state.ticket.urgency}
                        </Typography>
                      </TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          Desired Resolution Date:
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          {this.state.ticket.desiredResolutionDate}
                        </Typography>
                      </TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          Owner:
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          {this.state.ticket.ticketOwner}
                        </Typography>
                      </TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          Approver:
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          {this.state.ticket.approver || "Not assigned"}
                        </Typography>
                      </TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          Assignee:
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          {this.state.ticket.assignee || "Not assigned"}
                        </Typography>
                      </TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          Attachments:
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          {ticketAttachments.map((item, index) => {
                            return (
                              <TableRow key={index}>
                                <Link to={`${url}/attachments/${item.id}`}>{item.name}</Link>
                              </TableRow>
                            );
                          })}
                        </Typography>
                      </TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          Description:
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          {this.state.ticket.description || "Not assigned"}
                        </Typography>
                      </TableCell>
                    </TableRow>
                  </TableBody>
                </Table>
              </TableContainer>
            </div>
            {this.state.ticket.state === "draft" && (
              <div className="ticket-data-container__button-section">
                <ButtonGroup variant="contained" color="primary">
                  <Button
                    component={Link}
                    to="/tickets"
                    onClick={handleSubmitTicket}
                  >
                    Submit
                  </Button>

                  <Button
                    component={Link}
                    to={'/add-ticket/:id'}
                    onClick={handleEditTicket}
                  >
                    Edit
                  </Button>
                  <Button
                    component={Link}
                    to="/tickets"
                    onClick={handleCancelTicket}
                  >
                    Cancel
                  </Button>
                </ButtonGroup>
              </div>
            )}
            <div className="ticket-data-container__comments-section comments-section">
              <div className="">
                <Tabs
                  variant="fullWidth"
                  onChange={this.handleTabChange}
                  value={tabValue}
                  indicatorColor="primary"
                  textColor="primary"
                >
                  <Tab label="History" {...a11yProps(0)} />
                  <Tab label="Comments" {...a11yProps(1)} />
                </Tabs>
                <TabPanel value={tabValue} index={0}>
                  <HistoryTable
                    history={ticketHistory}
                    showAllHistoryCallback={handleShowAllHistory} />
                </TabPanel>
                <TabPanel value={tabValue} index={1}>
                  <CommentsTable
                    comments={ticketComments}
                    showAllCommentsCallback={handleShowAllComments} />
                </TabPanel>
              </div>
            </div>
            {tabValue && (
              <div className="ticket-data-container__enter-comment-section enter-comment-section">
                <TextField
                  label="Enter a comment"
                  multiline
                  rows={4}
                  value={comment}
                  variant="filled"
                  className="comment-text-field"
                  onChange={this.handleEnterComment}
                />
                <div className="enter-comment-section__add-comment-button">
                  <Button
                    variant="contained"
                    color="primary"
                    onClick={this.addComment}
                  >
                    Add Comment
                  </Button>
                </div>
                {
                  this.state.isInvalidComment ?
                    <div>
                      {this.state.createCommentError.map((error) => (
                        <div align='center'>
                          {error.text}
                        </div>
                      ))}
                    </div>
                    :
                    ''
                }
              </div>
            )}
          </div>
        </Route>
        <Route path="/add-ticket/:id">
          <TicketCreationPageWithRouter />
        </Route>
        <Route path="/tickets/:id/attachments/:attachmentId">
          <AttachmentWithRouter />
        </Route>
      </Switch>
    );
  }
}

TicketInfo.propTypes = {
  match: PropTypes.object,
};

const TicketInfoWithRouter = withRouter(TicketInfo);
export default TicketInfoWithRouter;
