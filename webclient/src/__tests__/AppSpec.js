import React from "react";
import { shallow } from "enzyme";
import {App} from "../App";
import Snackbar from "@material-ui/core/Snackbar";
import SnackbarErrorMessage from "../core/components/SnackbarErrorMessage";

describe("#App", () => {
    it("should render snackbar info", ()=>{
        const snackbarInfo = {
            show: true,
            message: "abc",
            variant: "info",
        }
        const wrapper = shallow(<App snackbarInfo={snackbarInfo} />);
        expect(wrapper.find(Snackbar).prop("open")).toEqual(true);
        expect(wrapper.find(SnackbarErrorMessage).prop("variant")).toEqual("info");
        expect(wrapper.find(SnackbarErrorMessage).prop("message")).toEqual("abc");
    });
})