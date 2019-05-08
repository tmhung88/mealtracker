import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import withStyles from '@material-ui/core/styles/withStyles';
import _ from "lodash";
import validate from "validate.js";

const styles = theme => ({
});


export class ValidationForm extends React.Component {
    constructor(props) {
        super(props);
        const dirty = {};
        _.keys(this.props.data).forEach(key => {
            dirty[key] = false;
        })
        this.state = {
            dirty,
            serverValidationResult: this.constructValidationErrorFromServer(this.props.serverValidationError),
        }
    }

    constructValidationErrorFromServer(serverValidationError) {
        if (!serverValidationError) {
            return {};
        }

        const validationFields = {};
        (serverValidationError.errorFields || []).forEach(v => {
            validationFields[v.name] = v.message;
        });

        return { validationFields, validationMessage: serverValidationError.message };
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.serverValidationError !== this.props.serverValidationError) {
            this.setState({
                serverValidationResult: this.constructValidationErrorFromServer(nextProps.serverValidationError),
            })
        }
    }

    constructConstraint(constraints, dirty) {
        _.keys(dirty).forEach(key => {
            if (!dirty[key]) {
                constraints = {
                    ...constraints,
                    [key]: undefined,
                };
            }
        })

        return constraints;
    }

    validate() {
        const { dirty } = this.state;
        const constraints = this.constructConstraint(this.props.constraints, dirty);

        let result = validate(this.props.data, constraints) || {};

        return result;
    }

    handleFieldChange = (fieldName, value) => {
        this.handleFieldsChange({ [fieldName]: value });
    }

    handleFieldsChange = (obj) => {
        let dirty = this.state.dirty;
        let serverValidationResult = this.state.serverValidationResult;
        let data = this.props.data;
        _.keys(obj).forEach((key) => {
            dirty = { ...dirty, [key]: true };
            if ((serverValidationResult || {})[key]) {
                const newServerValidationResult = { ...serverValidationResult };
                delete newServerValidationResult[key];
            }
            data = { ...data, [key]: obj[key] };
        })

        this.setState({ dirty, serverValidationResult });
        this.props.onDataChange(data);
    }

    isValid = () => {
        const result = !validate(this.props.data, this.props.constraints);
        this.setState({ dirty: {} });
        return result;
    }

    render() {
        const { children, data } = this.props;
        const { validationFields, validationMessage } = this.state.serverValidationResult;
        const validationResult = { ...this.validate(), ...validationFields };
        return <Fragment>
            {children({
                onFieldChange: this.handleFieldChange,
                onFieldsChange: this.handleFieldsChange,
                data: data,
                isValid: this.isValid,
                validationFields: validationResult,
                validationMessage: validationMessage,
            })}
        </Fragment>
    }
}

ValidationForm.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(ValidationForm);