import React from "react";
import { shallow } from "enzyme";
import { Table } from "../Table";
import TableCell from '@material-ui/core/TableCell';
import TableRow from '@material-ui/core/TableRow';
import TablePagination from '@material-ui/core/TablePagination';
import { TableHead } from "../TableHead";
import { Checkbox } from "@material-ui/core";
import TableToolbar from "../TableToolbar";

describe("#Table", () => {
    let tableState = {
        pagingInfo: {
            rowsPerPageOptions: [4, 8],
            total: 99,
            rowsPerPage: 4,
            pageIndex: 1,
        },
        orderInfo: {
            order: 'asc',
            orderBy: 'name',
        }
    }
    let rows = [
        { id: 1, name: "name1", email: "email 1" },
        { id: 2, name: "name2", email: "email 1" },
    ]
    let columns = [
        { id: "name", dataField: "name" },
        { id: "email", dataField: "email" },
    ]

    it("should pass correct data to inner components", () => {
        const wrapper = shallow(<Table classes={{}} tableState={tableState} rows={rows} columns={columns} />);
        const tableRows = wrapper.find(TableRow);
        expect(tableRows.at(0).key()).toEqual("1");
        expect(tableRows.at(1).key()).toEqual("2");

        const tablePagination = wrapper.find(TablePagination);
        expect(tablePagination.prop("rowsPerPageOptions")).toEqual([4, 8]);
        expect(tablePagination.prop("count")).toEqual(99);
        expect(tablePagination.prop("rowsPerPage")).toEqual(4);
        expect(tablePagination.prop("page")).toEqual(1);

        const tableHead = wrapper.find(TableHead);
        expect(tableHead.prop("columns")).toEqual(columns);
        expect(tableHead.prop("order")).toEqual("asc");
        expect(tableHead.prop("orderBy")).toEqual("name");
        expect(tableHead.prop("rowCount")).toEqual(2);
    })

    it("should raise events properly from inner components", () => {
        const onRowsPerPageChange = jest.fn();
        const onPageChange = jest.fn();
        const onRowSelect = jest.fn();
        const onDelete = jest.fn();
        const wrapper = shallow(<Table
            classes={{}}
            tableState={tableState}
            rows={rows}
            columns={columns}
            onRowsPerPageChange={onRowsPerPageChange}
            onPageChange={onPageChange}
            onRowSelect={onRowSelect}
            onDelete={onDelete}
        />);
        wrapper.setState({ selected: [1, 3] });

        wrapper.find(TableToolbar).simulate("delete");
        wrapper.find(TablePagination).simulate("changeRowsPerPage", { target: { value: 10 } });
        wrapper.find(TablePagination).simulate("changePage", null, 2);
        wrapper.find(TableRow).at(1).simulate("click");

        expect(onRowsPerPageChange).toHaveBeenCalledWith(10);
        expect(onPageChange).toHaveBeenCalledWith(2);
        expect(onRowSelect).toHaveBeenCalledWith(2);
        expect(onDelete).toHaveBeenCalledWith([1, 3]);

    })

    it("should render empty rows", () => {
        const wrapper = shallow(<Table classes={{}} tableState={tableState} rows={rows} columns={columns} />);
        const tableRows = wrapper.find(TableRow);
        expect(tableRows).toHaveLength(3);
        expect(tableRows.at(2).find(TableCell).prop("colSpan")).toEqual(3);
    })

    it("Render cell properly", () => {
        let rows = [
            { id: 1, name: "name1", email: "email 1", calories: 10 },
        ]
        let columns = [
            { id: "name", dataField: "name" },
            { id: "calories", dataField: "calories", numeric: true },
            { id: "email", dataField: "email", renderContent(d) { return d + "test" } },
        ]
        const wrapper = shallow(<Table classes={{}} tableState={tableState} rows={rows} columns={columns} />);
        const firstRow = wrapper.find(TableRow).at(0);
        const cells = firstRow.find(TableCell);
        expect(cells.at(0).find(Checkbox)).toHaveLength(1);
        expect(cells.at(1).childAt(0).text()).toEqual("name1");
        expect(cells.at(2).childAt(0).text()).toEqual("10");
        expect(cells.at(2).prop("align")).toEqual("right");
        expect(cells.at(3).childAt(0).text()).toEqual("email 1test");
    });

    describe("On sort", () => {
        it("should sort desc on sorted asc field", () => {
            const onSort = jest.fn();
            const wrapper = shallow(<Table onSort={onSort} classes={{}} tableState={tableState} rows={rows} columns={columns} />);
            const tableHead = wrapper.find(TableHead);
            tableHead.simulate("requestSort", null, "name");
            expect(onSort).toHaveBeenCalledWith("name", "desc");
        })

        it("should sort desc on non sorted field", () => {
            const onSort = jest.fn();
            const wrapper = shallow(<Table onSort={onSort} classes={{}} tableState={tableState} rows={rows} columns={columns} />);
            const tableHead = wrapper.find(TableHead);
            tableHead.simulate("requestSort", null, "email");
            expect(onSort).toHaveBeenCalledWith("email", "desc");
        })

        it("should sort asc on sorted desc field", () => {
            tableState = {
                ...tableState,
                orderInfo: {
                    order: 'desc',
                    orderBy: 'name',
                }
            }
            const onSort = jest.fn();
            const wrapper = shallow(<Table onSort={onSort} classes={{}} tableState={tableState} rows={rows} columns={columns} />);
            const tableHead = wrapper.find(TableHead);
            tableHead.simulate("requestSort", null, "name");
            expect(onSort).toHaveBeenCalledWith("name", "asc");
        })
    })

    describe("On Checkbox Row select", () => {
        it("should set selected on unselected", () => {
            const wrapper = shallow(<Table classes={{}} tableState={tableState} rows={rows} columns={columns} />);
            const checkboxes = wrapper.find(Checkbox);
            checkboxes.at(0).parent().simulate("click", { stopPropagation: jest.fn() });
            const selectedRows = wrapper.find(TableRow).filter(`[selected=true]`);
            expect(selectedRows.key()).toEqual("1");
        })

        it("should set unselected on selected", () => {
            const wrapper = shallow(<Table classes={{}} tableState={tableState} rows={rows} columns={columns} />);
            let checkboxes = wrapper.find(Checkbox);
            checkboxes.at(0).parent().simulate("click", { stopPropagation: jest.fn() });
            let selectedRows = wrapper.find(TableRow).filter(`[selected=true]`);
            expect(selectedRows.key()).toEqual("1");
            checkboxes = wrapper.find(Checkbox);
            checkboxes.at(0).parent().simulate("click", { stopPropagation: jest.fn() });
            selectedRows = wrapper.find(TableRow).filter(`[selected=true]`);
            expect(selectedRows).toHaveLength(0);
        })
    });

    describe("On Checkbox all select", () => {
        it("should check all rows on select all checked", () => {
            const wrapper = shallow(<Table classes={{}} tableState={tableState} rows={rows} columns={columns} />);
            wrapper.find(TableHead).simulate("selectAllClick", { target: { checked: true } });
            const selectedRows = wrapper.find(TableRow).filter(`[selected=true]`);
            expect(selectedRows).toHaveLength(2);
        })

        it("should uncheck all rows on select all unchecked", () => {
            const wrapper = shallow(<Table classes={{}} tableState={tableState} rows={rows} columns={columns} />);
            wrapper.find(TableHead).simulate("selectAllClick", { target: { checked: true } });
            let selectedRows = wrapper.find(TableRow).filter(`[selected=true]`);
            expect(selectedRows).toHaveLength(2);
            wrapper.find(TableHead).simulate("selectAllClick", { target: { checked: false } });
            selectedRows = wrapper.find(TableRow).filter(`[selected=true]`);
            expect(selectedRows).toHaveLength(0);
        })
    })
})