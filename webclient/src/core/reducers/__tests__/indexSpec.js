import rootReducer from "../index"

describe("#RootReducer", () => {
    it("should return state on close message", () => {
        const result = rootReducer({
            snackbarInfo: {
                show: true,
                variant: null,
                message: "",
            }
        }, { type: "CLOSE_SNACKBAR" });
        expect(result).toEqual({
            snackbarInfo: {
                show: false,
                variant: null,
                message: "",
            }
        });
    })

    it("should return state on show message", () => {
        const result = rootReducer(undefined, {
            type: "SHOW_SNACKBAR",
            info: {
                variant: "error",
                message: "error 1",
            }
        });
        expect(result).toEqual({
            snackbarInfo: {
                show: true,
                variant: "error",
                message: "error 1",
            }
        });
    })
})