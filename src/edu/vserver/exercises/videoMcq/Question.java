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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Question class holds all the information of a single VideoMcq question.
 * 
 * <p><strong>copyright &copy; Ville-Video 2013</strong></p>
 * 
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @author  Juha Mäkilä
 * @version 2.01
 * @since   16.10.2013
 */
public class Question implements Serializable {

	private static final long serialVersionUID = -6091572676536767164L;

	private int time;
	private String question = "";
	private final LinkedHashSet<String> correctAnswers = new LinkedHashSet<String>();
	private final LinkedHashSet<String> incorrectAnswers = new LinkedHashSet<String>();
	private String answerDescription = "";
	private final LinkedHashSet<String> givenAnswers = new LinkedHashSet<String>();

	/**
	 * Constructs an empty question.
	 * 
	 * <p><strong class="red">NOTE:</strong> An empty question contains one correct
	 * and incorrect empty (<em>answer.equals("")</em>) answers.</p>
	 */
	public Question() {
		this.correctAnswers.add("");
		this.incorrectAnswers.add("");
	}

	/**
	 * Constructs a question with time question and answer.
	 * 
	 * @.pre  time >= 0
	 * @param time the time of the question in video stream.
	 * @param question the question text.
	 * @param answer the correct answer text.
	 */
	public Question(int time, String question, String answer) {
		this(time, question, answer, "", new ArrayList<String>());
	}

	/**
	 * Constructs a question with time question and answer.
	 * 
	 * @.pre  time >= 0
	 * @param time the time of the question in video stream.
	 * @param question the question text.
	 * @param correctAnswer the correct answer text.
	 * @param answerDesc
	 * @param incorrectAnswers
	 */
	public Question(int time, String question, String correctAnswer, String answerDesc, Collection<String> incorrectAnswers) {
		this.time = time;
		this.question = question;
		this.correctAnswers.add(correctAnswer);
		this.incorrectAnswers.addAll(incorrectAnswers);
		this.answerDescription = answerDesc;
	}

	/**
	 * 
	 * 
	 * @.pre  time >= 0
	 * @param time the time of the question in video stream.
	 * @param question the question text.
	 * @param correctAnswers a collection of the correct answers.
	 * @param answerDesc
	 * @param incorrectAnswers a collection of the incorrect answers.
	 */
	public Question(int time, String question, Collection<String> correctAnswers, String answerDesc, Collection<String> incorrectAnswers) {
		this.time = time;
		this.question = question;
		this.correctAnswers.addAll(correctAnswers);
		this.incorrectAnswers.addAll(incorrectAnswers);
		this.answerDescription = answerDesc;
	}

	/**
	 * Sets the time of the question in video stream.
	 * 
	 * @.pre  time >= 0
	 * @param time the time of the question in video stream.
	 */
	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * Returns the time of the question in video stream.
	 * 
	 * @return the time of the question in video stream.
	 */
	public int getTime() {
		return this.time;
	}

	/**
	 * Sets the question text.
	 * 
	 * @param question the question text.
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * Returns the question text.
	 * 
	 * @return the question text.
	 */
	public String getQuestion() {
		return this.question;
	}

	/**
	 * Returns the correct answer at the specified position in this Question.
	 * 
	 * @param  index index of the correct answer to return.
	 * @return the correct answer at the specified position in this Question.
	 * @throws java.lang.IndexOutOfBoundsException - if the index is out of range (<em>index < 0 || index >= size()</em>)
	 */
	public String getCorrectAnswer(int index) {
		return this.getCorrectAnswers().get(index);
	}

	/**
	 * Returns the list of the correct answers.
	 * 
	 * @return the list of the correct answers.
	 */
	public ArrayList<String> getCorrectAnswers() {
		return new ArrayList<String>(this.correctAnswers);
	}

	/**
	 * Returns the incorrect answer at the specified position in this Question.
	 * 
	 * @param  index index of the incorrect answer to return.
	 * @return the incorrect answer at the specified position in this Question.
	 * @throws java.lang.IndexOutOfBoundsException - if the index is out of range
	 *         (<em>index < 0 || index >= </em>{@link #getIncorrectAnswers()}.size()).
	 */
	public String getIncorrectAnswer(int index) {
		return this.getIncorrectAnswers().get(index);
	}

	/**
	 * Returns the list of the incorrect answers.
	 * 
	 * @return the list of the incorrect answers.
	 */
	public ArrayList<String> getIncorrectAnswers() {
		return new ArrayList<String>(this.incorrectAnswers);
	}

	/**
	 * Returns the list of the all correct and incorrect answers.
	 * 
	 * @return the list of the all correct and incorrect answers.
	 */
	public ArrayList<String> getAllAnswers() {
		ArrayList<String> allAnswers = new ArrayList<String>(this.incorrectAnswers);
		allAnswers.addAll(this.correctAnswers);
		return allAnswers;
	}

	/**
	 * Sets a single incorrect answer to the question.
	 * 
	 * <p> <strong class="Red">NOTE:</strong> Replaces all old incorrect answers from the question!</p>
	 * 
	 * @param incorrectAnswer the incorrect answer.
	 */
	public void setIncorrectAnswer(String incorrectAnswer) {
		this.correctAnswers.clear();
		this.correctAnswers.add(incorrectAnswer);
	}

	/**
	 * Sets (Replaces) the collection of the incorrect answer choices to the
	 * question.
	 * 
	 * <p><strong class="Red">NOTE:</strong> Replaces all old incorrect answers from the question!</p>
	 * 
	 * @param incorrectAnswers the collection of the wrong answer choices.
	 */
	public void setIncorrectAnswers(Collection<String> incorrectAnswers) {
		this.incorrectAnswers.clear();
		this.incorrectAnswers.addAll(incorrectAnswers);
	}

