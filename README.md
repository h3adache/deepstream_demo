# Websocket demo using deepstream.

**Do not use this to actually trade with. Code should be treated as demo quality only.**

### *Prereqs*
* install deepstream for your platform => https://deepstreamhub.com/open-source/install

### *Running*
1. Run deepstream from project directory using the [included config](deepstream/config.yml)
```
    deepstream start -c deepstream/config.yml
```
2. Start server in separate terminal
```
    cd ws-server
    ./gradlew bootRun
```
3. Start web in separate terminal
```
    cd ws-web
    yarn start
```

### *Code pointers*
* All client socket websocket code (using deepstream library)
    * [App.tsx](./ws-web/src/App.tsx)

* Server code to receive client requests and feed (using deepstream java library)
    * [MarketEventsService.java](./ws-server/src/main/java/com/twosigma/ws/service/MarketEventsService.java)

* Server code to handle websocket feed (using standard java apis)
    * [GdaxFeedService.java](./ws-server/src/main/java/com/twosigma/ws/feed/GdaxFeedService.java)
    * [GdaxFeedProcessor.java](./ws-server/src/main/java/com/twosigma/ws/feed/client/GdaxFeedProcessor.java)