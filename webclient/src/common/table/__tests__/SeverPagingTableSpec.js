import React from "react";
import { shallow, render } from "enzyme";
import { ServerPagingTable } from "../ServerPagingTable";
import Bluebird from "bluebird";
import { Table } from "../Table";
import { Loading } from "../../loading/Loading";

describe("#ServerPagingTable", () => {
    let api;
    const baseUrl = "/api/list";
    const columns = [
        { id: "a", fieldName: "a" }
    ]

    beforeEach(() => {
        api = {
            get: jest.fn().mockResolvedValue({ json: jest.fn().mockResolvedValue({ data: "any-data" }) }),
            delete: jest.fn().mockResolvedValue({}),
        }
    })

    it("should request data on mounted", async () => {
        const wrapper = shallow(<ServerPagingTable classes={{}} api={api} baseUrl={baseUrl} columns={columns} />);
        expect(wrapper.find(Loading).prop("active")).toEqual(true);
        await Bluebird.delay(10);

        expect(api.get).toHaveBeenCalledWith("/api/list?&rowsPerPage=5&pageIndex=0&order=asc&orderBy=a");
        const table = wrapper.find(Table);
        expect(table.prop("rows")).toEqual("any-data");
        expect(wrapper.find(Loading).prop("active")).toEqual(false);
    })

    it("should handle paging information from server", async () => {
        api = {
            ...api,
            get: jest.fn().mockResolvedValue({
                json: jest.fn().mockResolvedValue({
                    data: "any-data",
                    metaData: {
                        totalElements: 300,
                    }
                })
            }),

        }

        const wrapper = shallow(<ServerPagingTable classes={{}} api={api} baseUrl={baseUrl} columns={columns} />);
        await Bluebird.delay(10);

        const table = wrapper.find(Table);
        expect(table.prop("tableState").pagingInfo.total).toEqual(300);

    })

    it("should handle error on fetch data request failed", async () => {
        api = {
            get: jest.fn().mockRejectedValue({ error: "any" }),
            delete: jest.fn().mockResolvedValue({}),
        }
        const handleError = jest.fn();
        const wrapper = shallow(<ServerPagingTable classes={{}} api={api} baseUrl={baseUrl} columns={columns} handleError={handleError} />);
        await Bluebird.delay(10);
        expect(handleError).toHaveBeenCalledWith({ error: "any" });
    })

    it("should request data when queryString change", async () => {
        const wrapper = shallow(<ServerPagingTable classes={{}} api={api} baseUrl={baseUrl} columns={columns} />);
        await Bluebird.delay(10);
        expect(api.get).toHaveBeenCalledWith("/api/list?&rowsPerPage=5&pageIndex=0&order=asc&orderBy=a");
        api.get.mockClear();
        wrapper.setProps({ queryString: "abc" });
        await Bluebird.delay(10);
        expect(api.get).toHaveBeenCalledWith("/api/list?abc&rowsPerPage=5&pageIndex=0&order=asc&orderBy=a");
    })

    it("should request on page index changed", async () => {
        const wrapper = shallow(<ServerPagingTable classes={{}} api={api} baseUrl={baseUrl} columns={columns} />);
        let table = wrapper.find(Table);
        table.simulate("pageChange", 10);
        expect(api.get).toHaveBeenCalledWith("/api/list?&rowsPerPage=5&pageIndex=10&order=asc&orderBy=a");

        await Bluebird.delay(10);
        table = wrapper.find(Table);
        expect(table.prop("tableState").pagingInfo.pageIndex).toEqual(10);
    })

    it("should request on rows per page changed",async () => {
        const wrapper = shallow(<ServerPagingTable classes={{}} api={api} baseUrl={baseUrl} columns={columns} />);
        let table = wrapper.find(Table);
        table.simulate("rowsPerPageChange", 20);
        expect(api.get).toHaveBeenCalledWith("/api/list?&rowsPerPage=20&pageIndex=0&order=asc&orderBy=a");
        await Bluebird.delay(10);
        table = wrapper.find(Table);
        expect(table.prop("tableState").pagingInfo.rowsPerPage).toEqual(20);
    })

    it("should request on sort", async () => {
        const wrapper = shallow(<ServerPagingTable classes={{}} api={api} baseUrl={baseUrl} columns={columns} />);
        let table = wrapper.find(Table);
        table.simulate("sort", "abc", "123");
        expect(api.get).toHaveBeenCalledWith("/api/list?&rowsPerPage=5&pageIndex=0&order=123&orderBy=abc");
        await Bluebird.delay(10);
        table = wrapper.find(Table);
        expect(table.prop("tableState").orderInfo).toEqual({
            order: "123",
            orderBy: "abc",
        });
    });

    it("should request delete and reload data", async () => {
        const wrapper = shallow(<ServerPagingTable classes={{}} api={api} baseUrl={baseUrl} columns={columns} />);
        const table = wrapper.find(Table);
        table.simulate("delete", [1, 2, 3]);
        await Bluebird.delay(10);
        expect(api.delete).toHaveBeenCalledWith("/api/list", { ids: [1, 2, 3] });
        expect(api.get).toBeCalledTimes(2);
    })

    it("should handle error on delete data request failed", async () => {
        api = {
            ...api,
            delete: jest.fn().mockRejectedValue({ error: "any" }),
        }
        const handleError = jest.fn();
        const wrapper = shallow(<ServerPagingTable classes={{}} api={api} baseUrl={baseUrl} columns={columns} handleError={handleError} />);
        const table = wrapper.find(Table);
        table.simulate("delete", [1, 2, 3]);
        await Bluebird.delay(10);
        expect(handleError).toHaveBeenCalledWith({ error: "any" });
    })
})