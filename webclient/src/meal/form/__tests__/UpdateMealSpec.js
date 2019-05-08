import React from "react";
import { shallow } from "enzyme";
import MealFilter from "../../list/MealFilter";
import { UpdateMeal } from "../UpdateMeal";
import MealForm from "../MealForm";

describe("#UpdateMeal", () => {
    it("allow user select passed from prop", () => {
        const goBackOrReplace = jest.fn();
        const api = { post: jest.fn() };
        const handleError = jest.fn();
        const location = { search: "?user-select=1" };
        const wrapper = shallow(<UpdateMeal
            classes={{}}
            api={api}
            handleError={handleError}
            location={location}
            goBackOrReplace={goBackOrReplace}
            userSelect
        />);

        const mealForm = wrapper.find(MealForm);
        expect(mealForm.prop("userSelect")).toEqual(true);
    })

    it("should load data from server", async ()=>{

    })
})

async function renderActionButtons(wrapper) {
    const mealForm = wrapper.find(MealFilter);
    return mealForm.renderProp("renderActionButtons")(jest.fn().mockReturnValue(true));
}