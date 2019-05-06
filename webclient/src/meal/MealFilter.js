import React from "react";
import withStyles from '@material-ui/core/styles/withStyles';
import FormControl from '@material-ui/core/FormControl';
import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';
import { Button } from "@material-ui/core";

const styles = {

}

class MealFilter extends React.Component {
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
                    <Button
                        color="primary"
                        variant="contained"
                    >Filter</Button>
                </Grid>
            </Grid>

        </main>);
    }
}

export default withStyles(styles)(MealFilter);