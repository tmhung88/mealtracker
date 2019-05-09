import React, { Fragment } from "react";
import { withRouter as withRouterFunc } from "react-router-dom";
import api, { UnauthorizedError, UnauthenticatedError, ServerError } from "../api";
import bluebird from "bluebird";
import Snackbar from "@material-ui/core/Snackbar";
import userSession from "../userSession";
import SnackbarErrorMessage from "./SnackbarErrorMessage";
import { Pages } from "../../constants/Pages";

export function withUserSession(ComponentNeedSession) {
    return function (props) {
        return <ComponentNeedSession {...props} userSession={userSession} />
    }
}

export function withPage(ComponentToProtect, { withRouter } = {}) {
    withRouter = withRouter || withRouterFunc;
    return withRouter(class Wrap extends React.Component {
        state = { snackbarOpen: false, errorMessage: "", renderError: false }
        tryGetErrorMessage(serverError) {
            if (!serverError.body) {
                return serverError.message;
            }

            let errorMessage = serverError.message;
            if (typeof serverError.body === "string") {
                try {
                    const jsonObj = JSON.parse(serverError.body);
                    errorMessage = jsonObj.error.message;
                } catch{
                    errorMessage = serverError.body;
                }
            } else if(serverError.body.error && serverError.body.error.message) {
                errorMessage = serverError.body.error.message;
            } else {
                errorMessage =JSON.stringify(serverError.body);
            }

            return errorMessage;
        }
        handleError = (error) => {
            if (error instanceof UnauthorizedError || error instanceof UnauthenticatedError) {
                this.props.history.push(Pages.LOGIN);
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

        componentDidCatch(error, info){
            this.setState({
                renderError: true,
            });
            console.error(error);
            console.error(info);
        }

        render() {
            if(this.state.renderError) {
                return <div>
                    <span>There are some errors on rendering Page, please try to refresh this Page</span>
                </div>
            }
            return <Fragment>
                <ComponentToProtect
                    {...this.props}
                    userSession={userSession}
                    api={api}
                    goBackOrReplace={this.goBackOrReplace}
                    handleError={this.handleError}/>
                <Snackbar
                    anchorOrigin={{
                        vertical: "bottom",
                        horizontal: "center",
                    }}
                    open={this.state.snackbarOpen}
                    autoHideDuration={6000}
                    onClose={this.handleClose}
                >
                    <SnackbarErrorMessage
                        onClose={this.handleClose}
                        variant="error"
                        message={this.state.errorMessage}
                    />
                </Snackbar>
            </Fragment>;
        }
    });
}
