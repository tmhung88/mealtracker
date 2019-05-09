import React from 'react';
import PropTypes from 'prop-types';
import Button from '@material-ui/core/Button';
import withStyles from '@material-ui/core/styles/withStyles';
import { withPage } from '../../core/components/AppPage';
import MealForm from './MealForm';
import { NotFoundRequestError } from '../../core/api';

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


export class UpdateMeal extends React.Component {
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
            let url = `${this.props.baseApiUrl}/${this.props.match.params.id}`;
            const response = await this.props.api.get(url);
            const json = await response.json();
            const {consumer, ...rest} = json.data;
            this.setState({
                meal: {
                    ...rest,
                    consumerId: consumer && consumer.id,
                    consumerEmail: consumer && consumer.email,
                },
            })
        } catch (error) {
            if (error instanceof NotFoundRequestError) {
                this.setState({ meal: null });
            } else {
                this.props.handleError(error);
            }
        } finally {
            this.setState({ loading: false });
        }

    }

    hasUserSelect() {
        return !!this.props.userSelect;
    }

    handleSubmit = async (e) => {
        this.setState({ loading: true });
            try {
                await this.props.api.put(`${this.props.baseApiUrl}/${this.props.match.params.id}`, this.state.meal);
                this.props.goBackOrReplace(this.props.cancelPage)
            } catch(e){
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
                notFound={!this.state.meal}
                userSelect={this.hasUserSelect()}
                loading={this.state.loading}
                onMealChange={this.handleMealChange}
                meal={this.state.meal}
                renderActionButtons={(isValid) => {
                    return <div>
                        <Button onClick={() => this.props.goBackOrReplace(this.props.cancelPage)}
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