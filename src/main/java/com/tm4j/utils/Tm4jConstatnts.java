package com.tm4j.utils;

public interface Tm4jConstatnts {

	public String baserUrl = "https://api.adaptavist.io/tm4j/v2";
	public String createTestCase = baserUrl + "/testcases";
	public String updateTestCase = baserUrl + "/testcases/%1s";
	public String getAllTestCase = baserUrl + "/testcases?projectKey=%1s&maxResults=4000&startAt=0";
	public String getTestCase = baserUrl + "/testcases/%1s";
	public String createCycle = baserUrl + "/testcycles";
	public String createTestExecution = baserUrl + "/testexecutions";
	public String projectyKey = "TEST";
	public String projectId = "47572";
	// [VJ]This need to go to Vault 
	public String apiToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxYjAxYmZlNS02NTZkLTMwYmItODVmOC0zZmYzMzc1MzRlMWQiLCJjb250ZXh0Ijp7ImJhc2VVcmwiOiJodHRwczpcL1wvbWV6b2NsaXEuYXRsYXNzaWFuLm5ldCIsInVzZXIiOnsiYWNjb3VudElkIjoiNWRjZGNkNDhiYjM1NWIwZGRiZDFjZjY5In19LCJpc3MiOiJjb20ua2Fub2FoLnRlc3QtbWFuYWdlciIsImV4cCI6MTYzOTAzNzc1OSwiaWF0IjoxNjA3NTAxNzU5fQ.7mnAl13aCI37H-R1BR_I9Xn3KSijzgh_6N2Wmfjl6Vo";

}
