package com.availity.spark.provider

import org.apache.spark.sql.SparkSession
//import org.apache.spark.sql.types.{StructType, DateType, StringType, StructField}
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._

object ProviderRoster {
  
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Provider Visits Analysis")
      .config("spark.master", "local")
      .getOrCreate()

    // process function is called here
    process(spark)

    spark.stop()
  }

  def process(spark: SparkSession): Unit = {
    import spark.implicits._

    // Load the data
    val providersDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .option("delimiter", "|")
      .csv("/Users/sbommineni/work/scala-spark/provider-roster/data/providers.csv")

    // Define schema for visits
    val visitSchema = StructType(Array(
      StructField("visit_id", StringType, true),
      StructField("provider_id", StringType, true),
      StructField("date_of_service", DateType, true)
    ))

    val visitsDF = spark.read
      .schema(visitSchema)
      .option("header", "true")
      .csv("/Users/sbommineni/work/scala-spark/provider-roster/data/visits.csv")

    // Task 1: Total number of visits per provider
    val visitsPerProvider = visitsDF
      .groupBy("provider_id")
      .agg(count("visit_id").alias("total_visits"))
      .join(providersDF, "provider_id")
      .select("provider_id", "first_name", "middle_name", "last_name", "provider_specialty", "total_visits")

    // Write the results partitioned by provider specialty

    visitsPerProvider.coalesce(1).write.mode("overwrite").format("json")
      .save("output/visits_per_provider_json")

    // Task 2: Total number of visits per provider per month
    val visitsPerProviderPerMonth = visitsDF
      .withColumn("month", month(col("date_of_service")))
      .groupBy("provider_id", "month")
      .agg(count("visit_id").alias("total_visits"))
      .join(providersDF, "provider_id")
      .select("provider_id", "month", "total_visits")

    // Write the results as JSON
   
    visitsPerProviderPerMonth.coalesce(1).write.mode("overwrite").format("json")
      .save("output/visits_per_provider_per_month_json")
  }
}