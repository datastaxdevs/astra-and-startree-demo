package com.example.suggestionsexample;

import com.example.suggestionsexample.models.PageView;
import com.example.suggestionsexample.models.Suggestion;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.apache.pinot.client.Connection;
import org.apache.pinot.client.ConnectionFactory;
import org.apache.pinot.client.Request;
import org.apache.pinot.client.ResultSetGroup;
import org.apache.pinot.client.ResultSet;

@RestController
public class ClickDataController {
  private final Connection pinotConnection;

  ClickDataController() {
    String zkUrl = "localhost:2181";
    String pinotClusterName = "PinotCluster";
    pinotConnection = ConnectionFactory.fromZookeeper(zkUrl + "/" + pinotClusterName);
  }

  @PostMapping("/pv")
  Suggestion[] newPageView(@RequestBody PageView newPageView) {
    String query = "insert into myTable() values()";
    Request pinotClientRequest = new Request("sql", query);
    pinotConnection.executeAsync(pinotClientRequest);

    query = "SELECT COUNT(*) FROM myTable GROUP BY foo";
    // set queryType=sql for querying the sql endpoint
    pinotClientRequest = new Request("sql", query);
    ResultSetGroup pinotResultSetGroup = pinotConnection.execute(pinotClientRequest);
    ResultSet resultTableResultSet = pinotResultSetGroup.getResultSet(0);

    return new Suggestion[0];
  }
}
