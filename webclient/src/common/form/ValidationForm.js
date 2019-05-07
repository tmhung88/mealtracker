import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import withStyles from '@material-ui/core/styles/withStyles';
import _ from "lodash";
import validate from "validate.js";

const styles = theme => ({

});


class ValidationForm extends React.Component {
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

        const validationResult = {};
        serverValidationError.forEach(v => {
            validationResult[v.name] = v.message;
        });

        return validationResult;
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
        this.setState({
            dirty: {
                ...this.state.dirty,
                [fieldName]: true,
            }
        });

        if ((this.state.serverValidationResult || {})[fieldName]) {
            const newServerValidationResult = { ...this.state.serverValidationResult };
            delete newServerValidationResult[fieldName];
            this.setState({
                serverValidationResult: newServerValidationResult,
            })
        }

        this.props.onDataChange({
            ...this.props.data,
            [fieldName]: value,
        })
    }

    isValid = () => {
        const result = !validate(this.props.data, this.props.constraints);
        this.setState({ dirty: {} });
        return result;
    }

    render() {
        const { children, data } = this.props;
        const validationResult = { ...this.validate(), ...this.state.serverValidationResult };
        return <Fragment>
            {children({
                onFieldChange: this.handleFieldChange,
                data: data,
                isValid: this.isValid,
                validationResult: validationResult,
            })}
        </Fragment>
    }
}

ValidationForm.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(ValidationForm);