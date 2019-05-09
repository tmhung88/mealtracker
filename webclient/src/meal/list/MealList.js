import React from "react";
import Button from "@material-ui/core/Button";
import withStyles from "@material-ui/core/styles/withStyles";
import { Link } from "react-router-dom";
import moment from "moment";
import ServerPagingTable from "../../common/table/ServerPagingTable";
import UrlMealFilter from "./UrlMealFilter";
import Alert from "./Alert";
import { withPage } from "../../core/components/AppPage";
import { DateTimeHelper } from "../../datetimeHelper";
import { ApiUrl } from '../../constants/ApiUrl';
import { Pages } from '../../constants/Pages';

const styles = theme => ({
    button: {
        margin: theme.spacing.unit,
        marginLeft: 0,
    },
})

const columns = [
    { id: "consumedDate", dataField: "datetime", numeric: false, label: "Date", renderContent(d) { return moment(d).format("DD MMM YYYY") } },
    { id: "consumedTime", dataField: "datetime", numeric: false, label: "Time", renderContent(d) { return moment(d).format("hh:mm A") } },
    { id: "name", dataField: "name", numeric: false, label: "Name" },
    { id: "calories", dataField: "calories", numeric: true, label: "Calories" },
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
            const response = await this.props.api.get(`${ApiUrl.ME_ALERT_CALORIES}?date=${moment().format(DateTimeHelper.DATE_FORMAT)}`);
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
                baseUrl={ApiUrl.ME_MEALS}
                queryString={this.props.location.search}
                onRowSelect={(id) => {
                    this.props.history.push(Pages.MY_UPDATE_MEAL.replace(":id", id));
                }}
            />
            <Button component={Link} to={Pages.MY_NEW_MEAL}
                variant="contained" color="primary" className={classes.button}>
                New Meal
      </Button>
        </div>
    }
}

export default withPage(withStyles(styles)(MealList));