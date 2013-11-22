package org.openpos.timerecording.dao;

import java.util.Date;
import java.util.List;

import org.openpos.timerecording.TimeRecordingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DaoManager {

	@Autowired
	private TimeRecordingInsert timeRecordingInsert;
	@Autowired
	private TimeRecordingSelect timeRecordingSelect;
	@Autowired
	private TimeRecordingUpdate timeRecordingUpdate;

	public void insertTimeRecording(TimeRecordingModel model) {
		timeRecordingInsert.insert(model);
	}

	public void updateTimeRecording(TimeRecordingModel model) {
		timeRecordingUpdate.update(model);
	}

	public List<TimeRecordingModel> selectTimeRecordingModel(String empoyeeName, Date fromDate, Date toDate) {
		return timeRecordingSelect.findTimeRecording(empoyeeName, fromDate, toDate);
	}
}
