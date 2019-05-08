import React from "react";
import { shallow } from "enzyme";
import { NewMeal } from "../NewMeal";
import MealForm from "../MealForm";
import Bluebird from "bluebird";

describe("#NewMeal", () => {
    it("allow user select passed from prop", () => {
        const goBackOrReplace = jest.fn();
        const api = { post: jest.fn() };
        const handleError = jest.fn();
        const wrapper = shallow(<NewMeal
            classes={{}}
            api={api}
            handleError={handleError}
            goBackOrReplace={goBackOrReplace}
            userSelect
        />);

        const mealForm = wrapper.find(MealForm);
        expect(mealForm.prop("userSelect")).toEqual(true);
    })

    it("should submit meal info from meal-form", async () => {
        const goBackOrReplace = jest.fn();
        const api = { post: jest.fn() };
        const handleError = jest.fn();
        const wrapper = shallow(<NewMeal
            classes={{}}
            api={api}
            handleError={handleError}
            goBackOrReplace={goBackOrReplace}
            baseApiUrl="/v1/users/me/meals"
            cancelPage="/meals"
        />);

        const mealForm = wrapper.find(MealForm);
        mealForm.simulate("mealChange", { mealData: "meal" });

        await submit(wrapper);

        expect(api.post).toHaveBeenCalledWith("/v1/users/me/meals", { mealData: "meal" });
        expect(goBackOrReplace).toHaveBeenCalledWith("/meals");
    });

    it("should handle request error", async () => {
        const goBackOrReplace = jest.fn();
        const error = new Error("abc");
        const api = { post: jest.fn().mockReturnValue(Promise.reject(error)) };
        const handleError = jest.fn();
        const wrapper = shallow(<NewMeal
            classes={{}}
            api={api}
            handleError={handleError}
            goBackOrReplace={goBackOrReplace}
            baseApiUrl="/v1/users/me/meals"
            cancelpage="/meals"
        />);
        await submit(wrapper);

        expect(handleError).toHaveBeenCalledWith(error);
    });

    it("Cancel should to to previous page", ()=>{
        const goBackOrReplace = jest.fn();
        const error = new Error("abc");
        const api = { post: jest.fn().mockReturnValue(Promise.reject(error)) };
        const handleError = jest.fn();
        const wrapper = shallow(<NewMeal
            classes={{}}
            api={api}
            handleError={handleError}
            goBackOrReplace={goBackOrReplace}
            baseApiUrl="/v1/users/me/meals"
            cancelPage="/meals"
        />);

        const mealForm = wrapper.find(MealForm);
        const renderActionButtons = mealForm.renderProp("renderActionButtons")(jest.fn().mockReturnValue(true));
        renderActionButtons.find(`[color="secondary"]`).simulate("click");
        expect(goBackOrReplace).toHaveBeenCalledWith("/meals");
    });

    async function submit(wrapper) {
        const mealForm = wrapper.find(MealForm);
        const renderActionButtons = mealForm.renderProp("renderActionButtons")(jest.fn().mockReturnValue(true));
        renderActionButtons.find(`[type="submit"]`).simulate("click", { preventDefault: jest.fn() });
        await Bluebird.delay(10);
    }
})