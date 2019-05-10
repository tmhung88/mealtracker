const initState = {
    snackbarInfo:{
        show:false,
        variant:null,
        message:"",
    }
}

export default (state = initState, action)=>{
    switch(action.type) {
        case "CLOSE_SNACKBAR": {
            return {
                ...state,
                snackbarInfo: {
                    ...state.snackbarInfo,
                    show: false,
                }
            }
        }
        case "SHOW_SNACKBAR": {
            return {
                ...state,
                snackbarInfo: {
                    ...state.snackbarInfo,
                    show: true,
                    variant: action.info.variant,
                    message: action.info.message,
                }
            }
        }
        default: return state;
    }
}