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

export class NewMeal extends React.Component {
    state = {
        meal: {
            datetime: new Date(),
            calories: 0,
            text: "",
            userId: null,
        },
        loading: false,
    }

    hasUserSelect() {
        return !!queryString.parse(this.props.location.search)["user-select"];
    }
    handleSubmit = async () => {
        this.setState({ loading: true });
        try {
            await this.props.api.post("/v1/meals", this.state.meal);
            this.props.goBackOrReplace("/meals")
        } catch (e) {
            this.props.handleError(e);
        } finally {
            this.setState({ loading: false });
        }
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
                renderActionButtons={(isValid) => {
                    return <div>
                        <Button onClick={() => this.props.goBackOrReplace("/meals")}
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
                            onClick={(e) => {
                                e.preventDefault();
                                if (!isValid()) {
                                    return;
                                }

                                this.handleSubmit();
                            }}
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

