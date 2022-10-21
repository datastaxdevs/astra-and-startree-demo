package com.example.suggestionsexample;

import com.example.suggestionsexample.models.PageView;
import com.example.suggestionsexample.models.Suggestion;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.stargate.grpc.StargateBearerToken;
import io.stargate.proto.QueryOuterClass;
import io.stargate.proto.StargateGrpc;
import org.apache.pinot.client.Connection;
import org.apache.pinot.client.ConnectionFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class ClickDataController {
  private final ManagedChannel channel;
  private final Connection pinotConnection;
  private static final String ASTRA_DB_ID      = "<id>";
  private static final String ASTRA_DB_REGION  = "<region>";
  private static final String ASTRA_TOKEN      = "<token>";
  private static final String ASTRA_KEYSPACE   = "<keyspace>";

  ClickDataController() throws Exception {
    String zkUrl = "localhost:2181";
    String pinotClusterName = "PinotCluster";
    pinotConnection = ConnectionFactory.fromZookeeper(zkUrl + "/" + pinotClusterName);

    channel = ManagedChannelBuilder
        .forAddress(ASTRA_DB_ID + "-" + ASTRA_DB_REGION + ".apps.astra.datastax.com", 443)
        .useTransportSecurity()
        .build();
  }

  @PostMapping("/pv")
  Suggestion[] newPageView(@RequestBody @NotNull PageView newPageView) {

    String query = "insert into " + ASTRA_KEYSPACE + "click_data(click_epoch,request_url,visitor_id,utc_offset,coords,user_agent) " +
        String.format("values(%d,'%s','%s','%s','%s','%s')",
            newPageView.clickEpoch(),
            newPageView.requestUrl(),
            newPageView.visitorId(),
            newPageView.utcOffset(),
            newPageView.coords(),
            newPageView.userAgent());

    StargateGrpc.StargateBlockingStub blockingStub = StargateGrpc.newBlockingStub(channel)
        .withDeadlineAfter(10, TimeUnit.SECONDS)
        .withCallCredentials(new StargateBearerToken(ASTRA_TOKEN));

    QueryOuterClass.Response queryString = blockingStub.executeQuery(QueryOuterClass
        .Query.newBuilder()
        .setCql(query)
        .build());

//    query = "SELECT COUNT(*) FROM myTable GROUP BY foo";
//    // set queryType=sql for querying the sql endpoint
//    pinotClientRequest = new Request("sql", query);
//    ResultSetGroup pinotResultSetGroup = pinotConnection.execute(pinotClientRequest);
//    ResultSet resultTableResultSet = pinotResultSetGroup.getResultSet(0);
    var s = new Suggestion("asd","sdf","dfg", "fgh", new String[]{"asd","asd"});
    return new Suggestion[]{s};
  }
}
