import React from "react";
import { shallow } from "enzyme";
import ServerPagingTable from "../../../common/table/ServerPagingTable";
import {AllMealList} from "../AllMealList";

describe("#MealList", () => {

    it("navigate to update meal on row select", () => {
        const api = {
            get: jest.fn().mockReturnValue({ json: jest.fn().mockReturnValue({ data: { alerted: false, totalCalories: 30, dailyCalorieLimit: 20 } }) })
        }

        const location = { search: "" };
        const history = { push: jest.fn() };
        const handleError = jest.fn();
        const wrapper = shallow(<AllMealList classes={{}} api={api} handleError={handleError} location={location} history={history} />);

        wrapper.find(ServerPagingTable).simulate("rowSelect", 10);
        expect(history.push).toHaveBeenCalledWith("/meals/all/10/update");
    });
})