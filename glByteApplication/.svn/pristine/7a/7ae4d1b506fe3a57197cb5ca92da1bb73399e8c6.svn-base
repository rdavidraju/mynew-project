import _root_.io.gatling.core.scenario.Simulation
import ch.qos.logback.classic.{Level, LoggerContext}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

/**
 * Performance test for the JournalApprovalInfo entity.
 */
class JournalApprovalInfoGatlingTest extends Simulation {

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

    val scn = scenario("Test the JournalApprovalInfo entity")
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
            exec(http("Get all journalApprovalInfos")
            .get("/agreeapplication/api/journal-approval-infos")
            .headers(headers_http_authenticated)
            .check(status.is(200)))
            .pause(10 seconds, 20 seconds)
            .exec(http("Create new journalApprovalInfo")
            .post("/agreeapplication/api/journal-approval-infos")
            .headers(headers_http_authenticated)
            .body(StringBody("""{"id":null, "jeHeaderId":null, "approvalGroupId":null, "approvalRuleId":null, "approvalInitiationDate":"2020-01-01T00:00:00.000Z", "approvalBatchId":null, "apprRef01":"SAMPLE_TEXT", "apprRef02":"SAMPLE_TEXT", "apprRef03":"SAMPLE_TEXT", "apprRef04":"SAMPLE_TEXT", "apprRef05":"SAMPLE_TEXT", "apprRef06":"SAMPLE_TEXT", "apprRef07":"SAMPLE_TEXT", "apprRef08":"SAMPLE_TEXT", "apprRef09":"SAMPLE_TEXT", "apprRef10":"SAMPLE_TEXT", "apprRef11":"SAMPLE_TEXT", "apprRef12":"SAMPLE_TEXT", "apprRef13":"SAMPLE_TEXT", "apprRef14":"SAMPLE_TEXT", "apprRef15":"SAMPLE_TEXT", "finalActionDate":"2020-01-01T00:00:00.000Z", "finalStatus":"SAMPLE_TEXT"}""")).asJSON
            .check(status.is(201))
            .check(headerRegex("Location", "(.*)").saveAs("new_journalApprovalInfo_url"))).exitHereIfFailed
            .pause(10)
            .repeat(5) {
                exec(http("Get created journalApprovalInfo")
                .get("/agreeapplication${new_journalApprovalInfo_url}")
                .headers(headers_http_authenticated))
                .pause(10)
            }
            .exec(http("Delete created journalApprovalInfo")
            .delete("/agreeapplication${new_journalApprovalInfo_url}")
            .headers(headers_http_authenticated))
            .pause(10)
        }

    val users = scenario("Users").exec(scn)

    setUp(
        users.inject(rampUsers(Integer.getInteger("users", 100)) over (Integer.getInteger("ramp", 1) minutes))
    ).protocols(httpConf)
}
