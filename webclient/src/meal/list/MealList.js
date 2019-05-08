import React from "react";
import Button from "@material-ui/core/Button";
import withStyles from "@material-ui/core/styles/withStyles";
import { Link } from "react-router-dom";
import { withPage } from "../../AppPage";
import moment from "moment";
import ServerPagingTable from "../../common/table/ServerPagingTable";
import UrlMealFilter from "./UrlMealFilter";
import Alert from "./Alert";

const styles = theme => ({
    button: {
        margin: theme.spacing.unit,
        marginLeft: 0,
    },
})

const columns = [
    { id: "date", dataField: "datetime", numeric: false, disablePadding: true, label: "Date", renderContent(d) { return moment(d).format("DD MMM YYYY") } },
    { id: "time", dataField: "datetime", numeric: false, disablePadding: false, label: "Time", renderContent(d) { return moment(d).format("hh:mm A") } },
    { id: "text", dataField: "text", numeric: false, disablePadding: false, label: "Text" },
    { id: "calories", dataField: "calories", numeric: true, disablePadding: false, label: "Calories" },
];


export class MealList extends React.Component {
    state = { alertInfo: { alerted: false, dailyCalorieLimit: 0, totalCalories: 0 } };
    renderAlert() {
        const { alertInfo } = this.state;
        if (alertInfo.alerted) {
            return <Alert
            >{`You have consumed ${alertInfo.totalCalories} calroies today that exceeds your daily limit (${alertInfo.dailyCalorieLimit})`}</Alert>
        }
    }

    async componentDidMount() {
        try {
            const response = await this.props.api.get(`/v1/users/me/alerts/calorie?date=${moment().format("yyyy-MM-dd")}`);
            const json = await response.json();
            this.setState({
                alertInfo: json.data,
            })
        } catch (e) {
            this.props.handleError(e);
        }

    }
    render() {
        const { classes } = this.props;
        return <div>
            {this.renderAlert()}
            <UrlMealFilter
                queryString={this.props.location.search}
                onQueryStringChange={(queryString) => {
                    this.props.history.push({
                        pathname: this.props.location.pathname,
                        search: queryString,
                    })

                }} />

            <ServerPagingTable
                columns={columns}
                tableName="Meals"
                baseUrl="/v1/meals"
                queryString={this.props.location.search}
                onRowSelect={(id) => {
                    this.props.history.push(`/meals/${id}/update`);
                }}
            />
            <Button component={Link} to="/meals/new"
                variant="contained" color="primary" className={classes.button}>
                New Meal
      </Button>
        </div>
    }
}

export default withPage(withStyles(styles)(MealList));