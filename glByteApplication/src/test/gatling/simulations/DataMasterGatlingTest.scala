import _root_.io.gatling.core.scenario.Simulation
import ch.qos.logback.classic.{Level, LoggerContext}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

/**
 * Performance test for the DataMaster entity.
 */
class DataMasterGatlingTest extends Simulation {

    val context: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
    // Log all HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))
    // Log failed HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("DEBUG"))

    val baseURL = Option(System.getProperty("baseURL")) getOrElse """http://127.0.0.1:8080"""

    val httpConf = http
        .baseURL(baseURL)
        .inferHtmlResources()
        .acceptHeader("*/*")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
        .connectionHeader("keep-alive")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:33.0) Gecko/20100101 Firefox/33.0")

    val headers_http = Map(
        "Accept" -> """application/json"""
    )

    val headers_http_authentication = Map(
        "Content-Type" -> """application/json""",
        "Accept" -> """application/json"""
    )

    val headers_http_authenticated = Map(
        "Accept" -> """application/json""",
        "Authorization" -> "${access_token}"
    )

    val scn = scenario("Test the DataMaster entity")
        .exec(http("First unauthenticated request")
        .get("/api/account")
        .headers(headers_http)
        .check(status.is(401))).exitHereIfFailed
        .pause(10)
        .exec(http("Authentication")
        .post("/api/authenticate")
        .headers(headers_http_authentication)
        .body(StringBody("""{"username":"admin", "password":"admin"}""")).asJSON
        .check(header.get("Authorization").saveAs("access_token"))).exitHereIfFailed
        .pause(1)
        .exec(http("Authenticated request")
        .get("/api/account")
        .headers(headers_http_authenticated)
        .check(status.is(200)))
        .pause(10)
        .repeat(2) {
            exec(http("Get all dataMasters")
            .get("/agreeapplication/api/data-masters")
            .headers(headers_http_authenticated)
            .check(status.is(200)))
            .pause(10 seconds, 20 seconds)
            .exec(http("Create new dataMaster")
            .post("/agreeapplication/api/data-masters")
            .headers(headers_http_authenticated)
            .body(StringBody("""{"id":null, "tenantId":null, "profileId":null, "templateId":null, "fileName":"SAMPLE_TEXT", "fileDate":"2020-01-01T00:00:00.000Z", "lineContent":null, "field01":"SAMPLE_TEXT", "field02":"SAMPLE_TEXT", "field03":"SAMPLE_TEXT", "field04":"SAMPLE_TEXT", "field05":"SAMPLE_TEXT", "field06":"SAMPLE_TEXT", "field07":"SAMPLE_TEXT", "field08":"SAMPLE_TEXT", "field09":"SAMPLE_TEXT", "field10":"SAMPLE_TEXT", "field11":"SAMPLE_TEXT", "field12":"SAMPLE_TEXT", "field13":"SAMPLE_TEXT", "field14":"SAMPLE_TEXT", "field15":"SAMPLE_TEXT", "field16":"SAMPLE_TEXT", "field17":"SAMPLE_TEXT", "field18":"SAMPLE_TEXT", "field19":"SAMPLE_TEXT", "field20":"SAMPLE_TEXT", "field21":"SAMPLE_TEXT", "field22":"SAMPLE_TEXT", "field23":"SAMPLE_TEXT", "field24":"SAMPLE_TEXT", "field25":"SAMPLE_TEXT", "field26":"SAMPLE_TEXT", "field27":"SAMPLE_TEXT", "field28":"SAMPLE_TEXT", "field29":"SAMPLE_TEXT", "field30":"SAMPLE_TEXT", "field31":"SAMPLE_TEXT", "field32":"SAMPLE_TEXT", "field33":"SAMPLE_TEXT", "field34":"SAMPLE_TEXT", "field35":"SAMPLE_TEXT", "field36":"SAMPLE_TEXT", "field37":"SAMPLE_TEXT", "field38":"SAMPLE_TEXT", "field39":"SAMPLE_TEXT", "field40":"SAMPLE_TEXT", "field41":"SAMPLE_TEXT", "field42":"SAMPLE_TEXT", "field43":"SAMPLE_TEXT", "field44":"SAMPLE_TEXT", "field45":"SAMPLE_TEXT", "field46":"SAMPLE_TEXT", "field47":"SAMPLE_TEXT", "field48":"SAMPLE_TEXT", "field49":"SAMPLE_TEXT", "field50":"SAMPLE_TEXT", "field51":"SAMPLE_TEXT", "field52":"SAMPLE_TEXT", "field53":"SAMPLE_TEXT", "field54":"SAMPLE_TEXT", "field55":"SAMPLE_TEXT", "field56":"SAMPLE_TEXT", "field57":"SAMPLE_TEXT", "field58":"SAMPLE_TEXT", "field59":"SAMPLE_TEXT", "field60":"SAMPLE_TEXT", "field61":"SAMPLE_TEXT", "field62":"SAMPLE_TEXT", "field63":"SAMPLE_TEXT", "field64":"SAMPLE_TEXT", "field65":"SAMPLE_TEXT", "field66":"SAMPLE_TEXT", "field67":"SAMPLE_TEXT", "field68":"SAMPLE_TEXT", "field69":"SAMPLE_TEXT", "field70":"SAMPLE_TEXT", "field71":"SAMPLE_TEXT", "field72":"SAMPLE_TEXT", "field73":"SAMPLE_TEXT", "field74":"SAMPLE_TEXT", "field75":"SAMPLE_TEXT", "field76":"SAMPLE_TEXT", "field77":"SAMPLE_TEXT", "field78":"SAMPLE_TEXT", "field79":"SAMPLE_TEXT", "field80":"SAMPLE_TEXT", "field81":"SAMPLE_TEXT", "field82":"SAMPLE_TEXT", "field83":"SAMPLE_TEXT", "field84":"SAMPLE_TEXT", "field85":"SAMPLE_TEXT", "field86":"SAMPLE_TEXT", "field87":"SAMPLE_TEXT", "field88":"SAMPLE_TEXT", "field89":"SAMPLE_TEXT", "field90":"SAMPLE_TEXT", "field91":"SAMPLE_TEXT", "field92":"SAMPLE_TEXT", "field93":"SAMPLE_TEXT", "field94":"SAMPLE_TEXT", "field95":"SAMPLE_TEXT", "field96":"SAMPLE_TEXT", "field97":"SAMPLE_TEXT", "field98":"SAMPLE_TEXT", "field99":"SAMPLE_TEXT", "field100":"SAMPLE_TEXT", "reconFlag":"SAMPLE_TEXT", "reconciledDate":"SAMPLE_TEXT", "accountedFlag":"SAMPLE_TEXT", "createdBy":null, "createdDate":"2020-01-01T00:00:00.000Z", "lastUpdatedBy":null, "lastUpdatedDate":"2020-01-01T00:00:00.000Z"}""")).asJSON
            .check(status.is(201))
            .check(headerRegex("Location", "(.*)").saveAs("new_dataMaster_url"))).exitHereIfFailed
            .pause(10)
            .repeat(5) {
                exec(http("Get created dataMaster")
                .get("/agreeapplication${new_dataMaster_url}")
                .headers(headers_http_authenticated))
                .pause(10)
            }
            .exec(http("Delete created dataMaster")
            .delete("/agreeapplication${new_dataMaster_url}")
            .headers(headers_http_authenticated))
            .pause(10)
        }

    val users = scenario("Users").exec(scn)

    setUp(
        users.inject(rampUsers(100) over (1 minutes))
    ).protocols(httpConf)
}
