import React from 'react';
import PropTypes from 'prop-types';
import Button from '@material-ui/core/Button';
import withStyles from '@material-ui/core/styles/withStyles';
import MealForm from './MealForm';
import { withPage } from '../../core/components/AppPage';
import { DateTimeHelper } from '../../datetimeHelper';
import moment from "moment";

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
            consumedDate: moment().format(DateTimeHelper.DATE_FORMAT),
            consumedTime: moment().format(DateTimeHelper.TIME_FORMAT),
            calories: 0,
            name: "",            
            consumerId: null,
        },
        loading: false,
    }

    hasUserSelect() {
        return !!this.props.userSelect;
    }
    handleSubmit = async () => {
        this.setState({ loading: true });
        try {
            await this.props.api.post(this.props.baseApiUrl, this.state.meal);
            this.props.goBackOrReplace(this.props.cancelPage)
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

