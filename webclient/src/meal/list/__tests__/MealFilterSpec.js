import React from "react";
import { shallow } from "enzyme";
import { MealFilter } from "../MealFilter";
import moment from "moment";
import { DateTimeHelper } from "../../../datetimeHelper";

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

    it("should render correct info", () => {
        const onFilter = jest.fn();
        const wrapper = shallow(<MealFilter filter={{
            fromDate: "2019-05-04",
            toDate: "2019-05-03",
            fromTime: "11:00",
            toTime: null,
        }} classes={{}} onFilter={onFilter} />);

        expect(wrapper.find(`[id="from-date"]`).prop("value")).toEqual("2019-05-04");
        expect(wrapper.find(`[id="to-date"]`).prop("value")).toEqual("2019-05-03");
        expect(wrapper.find(`[id="from-time"]`).prop("value")).toEqual("11:00");
        expect(wrapper.find(`[id="to-time"]`).prop("value")).toEqual("");
    })

    describe("Preset buttons", () => {
        let onFilter;
        let wrapper;
        beforeEach(() => {
            onFilter = jest.fn();
            wrapper = shallow(<MealFilter filter={{
                fromDate: "2019-05-04",
                toDate: "2019-05-03",
                fromTime: "11:00",
                toTime: "12:00",
            }} classes={{}} onFilter={onFilter} />);
        })
        it("Today button should set fromDate today and clear toDate", () => {
            wrapper.find(`[name="today-filter"]`).simulate("click");
            expect(wrapper.find(`[id="from-date"]`).prop("value")).toEqual(moment().format(DateTimeHelper.DATE_FORMAT));
            expect(wrapper.find(`[id="to-date"]`).prop("value")).toEqual("");
            expect(wrapper.find(`[id="from-time"]`).prop("value")).toEqual("11:00");
            expect(wrapper.find(`[id="to-time"]`).prop("value")).toEqual("12:00");

            expect(onFilter).toHaveBeenCalledWith({
                fromDate: moment().format(DateTimeHelper.DATE_FORMAT),
                toDate: undefined,
                fromTime: "11:00",
                toTime: "12:00"
            });
        })

        it("Yesterday button should set fromDate and toDate", () => {
            wrapper.find(`[name="yesterday-filter"]`).simulate("click");
            expect(wrapper.find(`[id="from-date"]`).prop("value")).toEqual(moment().subtract(1, "day").format(DateTimeHelper.DATE_FORMAT));
            expect(wrapper.find(`[id="to-date"]`).prop("value")).toEqual(moment().format(DateTimeHelper.DATE_FORMAT));
            expect(wrapper.find(`[id="from-time"]`).prop("value")).toEqual("11:00");
            expect(wrapper.find(`[id="to-time"]`).prop("value")).toEqual("12:00");

            expect(onFilter).toHaveBeenCalledWith({
                fromDate: moment().subtract(1, "day").format(DateTimeHelper.DATE_FORMAT),
                toDate: moment().format(DateTimeHelper.DATE_FORMAT),
                fromTime: "11:00",
                toTime: "12:00"
            });
        })

        it("Lunch button should set fromTIme and toTime", () => {
            wrapper.find(`[name="lunch-filter"]`).simulate("click");
            expect(wrapper.find(`[id="from-date"]`).prop("value")).toEqual("2019-05-04");
            expect(wrapper.find(`[id="to-date"]`).prop("value")).toEqual("2019-05-03");
            expect(wrapper.find(`[id="from-time"]`).prop("value")).toEqual("11:00");
            expect(wrapper.find(`[id="to-time"]`).prop("value")).toEqual("14:00");

            expect(onFilter).toHaveBeenCalledWith({
                fromDate: "2019-05-04",
                toDate: "2019-05-03",
                fromTime: "11:00",
                toTime: "14:00"
            });
        })

        it("Dinner button should set fromTIme and toTime", () => {
            wrapper.find(`[name="dinner-filter"]`).simulate("click");
            expect(wrapper.find(`[id="from-date"]`).prop("value")).toEqual("2019-05-04");
            expect(wrapper.find(`[id="to-date"]`).prop("value")).toEqual("2019-05-03");
            expect(wrapper.find(`[id="from-time"]`).prop("value")).toEqual("18:00");
            expect(wrapper.find(`[id="to-time"]`).prop("value")).toEqual("21:00");

            expect(onFilter).toHaveBeenCalledWith({
                fromDate: "2019-05-04",
                toDate: "2019-05-03",
                fromTime: "18:00",
                toTime: "21:00"
            });
        })

        it("Whole Time button should clear fromTIme and toTime", () => {
            wrapper.find(`[name="whole-time-filter"]`).simulate("click");
            expect(wrapper.find(`[id="from-date"]`).prop("value")).toEqual("2019-05-04");
            expect(wrapper.find(`[id="to-date"]`).prop("value")).toEqual("2019-05-03");
            expect(wrapper.find(`[id="from-time"]`).prop("value")).toEqual("");
            expect(wrapper.find(`[id="to-time"]`).prop("value")).toEqual("");

            expect(onFilter).toHaveBeenCalledWith({
                fromDate: "2019-05-04",
                toDate: "2019-05-03",
                fromTime: undefined,
                toTime: undefined
            });
        })
    })
})