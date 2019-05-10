export const closeSnackbar = () => ({
    type: "CLOSE_SNACKBAR",
})

export const showError = (message) => ({
    type: "SHOW_SNACKBAR",
    info: {
        variant: "error",
        message: message,
    }
})

export const showSuccess = (message) => ({
    type: "SHOW_SNACKBAR",
    info: {
        variant: "success",
        message: message,
    }
})