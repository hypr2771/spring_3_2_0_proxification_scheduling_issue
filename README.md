## What is this repo ?

This repo simply aims to exhibit the Spring `@Scheduled` + `@Aspect` proxification issue after 3.2.0 upgrade

## Reproduce

### Set your parent to 3.1.5

When in 3.1.5, `@Scheduled` proxification works perfectly:

```sh
mvn versions:update-parent -DparentVersion="[3.1.5]"
mvn clean package
mvn exec:java -Dexec.mainClass="org.example.Main"
```

Should look like:

```log
2023-12-05T12:45:32.143+07:00  INFO 5615 --- [ple.Main.main()] org.example.Main                         : Starting Main using Java 17.0.1 with PID 5615 (/Users/vincentcastelluci/Workspace/Perso/Java/Tuto/spring_3_2_0_scheduling_issue/target/classes started by vincentcastelluci in /Users/vincentcastelluci/Workspace/Perso/Java/Tuto/spring_3_2_0_scheduling_issue)
2023-12-05T12:45:32.144+07:00  INFO 5615 --- [ple.Main.main()] org.example.Main                         : No active profile set, falling back to 1 default profile: "default"
2023-12-05T12:45:32.664+07:00  INFO 5615 --- [ple.Main.main()] o.s.b.web.embedded.netty.NettyWebServer  : Netty started on port 8080
Running scheduled Mono org.example.Task.test()
Ended scheduled Mono org.example.Task.test() with result 1701755132673
2023-12-05T12:45:32.697+07:00  INFO 5615 --- [ple.Main.main()] org.example.Main                         : Started Main in 0.632 seconds (process running for 1.555)
Running scheduled Mono org.example.Task.test()
Ended scheduled Mono org.example.Task.test() with result 1701755133681
Running scheduled Mono org.example.Task.test()
Ended scheduled Mono org.example.Task.test() with result 1701755134685

```

### Set your parent to 3.2.0

When in 3.2.0, `@Scheduled` proxification seems broken:

```sh
mvn versions:update-parent -DparentVersion="[3.2.0]"
mvn clean package
mvn exec:java -Dexec.mainClass="org.example.Main"
```

Should look like:

```log
2023-12-05T12:48:15.475+07:00  INFO 5724 --- [ple.Main.main()] org.example.Main                         : Starting Main using Java 17.0.1 with PID 5724 (/Users/vincentcastelluci/Workspace/Perso/Java/Tuto/spring_3_2_0_scheduling_issue/target/classes started by vincentcastelluci in /Users/vincentcastelluci/Workspace/Perso/Java/Tuto/spring_3_2_0_scheduling_issue)
2023-12-05T12:48:15.476+07:00  INFO 5724 --- [ple.Main.main()] org.example.Main                         : No active profile set, falling back to 1 default profile: "default"
Running scheduled Mono org.example.Task.test()
Ended scheduled Mono org.example.Task.test() with result 1701755295851
2023-12-05T12:48:16.000+07:00  INFO 5724 --- [ple.Main.main()] o.s.b.web.embedded.netty.NettyWebServer  : Netty started on port 8080
2023-12-05T12:48:16.009+07:00  INFO 5724 --- [ple.Main.main()] org.example.Main                         : Started Main in 0.646 seconds (process running for 2.494)
```

You can note the task is being run once on boot, rescheduled but never actually subscribed it seems.

### Idea

No real clue but since it keeps working if we run the `.subscribe` on the `@Scheduled` process, my guess is that the underlying subscriber, related to changes made for async improvements, is disposed before it gets actually subscribed when returned by the proxified method.