import React from "react";
import { BrowserRouter as Router, Switch, Route, Link } from "react-router-dom";
import Login from "./user/Login";
import Dashboard from "./dashboard/Dashboard";
import Register from "./user/Register";

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
          <nav style={{ position: "fixed", bottom: 0, zIndex: 1000000 }}>
            <ul>
              <li>
                <Link to="/users/login">Login</Link>
              </li>
              <li>
                <Link to="/users/register">Register</Link>
              </li>
              <li>
                <Link to="/meals/new">New Meal</Link>
              </li>

              <li>
                <Link to="/meals">Meals</Link>
              </li>
              <li>
                <Link to="/users">Users</Link>
              </li>
              <li>
                <Link to="/users/settings">User Settings</Link>
              </li>
            </ul>
          </nav>
          <Switch>
            <Route path="/users/login" component={Login} />
            <Route path="/users/register" component={Register} />
            <Route path="/*" component={Dashboard} />
          </Switch>


        </div>
      </Router>
    );
  }
}

export default AppRouter;