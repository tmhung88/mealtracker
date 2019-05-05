import React from 'react';
import PropTypes from 'prop-types';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import FormControl from '@material-ui/core/FormControl';
import withStyles from '@material-ui/core/styles/withStyles';
import { withPage } from '../AppPage';
import Form from '../common/form/Form';


const styles = theme => ({
    submit: {
        marginTop: theme.spacing.unit * 3,
    },
});

class UserSettings extends React.Component {
    state = { loading: true, userSettings: { calories: 0 } }

    async componentDidMount() {
        try {
            const response = await this.props.api.get(`/api/users/settings`);
            const json = await response.json();
            this.setState({
                userSettings: json,
            })
        } finally {
            this.setState({ loading: false });
        }
    }

    handleSubmit = async (e) => {
        e.preventDefault();
        e.preventDefault();
        this.setState({ loading: true });
        try {
            await this.props.api.put(`/api/users/settings`, this.state.userSettings);
        } finally {
            this.setState({ loading: false });
        }
    }
    render() {
        const { classes } = this.props;
        const { userSettings } = this.state;
        return (
            <Form
                formName="User Settings"
                loading={this.state.loading}
            >
                <FormControl margin="normal" required fullWidth>
                    <TextField
                        id="calories"
                        type="number"
                        label="Calories"
                        className={classes.textField}
                        InputLabelProps={{
                            shrink: true,
                        }}
                        value={userSettings.calories || 0}
                        onChange={e => {
                            this.setState({
                                userSettings: {
                                    ...userSettings,
                                    calories: Number.parseInt(e.currentTarget.value, 10),
                                }
                            });
                        }}
                        margin="normal"
                    />

                </FormControl>

                <Button
                    type="submit"
                    fullWidth
                    variant="contained"
                    color="primary"
                    className={classes.submit}
                    onClick={this.handleSubmit}
                >
                    Save
            </Button>
            </Form>
        );
    }
}

UserSettings.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withPage(withStyles(styles)(UserSettings));