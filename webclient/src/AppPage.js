import React, { Fragment } from "react";
import { withRouter } from "react-router-dom";
import { UnauthorizedError, UnauthenticatedError, get, put, post, deleteRequest, ServerError } from "./api";
import bluebird from "bluebird";
import Snackbar from '@material-ui/core/Snackbar';
import SnackbarContent from '@material-ui/core/SnackbarContent';
import PropTypes from 'prop-types';
import classNames from 'classnames';
import CheckCircleIcon from '@material-ui/icons/CheckCircle';
import ErrorIcon from '@material-ui/icons/Error';
import InfoIcon from '@material-ui/icons/Info';
import CloseIcon from '@material-ui/icons/Close';
import green from '@material-ui/core/colors/green';
import amber from '@material-ui/core/colors/amber';
import IconButton from '@material-ui/core/IconButton';
import WarningIcon from '@material-ui/icons/Warning';
import { withStyles } from '@material-ui/core/styles';
import userSession from "./userSession";

export function withPage(ComponentToProtect) {

    return withRouter(class Wrap extends React.Component {
        state = { snackbarOpen: false, errorMessage: "" }
        tryGetErrorMessage(serverError) {
            if (!serverError.body) {
                return serverError.message;
            }

            let errorMessage = null;
            if (typeof serverError.body === "string") {
                try {
                    const jsonObj = JSON.parse(serverError.body);
                    errorMessage = jsonObj.error.message;
                } catch{
                    errorMessage = serverError.body;
                }
            } else {
                errorMessage = serverError.body.error.message;
            }


            return errorMessage;
        }
        handleError = (error) => {
            if (error instanceof UnauthorizedError || error instanceof UnauthenticatedError) {
                this.props.history.push("/users/login");
                /**delay to prevent component showing error */
                return bluebird.delay(1000);
            }
            else if (error instanceof ServerError) {
                this.setState({
                    snackbarOpen: true,
                    errorMessage: this.tryGetErrorMessage(error),
                })
            } else {
                this.setState({
                    snackbarOpen: true,
                    errorMessage: error.message || JSON.stringify(error),
                })
            }

            throw error;
        }
        handleClose = () => {
            this.setState({ snackbarOpen: false });
        }

        goBackOrReplace = (path) => {
            if (this.props.history.length > 1) {
                this.props.history.goBack();
                return;
            }

            this.props.history.replace(path);

        }

        handleErrorContext = (func) => {
            return Promise.resolve(func()).catch(this.handleError);
        }

        render() {
            const api = {
                get: get,
                put: put,
                post: post,
                delete: deleteRequest,
            }
            return <Fragment>
                <ComponentToProtect
                    {...this.props}
                    userSession={userSession} 
                    api={api} 
                    goBackOrReplace={this.goBackOrReplace} 
                    handleError={this.handleError}
                    handleErrorContext={this.handleErrorContext} />
                <Snackbar
                    anchorOrigin={{
                        vertical: 'bottom',
                        horizontal: 'center',
                    }}
                    open={this.state.snackbarOpen}
                    autoHideDuration={6000}
                    onClose={this.handleClose}
                >
                    <MySnackbarContentWrapper
                        onClose={this.handleClose}
                        variant="error"
                        message={this.state.errorMessage}
                    />
                </Snackbar>
            </Fragment>;
        }
    });
}

const variantIcon = {
    success: CheckCircleIcon,
    warning: WarningIcon,
    error: ErrorIcon,
    info: InfoIcon,
};

const styles1 = theme => ({
    success: {
        backgroundColor: green[600],
    },
    error: {
        backgroundColor: theme.palette.error.dark,
    },
    info: {
        backgroundColor: theme.palette.primary.dark,
    },
    warning: {
        backgroundColor: amber[700],
    },
    icon: {
        fontSize: 20,
    },
    iconVariant: {
        opacity: 0.9,
        marginRight: theme.spacing.unit,
    },
    message: {
        display: 'flex',
        alignItems: 'center',
    },
});

function MySnackbarContent(props) {
    const { classes, className, message, onClose, variant, ...other } = props;
    const Icon = variantIcon[variant];

    return (
        <SnackbarContent
            className={classNames(classes[variant], className)}
            aria-describedby="client-snackbar"
            message={
                <span id="client-snackbar" className={classes.message}>
                    <Icon className={classNames(classes.icon, classes.iconVariant)} />
                    {message}
                </span>
            }
            action={[
                <IconButton
                    key="close"
                    aria-label="Close"
                    color="inherit"
                    className={classes.close}
                    onClick={onClose}
                >
                    <CloseIcon className={classes.icon} />
                </IconButton>,
            ]}
            {...other}
        />
    );
}

MySnackbarContent.propTypes = {
    classes: PropTypes.object.isRequired,
    className: PropTypes.string,
    message: PropTypes.node,
    onClose: PropTypes.func,
    variant: PropTypes.oneOf(['success', 'warning', 'error', 'info']).isRequired,
};

const MySnackbarContentWrapper = withStyles(styles1)(MySnackbarContent);