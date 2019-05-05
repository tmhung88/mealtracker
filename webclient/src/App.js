import React from "react";
import { BrowserRouter as Router, Switch, Route, Link } from "react-router-dom";
import Login from "./user/Login";
import Dashboard from "./dashboard/Dashboard";
import Register from "./user/Register";
import NotFound from "./NotFound";

class AppRouter extends React.Component{
  state = { error: false }
  componentDidCatch() {
    this.setState({ error: true });
  }
  render() {
    if (this.state.error) {
      return <span>Error</span>
    }
    return (
      <Router>
        <div>
          <Switch>
            <Route path="/users/login" component={Login} />
            <Route path="/users/register" component={Register} />
            <Route path="/not-found" component={NotFound} />
            <Route path="/*" component={Dashboard} />
          </Switch>


        </div>
      </Router>
    );
  }
}

export default AppRouter;