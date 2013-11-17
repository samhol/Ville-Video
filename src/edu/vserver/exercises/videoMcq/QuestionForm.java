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

import sph.Time;
import sph.event.BasicEventManager;
import sph.event.EventListener;
import sph.vaadin.TextFieldValidator;
import sph.vaadin.TextSelector;
import sph.vaadin.ui.ComponentFactory;
import sph.vaadin.ui.SPH_Theme;
import sph.vaadin.ui.videojs.Videojs;

import com.vaadin.data.validator.AbstractStringValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

/**
 * Contains all UI fields and controls for {@link Question question} edition and creation.
 * 
 * <p><strong>copyright &copy; Ville-Video 2013</strong></p>
 * 
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @author  Juha Mäkilä
 * @version 1.1
 * @since   1.11.2013
 */
public class QuestionForm extends CustomComponent {

	private static final long serialVersionUID = 4425084946164635910L;

	private final Videojs vjs;

	private final Button newQuestionBtn = ComponentFactory.createSmallBlueButton("New", true, SPH_Theme.ADD_ICON_16PX);
	private final Button cancelButton = ComponentFactory.createSmallBlueButton("Cancel", false, SPH_Theme.CANCEL_ICON_16PX);
	private final Button saveButton = ComponentFactory.createSmallBlueButton("Save", false, SPH_Theme.SAVE_ICON_16PX);
	private final Button addIncorrectAnswer = ComponentFactory.createSmallBlueButton("Add", false, SPH_Theme.ADD_ICON_16PX);
	private final Button clearIncorrectAnswers = ComponentFactory.createSmallBlueButton("Clear", false, SPH_Theme.CLEAR_ICON_16PX);
	private final HorizontalLayout inputLayout = new HorizontalLayout();
	private final TextArea questionField = new TextArea("Question");
	private final TextField answerField = new TextField("Answer");
	private final TextArea answerDescriptionField = new TextArea("Description of the answer");
	private final VerticalLayout wrongAnswersLayout = new VerticalLayout();
	private final IncorrectAnswerFieldsHandler wrongAnswerHandler = new IncorrectAnswerFieldsHandler();
	private final HorizontalLayout timeLayout = new HorizontalLayout();
	private final Label timeLabel = new Label(Time.convertToTimeString(0), ContentMode.HTML);
	private final Label durationLabel = new Label("??:??:??", ContentMode.HTML);
	private int operationType;
	private Question originalQuestion;
	private static final int EDIT_QUESTION = 1;
	private static final int NEW_QUESTION = 2;

	/**
	 * a QuestionForm EventManager
	 */
	private final BasicEventManager<String, QuestionForm, Question, QuestionFormListener> eventManager = new BasicEventManager<String, QuestionForm, Question, QuestionFormListener>();

	/**
	 * Constructs a new empty {@link QuestionForm}.
	 * 
	 * @param  vjs the video player component that handles the videostream playback.
	 * @throws IllegalArgumentException if {@link sph.vaadin.ui.videojs.Videojs vjs} argument is null.
	 */
	public QuestionForm(Videojs vjs) {
		this(vjs, new Question());
	}

	/**
	 * Constructs a new {@link QuestionForm} with a question.
	 * 
	 * <p><strong class="Red">IMPORTANT:</strong> This must be called on creation!<P>
	 * 
	 * @param vjs the video player component that handles the videostream playback.
	 * @param question the question
	 * @throws IllegalArgumentException if {@link sph.vaadin.ui.videojs.Videojs vjs} argument is null.
	 */
	private QuestionForm(final Videojs vjs, Question question) {
		if (vjs != null) {
			this.vjs = vjs;
		} else {
			throw new IllegalArgumentException("Vidojs cannot be null");
		}
		if (question == null) {
			question = new Question();
		}
		this.setListeners();
		this.wrongAnswerHandler.setAnswers(question.getIncorrectAnswers());
		this.doLayout();
	}

