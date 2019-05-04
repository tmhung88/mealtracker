import React from 'react';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import withStyles from '@material-ui/core/styles/withStyles';
import UserTable from './UserTable';
import { BrowserRouter as Router, Route, Link, Switch } from "react-router-dom";
import LoadingOverlay from 'react-loading-overlay';
import { get } from '../api';

const styles = theme => ({
    button: {
        margin: theme.spacing.unit,
        marginLeft: 0,
    },
})

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
                    <UserTable rows={this.state.data} />
                </LoadingOverlay>
            </div>
            <Button component={Link} to="/users/new"
                variant="contained" color="primary" className={classes.button}>
                New User
      </Button>
        </div>
    }
}

export default withStyles(styles)(UserList);