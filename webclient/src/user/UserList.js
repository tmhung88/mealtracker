import React from "react";
import Button from "@material-ui/core/Button";
import withStyles from "@material-ui/core/styles/withStyles";
import { Link } from "react-router-dom";
import ServerPagingTable from "../common/table/ServerPagingTable";
import { withPage } from "../core/components/AppPage";
import { roleIdToName } from "../core/userSession";

const styles = theme => ({
    button: {
        margin: theme.spacing.unit,
        marginLeft: 0,
    },
})

const columns = [
    { id: "email", dataField: "email", numeric: false, label: "Email" },
    { id: "fullName", dataField: "fullName", numeric: false, label: "Full Name" },
    { id: "dailyCalorieLimit", dataField: "dailyCalorieLimit", numeric: true, label: "Calories" },
    { id: "role", dataField: "role", numeric: true, label: "Role", renderContent(d) { return roleIdToName(d) } },
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