	/**
	 * Builds UI components and the layout for them.
	 */
	private void doLayout() {

		VerticalLayout questionUI = new VerticalLayout();

		HorizontalLayout mainButtons = new HorizontalLayout();
		//mainButtons.setMargin(true);
		mainButtons.setSpacing(true);
		this.newQuestionBtn.setWidth(100, Unit.PIXELS);
		this.cancelButton.setWidth(100, Unit.PIXELS);
		this.saveButton.setWidth(100, Unit.PIXELS);
		mainButtons.addComponents(this.newQuestionBtn, this.cancelButton, this.saveButton);
		questionUI.addComponent(mainButtons);
		this.initTextFields();
		this.inputLayout.setSpacing(true);
		this.inputLayout.setDefaultComponentAlignment(Alignment.TOP_LEFT);
		VerticalLayout questionLayout = new VerticalLayout();

		this.timeLayout.setMargin(new MarginInfo(true, false, true, false));
		this.timeLayout.addComponents(
				new Label("<strong>Time:</strong>", ContentMode.HTML),
				this.timeLabel,
				new Label("<strong> / </strong>", ContentMode.HTML),
				this.durationLabel);
		this.timeLayout.setSpacing(true);
		questionLayout.addComponents(this.timeLayout, this.questionField);

		VerticalLayout correctAnswers = new VerticalLayout();
		correctAnswers.addComponents(this.answerField, this.answerDescriptionField);

		VerticalLayout wrongAnswers = new VerticalLayout();
		wrongAnswers.setCaption("Incorrect answer choices");
		HorizontalLayout wrongAnswerButtons = new HorizontalLayout();
		wrongAnswerButtons.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		wrongAnswerButtons.setSpacing(true);
		wrongAnswerButtons.setMargin(true);
		wrongAnswerButtons.addComponents(this.addIncorrectAnswer, this.clearIncorrectAnswers);

		Panel wrongAswersScroller = new Panel();
		wrongAswersScroller.setSizeUndefined();
		wrongAswersScroller.setHeight(100, Unit.PIXELS);
		wrongAswersScroller.setWidth(215, Unit.PIXELS);
		wrongAswersScroller.addStyleName("borderless");

		wrongAswersScroller.setContent(this.wrongAnswersLayout);
		wrongAnswers.addComponents(wrongAnswerButtons, wrongAswersScroller);

		this.inputLayout.addComponents(questionLayout, correctAnswers, wrongAnswers);
		questionUI.addComponent(this.inputLayout);
		this.setCompositionRoot(questionUI);
	}

