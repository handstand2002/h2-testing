package com.brokencircuits.h2testing.config;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.TaskScheduler;

@Slf4j
@Configuration
@ManagedResource
public class RuntimeConfig {

  final private static Random RAND = new Random(Instant.now().toEpochMilli());

  @Bean
  ScheduledFuture<?> insertData(JdbcTemplate jdbcTemplate, TaskScheduler taskScheduler) {
    return taskScheduler.schedule(() -> {
      final int numberCustomers = 300000;
      final int numberProducts = 300000;
      final int numberOrders = 300000;

      log.info("Inserting customers");
      for (int i = 1; i <= numberCustomers; i++) {
        jdbcTemplate.execute(customerInsertStatement(row(i)));
      }

      log.info("Inserting Products");
      for (int i = 1; i <= numberProducts; i++) {
        jdbcTemplate.execute(productInsertStatement(row(i)));
      }

      log.info("Inserting Orders");
      for (int i = 0; i <= numberOrders; i++) {
        jdbcTemplate.execute(orderInsertStatement(orderRow(i, numberCustomers, numberProducts)));
      }
      log.info("Finished inserting data");

    }, Instant.now());

  }

  private String orderInsertStatement(String rowValueString) {
    return "insert into Orders(product_id, customer_id, "
        + "field1, field2, field3, field4, field5, field6, field7, field8, field9, "
        + "field10, field11, field12, field13, field14, field15, field16, field17, field18, field19,"
        + "field20, field21, field22, field23, field24, field25, field26, field27, field28, field29"
        + ") values " + rowValueString;
  }

  private String productInsertStatement(String rowValueString) {
    return "insert into Products(product_name, "
        + "field1, field2, field3, field4, field5, field6, field7, field8, field9, "
        + "field10, field11, field12, field13, field14, field15, field16, field17, field18, field19,"
        + "field20, field21, field22, field23, field24, field25, field26, field27, field28, field29"
        + ") values " + rowValueString;
  }

  private static String customerInsertStatement(String rowValueString) {
    return "insert into Customers(name, "
        + "field1, field2, field3, field4, field5, field6, field7, field8, field9, "
        + "field10, field11, field12, field13, field14, field15, field16, field17, field18, field19,"
        + "field20, field21, field22, field23, field24, field25, field26, field27, field28, field29"
        + ") values " + rowValueString;
  }

  private String orderRow(int insertCount, int numberCustomers, int numberProducts) {
    int selectCustomerId = RAND.nextInt(numberCustomers) + 1;
    int selectProductId = RAND.nextInt(numberProducts) + 1;

    StringBuilder row = new StringBuilder("('" + selectProductId + "', '" + selectCustomerId + "'");
    for (int i = 1; i < 5; i++) {
      row.append(", '").append(field(i, selectProductId)).append("'");
    }
    for (int i = 5; i < 9; i++) {
      row.append(", '").append(field(i - 4, selectCustomerId)).append("'");
    }
    for (int i = 9; i < 30; i++) {
      row.append(", '").append(field(i, insertCount)).append("'");
    }
    return row.append(")\n").toString();
  }

  private static String row(int insertCount) {
    StringBuilder row = new StringBuilder("('name_" + insertCount + "'");
    for (int i = 1; i < 30; i++) {
      row.append(", '").append(field(i, insertCount)).append("'");
    }
    return row.append(")\n").toString();
  }

  private static String field(int fieldNum, int insertCount) {
    return "field" + fieldNum + "_" + insertCount;
  }

}
