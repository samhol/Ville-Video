package edu.vserver.exercises.videoMcq;

import java.util.Collections;

import sph.Time;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import edu.vserver.exercises.model.ExerciseException;
import edu.vserver.exercises.model.SubmissionVisualizer;
import edu.vserver.standardutils.Localizer;
import edu.vserver.standardutils.TempFilesManager;
/**
 * VideoMcqSubmissionViewer
 *
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @author  Juha Mäkilä
 * @version 1.0
 * @since   16.10.2013
 */
public class VideoMcqSubmissionViewer extends VerticalLayout implements SubmissionVisualizer<VideoMcqExerciseData, VideoMcqSubmissionInfo> {

	private static final long serialVersionUID = -6260031633710031462L;
	private VideoMcqExerciseData exer;
	private VideoMcqSubmissionInfo submInfo;

	/**
	 * 
	 */
	public VideoMcqSubmissionViewer() {
		super();
	}

	@Override
	public void initialize( VideoMcqExerciseData exercise, VideoMcqSubmissionInfo dataObject, Localizer localizer,
			TempFilesManager tempManager ) throws ExerciseException {
		this.exer = exercise;
		this.submInfo = dataObject;
		doLayout();
	}

	private void doLayout() {
		this.addComponents( buildGlobalExerInfo() );
		int index = 1;
		for (Question q : submInfo.getQuestionLibrary()) {
			this.addComponents(buildQuestionInfo(q, index));
			index++;
		}
	}

	/**
	 * Builds a new global Exercise info label.
	 * 
	 * @return global Exercise info label.
	 */
	private Label buildGlobalExerInfo() {
		String html = "<h1>Exercise name: <span style=\"text-decoration: underline;\">" + exer.getExerName() + "</span></h1>";
		String url = exer.getVideoURL();
		html += "<ul><li><strong>video URL:</strong> <em><a href=\"" + url + "\">" + url + "</a></em></li>";
		html += "<li><strong>MIME type:</strong> <em>" + exer.getMimeType() + "</em></li>";
		html += "<li><strong>Score:</strong> <em>" + Math.round(submInfo.getQuestionLibrary().getScore() * 100) + "% correct</em></li></ul>";
		return new Label(html, ContentMode.HTML);
	}

	/**
	 * Builds a new question info label.
	 * 
	 * @param q the question.
	 * @param index the index of the question in the library.
	 * @return question info label.
	 */
	private Label buildQuestionInfo(Question q, int index) {
		String html = "<h3>Question " + (index) + ":</h3>";
		html += "<ul><li><strong>time:</strong> <em>" + Time.convertToTimeString(q.getTime()) + "</em></li>";
		html += "<li><strong>question:</strong> <em>" + q.getQuestion() + "</em></li>";
		Collections.sort(q.getCorrectAnswers());
		html += "<li><strong>correct answer(s):</strong> <em>" + q.getCorrectAnswers() + "</em></li>";
		Collections.sort(q.getGivenAnswers());
		if (q.getGivenAnswers().size() > 0) {
			html += "<li><strong>given answer(s):</strong> <em>" + q.getGivenAnswers() + "</em></li>";
		} else {
			html += "<li><strong>given answer(s):</strong> <em style=\"color: #f00; text-decoration: underline;\">No answer was given!</em></li>";
		}
		html += "<li><strong>score:</strong> <em>" + Math.round(q.getQuestionScore() * 100) + "% correct</em></li></ul>";
		return new Label(html, ContentMode.HTML);
	}



	@Override
	public Component getView() {
		return this;
	}

	@Override
	public String exportSubmissionDataAsText() {
		String text = "Exercise Name: " + exer.getExerName() +
				"\n\tvideo URL:\t" + exer.getVideoURL() +
				"\n\tMIME type:\t" + exer.getMimeType();
		int index = 1;
		for (Question q : submInfo.getQuestionLibrary()) {
			text += "\n\tQuestion " + index +
					":\n\t\ttime:\t\t\t\t" + Time.convertToTimeString(q.getTime()) +
					"\n\t\tcorrect answer(s):\t" + q.getCorrectAnswers() +
					"\n\t\tgiven answer(s):\t" + q.getGivenAnswers() +
					"\n\t\tscore:\t\t\t\t" + Math.round(q.getQuestionScore() * 100) + "% correct";
			index++;
		}
		return text;
	}

}
