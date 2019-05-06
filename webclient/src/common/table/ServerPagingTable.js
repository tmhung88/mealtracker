import React from "react";
import withStyles from '@material-ui/core/styles/withStyles';
import { EnhancedTable } from "./Table";
import { Loading } from "../loading/Loading";
import { withPage } from "../../AppPage";

const styles = {

}

class ServerPagingTable extends React.Component {
    state = {
        dataLoaded: false,
        data: [],
        tableState: {
            pagingInfo: {
                rowsPerPageOptions: [5, 10, 25],
                total: 200,
                rowsPerPage: 5,
                pageIndex: 0,
            },
            orderInfo: {
                order: 'asc',
                orderBy: this.props.columns[0].id,
            }
        }
    };
    async componentDidMount() {
        const baseGetUrl = this.props.baseGetUrl;
        try {
            const response = await this.props.api.get(baseGetUrl);
            const json = await response.json();
            console.log(json);
            this.setState({ dataLoaded: true, data: json })
        } catch {
            this.setState({ dataLoaded: true, data: [] })
        }

    }

    onPageChange = (pageIndex) => {
        this.setState({
            tableState:{
                ...this.state.tableState,
                pagingInfo: {
                    ...this.state.tableState.pagingInfo,
                    pageIndex,
                }
            }
        })
    }

    onRowsPerPageChange = (rowsPerPage) => {
        this.setState({
            tableState:{
                ...this.state.tableState,
                pagingInfo: {
                    ...this.state.tableState.pagingInfo,
                    rowsPerPage,
                }
            }
        })
    }

    onSort = (orderBy, order) => {
        this.setState({
            tableState:{
                ...this.state.tableState,
                orderInfo: {
                    ...this.state.tableState.orderInfo,
                    orderBy,
                    order
                }
            }
        })
    }
    render() {
        const { classes, columns, tableName } = this.props;
        return <div>
            <div className={classes.tableContainer}>
                <Loading
                    active={!this.state.dataLoaded}
                >
                    <EnhancedTable
                        tableState={this.state.tableState}
                        columns={columns}
                        tableName={tableName}
                        onRowSelect={this.props.onRowSelect}
                        onPageChange={this.onPageChange}
                        onRowsPerPageChange={this.onRowsPerPageChange}
                        onSort={this.onSort}
                        rows={this.state.data} />

                </Loading>
            </div>

        </div>
    }
}

export default withPage(withStyles(styles)(ServerPagingTable));