import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TablePagination from '@material-ui/core/TablePagination';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import Checkbox from '@material-ui/core/Checkbox';
import { EnhancedTableToolbar } from './TableToolbar';
import { EnhancedTableHead } from './TableHead';


const styles = theme => ({
    root: {
        width: '100%',
        marginTop: theme.spacing.unit * 3,
    },
    table: {
        minWidth: 400,
    },
    tableWrapper: {
        overflowX: 'auto',
    },
});


export class EnhancedTable extends React.Component {
    state = {
        selected: [],
        data: this.props.rows,
    };

    getPagingInfo() {
        return this.props.tableState.pagingInfo || tableState.pagingInfo;
    }

    getOrderInfo() {
        return this.props.tableState.orderInfo || tableState.orderInfo;
    }

    componentWillReceiveProps(nextProps) {
        if (this.props.rows !== nextProps.rows) {
            this.setState({ data: nextProps.rows, selected: [] });
        }
    }

    handleRequestSort = (event, property) => {
        const orderBy = property;
        let order = 'desc';
        const orderInfo = this.getOrderInfo();
        if (orderInfo.orderBy === property && orderInfo.order === 'desc') {
            order = 'asc';
        }
        this.props.onSort(orderBy, order);
    };

    handleSelectAllClick = event => {
        if (event.target.checked) {
            this.setState(state => ({ selected: state.data.map(n => n.id) }));
            return;
        }
        this.setState({ selected: [] });
    };

    handleCheckboxClick = (event, id) => {
        event.stopPropagation();
        const { selected } = this.state;
        const selectedIndex = selected.indexOf(id);
        let newSelected = [];

        if (selectedIndex === -1) {
            newSelected = newSelected.concat(selected, id);
        } else if (selectedIndex === 0) {
            newSelected = newSelected.concat(selected.slice(1));
        } else if (selectedIndex === selected.length - 1) {
            newSelected = newSelected.concat(selected.slice(0, -1));
        } else if (selectedIndex > 0) {
            newSelected = newSelected.concat(
                selected.slice(0, selectedIndex),
                selected.slice(selectedIndex + 1),
            );
        }

        this.setState({ selected: newSelected });
    };

    handleChangePage = (event, page) => {
        this.props.onPageChange(page);
    };

    handleChangeRowsPerPage = event => {
        this.props.onRowsPerPageChange(event.target.value);
        // this.setState({ rowsPerPage: event.target.value });
    };

    isSelected = id => this.state.selected.indexOf(id) !== -1;

    render() {
        const { classes, columns, tableName, onRowSelect } = this.props;
        const { data, selected } = this.state;
        const pagingInfo = this.getPagingInfo();
        const emptyRows = pagingInfo.rowsPerPage - Math.min(pagingInfo.rowsPerPage, data.length);
        const orderInfo = this.getOrderInfo();
        console.log("orderInfo", orderInfo);
        return (
            <Paper className={classes.root}>
                <EnhancedTableToolbar
                    numSelected={selected.length}
                    tableName={tableName}
                    onDelete={() => this.props.onDelete(this.state.selected)}
                    onRefresh={this.props.onRefresh}
                     />
                <div className={classes.tableWrapper}>
                    <Table className={classes.table} aria-labelledby="tableTitle" >
                        <EnhancedTableHead
                            columns={columns}
                            numSelected={selected.length}
                            order={orderInfo.order}
                            orderBy={orderInfo.orderBy}
                            onSelectAllClick={this.handleSelectAllClick}
                            onRequestSort={this.handleRequestSort}
                            rowCount={data.length}
                        />
                        <TableBody>
                            {
                                data.map(n => {
                                    const isSelected = this.isSelected(n.id);
                                    return (
                                        <TableRow
                                            hover

                                            onClick={() => onRowSelect(n.id)}
                                            role="checkbox"
                                            aria-checked={isSelected}
                                            tabIndex={-1}
                                            key={n.id}
                                            selected={isSelected}
                                        >
                                            <TableCell padding="checkbox" onClick={event => this.handleCheckboxClick(event, n.id)}>
                                                <Checkbox checked={isSelected} />
                                            </TableCell>
                                            {columns.map(c => {
                                                return <TableCell key={c.id} align={c.numeric ? "right" : "left"} >
                                                    {c.renderContent ? c.renderContent(n[c.dataField]) : n[c.dataField]}
                                                </TableCell>
                                            })}
                                        </TableRow>
                                    );
                                })}
                            {emptyRows > 0 && (
                                <TableRow style={{ height: 49 * emptyRows }}>
                                    <TableCell colSpan={6} />
                                </TableRow>
                            )}
                        </TableBody>
                    </Table>
                </div>
                <TablePagination
                    rowsPerPageOptions={pagingInfo.rowsPerPageOptions}
                    component="div"
                    count={pagingInfo.total}
                    rowsPerPage={pagingInfo.rowsPerPage}
                    page={pagingInfo.pageIndex}
                    backIconButtonProps={{
                        'aria-label': 'Previous Page',
                    }}
                    nextIconButtonProps={{
                        'aria-label': 'Next Page',
                    }}
                    onChangePage={this.handleChangePage}
                    onChangeRowsPerPage={this.handleChangeRowsPerPage}
                />
            </Paper>
        );
    }
}

const tableState = {
    pagingInfo: {
        rowsPerPageOptions: [5, 10, 25],
        total: 200,
        rowsPerPage: 5,
        pageIndex: 0,
    },
    orderInfo: {
        order: 'asc',
        orderBy: 'calories',
    }
}


EnhancedTable.propTypes = {
    classes: PropTypes.object.isRequired,
};

EnhancedTable = withStyles(styles)(EnhancedTable);