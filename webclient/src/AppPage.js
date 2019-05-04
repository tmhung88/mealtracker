import React from "react";
import { hasToken, UnauthorizedError } from "./api";
import { BrowserRouter as Router, Route, Link, Switch, withRouter } from "react-router-dom";

export function withPage(ComponentToProtect) {
    return withRouter(class Page extends React.Component {
        state = { error: false }
        componentWillMount() {
            if (!hasToken()) {
                this.props.history.push("/users/login");
                this.setState({ error: true });    
                // throw new UnauthorizedError();
            }
        }
        componentDidCatch() {
            this.setState({ error: true });
        }
        render() {
            if (this.state.error) {
                return <span></span>
            }
            return (<React.Fragment>
                <ComponentToProtect {...this.props} />
            </React.Fragment>);
        }
    })
}