# astra-and-startree-demo
A collection of the things used during the Astra and StarTree demonstration

> This is a WIP at the moment. It doesn't work... yet.

We are using [Astra CLI](https://awesome-astra.github.io/docs/pages/astra/astra-cli/) to interact with Astra

## Create the database to hold raw data

```bash
ORG_ID=$(astra org id)
CLOUD_PROVIDER_DB="GCP"
CLOUD_REGION_DB="us-central1"
CLOUD_PROVIDER_STREAMING="gcp"
CLOUD_REGION_STREAMING="uscentral1"
DB_NAME="webstore-page-views"
KEYSPACE_NAME="page_view_data"
astra db create --keyspace ${KEYSPACE_NAME} --cloud ${CLOUD_PROVIDER_DB} --region ${CLOUD_REGION_DB} --wait ${DB_NAME}
DB_ID=$(astra db get ${DB_NAME} --key id)
```

## Create the raw clicks table

```bash
TABLE_NAME="raw_clicks"
astra db cqlsh ${DB_NAME} --execute "CREATE TABLE IF NOT EXISTS ${KEYSPACE_NAME}.${TABLE_NAME}(click_epoch bigint PRIMARY KEY, UTC_offset int, request_url text, user_agent text, visitor_id text, coords text);"
```

## Create the streaming tenant for CDC

```bash
TENANT="webstore-page-views"
astra streaming create --cloud ${CLOUD_PROVIDER_STREAMING} --region ${CLOUD_REGION_STREAMING} ${TENANT}
WEB_SERVICE_URL=$(astra streaming get ${TENANT} -o json | jq -r '.data.cellValues[] | select(.Attribute == "WebServiceUrl") | .Value')
CLUSTER_NAME=$(astra streaming get ${TENANT} -o json | jq -r '.data.cellValues[] | select(.Attribute == "Cluster Name") | .Value')
TOKEN=$(astra streaming pulsar-token ${TENANT})
```

## Enable CDC on the table to create a corresponding topic in the new tenant

```bash
curl -X POST ${WEB_SERVICE_URL}/admin/v3/astra/tenants/${TENANT}/cdc \
  --header "Authorization: ${TOKEN}" \
  --header "X-DataStax-Pulsar-Cluster: ${CLUSTER_NAME}" \
  --data '{
  "databaseId": "'${DB_ID}'",
  "databaseName": "'${DB_NAME}'",
  "keyspace": "'${KEYSPACE_NAME}'",
  "orgId": "'${ORG_ID}'",
  "tableName": "'${TABLE_NAME}'",
  "topicPartitions": 3
}'
```

## Create table schema

```bash
./pinot-admin.sh AddSchema -schemaFile ./full-click-data-schema.json -exec
```

## Create Pinot table using schema

```bash
/pinot-admin.sh AddTable -tableConfigFile ./full_click_data_config.json -exec
```

## Create Pulsar ingestion for Pinot table

```bash
./pinot-admin.sh LaunchDataIngestionJob -jobSpecFile ./pulsar-ingestion-job-spec.yaml
```