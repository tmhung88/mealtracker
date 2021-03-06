import React, { Fragment } from "react";
import Button from "@material-ui/core/Button";
import CssBaseline from "@material-ui/core/CssBaseline";
import FormControl from "@material-ui/core/FormControl";
import FormHelperText from "@material-ui/core/FormHelperText";
import Input from "@material-ui/core/Input";
import InputLabel from "@material-ui/core/InputLabel";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import withStyles from "@material-ui/core/styles/withStyles";
import { Link } from "@material-ui/core";
import { Link as RouterLink } from "react-router-dom"
import { Loading } from "../common/loading/Loading";

import ValidationForm from "../common/form/ValidationForm";
import { withPage } from "../core/components/AppPage";
import { BadRequestError } from "../core/api";
import { ApiUrl } from '../constants/ApiUrl';
import { Pages, getDefaultPage } from "../constants/Pages";

const styles = theme => ({
  main: {
    width: "auto",
    display: "block", // Fix IE 11 issue.
    marginLeft: theme.spacing.unit * 3,
    marginRight: theme.spacing.unit * 3,
    [theme.breakpoints.up(400 + theme.spacing.unit * 3 * 2)]: {
      width: 400,
      marginLeft: "auto",
      marginRight: "auto",
    },
  },
  paper: {
    marginTop: theme.spacing.unit * 8,
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    padding: `${theme.spacing.unit * 2}px ${theme.spacing.unit * 3}px ${theme.spacing.unit * 3}px`,
  },
  avatar: {
    margin: theme.spacing.unit,
    backgroundColor: theme.palette.secondary.main,
  },
  form: {
    width: "100%", // Fix IE 11 issue.
    marginTop: theme.spacing.unit,
  },
  submit: {
    marginTop: theme.spacing.unit * 3,
  },
  link: {
    marginTop: theme.spacing.unit * 1,
    display: "inline-block",
  }
});

export class Register extends React.Component {
  state = {
    user: {
      email: "",
      fullName: "",
      password: "",
    },
    loading: false,
    serverValidationError: null,
  }

  handleSubmit = async (e) => {
    e.preventDefault();
    this.setState({ loading: true });
    try {
      await this.props.api.post(ApiUrl.USERS, this.state.user);
      const response = await this.props.api.login(ApiUrl.SESSION, {
        email: this.state.user.email,
        password: this.state.user.password,
      });

      const json = await response.json();
      this.props.userSession.setToken(json.data.accessToken);
      this.props.history.replace(getDefaultPage(this.props.userSession));
      this.props.showSuccessMessage("Register successfully");
    } catch (error) {
      if (error instanceof BadRequestError) {
        this.setState({
          serverValidationError: error.body.error,
        })
      } else {
        this.props.handleError(error);
      }
    } finally {
      this.setState({ loading: false });
    }
  }

  render() {
    return <Loading active={this.state.loading}>
      {this.renderContent()}
    </Loading>
  }

  renderContent() {
    const { classes } = this.props;
    return (
        <main className={classes.main}>
          <CssBaseline />
          <Paper className={classes.paper}>
            <Typography component="h1" variant="h5">
              Register new User
            </Typography>
            <form className={classes.form}>
              <ValidationForm
                  serverValidationError={this.state.serverValidationError}
                  constraints={{
                    email: {
                      email: true,
                      presence: { allowEmpty: false },
                      length: {
                        minimum: 5,
                        maximum: 200
                      }
                    },
                    fullName: {
                      presence: { allowEmpty: false },
                      length: {
                        minimum: 5,
                        maximum: 200
                      }
                    },
                    password: {
                      presence: { allowEmpty: false },
                      length: {
                        minimum: 5,
                        maximum: 100
                      }
                    }
                  }}
                  data={this.state.user}
                  onDataChange={(user) => this.setState({ user: user })}
              >
                {({ onFieldChange, data, isValid, validationFields, validationMessage }) => {
                  return <Fragment>
                    <FormControl margin="normal" required fullWidth error={!!validationFields.email}>
                      <InputLabel htmlFor="email">Email Address</InputLabel>
                      <Input id="email" name="email"
                             autoComplete="email"
                             autoFocus
                             value={data.email}
                             onChange={e => onFieldChange("email", e.currentTarget.value)}
                      />
                      <FormHelperText>{validationFields.email}</FormHelperText>
                    </FormControl>
                    <FormControl margin="normal" required fullWidth error={!!validationFields.fullName}>
                      <InputLabel htmlFor="fullName">Full Name</InputLabel>
                      <Input name="fullName" id="fullName" value={data.fullName}
                             onChange={e => onFieldChange("fullName", e.currentTarget.value)}
                      />
                      <FormHelperText>{validationFields.fullName}</FormHelperText>
                    </FormControl>

                    <FormControl margin="normal" required fullWidth error={!!validationFields.password}>
                      <InputLabel htmlFor="password">Password</InputLabel>
                      <Input name="password" type="password" id="password" autoComplete="new-password" value={data.password}
                             onChange={e => onFieldChange("password", e.currentTarget.value)}
                      />
                      <FormHelperText>{validationFields.password}</FormHelperText>
                    </FormControl>
                    {validationMessage ? <FormHelperText error>{validationMessage}</FormHelperText> : undefined}
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        color="primary"
                        className={classes.submit}
                        onClick={async (e) => {
                          e.preventDefault();
                          if (!isValid()) {
                            return;
                          }

                          await this.handleSubmit(e);
                        }}
                    >
                      Register
                    </Button>
                  </Fragment>
                }}
              </ValidationForm>
              <Link className={classes.link} component={RouterLink} to={Pages.LOGIN}>
                Or Login
              </Link>
            </form>
          </Paper>
        </main>
    );
  }
}

export default withPage(withStyles(styles)(Register));
