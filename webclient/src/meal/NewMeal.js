import React from 'react';
import PropTypes from 'prop-types';
import Button from '@material-ui/core/Button';
import withStyles from '@material-ui/core/styles/withStyles';
import { withPage } from '../AppPage';
import MealForm from './MealForm';
import queryString from 'query-string'


const styles = theme => ({
    add: {
        marginTop: theme.spacing.unit * 3,
        marginLeft: theme.spacing.unit * 3,
        paddingLeft: theme.spacing.unit * 4,
        paddingRight: theme.spacing.unit * 4,
    },
    cancel: {
        marginTop: theme.spacing.unit * 3,
    }
});


class NewMeal extends React.Component {
    state = {
        meal: {
            datetime: new Date(),
            calories: 0,
            text: "Meal Info",
            user: null,
        },
        loading: false,
    }

    buildSubmitData() {
        const { user, ...mealWithoutUser } = this.state.meal;
        if (this.hasUserSelect()) {
            mealWithoutUser.userId = (this.state.meal.user || {}).key;
        }

        return mealWithoutUser;
    }

    hasUserSelect() {
        return !!queryString.parse(this.props.location.search)["user-select"];
    }
    handleSubmit = async (e) => {
        e.preventDefault();
        this.setState({ loading: true });
        try {
            await this.props.api.post("/api/meals", this.buildSubmitData());
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
        return (
            <MealForm
                userSelect={this.hasUserSelect()}
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
                            className={classes.add}
                            onClick={this.handleSubmit}
                        >
                            Add
                        </Button>
                    </div>
                }} />
        );
    }


}

NewMeal.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withPage(withStyles(styles)(NewMeal));

