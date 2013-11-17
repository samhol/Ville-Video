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
import java.util.Collection;

import sph.event.BasicEventManager;
import sph.event.EventListener;
import sph.vaadin.ui.ComponentFactory;
import sph.vaadin.ui.SPH_Theme;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * QuestionWindow
 * 
 * <p><strong>copyright &copy; Ville-Video 2013</strong></p>
 *
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @author  Juha Mäkilä
 * @version 1.01
 * @since   3.11.2013
 */
public class QuestionWindow extends Window {

	private static final long serialVersionUID = 9013195449997622239L;

	/**
	 * a QuestionWindow EventManager
	 */
	private final BasicEventManager<String, QuestionWindow, Integer, QuestionEventListener> evtMngr = new BasicEventManager<String, QuestionWindow, Integer, QuestionEventListener>();

	private QuestionLibrary questionLibrary;
	private Question currentQuestion;
	private int currentTimeSlot;

	private final Label qLabel = new Label("", ContentMode.HTML);
	private final OptionGroup optionGroup = new OptionGroup();
	private final Button submitButton;
	private final Button nextButton;
	private boolean isVisible;
	private boolean informative;
	private boolean skippingAllowed;

	/**
	 * Constructs a question window object for showing the question with the
	 * answer choices and submit- and next-button.
	 */
	public QuestionWindow() {
		super();
		setModal(true);
		setResizable(false);
		setClosable(false);
		addStyleName("questionWindow");
		optionGroup.setImmediate(true);
		optionGroup.addStyleName("answerOptions");
		HorizontalLayout dots = new HorizontalLayout();

		VerticalLayout v = new VerticalLayout();
		v.setSpacing(true);
		v.setMargin(true);

		HorizontalLayout btnFooter = new HorizontalLayout();
		btnFooter.addStyleName("buttons");
		btnFooter.setMargin(true);
		btnFooter.setSpacing(true);
		btnFooter.setWidth(100, Unit.PERCENTAGE);
		btnFooter.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		submitButton = ComponentFactory.createLargeBlueButton("Submit", true, null);
		submitButton.setEnabled(false);
		nextButton = ComponentFactory.createLargeBlueButton("Next", true, SPH_Theme.NEXT_ICON_16PX);
		btnFooter.addComponents(submitButton, nextButton);

		v.addComponents(qLabel, optionGroup, btnFooter, dots);
		this.setContent(v);
		this.createButtonListeners();
	}

	/**
	 * Indicates whether answering the question should give any information about the question.
	 *
	 * @return true if answering the question gives any information about the question.
	 */
	public boolean isInformative() {
		return informative;
	}

	/**
	 * Sets whether answering the question should give any information about the question.
	 * 
	 * @param informative whether answering the question should give any information about the question.
	 */
	public void setInformative(boolean informative) {
		this.informative = informative;
	}

	/**
	 * Indicates whether jumping back and forth between questions is allowed.
	 *
	 * @return true if jumping back and forth between questions is allowed.
	 */
	public boolean isSkippingAllowed() {
		return skippingAllowed;
	}

	/**
	 * Sets whether jumping back and forth between questions is allowed.
	 *
	 * @param allowSkipping whether jumping back and forth between questions is allowed.
	 */
	public void setAllowSkipping(boolean allowSkipping) {
		this.skippingAllowed = allowSkipping;
	}

	/**
	 * Adds a new {@link QuestionEventListener} listener into the {@link QuestionWindow} component.
	 * 
	 * @param eventName The {@link QuestionEventListener} event the listener will listen to.
	 * @param listener The event listener object itself.
	 */
	public void addQuestionEventListener(String eventName, QuestionEventListener listener) {
		this.evtMngr.addListener(eventName, listener);
	}

	/**
	 * Remove listener from a specific {@link QuestionEventListener} event.
	 * 
	 * @param eventName the name of the {@link QuestionEventListener} event.
	 * @param listener {@link QuestionEventListener} Event listener to remove.
	 */
	public void removeQuestionEventListener(String eventName, QuestionEventListener listener) {
		this.evtMngr.removeListener(eventName, listener);
	}

	/**
	 * Remove listener from all {@link QuestionEventListener} events it is registered
	 * by. Convenient way of cleaning up an listener object being destroyed.
	 * 
	 * @param listener {@link QuestionEventListener} Event listener to remove.
	 */
	public void removeQuestionEventListener(QuestionEventListener listener) {
		this.evtMngr.removeListener(listener);
	}

	/**
	 * Returns the question library.
	 *
	 * @return the question library.
	 */
	public QuestionLibrary getQuestionLibrary() {
		return questionLibrary;
	}

	/**
	 * Sets the question library.
	 *
	 * @param questionLibrary the question library to set.
	 */
	public void setQuestionLibrary(QuestionLibrary questionLibrary) {
		this.questionLibrary = questionLibrary;
	}

