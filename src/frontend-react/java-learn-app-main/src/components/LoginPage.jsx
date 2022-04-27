import React from "react";
import { Button, TextField, Typography } from "@material-ui/core";
import AuthenticationService from '../services/AuthenticationService';

class LoginPage extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      nameValue: "",
      passwordValue: "",
      isForbidden: false,
      loginErrors: [],
    };
    this.login = this.login.bind(this);
    this.register = this.register.bind(this);
  }

  handleNameChange = (event) => {
    this.setState({ nameValue: event.target.value });
  };

  handlePasswordChange = (event) => {
    this.setState({ passwordValue: event.target.value });
  };

  register() {
    this.props.history.push(`/register`)
  }

  login() {
    AuthenticationService
      .executeJwtAuthenticationService(this.state.nameValue, this.state.passwordValue)
      .then((response) => {
        AuthenticationService.registerSuccessfulLoginForJwt(this.state.nameValue, response.data.token);
        sessionStorage.setItem("userRole", response.data.role)
        this.props.history.push(`/tickets`);
      }).catch(err => {
        if (err.response) {
          this.setState({ loginErrors: err.response.data });
        }
        if (err.request) {
          this.setState({ isForbidden: true });
        }
      })
  }

  render() {
    return (
      <div className="container">
        <div className="container__title-wrapper">
          <Typography component="h2" variant="h3">
            {this.state.isForbidden ?
              <Typography component="h6" variant="h5">
                Go to register or enter valid credentials
              </Typography> :
              <Typography component="h2" variant="h3">
                Login to the Help Desk
              </Typography>}
          </Typography>
        </div>
        <div className="container__from-wrapper">
          <form>
            <div className="container__inputs-wrapper">
              <div className="form__input-wrapper">
                <TextField
                  onChange={this.handleNameChange}
                  label="User name"
                  variant="outlined"
                  placeholder="User name"
                />
              </div>
              <div className="form__input-wrapper">
                <TextField
                  onChange={this.handlePasswordChange}
                  label="Password"
                  variant="outlined"
                  type="password"
                  placeholder="Password"
                />
              </div>
            </div>
          </form>
        </div>
        <div className="container__button-wrapper">
          <Button
            size="large"
            variant="contained"
            color="primary"
            onClick={this.login}
          >
            Enter
          </Button>
        </div>
        <div className="container__button-wrapper">
          <Button
            size="large"
            variant="contained"
            color="primary"
            onClick={this.register}
          >
            Register
          </Button>
        </div>
        {
          this.state.loginErrors.length > 0 ?

            <div>
              <ol>
                {this.state.loginErrors.map((key) => {
                  return <li>{key} </li>
                })}
              </ol>
            </div>
            :
            ''
        }
      </div>
    );
  }
};

export default LoginPage;
