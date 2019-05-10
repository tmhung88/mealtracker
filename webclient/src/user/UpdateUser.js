import React from 'react';
import Button from '@material-ui/core/Button';
import withStyles from '@material-ui/core/styles/withStyles';
import UserForm from './UserForm';
import { NotFoundRequestError, BadRequestError } from '../core/api';
import { withPage } from '../core/components/AppPage';
import { ApiUrl } from '../constants/ApiUrl';
import { Pages } from '../constants/Pages';

const styles = theme => ({
    update: {
        marginTop: theme.spacing.unit * 3,
        marginLeft: theme.spacing.unit * 3,
        paddingLeft: theme.spacing.unit * 4,
        paddingRight: theme.spacing.unit * 4,
    },
    cancel: {
        marginTop: theme.spacing.unit * 3,
    }
});


export class UpdateUser extends React.Component {
    state = {
        user: {
            calories: 0,
            email: "",
            fullName:"",
            password:"",
        },
        loading: true,
    }
    async componentDidMount() {
        try {
            const response = await this.props.api.get(`${ApiUrl.USERS}/${this.props.match.params.id}`);
            const json = await response.json();
            this.setState({
                user: json.data,
            })
        } catch (error) {
            if (error instanceof NotFoundRequestError) {
                this.setState({ user: null });
            } else {
                this.props.handleError(error);
            }
        } finally {
            this.setState({ loading: false });
        }
    }

    handleSubmit = async (e) => {
        this.setState({ loading: true });
            try {
                await this.props.api.put(`${ApiUrl.USERS}/${this.props.match.params.id}`, this.state.user);
                this.props.goBackOrReplace(Pages.USERS);
                this.props.showSuccessMessage("Update User successfully");
            } catch (error) {
                if (error instanceof BadRequestError) {
                    this.setState({
                        serverValidationError: error.body.error,
                    })
                } else {
                    this.props.handleError(error);
                }
            } finally {
                this.setState({ loading: false });
            }
    };


    handleUserChange = (user) => {
        this.setState({
            user: user,
        })
    }

    render() {
        const { classes } = this.props;
        return (
            <UserForm
                passwordOptional
                notFound={!this.state.user}
                serverValidationError={this.state.serverValidationError}
                loading={this.state.loading}
                onUserChange={this.handleUserChange}
                user={this.state.user}
                renderActionButtons={(isValid) => {
                    return <div>
                        <Button onClick={() => this.props.goBackOrReplace(Pages.USERS)}
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
                            className={classes.update}
                            onClick={(e) => {
                                e.preventDefault();

                                if (!isValid()) {
                                    return;
                                }

                                this.handleSubmit(e)
                            }}
                        >
                            Update
                        </Button>
                    </div>
                }}
            />
        );
    }
}

export default withPage(withStyles(styles)(UpdateUser));