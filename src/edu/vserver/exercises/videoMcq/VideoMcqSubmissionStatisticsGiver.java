package edu.vserver.exercises.videoMcq;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import edu.vserver.exercises.helpers.StatSubmInfoFilterEditor;
import edu.vserver.exercises.helpers.StatSubmInfoFilterEditor.FilterEditorFactory;
import edu.vserver.exercises.helpers.StatSubmInfoFilterTable;
import edu.vserver.exercises.helpers.StatSubmInfoFilterTable.ShowSubmissionColGenerator;
import edu.vserver.exercises.helpers.StatSubmInfoFilterTable.SubmInfoColumnGenerator;
import edu.vserver.exercises.model.ExerciseException;
import edu.vserver.exercises.model.StatisticalSubmissionInfo;
import edu.vserver.exercises.model.SubmissionStatisticsGiver;
import edu.vserver.standardutils.Localizer;
import edu.vserver.standardutils.TempFilesManager;

/**
 * Statistics giver.
 *
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @author  Juha Mäkilä
 * @version 1.1
 * @since   16.10.2013
 */
public class VideoMcqSubmissionStatisticsGiver extends VerticalLayout implements SubmissionStatisticsGiver<VideoMcqExerciseData, VideoMcqSubmissionInfo> {

	private static final long serialVersionUID = -1410253605264134011L;

	private List<StatisticalSubmissionInfo<VideoMcqSubmissionInfo>> data;
	private VideoMcqExerciseData exer;

	private StatSubmInfoFilterTable<VideoMcqSubmissionInfo> genTable;
	private StatSubmInfoFilterEditor<VideoMcqSubmissionInfo> genTableEditor;

	private Localizer localizer;

	@Override
	public void initialize(
			VideoMcqExerciseData exercise,
			List<StatisticalSubmissionInfo<VideoMcqSubmissionInfo>> dataObjects,
			Localizer localizer, TempFilesManager tempManager)
					throws ExerciseException {
		exer = exercise;
		data = dataObjects;
		this.localizer = localizer;
		initAllSubmissionsTable();

		doLayout();
	}


	private void initAllSubmissionsTable() {

		List<SubmInfoColumnGenerator<?, VideoMcqSubmissionInfo>> extraCols = new ArrayList<SubmInfoColumnGenerator<?, VideoMcqSubmissionInfo>>();

		//extraCols.add(new AnswerColGen());
		// using null for temp-files-manager is safe here as it is known
		// that TemplateSubmissionViewer does not need TempFilesManager;
		// though it would not hurt to pass the real TempFilesManager anyway
		extraCols.add(new ShowSubmissionColGenerator<VideoMcqExerciseData, VideoMcqSubmissionInfo>(
				VideoMcqDescriptor.INSTANCE, exer, null));

		genTable = new StatSubmInfoFilterTable<VideoMcqSubmissionInfo>(
				localizer, data, extraCols);

		List<FilterEditorFactory<VideoMcqSubmissionInfo>> extras = new ArrayList<FilterEditorFactory<VideoMcqSubmissionInfo>>();

		genTableEditor = new StatSubmInfoFilterEditor<VideoMcqSubmissionInfo>(genTable, localizer, extras);

	}

	private void doLayout() {
		this.setWidth("100%");

		VerticalLayout centeredLayout = new VerticalLayout();
		centeredLayout.setWidth("800px");

		Label questionLabel = new Label("Video exercise: " + exer.getExerName());
		questionLabel.addStyleName("template-exercise-title");
		centeredLayout.addComponent(questionLabel);
		centeredLayout.addComponent(genTableEditor.getView());
		centeredLayout.addComponent(genTable.getStatInfoTableView());

		this.addComponent(centeredLayout);
		this.setComponentAlignment(centeredLayout, Alignment.TOP_CENTER);

	}

	@Override
	public Component getView() {
		return this;
	}

	@Override
	public String exportStatisticsDataAsText() {
		// TODO Auto-generated method stub
		return "exportStatisticsDataAsText";
	}

}