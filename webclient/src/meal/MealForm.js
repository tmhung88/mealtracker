import React from 'react';
import PropTypes from 'prop-types';
import TextField from '@material-ui/core/TextField';
import FormControl from '@material-ui/core/FormControl';
import withStyles from '@material-ui/core/styles/withStyles';
import moment from "moment";
import Form from '../common/form/Form';
import UserSelect from '../user/UserSelect';
import { ShowWithRight, Rights } from '../userSession';

const styles = () => ({

});


class MealForm extends React.Component {

    renderUserSelect() {
        if (this.props.userSelect) {
            return <ShowWithRight right={Rights.AllMeal}>
                <FormControl margin="normal" required fullWidth>
                    <UserSelect
                        user={this.props.meal.user}
                        onUserChange={(user) => {
                            this.props.onMealChange({
                                ...this.props.meal,
                                user: user,
                            })
                        }}
                    />
                </FormControl>
            </ShowWithRight>
        }
    }

    render() {
        const { classes, renderActionButtons, onMealChange, loading } = this.props;
        const meal = this.props.meal || {
            datetime: new Date(),
            calories: 0,
            text: "",
        };
        return (
            <Form formName="Meal" loading={loading}>
                <FormControl margin="normal" required fullWidth>
                    <TextField
                        id="date"
                        label="Date"
                        type="date"
                        value={moment(meal.datetime).format("YYYY-MM-DD")}
                        onChange={(e) => {
                            const value = e.currentTarget.value;
                            const dateMoment = value ? moment(value) : moment();
                            onMealChange({
                                ...meal,
                                datetime: moment(meal.datetime)
                                    .year(dateMoment.year())
                                    .month(dateMoment.month())
                                    .date(dateMoment.date())
                            })
                        }}
                        className={classes.textField}
                        InputLabelProps={{
                            shrink: true,
                        }}
                    />
                </FormControl>
                <FormControl margin="normal" required fullWidth>
                    <TextField
                        id="time"
                        label="Time"
                        type="time"
                        value={moment(meal.datetime).format("HH:mm")}
                        onChange={(e) => {
                            const value = e.currentTarget.value;
                            console.log(value);
                            const dateMoment = value ? moment(value, "HH:mm") : moment();
                            onMealChange({
                                ...meal,
                                datetime: moment(meal.datetime)
                                    .hour(dateMoment.hour())
                                    .minute(dateMoment.minute())
                            })
                        }}
                        className={classes.textField}
                        InputLabelProps={{
                            shrink: true,
                        }}
                        inputProps={{
                            step: 300, // 5 min
                        }}
                    />
                </FormControl>
                <FormControl margin="normal" required fullWidth>
                    <TextField
                        id="text"
                        label="Text"
                        multiline
                        className={classes.textField}
                        margin="normal"
                        value={meal.text || ""}
                        onChange={e => {
                            onMealChange({
                                ...meal,
                                text: e.currentTarget.value,
                            });
                        }}
                    />

                </FormControl>
                <FormControl margin="normal" required fullWidth>

                    <TextField
                        id="calories"
                        type="number"
                        label="Calories"
                        className={classes.textField}
                        InputLabelProps={{
                            shrink: true,
                        }}
                        margin="normal"
                        value={meal.calories || 0}
                        onChange={e => {
                            onMealChange({
                                ...meal,
                                calories: Number.parseInt(e.currentTarget.value, 10),
                            });
                        }}
                    />

                </FormControl>
                {this.renderUserSelect()}
                {renderActionButtons()}
            </Form>
        );
    }


}

MealForm.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(MealForm);