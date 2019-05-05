import React from "react";
import { withRouter } from "react-router-dom";
import { UnauthorizedError, UnauthenticatedError, get, put, post, deleteRequest } from "./api";
import bluebird from "bluebird";

export function withPage(ComponentToProtect) {

    return withRouter(class Wrap extends React.Component {
        handleUnAuth = (error) => {
            if (error instanceof UnauthorizedError || error instanceof UnauthenticatedError) {
                this.props.history.push("/users/login");
                /**delay to prevent component showing error */
                return bluebird.delay(1000);
            }

            throw error;
        }
        render() {
            const that= this;
            const api = {
                get: function () { return get.call(this, ...arguments).catch(that.handleUnAuth) },
                put: function () { return put.call(this, ...arguments).catch(that.handleUnAuth) },
                post: function () { return post.call(this, ...arguments).catch(that.handleUnAuth) },
                deleteRequest: function () { deleteRequest.call(this, ...arguments).catch(that.handleUnAuth) },
            }
            return <ComponentToProtect {...this.props} api={api} />;
        }
    });
}