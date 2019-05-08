import React from 'react';
import PropTypes from 'prop-types';
import Button from '@material-ui/core/Button';
import withStyles from '@material-ui/core/styles/withStyles';
import { withPage } from '../AppPage';
import UserForm from './UserForm';
import { BadRequestError, NotFoundRequestError } from '../api';

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
            const response = await this.props.api.get(`/v1/users/${this.props.match.params.id}`);
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
                await this.props.api.put(`/v1/users/${this.props.match.params.id}`, this.state.user);
                this.props.goBackOrReplace("/users");
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
                notFound={!this.state.user}
                serverValidationError={this.state.serverValidationError}
                loading={this.state.loading}
                onUserChange={this.handleUserChange}
                user={this.state.user}
                renderActionButtons={(isValid) => {
                    return <div>
                        <Button onClick={() => this.props.goBackOrReplace("/users")}
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

UpdateUser.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withPage(withStyles(styles)(UpdateUser));