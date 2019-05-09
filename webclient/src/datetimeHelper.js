import moment from "moment";

export class DateTimeHelper {

    static DATE_FORMAT = "YYYY-MM-DD";
    static TIME_FORMAT = "HH:mm";

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
        return this.verifyFormatOrUndefined(value, DateTimeHelper.DATE_FORMAT);
    }

    verifyTimeOrUndefined(value){
        return this.verifyFormatOrUndefined(value, DateTimeHelper.TIME_FORMAT);
    }
}

export default new DateTimeHelper();