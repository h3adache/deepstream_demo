import {
  AppBar,
  FormControlLabel,
  Radio,
  RadioGroup,
  Toolbar
} from "@material-ui/core";
import Grid from "@material-ui/core/Grid";
import createDeepstream from "deepstream.io-client-js";
import React, { Component } from "react";
import BookDepth from "./components/BookDepth";
import EventsList from "./components/EventsList";
import PaperBox from "./components/PaperBox";
import TradesList from "./components/TradesList";

class App extends Component<any, any> {
  client: deepstreamIO.Client;
  productRecord: deepstreamIO.Record;

  constructor(props: any) {
    super(props);
    this.state = {
      product_id: "ETH-USD",
      bids: [],
      asks: [],
      trades: [],
      events: []
    };

    const ds = createDeepstream("ws://127.0.0.1:6020/feed");
    this.client = ds.login();

    this.productRecord = this.subscribe(this.state.product_id);

    this.client.event.subscribe(
      "trade",
      trade =>
        trade[0] == this.state.product_id &&
        this.setState({ trades: [trade, ...this.state.trades] })
    );

    this.client.event.subscribe("news", event => {
      const old_events = this.state.events.slice(0, 4);
      this.setState({ events: [event, ...old_events] });
    });
  }

  subscribe(product_id: string): deepstreamIO.Record {
    console.log("subscribe to", product_id);
    const newRecord = this.client.record.getRecord(product_id);
    newRecord.subscribe(record => {
      // Update state on input change
      this.setState({
        product_id: record.product_id,
        bids: record.buy,
        asks: record.sell
      });
    });

    return newRecord;
  }

  changeProduct = (event: React.ChangeEvent<{}>, value: string) => {
    console.log("Changing product", value);
    this.setState({ product_id: value, trades: [] });
    this.productRecord.unsubscribe();
    this.productRecord = this.subscribe(value);
  };

  render() {
    return (
      <div style={{margin:5}}>
        <AppBar position="static" color="default">
          <Toolbar>
            <RadioGroup
              aria-label="Product"
              value={this.state.product_id}
              onChange={this.changeProduct}
              style={{
                display: "flex",
                width: "auto",
                flexWrap: "nowrap",
                flexDirection: "row"
              }}
            >
              <FormControlLabel
                value="ETH-USD"
                control={<Radio />}
                label="ETH-USD"
              />
              <FormControlLabel
                value="BTC-USD"
                control={<Radio />}
                label="BTC-USD"
              />
            </RadioGroup>
          </Toolbar>
        </AppBar>
        <Grid container spacing={24}>
          <Grid item md={12}>
            <PaperBox header="News">
              <EventsList events={this.state.events} />
            </PaperBox>
          </Grid>
          <Grid item md={4}>
            <PaperBox header="Order Book">
              <BookDepth
                product_id={this.state.product_id}
                bids={this.state.bids || []}
                asks={this.state.asks || []}
              />
            </PaperBox>
          </Grid>
          <Grid item md={8}>
            <PaperBox header="Trade History">
              <TradesList trades={this.state.trades} />
            </PaperBox>
          </Grid>
        </Grid>
      </div>
    );
  }
}

export default App;
