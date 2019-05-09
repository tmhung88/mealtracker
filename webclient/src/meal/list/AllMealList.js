import React from "react";
import Button from "@material-ui/core/Button";
import withStyles from "@material-ui/core/styles/withStyles";
import { Link } from "react-router-dom";
import moment from "moment";
import ServerPagingTable from "../../common/table/ServerPagingTable";
import { withPage } from "../../core/components/AppPage";
import { ApiUrl } from '../../constants/ApiUrl';
import { Pages } from '../../constants/Pages';

const styles = theme => ({
    button: {
        margin: theme.spacing.unit,
        marginLeft: 0,
    },
})

const columns = [
    { id: "date", dataField: "datetime", numeric: false, label: "Date", renderContent(d) { return moment(d).format("DD MMM YYYY") } },
    { id: "time", dataField: "datetime", numeric: false, label: "Time", renderContent(d) { return moment(d).format("hh:mm A") } },
    { id: "name", dataField: "name", numeric: false, label: "Name" },
    { id: "calories", dataField: "calories", numeric: true, label: "Calories" },
    {
        id: "consumerEmail", dataField: "consumer", numeric: true, label: "Consumer Email",
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
                baseUrl={ApiUrl.MEALS}
                onRowSelect={(id) => {
                    this.props.history.push(Pages.ALL_UPDATE_MEAL.replace(":id",id));
                }}
            />
            <Button component={Link} to={Pages.ALL_NEW_MEAL}
                variant="contained" color="primary" className={classes.button}>
                New Meal
      </Button>
        </div>
    }
}

export default withPage(withStyles(styles)(AllMealList));