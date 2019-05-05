import React from "react";
import { hasToken } from "./api";
import { Route, Redirect } from "react-router-dom";

export  class AppRoute extends React.Component {
    render() {
        const { component: Component, ...rest } = this.props;
        return (<Route
            {...rest}
            render={props =>
                hasToken() ? (
                    <Component {...props} />
                ) : (
                        <Redirect
                            to={{
                                pathname: "/users/login",
                                state: { from: props.location }
                            }}
                        />
                    )
            }
        />);
    }
}