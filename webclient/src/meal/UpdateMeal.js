import React from 'react';
import PropTypes from 'prop-types';
import Button from '@material-ui/core/Button';
import withStyles from '@material-ui/core/styles/withStyles';
import { withPage } from '../AppPage';
import MealForm from './MealForm';
import queryString from 'query-string'

const styles = theme => ({
    
    update: {
        marginTop: theme.spacing.unit * 3,
        marginLeft: theme.spacing.unit * 3,
        paddingLeft: theme.spacing.unit * 4,
        paddingRight: theme.spacing.unit * 4,
    },
    cancel: {
        marginTop: theme.spacing.unit * 3,
    }
});


class UpdateMeal extends React.Component {
    state = {
        meal: {
            datetime: new Date(),
            calories: 0,
            text: "Meal Info",
        },
        loading: true,
    }
    async componentDidMount() {
        try {
            const response = await this.props.api.get(`/api/meals/${this.props.match.params.id}`);
            const json = await response.json();
            this.setState({
                meal: json,
            })
        } finally {
            this.setState({ loading: false });
        }
    }

    handleSubmit = async (e) => {
        e.preventDefault();
        this.setState({ loading: true });
        try {
            await this.props.api.put(`/api/meals/${this.state.meal.id}`, this.state.meal);
            this.props.goBackOrReplace("/meal")
        } finally {
            this.setState({ loading: false });
        }
        console.log("finished");
    };

    handleMealChange = (meal) => {
        this.setState({
            meal: meal,
        })
    }

    render() {
        const { classes } = this.props;
        const values = queryString.parse(this.props.location.search)
        return (
            <MealForm 
                userSelect={!!values["user-select"]}
                loading={this.state.loading}
                onMealChange={this.handleMealChange}
                meal={this.state.meal}
                renderActionButtons={() => {
                    return <div>
                        <Button onClick={() => this.props.goBackOrReplace("/meal")}
                            variant="contained"
                            color="secondary"
                            className={classes.cancel}
                        >
                            Cancel
                        </Button>
                        <Button
                            type="submit"
                            variant="contained"
                            color="primary"
                            className={classes.update}
                            onClick={this.handleSubmit}
                        >
                            Update
                        </Button>
                    </div>
                }} />
        );
    }


}

UpdateMeal.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withPage(withStyles(styles)(UpdateMeal));