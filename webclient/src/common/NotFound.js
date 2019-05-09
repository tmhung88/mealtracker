import React from "react";
import Button from "@material-ui/core/Button";
import Typography from "@material-ui/core/Typography";
import withStyles from "@material-ui/core/styles/withStyles";
import { withPage } from "../core/components/AppPage";
import Form from "./form/Form";

const styles = theme => ({
    backButton: {
        marginTop: theme.spacing.unit * 2,
    }
});

export class NotFound extends React.Component {
  render() {
    const { classes } = this.props;
    return <Form>
        <Typography component="h1" variant="h5">
            Page not found 
        </Typography>
        <Button onClick={() => this.props.history.replace("")}
            variant="contained"
            color="primary"
            className={classes.backButton}
        >
            Home
        </Button>
    </Form>
  }
}

export default withPage(withStyles(styles)(NotFound));