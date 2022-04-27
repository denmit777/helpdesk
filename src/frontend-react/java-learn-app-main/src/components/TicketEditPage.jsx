import React from "react";
import {
  Button,
  InputLabel,
  FormControl,
  MenuItem,
  Select,
  TextField,
  Typography,
} from "@material-ui/core";
import { withRouter } from "react-router-dom";
import { CATEGORIES_OPTIONS, URGENCY_OPTIONS } from "../constants/inputsValues";
import TicketService from '../services/TicketService';
import HistoryService from '../services/HistoryService';
import CommentService from '../services/CommentService';
import AttachmentService from "../services/AttachmentService";

class TicketEditPage extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      id: this.props.match.params.id,
      category: 'APPLICATION_AND_SERVICES',
      name: '',
      description: "",
      urgency: 'CRITICAL',
      desiredResolutionDate: "2022-06-06",
      ticketHistory: [],
      ticketComments: [],
      ticketAttachments: [],
      comment: '',
      selectedFile: null,
      updateTicketAccessError: [],
      haveAccess: true,
      updateTicketError: [],
      isInvalidTicket: false,
      attachmentUploadError: [],
      isInvalidAttachment: false,
      createCommentError: [],
      isInvalidComment: false,
    };

    this.handleCategoryChange = this.handleCategoryChange.bind(this);
    this.handleNameChange = this.handleNameChange.bind(this);
    this.handleDescriptionChange = this.handleDescriptionChange.bind(this);
    this.handleUrgencyChange = this.handleUrgencyChange.bind(this);
    this.handleDesiredResolutionDate = this.handleDesiredResolutionDate.bind(this);
    this.handleCommentChange = this.handleCommentChange.bind(this);
    this.handleAttachmentChange = this.handleAttachmentChange.bind(this);

    this.handleSaveDraft = this.handleSaveDraft.bind(this);
    this.handleSubmitTicket = this.handleSubmitTicket.bind(this);
    this.viewTicket = this.viewTicket.bind(this);
  }

  componentDidMount() {

    TicketService.getTicketById(this.state.id).then((res) => {
      let ticket = res.data;
      this.setState({
        category: ticket.category,
        name: ticket.name,
        description: ticket.description,
        urgency: ticket.urgency,
        desiredResolutionDate: ticket.desiredResolutionDate,
      });
    });

    AttachmentService.getAllAttachmentsByTicketId(this.state.id).then(res => {
      this.setState({ ticketAttachments: res.data });
    });
  }

  handleCategoryChange = (event) => {
    this.setState({
      category: event.target.value,
    });
  };

  handleNameChange = (event) => {
    this.setState({
      name: event.target.value,
    });
  };

  handleDescriptionChange = (event) => {
    this.setState({
      description: event.target.value,
    });
  };

  handleUrgencyChange = (event) => {
    this.setState({
      urgency: event.target.value,
    });
  };

  handleDesiredResolutionDate = (event) => {
    this.setState({
      desiredResolutionDate: event.target.value,
    });
  };

  handleCommentChange = (event) => {
    this.setState({
      comment: event.target.value,
    });
  };

  handleAttachmentChange = (event) => {
    this.setState({
      selectedFile: event.target.files[0]
    });
  };

  addFile = (event) => {
    const ticketId = this.props.match.params.id;

    const attachment = new FormData();
    attachment.append(
      "file", this.state.selectedFile,
    );

    const attachmentHistory = {
      action: "File is attached",
      description: `File is attached: ${this.state.selectedFile !== null ? this.state.selectedFile.name : ""}`
    };

    const ticketStatus = sessionStorage.getItem("status");

    if (ticketStatus === "DRAFT") {
      if (this.state.selectedFile != null) {
        AttachmentService.createAttachment(attachment, ticketId, this.state.selectedFile.name).then(res => {
          this.setState({ attachmentUploadError: [] })
          this.setState({
            ticketAttachments: [...this.state.ticketAttachments, attachment],
          })
          HistoryService.createHistory(attachmentHistory, ticketId).then(res => {
            this.setState({
              ticketHistory: [...this.state.ticketHistory, attachmentHistory]
            });
          })
        }).catch(err => {
          this.setState({ isInvalidAttachment: true });
          this.setState({ attachmentUploadError: err.response.data })
        })
      }
    }
  }

  deleteFile = (event, attachmentId) => {
    const ticketId = this.props.match.params.id;

    AttachmentService.getAttachmentById(attachmentId, ticketId).then(res => {
      sessionStorage.setItem("fileName", res.data.name)
    })

    const fileName = sessionStorage.getItem("fileName");
    const ticketStatus = sessionStorage.getItem("status");

    const attachmentHistory = {
      action: "File is removed",
      description: `File is removed: ${fileName}`
    };

    if (ticketStatus === "DRAFT") {
      AttachmentService.deleteAttachment(attachmentId, ticketId).then(res => {
        HistoryService.createHistory(attachmentHistory, ticketId).then(res => {
          this.setState({
            ticketHistory: [...this.state.ticketHistory, attachmentHistory]
          });
        });
        attachmentId--;
      });
    }
  }

  add_Comment_History = (ticketId) => {
    const ticketHistory = {
      action: "Ticket is edited",
      description: "Ticket is edited"
    };

    const comment = {
      text: this.state.comment
    }

    HistoryService.createHistory(ticketHistory, ticketId).then(res => {
      this.setState({
        ticketHistory: [...this.state.ticketHistory, ticketHistory]
      });
    });

    if (this.state.comment !== "") {
      CommentService.createComment(comment, ticketId).then(res => {
        this.setState({ createCommentError: [] })
        this.setState({
          ticketComments: [...this.state.ticketComments, comment]
        });
        this.props.history.push(`/tickets/${this.state.id}`)
      }).catch(err => {
        this.setState({ isInvalidComment: true });
        this.setState({ createCommentError: err.response.data })
      })
    } else {
      this.setState({ isInvalidComment: false });
      this.props.history.push(`/tickets/${this.state.id}`);
    }
  }

  handleSaveDraft = () => {
    this.handleSubmitTicket("Draft");
  }

  handleSubmitTicket = (e) => {
    const ticket = {
      category: this.state.category,
      name: this.state.name,
      description: this.state.description,
      urgency: this.state.urgency,
      desiredResolutionDate: this.state.desiredResolutionDate,
      status: this.state.status,
    };

    if (!this.state.isInvalidComment) {
      e === "Draft" ? TicketService.updateTicketAndSaveAsDraft(ticket, this.state.id).then(res => {
        this.setState({ updateTicketError: [] })
        this.add_Comment_History(this.state.id);
      }).catch(err => {
        if (err.response.status === 403) {
          this.setState({ haveAccess: false });
          this.setState({ updateTicketAccessError: err.response.data })
        }
        if (err.response.status === 400) {
          this.setState({ isInvalidTicket: true });
          this.setState({ updateTicketError: err.response.data })
        }
      }) : TicketService.updateTicket(ticket, this.state.id).then(res => {
        this.setState({ updateTicketError: [] })
        this.add_Comment_History(this.state.id);
      }).catch(err => {
        if (err.response.status === 403) {
          this.setState({ haveAccess: false });
          this.setState({ updateTicketAccessError: err.response.data })
        }
        if (err.response.status === 400) {
          this.setState({ isInvalidTicket: true });
          this.setState({ updateTicketError: err.response.data })
        }
      })
    }
    else {
      this.props.history.push(`/tickets/${this.state.id}`);
    }
  }

  viewTicket(id) {
    this.props.history.push(`/tickets/${id}`);
  }

  render() {
    const {
      name,
      category,
      comment,
      description,
      desiredResolutionDate,
      urgency,
    } = this.state;

    return (
      <div className="ticket-creation-form-container" >
        <header className="ticket-form-container__navigation-container">
          <Button onClick={() => this.viewTicket(this.state.id)} variant="contained" color="secondary">
            Ticket Overview
          </Button>
        </header>
        <div className="ticket-creation-form-container__title">
          <Typography variant="h4">{`Edit Ticket(${this.state.id})`}</Typography>
        </div>
        <div className="ticket-creation-form-container__form">
          <div className="inputs-section">
            <div className="ticket-creation-form-container__inputs-section inputs-section__ticket-creation-input ticket-creation-input ticket-creation-input_width200">
              <FormControl>
                <TextField
                  required
                  label="Name"
                  variant="outlined"
                  onChange={this.handleNameChange}
                  id="name-label"
                  value={name}
                />
              </FormControl>
            </div>
            <div className="inputs-section__ticket-creation-input ticket-creation-input ticket-creation-input_width200">
              <FormControl variant="outlined" required>
                <InputLabel shrink htmlFor="category-label">
                  Category
                </InputLabel>
                <Select
                  value={category}
                  label="Category"
                  onChange={this.handleCategoryChange}
                  inputProps={{
                    name: "category",
                    id: "category-label",
                  }}
                >
                  {CATEGORIES_OPTIONS.map((item, index) => {
                    return (
                      <MenuItem value={item.value} key={index}>
                        {item.label}
                      </MenuItem>
                    );
                  })}
                </Select>
              </FormControl>
            </div>
            <div className="inputs-section__ticket-creation-input ticket-creation-input">
              <FormControl variant="outlined" required>
                <InputLabel shrink htmlFor="urgency-label">
                  Urgency
                </InputLabel>
                <Select
                  value={urgency}
                  label="Urgency"
                  onChange={this.handleUrgencyChange}
                  className={"ticket-creation-input_width200"}
                  inputProps={{
                    name: "urgency",
                    id: "urgency-label",
                  }}
                >
                  {URGENCY_OPTIONS.map((item, index) => {
                    return (
                      <MenuItem value={item.value} key={index}>
                        {item.label}
                      </MenuItem>
                    );
                  })}
                </Select>
              </FormControl>
            </div>
          </div>
          <div className="inputs-section-attachment">
            <div className="inputs-section__ticket-creation-input ticket-creation-input ticket-creation-input_width200">
              <FormControl>
                <InputLabel shrink htmlFor="urgency-label">
                  Desired resolution date
                </InputLabel>
                <TextField
                  onChange={this.handleDesiredResolutionDate}
                  label="Desired resolution date"
                  type="date"
                  id="resolution-date"
                  value={desiredResolutionDate}
                  InputLabelProps={{
                    shrink: true,
                  }}
                />
              </FormControl>
            </div>
            <div className="ticket-creation-input">
              <FormControl>
                <Typography variant="caption">Add attachment</Typography>
                <div align="left">
                  <input type="file" onChange={this.handleAttachmentChange} />
                  <button onClick={(e) => this.addFile(e)}>Add File</button>
                </div>
                <div align="left" variant="subtitle1">
                  {this.state.ticketAttachments.map((item, index) => {
                    return (
                      <div key={index}>
                        {item.name}
                        <button key={item.id} onClick={(e) => this.deleteFile(e, item.id)}>Delete File</button>
                      </div>
                    );
                  })}
                </div>
              </FormControl>
            </div>
          </div>
          <div className="inputs-section">
            <FormControl>
              <TextField
                label="Description"
                multiline
                rows={4}
                variant="outlined"
                value={description}
                className="creation-text-field creation-text-field_width680"
                onChange={this.handleDescriptionChange}
              />
            </FormControl>
          </div>
          <div className="inputs-section">
            <FormControl>
              <TextField
                label="Comment"
                multiline
                rows={4}
                variant="outlined"
                value={comment}
                className="creation-text-field creation-text-field_width680"
                onChange={this.handleCommentChange}
              />
            </FormControl>
          </div>
          <section className="submit-button-section">
            <Button variant="contained" onClick={this.handleSaveDraft}>
              Save as Draft
            </Button>
            <Button
              variant="contained" onClick={this.handleSubmitTicket} color="primary">
              Submit
            </Button>
          </section>
          <div align='center'>
            {
              !this.state.haveAccess ?
                <div>
                  {this.state.updateTicketAccessError.info}
                </div>
                :
                ''
            }
          </div>
          {
            this.state.isInvalidTicket ?
              <div>
                {this.state.updateTicketError.map((error) => (
                  <div align='center'>
                    <ul>
                      <li>{error.name}</li>
                      <li>{error.description}</li>
                      <li>{error.desiredResolutionDate}</li>
                    </ul>
                  </div>
                ))}
              </div>
              :
              ''
          }
          {
            this.state.isInvalidAttachment ?
              <div align='center'>
                {this.state.attachmentUploadError}
              </div>
              :
              ''
          }
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
      </div>
    );
  }
}

const TicketEditPageWithRouter = withRouter(TicketEditPage);
export default TicketEditPageWithRouter;