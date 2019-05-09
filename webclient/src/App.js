import React from "react";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import Login from "./user/Login";
import Dashboard from "./dashboard/Dashboard";
import Register from "./user/Register";
import  AppRoute  from "./core/components/AppRoute";
import NotFound from "./common/NotFound";
import { Pages } from "./constants/Pages";

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
            <Route path={Pages.LOGIN} component={Login} />
            <Route path={Pages.REGISTER} component={Register} />
            <Route path={Pages.NOT_FOUND} component={NotFound} />
            <AppRoute path="/*" component={Dashboard} />
          </Switch>


        </div>
      </Router>
    );
  }
}

export default AppRouter;