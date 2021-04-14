package testNG.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.collections.Lists;
import org.testng.internal.Utils;
import org.testng.xml.XmlSuite;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.tm4j.pojo.createcycle.CreateCycleData;
import com.tm4j.pojo.updatecycle.UpdateCycleData;
import com.tm4j.utils.TM4JApiHelper;
import com.tm4j.utils.Tm4jConstatnts;

import core.utilities.HTMLTableBuilder;
import core.utilities.LogParser;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @summary Reporter that generates a single-page HTML report of the test results
 */
public class TestNGEmailableReport implements IReporter {

	private static final Logger logger = Logger.getLogger(TestNGEmailableReport.class);

	protected PrintWriter writer;
	public static String subcategory;
	protected final List<SuiteResult> suiteResults = Lists.newArrayList();
	private final StringBuilder buffer = new StringBuilder();

	private String dReportTitle = "TEST RESULTS - SUMMARY";
	private String dReportFileName = "emailable-report.html";

	public static String getSubcategory() {
		return subcategory;
	}

	public static void setSubcategory(String subcategory) {
		TestNGEmailableReport.subcategory = subcategory;
	}

	private String getCurrentJenkinsBuildNumber() {
		if (System.getProperty("buildNumber") != null) {
			return System.getProperty("buildNumber");
		}
		return "NO JENKINS";
	}

	private String getCurrentJenkinsUrl() {
		if (System.getProperty("buildUrl") != null) {
			return System.getProperty("buildUrl");
		}
		return "NO JENKINS";
	}

	private static String getEmailSubjectLine() {
		if (System.getProperty("subjectLine") != null) {
			return System.getProperty("subjectLine");
		}
		return "DUMMY SUBJECT";
	}
	
	private static String getSuiteName() {
		if (System.getProperty("subjectLine") != null) {
			logger.info("Subject Line :" +getEmailSubjectLine());
			String[] val = System.getProperty("subjectLine").split(" ");
			return val[0];
		}
		return "DUMMY SUBJECT";
	}

	private String getAppUrl() {
		return System.getProperty("appUrl");
	}

	private String browserName() {
		return System.getProperty("browser");
	}

	private String dReportTitle() {
		return getEmailSubjectLine();
	}

	public String getCurrentDateTime() {
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		return formatter.format(currentDate.getTime());
	}

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		try {
			writer = createWriter(outputDirectory);
		} catch (IOException e) {
			logger.error("Unable to create output file", e);
			return;
		}
		for (ISuite suite : suites) {
			suiteResults.add(new SuiteResult(suite));
		}

