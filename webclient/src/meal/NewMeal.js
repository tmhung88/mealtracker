import React from 'react';
import PropTypes from 'prop-types';
import Button from '@material-ui/core/Button';
import withStyles from '@material-ui/core/styles/withStyles';
import { Link } from "react-router-dom";
import { post } from '../api';
import { withPage } from '../AppPage';
import MealForm from './MealForm';

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
        },
        loading: false,
    }
    handleSubmit = async (e) => {
        e.preventDefault();
        this.setState({ loading: true });
        try {
            await post("/api/meals", this.state.meal);
            this.props.history.replace("/meals")
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
                loading={this.state.loading}
                onMealChange={this.handleMealChange}
                meal={this.state.meal}
                renderActionButtons={() => {
                    return <div>
                        <Button component={Link} to="/meals"
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

