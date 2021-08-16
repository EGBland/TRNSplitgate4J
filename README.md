# TRNSplitgate4J

A Java API wrapper for the [TRN Splitgate](https://tracker.gg/splitgate) stat tracking API, written using [Reactor 3](https://projectreactor.io).

## Usage

The `Splitgate` class is where the API requests are made. Its only constructor takes two arguments -- the TRN API key, and a name to fill the `User-Agent` header in the HTTP request.
Within the `Splitgate` class is `getPlayer(String)`, which takes a Steam UID as its argument and returns a `Mono<SplitgatePlayer>`.
It is the `SplitgatePlayer` object where most of the actual querying takes place. Currently, only the name, Steam UID and TRN 'segments' are available.

TL;DR use `Splitgate` to get data on a player then use the produced `SplitgatePlayer` instance to use individual parts of the data.
