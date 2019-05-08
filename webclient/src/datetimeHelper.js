import moment from "moment";

class DateTimeHelper {
    verifyFormatOrUndefined(value, format){
        if(!value) {
            return undefined;
        }

        const result = moment(value, format);
        if(result.isValid()) {
            return result.format(format);
        }

        return undefined;
    }

    verifyDateOrUndefined(value){
        return this.verifyFormatOrUndefined(value, "YYYY-MM-DD");
    }

    verifyTimeOrUndefined(value){
        return this.verifyFormatOrUndefined(value, "HH:mm");
    }
}

export default new DateTimeHelper();