import React from 'react';
import Typography from '@material-ui/core/Typography';
import withStyles from '@material-ui/core/styles/withStyles';
import SimpleTable from './SimpleTable';

const styles = theme => ({})

function UserList(props) {
    const { classes } = props;
    return <div>
        <Typography variant="h4" gutterBottom component="h2">
            Users
          </Typography>
        <div className={classes.tableContainer}>
            <SimpleTable />
        </div>
    </div>
}

export default withStyles(styles)(UserList);