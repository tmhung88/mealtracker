import React from 'react';
import PropTypes from 'prop-types';
import Button from '@material-ui/core/Button';
import withStyles from '@material-ui/core/styles/withStyles';

import { post, BadRequestError } from '../api';
import UserForm from './UserForm';
import { withPage } from '../AppPage';

const styles = theme => ({
    add: {
        marginTop: theme.spacing.unit * 3,
        marginLeft: theme.spacing.unit * 3,
        paddingLeft: theme.spacing.unit * 4,
        paddingRight: theme.spacing.unit * 4,
    },
    cancel: {
        marginTop: theme.spacing.unit * 3,
    }
});


class NewUser extends React.Component {
    state = {
        user: {
            calories: 0,
            email: "",
        },
        loading: false,
    }
    handleSubmit = async (e) => {
        this.setState({ loading: true });
        try {
            await post("/api/users", this.state.user);
            this.props.goBackOrReplace("/users");
        } catch (error) {
            if (error instanceof BadRequestError) {
                this.setState({
                    serverErrorFields: error.body.error.errorFields,
                })
            }
        } finally {
            this.setState({ loading: false });
        }
        console.log("finished");
    };

    handleUserChange = (user) => {
        this.setState({
            user: user,
        })
    }


    render() {
        const { classes } = this.props;
        return <UserForm
            serverErrorFields={this.state.serverErrorFields}
            onUserChange={this.handleUserChange}
            user={this.state.user}
            loading={this.state.loading}
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
                        className={classes.add}
                        onClick={(e) => {
                            e.preventDefault();

                            if (!isValid()) {
                                return;
                            }

                            this.handleSubmit(e)
                        }}
                    >
                        Add
                        </Button>
                </div>
            }}
        />
    }


}

NewUser.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withPage(withStyles(styles)(NewUser));