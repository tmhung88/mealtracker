import React from 'react';
import Button from '@material-ui/core/Button';
import withStyles from '@material-ui/core/styles/withStyles';

import UserForm from './UserForm';
import { BadRequestError } from '../core/api';
import { withPage } from '../core/components/AppPage';
import { Roles } from '../core/userSession';
import { ApiUrl } from '../constants/ApiUrl';
import { Pages } from '../constants/Pages';

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


export class NewUser extends React.Component {
    state = {
        user: {
            dailyCalorieLimit: 0,
            email: "",
            fullName:"",
            password:"",
            role: Roles.REGULAR_USER,
        },
        loading: false,
    }
    handleSubmit = async (e) => {
        this.setState({ loading: true });
            try {
                await this.props.api.post(ApiUrl.USERS, this.state.user);
                this.props.goBackOrReplace(Pages.USERS);
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
        return <UserForm
            serverValidationError={this.state.serverValidationError}
            onUserChange={this.handleUserChange}
            user={this.state.user}
            loading={this.state.loading}
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

export default withPage(withStyles(styles)(NewUser));