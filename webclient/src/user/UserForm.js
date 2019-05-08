import React, { Fragment } from "react";
import PropTypes from "prop-types";
import TextField from "@material-ui/core/TextField";
import FormControl from "@material-ui/core/FormControl";
import withStyles from "@material-ui/core/styles/withStyles";
import Form from "../common/form/Form";
import ValidationForm from "../common/form/ValidationForm";
import FormHelperText from "@material-ui/core/FormHelperText";
import NotFoundForm from "../common/form/NotFoundForm";
import { Roles } from "../userSession";
import { InputLabel, Input, Select, MenuItem } from "@material-ui/core";

const styles = () => ({

});


export class UserForm extends React.Component {

    renderRoleItems() {
        const { userSession } = this.props;
        switch (userSession.currentRole()) {
            case Roles.USER_MANAGER: return [
                <MenuItem key={Roles.REGULAR_USER} value={Roles.REGULAR_USER}>Regular User</MenuItem>,
                <MenuItem key={Roles.USER_MANAGER} value={Roles.USER_MANAGER}>User Manager</MenuItem>
            ];
            case Roles.ADMIN: return [
                <MenuItem key={Roles.REGULAR_USER} value={Roles.REGULAR_USER}>Regular User</MenuItem>,
                <MenuItem key={Roles.USER_MANAGER} value={Roles.USER_MANAGER}>User Manager</MenuItem>,
                <MenuItem key={Roles.ADMIN} value={Roles.ADMIN}>Admin</MenuItem>
            ];
            default: return [];
        }

    }

    render() {
        const { classes, renderActionButtons, onUserChange, loading, serverValidationError, notFound } = this.props;
        const user = this.props.user || {
            dailyCalorieLimit: 0,
            password: "",
            fullName: "",
            role: "USER_MANAGER",
            email: "",
        };
        if (notFound) {
            return <NotFoundForm formName="User" />
        }
        return (
            <Form formName="User" loading={loading}>
                <ValidationForm
                    serverValidationError={serverValidationError}
                    constraints={{
                        email: {
                            email: true,
                            presence: { allowEmpty: false },
                        },
                        fullName: {
                            presence: { allowEmpty: false },
                        },
                        password: {
                            presence: { allowEmpty: false },
                            length: {
                                minimum: 6,
                                message: "must be at least 6 characters"
                            }
                        }
                    }}
                    data={user}
                    onDataChange={(user) => onUserChange(user)}
                >
                    {({ onFieldChange, data, isValid, validationFields, validationMessage }) => {
                        return (<Fragment>
                            <FormControl margin="normal" required fullWidth error={!!validationFields.email}>
                                <TextField
                                    error={!!validationFields.email}
                                    id="email"
                                    label="Email"
                                    className={classes.textField}
                                    margin="normal"
                                    value={data.email}
                                    onChange={e => {
                                        onFieldChange("email", e.currentTarget.value);
                                    }}
                                />
                                <FormHelperText>{validationFields.email}</FormHelperText>
                            </FormControl>
                            <FormControl margin="normal" required fullWidth error={!!validationFields.fullName}>
                                <InputLabel htmlFor="fullName">Full Name</InputLabel>
                                <Input name="fullName" id="fullName" value={data.fullName}
                                    onChange={e => onFieldChange("fullName", e.currentTarget.value)}
                                />
                                <FormHelperText>{validationFields.fullName}</FormHelperText>
                            </FormControl>
                            <FormControl margin="normal" required fullWidth error={!!validationFields.password}>
                                <InputLabel htmlFor="password">Password</InputLabel>
                                <Input name="password" type="password" id="password" autoComplete="new-password" value={data.password}
                                    onChange={e => onFieldChange("password", e.currentTarget.value)}
                                />
                                <FormHelperText>{validationFields.password}</FormHelperText>
                            </FormControl>
                            <FormControl margin="normal" required fullWidth>
                                <TextField
                                    id="dailyCalorieLimit"
                                    type="number"
                                    label="Daily Calorie Limit"
                                    className={classes.textField}
                                    InputLabelProps={{
                                        shrink: true,
                                    }}
                                    margin="normal"
                                    value={data.dailyCalorieLimit || 0}
                                    onChange={e => {
                                        onFieldChange("dailyCalorieLimit", Number.parseInt(e.currentTarget.value, 10));
                                    }}
                                />
                            </FormControl>
                            <FormControl margin="normal" required fullWidth >
                                <InputLabel htmlFor="role">Role</InputLabel>
                                <Select
                                    id="role"
                                    value={data.role}
                                    onChange={(e) => { onFieldChange("role", e.currentTarget.value) }}
                                    inputProps={{
                                        name: "role",
                                        id: "role",
                                    }}
                                >
                                    {this.renderRoleItems()}
                                </Select>
                                <FormHelperText>{validationFields.role}</FormHelperText>
                            </FormControl>
                            {validationMessage ? <FormHelperText error>{validationMessage}</FormHelperText> : undefined}
                            {renderActionButtons(isValid)}
                        </Fragment>)
                    }}
                </ValidationForm>
            </Form>
        );
    }
}

UserForm.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(UserForm);