import React, { Fragment } from "react";
import { withRouter as withRouterFunc } from "react-router-dom";
import api, { UnauthorizedError, UnauthenticatedError, ServerError } from "../api";
import bluebird from "bluebird";
import userSession from "../userSession";
import { Pages } from "../../constants/Pages";
import { connect as reduxConnect } from "react-redux";
import {  showError, showSuccess } from "../actions";

export function withUserSession(ComponentNeedSession) {
    return function (props) {
        return <ComponentNeedSession {...props} userSession={userSession} />
    }
}

export function withPage(ComponentToProtect, { withRouter, connect } = {}) {
    withRouter = withRouter || withRouterFunc;
    connect = connect || reduxConnect;
    const mapStateToProps = state => {
        return {
        }
      }
      
      const mapDispatchToProps = dispatch => ({
        showError: message => dispatch(showError(message)),
        showSuccess: message => dispatch(showSuccess(message)),
        
      })
      
    return connect(mapStateToProps,mapDispatchToProps)(withRouter(class Wrap extends React.Component {
        state = {
            renderError: false,
        }
        
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
            } else if (serverError.body.error && serverError.body.error.message) {
                errorMessage = serverError.body.error.message;
            } else {
                errorMessage = JSON.stringify(serverError.body);
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
                this.props.showError(this.tryGetErrorMessage(error));
                
            } else {
                this.props.showError(error.message || JSON.stringify(error));
            }
        }

        goBackOrReplace = (path) => {
            if (this.props.history.length > 1) {
                this.props.history.goBack();
                return;
            }

            this.props.history.replace(path);

        }

        componentDidCatch(error, info) {
            this.setState({
                renderError: true,
            });
            console.error(error);
            console.error(info);
        }

        showSuccessMessage = (message) => {
            this.props.showSuccess(message);            
        }

        render() {
            if (this.state.renderError) {
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
                    showSuccessMessage={this.showSuccessMessage}
                    handleError={this.handleError} />
                
            </Fragment>;
        }
    }));
}
