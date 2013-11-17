/**
 * <p><b>Copyright 2013 Juha Mäkilä and Sami Holck</b></p>
 * 
 * <p>Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at</p>
 * 
 *     <a href="http://www.apache.org/licenses/LICENSE-2.0"
 *     target="_new">http://www.apache.org/licenses/LICENSE-2.0</a>
 * 
 * <p>Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.</p>
 */
package edu.vserver.exercises.videoMcq;

import java.util.ArrayList;

import sph.vaadin.ui.svg.SvgDots;

/**
 * QuestionDots
 * 
 * <p><strong>copyright &copy; Ville-Video 2013</strong></p>
 *
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @author  Juha Mäkilä
 * @version 0.1
 * @since   11.11.2013
 */
public class QuestionDots extends SvgDots {

	private static final long serialVersionUID = -2952952191258363922L;

	private QuestionLibrary questionLibrary;

	private boolean clickable;
	private boolean informative;

	/**
	 * Constructs an empty object.
	 */
	public QuestionDots() {
		questionLibrary = new QuestionLibrary();
	}

	/**
	 * Construcs an object with QuestionLibrary that contains all the questions, clickable
	 * parameter that indicates whether the dots are clickable, and informative parameter that
	 * indicates whether answering a question gives information about the question.
	 * 
	 * @param questionLibrary contains all the questions.
	 * @param clickable indicates whether the dots are clickable.
	 * @param informative indicates whether answering a question gives any information about the question.
	 */
	public QuestionDots(final QuestionLibrary questionLibrary, boolean clickable, boolean informative) {
		super();
		this.clickable = clickable;
		this.informative = informative;
		this.draw(questionLibrary);
	}

	/**
	 * Draws the {@link sph.vaadin.ui.svg.SvgDots.Dot} elements describing questions
	 * in the given questionLibrary to the SVG canvas.
	 * 
	 * <p><strong class="red">NOTE:</strong> removes all previously defined {@link sph.vaadin.ui.svg.SvgDots.Dot} elements.</p>
	 * 
	 * @param questionLibrary
	 */
	public void draw(final QuestionLibrary questionLibrary) {
		this.questionLibrary = questionLibrary;
		ArrayList<SvgDots.Dot> dotData = new ArrayList<SvgDots.Dot>();
		for (int i = 0; i < this.questionLibrary.size(); i++) {
			dotData.add(this.getQuestionDot(this.questionLibrary.get(i), i));
		}
		super.draw(dotData);
	}

	/**
	 * Private method for manipulating and getting a specified dot.
	 */
	private SvgDots.Dot getQuestionDot(Question question, int index) {
		String title = "Question " + (index + 1);
		String color = "#000";
		if (!question.isAnswered()) {
			color = "Silver";
		} else {
			if (this.informative) {
				if (question.isCorrectAnswer()) {
					color = "Green";
				} else {
					color = "red";
				}
			}
		}
		SvgDots.Dot dot = new SvgDots.Dot(color, title, this.clickable);
		dot.setData(question);
		return dot;
	}

	/**
	 * Returns the questionLibrary that contains all the question objects.
	 *
	 * @return the questionLibrary
	 */
	public QuestionLibrary getQuestionLibrary() {
		return questionLibrary;
	}

	/**
	 * Indicates whether the dots are clickable (that is, they should have
	 * a listener attached to them).
	 *
	 * @return <em>true</em> if the dots are clickable; <em>false</em> otherwise.
	 */
	public boolean isClickable() {
		return clickable;
	}

	/**
	 * Sets whether clicking the dots is possible or not.
	 *
	 * @param clickable the clickable to set.
	 */
	public void setClickable(boolean clickable) {
		this.clickable = clickable;
	}

	/**
	 * Indicates whether answering a question gives any information about the question.
	 *
	 * @return <em>true</em> if clicking the dots should give information
	 * about the questions; <em>false</em> otherwise.
	 */
	public boolean isInformative() {
		return informative;
	}

	/**
	 * Sets whether clicking the dots gives information about the questions.
	 *
	 * @param informative the informative to set.
	 */
	public void setInformative(boolean informative) {
		this.informative = informative;
	}

	/**
	 * {@link sph.vaadin.ui.svg.SvgDots.DotListener} Interface for {@link Question} dot listeners.
	 * 
	 * <p><strong>copyright &copy; Ville-Video 2013</strong></p>
	 *
	 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
	 * @author  Juha Mäkilä
	 * @version 0.1
	 * @since   12.11.2013
	 */
	public static interface QuestionDotListener extends SvgDots.DotListener {

	}
}
