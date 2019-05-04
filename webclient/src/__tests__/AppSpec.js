import React from "react";
import { shallow } from "enzyme";
import App from "../App";

describe("#App", () => {
    it("should assert", () => {
        const wrapper = shallow(<App />);
        console.log(wrapper.debug());
    });
})
