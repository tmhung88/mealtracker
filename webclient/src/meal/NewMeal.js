import React from 'react';
import PropTypes from 'prop-types';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import FormControl from '@material-ui/core/FormControl';
import Input from '@material-ui/core/Input';
import InputLabel from '@material-ui/core/InputLabel';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';
import withStyles from '@material-ui/core/styles/withStyles';
import { BrowserRouter as Router, Route, Link, Switch, withRouter } from "react-router-dom";
import moment from "moment";
import { put, post } from '../api';
import LoadingOverlay from 'react-loading-overlay';

const styles = theme => ({
    main: {
        width: 'auto',
        display: 'block', // Fix IE 11 issue.
        marginLeft: theme.spacing.unit * 2,
        marginRight: theme.spacing.unit * 2,
        [theme.breakpoints.up(600 + theme.spacing.unit * 2 * 2)]: {
            width: 600,
            marginLeft: 'auto',
            marginRight: 'auto',
        },
    },
    paper: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        marginTop: theme.spacing.unit * 3,
        marginBottom: theme.spacing.unit * 3,
        padding: theme.spacing.unit * 2,
        [theme.breakpoints.up(600 + theme.spacing.unit * 3 * 2)]: {
            marginTop: theme.spacing.unit * 6,
            marginBottom: theme.spacing.unit * 6,
            padding: theme.spacing.unit * 3,
        },
    },
    avatar: {
        margin: theme.spacing.unit,
        backgroundColor: theme.palette.secondary.main,
    },
    form: {
        width: '100%', // Fix IE 11 issue.
        marginTop: theme.spacing.unit,
    },
    add: {
        marginTop: theme.spacing.unit * 3,
        marginLeft: theme.spacing.unit * 3,
        paddingLeft: theme.spacing.unit * 4,
        paddingRight: theme.spacing.unit * 4,
    },
    cancel: {
        marginTop: theme.spacing.unit * 3,
    }
});


class NewMeal extends React.Component {
    state = {
        meal: {
            datetime: new Date(),
            calories: 0,
            text: "Meal Info",
        },
        loading: false,
    }
    handleSubmit = async (e) => {
        e.preventDefault();
        this.setState({ saving: true });
        try {
            await post("/api/meals", this.state.meal);
            this.props.history.replace("/meals")
        } finally {
            this.setState({ saving: false });
        }
        console.log("finished");
    };

    render() {
        return (<LoadingOverlay
            active={this.state.loading}
            spinner
        >
            {this.renderContent()}
        </LoadingOverlay>)
    }
    renderContent() {
        const { classes } = this.props;
        return (
            <main className={classes.main}>
                <CssBaseline />
                <Paper className={classes.paper}>
                    <Typography component="h1" variant="h5">
                        New Meal
                    </Typography>
                    <form className={classes.form}>
                        <FormControl margin="normal" required fullWidth>
                            <TextField
                                id="date"
                                label="Date"
                                type="date"
                                value={moment(this.state.meal.datetime).format("YYYY-MM-DD")}
                                onChange={(e) => {
                                    const value = e.currentTarget.value;
                                    console.log(value);
                                    const dateMoment = value ? moment(value) : moment();
                                    this.setState({
                                        meal: {
                                            ...this.state.meal,
                                            datetime: moment(this.state.meal.datetime)
                                                .year(dateMoment.year())
                                                .month(dateMoment.month())
                                                .date(dateMoment.date())
                                        }
                                    })
                                }}
                                className={classes.textField}
                                InputLabelProps={{
                                    shrink: true,
                                }}
                            />
                        </FormControl>
                        <FormControl margin="normal" required fullWidth>
                            <TextField
                                id="time"
                                label="Time"
                                type="time"
                                value={moment(this.state.meal.datetime).format("HH:mm")}
                                onChange={(e) => {
                                    const value = e.currentTarget.value;
                                    console.log(value);
                                    const dateMoment = value ? moment(value, "HH:mm") : moment();
                                    this.setState({
                                        meal: {
                                            ...this.state.meal,
                                            datetime: moment(this.state.meal.datetime)
                                                .hour(dateMoment.hour())
                                                .minute(dateMoment.minute())
                                        }
                                    })
                                }}
                                className={classes.textField}
                                InputLabelProps={{
                                    shrink: true,
                                }}
                                inputProps={{
                                    step: 300, // 5 min
                                }}
                            />
                        </FormControl>
                        <FormControl margin="normal" required fullWidth>
                            <TextField
                                id="text"
                                label="Text"
                                multiline
                                className={classes.textField}
                                margin="normal"
                                value={this.state.meal.text || ""}
                                onChange={e => {
                                    this.setState({
                                        meal: {
                                            ...this.state.meal,
                                            text: e.currentTarget.value,
                                        }
                                    });
                                }}
                            />

                        </FormControl>
                        <FormControl margin="normal" required fullWidth>
                            <TextField
                                id="calories"
                                type="number"
                                label="Calories"
                                className={classes.textField}
                                InputLabelProps={{
                                    shrink: true,
                                }}
                                margin="normal"
                                value={this.state.meal.colories || 0}
                                onChange={e => {
                                    this.setState({
                                        meal: {
                                            ...this.state.meal,
                                            colories: Number.parseInt(e.currentTarget.value, 10),
                                        }
                                    });
                                }}
                            />

                        </FormControl>
                        <Button component={Link} to="/meals"
                            variant="contained"
                            color="secondary"
                            className={classes.cancel}
                        >
                            Cancel
                        </Button>
                        <Button
                            type="submit"
                            variant="contained"
                            color="primary"
                            className={classes.add}
                            onClick={this.handleSubmit}
                        >
                            Add
                        </Button>

                    </form>
                </Paper>
            </main>
        );
    }


}

NewMeal.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default  withRouter(withStyles(styles)(NewMeal));