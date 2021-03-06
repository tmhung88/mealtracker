import React from "react";
import { Route, Redirect } from "react-router-dom";
import { withUserSession } from "./AppPage";
import { Pages } from "../../constants/Pages";

export class AppRoute extends React.Component {
    render() {
        const { component: Component, right, userSession, ...rest } = this.props;
        return (<Route
            {...rest}
            render={(props) => {
                const isLoggedIn = userSession.isLoggedIn();
                const noRightPermission = right && !userSession.hasRight(right);
                if(!isLoggedIn || noRightPermission) {
                    return <Redirect
                        to={{
                            pathname: Pages.LOGIN,
                            state: { from: props.location }
                        }}
                    />;
                }                
                return <Component {...props} />
            }}
        />);
    }
}

export default withUserSession(AppRoute);