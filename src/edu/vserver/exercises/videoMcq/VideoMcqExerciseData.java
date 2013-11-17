package edu.vserver.exercises.videoMcq;

import edu.vserver.exercises.model.ExerciseData;

/**
 * VideoMcqExerciseData has all the required data to create a specified Video exercise.
 * 
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @author Juha Mäkilä
 * @version v2.0
 * @since 17.10.2013
 * 
 */
public class VideoMcqExerciseData implements ExerciseData {

	private static final long serialVersionUID = -716445297446246493L;

	private String videoURL = "";
	private String mimeType = "";
	private String exerName = "";
	private QuestionLibrary questionLibrary;
	private boolean sliderEnabled;
	private boolean instantResponse;

	/**
	 * 
	 */
	public VideoMcqExerciseData() {
		this.questionLibrary = new QuestionLibrary();
	}

	/**
	 * 
	 * @param videoURL the URL of the video stream.
	 * @param mimetype the MIME type of the video stream.
	 */
	public VideoMcqExerciseData(String videoURL, String mimetype) {
		this(videoURL, mimetype, "", new QuestionLibrary(), false);
	}

	/**
	 * 
	 * @param videoURL the URL of the video stream.
	 * @param mimeType the MIME type of the video stream.
	 * @param name
	 * @param questions
	 * @param sliderEnabled
	 */
	public VideoMcqExerciseData(String videoURL, String mimeType, String name, QuestionLibrary questions, boolean sliderEnabled) {
		this.videoURL = videoURL;
		this.mimeType = mimeType;
		this.exerName = name;
		this.questionLibrary = questions;
		this.sliderEnabled = sliderEnabled;
	}

	/**
	 * Returns the URL of the video stream.
	 *
	 * @return the URL of the video stream.
	 */
	public final String getVideoURL() {
		return videoURL;
	}

	/**
	 * Sets the URL of the video stream.
	 *
	 * @param videoURL the URL of the video stream to set.
	 */
	public final void setVideoURL(String videoURL) {
		this.videoURL = videoURL;
	}

	/**
	 * Returns the MIME type of the video stream.
	 *
	 * @return the MIME type of the video stream.
	 */
	public final String getMimeType() {
		return mimeType;
	}

	/**
	 * Sets the MIME type of the video stream.
	 *
	 * @param mimeType the MIME type of the video stream to set.
	 */
	public final void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * Returns the name of the exercise.
	 *
	 * @return the name of the exercise.
	 */
	public final String getExerName() {
		return exerName;
	}

	/**
	 * Sets the name of the exercise.
	 *
	 * @param exerName the name of the exercise to set.
	 */
	public final void setExerName(String exerName) {
		this.exerName = exerName;
	}

	/**
	 * Returns the library holding all the questions.
	 *
	 * @return the library holding all the questions.
	 */
	public final QuestionLibrary getQuestionLibrary() {
		return questionLibrary;
	}

	/**
	 * Sets the library holding all the questions.
	 *
	 * @param questionLibrary the library holding all the questions to set.
	 */
	public final void setQuestionLibrary(QuestionLibrary questionLibrary) {
		this.questionLibrary = questionLibrary;
	}

	/**
	 * Indicates whether the slider is enabled in this exercise.
	 *
	 * @return true if the slider is enabled in this exercise; false otherwise.
	 */
	public final boolean isSliderEnabled() {
		return sliderEnabled;
	}

	/**
	 * Indicates whether the slider is enabled in this exercise.
	 *
	 * @param sliderEnabled the sliderEnabled to set
	 */
	public final void setSliderEnabled(boolean sliderEnabled) {
		this.sliderEnabled = sliderEnabled;
	}

	/**
	 * Specifies whether the slider is enabled in this exercise.
	 *
	 * @param sliderEnabled specifies whether the slider is enabled in this exercise.
	 */
	public final void exerData(boolean sliderEnabled) {
		this.sliderEnabled = sliderEnabled;
	}

	/**
	 * Indicates whether the instant response is enabled in this exercise.
	 *
	 * @return true if the instant response is enabled in this exercise; false otherwise.
	 */
	public final boolean isInstantResponse() {
		return instantResponse;
	}

	/**
	 * Specifies whether the instant response is enabled in this exercise.
	 *
	 * @param instantResponse the instant response is enabled (true).
	 */
	public final void setInstantResponse(boolean instantResponse) {
		this.instantResponse = instantResponse;
	}

	/**
	 *
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((exerName == null) ? 0 : exerName.hashCode());
		result = prime * result + (instantResponse ? 1231 : 1237);
		result = prime * result + ((mimeType == null) ? 0 : mimeType.hashCode());
		result = prime * result + ((questionLibrary == null) ? 0 : questionLibrary.hashCode());
		result = prime * result + (sliderEnabled ? 1231 : 1237);
		result = prime * result + ((videoURL == null) ? 0 : videoURL.hashCode());
		return result;
	}

	/**
	 *
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		VideoMcqExerciseData other = (VideoMcqExerciseData) obj;
		if (exerName == null) {
			if (other.exerName != null) {
				return false;
			}
		} else if (!exerName.equals(other.exerName)) {
			return false;
		}
		if (instantResponse != other.instantResponse) {
			return false;
		}
		if (mimeType == null) {
			if (other.mimeType != null) {
				return false;
			}
		} else if (!mimeType.equals(other.mimeType)) {
			return false;
		}
		if (questionLibrary == null) {
			if (other.questionLibrary != null) {
				return false;
			}
		} else if (!questionLibrary.equals(other.questionLibrary)) {
			return false;
		}
		if (sliderEnabled != other.sliderEnabled) {
			return false;
		}
		if (videoURL == null) {
			if (other.videoURL != null) {
				return false;
			}
		} else if (!videoURL.equals(other.videoURL)) {
			return false;
		}
		return true;
	}

}