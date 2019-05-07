import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import TextField from '@material-ui/core/TextField';
import FormControl from '@material-ui/core/FormControl';
import withStyles from '@material-ui/core/styles/withStyles';
import moment from "moment";
import Form from '../common/form/Form';
import UserSelect from '../user/UserSelect';
import { ShowWithRight, Rights } from '../userSession';
import ValidationForm from '../common/form/ValidationForm';
import FormHelperText from '@material-ui/core/FormHelperText';

const styles = () => ({

});


class MealForm extends React.Component {

    renderUserSelect(userId, username, onFieldsChange, errorMessage) {
        if (this.props.userSelect) {
            const user = userId != null ? { key: userId, label: username } : null;
            console.log(user);
            return <ShowWithRight right={Rights.AllMeal}>
                <FormControl margin="normal" required fullWidth error={!!errorMessage}>
                    <UserSelect
                        user={user}
                        onUserChange={(user) => {
                            onFieldsChange({
                                username: user && user.label,
                                userId: user && user.key
                            });
                        }}
                    />
                    <FormHelperText>{errorMessage}</FormHelperText>
                </FormControl>
            </ShowWithRight>
        }
    }

    getValidationConstraints() {
        if (this.props.userSelect) {
            return {
                userId: {
                    presence: { message: "^User can't be blank" },
                },
            }
        }

        return null;
    }

    render() {
        const { classes, renderActionButtons, onMealChange, loading, serverValidationError } = this.props;
        const meal = this.props.meal || {
            datetime: new Date(),
            calories: 0,
            text: "",
            userId: "",
        };
        return (
            <Form formName="Meal" loading={loading} >
                <ValidationForm
                    serverValidationError={serverValidationError}
                    constraints={this.getValidationConstraints()}
                    data={meal}
                    onDataChange={(meal) => onMealChange(meal)}
                >
                    {({ onFieldChange, onFieldsChange, data, isValid, validationFields, validationMessage }) => {
                        return (<Fragment>
                            <FormControl margin="normal" required fullWidth>
                                <TextField
                                    id="date"
                                    label="Date"
                                    type="date"
                                    value={moment(meal.datetime).format("YYYY-MM-DD")}
                                    onChange={(e) => {
                                        const value = e.currentTarget.value;
                                        const dateMoment = value ? moment(value) : moment();
                                        onFieldChange("datetime", moment(meal.datetime)
                                            .year(dateMoment.year())
                                            .month(dateMoment.month())
                                            .date(dateMoment.date()))

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
                                        onFieldChange("datetime", moment(meal.datetime)
                                            .hour(dateMoment.hour())
                                            .minute(dateMoment.minute()))
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
                                        onFieldChange("text", e.currentTarget.value);
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
                                        onFieldChange("calories", Number.parseInt(e.currentTarget.value, 10));
                                    }}
                                />

                            </FormControl>
                            {this.renderUserSelect(meal.userId, meal.username, onFieldsChange, validationFields.userId)}
                            {renderActionButtons(isValid)}
                        </Fragment>)
                    }}
                </ValidationForm>

            </Form>
        );
    }


}

MealForm.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(MealForm);