import React from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';
import { withStyles } from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import Drawer from '@material-ui/core/Drawer';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import List from '@material-ui/core/List';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import AccountCircleIcon from '@material-ui/icons/AccountCircle';
import { MainListItems } from './ListItems';
import { Link, Switch, withRouter, Redirect } from "react-router-dom";

import MyNewMeal from "../meal/form/my/MyNewMeal";
import MyUpdateMeal from "../meal/form/my/MyUpdateMeal";

import ManagementNewMeal from "../meal/form/management/ManagementNewMeal";
import ManagementUpdateMeal from "../meal/form/management/ManagementUpdateMeal";

import MealList from "../meal/list/MealList";
import AllMealList from "../meal/list/AllMealList";
import UserList from "../user/UserList";
import UserSettings from "../user/UserSettings";
import UpdateUser from "../user/UpdateUser";
import NewUser from "../user/NewUser";
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import { styles } from "./DashboardStyles";
import AppRoute from '../core/components/AppRoute';
import { ShowWithRight, Rights } from '../core/userSession';
import { withUserSession } from '../core/components/AppPage';
import { Pages } from "../constants/Pages";

class Dashboard extends React.Component {
  state = {
    open: true,
    anchorEl: null,
  };

  handleDrawerOpen = () => {
    this.setState({ open: true });
  };

  handleDrawerClose = () => {
    this.setState({ open: false });
  };

  handleUserMenuClose = () => {
    this.setState({ anchorEl: null });
  }
  handleUserMenuClick = event => {
    this.setState({ anchorEl: event.currentTarget });
  };

  handleLogout = () => {
    this.props.userSession.logout();
    this.props.history.push(Pages.LOGIN);
  }
  render() {
    const { classes } = this.props;
    console.log(this.props.location.pathname);
    return (
      <div className={classes.root}>
        <CssBaseline />
        <AppBar
          position="absolute"
          className={classNames(classes.appBar, this.state.open && classes.appBarShift)}
        >
          <Toolbar disableGutters={!this.state.open} className={classes.toolbar}>
            <IconButton
              color="inherit"
              aria-label="Open drawer"
              onClick={this.handleDrawerOpen}
              className={classNames(
                classes.menuButton,
                this.state.open && classes.menuButtonHidden,
              )}
            >
              <MenuIcon />
            </IconButton>
            <Typography
              component="h1"
              variant="h6"
              color="inherit"
              noWrap
              className={classes.title}
            >
              Dashboard
            </Typography>
            <IconButton color="inherit" onClick={this.handleUserMenuClick} >
              <AccountCircleIcon />              
            </IconButton>
            <Menu
              id="simple-menu"
              anchorEl={this.state.anchorEl}
              open={Boolean(this.state.anchorEl)}
              onClose={this.handleUserMenuClose}
            >
              <ShowWithRight right={Rights.MyMeal}>
                <MenuItem component={Link} to={Pages.MY_SETTINGS} onClick={this.handleUserMenuClose}>
                  Settings
                </MenuItem>
              </ShowWithRight>

              <MenuItem onClick={this.handleLogout}>Logout</MenuItem>
            </Menu>
          </Toolbar>
        </AppBar>
        <Drawer
          variant="permanent"
          classes={{
            paper: classNames(classes.drawerPaper, !this.state.open && classes.drawerPaperClose),
          }}
          open={this.state.open}
        >
          <div className={classes.toolbarIcon}>
            <IconButton onClick={this.handleDrawerClose}>
              <ChevronLeftIcon />
            </IconButton>
          </div>
          <Divider />
          <List>
            <MainListItems selectedPathName={this.props.location.pathname} />
          </List>
        </Drawer>
        <main className={classes.content}>
          <div className={classes.appBarSpacer} />
          <Switch>
            <AppRoute right={Rights.AllMeal} path={Pages.ALL_MEALS} exact component={AllMealList} />
            <AppRoute right={Rights.AllMeal} path={Pages.ALL_NEW_MEAL} component={ManagementNewMeal} />
            <AppRoute right={Rights.AllMeal} path={Pages.ALL_UPDATE_MEAL} component={ManagementUpdateMeal} />

            <AppRoute right={Rights.MyMeal} path={Pages.MY_MEALS} exact component={MealList} />
            <AppRoute right={Rights.MyMeal} path={Pages.MY_NEW_MEAL} component={MyNewMeal} />
            <AppRoute right={Rights.MyMeal} path={Pages.MY_UPDATE_MEAL} component={MyUpdateMeal} />

            <AppRoute right={Rights.MyMeal} path={Pages.MY_SETTINGS} component={UserSettings} />

            <AppRoute right={Rights.UserManagement} path={Pages.USERS} exact component={UserList} />
            <AppRoute right={Rights.UserManagement} path={Pages.UPDATE_USER} exact component={UpdateUser} />
            <AppRoute right={Rights.UserManagement} path={Pages.NEW_USER} exact component={NewUser} />
            <Redirect from="/" exact to={Pages.MY_MEALS} />
            <Redirect to={Pages.NOT_FOUND} />
          </Switch>
        </main>
      </div>
    );
  }
}

Dashboard.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withUserSession(withRouter(withStyles(styles)(Dashboard)));