import moment from "moment";

class DateTimeHelper {
    parseWithFormatOrUndefined(value, format){
        if(!value) {
            return undefined;
        }

        const result = moment(value, format);
        if(result.isValid()) {
            return result;
        }

        return undefined;
    }

    parseDateOrUndefined(value){
        return this.parseWithFormatOrUndefined(value);
    }

    parseTimeOrUndefined(value){
        return this.parseWithFormatOrUndefined(value, "HH:mm");
    }
}

export default new DateTimeHelper();