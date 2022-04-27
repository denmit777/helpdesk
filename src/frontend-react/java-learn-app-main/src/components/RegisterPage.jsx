import React from "react";
import { Button, TextField, Typography } from "@material-ui/core";
import AuthenticationService from '../services/AuthenticationService';

class RegisterPage extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      nameValue: "",
      passwordValue: "",
      registerErrors: []
    };
    this.signIn = this.signIn.bind(this);
  }

  handleNameChange = (event) => {
    this.setState({ nameValue: event.target.value });
  };

  handlePasswordChange = (event) => {
    this.setState({ passwordValue: event.target.value });
  };

  signIn() {
    AuthenticationService
      .createUser(this.state.nameValue, this.state.passwordValue)
      .then((response) => {
        this.props.history.push('/')
      })
      .catch(err => {
        if (err.response) {
          this.setState({ registerErrors: err.response.data });
        }
      })
  }

  render() {
    return (
      <div className="container">
        <div className="container__title-wrapper">
          <Typography component="h2" variant="h3">
            Login to the Help Desk
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
            onClick={this.signIn}
          >
            Enter
          </Button>
        </div>
        {
          this.state.registerErrors.length > 0 ?

            <div>
              <ol>
                {this.state.registerErrors.map((key) => {
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

export default RegisterPage;