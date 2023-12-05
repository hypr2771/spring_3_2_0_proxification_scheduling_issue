package org.example;

import java.util.concurrent.TimeUnit;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class Task {

  @Scheduled(fixedDelay = 1L, timeUnit = TimeUnit.SECONDS)
  Mono<String> test() {
    return Mono.fromSupplier(() -> {
      try {
        Thread.sleep(2);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return "%s".formatted(System.currentTimeMillis());
    });
  }

}
