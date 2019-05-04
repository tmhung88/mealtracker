import React from 'react';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import withStyles from '@material-ui/core/styles/withStyles';
import SimpleTable from './SimpleTable';
import { BrowserRouter as Router, Route, Link, Switch } from "react-router-dom";

const styles = theme => ({
    button: {
        margin: theme.spacing.unit,
        marginLeft: 0,
    },
})

function MealList(props) {
    const { classes } = props;
    return <div>
        <Typography variant="h4" gutterBottom component="h2">
            Meals
          </Typography>
        <div className={classes.tableContainer}>
            <SimpleTable />
        </div>
        <Button component={Link} to="/meals/new"
            variant="contained" color="primary" className={classes.button}>
            New Meal
      </Button>
    </div>
}

export default withStyles(styles)(MealList);