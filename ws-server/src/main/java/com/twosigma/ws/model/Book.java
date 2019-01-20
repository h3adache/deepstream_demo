package com.twosigma.ws.model;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import static com.twosigma.ws.model.Side.buy;
import static com.twosigma.ws.model.Side.unknown;

public class Book {
    private static final double epsilon = 0.000000001;

    private final Map<Double, Double> bids;
    private final Map<Double, Double> asks;
    private final Stack<Object[]> trades;

    public Book() {
        this.bids = new TreeMap<>(Comparator.reverseOrder());
        this.asks = new TreeMap<>();

        this.trades = new Stack<>();
    }

    public double[][] bidDepth(final int depth) {
        return bids.entrySet().stream().limit(depth).map(this::nextLevel).toArray(double[][]::new);
    }

    public double[][] askDepth(final int depth) {
        return asks.entrySet().stream().limit(depth).map(this::nextLevel).toArray(double[][]::new);
    }

    public Object[][] trades(final int history) {
        return trades.stream().limit(history).toArray(Object[][]::new);
    }

    public Object[] lastTrade() {
        return trades.empty() ? new Object[]{unknown, 0, 0} : trades.peek();
    }

    private double[] nextLevel(Map.Entry<Double, Double> entry) {
        return new double[]{entry.getKey(), entry.getValue()};
    }

    public void quote(final Side side, final double price, final double quantity) {
        final Map<Double, Double> sidedLevels = side == buy ? bids : asks;
        if (quantity - epsilon < 0d) {
            sidedLevels.remove(price);
        } else {
            sidedLevels.put(price, quantity);
        }
    }

    public void trade(final Side side, final double price, final double quantity) {
        trades.add(new Object[]{side, price, quantity});
    }
}
