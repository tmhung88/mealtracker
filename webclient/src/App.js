import React from "react";
import { BrowserRouter as Router, Switch, Route, Link } from "react-router-dom";
import Login from "./user/Login";
import Dashboard from "./dashboard/Dashboard";
import Register from "./user/Register";

function AppRouter() {
  return (
    <Router>
      <div>
        <nav style={{position:"fixed", bottom:0, zIndex:1000000}}>
          <ul>
            <li>
              <Link to="/login">Login</Link>
            </li>
            <li>
              <Link to="/register">Register</Link>
            </li>
            <li>
              <Link to="/new-meal">New Meal</Link>
            </li>
            <li>
              <Link to="/meals">Meals</Link>
            </li>
          </ul>
        </nav>
        <Switch>
          <Route path="/login" component={Login} />
          <Route path="/register" component={Register} />
          <Route path="/*" component={Dashboard} />
        </Switch>


      </div>
    </Router>
  );
}

export default AppRouter;