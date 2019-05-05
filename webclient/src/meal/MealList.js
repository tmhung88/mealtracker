import React from 'react';
import Button from '@material-ui/core/Button';
import withStyles from '@material-ui/core/styles/withStyles';
import { Link } from "react-router-dom";
import LoadingOverlay from 'react-loading-overlay';
import { get } from '../api';
import { withPage } from '../AppPage';
import { EnhancedTable } from '../common/table/Table';
import moment from "moment";

const styles = theme => ({
    button: {
        margin: theme.spacing.unit,
        marginLeft: 0,
    },
})

const columns = [
    { id: 'date', numeric: false, disablePadding: true, label: 'Date', renderContent(d){ return moment(d).format("DD MMM YYYY") } },
    { id: 'time', numeric: false, disablePadding: false, label: 'Time', renderContent(d){ return moment(d).format("hh:mm A") } },
    { id: 'text', numeric: false, disablePadding: false, label: 'Text' },
    { id: 'calories', numeric: true, disablePadding: false, label: 'Calories' },
];


class MealList extends React.Component {
    state = { dataLoaded: false, data: [] };
    async componentDidMount() {
        try {
            const response = await get("/api/meals");
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
                <LoadingOverlay
                    active={!this.state.dataLoaded}
                    spinner
                >
                    <EnhancedTable
                        columns={columns}
                        tableName="Meals"
                        onRowSelect={(id)=> {
                            this.props.history.push(`/meals/${id}/update`);
                        }}
                        rows={this.state.data} />

                </LoadingOverlay>
            </div>
            <Button component={Link} to="/meals/new"
                variant="contained" color="primary" className={classes.button}>
                New Meal
      </Button>
        </div>
    }
}

export default withPage(withStyles(styles)(MealList));