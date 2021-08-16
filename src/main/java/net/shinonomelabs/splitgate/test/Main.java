package net.shinonomelabs.splitgate.test;

import net.shinonomelabs.splitgate.Splitgate;
import net.shinonomelabs.splitgate.SplitgatePlayer;
import net.shinonomelabs.splitgate.SplitgateSegment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.PrintStream;

public class Main {
    public static void main(String[] args) {
        if(args.length != 1) {
            System.err.println("Usage: TRNSplitgate4J <api key>");
            System.exit(1);
        }

        Splitgate api = new Splitgate(args[0], "KeanuTracker/0.1.0");
        SplitgatePlayer player = api.getPlayer("76561198082755649");

        player.getSegment("Lifetime Overview")
                .map(SplitgateSegment::getAvailableStats)
                .flatMapMany(Flux::fromIterable)
                .subscribe(System.out::println);
    }
}
