import React from 'react';
import Button from '@material-ui/core/Button';
import withStyles from '@material-ui/core/styles/withStyles';
import { Link } from "react-router-dom";
import { get } from '../api';
import { withPage } from '../AppPage';
import { EnhancedTable } from '../common/table/Table';
import { Loading } from '../common/loading/Loading';

const styles = theme => ({
    button: {
        margin: theme.spacing.unit,
        marginLeft: 0,
    },
})

const columns = [
    { id: 'email', dataField: "email", numeric: false, disablePadding: true, label: 'Email' },
    { id: 'calories', dataField: "calories", numeric: true, disablePadding: false, label: 'Calories' },
];

class UserList extends React.Component {
    state = { dataLoaded: false, data: [] };
    async componentDidMount() {
        const response = await get("/api/users");
        const json = await response.json();
        console.log(json);
        this.setState({ dataLoaded: true, data: json })
    }
    render() {
        const { classes } = this.props;
        return <div>
            <div className={classes.tableContainer}>
                <Loading
                    active={!this.state.dataLoaded}
                >
                    <EnhancedTable
                        onRowSelect={(id) => {
                            this.props.history.push(`/users/${id}/update`);
                        }}
                        rows={this.state.data} columns={columns} tableName="Users" />
                </Loading>
            </div>
            <Button component={Link} to="/users/new"
                variant="contained" color="primary" className={classes.button}>
                New User
      </Button>
        </div>
    }
}

export default withPage(withStyles(styles)(UserList));