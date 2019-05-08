import React from "react";
import { shallow } from "enzyme";
import MealFilter from "../../list/MealFilter";
import { UpdateMeal } from "../UpdateMeal";
import MealForm from "../MealForm";
import Bluebird from "bluebird";

describe("#UpdateMeal", () => {
    const serverMealResponse = {
        data: {
            name: "Meal 1",
            consumer: {
                id: 123,
                email: "email1@a.com"
            }
        }
    }
    it("allow user select passed from prop", async () => {
        const goBackOrReplace = jest.fn();
        const api = {
            post: jest.fn(),
            get: jest.fn().mockResolvedValue({ json: jest.fn().mockResolvedValue(serverMealResponse) }),
        };
        const handleError = jest.fn();
        const wrapper = shallow(<UpdateMeal
            classes={{}}
            api={api}
            handleError={handleError}
            goBackOrReplace={goBackOrReplace}
            userSelect
            match={{ params: { id: "12" } }}
        />);

        const mealForm = wrapper.find(MealForm);
        expect(mealForm.prop("userSelect")).toEqual(true);
    })

    it("should load data from server", async () => {
        const goBackOrReplace = jest.fn();
        const api = {
            post: jest.fn(),
            get: jest.fn().mockResolvedValue({ json: jest.fn().mockResolvedValue(serverMealResponse) }),
        };

        const handleError = jest.fn();
        const wrapper = shallow(<UpdateMeal
            classes={{}}
            api={api}
            handleError={handleError}
            goBackOrReplace={goBackOrReplace}
            userSelect
            match={{ params: { id: "12" } }}
            baseApiUrl="/abc"
            cancelPage="/def"
        />);

        await Bluebird.delay(10);
        const mealForm = wrapper.find(MealForm);
        expect(mealForm.prop("meal")).toEqual({
            name: "Meal 1",
            consumerId: 123,
            consumerEmail: "email1@a.com"            
        });
        expect(api.get).toHaveBeenCalledWith("/abc/12");
    })

    it("should load data from server without consumer", async () => {
        const goBackOrReplace = jest.fn();
        const api = {
            post: jest.fn(),
            get: jest.fn().mockResolvedValue({ json: jest.fn().mockResolvedValue({
                ...serverMealResponse,
                data: {
                    ...serverMealResponse.data,
                    consumer: null,
                }
            }) }),
        };

        const handleError = jest.fn();
        const wrapper = shallow(<UpdateMeal
            classes={{}}
            api={api}
            handleError={handleError}
            goBackOrReplace={goBackOrReplace}
            userSelect
            match={{ params: { id: "12" } }}
            baseApiUrl="/abc"
            cancelPage="/def"
        />);

        await Bluebird.delay(10);
        const mealForm = wrapper.find(MealForm);
        expect(mealForm.prop("meal")).toEqual({
            name: "Meal 1",
            consumerId: null,
            consumerEmail: null,
        });
        expect(api.get).toHaveBeenCalledWith("/abc/12");
    })

    it("should submit meal info from meal-form", async () => {
        const goBackOrReplace = jest.fn();
        const api = {
            post: jest.fn(),
            put: jest.fn(),
            get: jest.fn().mockResolvedValue({ json: jest.fn().mockResolvedValue(serverMealResponse) }),
        };
        const handleError = jest.fn();
        const wrapper = shallow(<UpdateMeal
            classes={{}}
            api={api}
            handleError={handleError}
            goBackOrReplace={goBackOrReplace}
            match={{ params: { id: "12" } }}
            baseApiUrl="/v1/users/me/meals"
            cancelPage="/meals"
        />);

        const mealForm = wrapper.find(MealForm);
        mealForm.simulate("mealChange", { mealData: "meal" });

        await submit(wrapper);

        expect(api.put).toHaveBeenCalledWith("/v1/users/me/meals/12", { mealData: "meal" });
        expect(goBackOrReplace).toHaveBeenCalledWith("/meals");
    });
})

function renderActionButtons(wrapper) {
    const mealForm = wrapper.find(MealForm);
    return mealForm.renderProp("renderActionButtons")(jest.fn().mockReturnValue(true));
}

async function submit(wrapper) {
    const actionButtons = renderActionButtons(wrapper);
    actionButtons.find(`[type="submit"]`).simulate("click", { preventDefault: jest.fn() });
    await Bluebird.delay(10);
}