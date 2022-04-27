import React from "react";
import TabPanel from "./TabPanel";
import TicketsTable from "./TicketsTable";
import { AppBar, Button, Tab, Tabs } from "@material-ui/core";
import { Switch, Route } from "react-router-dom";
import { withRouter } from "react-router";
import TicketInfoWithRouter from "./TicketInfo";
import TicketService from '../services/TicketService';

function a11yProps(index) {
  return {
    id: `full-width-tab-${index}`,
    "aria-controls": `full-width-tabpanel-${index}`,
  };
}
class MainPage extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      prop: 42,
      tabValue: 0,
      myTickets: [],
      allTickets: [],
      filteredTickets: [],
      searchError: [],
      createTicketAccess: "",
    };
    this.addTicket = this.addTicket.bind(this);
    this.handleLogout = this.handleLogout.bind(this);
  }

  componentDidMount() {
    TicketService.getAllTickets().then((res) => {
      this.setState({ allTickets: res.data })
    });

    TicketService.getMyTickets().then((res) => {
      this.setState({ myTickets: res.data })
    })
  }

  addTicket() {
    const userRole = sessionStorage.getItem("userRole");

    if (userRole !== "ROLE_ENGINEER") {
      this.props.history.push('/add-ticket/_add');
    } else {
      this.setState({ createTicketAccess: "You can't have access to create ticket" });
    }
  }

  handleLogout = () => {
    window.location.href = "/";
  };

  handleTabChange = (event, value) => {
    this.setState({
      tabValue: value,
      filteredTickets: []
    });
  };

  handleSortTicketAsc = (event, field) => {
    TicketService.getAllSortedTickets(field).then((res) => {
      this.setState({ allTickets: res.data })
    });

    TicketService.getMySortedTickets(field).then((res) => {
      this.setState({ myTickets: res.data })
    });
  }

  handleSortTicketDesc = (event, field) => {
    TicketService.getAllDescSortedTickets(field).then((res) => {
      this.setState({ allTickets: res.data })
    });

    TicketService.getMyDescSortedTickets(field).then((res) => {
      this.setState({ myTickets: res.data })
    });
  }

  handleSearchTicketById = (event) => {
    const { tabValue } = this.state;

    if (tabValue === 0) {
      TicketService.getMyTicketsSearchedById(event.target.value).then((res) => {
        this.setState({ filteredTickets: res.data })
      });
    }

    if (tabValue === 1) {
      TicketService.getAllTicketsSearchedById(event.target.value).then((res) => {
        this.setState({ filteredTickets: res.data })
      });
    }
  }

  handleSearchTicketByName = (event) => {
    const { tabValue } = this.state;

    if (tabValue === 0) {
      TicketService.getMyTicketsSearchedByName(event.target.value).then((res) => {
        this.setState({ filteredTickets: res.data });
        this.setState({ searchError: [] })
      }).catch(err => {
        if (err.response) {
          this.setState({ searchError: err.response.data })
        }
      })
    };

    if (tabValue === 1) {
      TicketService.getAllTicketsSearchedByName(event.target.value).then((res) => {
        this.setState({ filteredTickets: res.data });
        this.setState({ searchError: [] })
      }).catch(err => {
        if (err.response) {
          this.setState({ searchError: err.response.data })
        }
      })
    }
  }

  handleSearchTicketByDesiredDate = (event) => {
    const { tabValue } = this.state;

    if (tabValue === 0) {
      TicketService.getMyTicketsSearchedByDesiredDate(event.target.value).then((res) => {
        this.setState({ filteredTickets: res.data })
      })
    };

    if (tabValue === 1) {
      TicketService.getAllTicketsSearchedByDesiredDate(event.target.value).then((res) => {
        this.setState({ filteredTickets: res.data })
      })
    }
  }

  handleSearchTicketByUrgency = (event) => {
    const { tabValue } = this.state;

    if (tabValue === 0) {
      TicketService.getMyTicketsSearchedByUrgency(event.target.value).then((res) => {
        this.setState({ filteredTickets: res.data });
        this.setState({ searchError: [] })
      }).catch(err => {
        if (err.response) {
          this.setState({ searchError: err.response.data })
        }
      })
    };

    if (tabValue === 1) {
      TicketService.getAllTicketsSearchedByUrgency(event.target.value).then((res) => {
        this.setState({ filteredTickets: res.data })
        this.setState({ searchError: [] })
      }).catch(err => {
        if (err.response) {
          this.setState({ searchError: err.response.data })
        }
      })
    }
  }

  handleSearchTicketByStatus = (event) => {
    const { tabValue } = this.state;

    if (tabValue === 0) {
      TicketService.getMyTicketsSearchedByStatus(event.target.value).then((res) => {
        this.setState({ filteredTickets: res.data })
        this.setState({ searchError: [] })
      }).catch(err => {
        if (err.response) {
          this.setState({ searchError: err.response.data })
        }
      })
    };

    if (tabValue === 1) {
      TicketService.getAllTicketsSearchedByStatus(event.target.value).then((res) => {
        this.setState({ filteredTickets: res.data })
        this.setState({ searchError: [] })
      }).catch(err => {
        if (err.response) {
          this.setState({ searchError: err.response.data })
        }
      })
    }
  }

  handleTicketState = (e) => {

    if (this.state.tabValue === 0) {
      let result = this.state.myTickets;
      result = result.map(todo => {
        if (todo.id === e.ticketId) todo.status = e.newStatus;
        return todo;
      })
      this.setState({ myTickets: result })
    }

    if (this.state.tabValue === 1) {
      let result = this.state.allTickets;
      result = result.map(todo => {
        if (todo.id === e.ticketId) todo.status = e.newStatus;
        return todo;
      })
      this.setState({ allTickets: result })
    }
  }

  render() {
    const { allTickets, filteredTickets, myTickets, tabValue } = this.state;
    const { path } = this.props.match;
    const { handleSortTicketAsc, handleSortTicketDesc, handleSearchTicketById, handleSearchTicketByName,
      handleSearchTicketByDesiredDate, handleSearchTicketByUrgency, handleSearchTicketByStatus, handleTicketState } = this;

    return (
      <>
        <Switch>
          <Route exact path={path}>
            <div className="buttons-container">
              <Button
                onClick={this.addTicket}
                variant="contained"
                color="primary"
              >
                Create Ticket
              </Button>
              <div align="center">
                {
                  this.state.createTicketAccess.length > 0 ?
                    <div>
                      {this.state.createTicketAccess}
                    </div>
                    :
                    ''
                }
                {
                  this.state.searchError.length > 0 ?
                    <div>
                      {this.state.searchError}
                    </div>
                    :
                    ''
                }
              </div>
              <Button
                onClick={this.handleLogout}
                variant="contained"
                color="secondary"
              >
                Logout
              </Button>
            </div>
            <div className="table-container">
              <AppBar position="static">
                <Tabs
                  variant="fullWidth"
                  onChange={this.handleTabChange}
                  value={tabValue}
                >
                  <Tab label="My tickets" {...a11yProps(0)} />
                  <Tab label="All tickets" {...a11yProps(1)} />
                </Tabs>
                <TabPanel value={tabValue} index={0}>
                  <TicketsTable
                    sortAscCallback={handleSortTicketAsc}
                    sortDescCallback={handleSortTicketDesc}
                    searchByIdCallback={handleSearchTicketById}
                    searchByNameCallback={handleSearchTicketByName}
                    searchByDesiredDateCallback={handleSearchTicketByDesiredDate}
                    searchByUrgencyCallback={handleSearchTicketByUrgency}
                    searchByStatusCallback={handleSearchTicketByStatus}
                    ticketsState={handleTicketState}
                    tickets={
                      filteredTickets.length ? filteredTickets : myTickets
                    }
                  />
                </TabPanel>
                <TabPanel value={tabValue} index={1}>
                  <TicketsTable
                    sortAscCallback={handleSortTicketAsc}
                    sortDescCallback={handleSortTicketDesc}
                    searchByIdCallback={handleSearchTicketById}
                    searchByNameCallback={handleSearchTicketByName}
                    searchByDesiredDateCallback={handleSearchTicketByDesiredDate}
                    searchByUrgencyCallback={handleSearchTicketByUrgency}
                    searchByStatusCallback={handleSearchTicketByStatus}
                    ticketsState={handleTicketState}
                    tickets={
                      filteredTickets.length ? filteredTickets : allTickets
                    }
                  />
                </TabPanel>
              </AppBar>
            </div>
          </Route>
          <Route path={`${path}/:ticketId`}>
            <TicketInfoWithRouter />
          </Route>
        </Switch>
      </>
    );
  }
}

const MainPageWithRouter = withRouter(MainPage);
export default MainPageWithRouter;
