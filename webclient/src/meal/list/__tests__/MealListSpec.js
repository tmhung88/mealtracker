import React from "react";
import { shallow } from "enzyme";
import { MealList } from "../MealList";
import Bluebird from "bluebird";
import Alert from "../Alert";
import moment from "moment";
import ServerPagingTable from "../../../common/table/ServerPagingTable";
import UrlMealFilter from "../UrlMealFilter";
import { DateTimeHelper } from "../../../datetimeHelper";

describe("#MealList", () => {
    it("should send correct alert request", async () => {
        const api = {
            get: jest.fn().mockReturnValue({ json: jest.fn().mockReturnValue({ data: { alerted: true, totalCalories: 30, dailyCalorieLimit: 20 } }) })
        }

        const location = {
            search: "",
        }

        const handleError = jest.fn();
        shallow(<MealList classes={{}} api={api} handleError={handleError} location={location} />);

        await Bluebird.delay(10);

        expect(api.get).toHaveBeenCalledWith(`/v1/users/me/alerts/calorie?date=${moment().format(DateTimeHelper.DATE_FORMAT)}`)
    });

    it("show alert if alert response return true", async () => {
        const api = {
            get: jest.fn().mockReturnValue({ json: jest.fn().mockReturnValue({ data: { alerted: true, totalCalories: 30, dailyCalorieLimit: 20 } }) })
        }

        const location = {
            search: "",
        }

        const handleError = jest.fn();
        const wrapper = shallow(<MealList classes={{}} api={api} handleError={handleError} location={location} />);

        await Bluebird.delay(10);
        expect(wrapper.find(Alert)).toHaveLength(1);
        expect(wrapper.find(Alert).childAt(0).text()).toContain("30");
        expect(wrapper.find(Alert).childAt(0).text()).toContain("20");
    })

    it("show alert if alert response return false", async () => {
        const api = {
            get: jest.fn().mockReturnValue({ json: jest.fn().mockReturnValue({ data: { alerted: false, totalCalories: 30, dailyCalorieLimit: 20 } }) })
        }
        const location = {
            search: "",
        }
        const handleError = jest.fn();
        const wrapper = shallow(<MealList classes={{}} api={api} handleError={handleError} location={location} />);

        await Bluebird.delay(10);
        expect(wrapper.find(Alert)).toHaveLength(0);
    })

    it("navigate to update meal on row select", () => {
        const api = {
            get: jest.fn().mockReturnValue({ json: jest.fn().mockReturnValue({ data: { alerted: false, totalCalories: 30, dailyCalorieLimit: 20 } }) })
        }

        const location = { search: "" };
        const history = { push: jest.fn() };
        const handleError = jest.fn();
        const wrapper = shallow(<MealList classes={{}} api={api} handleError={handleError} location={location} history={history} />);

        wrapper.find(ServerPagingTable).simulate("rowSelect", 10);
        expect(history.push).toHaveBeenCalledWith("/meals/10/update");
    });

    it("should change url on filter query string changed", () => {
        const api = {
            get: jest.fn().mockReturnValue({ json: jest.fn().mockReturnValue({ data: { alerted: false, totalCalories: 30, dailyCalorieLimit: 20 } }) })
        }

        const location = { search: "", pathname: "pathname1" };
        const history = { push: jest.fn() };
        const handleError = jest.fn();
        const wrapper = shallow(<MealList classes={{}} api={api} handleError={handleError} location={location} history={history} />);

        wrapper.find(UrlMealFilter).simulate("queryStringChange", "query=123");
        expect(history.push).toHaveBeenCalledWith({
            pathname: "pathname1",
            search: "query=123"
        });
    })
})