	/**
	 * Asks all of the existing questions in the given time slot.
	 * 
	 * @param timeSlot the time slot to ask.
	 * @return true if there are any unanswered questions in this time slot.
	 */
	public boolean ask(int timeSlot) {
		ArrayList<Question> unanswered = questionLibrary.getUnansweredFromTimeSlot(timeSlot);
		if (unanswered != null && unanswered.size() > 0) {
			currentTimeSlot = timeSlot;
			currentQuestion = unanswered.get(0);
			setQuestionValues(currentQuestion);
			show();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param question
	 */
	private void setQuestionValues(Question question) {
		this.setCaption("QUESTION " + (questionLibrary.indexOf(question) + 1));
		qLabel.addStyleName("question");
		qLabel.setValue(question.getQuestion());
		optionGroup.removeAllItems();
		ArrayList<String>answers = question.getIncorrectAnswers();
		answers.addAll(question.getCorrectAnswers());
		java.util.Collections.shuffle(answers);
		for (String answer : answers) {
			optionGroup.addItem(answer);
		}
		optionGroup.setEnabled(true);
		if (!this.isSkippingAllowed()) {
			this.nextButton.setEnabled(false);
		}
	}

	/**
	 * Sets the question values for the question given.
	 * 
	 * @param question question to be asked.
	 */
	private void askQuestion(Question question) {
		setQuestionValues(question);
	}

	/**
	 * Creates all button listeners.
	 */
	private void createButtonListeners() {
		submitButton.addClickListener( new Button.ClickListener() {

			private static final long serialVersionUID = 1073401637447482407L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (optionGroup.getValue() != null) {
					currentQuestion.setGivenAnswers(getGivenAnswer());
				}
				submitButton.setEnabled(false);
				optionGroup.setEnabled(false);
				informUser(currentQuestion);
				nextButton.setEnabled(true);
				evtMngr.callListeners(QuestionEventListener.QUESTION_FINISHED_EVENT, QuestionWindow.this, currentTimeSlot);
			}
		});
		nextButton.addClickListener( new Button.ClickListener() {

			private static final long serialVersionUID = 4888684514443016037L;

			@Override
			public void buttonClick(ClickEvent event) {
				currentQuestion = questionLibrary.getNextUnanswerded(currentQuestion);
				if (currentQuestion != null && currentQuestion.getTime() == currentTimeSlot) {
					askQuestion(currentQuestion);
					//				nextButton.setEnabled(false);
				} else {
					evtMngr.callListeners(QuestionEventListener.TIMESLOT_FINISHED_EVENT, QuestionWindow.this, currentTimeSlot);
					hide();
				}
				System.out.println(questionLibrary.get(0));
				submitButton.setEnabled(false);
			}
		});
		optionGroup.addValueChangeListener(new Property.ValueChangeListener() {

			private static final long serialVersionUID = 6312324411980899703L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (optionGroup.getValue() != null) {
					submitButton.setEnabled(true);
				}
			}
		});
	}

	/**
	 * Returns a list of the given answer(s) for the question.
	 * 
	 * @return a list of given answer choices.
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<String> getGivenAnswer() {
		ArrayList<String> answers = new ArrayList<String>();
		if (this.optionGroup.isMultiSelect()) {
			//System.out.println(optionGroup.getValue());
			answers.addAll((Collection<String>) optionGroup.getValue());
		} else {
			//System.out.println(optionGroup.getValue());
			answers.add((String)optionGroup.getValue());

		}
		return answers;
	}

	/**
	 * Sets a Notification about the correctness of the given answer to the browser window.
	 * 
	 * @param question latest answered question.
	 */
	private void informUser(Question question) {
		if (this.isInformative()) {
			Notification n;
			if (question.isCorrectAnswer()) {
				n = new Notification("CORRECT", Notification.Type.HUMANIZED_MESSAGE);
				n.setIcon(new ThemeResource(SPH_Theme.CORRECT_ICON_48PX));
				n.setStyleName("correctAnswer");
			} else {
				n = new Notification("INCORRECT", Notification.Type.HUMANIZED_MESSAGE);
				n.setIcon(new ThemeResource(SPH_Theme.INCORRECT_ICON_48PX));
				n.setStyleName("incorrectAnswer");
			}
			if (question.containsAnswerDescription()) {
				n.setDescription(question.getAnswerDescription());
				n.setDelayMsec(Notification.DELAY_FOREVER);
			} else {
				n.setDelayMsec(500);
			}
			n.setPosition(com.vaadin.shared.Position.MIDDLE_CENTER);
			n.show(Page.getCurrent());
		}

	}

	/**
	 * Shows the dialog window in the <em>UI</em>.
	 */
	public void show() {
		if (!isVisible) {
			UI ui = UI.getCurrent();
			ui.addWindow(this);
			isVisible = true;
		}
	}

	/**
	 * Hides the dialog window from the <em>UI</em>.
	 */
	public void hide() {
		if (isVisible) {
			UI ui = UI.getCurrent();
			ui.removeWindow(this);
			isVisible = false;
		}
	}

	/**
	 * {@link EventListener} Interface for listening to question related events
	 *  fired by a {@link QuestionWindow} component.
	 * 
	 * <p><strong>copyright &copy; Ville-Video 2013</strong></p>
	 *
	 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
	 * @author  Juha Mäkilä
	 * @version 1.0
	 * @since   10.11.2013
	 */
	public interface QuestionEventListener extends EventListener<String, QuestionWindow, Integer> {

		/**
		 * Fired when the {@link QuestionWindow} has asked all of the {@link Question questions} in the time slot.
		 */
		public static final String TIMESLOT_FINISHED_EVENT = "TIMESLOT_FINISHED_EVENT";

		/**
		 * Fired every time when the {@link QuestionWindow} has asked one {@link Question question}.
		 */
		public static final String QUESTION_FINISHED_EVENT = "QUESTION_FINISHED_EVENT";

		/**
		 * {@link QuestionWindow} Component has triggered an event and corresponding listener method is executed.
		 * 
		 * @param eventName name of the {@link QuestionEventListener} event.
		 * @param source the source component of the {@link QuestionEventListener VideojsListener} event.
		 * @param timeSlot the {@link Question question} time slot handled by the {@link QuestionWindow}.
		 */
		@Override
		public void on(String eventName, QuestionWindow source, Integer timeSlot);

	}
}