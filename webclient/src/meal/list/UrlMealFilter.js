import React from "react";
import withStyles from '@material-ui/core/styles/withStyles';
import MealFilter from "./MealFilter";
import queryString from 'query-string';
import datetimeHelper from "../../datetimeHelper";

const styles = {

}

class UrlMealFilter extends React.Component {
    getDate(field, filter) {
        return filter[field] ? filter[field].format("YYYY-MM-DD") : "";
    }

    getTime(field, filter) {
        return filter[field] ? filter[field].format("HH:mm") : "";
    }

    buildFilterString(filterInfo) {
        return `fromDate=${this.getDate("fromDate", filterInfo)}&toDate=${this.getDate("toDate", filterInfo)}&fromTime=${this.getTime("fromTime", filterInfo)}&toTime=${this.getTime("toTime", filterInfo)}`
    }

    parseDate(value) {
        return datetimeHelper.parseDateOrUndefined(value);
    }

    parseTime(value) {
        return datetimeHelper.parseTimeOrUndefined(value);
    }

    shouldComponentUpdate(nextProps) {
        return this.props.queryString !== nextProps.queryString;
    }

    render() {
        const filterRaw = queryString.parse(this.props.queryString || "");

        const filter = {
            fromDate: this.parseDate(filterRaw.fromDate),
            toDate: this.parseDate(filterRaw.toDate),
            fromTime: this.parseTime(filterRaw.fromTime),
            toTime: this.parseTime(filterRaw.toTime)
        }
        return (<MealFilter filter={filter}
            onFilter={(filterInfo) => {
                const query = this.buildFilterString(filterInfo);
                this.props.onQueryStringChange(query);
            }} />)
    }
}

export default withStyles(styles)(UrlMealFilter);