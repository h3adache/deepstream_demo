import { Typography } from "@material-ui/core";
import React, { CSSProperties } from "react";

interface Props {
  events: string[];
}

const news_alert: CSSProperties = {
  textAlign: "left",
  fontWeight: 'bold',
  color: "#e2062c",
  width: 600,
  maxHeight: 100,
  minHeight: 100
};

function EventsList(props: Props) {
  const eventsList = props.events || [];
  const events = eventsList.map((event: String, index: number) => <div key={index}>{event}</div>);
  return (
    <div style={news_alert}>
      {events}
    </div>
  );
}

export default EventsList;
