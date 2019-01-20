package com.twosigma.ws.service;

import com.google.common.collect.Maps;
import com.twosigma.ws.feed.client.FeedListener;
import com.twosigma.ws.model.Book;
import com.twosigma.ws.model.Side;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BookService implements FeedListener {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    private final Map<String, Book> books;
    private final int depth;

    private BookEventListener listener;

    public BookService(@Value("${depth}") final int depth) {
        this.depth = depth;
        this.books = Maps.newHashMap();
    }

    Book book(final String productId) {
        return books.computeIfAbsent(productId, s -> new Book());
    }

    @Override
    public void start(final String productId) {
        logger.info("Book connected {}", productId);
        listener.start(productId);
    }

    @Override
    public void quote(final String productId, final Side side, final double price, final double quantity) {
        logger.info("{} quote - {}   {}@{}", productId, side, quantity, price);
        final Book book = book(productId);
        book.quote(side, price, quantity);
        listener.update(productId, side, side == Side.buy ? book.bidDepth(depth) : book.askDepth(depth));
    }

    @Override
    public void trade(final String productId, final Side side, final double price, final double quantity) {
        logger.info("{} trade - {}   {}@{}", productId, side, quantity, price);
        final Book book = book(productId);
        book.trade(side, price, quantity);

        final Object[][] tradeHistory = book.trades(5);

        listener.trade(productId, side, price, quantity);
        listener.tradeHistory(productId, tradeHistory);
    }

    void listener(final BookEventListener listener) {
        this.listener = listener;
    }
}