		writeDocumentStart();
		writeHead();
		writeBody();
		writePerfResults();
		writeFooter();
		writeDocumentEnd();
		writer.close();
		for (SuiteResult suiteResult : suiteResults) {
			try {
				updateCreateCycle(suiteResult);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Generate CSV file.
		try {
			MergeJsons mergeJsons = new MergeJsons();
			List<Map<String, String>> jsonObject = mergeJsons.getJsonObject("target");
			if (jsonObject.size() > 0) {
				JsonToCsv jsonToCsv = new JsonToCsv();
				jsonToCsv.csvWriter(jsonObject, "matrix");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Push Performance result to elastic search
		pushPerfResultToElastic();
		logger.info("Done");
	}

	private static void ElasticSearch(String name, String duration, String category, String subcategory,
			Calendar testStartTime, String Exception) {
		// TODO Auto-generated method stub

		try {
			BasicInfoUtility basicInfoUtility = new BasicInfoUtility(System.getProperty("appUrl"));
			String build = basicInfoUtility.getBuildInfo();
			String env = basicInfoUtility.getEnvName();
			String browser = System.getProperty("browser");
			Calendar starttime = testStartTime;
			String execon = starttime.getTime().toString();
			String TCstatus = null;
			String bugid = null;
			String notes = null;

			if (Exception.isBlank()) {
				TCstatus = "Pass";
			} else {
				TCstatus = "FAIL";
			}

			OkHttpClient client = new OkHttpClient().newBuilder().build();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType,
					"{\r\n\"build\":\"" + build + "\",\r\n\"environment\":\"" + env + "\", \r\n\"environment\":\"" + System.getProperty("subjectLine") + "\", \r\n\"browser\":\"" + browser
							+ "\",\r\n\"testcasename\":\"" + name + "\",\r\n\"testcasecategory\":\"" + category
							+ "\",\r\n\"testcasebattery\":\"" + subcategory + "\",\r\n\"testcasestatus\":\"" + TCstatus
							+ "\",\r\n\"testcaseduration\":\"" + duration + "\",\r\n\"testcaseexception\":\"" + Exception
							+ "\",\r\n\"execon\":\"" + execon + "\",\r\n\"bugid\":\"" + bugid + "\",\r\n\"notes\":\""
							+ notes + "\"\r\n}\r\n");
			Request request = new Request.Builder().url("http://172.17.0.13:9200/qa-results-automationrun/_doc/")
					.method("POST", body).addHeader("Content-Type", "application/json").build();
			okhttp3.Response response = client.newCall(request).execute();
			logger.info(" request is " + request);
			logger.info(" response is " + response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void pushPerfResultToElastic() {
		LogParser logParser = new LogParser("logging.log");
		Map<String, String> readLogFileWithTimeStamp = logParser.readLogFileWithTimeStamp();
		Set<String> keySet = readLogFileWithTimeStamp.keySet();
		for (String key : keySet) {
			String[] keyArray = key.split("##");
			String duration = readLogFileWithTimeStamp.get(key);
			perfResult(keyArray[1], duration, keyArray[0]);
		}
	}

	private static void perfResult(String actionName, String actionDuration, String timeStamp) {
		try {
			BasicInfoUtility basicInfoUtility = new BasicInfoUtility(System.getProperty("appUrl"));
			String build = "Branch: origin/20H1 Build: 2275";
			String env = basicInfoUtility.getEnvName();
			String browser = System.getProperty("browser");
			String suiteName = getSuiteName();

			OkHttpClient client = new OkHttpClient().newBuilder().build();
			MediaType mediaType = MediaType.parse("application/json");
			Map<String, String> valueMap = new HashMap<String, String>();
			valueMap.put("build", build);
			valueMap.put("environment", env);
			valueMap.put("browser", browser);
			valueMap.put("action", actionName);
			valueMap.put("actionDuration", actionDuration);
			valueMap.put("suiteName", suiteName);
			valueMap.put("timestamp", timeStamp);
			String jsonBody = new Gson().toJson(valueMap);

			RequestBody body = RequestBody.create(mediaType, jsonBody);
			Request request = new Request.Builder().url("http://172.17.0.13:9200/performance-result/_doc/")
					.method("POST", body).addHeader("Content-Type", "application/json").build();
			okhttp3.Response response = client.newCall(request).execute();
			logger.info(" request is " + request);
			logger.info(" response is " + response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeBuildInfo() {
		try {
			if (System.getProperty("baseUrl") == null) {
				BasicInfoUtility basicInfoUtility = new BasicInfoUtility(getAppUrl());
				String buildInfo = basicInfoUtility.getBuildInfo();
				String envName = basicInfoUtility.getEnvName();
				writer.write("<p>");
				writeReportTitle(dReportTitle);
				writer.write("<br>");
				writer.write("ENVIRONMENT: ");
				writer.write(envName);
				writer.write("</br>");
				writer.write("BUILD: ");
				writer.write(buildInfo);
				writer.write("<br>");
				writer.write("BROWSER: ");
				writer.write(browserName());
				writer.write("</br>");
				writer.write("</p>");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void writeFooter() {
		writer.write("<footer>");
		writer.write("<hr>");
		writer.write(
				"<p>This message was sent from an unmonitored account. Please address replies to <a href=\"mailto:TestinQ@mezocliq.com\">TestinQ@mezocliq.com</a></p>");
		writer.write("</hr>");
		writer.write("</footer>");
	}

	public void writePerfResults() {
		int serialNumber = 0;
		HTMLTableBuilder htmlBuilder = new HTMLTableBuilder(null, true, 2, 8);
		htmlBuilder.addTableHeader("#", "ACTIVITY", "COUNT", "MIN", "MAX", "MEAN", "90P", "95P");
		LogParser logParser = new LogParser("logging.log");
		Map<String, String> readLogFile = logParser.readLogFile();
		Map<String, Long> resultList90P = logParser.actionResultList(readLogFile, 90);
		Map<String, Long> resultList95P = logParser.actionResultList(readLogFile, 95);
		Set<String> keySet = resultList90P.keySet();
		for (String key : keySet) {
			serialNumber = serialNumber + 1;
			Long val90P = resultList90P.get(key);
			long seconds90P = val90P;
			Long val95P = resultList95P.get(key);
			long seconds95P = val95P;
			String[] keys = key.split("###");
			String activity = keys[0];
			String count = keys[1];
			String min = keys[2];
			String max = keys[3];
			String colorCodedPercentile = String.valueOf(seconds90P);
			if (seconds90P >= 5000) {
				colorCodedPercentile = String.valueOf(seconds90P) + ":COLOR-RED";
			} else if (seconds90P <= 5000 && seconds90P >= 3000) {
				colorCodedPercentile = String.valueOf(seconds90P) + ":COLOR-YELLOW";
			} else if (seconds90P <= 3000) {
				colorCodedPercentile = String.valueOf(seconds90P) + ":COLOR-GREEN";
			}
			String mean = keys[4];
			htmlBuilder.addRowValues(String.valueOf(serialNumber), activity.toUpperCase() + " ", count + " ", min + " ",
					max + " ", mean + " ", colorCodedPercentile + " ", seconds95P + " ");
		}
		String table = htmlBuilder.build();
		writer.write("<b>TEST RESULTS - PERFORMANCE</b>");
		writer.write(table);
		writer.write("<b>THRESHOLDS: ");
		writer.write("<span style='background-color:#90ee90'>");
		writer.write("T");
		writer.write("<span style='background-color:#90ee90' >&#8804;</span>");
		writer.write("3000");
		writer.write("</span>");
		writer.write("<span>, &nbsp;</span>");

		writer.write("<span style='background-color:#ffd700'>");
		writer.write("3000");
		writer.write("<span style='background-color:#ffd700' >&#8804;</span>");
		writer.write("T");
		writer.write("<span style='background-color:#ffd700' >&#8805;</span>");
		writer.write("5000");
		writer.write("</span>");
		writer.write("<span>, &nbsp;</span>");

		writer.write("<span style='background-color:#f4cccc'>");
		writer.write("T");
		writer.write("<span style='background-color:#f4cccc' >&#8805;</span>");
		writer.write("5000");
		writer.write("</span>");
		writer.write("</b>");
	}

	protected PrintWriter createWriter(String outdir) throws IOException {
		new File(outdir).mkdirs();
		return new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir, dReportFileName))));
	}

	private void writeDetailsLink() {
		writer.println("<p> <a href='" + getCurrentJenkinsUrl() + "allure"
				+ "'> Click here </a>for detailed test results (Accessible only on VMs).</p>");
	}

	protected void writeDocumentStart() {
		writer.println(
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
		writer.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
	}

	protected void writeHead() {
		writer.println("<head>");
		writer.println("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>");
		writer.println("<title>Test Report</title>");
		writeStylesheet();
		writer.println("</head>");
	}

	protected void writeStylesheet() {
		writer.println(
				"<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\">");
		writer.print("<style type=\"text/css\">");
		writer.print("table {margin-bottom:10px;border-collapse:collapse;empty-cells:show; border: 1px solid black}");
		writer.print("#summary");
		writer.print("h1 {font-size:30px}");
		writer.print("body {width:100%; margin-left:10px}");
		writer.print("th,td {padding: 4px; border: 1px solid black}");
		writer.print("th {vertical-align:bottom}");
		writer.print("td {vertical-align:top}");
		writer.print("table a {font-weight:bold;color:#0D1EB6;}");
		writer.print(".easy-overview");
		writer.print(".easy-test-overview tr:first-child {color:#000}");
		writer.print(".stripe td {color: #E6EBF9}");
		writer.print(".num {text-align:center}");
		writer.print(".passedodd td {color: #0b6623}");
		writer.print(".passedeven td {color: #0b6623}");
		writer.print(".skippedodd td {color: #CCC}");
		writer.print(".skippedeven td {color: #CCC}");
		writer.print(".failedodd td,.attn {color: #F33}");
		writer.print(".failedeven td,.stripe .attn {color: #F33}");
		writer.print(".stacktrace {font-family:monospace}");
		writer.print(".totop {font-size:85%;text-align:center;border-bottom:2px solid #000}");
		writer.print(".invisible {display:none}");
		writer.print(".center {  text-align:center; background-color: #d3d3d3}");
		writer.println("</style>");
	}

	protected void writeBody() {
		writer.println("<body>");
		writeSuiteSummary();
		writeScenarioSummary();
		writeScenarioDetails();
		writer.println("</body>");
	}

	protected void writeDocumentEnd() {
		writer.println("</html>");
	}

	protected void writeReportTitle(String title) {
		writer.print("<b>" + title + "</b>");
	}

	private void updateCreateCycle(SuiteResult suiteResult) throws IOException {
		// Create test cycle ..
		String run_name = suiteResult.getSuiteName();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime now = LocalDateTime.now();
		String dateTime = dtf.format(now);

		CreateCycleData createCycleData = new CreateCycleData();
		createCycleData.setProjectKey(Tm4jConstatnts.projectyKey);
		createCycleData.setName(run_name + "_" + dateTime);
		createCycleData.setDescription(suiteResult.getSuiteName());
		JsonElement createCycle = TM4JApiHelper.createCycle(new Gson().toJson(createCycleData));
		String cycleKey = createCycle.getAsJsonObject().get("key").getAsString();

		List<TestResult> testResults = suiteResult.getTestResults();
		for (TestResult testResult : testResults) {
			List<ClassResult> allTests = new ArrayList<TestNGEmailableReport.ClassResult>();
			allTests.addAll(testResult.getFailedConfigurationResults());
			allTests.addAll(testResult.getFailedTestResults());
			allTests.addAll(testResult.getSkippedConfigurationResults());
			allTests.addAll(testResult.getSkippedTestResults());
			allTests.addAll(testResult.getPassedTestResults());

			for (ClassResult classResult : allTests) {
				List<MethodResult> methodResults = classResult.getMethodResults();
				for (MethodResult methodResult : methodResults) {
					List<ITestResult> results = methodResult.getResults();

					for (ITestResult result : results) {
						ITestResult firstResult = results.iterator().next();
						Link annotation = firstResult.getMethod().getConstructorOrMethod().getMethod()
								.getAnnotation(Link.class);
						String testKey = Utils.escapeHtml(annotation.value());
						String statusString;
						int status = firstResult.getStatus();
						if (status == ITestResult.FAILURE) {
							statusString = "Fail";
						} else if (status == ITestResult.SUCCESS) {
							statusString = "Pass";
						} else {
							statusString = "Not Executed";
						}
						UpdateCycleData updateCycleData = new UpdateCycleData();
						updateCycleData.setProjectKey(Tm4jConstatnts.projectyKey);
						updateCycleData.setTestCycleKey(cycleKey);
						updateCycleData.setTestCaseKey(testKey);
						updateCycleData.setStatusName(statusString);
						logger.info("************************************************");
						logger.info(new Gson().toJson(updateCycleData));
						logger.info("************************************************");
						TM4JApiHelper.updateTestResult(new Gson().toJson(updateCycleData));
					}
				}
			}

		}
	}

	protected void writeSuiteSummary() {
		NumberFormat integerFormat = NumberFormat.getIntegerInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");

		int totalTestsCount = 0;
		int totalPassedTests = 0;
		int totalSkippedTests = 0;
		int totalFailedTests = 0;
		long totalDuration = 0;
		//writeBuildInfo(); //REmoving for the time due to performance issue
		writer.println("<div class=\"easy-test-overview\">");
		writer.println("<table class=\"table-bordered easy-overview\">");
		writer.print("<tr>");
		writer.print("<th class=\"center\">CATEGORY</th>");
		writer.print("<th class=\"center\">SUBCATEGORY</th>");
		writer.print("<th class=\"center\">TOTAL</th>");
		writer.print("<th class=\"center\">PASSED</th>");
		writer.print("<th class=\"center\">SKIPPED</th>");
		writer.print("<th class=\"center\">FAILED</th>");
		writer.print("<th class=\"center\">START TIME (EST)</th>");
		writer.print("<th class=\"center\">END TIME (EST)</th>");
		writer.print("<th class=\"center\">DURATION<br/>(hh:mm:ss)</th>");
		writer.println("</tr>");

		int testIndex = 0;
		for (SuiteResult suiteResult : suiteResults) {
			for (TestResult testResult : suiteResult.getTestResults()) {
				int testsCount = testResult.getTestCount();
				int passedTests = testResult.getPassedTestCount();
				int skippedTests = testResult.getSkippedTestCount();
				int failedTests = testResult.getFailedTestCount();

				Date startTime = testResult.getTestStartTime();
				Date endTime = testResult.getTestEndTime();
				long duration = testResult.getDuration();

				writer.print("<tr");
				writer.print(">");
				setSubcategory(testResult.getSubcategory().toString());
				buffer.setLength(0);
				writeTableData(Utils.escapeHtml(testResult.getCategory()).toString());
				writeTableData(Utils.escapeHtml(testResult.getSubcategory()).toString());
				writeTableData(integerFormat.format(testsCount), "num");
				writeTableData(integerFormat.format(passedTests), "num");
				writeTableData(integerFormat.format(skippedTests), (skippedTests > 0 ? "num attn" : "num"));
				writeTableData(integerFormat.format(failedTests), (failedTests > 0 ? "num attn" : "num"));
				writeTableData(dateFormat.format(startTime), "num");
				writeTableData(dateFormat.format(endTime), "num");
				writeTableData(convertTimeToString(duration), "num");
				writer.println("</tr>");

				totalTestsCount += testsCount;
				totalPassedTests += passedTests;
				totalSkippedTests += skippedTests;
				totalFailedTests += failedTests;
				totalDuration += duration;
				testIndex++;
			}
		}

		// Print totals if there was more than one test
		if (testIndex > 1) {

			writer.print("<tr>");
			writer.print("<th>Total</th>");
			writer.print("<th colspan=\"1\"></th>");
			writeTableHeader(integerFormat.format(totalTestsCount), "num");
			writeTableHeader(integerFormat.format(totalPassedTests), "num");
			writeTableHeader(integerFormat.format(totalSkippedTests), (totalSkippedTests > 0 ? "num attn" : "num"));
			writeTableHeader(integerFormat.format(totalFailedTests), (totalFailedTests > 0 ? "num attn" : "num"));
			writer.print("<th colspan=\"2\"></th>");
			writeTableHeader(convertTimeToString(totalDuration), "num");
			writer.println("</tr>");
		}
		writer.println("</table>");
		writeDetailsLink();
		writer.println("</div>");
	}

	/**
	 * Writes a summary of all the test scenarios.
	 */
	protected void writeScenarioSummary() {
		writer.print("<div class=\"easy-test-summary\">");
		writer.print("<b>TEST RESULTS - DETAILS </b>");
		writer.print("<table class=\"table-bordered\" id='summary'>");
		writer.print("<thead>");
		writer.print("<tr>");
		writer.print("<th class=\"center\"\">#</th>");
		writer.print("<th class=\"center\"\">CATEGORY</th>");
		writer.print("<th class=\"center\"\">SUBCATEGORY</th>");
		writer.print("<th class=\"center\"\">TEST CAPTION</th>");
		writer.print("<th class=\"center\"\">TEST DESCRIPTION</th>");
		writer.print("<th class=\"center\"\">START TIME</th>");
		writer.print("<th class=\"center\"\">END TIME</th>");
		writer.print("<th class=\"center\"\">DURATION<br/>(hh:mm:ss)</th>");
		writer.print("<th class=\"center\"\">EXCEPTION</th>");
		writer.print("</tr>");
		writer.print("</thead>");

		int testIndex = 0;
		int scenarioIndex = 0;

		for (SuiteResult suiteResult : suiteResults) {
			for (TestResult testResult : suiteResult.getTestResults()) {
				writer.printf("<tbody id=\"t%d\">", testIndex);
				String name = Utils.escapeHtml(testResult.getname());
				int startIndex = scenarioIndex;

				scenarioIndex += writeScenarioSummary(name + " &#8212; failed (configuration methods)",
						testResult.getFailedConfigurationResults(), "failed", scenarioIndex);
				scenarioIndex += writeScenarioSummary(name + " &#8212; FAILED", testResult.getFailedTestResults(),
						"failed", scenarioIndex);
				scenarioIndex += writeScenarioSummary(name + " &#8212; skipped (configuration methods)",
						testResult.getSkippedConfigurationResults(), "skipped", scenarioIndex);
				scenarioIndex += writeScenarioSummary(name + " &#8212; SKIPPED", testResult.getSkippedTestResults(),
						"skipped", scenarioIndex);
				scenarioIndex += writeScenarioSummary(name + " &#8212; PASSED", testResult.getPassedTestResults(),
						"passed", scenarioIndex);

				if (scenarioIndex == startIndex) {
					writer.print("<tr><th colspan=\"4\" class=\"invisible\"/></tr>");
				}

				writer.println("</tbody>");

				testIndex++;
			}
		}

		writer.println("</table>");
		writer.println("</div>");
	}

	/**
	 * Writes the scenario summary for the results of a given state for a single
	 * test.
	 */
	private int writeScenarioSummary(String name, List<ClassResult> classResults, String cssClassPrefix,
			int startingScenarioIndex) {
		int scenarioCount = 0;
		if (!classResults.isEmpty()) {
			/*
			 * writer.print("<tr><th colspan=\"6\">"); writer.print(name);
			 * writer.print("</th></tr>");
			 */

			int scenarioIndex = startingScenarioIndex;
			int classIndex = 0;
			for (ClassResult classResult : classResults) {
				String cssClass = cssClassPrefix + ((classIndex % 2) == 0 ? "even" : "odd");

				buffer.setLength(0);

				int scenariosPerClass = 0;
				int methodIndex = 0;

				for (MethodResult methodResult : classResult.getMethodResults()) {
					List<ITestResult> results = methodResult.getResults();
					int resultsCount = results.size();
					assert resultsCount > 0;

					ITestResult firstResult = results.iterator().next();
					/*
					 * String methodName = Utils.escapeHtml(firstResult
					 * .getMethod().getMethodName());
					 */
					Description annotation = firstResult.getMethod().getConstructorOrMethod().getMethod()
							.getAnnotation(Description.class);
					String description = Utils.escapeHtml(annotation.value());

					Epic categoryAnnotation = firstResult.getMethod().getConstructorOrMethod().getMethod()
							.getAnnotation(Epic.class);
					String category = Utils.escapeHtml(categoryAnnotation.value());

					Feature featureAnnotation = firstResult.getMethod().getConstructorOrMethod().getMethod()
							.getAnnotation(Feature.class);
					String feature = Utils.escapeHtml(featureAnnotation.value());

					long start = firstResult.getStartMillis();
					long end = firstResult.getEndMillis();

					String shortException = "";

					String failureScreenShot = "";

					Throwable exception = firstResult.getThrowable();
					boolean hasThrowable = exception != null;
					if (hasThrowable) {
						String str = Utils.shortStackTrace(exception, true);
						Scanner scanner = new Scanner(str);
						shortException = scanner.nextLine();
						scanner.close();
						List<String> msgs = Reporter.getOutput(firstResult);
						boolean hasReporterOutput = msgs.size() > 0;
						if (hasReporterOutput) {
							for (String info : msgs) {
								failureScreenShot += info + "<br/>";
							}
						}
					}

					// Remove assertionError tag from Exception
					if (shortException.contains(":")) {
						shortException = shortException.split(":")[1];
					}

					DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
					Calendar startTime = Calendar.getInstance();
					startTime.setTimeInMillis(start);

					Calendar endTime = Calendar.getInstance();
					endTime.setTimeInMillis(end);

					// The first method per class shares a row with the class
					// header
					if (methodIndex > 0) {
						buffer.append("<tr class=\"").append(cssClass).append("\">");

					}

					// Write the timing information with the first scenario per
					// method
					String fullClassName = classResult.getClassName();
					String simpleClassName = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
					String cname = Utils.escapeHtml(simpleClassName);
					String finalname = cname.replace("_", " ");
					buffer.append("<td style=\"text-align:left\">").append(category).append("</td>")
							.append("<td style=\"text-align:left\">").append(feature).append("</td>")
							.append("<td style=\"text-align:left\">").append(finalname).append("</td>")
							.append("<td style=\"text-align:left\">").append(description).append("</td>")
							.append("<td style=\"text-align:center\">").append(formatter.format(startTime.getTime()))
							.append("</td>").append("<td style=\"text-align:center\">")
							.append(formatter.format(endTime.getTime())).append("</td>")
							.append("<td style=\"text-align:center\">")
							.append(convertTimeToString(endTime.getTimeInMillis() - startTime.getTimeInMillis()))
							.append("</td>").append("<td>").append(shortException).append("</td></tr>");
					scenarioIndex++;
					ElasticSearch(finalname,
							convertTimeToString(endTime.getTimeInMillis() - startTime.getTimeInMillis()), category,
							feature, startTime, shortException);
					// Write the remaining scenarios for the method
					for (int i = 1; i < resultsCount; i++) {
						buffer.append("<tr class=\"").append(cssClass).append("\">").append("<td><a href=\"#m")
								.append(scenarioIndex).append("\">")
								/* .append(methodName) */
								.append("</a></td></tr>");
						scenarioIndex++;
					}

					scenariosPerClass += resultsCount;
					methodIndex++;
				}

				// Write the test results for the class
				writer.print("<tr class=\"");
				writer.print(cssClass);
				writer.print("\">");
				writer.print("<td>");
				writer.print(scenarioIndex);
				writer.print("</td>");
				writer.print(buffer);
				classIndex++;
			}
			scenarioCount = scenarioIndex - startingScenarioIndex;
		}
		return scenarioCount;
	}

	/**
	 * Writes the details for all test scenarios.
	 */
	protected void writeScenarioDetails() {
		int scenarioIndex = 0;
		for (SuiteResult suiteResult : suiteResults) {
			for (TestResult testResult : suiteResult.getTestResults()) {
				// Uncomment when need details of test cases ..
				/*
				 * writer.print("<h2>"); writer.print(Utils.escapeHtml(testResult.getname()));
				 * writer.print("</h2>");
				 */

				scenarioIndex += writeScenarioDetails(testResult.getFailedConfigurationResults(), scenarioIndex);
				scenarioIndex += writeScenarioDetails(testResult.getFailedTestResults(), scenarioIndex);
				scenarioIndex += writeScenarioDetails(testResult.getSkippedConfigurationResults(), scenarioIndex);
				scenarioIndex += writeScenarioDetails(testResult.getSkippedTestResults(), scenarioIndex);
				scenarioIndex += writeScenarioDetails(testResult.getPassedTestResults(), scenarioIndex);
			}
		}
	}

	/**
	 * Writes the scenario details for the results of a given state for a single
	 * test.
	 */
	private int writeScenarioDetails(List<ClassResult> classResults, int startingScenarioIndex) {
		int scenarioIndex = startingScenarioIndex;
		for (ClassResult classResult : classResults) {
			String className = classResult.getClassName();
			for (MethodResult methodResult : classResult.getMethodResults()) {
				List<ITestResult> results = methodResult.getResults();
				assert !results.isEmpty();

				String label = Utils
						.escapeHtml(className + "#" + results.iterator().next().getMethod().getMethodName());
				for (ITestResult result : results) {
					// Commenting as no need to have details for each case in email
					// writeScenario(scenarioIndex, label, result);
					scenarioIndex++;
				}
			}
		}

		return scenarioIndex - startingScenarioIndex;
	}

	/**
	 * Writes the details for an individual test scenario.
	 */
	private void writeScenario(int scenarioIndex, String label, ITestResult result) {
		writer.print("<h3 id=\"m");
		writer.print(scenarioIndex);
		writer.print("\">");
		writer.print(label);
		writer.print("</h3>");

		writer.print("<table class=\"table-bordered result\">");

		boolean hasRows = false;

		// Write test parameters (if any)
		Object[] parameters = result.getParameters();
		int parameterCount = (parameters == null ? 0 : parameters.length);
		if (parameterCount > 0) {
			writer.print("<tr class=\"param\">");
			for (int i = 1; i <= parameterCount; i++) {
				writer.print("<th>Parameter #");
				writer.print(i);
				writer.print("</th>");
			}
			writer.print("</tr><tr class=\"param stripe\">");
			for (Object parameter : parameters) {
				writer.print("<td>");
				writer.print(Utils.escapeHtml(Utils.toString(parameter)));
				writer.print("</td>");
			}
			writer.print("</tr>");
			hasRows = true;
		}

		// Write reporter messages (if any)
		List<String> reporterMessages = Reporter.getOutput(result);
		if (!reporterMessages.isEmpty()) {
			writer.print("<tr><th");
			if (parameterCount > 1) {
				writer.printf(" colspan=\"%d\"", parameterCount);
			}
			writer.print(">Messages</th></tr>");

			writer.print("<tr><td");
			if (parameterCount > 1) {
				writer.printf(" colspan=\"%d\"", parameterCount);
			}
			writer.print(">");
			writeReporterMessages(reporterMessages);
			writer.print("</td></tr>");
			hasRows = true;
		}

		// Write exception (if any)
		Throwable throwable = result.getThrowable();
		if (throwable != null) {
			writer.print("<tr><th");
			if (parameterCount > 1) {
				writer.printf(" colspan=\"%d\"", parameterCount);
			}
			writer.print(">");
			writer.print((result.getStatus() == ITestResult.SUCCESS ? "Expected Exception" : "Exception"));
			writer.print("</th></tr>");

			writer.print("<tr><td");
			if (parameterCount > 1) {
				writer.printf(" colspan=\"%d\"", parameterCount);
			}
			writer.print(">");
			writeStackTrace(throwable);
			writer.print("</td></tr>");
			hasRows = true;
		}

		if (!hasRows) {
			writer.print("<tr><th");
			if (parameterCount > 1) {
				writer.printf(" colspan=\"%d\"", parameterCount);
			}
			writer.print(" class=\"invisible\"/></tr>");
		}

		writer.print("</table>");
		writer.println("<p class=\"totop\"><a href=\"#summary\">back to summary</a></p>");
	}

	protected void writeReporterMessages(List<String> reporterMessages) {
		writer.print("<div class=\"messages\">");
		Iterator<String> iterator = reporterMessages.iterator();
		assert iterator.hasNext();
		if (Reporter.getEscapeHtml()) {
			writer.print(Utils.escapeHtml(iterator.next()));
		} else {
			writer.print(iterator.next());
		}
		while (iterator.hasNext()) {
			writer.print("<br/>");
			if (Reporter.getEscapeHtml()) {
				writer.print(Utils.escapeHtml(iterator.next()));
			} else {
				writer.print(iterator.next());
			}
		}
		writer.print("</div>");
	}

	protected void writeStackTrace(Throwable throwable) {
		writer.print("<div class=\"stacktrace\">");
		writer.print(Utils.shortStackTrace(throwable, true));
		writer.print("</div>");
	}

	/**
	 * Writes a TH element with the specified contents and CSS class names.
	 * 
	 * @param html       the HTML contents
	 * @param cssClasses the space-delimited CSS classes or null if there are no
	 *                   classes to apply
	 */
	protected void writeTableHeader(String html, String cssClasses) {
		writeTag("th", html, cssClasses);
	}

	/**
	 * Writes a TD element with the specified contents.
	 * 
	 * @param html the HTML contents
	 */
	protected void writeTableData(String html) {
		writeTableData(html, null);
	}

	/**
	 * Writes a TD element with the specified contents and CSS class names.
	 * 
	 * @param html       the HTML contents
	 * @param cssClasses the space-delimited CSS classes or null if there are no
	 *                   classes to apply
	 */
	protected void writeTableData(String html, String cssClasses) {
		writeTag("td", html, cssClasses);
	}

	/**
	 * Writes an arbitrary HTML element with the specified contents and CSS class
	 * names.
	 * 
	 * @param tag        the tag name
	 * @param html       the HTML contents
	 * @param cssClasses the space-delimited CSS classes or null if there are no
	 *                   classes to apply
	 */
	protected void writeTag(String tag, String html, String cssClasses) {
		writer.print("<");
		writer.print(tag);
		if (cssClasses != null) {
			writer.print(" class=\"");
			writer.print(cssClasses);
			writer.print("\"");
		}
		writer.print(">");
		writer.print(html);
		writer.print("</");
		writer.print(tag);
		writer.print(">");
	}

	/**
	 * Groups {@link TestResult}s by suite.
	 */
	protected static class SuiteResult {
		private final String suiteName;
		private final List<TestResult> testResults = Lists.newArrayList();

		public SuiteResult(ISuite suite) {
			suiteName = suite.getName();
			for (ISuiteResult suiteResult : suite.getResults().values()) {
				testResults.add(new TestResult(suiteResult.getTestContext()));
			}
		}

		public String getSuiteName() {
			return suiteName;
		}

		/**
		 * @return the test results (possibly empty)
		 */
		public List<TestResult> getTestResults() {
			return testResults;
		}
	}

	/**
	 * Groups {@link ClassResult}s by test, type (configuration or test), and
	 * status.
	 */
	protected static class TestResult {
		/**
		 * Orders test results by class name and then by method name (in lexicographic
		 * order).
		 */
		protected static final Comparator<ITestResult> RESULT_COMPARATOR = new Comparator<ITestResult>() {
			@Override
			public int compare(ITestResult o1, ITestResult o2) {
				int result = o1.getTestClass().getName().compareTo(o2.getTestClass().getName());
				if (result == 0) {
					result = o1.getMethod().getMethodName().compareTo(o2.getMethod().getMethodName());
				}
				return result;
			}
		};

		private final String name;
		private final Date testStartTime;
		private final Date testEndTime;
		private final List<ClassResult> failedConfigurationResults;
		private final List<ClassResult> failedTestResults;
		private final List<ClassResult> skippedConfigurationResults;
		private final List<ClassResult> skippedTestResults;
		private final List<ClassResult> passedTestResults;
		private final int failedTestCount;
		private final int skippedTestCount;
		private final int passedTestCount;
		private final int testCount;
		private final long duration;
		private final String includedGroups;
		private final String excludedGroups;
		private final String category;
		private final String subcategory;

		public TestResult(ITestContext context) {
			name = context.getName();

			Set<ITestResult> failedConfigurations = context.getFailedConfigurations().getAllResults();
			Set<ITestResult> failedTests = context.getFailedTests().getAllResults();
			Set<ITestResult> skippedConfigurations = context.getSkippedConfigurations().getAllResults();
			Set<ITestResult> skippedTests = context.getSkippedTests().getAllResults();
			Set<ITestResult> passedTests = context.getPassedTests().getAllResults();

			failedConfigurationResults = groupResults(failedConfigurations);
			failedTestResults = groupResults(failedTests);
			skippedConfigurationResults = groupResults(skippedConfigurations);
			skippedTestResults = groupResults(skippedTests);
			passedTestResults = groupResults(passedTests);

			testStartTime = context.getStartDate();
			testEndTime = context.getEndDate();

			failedTestCount = failedTests.size();
			skippedTestCount = skippedTests.size();
			passedTestCount = passedTests.size();
			testCount = context.getAllTestMethods().length;

			duration = context.getEndDate().getTime() - context.getStartDate().getTime();

			includedGroups = formatGroups(context.getIncludedGroups());
			excludedGroups = formatGroups(context.getExcludedGroups());
			Map<String, String> allParameters = context.getCurrentXmlTest().getAllParameters();
			category = allParameters.get("category");
			subcategory = allParameters.get("subcategory");

		}

		/**
		 * Groups test results by method and then by class.
		 */
		protected List<ClassResult> groupResults(Set<ITestResult> results) {
			List<ClassResult> classResults = Lists.newArrayList();
			if (!results.isEmpty()) {
				List<MethodResult> resultsPerClass = Lists.newArrayList();
				List<ITestResult> resultsPerMethod = Lists.newArrayList();

				List<ITestResult> resultsList = Lists.newArrayList(results);
				Collections.sort(resultsList, RESULT_COMPARATOR);
				Iterator<ITestResult> resultsIterator = resultsList.iterator();
				assert resultsIterator.hasNext();

				ITestResult result = resultsIterator.next();
				resultsPerMethod.add(result);

				String previousClassName = result.getTestClass().getName();
				String previousMethodName = result.getMethod().getMethodName();
				while (resultsIterator.hasNext()) {
					result = resultsIterator.next();

					String className = result.getTestClass().getName();
					if (!previousClassName.equals(className)) {
						// Different class implies different method
						assert !resultsPerMethod.isEmpty();
						resultsPerClass.add(new MethodResult(resultsPerMethod));
						resultsPerMethod = Lists.newArrayList();

						assert !resultsPerClass.isEmpty();
						classResults.add(new ClassResult(previousClassName, resultsPerClass));
						resultsPerClass = Lists.newArrayList();

						previousClassName = className;
						previousMethodName = result.getMethod().getMethodName();
					} else {
						String methodName = result.getMethod().getMethodName();
						if (!previousMethodName.equals(methodName)) {
							assert !resultsPerMethod.isEmpty();
							resultsPerClass.add(new MethodResult(resultsPerMethod));
							resultsPerMethod = Lists.newArrayList();

							previousMethodName = methodName;
						}
					}
					resultsPerMethod.add(result);
				}
				assert !resultsPerMethod.isEmpty();
				resultsPerClass.add(new MethodResult(resultsPerMethod));
				assert !resultsPerClass.isEmpty();
				classResults.add(new ClassResult(previousClassName, resultsPerClass));
			}
			return classResults;
		}

		public String getCategory() {
			return category;
		}

		public String getSubcategory() {
			return subcategory;
		}

		public String getname() {
			return name;
		}

		public Date getTestStartTime() {
			return testStartTime;
		}

		public Date getTestEndTime() {
			return testEndTime;
		}

		/**
		 * @return the results for failed configurations (possibly empty)
		 */
		public List<ClassResult> getFailedConfigurationResults() {
			return failedConfigurationResults;
		}

		/**
		 * @return the results for failed tests (possibly empty)
		 */
		public List<ClassResult> getFailedTestResults() {
			return failedTestResults;
		}

		/**
		 * @return the results for skipped configurations (possibly empty)
		 */
		public List<ClassResult> getSkippedConfigurationResults() {
			return skippedConfigurationResults;
		}

		/**
		 * @return the results for skipped tests (possibly empty)
		 */
		public List<ClassResult> getSkippedTestResults() {
			return skippedTestResults;
		}

		/**
		 * @return the results for passed tests (possibly empty)
		 */
		public List<ClassResult> getPassedTestResults() {
			return passedTestResults;
		}

		public int getFailedTestCount() {
			return failedTestCount;
		}

		public int getSkippedTestCount() {
			return skippedTestCount;
		}

		public int getPassedTestCount() {
			return passedTestCount;
		}

		public long getDuration() {
			return duration;
		}

		public String getIncludedGroups() {
			return includedGroups;
		}

		public String getExcludedGroups() {
			return excludedGroups;
		}

		public int getTestCount() {
			return testCount;
		}

		/**
		 * Formats an array of groups for display.
		 */
		protected String formatGroups(String[] groups) {
			if (groups.length == 0) {
				return "";
			}

			StringBuilder builder = new StringBuilder();
			builder.append(groups[0]);
			for (int i = 1; i < groups.length; i++) {
				builder.append(", ").append(groups[i]);
			}
			return builder.toString();
		}
	}

	/**
	 * Groups {@link MethodResult}s by class.
	 */
	protected static class ClassResult {
		private final String className;
		private final List<MethodResult> methodResults;

		/**
		 * @param className     the class name
		 * @param methodResults the non-null, non-empty {@link MethodResult} list
		 */
		public ClassResult(String className, List<MethodResult> methodResults) {
			this.className = className;
			this.methodResults = methodResults;
		}

		public String getClassName() {
			return className;
		}

		/**
		 * @return the non-null, non-empty {@link MethodResult} list
		 */
		public List<MethodResult> getMethodResults() {
			return methodResults;
		}
	}

	/**
	 * Groups test results by method.
	 */
	protected static class MethodResult {
		private final List<ITestResult> results;

		/**
		 * @param results the non-null, non-empty result list
		 */
		public MethodResult(List<ITestResult> results) {
			this.results = results;
		}

		/**
		 * @return the non-null, non-empty result list
		 */
		public List<ITestResult> getResults() {
			return results;
		}
	}

	/* Convert long type milliseconds to format hh:mm:ss */
	public String convertTimeToString(long miliSeconds) {
		int hrs = (int) TimeUnit.MILLISECONDS.toHours(miliSeconds) % 24;
		int min = (int) TimeUnit.MILLISECONDS.toMinutes(miliSeconds) % 60;
		int sec = (int) TimeUnit.MILLISECONDS.toSeconds(miliSeconds) % 60;
		return String.format("%02d:%02d:%02d", hrs, min, sec);
	}
}