	/**
	 * Sets a single correct answer to the question.
	 * 
	 * <p>
	 * <strong class="Red">NOTE:</strong> Replaces all old correct answers from
	 * the question!
	 * </p>
	 * 
	 * @param correctAnswer the correct answer.
	 */
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswers.clear();
		this.correctAnswers.add(correctAnswer);
	}

	/**
	 * Sets a collection of the correct answer choices to the question.
	 * 
	 * <p>
	 * <strong class="Red">NOTE:</strong> Replaces all old correct answers from
	 * the question!
	 * </p>
	 * 
	 * @param correctAnswers the collection of the correct answer choices.
	 */
	public void setCorrectAnswers(Collection<String> correctAnswers) {
		this.correctAnswers.clear();
		this.correctAnswers.addAll(correctAnswers);
	}

	/**
	 * Sets the description of the correct answer(s) to the question.
	 * 
	 * @param answerDesc the description of the correct answer(s).
	 */
	public void setAnswerDescription(String answerDesc) {
		this.answerDescription = answerDesc;
	}

	/**
	 * Returns the description of the correct answer(s).
	 * 
	 * @return the description of the correct answer(s).
	 */
	public String getAnswerDescription() {
		return this.answerDescription;
	}

	/**
	 * Indicates whether this question contains a description text.
	 * 
	 * @return true if this question contains a description text.
	 */
	public boolean containsAnswerDescription() {
		return this.answerDescription != null && !this.answerDescription.equals("");
	}

	/**
	 * Returns a list of all the given answers for the question.
	 * 
	 * @return the list of all the given answers.
	 */
	public ArrayList<String> getGivenAnswers() {
		ArrayList<String> answerList = new ArrayList<String>();
		answerList.addAll( givenAnswers );
		return answerList;
	}

	/**
	 * Sets (Replaces) the collection of the given answers to the question.
	 * 
	 * @param answerList the collection of the given answers.
	 */
	public void setGivenAnswers(ArrayList<String> answerList) {
		givenAnswers.clear();
		givenAnswers.addAll(answerList);
	}

	/**
	 * 
	 * @param answer
	 */
	public void setGivenAnswer(String answer) {
		givenAnswers.clear();
		givenAnswers.add(answer);
	}

	/**
	 * Returns the percentage of correct given answers.
	 * 
	 * @return the percentage of correct given answers.
	 */
	public double getScore() {
		double fraction = 0;
		for (String ans: givenAnswers) {
			if (correctAnswers.contains(ans)) {
				fraction++;
			}
		}
		return fraction / correctAnswers.size();
	}
	/**
	 * Indicates whether there are any given answers to the question.
	 * 
	 * @return <em>true</em> if at least one question has been answered; <em>false</em> otherwise.
	 */
	public boolean isAnswered() {
		return givenAnswers.size() > 0;
	}

	/**
	 * Indicates whether the given answer is an correct answer to the question.
	 * 
	 * @return <em>true</em> if the answer is correct; <em>false</em> otherwise.
	 */
	public boolean isCorrectAnswer() {
		return correctAnswers.containsAll(givenAnswers);
	}

	/**
	 * Returns the percentage of the correct given answers.
	 * 
	 * @return the percentage of the correct given answers.
	 */
	public double getQuestionScore() {
		double score = 0;
		for (String s : givenAnswers) {
			if (correctAnswers.contains(s)) {
				score++;
			}
		}
		return score / correctAnswers.size();
	}

	/**
	 * Clears all the given answers for the question.
	 */
	public void resetGivenAnswers() {
		this.givenAnswers.clear();
	}

	/**
	 * Returns a hash code value for the object.
	 *
	 * @return a hash code value for this object.
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((answerDescription == null) ? 0 : answerDescription.hashCode());
		result = prime * result + ((correctAnswers == null) ? 0 : correctAnswers.hashCode());
		result = prime * result + ((incorrectAnswers == null) ? 0 : incorrectAnswers.hashCode());
		result = prime * result + ((question == null) ? 0 : question.hashCode());
		result = prime * result + time;
		return result;
	}

	/**
	 * Compares this question to the specified object.
	 *
	 * @param  obj the object to compare this against.
	 * @return true if the given object represents a question equivalent to this, false otherwise.
	 * @see    java.lang.Object#equals(java.lang.Object)
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
		Question other = (Question) obj;
		if (answerDescription == null) {
			if (other.answerDescription != null) {
				return false;
			}
		} else if (!answerDescription.equals(other.answerDescription)) {
			return false;
		}
		if (correctAnswers == null) {
			if (other.correctAnswers != null) {
				return false;
			}
		} else if (!correctAnswers.equals(other.correctAnswers)) {
			return false;
		}
		if (incorrectAnswers == null) {
			if (other.incorrectAnswers != null) {
				return false;
			}
		} else if (!incorrectAnswers.equals(other.incorrectAnswers)) {
			return false;
		}
		if (question == null) {
			if (other.question != null) {
				return false;
			}
		} else if (!question.equals(other.question)) {
			return false;
		}
		if (time != other.time) {
			return false;
		}
		return true;
	}

	/**
	 * Returns a string representation of this question.
	 *
	 * @return a string representation of this question.
	 * @see    java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Question [time=");
		builder.append(time);
		builder.append("s, question=");
		builder.append(question);
		builder.append(", correctAnswers=");
		builder.append(correctAnswers);
		builder.append(", incorrectAnswers=");
		builder.append(incorrectAnswers);
		builder.append(", answerDescription=");
		builder.append(answerDescription);
		builder.append(", answered=");
		builder.append(givenAnswers);
		builder.append("]");
		return builder.toString();
	}
}