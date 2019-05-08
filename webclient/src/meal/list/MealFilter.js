import React from "react";
import withStyles from "@material-ui/core/styles/withStyles";
import FormControl from "@material-ui/core/FormControl";
import TextField from "@material-ui/core/TextField";
import Grid from "@material-ui/core/Grid";
import { Button } from "@material-ui/core";
import moment from "moment";

const styles = {

}

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

    render() {
        const { classes } = this.props;
        const {fromDate, toDate, fromTime, toTime} = this.state;
        return (<main className={classes.main}>
            <Grid container spacing={24}>
                <Grid item xs={12} sm={6}>
                    <FormControl margin="normal" required fullWidth>
                        <TextField
                            id="from-date"
                            label="From Date"
                            type="date"
                            value={fromDate}
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
                    <FormControl margin="normal" required fullWidth>
                        <TextField
                            id="to-date"
                            label="To Date"
                            type="date"
                            value={toDate}
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
                <Grid item xs={12} sm={6}>
                    <FormControl margin="normal" required fullWidth>
                        <TextField
                            id="from-time"
                            label="From Time"
                            type="time"
                            value={fromTime}
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
                    <FormControl margin="normal" required fullWidth>
                        <TextField
                            id="to-time"
                            label="To Time"
                            type="time"
                            value={toTime}
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
                <Grid item xs={12} >
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