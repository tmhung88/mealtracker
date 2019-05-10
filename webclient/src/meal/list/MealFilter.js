import React from "react";
import withStyles from "@material-ui/core/styles/withStyles";
import FormControl from "@material-ui/core/FormControl";
import TextField from "@material-ui/core/TextField";
import Grid from "@material-ui/core/Grid";
import { Button } from "@material-ui/core";
import moment from "moment";
import { DateTimeHelper } from "../../datetimeHelper";

const styles = theme => ({
    presetFilterButton : {
        marginLeft: theme.spacing.unit,
        margiRight: theme.spacing.unit,
    },
    prestFilterButtonsContainer: {
        textAlign: "right",
    }
})

export class MealFilter extends React.Component {
    state = {
        filter: this.props.filter || {},
    }

    changeField(field, value) {
        return this.setState({
            filter: {
                ...this.state.filter,
                [field]: value,
            }
        })
    }

    setToday=()=>{
        this.setState({
            filter: {
                ...this.state.filter,
                fromDate: moment().format(DateTimeHelper.DATE_FORMAT),
                toDate: undefined,
            }
        }, ()=>{
            this.props.onFilter(this.state.filter);
        })
    }

    setYesterday=()=>{
        this.setState({
            filter: {
                ...this.state.filter,
                fromDate: moment().subtract(1,"day").format(DateTimeHelper.DATE_FORMAT),
                toDate: moment().format(DateTimeHelper.DATE_FORMAT),
            }
        }, ()=>{
            this.props.onFilter(this.state.filter);
        })
    }

    setLunchTime=()=>{
        this.setState({
            filter: {
                ...this.state.filter,
                fromTime: "11:00",
                toTime: "14:00",
            }
        }, ()=>{
            this.props.onFilter(this.state.filter);
        })
    }

    setDinnerTime=()=>{
        this.setState({
            filter: {
                ...this.state.filter,
                fromTime: "18:00",
                toTime: "21:00",
            }
        }, ()=>{
            this.props.onFilter(this.state.filter);
        })
    }

    setWholeTime=()=>{
        this.setState({
            filter: {
                ...this.state.filter,
                fromTime: undefined,
                toTime: undefined,
            }
        }, ()=>{
            this.props.onFilter(this.state.filter);
        })
    }

    render() {
        const { classes } = this.props;
        const {fromDate, toDate, fromTime, toTime} = this.state.filter;
        return (<main className={classes.main}>
            <Grid container spacing={24}>
                <Grid item xs={12} sm={6}>
                    <FormControl required fullWidth margin="dense">
                        <TextField
                            id="from-date"
                            label="From Date"
                            type="date"
                            value={fromDate || ""}
                            onChange={(e) => {
                                this.changeField("fromDate", e.currentTarget.value)
                            }}
                            className={classes.textField}
                            InputLabelProps={{
                                shrink: true,
                            }}
                        />
                    </FormControl>
                </Grid>
                <Grid item xs={12} sm={6}>
                    <FormControl required fullWidth margin="dense">
                        <TextField
                            id="to-date"
                            label="To Date"
                            type="date"
                            value={toDate || ""}
                            onChange={(e) => {
                                this.changeField("toDate", e.currentTarget.value)
                            }}
                            className={classes.textField}
                            InputLabelProps={{
                                shrink: true,
                            }}
                        />
                    </FormControl>
                </Grid>
                <Grid className={classes.prestFilterButtonsContainer} item xs={12} >
                    <Button className={classes.presetFilterButton} onClick={this.setToday}
                        name="today-filter"
                        color="secondary"
                        variant="contained"
                    >Today</Button>

                    <Button  className={classes.presetFilterButton} onClick={this.setYesterday}
                        name="yesterday-filter"
                        color="secondary"
                        variant="contained"
                    >Yesterday</Button>
                </Grid>
                <Grid item xs={12} sm={6}>
                    <FormControl required fullWidth margin="dense">
                        <TextField
                            id="from-time"
                            label="From Time"
                            type="time"
                            value={fromTime || ""}
                            onChange={(e) => {
                                this.changeField("fromTime", e.currentTarget.value)
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
                    <FormControl required fullWidth margin="dense">
                        <TextField
                            id="to-time"
                            label="To Time"
                            type="time"
                            value={toTime || ""}
                            onChange={(e) => {
                                this.changeField("toTime", e.currentTarget.value)
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
                <Grid  className={classes.prestFilterButtonsContainer} item xs={12} >
                    <Button className={classes.presetFilterButton} onClick={this.setLunchTime}
                        name="lunch-filter"
                        color="secondary"
                        variant="contained"
                    >Lunch</Button>

                    <Button  className={classes.presetFilterButton} onClick={this.setDinnerTime}
                        name="dinner-filter"
                        color="secondary"
                        variant="contained"
                    >Dinner</Button>

                    <Button  className={classes.presetFilterButton} onClick={this.setWholeTime}
                        name="whole-time-filter"
                        color="secondary"
                        variant="contained"
                    >Whole Time</Button>
                </Grid>
                <Grid item xs={12} sm={6}>
                    <Button onClick={() => this.props.onFilter(this.state.filter)}
                        name="filter"
                        color="primary"
                        variant="contained"
                    >Filter</Button>
                </Grid>
                
            </Grid>

        </main>);
    }
}

export default withStyles(styles)(MealFilter);