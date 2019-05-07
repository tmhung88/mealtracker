import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import TextField from '@material-ui/core/TextField';
import FormControl from '@material-ui/core/FormControl';
import withStyles from '@material-ui/core/styles/withStyles';
import Form from '../common/form/Form';
import ValidationForm from '../common/form/ValidationForm';
import FormHelperText from '@material-ui/core/FormHelperText';

const styles = () => ({

});


class UserForm extends React.Component {

    render() {
        const { classes, renderActionButtons, onUserChange, loading, serverErrorFields } = this.props;
        const user = this.props.user || {
            calories: 0,
            email: "",
        };
        return (
            <Form formName="User" loading={loading}>
                <ValidationForm
                    serverErrorFields={serverErrorFields}
                    constraints={{
                        email: {
                            email: true,
                            presence: true,
                        },
                    }}
                    data={user}
                    onDataChange={(user) => onUserChange(user)}
                >
                    {({ onFieldChange, data, isValid, validationResult }) => {
                        return (<Fragment>
                            <FormControl margin="normal" required fullWidth error={!!validationResult.email}>
                                <TextField
                                    id="mail"
                                    label="Mail"
                                    className={classes.textField}
                                    margin="normal"
                                    value={data.email || ""}
                                    onChange={e => {
                                        onFieldChange("email", e.currentTarget.value);
                                    }}
                                />
                                <FormHelperText>{validationResult.email}</FormHelperText>
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
                                    value={data.calories || 0}
                                    onChange={e => {
                                        onFieldChange("calories", Number.parseInt(e.currentTarget.value, 10));
                                    }}
                                />

                            </FormControl>
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