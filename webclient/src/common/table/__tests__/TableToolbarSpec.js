import React from "react";
import { shallow } from "enzyme";
import { TableToolbar } from "../TableToolbar";
import { Typography, IconButton } from "@material-ui/core";

describe("#TableToolbar", () => {
    describe("On any items selected", ()=>{
        it("render number of selected items", ()=>{
            const wrapper = shallow(<TableToolbar classes={{}} numSelected={1}/>);
            expect(wrapper.find(Typography).childAt(0).text()).toEqual("1");
        })

        it("render delete button", ()=>{
            const onDelete = jest.fn();
            const wrapper = shallow(<TableToolbar classes={{}} numSelected={1} onDelete={onDelete}/>);
            wrapper.find(IconButton).simulate("click");
            expect(onDelete).toHaveBeenCalled();
        })
    });

    describe("On no item selected", ()=>{
        it("render Table name", ()=>{
            const wrapper = shallow(<TableToolbar classes={{}} numSelected={0} tableName="Table Name 1"/>);
            expect(wrapper.find(Typography).childAt(0).text()).toEqual("Table Name 1");
        })

        it("render refresh button", ()=>{
            const onRefresh = jest.fn();
            const wrapper = shallow(<TableToolbar classes={{}} numSelected={0} onRefresh={onRefresh}/>);
            wrapper.find(IconButton).simulate("click");
            expect(onRefresh).toHaveBeenCalled();
        })
    });
})