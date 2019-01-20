import { createStyles, Paper, Theme, Typography, withStyles } from "@material-ui/core";
import React, { ReactNode } from "react";

const styles = (theme: Theme) =>
  createStyles({
    paper: { height: '100%', padding: 10, textAlign: "center" }
  });

interface Props {
  header: string,
  children: ReactNode,
  classes: {
    paper: string;
  };
}

function PaperBox(props: Props) {
  return (
    <Paper className={props.classes.paper}>
      <Typography variant="h5">{props.header}</Typography>
      {props.children}
    </Paper>
  );
}

export default withStyles(styles)(PaperBox);
