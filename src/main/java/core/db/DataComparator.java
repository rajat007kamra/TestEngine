package core.db;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import core.cliqdb.model.StepData;

public class DataComparator 
{
	public static void sortSteps(List<StepData> steps)
	{
		Collections.sort((List<StepData>) steps, new Comparator<StepData>()
		{
			@Override
			public int compare(StepData o1, StepData o2)
			{
				return (o1.getStepSequenceNumber().compareTo(o2.getStepSequenceNumber()));
			}
		});
	}
}
