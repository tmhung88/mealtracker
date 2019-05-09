import React, { Fragment } from "react";
import PropTypes from "prop-types";
import TextField from "@material-ui/core/TextField";
import FormControl from "@material-ui/core/FormControl";
import withStyles from "@material-ui/core/styles/withStyles";
import Form from "../../common/form/Form";
import UserSelect from "../../user/UserSelect";
import ValidationForm from "../../common/form/ValidationForm";
import FormHelperText from "@material-ui/core/FormHelperText";
import NotFoundForm from "../../common/form/NotFoundForm";
import { ShowWithRight, Rights } from "../../core/userSession";

const styles = () => ({

});


export class MealForm extends React.Component {

    renderUserSelect(userId, username, onFieldsChange, errorMessage) {
        if (this.props.userSelect) {
            const user = userId != null ? { key: userId, label: username } : null;
            return <ShowWithRight right={Rights.AllMeal}>
                <FormControl margin="normal" required fullWidth error={!!errorMessage}>
                    <UserSelect
                        user={user}
                        onUserChange={(user) => {
                            onFieldsChange({
                                consumerEmail: user && user.label,
                                consumerId: user && user.key
                            });
                        }}
                    />
                    <FormHelperText>{errorMessage}</FormHelperText>
                </FormControl>
            </ShowWithRight>
        }
    }

    getValidationConstraints() {
        let constraints = {
            name: {
                presence: { allowEmpty: false },
            },
            consumedDate: {
                presence: { allowEmpty: false },
            },
            consumedTime: {
                presence: { allowEmpty: false },
            },
        }
        if (this.props.userSelect) {
            constraints = {
                ...constraints,
                consumerId: {
                    presence: { message: "^User can't be blank" },
                },
            }
        }

        return constraints;
    }

    render() {
        const { classes, renderActionButtons, onMealChange, loading, serverValidationError, notFound } = this.props;
        if (notFound) {
            return <NotFoundForm formName="Meal" />
        }
        return (
            <Form formName="Meal" loading={loading} >
                <ValidationForm
                    serverValidationError={serverValidationError}
                    constraints={this.getValidationConstraints()}
                    data={this.props.meal}
                    onDataChange={(meal) => onMealChange(meal)}
                >
                    {({ onFieldChange, onFieldsChange, data, isValid, validationFields, validationMessage }) => {
                        return (<Fragment>
                            <FormControl margin="normal" required fullWidth error={!!validationFields.consumedDate}>
                                <TextField
                                    id="consumedDate"
                                    label="Date"
                                    type="date"
                                    value={data.consumedDate}
                                    required
                                    onChange={(e) => {
                                        onFieldChange("consumedDate", e.currentTarget.value)

                                    }}
                                    className={classes.textField}
                                    InputLabelProps={{
                                        shrink: true,
                                    }}
                                />
                                <FormHelperText>{validationFields.consumedDate}</FormHelperText>
                            </FormControl>
                            <FormControl margin="normal" required fullWidth error={!!validationFields.consumedTime}>
                                <TextField
                                    id="consumedTime"
                                    label="Time"
                                    type="time"
                                    value={data.consumedTime}
                                    required
                                    onChange={(e) => {
                                        onFieldChange("consumedTime", e.currentTarget.value)
                                    }}
                                    className={classes.textField}
                                    InputLabelProps={{
                                        shrink: true,
                                    }}
                                    inputProps={{
                                        step: 300, // 5 min
                                    }}
                                />
                                <FormHelperText>{validationFields.consumedTime}</FormHelperText>
                            </FormControl>
                            <FormControl margin="normal" required fullWidth error={!!validationFields.name}>
                                <TextField
                                    id="name"
                                    label="Name"
                                    className={classes.textField}
                                    InputLabelProps={{
                                        shrink: true,
                                    }}
                                    required
                                    margin="normal"
                                    value={data.name}
                                    onChange={e => {
                                        onFieldChange("name", e.currentTarget.value);
                                    }}
                                />
                                <FormHelperText>{validationFields.name}</FormHelperText>
                            </FormControl>
                            <FormControl margin="normal" required fullWidth>
                                <TextField
                                    id="calories"
                                    label="Calories"
                                    type="number"
                                    className={classes.textField}
                                    InputLabelProps={{
                                        shrink: true,
                                    }}
                                    required
                                    margin="normal"
                                    value={data.calories || 0}
                                    onChange={e => {
                                        onFieldChange("calories", Number.parseInt(e.currentTarget.value, 10));
                                    }}
                                />

                            </FormControl>
                            {this.renderUserSelect(data.consumerId, data.consumerEmail, onFieldsChange, validationFields.consumerId)}
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