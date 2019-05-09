import React, { Fragment } from "react";
import PropTypes from "prop-types";
import Avatar from "@material-ui/core/Avatar";
import Button from "@material-ui/core/Button";
import CssBaseline from "@material-ui/core/CssBaseline";
import FormControl from "@material-ui/core/FormControl";
import Input from "@material-ui/core/Input";
import InputLabel from "@material-ui/core/InputLabel";
import LockOutlinedIcon from "@material-ui/icons/LockOutlined";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import withStyles from "@material-ui/core/styles/withStyles";
import { Loading } from "../common/loading/Loading";
import { Link } from "@material-ui/core";
import { Link as RouterLink } from "react-router-dom"
import ValidationForm from "../common/form/ValidationForm";
import FormHelperText from "@material-ui/core/FormHelperText";
import { withPage } from "../core/components/AppPage";
import { BadRequestError, UnauthenticatedError } from "../core/api";
import { Rights } from "../core/userSession";

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

export class Login extends React.Component {
  state = {
    form: { email: "", password: "" },
    loading: false,
  }

  navigateToProperPage() {
    if (this.props.userSession.hasRight(Rights.MyMeal)) {
      this.props.history.replace("/meals");
    } else if (this.props.userSession.hasRight(Rights.UserManagement)) {
      this.props.history.replace("/users");
    } else {
      this.props.history.replace("/meals/all");
    }
  }
  handleSubmit = async (e) => {
    e.preventDefault();

    try {
      this.setState({ loading: true })
      const response = await this.props.api.post("/v1/session", this.state.form);
      const json = await response.json();
      this.props.userSession.setToken(json.token);
      this.navigateToProperPage();

    } catch (error) {
      if (error instanceof BadRequestError) {
        this.setState({
          serverValidationError: error.body.error,
        })
      } else if (error instanceof UnauthenticatedError) {
        this.setState({
          serverValidationError: {
            message: "Wrong Email or Password",
          }
        })
      } else {
        this.props.handleError(error);
      }
    } finally {
      this.setState({ loading: false })
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
          <Avatar className={classes.avatar}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign in
          </Typography>
          <form className={classes.form}>
            <ValidationForm
              serverValidationError={this.state.serverValidationError}
              constraints={{
                email: {
                  email: true,
                  presence: { allowEmpty: false },
                },
              }}
              data={this.state.form}
              onDataChange={(data) => this.setState({ form: data })}
            >
              {({ onFieldChange, data, isValid, validationFields, validationMessage }) => {
                return (<Fragment>
                  <FormControl margin="normal" required fullWidth error={!!validationFields.email}>
                    <InputLabel htmlFor="email">Email Address</InputLabel>
                    <Input id="email" name="email" autoComplete="email" autoFocus
                      value={data.email}
                      onChange={e => onFieldChange("email", e.currentTarget.value)} />
                    <FormHelperText>{validationFields.email}</FormHelperText>
                  </FormControl>
                  <FormControl margin="normal" required fullWidth error={!!validationFields.password}>
                    <InputLabel htmlFor="password">Password</InputLabel>
                    <Input name="password" type="password" id="password" autoComplete="current-password" value={data.password}
                      onChange={e => onFieldChange("password", e.currentTarget.value)} />

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
                    Sign in
                  </Button>

                  <Link className={classes.link} component={RouterLink} to="/users/register">
                    Register new User
                  </Link>
                </Fragment>)
              }}
            </ValidationForm>

          </form>
        </Paper>
      </main>
    );
  }
}

Login.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withPage(withStyles(styles)(Login));