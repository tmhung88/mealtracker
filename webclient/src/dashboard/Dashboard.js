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
import { Route, Link, Switch, withRouter } from "react-router-dom";
import NewMeal from "../meal/NewMeal";
import UpdateMeal from "../meal/UpdateMeal";
import MealList from "../meal/MealList";
import AllMealList from "../meal/AllMealList";
import UserList from "../user/UserList";
import UserSettings from "../user/UserSettings";
import UpdateUser from "../user/UpdateUser";
import NewUser from "../user/NewUser";
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import { styles } from "./DashboardStyles";
import { clearToken } from '../api';


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

  handleLogout = ()=>{
    clearToken();
    this.props.history.push("/users/login");
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
              <MenuItem component={Link} to="/users/settings" onClick={this.handleUserMenuClose}>
                Settings
                </MenuItem>
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
            <MainListItems selectedPathName={this.props.location.pathname}/>
          </List>
        </Drawer>
        <main className={classes.content}>
          <div className={classes.appBarSpacer} />
          <Switch>
            <Route path="/meals/new" component={NewMeal} />
            <Route path="/meals/:id/update" component={UpdateMeal} />
            <Route path="/meals" exact component={MealList} />
            <Route path="/meals/all" exact component={AllMealList} />
            <Route path="/users" exact component={UserList} />
            <Route path="/users/:id/update" exact component={UpdateUser} />
            <Route path="/users/new" exact component={NewUser} />
            <Route path="/users/settings" component={UserSettings} />
          </Switch>
        </main>
      </div>
    );
  }
}

Dashboard.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default  withRouter(withStyles(styles)(Dashboard));