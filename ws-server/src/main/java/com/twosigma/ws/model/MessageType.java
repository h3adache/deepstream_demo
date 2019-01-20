package com.twosigma.ws.model;

public enum MessageType {
    subscribe,
    heartbeat,
    ticker,
    snapshot,
    l2update,
    match,
    last_match,
    ignore
}