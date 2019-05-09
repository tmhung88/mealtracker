import React from "react";
import Button from "@material-ui/core/Button";
import withStyles from "@material-ui/core/styles/withStyles";
import { Link } from "react-router-dom";
import ServerPagingTable from "../common/table/ServerPagingTable";
import { withPage } from "../core/components/AppPage";
import { roleIdToName } from "../core/userSession";
import { ApiUrl } from '../constants/ApiUrl';
import { Pages } from '../constants/Pages';

const styles = theme => ({
    button: {
        margin: theme.spacing.unit,
        marginLeft: 0,
    },
})

const columns = [
    { id: "email", dataField: "email", numeric: false, label: "Email" },
    { id: "fullName", dataField: "fullName", numeric: false, label: "Full Name" },
    { id: "dailyCalorieLimit", dataField: "dailyCalorieLimit", numeric: true, label: "Daily Calories Limit" },
    { id: "role", dataField: "role", numeric: true, label: "Role", renderContent(d) { return roleIdToName(d) } },
];

export class UserList extends React.Component {
    render() {
        const { classes } = this.props;
        return <div>
            <ServerPagingTable
                baseUrl={ApiUrl.USERS}
                onRowSelect={(id) => {
                    this.props.history.push(Pages.UPDATE_USER.replace(":id", id));
                }}

                columns={columns}
                tableName="Users" />
            <Button component={Link} to={Pages.NEW_USER}
                variant="contained" color="primary" className={classes.button}>
                New User
      </Button>
        </div>
    }
}

export default withPage(withStyles(styles)(UserList));