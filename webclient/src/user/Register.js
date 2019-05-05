import React from 'react';
import PropTypes from 'prop-types';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import FormControl from '@material-ui/core/FormControl';
import FormHelperText from '@material-ui/core/FormHelperText';
import Input from '@material-ui/core/Input';
import InputLabel from '@material-ui/core/InputLabel';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';
import withStyles from '@material-ui/core/styles/withStyles';
import { Link } from '@material-ui/core';
import { Link as RouterLink } from 'react-router-dom'
import validate from "validate.js";
import _ from "lodash";
import { Loading } from '../common/loading/Loading';
import { post } from '../api';

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

class Register extends React.Component {
  state = {
    user: {
      email: "",
      password: "",
    },
    dirty: {
      email: false,
      password: false,
    },
    loading: false,
  }

  constructConstraint(constraints, dirty) {
    _.keys(dirty).forEach(key => {
      if (!dirty[key]) {
        constraints = {
          ...constraints,
          [key]: undefined,
        };
      }
    })

    return constraints;
  }

  validate() {
    const { dirty, user } = this.state;
    const constraints = this.constructConstraint({
      email: {
        email: true,
        presence: true,
      },
      password: {
        presence: true,
        length: {
          minimum: 6,
          message: "must be at least 6 characters"
        }
      }
    }, dirty);

    return validate(user, constraints);
  }

  handleSubmit = async (e) => {
    e.preventDefault();
    this.setState({ dirty: {} }, async () => {
      const validationResult = this.validate();
      if (validationResult) {
        return;
      }

      this.setState({ loading: true });
      try {
        await post("/api/users/register", this.state.user);
        this.props.history.replace("/users/login");
      } finally {
        this.setState({ loading: false });
      }
    })
  }

  handleFieldChange(fieldName, value) {
    this.setState({
      user: {
        ...this.state.user,
        [fieldName]: value,
      },
      dirty: {
        ...this.state.dirty,
        [fieldName]: true,
      }
    });
  }
  render() {
    return <Loading active={this.state.loading}>
      {this.renderContent()}
    </Loading>
  }

  renderContent() {
    const { classes } = this.props;
    const { user } = this.state;
    const validationResult = this.validate() || {};
    return (
      <main className={classes.main}>
        <CssBaseline />
        <Paper className={classes.paper}>
          <Typography component="h1" variant="h5">
            New User
        </Typography>
          <form className={classes.form}>
            <FormControl margin="normal" required fullWidth error={!!validationResult.email}>
              <InputLabel htmlFor="email">Email Address</InputLabel>
              <Input id="email" name="email" autoComplete="email" autoFocus value={user.email}
                onChange={e => this.handleFieldChange("email", e.currentTarget.value)}
              />
              <FormHelperText>{validationResult.email}</FormHelperText>
            </FormControl>
            <FormControl margin="normal" required fullWidth error={!!validationResult.password}>
              <InputLabel htmlFor="password">Password</InputLabel>
              <Input name="password" type="password" id="password" autoComplete="new-password" value={user.password}
                onChange={e => this.handleFieldChange("password", e.currentTarget.value)}
              />
              <FormHelperText>{validationResult.password}</FormHelperText>
            </FormControl>
            <Button
              type="submit"
              fullWidth
              variant="contained"
              color="primary"
              className={classes.submit}
              onClick={this.handleSubmit}
            >
              Register
          </Button>
            <Link className={classes.link} component={RouterLink} to="/users/login">
              Or Login
            </Link>
          </form>
        </Paper>
      </main>
    );
  }
}

Register.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(Register);