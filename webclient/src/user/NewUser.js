import React from 'react';
import PropTypes from 'prop-types';
import Button from '@material-ui/core/Button';
import withStyles from '@material-ui/core/styles/withStyles';
import { Link } from "react-router-dom";
import { post } from '../api';
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
            datetime: new Date(),
            calories: 0,
            text: "User Info",
        },
        loading: false,
    }
    handleSubmit = async (e) => {
        e.preventDefault();
        this.setState({ loading: true });
        try {
            await post("/api/users", this.state.user);
            this.props.history.replace("/users")
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
            onUserChange={this.handleUserChange}
            user={this.state.user}
            loading={this.state.loading}
            renderActionButtons={() => {
                return <div>
                    <Button component={Link} to="/users"
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
                        onClick={this.handleSubmit}
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