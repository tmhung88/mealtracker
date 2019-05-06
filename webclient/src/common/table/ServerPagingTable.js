import React from "react";
import withStyles from '@material-ui/core/styles/withStyles';
import { EnhancedTable } from "./Table";
import { Loading } from "../loading/Loading";
import { withPage } from "../../AppPage";
import Bluebird from "bluebird";

const styles = {

}

class ServerPagingTable extends React.Component {
    state = {
        loading: true,
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
            this.setState({ loading: false, data: json })
        } catch {
            this.setState({ loading: false, data: [] })
        }

    }

    onPageChange = async (pageIndex) => {
        this.setState({ loading: true });
        await Bluebird.delay(1000);
        this.setState({
            tableState: {
                ...this.state.tableState,
                pagingInfo: {
                    ...this.state.tableState.pagingInfo,
                    pageIndex,
                }
            },
            loading: false
        })

    }

    onRowsPerPageChange = async (rowsPerPage) => {
        this.setState({ loading: true });
        await Bluebird.delay(1000);
        this.setState({
            tableState: {
                ...this.state.tableState,
                pagingInfo: {
                    ...this.state.tableState.pagingInfo,
                    rowsPerPage,
                }
            },
            loading: false
        })

    }

    onSort = async (orderBy, order) => {
        this.setState({ loading: true });
        await Bluebird.delay(1000);
        this.setState({
            tableState: {
                ...this.state.tableState,
                orderInfo: {
                    ...this.state.tableState.orderInfo,
                    orderBy,
                    order
                }
            },
            loading: false
        })

    }
    render() {
        const { classes, columns, tableName } = this.props;
        return <div>
            <div className={classes.tableContainer}>
                <Loading
                    active={this.state.loading}
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