import React from "react";
import { shallow } from "enzyme";
import { TableHead } from "../TableHead";
import { Checkbox, TableSortLabel, TableCell } from "@material-ui/core";

describe("#TableHead", () => {
    it("should disable checkbox when rowCount = 0", ()=>{
        const wrapper = shallow(<TableHead numSelected={2} rowCount={0} columns={[]}/>);
        expect(wrapper.find(Checkbox).prop("disabled")).toEqual(true);
    })
    it("should check when numSelected = rowCount and rowCount > 0", ()=>{
        const wrapper = shallow(<TableHead numSelected={2} rowCount={2} columns={[]}/>);
        expect(wrapper.find(Checkbox).prop("checked")).toEqual(true);
    })

    it("should uncheck when rowCount = 0", ()=>{
        const wrapper = shallow(<TableHead numSelected={2} rowCount={0} columns={[]}/>);
        expect(wrapper.find(Checkbox).prop("checked")).toEqual(false);
    })

    it("should uncheck and itermidiate true when numSelected < rowCount", ()=>{
        const wrapper = shallow(<TableHead numSelected={1} rowCount={2} columns={[]}/>);
        expect(wrapper.find(Checkbox).prop("checked")).toEqual(false);
        expect(wrapper.find(Checkbox).prop("indeterminate")).toEqual(true);
    })

    it("should uncheck and itermidiate false when numSelected =0", ()=>{
        const wrapper = shallow(<TableHead numSelected={0} rowCount={2} columns={[]}/>);
        expect(wrapper.find(Checkbox).prop("checked")).toEqual(false);
        expect(wrapper.find(Checkbox).prop("indeterminate")).toEqual(false);
    })

    it("header column should be active as selected", ()=>{
        const columns=[
            {id :"name"},
            {id :"email"},
        ]
        const wrapper = shallow(<TableHead numSelected={2} rowCount={2} columns={columns} order="desc" orderBy="name" />);
        const sortLabels = wrapper.find(TableSortLabel);
        expect(sortLabels.at(0).prop("active")).toEqual(true);
        expect(sortLabels.at(0).prop("direction")).toEqual("desc");

        expect(sortLabels.at(1).prop("active")).toEqual(false);
        expect(sortLabels.at(1).prop("direction")).toEqual("desc");

        const tableCells = wrapper.find(TableCell);
        expect(tableCells.at(1).prop("sortDirection")).toEqual("desc");
        expect(tableCells.at(2).prop("sortDirection")).toEqual(false);
    })

    it("should raise sort event on click on header", ()=>{
        const columns=[
            {id :"name"},
            {id :"email"},
        ]
        const onRequestSort = jest.fn();
        const wrapper = shallow(<TableHead onRequestSort={onRequestSort} numSelected={2} rowCount={2} columns={columns} order="desc" orderBy="name" />);
        const sortLabels = wrapper.find(TableSortLabel);
        sortLabels.at(0).simulate("click",null);

        expect(onRequestSort).toHaveBeenCalledWith(null, "name");
    })
})