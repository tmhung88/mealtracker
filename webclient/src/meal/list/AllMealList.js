import React from "react";
import Button from "@material-ui/core/Button";
import withStyles from "@material-ui/core/styles/withStyles";
import { Link } from "react-router-dom";
import { withPage } from "../../AppPage";
import moment from "moment";
import ServerPagingTable from "../../common/table/ServerPagingTable";

const styles = theme => ({
    button: {
        margin: theme.spacing.unit,
        marginLeft: 0,
    },
})

const columns = [
    { id: "date", dataField: "datetime", numeric: false, disablePadding: true, label: "Date", renderContent(d) { return moment(d).format("DD MMM YYYY") } },
    { id: "time", dataField: "datetime", numeric: false, disablePadding: false, label: "Time", renderContent(d) { return moment(d).format("hh:mm A") } },
    { id: "name", dataField: "name", numeric: false, disablePadding: false, label: "Name" },
    { id: "calories", dataField: "calories", numeric: true, disablePadding: false, label: "Calories" },
    {
        id: "consumerEmail", dataField: "consumer", numeric: true, disablePadding: false, label: "Consumer Email",
        renderContent(d) { return d.email }
    },
];

export class AllMealList extends React.Component {
    render() {
        const { classes } = this.props;
        return <div>
            <ServerPagingTable
                columns={columns}
                tableName="All Meals"
                baseUrl="/v1/meals"
                onRowSelect={(id) => {
                    this.props.history.push(`/meals/all/${id}/update`);
                }}
            />
            <Button component={Link} to="/meals/all/new"
                variant="contained" color="primary" className={classes.button}>
                New Meal
      </Button>
        </div>
    }
}

export default withPage(withStyles(styles)(AllMealList));