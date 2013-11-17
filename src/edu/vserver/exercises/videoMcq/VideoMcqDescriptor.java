package edu.vserver.exercises.videoMcq;

import com.vaadin.server.Resource;

import edu.vserver.exercises.model.ExerciseTypeDescriptor;
import edu.vserver.exercises.model.SubmissionStatisticsGiver;
import edu.vserver.exercises.model.SubmissionVisualizer;
import edu.vserver.standardutils.Localizer;
import edu.vserver.standardutils.StandardIcon;

public class VideoMcqDescriptor implements ExerciseTypeDescriptor<VideoMcqExerciseData, VideoMcqSubmissionInfo> {

	private static final long serialVersionUID = 5743225101617556960L;

	public static final VideoMcqDescriptor INSTANCE = new VideoMcqDescriptor();

	private VideoMcqDescriptor() {

	}

	@Override
	public VideoMcqXMLHandler newExerciseXML() {
		return VideoMcqXMLHandler.INSTANCE;
	}

	@Override
	public VideoMcqExecutor newExerciseExecutor() {
		return new VideoMcqExecutor();
	}

	@Override
	public VideoMcqEditor newExerciseEditor() {
		return new VideoMcqEditor();
	}

	@Override
	public Class<VideoMcqExerciseData> getTypeDataClass() {
		return VideoMcqExerciseData.class;
	}

	@Override
	public Class<VideoMcqSubmissionInfo> getSubDataClass() {
		return VideoMcqSubmissionInfo.class;
	}

	@Override
	public SubmissionStatisticsGiver<VideoMcqExerciseData, VideoMcqSubmissionInfo> newStatisticsGiver() {
		return new VideoMcqSubmissionStatisticsGiver();
	}

	@Override
	public SubmissionVisualizer<VideoMcqExerciseData, VideoMcqSubmissionInfo> newSubmissionVisualizer() {
		return new VideoMcqSubmissionViewer();
	}

	@Override
	public String getTypeName(Localizer localizer) {
		return "VideoMcq";
	}

	@Override
	public String getTypeDescription(Localizer localizer) {
		return "Template-description";
	}

	@Override
	public Resource getSmallTypeIcon() {

		//return new ThemeResource("exercises/ourproggis/ikoni16.png");
		return StandardIcon.EDIT_ICON_SMALL.getIcon();
	}

	@Override
	public Resource getMediumTypeIcon() {
		return StandardIcon.EDIT_ICON_SMALL.getIcon();
	}

	@Override
	public Resource getLargeTypeIcon() {
		return StandardIcon.EDIT_ICON_SMALL.getIcon();
	}

}
