import React from "react";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import FormControl from "@material-ui/core/FormControl";
import withStyles from "@material-ui/core/styles/withStyles";
import Form from "../common/form/Form";
import { withPage } from "../core/components/AppPage";
import { FormHelperText } from "@material-ui/core";
import teal from '@material-ui/core/colors/teal';
import { ApiUrl } from '../constants/ApiUrl';

const styles = theme => ({
    submit: {
        marginTop: theme.spacing.unit * 3,
    },
    sucessMessage: {
        color: teal[500],
    }
});

export class UserSettings extends React.Component {
    state = { loading: true, userSettings: { dailyCalorieLimit: 0 }, updateSuccessfully: false }

    async componentDidMount() {
        try {
            const response = await this.props.api.get(ApiUrl.ME);
            const json = await response.json();
            this.setState({
                userSettings: json.data,
            })
        } catch (e) {
            this.props.handleError(e);
        } finally {
            this.setState({ loading: false });
        }
    }

    handleSubmit = async (e) => {
        e.preventDefault();
        this.setState({ loading: true });
        try {
            await this.props.api.patch(ApiUrl.ME, this.state.userSettings);
            this.setState({
                updateSuccessfully: true,
            })
        }
        catch (e) {
            this.props.handleError(e);
        }
        finally {
            this.setState({ loading: false });
        }
    }
    render() {
        const { classes } = this.props;
        const { userSettings, updateSuccessfully } = this.state;
        return (
            <Form
                formName="User Settings"
                loading={this.state.loading}
            >
                <FormControl margin="normal" required fullWidth>
                    <TextField
                        id="dailyCalorieLimit"
                        type="number"
                        label="Daily Calorie Limit"
                        className={classes.textField}
                        InputLabelProps={{
                            shrink: true,
                        }}
                        value={userSettings.dailyCalorieLimit || 0}
                        onChange={e => {
                            this.setState({
                                userSettings: {
                                    ...userSettings,
                                    dailyCalorieLimit: Number.parseInt(e.currentTarget.value, 10),
                                },
                                updateSuccessfully: false,
                            });
                        }}
                        margin="normal"
                    />
                </FormControl>
                <FormHelperText classes={{ root: classes.sucessMessage }}>{updateSuccessfully ? "Update succesfully" : undefined}</FormHelperText>
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

export default withPage(withStyles(styles)(UserSettings));