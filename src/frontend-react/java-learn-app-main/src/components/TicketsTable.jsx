import React from "react";
import PropTypes from "prop-types";
import {
  ButtonGroup,
  Button,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
  TextField,
} from "@material-ui/core";
import { Link } from "react-router-dom";
import { withRouter } from "react-router";
import { TICKETS_TABLE_COLUMNS } from "../constants/tablesColumns";
import TicketService from '../services/TicketService';
import HistoryService from '../services/HistoryService';

class TicketsTable extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      id: this.props.match.params.id,
      page: 0,
      rowsPerPage: 10,
      changeStatusError: [],
      ticketHistory: [],
      history: {}
    };
  }

  handleCancelTicket = (id, previousStatus) => {
    TicketService.changeTicketStatusToCanceled(id).then((res) => {
      if (res.status === 201) {
        this.props.ticketsState({ newStatus: 'CANCELED', ticketId: id });
        HistoryService.createHistoryAfterChangeStatusToCanceled(this.state.history, id, previousStatus).then(res => {
          this.setState({
            ticketHistory: [...this.state.ticketHistory, this.state.history]
          });
        })
      }
    }).catch(err => {
      if (err.response) {
        this.setState({ changeStatusError: err.response.data.info })
      }
    });
  }

  handleSubmitTicket = (id, previousStatus) => {
    TicketService.changeTicketStatusToNew(id).then((res) => {
      if (res.status === 201) {
        this.props.ticketsState({ newStatus: 'NEW', ticketId: id });
        HistoryService.createHistoryAfterChangeStatusToNew(this.state.history, id, previousStatus).then(res => {
          this.setState({
            ticketHistory: [...this.state.ticketHistory, this.state.history]
          });
        })
      }
    }).catch(err => {
      if (err.response) {
        this.setState({ changeStatusError: err.response.data.info })
      }
    });
  }

  handleApproveTicket = (id, previousStatus) => {
    TicketService.changeTicketStatusToApproved(id).then((res) => {
      if (res.status === 201) {
        this.props.ticketsState({ newStatus: 'APPROVED', ticketId: id });
        HistoryService.createHistoryAfterChangeStatusToApproved(this.state.history, id, previousStatus).then(res => {
          this.setState({
            ticketHistory: [...this.state.ticketHistory, this.state.history]
          });
        })
      }
    }).catch(err => {
      if (err.response) {
        this.setState({ changeStatusError: err.response.data.info })
      }
    });
  }

  handleDeclineTicket = (id, previousStatus) => {
    TicketService.changeTicketStatusToDeclined(id).then((res) => {
      if (res.status === 201) {
        this.props.ticketsState({ newStatus: 'DECLINED', ticketId: id });
        HistoryService.createHistoryAfterChangeStatusToDeclined(this.state.history, id, previousStatus).then(res => {
          this.setState({
            ticketHistory: [...this.state.ticketHistory, this.state.history]
          });
        })
      }
    }).catch(err => {
      if (err.response) {
        this.setState({ changeStatusError: err.response.data.info })
      }
    });
  }

  handleAssignTicket = (id, previousStatus) => {
    TicketService.changeTicketStatusToInProgress(id).then((res) => {
      if (res.status === 201) {
        this.props.ticketsState({ newStatus: 'IN_PROGRESS', ticketId: id });
        HistoryService.createHistoryAfterChangeStatusToInProgress(this.state.history, id, previousStatus).then(res => {
          this.setState({
            ticketHistory: [...this.state.ticketHistory, this.state.history]
          });
        })
      }
    }).catch(err => {
      if (err.response) {
        this.setState({ changeStatusError: err.response.data.info })
      }
    });
  }

  handleDoneTicket = (id, previousStatus) => {
    TicketService.changeTicketStatusToDone(id).then((res) => {
      if (res.status === 201) {
        this.props.ticketsState({ newStatus: 'DONE', ticketId: id });
        HistoryService.createHistoryAfterChangeStatusToDone(this.state.history, id, previousStatus).then(res => {
          this.setState({
            ticketHistory: [...this.state.ticketHistory, this.state.history]
          });
        })
      }
    }).catch(err => {
      if (err.response) {
        this.setState({ changeStatusError: err.response.data.info })
      }
    });
  }

  handleChangePage = () => {
    console.log("change page");
  };

  handleChangeRowsPerPage = (event) => {
    this.setState({
      rowsPerPage: +event.target.value,
    });
  };

  render() {
    const { tickets, sortAscCallback, sortDescCallback, searchByIdCallback, searchByNameCallback,
      searchByDesiredDateCallback, searchByUrgencyCallback, searchByStatusCallback } = this.props;
    const { page, rowsPerPage } = this.state;
    const { url } = this.props.match;
    const {
      handleChangePage,
      handleChangeRowsPerPage,
      handleCancelTicket,
      handleSubmitTicket,
      handleApproveTicket,
      handleDeclineTicket,
      handleAssignTicket,
      handleDoneTicket
    } = this;
    const userRole = sessionStorage.getItem("userRole");

    return (
      <Paper>
        <TableContainer>
          <TextField
            onChange={searchByIdCallback}
            id="filled-full-width"
            label="Search"
            style={{ margin: 5, width: "200px" }}
            placeholder="Search by id"
            margin="normal"
            InputLabelProps={{
              shrink: true,
            }}
          />
          <TextField
            onChange={searchByNameCallback}
            id="filled-full-width"
            label="Search"
            style={{ margin: 5, width: "200px" }}
            placeholder="Search by name"
            margin="normal"
            InputLabelProps={{
              shrink: true,
            }}
          />
          <TextField
            onChange={searchByDesiredDateCallback}
            id="filled-full-width"
            label="Search"
            style={{ margin: 5, width: "200px" }}
            placeholder="Search by date"
            margin="normal"
            InputLabelProps={{
              shrink: true,
            }}
          />
          <TextField
            onChange={searchByUrgencyCallback}
            id="filled-full-width"
            label="Search"
            style={{ margin: 5, width: "200px" }}
            placeholder="Search by urgency"
            margin="normal"
            InputLabelProps={{
              shrink: true,
            }}
          />
          <TextField
            onChange={searchByStatusCallback}
            id="filled-full-width"
            label="Search"
            style={{ margin: 5, width: "200px" }}
            placeholder="Search by status"
            margin="normal"
            InputLabelProps={{
              shrink: true,
            }}
          />
          <Table>
            <TableHead>
              <TableRow>
                {TICKETS_TABLE_COLUMNS.map((column) => (
                  <TableCell align={column.align} key={column.id}>
                    <div key={column.id}>
                      <div class="field"><b>{column.label}</b></div>
                      {column.label !== 'Action' ?
                        <div class="up-arrow" onClick={(e) => sortAscCallback(e, column.id)}></div>
                        : ''}
                      {column.label !== 'Action' ?
                        <div class="down-arrow" onClick={(e) => sortDescCallback(e, column.id)}></div>
                        : ''}
                    </div>
                  </TableCell>
                ))}
              </TableRow>
            </TableHead>
            <TableBody>
              {tickets
                .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                .map((row, index) => {
                  return (
                    <TableRow hover role="checkbox" key={index}>
                      {TICKETS_TABLE_COLUMNS.map((column) => {
                        const value = row[column.id];
                        if (column.id === "name") {
                          return (
                            <TableCell key={column.id}>
                              <Link to={`${url}/${row.id}`}>{value}</Link>
                            </TableCell>
                          );
                        }
                        if (column.id === "action") {
                          if (row.status === "DRAFT" && (userRole === "ROLE_MANAGER" || "ROLE_EMPLOYEE")) {
                            return <TableCell align="center" key={column.id}>
                              <ButtonGroup>
                                <Button
                                  onClick={() => handleCancelTicket(row.id, row.status)}
                                  variant="contained"
                                  color="secondary"
                                >
                                  Cancel
                                </Button>
                                <Button
                                  onClick={() => handleSubmitTicket(row.id, row.status)}
                                  variant="contained"
                                  color="primary"
                                >
                                  Submit
                                </Button>
                                <div align="left">
                                  {
                                    this.state.changeStatusError.length > 0 ?
                                      <div>
                                        {this.state.changeStatusError}
                                      </div>
                                      :
                                      ''
                                  }
                                </div>
                              </ButtonGroup>
                            </TableCell>
                          } else if (row.status === "NEW" && userRole === "ROLE_MANAGER") {
                            return <TableCell align="center" key={column.id}>
                              <ButtonGroup>
                                <Button
                                  onClick={() => handleApproveTicket(row.id, row.status)}
                                  variant="contained"
                                  color="secondary"
                                >
                                  Approve
                                </Button>
                                <Button
                                  onClick={() => handleDeclineTicket(row.id, row.status)}
                                  variant="contained"
                                  color="primary"
                                >
                                  Decline
                                </Button>
                                <Button
                                  onClick={() => handleCancelTicket(row.id, row.status)}
                                  variant="contained"
                                  color="primary"
                                >
                                  Cancel
                                </Button>
                              </ButtonGroup>
                              <div align="left">
                                {
                                  this.state.changeStatusError.length > 0 ?
                                    <div>
                                      {this.state.changeStatusError}
                                    </div>
                                    :
                                    ''
                                }
                              </div>
                            </TableCell>
                          } else if (row.status === "APPROVED" && userRole === "ROLE_ENGINEER") {
                            return <TableCell align="center" key={column.id}>
                              <ButtonGroup>
                                <Button
                                  onClick={() => handleAssignTicket(row.id, row.status)}
                                  variant="contained"
                                  color="secondary"
                                >
                                  Assign to me
                                </Button>
                                <Button
                                  onClick={() => handleCancelTicket(row.id, row.status)}
                                  variant="contained"
                                  color="primary"
                                >
                                  Cancel
                                </Button>
                                <div align="left">
                                  {
                                    this.state.changeStatusError.length > 0 ?
                                      <div>
                                        {this.state.changeStatusError}
                                      </div>
                                      :
                                      ''
                                  }
                                </div>
                              </ButtonGroup>
                            </TableCell>
                          } else if (row.status === "DECLINED" && (userRole === "ROLE_MANAGER" || userRole === "ROLE_EMPLOYEE")) {
                            return <TableCell align="center" key={column.id}>
                              <ButtonGroup>
                                <Button
                                  onClick={() => handleCancelTicket(row.id, row.status)}
                                  variant="contained"
                                  color="secondary"
                                >
                                  Cancel
                                </Button>
                                <Button
                                  onClick={() => handleSubmitTicket(row.id, row.status)}
                                  variant="contained"
                                  color="primary"
                                >
                                  Submit
                                </Button>
                                <div align="left">
                                  {
                                    this.state.changeStatusError.length > 0 ?
                                      <div>
                                        {this.state.changeStatusError}
                                      </div>
                                      :
                                      ''
                                  }
                                </div>
                              </ButtonGroup>
                            </TableCell>
                          } else if (row.status === "IN_PROGRESS" && userRole === "ROLE_ENGINEER") {
                            return <TableCell align="center" key={column.id}>
                              <ButtonGroup>
                                <Button
                                  onClick={() => handleDoneTicket(row.id, row.status)}
                                  variant="contained"
                                  color="secondary"
                                >
                                  Done
                                </Button>
                                <div align="left">
                                  {
                                    this.state.changeStatusError.length > 0 ?
                                      <div>
                                        {this.state.changeStatusError}
                                      </div>
                                      :
                                      ''
                                  }
                                </div>
                              </ButtonGroup>
                            </TableCell>
                          } else if (row.status === "DONE" && (userRole === "ROLE_MANAGER" || userRole === "ROLE_EMPLOYEE")) {
                            return <TableCell align="center" key={column.id}>
                              <ButtonGroup>
                                <Button
                                  component={Link}
                                  to={`/feedbacks/${row.id}`}
                                  variant="contained"
                                  color="primary"
                                >
                                  CREATE FEEDBACK
                                </Button>
                                <div align="left">
                                  {
                                    this.state.changeStatusError.length > 0 ?
                                      <div>
                                        {this.state.changeStatusError}
                                      </div>
                                      :
                                      ''
                                  }
                                </div>
                              </ButtonGroup>
                            </TableCell>
                          } else {
                            return <TableCell key={column.id}></TableCell>
                          }
                        } else {
                          return <TableCell key={column.id}>{value}</TableCell>;
                        }
                        // };
                      })}
                    </TableRow>
                  );
                })}
            </TableBody>
          </Table>
        </TableContainer>
        <TablePagination
          rowsPerPageOptions={[10, 25, 100]}
          component="div"
          count={tickets.length}
          rowsPerPage={rowsPerPage}
          page={page}
          onChangePage={handleChangePage}
          onChangeRowsPerPage={handleChangeRowsPerPage}
        />
      </Paper>
    );
  }
}

TicketsTable.propTypes = {
  searchCallback: PropTypes.func,
  tickets: PropTypes.array,
};

const TicketsTableWithRouter = withRouter(TicketsTable);
export default TicketsTableWithRouter;
