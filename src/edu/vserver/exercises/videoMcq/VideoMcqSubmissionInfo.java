package edu.vserver.exercises.videoMcq;

import edu.vserver.exercises.model.SubmissionInfo;

/**
 * Submission info
 * 
 * @author  Juha Mäkilä
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @version 1.01
 * @since   16.10.2013
 */
public class VideoMcqSubmissionInfo implements SubmissionInfo {

	private static final long serialVersionUID = 8702870727095225372L;

	private final QuestionLibrary questionLibrary;

	/**
	 * Constructs an empty submission info.
	 */
	public VideoMcqSubmissionInfo() {
		this.questionLibrary = new QuestionLibrary();
	}
	/**
	 * Constructs a submission info containing a question library.
	 * 
	 * @param questioLibrary the question library of this submission info.
	 */
	public VideoMcqSubmissionInfo(QuestionLibrary questioLibrary) {
		this.questionLibrary = questioLibrary;
	}

	/**
	 * Return the question library of this submission info.
	 * 
	 * @return the question library of this submission info.
	 */
	public QuestionLibrary getQuestionLibrary() {
		return this.questionLibrary;
	}

	/**
	 * Returns the received points of the submission.
	 * 
	 * @return the received points of the submission.
	 */
	public String getPoints() {
		return "" + this.questionLibrary.getScore();
	}
}
