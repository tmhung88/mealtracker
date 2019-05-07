import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import FormControl from '@material-ui/core/FormControl';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import Input from '@material-ui/core/Input';
import InputLabel from '@material-ui/core/InputLabel';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';
import withStyles from '@material-ui/core/styles/withStyles';
import { post, setToken, BadRequestError } from '../api';
import { withRouter } from "react-router-dom";
import { Loading } from '../common/loading/Loading';
import userSession, { Rights } from '../userSession';
import { Link } from '@material-ui/core';
import { Link as RouterLink } from 'react-router-dom'
import ValidationForm from '../common/form/ValidationForm';
import FormHelperText from '@material-ui/core/FormHelperText';

const styles = theme => ({
  main: {
    width: 'auto',
    display: 'block', // Fix IE 11 issue.
    marginLeft: theme.spacing.unit * 3,
    marginRight: theme.spacing.unit * 3,
    [theme.breakpoints.up(400 + theme.spacing.unit * 3 * 2)]: {
      width: 400,
      marginLeft: 'auto',
      marginRight: 'auto',
    },
  },
  paper: {
    marginTop: theme.spacing.unit * 8,
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    padding: `${theme.spacing.unit * 2}px ${theme.spacing.unit * 3}px ${theme.spacing.unit * 3}px`,
  },
  avatar: {
    margin: theme.spacing.unit,
    backgroundColor: theme.palette.secondary.main,
  },
  form: {
    width: '100%', // Fix IE 11 issue.
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

class SignIn extends React.Component {
  state = {
    form: { username: "", password: "" },
    loading: false,
  }

  navigateToProperPage() {
    if (userSession.hasRight(Rights.MyMeal)) {
      this.props.history.replace("/meals");
    } else if (userSession.hasRight(Rights.UserManagement)) {
      this.props.history.replace("/users");
    } else {
      this.props.history.replace("/meals/all");
    }
  }
  handleSubmit = async (e) => {
    e.preventDefault();
    try {
      this.setState({ loading: true })
      const response = await post("/api/users/login", this.state.form);
      const json = await response.json();
      setToken(json.token);
      this.navigateToProperPage();
    } catch (error) {
      if (error instanceof BadRequestError) {
        console.log("BadRequestError", error.body.error.errorFields);
        this.setState({
          serverErrorFields: error.body.error.errorFields,
        })
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
    const { form } = this.state;
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
              serverErrorFields={this.state.serverErrorFields}
              constraints={{
                username: {
                  email: true,
                  presence: true,
                },
              }}
              data={this.state.form}
              onDataChange={(data) => this.setState({ form: data })}
            >
              {({ onFieldChange, data, isValid, validationResult }) => {
                return (<Fragment>
                  <FormControl margin="normal" required fullWidth error={!!validationResult.username}>
                    <InputLabel htmlFor="email">Email Address</InputLabel>
                    <Input id="email" name="email" autoComplete="email" autoFocus
                      value={data.username}
                      onChange={e => onFieldChange("username", e.currentTarget.value)} />
                    <FormHelperText>{validationResult.username}</FormHelperText>
                  </FormControl>
                  <FormControl margin="normal" required fullWidth error={!!validationResult.password}>
                    <InputLabel htmlFor="password">Password</InputLabel>
                    <Input name="password" type="password" id="password" autoComplete="current-password" value={data.password}
                      onChange={e => onFieldChange("password", e.currentTarget.value)} />

                  </FormControl>
                  <FormControlLabel
                    control={<Checkbox value="remember" color="primary" />}
                    label="Remember me"
                  />
                  <Button
                    type="submit"
                    fullWidth
                    variant="contained"
                    color="primary"
                    className={classes.submit}
                    onClick={(e) => {
                      e.preventDefault();
                      if (!isValid()) {
                        return;
                      }

                      this.handleSubmit(e);
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

SignIn.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withRouter(withStyles(styles)(SignIn));