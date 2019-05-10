import React from "react";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import Login from "./user/Login";
import Dashboard from "./dashboard/Dashboard";
import Register from "./user/Register";
import  AppRoute  from "./core/components/AppRoute";
import NotFound from "./common/NotFound";
import { Pages } from "./constants/Pages";
import { connect } from "react-redux";
import Snackbar from "@material-ui/core/Snackbar";
import SnackbarErrorMessage from "./core/components/SnackbarErrorMessage";
import { closeSnackbar } from "./core/actions";


export class App extends React.Component{
  state = { error: false }
  componentDidCatch() {
    this.setState({ error: true });
  }
  render() {
    if (this.state.error) {
      return <span>Error</span>
    }

    const {snackbarInfo,closeSnackbar} = this.props;
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
        <Snackbar
            anchorOrigin={{
                vertical: "bottom",
                horizontal: "center",
            }}
            open={snackbarInfo.show}
            autoHideDuration={6000}
            onClose={closeSnackbar}
        >
            <SnackbarErrorMessage
                onClose={closeSnackbar}
                variant={snackbarInfo.variant}
                message={snackbarInfo.message}
            />
        </Snackbar>
      </Router>
    );
  }
}

const mapStateToProps = state => {
  return {
    snackbarInfo: state.snackbarInfo,
  }
}

const mapDispatchToProps = dispatch => ({
  closeSnackbar: id => dispatch(closeSnackbar(id))
})

export default connect(mapStateToProps,mapDispatchToProps)(App);