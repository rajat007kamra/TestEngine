package core.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Set;

/**
 * @summary Class responsible for parsing logfile and extracting response time for respective actions
 * @author Surendra.Shekhawat
 */
public class LogParser {
	private String logfile;
	public Map<String, String> resultMap;
	private String respTimeRegx = "[RESPTIME]";
	private String infoRegx = "INFO";

	/**
	 * @summary Constructor which initializes log file to be parsed
	 * @param logfile
	 */
	public LogParser(String logfile) {
		this.logfile = logfile;
		resultMap = new HashMap<String, String>();
	}

	/**
	 * @summary Reads log file and returns map for action and response time as KV pair
	 * @return
	 */
	public Map<String, String> readLogFile() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(this.logfile));
			String line = reader.readLine();
			while (line != null) {
				line = reader.readLine();
				if (line != null && line.contains(respTimeRegx)) {
					String[] infos = line.split(infoRegx);
					String[] responseTime = infos[1].trim().split(" ");
					resultMap.put(responseTime[0].trim()+Math.random(), responseTime[3].trim());
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resultMap;
	}
	
	/**
	 * @summary Reads log file and returns map for action and response time as KV pair along-with timestamp of action
	 * @return
	 */
	public Map<String, String> readLogFileWithTimeStamp() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(this.logfile));
			String line = reader.readLine();
			while (line != null) {
				line = reader.readLine();
				if (line != null && line.contains(respTimeRegx)) {
					String[] infos = line.split(infoRegx);
					String[] responseTime = infos[1].trim().split(" ");
					String respTime = responseTime[0].trim().split(":")[0];				
					resultMap.put(infos[0].trim()+"##"+respTime+"##"+Math.random(), responseTime[3].trim());
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resultMap;
	}

	/**
	 * @summary Fetches all action names out of map
	 * @param resultMap
	 * @return
	 */
	private Set<String> getAllActions(Map<String, String> resultMap) {
		Set<String> allActions = new HashSet<String>();
		Set<String> keySet = resultMap.keySet();
		for (String key : keySet) {
			String actionname = key.split(":")[0].trim();
			allActions.add(actionname);
		}
		return allActions;
	}

	/**
	 * @summary Fetches all times taken by action out of map
	 * @param resultMap
	 * @return
	 */
	private List<Long> getActionsTimes(String actionName) {
		List<Long> actionTimes = new ArrayList<Long>();
		Set<String> keySet = resultMap.keySet();
		for (String key : keySet) {
			if (key.startsWith(actionName)) {
				actionTimes.add(Long.parseLong(resultMap.get(key)));
			}
		}
		return actionTimes;
	}

	/**
	 * @summary Computes percentile for specified samples provided as list
	 * @param latencies
	 * @param percentile
	 * @return
	 */
	private long percentile(List<Long> latencies, double percentile) {
		Collections.sort(latencies);
		int index = (int) Math.ceil(percentile / 100.0 * latencies.size());
		return latencies.get(index - 1);
	}

	/**
	 * @summary Returns details of action (name, samples, min, max and mean) related for the percentile provided
	 * @param resultMap
	 * @param percentile
	 * @return
	 */
	public Map<String, Long> actionResultList(Map<String, String> resultMap, double percentile) {
		Map<String, Long> actionResult = new HashMap<String, Long>();
		Set<String> allActions = getAllActions(resultMap);
		for (String action : allActions) {
			
			List<Long> actionsTimes = getActionsTimes(action);
			Collections.sort(actionsTimes);
			long min = actionsTimes.get(0);
			long max = actionsTimes.get(actionsTimes.size()-1);
			OptionalDouble mean = actionsTimes
		            .stream()
		            .mapToDouble(a -> a)
		            .average();

			long percent = percentile(actionsTimes, percentile);
			actionResult.put(action+"###"+actionsTimes.size()+"###"+min+"###"+max+"###"+Math.round(mean.getAsDouble()), percent);
		}
		return actionResult;
	}

	/**
	 * @summary Writes results in a file which will be consumed by emailable report 
	 * @param outputFilePath
	 * @param results
	 */
	public void writeResults(String outputFilePath, Map<String, Long> results) {
		File file = new File(outputFilePath);
		BufferedWriter bf = null;
		try {
			bf = new BufferedWriter(new FileWriter(file));
			for (Map.Entry<String, Long> entry : results.entrySet()) {
				bf.write(entry.getKey() + ":" + entry.getValue());
				bf.newLine();
			}
			bf.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bf.close();
			} catch (Exception e) {
			}
		}
	}
}
