import React from "react";
import { shallow } from "enzyme";
import { MealFilter } from "../MealFilter";

describe("#MealList", () => {
    it("should gather correct info on submit", () => {
        const onFilter = jest.fn();
        const wrapper = shallow(<MealFilter classes={{}} onFilter={onFilter} />);
        function toEvent(value) { return { currentTarget: { value: value } } };
        wrapper.find(`[id="from-date"]`).simulate("change", toEvent("2017-08-08"));
        wrapper.find(`[id="to-date"]`).simulate("change", toEvent("2018-08-08"));
        wrapper.find(`[id="from-time"]`).simulate("change", toEvent("12:12"));
        wrapper.find(`[id="to-time"]`).simulate("change", toEvent("13:13"));

        wrapper.find(`[name="filter"]`).simulate("click");

        expect(onFilter).toHaveBeenCalledWith({
            fromDate: "2017-08-08",
            fromTime: "12:12",
            toDate: "2018-08-08",
            toTime: "13:13"
        });
    })

    it("should render correct info", ()=>{
        const onFilter = jest.fn();
        const wrapper = shallow(<MealFilter filter={{
            fromDate: "2019-05-04",
            toDate: "2019-05-03",
            fromTime: "11:00",
            toTime: "14:00",
        }} classes={{}} onFilter={onFilter} />);

        expect(wrapper.find(`[id="from-date"]`).prop("value")).toEqual("2019-05-04");
        expect(wrapper.find(`[id="to-date"]`).prop("value")).toEqual("2019-05-03");
        expect(wrapper.find(`[id="from-time"]`).prop("value")).toEqual("11:00");
        expect(wrapper.find(`[id="to-time"]`).prop("value")).toEqual("14:00");

    })
})