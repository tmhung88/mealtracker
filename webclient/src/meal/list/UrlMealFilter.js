import React from "react";
import withStyles from "@material-ui/core/styles/withStyles";
import MealFilter from "./MealFilter";
import queryString from "query-string";
import datetimeHelper from "../../datetimeHelper";

const styles = {

}

export class UrlMealFilter extends React.Component {
    getField(field, filter) {
        return filter[field] || "";
    }

    buildFilterString(filterInfo) {
        return `fromDate=${this.getField("fromDate", filterInfo)}&toDate=${this.getField("toDate", filterInfo)}&fromTime=${this.getField("fromTime", filterInfo)}&toTime=${this.getField("toTime", filterInfo)}`
    }

    parseDate(value) {
        return datetimeHelper.verifyDateOrUndefined(value);
    }

    parseTime(value) {
        return datetimeHelper.verifyTimeOrUndefined(value);
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