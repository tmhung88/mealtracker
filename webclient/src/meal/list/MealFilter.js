import React from "react";
import withStyles from "@material-ui/core/styles/withStyles";
import FormControl from "@material-ui/core/FormControl";
import TextField from "@material-ui/core/TextField";
import Grid from "@material-ui/core/Grid";
import { Button } from "@material-ui/core";
import moment from "moment";

const styles = {

}

class MealFilter extends React.Component {
    state = {
        filter: this.props.filter || {},
    }

    changeDate(field, value) {
        if (!value) {
            return this.setState({
                filter: {
                    ...this.state.filter,
                    [field]: null,
                }
            })
        }
        const dateMoment = moment(value);
        this.setState({
            filter: {
                ...this.state.filter,
                [field]: moment()
                    .year(dateMoment.year())
                    .month(dateMoment.month())
                    .date(dateMoment.date())
            }
        })
    }

    changeTime(field, value) {
        if (!value) {
            return this.setState({
                filter: {
                    ...this.state.filter,
                    [field]: null,
                }
            })
        }
        const dateMoment = moment(value,"HH:mm");
        this.setState({
            filter: {
                ...this.state.filter,
                [field]: moment()
                    .hour(dateMoment.hour())
                    .minute(dateMoment.minute())
            }
        })
    }

    getDate(field) {
        return this.state.filter[field] ? this.state.filter[field].format("YYYY-MM-DD") : null;
    }

    getTime(field) {
        return this.state.filter[field] ? this.state.filter[field].format("HH:mm") : null;
    }

    render() {
        const { classes } = this.props;
        return (<main className={classes.main}>
            <Grid container spacing={24}>
                <Grid item xs={12} sm={6}>
                    <FormControl margin="normal" required fullWidth>
                        <TextField
                            id="from-date"
                            label="From Date"
                            type="date"
                            value={this.getDate("fromDate")}
                            onChange={(e) => {
                                this.changeDate("fromDate", e.currentTarget.value)
                            }}
                            className={classes.textField}
                            InputLabelProps={{
                                shrink: true,
                            }}
                        />
                    </FormControl>
                </Grid>
                <Grid item xs={12} sm={6}>
                    <FormControl margin="normal" required fullWidth>
                        <TextField
                            id="to-date"
                            label="To Date"
                            type="date"
                            value={this.getDate("toDate")}
                            onChange={(e) => {
                                this.changeDate("toDate", e.currentTarget.value)
                            }}
                            className={classes.textField}
                            InputLabelProps={{
                                shrink: true,
                            }}
                        />
                    </FormControl>
                </Grid>
                <Grid item xs={12} sm={6}>
                    <FormControl margin="normal" required fullWidth>
                        <TextField
                            id="from-time"
                            label="From Time"
                            type="time"
                            value={this.getTime("fromTime")}
                            onChange={(e) => {
                                this.changeTime("fromTime", e.currentTarget.value)
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
                </Grid>
                <Grid item xs={12} sm={6}>
                    <FormControl margin="normal" required fullWidth>
                        <TextField
                            id="to-time"
                            label="To Time"
                            type="time"
                            value={this.getTime("toTime")}
                            onChange={(e) => {
                                this.changeTime("toTime", e.currentTarget.value)
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
                </Grid>
                <Grid item xs={12} >
                    <Button onClick={() => this.props.onFilter(this.state.filter)}
                        color="primary"
                        variant="contained"
                    >Filter</Button>
                </Grid>
            </Grid>

        </main>);
    }
}

export default withStyles(styles)(MealFilter);