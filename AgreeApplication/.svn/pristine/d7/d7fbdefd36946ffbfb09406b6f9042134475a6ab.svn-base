import _root_.io.gatling.core.scenario.Simulation
import ch.qos.logback.classic.{Level, LoggerContext}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

/**
 * Performance test for the BalanceType entity.
 */
class BalanceTypeGatlingTest extends Simulation {

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

    val scn = scenario("Test the BalanceType entity")
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
            exec(http("Get all balanceTypes")
            .get("/agreeapplication/api/balance-types")
            .headers(headers_http_authenticated)
            .check(status.is(200)))
            .pause(10 seconds, 20 seconds)
            .exec(http("Create new balanceType")
            .post("/agreeapplication/api/balance-types")
            .headers(headers_http_authenticated)
            .body(StringBody("""{"id":null, "reportId":null, "tenantId":null, "srcId":null, "field01":"SAMPLE_TEXT", "field02":"SAMPLE_TEXT", "field03":"SAMPLE_TEXT", "field04":"SAMPLE_TEXT", "field05":"SAMPLE_TEXT", "field06":"SAMPLE_TEXT", "field07":"SAMPLE_TEXT", "field08":"SAMPLE_TEXT", "field09":"SAMPLE_TEXT", "field10":"SAMPLE_TEXT", "field11":"SAMPLE_TEXT", "field12":"SAMPLE_TEXT", "field13":"SAMPLE_TEXT", "field14":"SAMPLE_TEXT", "field15":"SAMPLE_TEXT", "openingBalance":"SAMPLE_TEXT", "additionsAmt":"SAMPLE_TEXT", "reconciliationAmt":"0", "unReconAmt":"0", "accountingStatus":"SAMPLE_TEXT", "createdBy":null, "creationDate":"2020-01-01T00:00:00.000Z", "lastUpdatedBy":null, "lastUpdatedDate":"2020-01-01T00:00:00.000Z"}""")).asJSON
            .check(status.is(201))
            .check(headerRegex("Location", "(.*)").saveAs("new_balanceType_url"))).exitHereIfFailed
            .pause(10)
            .repeat(5) {
                exec(http("Get created balanceType")
                .get("/agreeapplication${new_balanceType_url}")
                .headers(headers_http_authenticated))
                .pause(10)
            }
            .exec(http("Delete created balanceType")
            .delete("/agreeapplication${new_balanceType_url}")
            .headers(headers_http_authenticated))
            .pause(10)
        }

    val users = scenario("Users").exec(scn)

    setUp(
        users.inject(rampUsers(Integer.getInteger("users", 100)) over (Integer.getInteger("ramp", 1) minutes))
    ).protocols(httpConf)
}
