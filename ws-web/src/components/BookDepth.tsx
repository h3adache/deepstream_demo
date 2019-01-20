import { Typography } from "@material-ui/core";
import React, { CSSProperties } from "react";

interface Props {
  product_id: string;
  bids: number[][];
  asks: number[][];
}

interface DepthProps {
  quote: number[];
  style: CSSProperties;
}

const quantity_style: CSSProperties = {
  textAlign: "right",
  width: 200,
  color: "#fff"
};

const bid_style: CSSProperties = {
  paddingLeft: 5,
  textAlign: "right",
  color: "#0e0"
};

const ask_style: CSSProperties = {
  ...bid_style,
  color: "#e00"
};

function DepthLine(props: DepthProps) {
  const {quote, style} = props;
  return (
    <tr>
      <td style={quantity_style}>{quote[1].toFixed(4)}</td>
      <td style={style}>{quote[0].toFixed(2)}</td>
    </tr>
  );
}

function BookDepth(props: Props) {
  const bids = props.bids.map((quote: number[]) => <DepthLine key={quote[0]} quote={quote} style={bid_style}/>);
  const asks = props.asks.reverse().map((quote: number[]) => <DepthLine key={quote[0]} quote={quote} style={ask_style}/>);
  return (
    <div>
      <table><tbody>{asks}</tbody></table>
      <table><tbody>{bids}</tbody></table>
    </div>
  );
}

export default BookDepth;
