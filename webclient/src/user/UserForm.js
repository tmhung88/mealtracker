import React, { Fragment } from "react";
import TextField from "@material-ui/core/TextField";
import FormControl from "@material-ui/core/FormControl";
import withStyles from "@material-ui/core/styles/withStyles";
import Form from "../common/form/Form";
import ValidationForm from "../common/form/ValidationForm";
import FormHelperText from "@material-ui/core/FormHelperText";
import NotFoundForm from "../common/form/NotFoundForm";
import { InputLabel, Input, Select, MenuItem } from "@material-ui/core";
import { withPage } from "../core/components/AppPage";
import { Roles, roleIdToName } from "../core/userSession";
import { Pages } from '../constants/Pages';

const styles = () => ({

});


export class UserForm extends React.Component {

    renderRoleItems() {
        const { userSession } = this.props;
        switch (userSession.currentRole()) {
            case Roles.USER_MANAGER: return [
                <MenuItem key={Roles.REGULAR_USER} value={Roles.REGULAR_USER}>{roleIdToName(Roles.REGULAR_USER)}</MenuItem>,
                <MenuItem key={Roles.USER_MANAGER} value={Roles.USER_MANAGER}>{roleIdToName(Roles.USER_MANAGER)}</MenuItem>
            ];
            case Roles.ADMIN: return [
                <MenuItem key={Roles.REGULAR_USER} value={Roles.REGULAR_USER}>{roleIdToName(Roles.REGULAR_USER)}</MenuItem>,
                <MenuItem key={Roles.USER_MANAGER} value={Roles.USER_MANAGER}>{roleIdToName(Roles.USER_MANAGER)}</MenuItem>,
                <MenuItem key={Roles.ADMIN} value={Roles.ADMIN}>{roleIdToName(Roles.ADMIN)}</MenuItem>
            ];
            default: return [];
        }

    }

    render() {
        const { classes, renderActionButtons, onUserChange, loading, serverValidationError, notFound, passwordOptional } = this.props;
        const user = this.props.user || {
            dailyCalorieLimit: 0,
            password: "",
            fullName: "",
            role: "USER_MANAGER",
            email: "",
        };
        if (notFound) {
            return <NotFoundForm formName="User" backPage={Pages.USERS} />
        }
        return (
            <Form formName="User" loading={loading}>
                <ValidationForm
                    serverValidationError={serverValidationError}
                    constraints={{
                        email: {
                            email: true,
                            presence: { allowEmpty: false },
                            length: {
                                minimum: 5,
                                maximum: 200
                            }
                        },
                        fullName: {
                            presence: { allowEmpty: false },
                            length: {
                                minimum: 5,
                                maximum: 200
                            }
                        },
                        password: passwordOptional ? undefined : {
                            presence: { allowEmpty: false },
                            length: {
                                minimum: 5,
                                maximum: 100
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
                                    required
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
                            <FormControl margin="normal" required={!passwordOptional} fullWid   th error={!!validationFields.password}>
                                <InputLabel htmlFor="password">Password</InputLabel>
                                <Input name="password" type="password" id="password" autoComplete="new-password" value={data.password}
                                    onChange={e => onFieldChange("password", e.currentTarget.value)}
                                />
                                <FormHelperText>{validationFields.password}</FormHelperText>
                            </FormControl>
                            <FormControl margin="normal" required fullWidth>
                                <TextField
                                    required
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
                                <InputLabel htmlFor="role" shrink>Role</InputLabel>
                                <Select
                                    value={data.role}
                                    onChange={(e) => {
                                        onFieldChange("role", e.target.value);
                                    }}
                                    input={<Input name="role" id="role" />}
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

export default withPage(withStyles(styles)(UserForm));