	/**
	 * Sets the listeners to the buttons.
	 */
	private void setListeners() {
		this.vjs.addVideojsListener(Videojs.VjsListener.ANY_EVENT, new Videojs.VjsListener() {

			private static final long serialVersionUID = 8427274367098667303L;

			@Override
			public void on(String eventName, Videojs source, Double triggerTime) {
				timeLabel.setValue(Time.convertToTimeString(triggerTime));
			}
		});
		this.vjs.addVideojsListener(Videojs.VjsListener.DURATIONCHANGE_EVENT, new Videojs.VjsListener() {

			private static final long serialVersionUID = -7568539997745781255L;

			@Override
			public void on(String eventName, Videojs source, Double triggerTime) {
				double duration = source.getDuration();
				if (duration > 0) {
					durationLabel.setValue(Time.convertToTimeString(duration));
				} else {
					durationLabel.setValue("??:??:??");
				}
			}
		});
		this.newQuestionBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 4746159071748280285L;

			@Override
			public void buttonClick(ClickEvent event) {
				newQuestionBtn.setEnabled(false);
				cancelButton.setEnabled(true);
				saveButton.setEnabled(true);
				toggleQuestionFields(true);
				setFields(new Question());
				operationType = QuestionForm.NEW_QUESTION;
			}
		});
		this.cancelButton.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 3774334826140277183L;

			@Override
			public void buttonClick(ClickEvent event) {
				newQuestionBtn.setEnabled(true);
				cancelButton.setEnabled(false);
				saveButton.setEnabled(false);
				setFields(new Question());
				toggleQuestionFields(false);
			}
		});
		this.saveButton.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -8736827762492077607L;

			@Override
			public void buttonClick(ClickEvent event) {
				Question q = getQuestion();
				if (isValid() && vjs.isPaused()) {
					newQuestionBtn.setEnabled(true);
					cancelButton.setEnabled(false);
					saveButton.setEnabled(false);
					toggleQuestionFields(false);
					if (operationType == QuestionForm.EDIT_QUESTION) {
						Notification n = new Notification("Question edited",
								"<strong>Question:</strong> '" + q.getQuestion() + "' was edited succesfully!",
								Notification.Type.TRAY_NOTIFICATION, true);
						n.show(Page.getCurrent());
						eventManager.callListeners(QuestionFormListener.QUESTION_EDITED_EVENT, QuestionForm.this, getQuestion());
					} else {
						Notification n = new Notification("New question added",
								"<strong>Question:</strong> '" + q.getQuestion() + "' was added succesfully!",
								Notification.Type.TRAY_NOTIFICATION, true);
						n.show(Page.getCurrent());
						eventManager.callListeners(QuestionFormListener.QUESTION_CREATED_EVENT, QuestionForm.this, getQuestion());
					}
				} else {
					Notification n = new Notification("Saving failed!",
							"<strong>Question form </strong> contains some errors!",
							Notification.Type.WARNING_MESSAGE, true);
					n.show(Page.getCurrent());
				}
			}
		});
		this.addIncorrectAnswer.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 4746159071748280285L;

			@Override
			public void buttonClick(ClickEvent event) {
				wrongAnswerHandler.addWrongAnswerRow("");
			}
		});
		this.clearIncorrectAnswers.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 3632221052083079517L;

			@Override
			public void buttonClick(ClickEvent event) {
				wrongAnswerHandler.clear();
			}
		});
	}

	/**
	 * Checks if all the question form fields are correctly filled.
	 * 
	 * @return true if all the question form fields are correctly filled; false otherwise.
	 */
	public boolean isValid() {
		return questionField.isValid() &&
				answerField.isValid() &&
				answerDescriptionField.isValid() &&
				this.wrongAnswerHandler.isValid();
	}

	/**
	 * Initializes all common textfields and areas except the incorrect Question fields.
	 */
	private void initTextFields() {
		questionField.setInputPrompt("The Question text");
		questionField.setRequired(true);
		questionField.setRequiredError("Required field");
		questionField.setWidth(250, Unit.PIXELS);
		questionField.addStyleName(SPH_Theme.TEXTFIELD_SMALL);
		questionField.addFocusListener(new TextSelector());
		questionField.addBlurListener(new TextFieldValidator());
		questionField.setNullRepresentation("");
		answerDescriptionField.setRows(7);

		answerField.setInputPrompt("Correct answer");
		answerField.addValidator(new StringLengthValidator("Required field", 1, null, false));
		answerField.setWidth(250, Unit.PIXELS);
		answerField.addStyleName(SPH_Theme.TEXTFIELD_SMALL);
		answerField.addFocusListener(new TextSelector());
		answerField.addBlurListener(new TextFieldValidator());
		answerField.setNullRepresentation("");

		answerDescriptionField.setInputPrompt("A description of the correct answer");
		answerDescriptionField.addFocusListener(new TextSelector());
		answerDescriptionField.setWidth(250, Unit.PIXELS);
		answerDescriptionField.addStyleName(SPH_Theme.TEXTFIELD_SMALL);
		answerDescriptionField.setRows(5);
		answerDescriptionField.setNullRepresentation("");

		toggleQuestionFields(false);
	}
	/**
	 * 
	 * @param enabled
	 */
	private void toggleQuestionFields(boolean enabled) {
		timeLayout.setEnabled(enabled);
		questionField.setEnabled(enabled);
		answerField.setEnabled(enabled);
		answerDescriptionField.setEnabled(enabled);
		addIncorrectAnswer.setEnabled(enabled);
		clearIncorrectAnswers.setEnabled(enabled);
		wrongAnswerHandler.toggleIncorrectFields(enabled);
	}

	private void setFields(Question question) {
		timeLabel.setValue(Time.convertToTimeString(vjs.getCurrentTime()));
		questionField.setValue(question.getQuestion());
		answerField.setValue(question.getCorrectAnswer(0));
		answerDescriptionField.setValue(question.getAnswerDescription());
		wrongAnswerHandler.setAnswers(question.getIncorrectAnswers());
	}

	/**
	 * Sets the given question to the form for editing.
	 * 
	 * @param question the question to edit in this form.
	 */
	public void editQuestion(Question question) {
		this.originalQuestion = question;
		this.setFields(question);
		this.operationType = QuestionForm.EDIT_QUESTION;
		this.vjs.seekTo(question.getTime());
		this.toggleQuestionFields(true);
		this.newQuestionBtn.setEnabled(true);
		this.cancelButton.setEnabled(true);
		this.saveButton.setEnabled(true);
	}

	/**
	 * Returns the original question (unmodified question).
	 *
	 * @return the originalQuestion.
	 */
	public final Question getOriginalQuestion() {
		return originalQuestion;
	}

	/**
	 * Returns the question generated from the form fields.
	 * 
	 * @return the question generated from the form fields.
	 */
	public Question getQuestion() {
		return new Question((int) vjs.getCurrentTime(), questionField.getValue(), answerField.getValue(),
				answerDescriptionField.getValue(), wrongAnswerHandler.getWrongAnswers());
	}

	/**
	 * Adds a new {@link QuestionFormListener} listener into the component.
	 * 
	 * @param eventName The {@link QuestionFormListener} event the listener will listen to.
	 * @param listener the listener object itself.
	 */
	public void addQuestionFormListener(String eventName, QuestionFormListener listener) {
		this.eventManager.addListener(eventName, listener);
	}

	/**
	 * Removes listener from a specific {@link QuestionFormListener} event.
	 * 
	 * @param eventName the name of the {@link QuestionFormListener} event.
	 * @param listener the listener to remove.
	 */
	public void removeQuestionFormListener(String eventName, QuestionFormListener listener) {
		this.eventManager.removeListener(eventName, listener);
	}

	/**
	 * Removes listener from all {@link QuestionFormListener} events it is registered
	 * by. Convenient way of cleaning up an listener object being destroyed.
	 * 
	 * @param listener the listener to remove.
	 */
	public void removeQuestionFormListener(QuestionFormListener listener) {
		this.eventManager.removeListener(listener);
	}

	/**
	 * Clears all the form fields and sets the form to it initial state.
	 */
	public void clearForm() {
		setFields(new Question());
		toggleQuestionFields( false );
		newQuestionBtn.setEnabled( true );
		cancelButton.setEnabled( false );
		saveButton.setEnabled( false );
	}

	/**
	 * IncorrectAnswerFieldsHandler
	 *
	 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
	 * @author  Juha Mäkilä
	 * @version 1.0
	 * @since   8.11.2013
	 */
	private class IncorrectAnswerFieldsHandler {

		private final ArrayList<TextField> wrongAnswerFields = new ArrayList<TextField>();
		private final ArrayList<Button> delBtns = new ArrayList<Button>();

		/**
		 * Clears incorrect all answer fields. Only one empty field remains.
		 * 
		 * @see #setAnswers(Collection)
		 */
		public void clear() {
			this.setAnswers(null);
		}

		/**
		 * Sets incorrect answer fields. Calling {@link #setAnswers(Collection)}
		 * with an empty Collection or null clears the fields and only one empty
		 * field remains.
		 * 
		 * @param wrongAnswers a collection of wrong answer fields
		 */
		public void setAnswers(Collection<String> wrongAnswers) {
			wrongAnswersLayout.removeAllComponents();
			wrongAnswerFields.clear();
			delBtns.clear();
			if ( wrongAnswers == null || wrongAnswers.size() == 0 ) {
				this.addWrongAnswerRow("");
			} else {
				for(String wrongAnswer : wrongAnswers) {
					this.addWrongAnswerRow(wrongAnswer);
				}
			}
		}

		/**
		 * Creates a new incorrect answer field.
		 * 
		 * @param value the value of the incorrect answer field or null.
		 * @return a new incorrect answer field.
		 */
		private TextField createIncorrecAnswerField(String value) {
			TextField wtf = new TextField();
			wtf.setInputPrompt("incorrect answer choice");
			wtf.addValidator(new StringLengthValidator("Required field", 1, null, false));
			wtf.addValidator(new UniqueAnswerFieldsValidator(wtf));
			wtf.setWidth(150, Unit.PIXELS);
			wtf.addStyleName(SPH_Theme.TEXTFIELD_SMALL);
			wtf.addFocusListener(new TextSelector());
			wtf.addBlurListener(new TextFieldValidator());
			wtf.setNullRepresentation("");
			wtf.setValue(value);
			return wtf;
		}
		/**
		 * Makes sure that there is always at least one incorrect answer field.
		 */
		private void disallowLastRowRemoval() {
			if (wrongAnswersLayout.getComponentCount() == 1) {
				HorizontalLayout row = (HorizontalLayout) wrongAnswersLayout.getComponent(0);
				row.getComponent(0).setEnabled(false);
			} else {
				HorizontalLayout row = (HorizontalLayout) wrongAnswersLayout.getComponent(0);
				row.getComponent(0).setEnabled(true);
			}
		}

		/**
		 * Adds a wrong answer field row to the element.
		 * 
		 * @param value incorrect field value.
		 */
		public void addWrongAnswerRow(String value) {
			TextField wtf = createIncorrecAnswerField(value);
			this.wrongAnswerFields.add(wtf);
			HorizontalLayout incorrectRow = new HorizontalLayout();
			incorrectRow.setSpacing(true);
			Button delBtn = ComponentFactory.createSmallBlueButton(null, true, SPH_Theme.DELETE_ICON_16PX);
			this.delBtns.add(delBtn);
			delBtn.setDescription("Remove this row");
			delBtn.setStyleName(BaseTheme.BUTTON_LINK);
			delBtn.setData(incorrectRow);
			incorrectRow.addComponents(delBtn, wtf);
			wrongAnswersLayout.addComponent(incorrectRow);
			this.disallowLastRowRemoval();
			delBtn.addClickListener(new Button.ClickListener() {

				private static final long serialVersionUID = 5648570030165001378L;

				@Override
				public void buttonClick(ClickEvent event) {
					Button b = event.getButton();
					HorizontalLayout row = (HorizontalLayout) b.getParent();
					TextField tf = (TextField) row.getComponent(1);
					wrongAnswerFields.remove(tf);
					delBtns.remove(b);
					wrongAnswersLayout.removeComponent(row);
					disallowLastRowRemoval();
				}
			});
		}

		public void toggleIncorrectFields(boolean enabled) {
			for (TextField tf: wrongAnswerFields) {
				tf.setEnabled(enabled);
			}
			if (delBtns.size() > 1) {
				for (Button b: delBtns) {
					b.setEnabled(enabled);
				}
			} else {
				for (Button b: delBtns) {
					b.setEnabled(false);
				}
			}
		}

		private ArrayList<TextField> getWrongAnswerFields() {
			return this.wrongAnswerFields;
		}

		public ArrayList<String> getWrongAnswers() {
			ArrayList<String> wrongAnswers = new ArrayList<String>();
			for (TextField tf : this.wrongAnswerFields) {
				wrongAnswers.add(tf.getValue());
			}
			return wrongAnswers;
		}

		/**
		 * Checks if all the incorrect answer fields are correctly filled.
		 * 
		 * @return true if all the incorrect answer fields are correctly filled, false otherwise.
		 */
		public boolean isValid() {
			for (TextField tf : this.wrongAnswerFields) {
				if (!tf.isValid()) {
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * Validates given answer field against all other answer fields in this {@link QuestionForm}.
	 * 
	 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
	 * @author  Juha Mäkilä
	 * @version 0.1
	 * @since   8.11.2013
	 */
	private class UniqueAnswerFieldsValidator extends AbstractStringValidator {

		private static final long serialVersionUID = 4751949791862420465L;

		private final TextField target;

		/**
		 * 
		 * @param tf the textual field that gets validated.
		 */
		public UniqueAnswerFieldsValidator(TextField tf) {
			super("The answer '{0}' already exists in this question");
			this.target = tf;
		}

		/**
		 * 
		 * @see com.vaadin.data.validator.AbstractValidator#isValidValue(java.lang.Object)
		 */
		@Override
		protected boolean isValidValue(String value) {
			ArrayList<TextField> compared = new ArrayList<TextField>(wrongAnswerHandler.getWrongAnswerFields());
			compared.add(answerField);
			compared.remove(this.target);
			for(TextField compare : compared) {
				if (this.target.getValue().equals(compare.getValue())) {
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * QuestionFormListener interface for listening to {@link QuestionForm} Events.
	 * 
	 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
	 * @author  Juha Mäkilä
	 * @version 1.01
	 * @since   11.10.2013
	 */
	public interface QuestionFormListener extends EventListener<String, QuestionForm, Question> {

		/**
		 * event fired when the edited question is ready for save
		 */
		public static final String QUESTION_EDITED_EVENT = "QUESTION_EDITED_EVENT";
		/**
		 * event fired when the edited question is ready for save
		 */
		public static final String QUESTION_CREATED_EVENT = "QUESTION_CREATED_EVENT";

		/**
		 * {@link QuestionWindow} Component's event listeners method.
		 * 
		 * @param eventName name of the {@link QuestionFormListener} event.
		 * @param source the source object of the {@link QuestionFormListener} event.
		 * @param question the question currently handled.
		 */
		@Override
		public void on(String eventName, QuestionForm source, Question question);

	}
}
