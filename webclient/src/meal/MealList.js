import React from 'react';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import withStyles from '@material-ui/core/styles/withStyles';
import MealTable from './MealTable';
import { BrowserRouter as Router, Route, Link, Switch } from "react-router-dom";
import LoadingOverlay from 'react-loading-overlay';
import { get } from '../api';
import { withPage } from '../AppPage';

const styles = theme => ({
    button: {
        margin: theme.spacing.unit,
        marginLeft: 0,
    },
})

class MealList extends React.Component {
    state = { dataLoaded: false, data: [] };
    async componentDidMount() {
        try {
            const response = await get("/api/meals");
            const json = await response.json();
            console.log(json);
            this.setState({ dataLoaded: true, data: json })
        } catch {
            this.setState({ dataLoaded: true, data: []})
        }

    }
    render() {
        const { classes } = this.props;
        return <div>
            <Typography variant="h4" gutterBottom component="h2">
                Meals
          </Typography>
            <div className={classes.tableContainer}>
                <LoadingOverlay
                    active={!this.state.dataLoaded}
                    spinner
                >
                    <MealTable rows={this.state.data} />

                </LoadingOverlay>
            </div>
            <Button component={Link} to="/meals/new"
                variant="contained" color="primary" className={classes.button}>
                New Meal
      </Button>
        </div>
    }
}

export default  withPage(withStyles(styles)(MealList));