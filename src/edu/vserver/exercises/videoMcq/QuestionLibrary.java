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
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * QuestionLibrary contains all of the questions for the Video stream.
 * 
 * <p><strong class="Red">NOTE:</strong> The library holds no duplicates of the
 * questions (see {@link Question#equals(Object) Question.equals(Object)}).</p>
 * 
 * <p><strong>copyright &copy; Ville-Video 2013</strong></p>
 * 
 * @.classInvariant FORALL({@link Question question} : this;
 *                  !EXIST({@link Question q} : this;
 *                  {@link Question#equals(Object) question.equals(q)}) && (
 *                  {@link #getNext(Question) question.getNextQuestion()} == null ||
 *                   question.getTime() <= {@link #getNext(Question) question.getNextQuestion()}.getTime())
 * 
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @author  Juha Mäkilä
 * @version 1.2
 * @since   1.11.2013
 */
public class QuestionLibrary implements Iterable<Question>, Serializable {

	private static final long serialVersionUID = 4657450323961667603L;

	private final SortedMap<Integer, ArrayList<Question>> questions = Collections.synchronizedSortedMap(new TreeMap<Integer, ArrayList<Question>>());

	/**
	 * Constructs an empty question library.
	 */
	public QuestionLibrary() {
	}

	/**
	 * Constructs a question library containing a single question.
	 * 
	 * @param question new question.
	 */
	public QuestionLibrary(Question question) {
		this.add(question);
	}

	/**
	 * Constructs a question library containing a unique set of {@link Question questions}.
	 * 
	 * <p><strong class="Red">NOTE:</strong> The library holds no duplicates of
	 * the questions (see {@link Question#equals(Object) Question.equals(Object)}).</p>
	 * 
	 * @param questions a collection of {@link Question questions}.
	 */
	public QuestionLibrary(Collection<Question> questions) {
		this.addAll(questions);
	}

	/**
	 * Adds a new {@link Question question} to the library.
	 * 
	 * <p><strong class="Red">NOTE:</strong> The library holds no duplicates of
	 * the questions (see {@link Question#equals(Object) Question.equals(Object)}).</p>
	 * 
	 * @param question new question.
	 * @return true if the {@link Question question} was added to this library.
	 */
	public boolean add(Question question) {
		int time = question.getTime();
		ArrayList<Question> questionList = this.questions.get(time);
		if (questionList == null) {
			questionList = new ArrayList<Question>();
			questionList.add(question);
			this.questions.put(time, questionList);
			return true;
		} else if (!questionList.contains(question)) {
			return questionList.add(question);
		} else {
			return false;
		}
	}

	/**
	 * Adds a collection of new {@link Question questions} to the library.
	 * 
	 * <p><strong class="Red">NOTE:</strong> The library holds no duplicates of
	 * the questions (see {@link Question#equals(Object) Question.equals(Object)}).</p>
	 * 
	 * @param questions new {@link Question questions}.
	 */
	public void addAll(Collection<Question> questions) {
		for (Question question : questions) {
			this.add(question);
		}
	}

	/**
	 * Replaces the old {@link Question question} with the new one.
	 * 
	 * <p></p>
	 * 
	 * @param  oldQ the old question.
	 * @param  newQ the new question.
	 * @return true if {@link Question oldQ} was replaced to {@link Question newQ} in this library.
	 * @throws QuestionNotFoundException if the {@link Question oldQ} was not present in this library.
	 */
	public boolean replace(Question oldQ, Question newQ) {
		ArrayList<Question> questions = getTimeSlot(oldQ.getTime());
		if (contains(oldQ)) {
			if (oldQ.getTime() == newQ.getTime()) {
				int index = questions.indexOf(oldQ);
				questions.set(index, newQ);
			} else {
				questions.remove(oldQ);
				this.add(newQ);
			}
			return true;
		} else {
			throw new QuestionNotFoundException("Cannot replace question that is not present in the library");
		}
	}

	/**
	 * Removes the only occurrence of the specified {@link Question question} from this library.
	 * 
	 * @param question the question to be removed from this library, if present.
	 * @return true if this library contained the specified {@link Question question}.
	 */
	public boolean remove(Question question) {
		int time = question.getTime();
		ArrayList<Question> questionList = this.questions.get(time);
		if (questionList == null) {
			return false;
		}
		boolean result = questionList.remove(question);
		if (questionList.size() == 0) {
			this.questions.remove(time);
		}
		return result;
	}

	/**
	 * Returns the index of the first occurrence of the specified {@link Question question}
	 * in this library, or -1 if this library does not contain the {@link Question question}.
	 * 
	 * @param question question to search for.
	 * @return the index of the first occurrence of the specified {@link Question question}
	 * in this library, or -1 if this library does not contain the {@link Question question}.
	 */
	public int indexOf(Question question) {
		return this.getAllQuestions().indexOf(question);
	}

	/**
	 * Returns the {@link Question question} at the specified position in this library.
	 * 
	 * @param  index index of the {@link Question question} to return.
	 * @return the {@link Question question} at the specified position in this library.
	 * @throws QuestionNotFoundException if the index is out of range (index < 0 || index >= {@link #size()})
	 */
	public Question get(int index) {
		try {
			return this.getAllQuestions().get(index);
		} catch (Exception e) {
			throw new QuestionNotFoundException("No question found: The index '" + index + "' is out of range");
		}
	}

	/**
	 * Returns true if this library contains a {@link Question question} after the given one.
	 * 
	 * @param question question whose successor in this library is to be tested.
	 * @return true if this library contains a successor for the specified question.
	 */
	public boolean containsNext(Question question) {
		ArrayList<Question> allQuestions = this.getAllQuestions();
		int index = allQuestions.indexOf(question);
		return (index >= 0 && allQuestions.size() >= (index + 2));
	}

	/**
	 * Returns the next question after the the given in this library.
	 * 
	 * @param question question whose successor in this library is returned.
	 * @return the {@link Question question} at the specified position in this library or null if none was found.
	 */
	public Question getNext(Question question) {
		ArrayList<Question> allQuestions = this.getAllQuestions();
		int index = allQuestions.indexOf(question);
		if (index < 0) {
			return null;
		} else if (allQuestions.size() >= (index + 2)) {
			return allQuestions.get(index + 1);
		} else {
			return null;
		}
	}

	/**
	 * Returns the next unanswered question after the the given in this library.
	 * 
	 * @.post  RESULT == null | !RESULT.isAnswered()
	 * @param question question whose unanswered successor in this library is returned.
	 * @return the unanswered {@link Question question} at the specified position in this library or null if none was found.
	 */
	public Question getNextUnanswerded(Question question) {
		Question next = this.getNext(question);
		while (next != null && next.isAnswered()) {
			next = this.getNext(next);
		}
		return next;
	}

	/**
	 * Checks if this library contains any unanswered {@link Question questions}.
	 * 
	 * @.post  EXIST(question : this; !{@link Question#isAnswered() question.isAnswered()})
	 * @return true if this library contains any unanswered {@link Question questions}.
	 */
	public boolean containsUnanswerded() {
		ArrayList<Question> allQuestions = this.getAllQuestions();
		for (Question q : allQuestions) {
			if (!q.isAnswered()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the list of the {@link Question questions} at the given time slot
	 * in seconds, or null if none were found.
	 * 
	 * @.post  RESULT == null | RESULT.size() > 0
	 * @param  timeSlot the time of the fetched questions in seconds.
	 * @return the list of all the questions at the given time slot, or null.
	 */
	public ArrayList<Question> getTimeSlot(int timeSlot) {
		return this.questions.get(timeSlot);
	}

	/**
	 * Returns the list of all the unanswered {@link Question questions} at the
	 * given time in seconds, or null if none were found.
	 * 
	 * @.post  RESULT == null | (RESULT.size() > 0 & FORALL(q : RESULT; q.{@link Question#isAnswered() isAnswered()}))
	 * @param  timeSlot index of the {@link Question question} to return.
	 * @return the list of all the unanswered questions at the given time slot, or null.
	 */
	public ArrayList<Question> getUnansweredFromTimeSlot(int timeSlot) {
		if (containsTimeSlot(timeSlot)) {
			ArrayList<Question> theSlot = getTimeSlot(timeSlot);
			ArrayList<Question> unanswered = new ArrayList<Question>();
			for (Question q : theSlot) {
				if (!q.isAnswered()) {
					unanswered.add(q);
				}
			}
			if (unanswered.size() != 0) {
				return unanswered;
			} else {
				return null;
			}
		} else {
			return null;
		}

	}

	/**
	 * Indicates whether there are {@link Question questions} in the given time slot and whether
	 * that time slot contains any {@link Question questions} that has not yet been answered.
	 * 
	 * @param timeSlot the time slot to be checked for a question containing any unanswered questions.
	 * @return true if at least one such a question exists; false otherwise.
	 */
	public boolean containsUnansweredInTimeSlot(int timeSlot) {
		return getUnansweredFromTimeSlot(timeSlot) != null;
	}

	/**
	 * Returns a sorted list of {@link Question question} time slots in this library.
	 * 
	 * @return a sorted list of {@link Question question} time slots in this library.
	 */
	public ArrayList<Integer> getTimeSlots() {
		ArrayList<Integer> timeSlots = new ArrayList<Integer>();
		timeSlots.addAll(this.questions.keySet());
		return timeSlots;
	}

	/**
	 * Checks if this library contains the specified {@link Question question}.
	 * 
	 * @param  question whose presence in this library is to be tested.
	 * @return true if this library contains the specified {@link Question question}.
	 */
	public boolean contains(Question question) {
		ArrayList<Question> allQuestions = this.getAllQuestions();
		return allQuestions.contains(question);
	}

	/**
	 * Checks if this library contains any {@link Question questions} at a specified time in seconds.
	 * 
	 * @param  timeSlot the time in seconds whose presence is to be tested.
	 * @return true if this library contains any {@link Question questions} for the specified time in seconds.
	 */
	public boolean containsTimeSlot(int timeSlot) {
		return questions.containsKey( timeSlot );
	}

	/**
	 * Returns a containing all {@link Question questions} in the library.
	 * 
	 * @return a Collection containing all {@link Question questions} in the library.
	 */
	public ArrayList<Question> getAllQuestions() {
		ArrayList<Question> questions = new ArrayList<Question>();
		for (ArrayList<Question> qlist : this.questions.values()) {
			questions.addAll(qlist);
		}
		return questions;
	}

	/**
	 * Returns the percentage of the correct given answers for all the questions
	 * in this question library.
	 * 
	 * @return the percentage of all the correct given answers.
	 */
	public double getScore() {
		double count = 0;
		for (Question q : getAllQuestions()) {
			count += q.getQuestionScore();
		}
		return count / getAllQuestions().size();
	}

	/**
	 * Resets the given answers for all the questions in this question library.
	 */
	public void resetGivenAnswers() {
		for(Question q : this.getAllQuestions()) {
			q.resetGivenAnswers();
		}
	}

	/**
	 * Returns the number of the {@link Question questions} in this library.
	 * 
	 * @return the number of the {@link Question questions} in this library.
	 */
	public int size() {
		int size = 0;
		for (ArrayList<Question> qlist : this.questions.values()) {
			size += qlist.size();
		}
		return size;
	}

	/**
	 * Returns an iterator over the {@link Question} elements in this library in proper sequence.
	 * 
	 * <p>The returned iterator is fail-fast. So it throws a
	 * {@link java.util.ConcurrentModificationException ConcurrentModificationException}
	 * under either of the following two conditions.</p>
	 * 
	 * <ol>
	 * <li>In multithreaded processing: if one thread is trying to modify a
	 * <em>Collection</em> while another thread is iterating over it.</li>
	 * 
	 * <li>In single-threaded or in multithreaded processing: if after the creation
	 * of the <em>Iterator</em>, the container is modified at any time by any method
	 * other than the <em>Iterator</em>'s own <em>remove</em> or <em>add</em> methods.</em></li>
	 * </ol>
	 * @return an iterator over the {@link Question} elements in this library in proper sequence.
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Question> iterator() {
		return this.getAllQuestions().iterator();
	}

	/**
	 * Returns a string representation of this library.
	 *
	 * @return a string representation of this library.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "QuestionLibrary [questions=" + questions + "]";
	}

	/**
	 * Returns a hash code for this question library.
	 *
	 * @return a hash code value for this object.
	 * @see    java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((questions == null) ? 0 : questions.hashCode());
		return result;
	}

	/**
	 * Compares this question library to the specified object.
	 *
	 * @param  obj the object to compare this against.
	 * @return true if the given object represents a question library equivalent to this, false otherwise
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
		QuestionLibrary other = (QuestionLibrary) obj;
		if (questions == null) {
			if (other.questions != null) {
				return false;
			}
		} else if (!questions.equals(other.questions)) {
			return false;
		}
		return true;
	}

	/**
	 * Thrown to indicate that a {@link Question} was not found from the {@link QuestionLibrary}.
	 *
	 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
	 * @version 0.1
	 * @since   10.11.2013
	 */
	public static class QuestionNotFoundException extends RuntimeException {

		private static final long serialVersionUID = -7586358744666727024L;
		/**
		 * Constructs a new runtime exception with null as its detail message.
		 * The cause is not initialized, and may subsequently be initialized by
		 * a call to {@link Throwable#initCause(Throwable)}.
		 */
		public QuestionNotFoundException() {
			super();
		}
		/**
		 * Constructs a new exception with the specified detail message.
		 * The cause is not initialized, and may subsequently be initialized
		 * by a call to {@link Throwable#initCause(Throwable)}.
		 * 
		 * @param message the detail message. The detail message is saved for
		 *        later retrieval by the {@link Throwable#getMessage()} method.
		 */
		public QuestionNotFoundException(String message) {
			super(message);
		}

	}
}
