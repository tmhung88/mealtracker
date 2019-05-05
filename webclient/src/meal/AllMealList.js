import React from 'react';
import Button from '@material-ui/core/Button';
import withStyles from '@material-ui/core/styles/withStyles';
import { Link } from "react-router-dom";
import { get } from '../api';
import { withPage } from '../AppPage';
import { EnhancedTable } from '../common/table/Table';
import moment from "moment";
import { Loading } from '../common/loading/Loading';

const styles = theme => ({
    button: {
        margin: theme.spacing.unit,
        marginLeft: 0,
    },
})

const columns = [
    { id: 'date', dataField: "datetime", numeric: false, disablePadding: true, label: 'Date', renderContent(d) { return moment(d).format("DD MMM YYYY") } },
    { id: 'time', dataField: "datetime", numeric: false, disablePadding: false, label: 'Time', renderContent(d) { return moment(d).format("hh:mm A") } },
    { id: 'text', dataField: "text",  numeric: false, disablePadding: false, label: 'Text' },
    { id: 'calories', dataField: "calories",  numeric: true, disablePadding: false, label: 'Calories' },
];


class AllMealList extends React.Component {
    state = { dataLoaded: false, data: [] };
    async componentDidMount() {
        try {
            const response = await get("/api/meals/all");
            const json = await response.json();
            console.log(json);
            this.setState({ dataLoaded: true, data: json })
        } catch {
            this.setState({ dataLoaded: true, data: [] })
        }

    }
    render() {
        const { classes } = this.props;
        return <div>

            <div className={classes.tableContainer}>
                <Loading
                    active={!this.state.dataLoaded}
                >
                    <EnhancedTable
                        columns={columns}
                        tableName="Meals"
                        onRowSelect={(id) => {
                            this.props.history.push(`/meals/${id}/update`);
                        }}
                        rows={this.state.data} />

                </Loading>
            </div>
            <Button component={Link} to="/meals/new"
                variant="contained" color="primary" className={classes.button}>
                New Meal
      </Button>
        </div>
    }
}

export default withPage(withStyles(styles)(AllMealList));