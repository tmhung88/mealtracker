import React from "react";
import { shallow } from "enzyme";
import MealFilter from "../MealFilter";
import { UrlMealFilter } from "../UrlMealFilter";

describe("#MealList", () => {

    it("should parse data from query string as empty", () => {
        const wrapper = shallow(<UrlMealFilter queryString="" />);
        const filter = wrapper.find(MealFilter);
        expect(filter.prop("filter")).toEqual({});
    });

    it("should parse data when query string has all", () => {
        const wrapper = shallow(<UrlMealFilter queryString={`fromDate=2019-05-04&toDate=2019-05-03&fromTime=11:00&toTime=14:00`} />);
        const filter = wrapper.find(MealFilter);
        expect(filter.prop("filter")).toEqual({
            fromDate: "2019-05-04",
            fromTime: "11:00",
            toDate: "2019-05-03",
            toTime: "14:00",
        });
    })

    it("should parse data as undefined when format is invalid", () => {
        const wrapper = shallow(<UrlMealFilter queryString={`fromDate=TTT&toDate=KKKLL&fromTime=KTT&toTime=TTT`} />);
        const filter = wrapper.find(MealFilter);
        expect(filter.prop("filter")).toEqual({
            fromDate: undefined,
            fromTime: undefined,
            toDate: undefined,
            toTime: undefined,
        });
    })

    it("should build querystring onFilter", ()=>{
        const onQueryStringChange = jest.fn();
        const wrapper = shallow(<UrlMealFilter queryString="" onQueryStringChange={onQueryStringChange}/>);
        const filter = wrapper.find(MealFilter);
        filter.simulate("filter", {
            fromDate: "2019-05-04",
            fromTime: "11:00",
            toDate: "2019-05-03",
            toTime: "14:00",
        });

        expect(onQueryStringChange).toHaveBeenCalledWith("fromDate=2019-05-04&toDate=2019-05-03&fromTime=11:00&toTime=14:00");
    })

    it("should build querystring onFilter when missing field", ()=>{
        const onQueryStringChange = jest.fn();
        const wrapper = shallow(<UrlMealFilter queryString="" onQueryStringChange={onQueryStringChange}/>);
        const filter = wrapper.find(MealFilter);
        filter.simulate("filter", {
            fromTime: "11:00",
            toDate: "2019-05-03",
            toTime: undefined,
        });

        expect(onQueryStringChange).toHaveBeenCalledWith("fromDate=&toDate=2019-05-03&fromTime=11:00&toTime=");
    })
})