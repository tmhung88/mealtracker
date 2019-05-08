import React from "react";
import withStyles from '@material-ui/core/styles/withStyles';
import { EnhancedTable } from "./Table";
import { Loading } from "../loading/Loading";
import { withPage } from "../../AppPage";

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

    componentDidUpdate(prevProps) {
        if (this.props.queryString !== prevProps.queryString) {
            this.requestData();
        }
    }

    buildPagingQuery(pagingInfo) {
        return `rowsPerPage=${pagingInfo.rowsPerPage}&pageIndex=${pagingInfo.pageIndex}`;
    }

    buildOrderQUery(orderInfo) {
        return `order=${orderInfo.order}&orderBy=${orderInfo.orderBy}`;
    }

    buildQueryString(tableState) {
        return `${this.buildPagingQuery(tableState.pagingInfo)}&${this.buildOrderQUery(tableState.orderInfo)}`;
    }

    async requestData(tableState = this.state.tableState) {
        const { baseUrl, queryString } = this.props;
        await this.props.handleErrorContext(async () => {
            try {
                this.setState({ loading: true })
                const response = await this.props.api.get(`${baseUrl}?${queryString || ""}&${this.buildQueryString(tableState)}`);
                const json = await response.json();
                console.log(json);
                this.setState({ loading: false, data: json.data })
            } finally {
                this.setState({ loading: false })
            }
        })
    }

    async componentDidMount() {
        await this.requestData();
    }

    onPageChange = async (pageIndex) => {
        this.setState({ loading: true });
        const newTableState = {
            ...this.state.tableState,
            pagingInfo: {
                ...this.state.tableState.pagingInfo,
                pageIndex,
            }
        };
        await this.requestData(newTableState);
        this.setState({
            tableState: newTableState,
            loading: false
        })

    }

    onRowsPerPageChange = async (rowsPerPage) => {
        this.setState({ loading: true });
        const newTableState = {
            ...this.state.tableState,
            pagingInfo: {
                ...this.state.tableState.pagingInfo,
                rowsPerPage,
            }
        }
        await this.requestData(newTableState);
        this.setState({
            tableState: newTableState,
            loading: false
        })

    }

    onSort = async (orderBy, order) => {
        this.setState({ loading: true });
        const newTableState = {
            ...this.state.tableState,
            orderInfo: {
                ...this.state.tableState.orderInfo,
                orderBy,
                order
            }
        };
        await this.requestData(newTableState);
        this.setState({
            tableState: newTableState,
            loading: false
        })

    }

    handleDelete = async (selectedIds) => {
        this.setState({ loading: true });
        console.log("selectedIds", selectedIds)
        await this.props.api.delete(this.props.baseUrl, { ids: selectedIds });
        await this.requestData();
        this.setState({ loading: false });
    }

    render() {
        const { classes, columns, tableName } = this.props;
        return <div>
            <div className={classes.tableContainer}>
                <Loading
                    active={this.state.loading}
                >
                    <EnhancedTable
                        onDelete={this.handleDelete}
                        tableState={this.state.tableState}
                        columns={columns}
                        tableName={tableName}
                        onRowSelect={this.props.onRowSelect}
                        onPageChange={this.onPageChange}
                        onRowsPerPageChange={this.onRowsPerPageChange}
                        onSort={this.onSort}
                        rows={this.state.data}
                        onRefresh={() => this.requestData()} />

                </Loading>
            </div>

        </div>
    }
}

export default withPage(withStyles(styles)(ServerPagingTable));