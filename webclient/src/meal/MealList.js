import React from 'react';
import Typography from '@material-ui/core/Typography';
import withStyles from '@material-ui/core/styles/withStyles';
import SimpleTable from './SimpleTable';

const styles = theme => ({})

function MealList(props) {
    const { classes } = props;
    return <div>
        <Typography variant="h4" gutterBottom component="h2">
            Products
          </Typography>
        <div className={classes.tableContainer}>
            <SimpleTable />
        </div>
    </div>
}

export default withStyles(styles)(MealList);