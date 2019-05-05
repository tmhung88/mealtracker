import React from 'react';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import withStyles from '@material-ui/core/styles/withStyles';
import { Link } from "react-router-dom";
import LoadingOverlay from 'react-loading-overlay';
import { get } from '../api';
import { withPage } from '../AppPage';
import { EnhancedTable } from '../common/table/Table';

const styles = theme => ({
    button: {
        margin: theme.spacing.unit,
        marginLeft: 0,
    },
})

const columns = [
    { id: 'email', numeric: false, disablePadding: true, label: 'Email' },
    { id: 'calories', numeric: true, disablePadding: false, label: 'Calories' },
];

class UserList extends React.Component {
    state = { dataLoaded: false, data: [] };
    async componentDidMount() {
        const response = await get("/api/users");
        const json = await response.json();
        console.log(json);
        this.setState({ dataLoaded: true, data: json })
    }
    render() {
        const { classes } = this.props;
        return <div>
            <Typography variant="h4" gutterBottom component="h2">
                Users
          </Typography>
            <div className={classes.tableContainer}>
                <LoadingOverlay
                    active={!this.state.dataLoaded}
                    spinner
                >
                    <EnhancedTable
                        onRowSelect={(id)=> {
                            this.props.history.push(`/users/${id}/update`);
                        }}
                        rows={this.state.data} columns={columns} tableName="Users" />
                </LoadingOverlay>
            </div>
            <Button component={Link} to="/users/new"
                variant="contained" color="primary" className={classes.button}>
                New User
      </Button>
        </div>
    }
}

export default withPage(withStyles(styles)(UserList));