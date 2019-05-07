import React from 'react';
import PropTypes from 'prop-types';
import Button from '@material-ui/core/Button';
import withStyles from '@material-ui/core/styles/withStyles';
import { withPage } from '../AppPage';
import UserForm from './UserForm';
import { BadRequestError } from '../api';

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


class UpdateUser extends React.Component {
    state = {
        user: {
            calories: 0,
            email: ""
        },
        loading: true,
    }
    async componentDidMount() {
        try {
            const response = await this.props.api.get(`/api/users/${this.props.match.params.id}`);
            const json = await response.json();
            this.setState({
                user: json,
            })
        } finally {
            this.setState({ loading: false });
        }
    }

    handleSubmit = async (e) => {
        this.props.handleErrorContext(async () => {
            e.preventDefault();
            this.setState({ loading: true });
            try {
                await this.props.api.put(`/api/users/${this.state.user.id}`, this.state.user);
                this.props.goBackOrReplace("/users");
            } catch (error) {
                if (error instanceof BadRequestError) {
                    this.setState({
                        serverValidationError: error.body.error,
                    })
                }
            } finally {
                this.setState({ loading: false });
            }
            console.log("finished");
        })
        
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