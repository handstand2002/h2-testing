package com.brokencircuits.h2testing;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QueryRestController {

  private int numQueries = 0;
  private long totalMsTaken = 0;
  final private JdbcTemplate template;

  @PostMapping("/")
  public String timeQuery(@RequestBody String query) {
    Instant startQuery = Instant.now();
    int size = template.queryForList(query).size();
    Instant endQuery = Instant.now();
    long msTaken = (endQuery.toEpochMilli() - startQuery.toEpochMilli());
    numQueries++;
    totalMsTaken += msTaken;

    return "Query returned " + size + " results in " + msTaken + "ms; Average time per query: " + (
        totalMsTaken / numQueries) + "ms";
  }
}
