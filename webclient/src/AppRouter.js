import React from "react";
import { Route, Redirect } from "react-router-dom";
import userSession from "./userSession";

export class AppRoute extends React.Component {
    render() {
        const { component: Component, ...rest } = this.props;
        return (<Route
            {...rest}
            render={(props) => {
                if (!userSession.isLoggedIn()) {
                    return <Redirect
                        to={{
                            pathname: "/users/login",
                            state: { from: props.location }
                        }}
                    />;
                }

                if (this.props.right && !userSession.hasRight(this.props.right)) {
                    return <Redirect
                        to={{
                            pathname: "/users/login",
                            state: { from: props.location }
                        }}
                    />;
                }
                return <Component {...props} />
            }}
        />);
    }
}