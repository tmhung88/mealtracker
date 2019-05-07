import React from 'react';
import PropTypes from 'prop-types';
import Button from '@material-ui/core/Button';
import withStyles from '@material-ui/core/styles/withStyles';
import { withPage } from '../AppPage';
import MealForm from './MealForm';
import queryString from 'query-string'
import { NotFoundRequestError } from '../api';

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
        this.props.handleErrorContext(async () => {
            try {
                let url = `/v1/meals/${this.props.match.params.id}`;
                if (this.hasUserSelect()) {
                    url += "/all";
                }

                const response = await this.props.api.get(url);
                const json = await response.json();
                this.setState({
                    meal: json,
                })
            } catch (error) {
                if (error instanceof NotFoundRequestError) {
                    this.setState({ meal: null });
                } else {
                    throw error;
                }
            } finally {
                this.setState({ loading: false });
            }
        })

    }

    hasUserSelect() {
        return !!queryString.parse(this.props.location.search)["user-select"];
    }

    handleSubmit = async (e) => {
        this.props.handleErrorContext(async () => {
            e.preventDefault();
            this.setState({ loading: true });
            try {
                await this.props.api.put(`/v1/meals/${this.state.meal.id}`, this.state.meal);
                this.props.goBackOrReplace("/meal")
            } finally {
                this.setState({ loading: false });
            }
            console.log("finished");
        })
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
                notFound={!this.state.meal}
                userSelect={this.hasUserSelect()}
                loading={this.state.loading}
                onMealChange={this.handleMealChange}
                meal={this.state.meal}
                renderActionButtons={(isValid) => {
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
                            onClick={(e) => {
                                e.preventDefault();
                                if (!isValid()) {
                                    return;
                                }

                                this.handleSubmit(e);
                            }}
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