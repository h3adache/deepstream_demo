package com.twosigma.ws.service;

import com.twosigma.ws.feed.FeedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Component
@Path("/")
public class MarketDataService {
    private static final Logger logger = LoggerFactory.getLogger(MarketDataService.class);
    private final BookService bookService;
    private final FeedService engine;

    @Autowired
    public MarketDataService(final BookService bookService, final FeedService engine) {
        this.bookService = bookService;
        this.engine = engine;

        logger.info("Using engine {}", engine);
    }

    @POST
    @Path("/subscribe/{productId}")
    public void subscribe(@PathParam("productId") final String productId) {
        engine.subscribe(productId);
    }

    @GET
    @Path("/last/{productId}")
    @Produces("application/json")
    public Object[] lastTrade(@PathParam("productId") final String productId) {
        return bookService.book(productId).lastTrade();
    }
}
