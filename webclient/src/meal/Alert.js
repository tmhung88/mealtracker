import React from 'react';
import withStyles from '@material-ui/core/styles/withStyles';
import ErrorIcon from '@material-ui/icons/Error';

const styles = theme => {
    console.log(theme);
    return {
        main: {
            minWidth: 150,
            padding: 15,
            marginBottom: 20,
            border: "1px solid transparent",
            borderRadius: 3,
            backgroundColor: "#ebc063",
            borderColor: "lighten(#E2A41F, 10%)",
            color: "#a07415",
            boxShadow: "0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24)",
            fontFamily: theme.typography.fontFamily,
            fontWeight: theme.typography.fontWeightRegular,
        },
        icon: {
            paddingRight: 5,
            verticalAlign: "middle",
            fontSize: 24,            
        }
    }
}

class Alert extends React.Component {
    render() {
        const { classes, children } = this.props;
        return (<div className={classes.main} role="alert">
            <ErrorIcon className={classes.icon} />
            {children}
        </div>);
    }
}

export default withStyles(styles)(Alert);