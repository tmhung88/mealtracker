import React from 'react';
import Button from '@material-ui/core/Button';
import withStyles from '@material-ui/core/styles/withStyles';
import { Link } from "react-router-dom";
import { withPage } from '../AppPage';
import ServerPagingTable from '../common/table/ServerPagingTable';
import { roleIdToName } from '../userSession';

const styles = theme => ({
    button: {
        margin: theme.spacing.unit,
        marginLeft: 0,
    },
})

const columns = [
    { id: 'email', dataField: "email", numeric: false, disablePadding: true, label: 'Email' },
    { id: 'fullName', dataField: "fullName", numeric: false, disablePadding: true, label: 'Full Name' },
    { id: 'dailyCalorieLimit', dataField: "dailyCalorieLimit", numeric: true, disablePadding: false, label: 'Calories' },
    { id: 'role', dataField: "role", numeric: true, disablePadding: false, label: 'Role', renderContent(d) { return roleIdToName(d) } },
];

export class UserList extends React.Component {
    render() {
        const { classes } = this.props;
        return <div>
            <ServerPagingTable
                baseUrl="/v1/users"
                onRowSelect={(id) => {
                    this.props.history.push(`/users/${id}/update`);
                }}

                columns={columns}
                tableName="Users" />
            <Button component={Link} to="/users/new"
                variant="contained" color="primary" className={classes.button}>
                New User
      </Button>
        </div>
    }
}

export default withPage(withStyles(styles)(UserList));