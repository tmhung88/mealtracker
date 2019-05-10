import React from "react";
import { shallow } from "enzyme";
import { UserSettings } from "../UserSettings";
import { TextField, FormHelperText } from "@material-ui/core";
import Bluebird from "bluebird";

describe("#UserSettings", () => {
    it("should fetch information", async () => {
        const api = {
            get: jest.fn().mockResolvedValue({ json: jest.fn().mockResolvedValue({ data: { dailyCalorieLimit: 400 } }) })
        }
        const wrapper = shallow(<UserSettings classes={{}} api={api} />);
        await Bluebird.delay(10);
        expect(wrapper.find(TextField).prop("value")).toEqual(400);
        expect(api.get).toHaveBeenCalledWith("/v1/users/me");
    })

    it("should handle error on fetching data", async () => {
        const api = {
            get: jest.fn().mockRejectedValue({})
        }
        const handleError = jest.fn();
        shallow(<UserSettings classes={{}} api={api} handleError={handleError} />);
        await Bluebird.delay(10);
        expect(handleError).toHaveBeenCalledWith({});
    })

    it("should submit changed data", async () => {
        const api = {
            get: jest.fn().mockResolvedValue({ json: jest.fn().mockResolvedValue({ data: { dailyCalorieLimit: 400 } }) }),
            patch: jest.fn(),
        }
        const handleError = jest.fn();
        const showSuccessMessage = jest.fn();
        const wrapper = shallow(<UserSettings showSuccessMessage={showSuccessMessage} classes={{}} api={api} handleError={handleError} />);
        wrapper.find(TextField).simulate("change", { currentTarget: { value: "100" } });
        wrapper.find(`[type="submit"]`).simulate("click", { preventDefault: jest.fn() });
        await Bluebird.delay();
        expect(api.patch).toHaveBeenCalledWith("/v1/users/me", { dailyCalorieLimit: 100 });
        expect(showSuccessMessage).toHaveBeenCalledWith("Update Settings successfully");
    })
})