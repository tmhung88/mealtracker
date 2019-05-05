import React from 'react';
import PropTypes from 'prop-types';
import TextField from '@material-ui/core/TextField';
import FormControl from '@material-ui/core/FormControl';
import withStyles from '@material-ui/core/styles/withStyles';
import Form from '../common/form/Form';

const styles = () => ({

});


class UserForm extends React.Component {

    render() {
        const { classes, renderActionButtons, onUserChange, loading } = this.props;
        const user = this.props.user || {
            datetime: new Date(),
            calories: 0,
            text: "",
        };
        return (
            <Form formName="Meal" loading={loading}>
                <FormControl margin="normal" required fullWidth>
                    <TextField
                        id="mail"
                        label="Mail"
                        multiline
                        className={classes.textField}
                        margin="normal"
                        value={user.email || ""}
                        onChange={e => {
                            onUserChange({
                                ...user,
                                email: e.currentTarget.value,
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
                        value={user.calories || 0}
                        onChange={e => {
                            onUserChange({
                                ...user,
                                calories: Number.parseInt(e.currentTarget.value, 10),
                            });
                        }}
                    />

                </FormControl>
                {renderActionButtons()}
            </Form>
        );
    }


}

UserForm.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(UserForm);