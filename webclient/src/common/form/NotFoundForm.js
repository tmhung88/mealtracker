import React from 'react';
import PropTypes from 'prop-types';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import withStyles from '@material-ui/core/styles/withStyles';
import Form from './Form';
import { withPage } from '../../core/components/AppPage';

const styles = theme => ({
    backButton: {
        marginTop: theme.spacing.unit * 2,
    }
});


class NotFoundForm extends React.Component {
    render() {
        const { classes, formName, backPage } = this.props;
        return (
            <Form>
                <Typography component="h1" variant="h5">
                    {`${formName} not found`}
                </Typography>
                <Button onClick={() => this.props.goBackOrReplace(backPage)}
                    variant="contained"
                    color="primary"
                    className={classes.backButton}
                >
                    Back
                </Button>
            </Form>
        );
    }
}

NotFoundForm.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withPage(withStyles(styles)(NotFoundForm));