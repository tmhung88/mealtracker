import React from 'react';
import Button from '@material-ui/core/Button';
import withStyles from '@material-ui/core/styles/withStyles';
import { Link } from "react-router-dom";
import { withPage } from '../AppPage';
import ServerPagingTable from '../common/table/ServerPagingTable';

const styles = theme => ({
    button: {
        margin: theme.spacing.unit,
        marginLeft: 0,
    },
})

const columns = [
    { id: 'email', dataField: "email", numeric: false, disablePadding: true, label: 'Email' },
    { id: 'calories', dataField: "calories", numeric: true, disablePadding: false, label: 'Calories' },
];

class UserList extends React.Component {
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