package edu.vserver.exercises.videoMcq;

import sph.Time;
import sph.vaadin.ui.ComponentFactory;
import sph.vaadin.ui.ConfirmDialog;
import sph.vaadin.ui.SPH_Theme;
import sph.vaadin.ui.videojs.Videojs;
import sph.vaadin.ui.videojs.VideojsFrame;

import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import edu.vserver.exercises.model.Editor;
import edu.vserver.exercises.model.EditorHelper;
import edu.vserver.exercises.model.EditorHelper.EditedExerciseGiver;
import edu.vserver.exercises.videoMcq.QuestionForm.QuestionFormListener;
import edu.vserver.standardutils.Localizer;

/**
 * VideoMcqExecutor is the teacher UI for video exercises.
 * 
 * <p><strong>copyright &copy; Ville-Video 2013</strong></p>
 *
 * @author  Juha Mäkilä
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @version 2.1
 * @since   16.08.2013
 */
public class VideoMcqEditor extends HorizontalLayout implements Editor<VideoMcqExerciseData> {

	private static final long serialVersionUID = -4600841604409240872L;
	private EditorHelper<VideoMcqExerciseData> exerInfoEditor;
	private VideoMcqExerciseData oldExerData;

	private final Panel qEditPanel = new Panel("Saved Questions:");
	private VerticalLayout questionEditButtons;

	private Panel exerPanel;
	private final VideojsFrame videojsFrame = new VideojsFrame();
	private Videojs videojs;
	private final Panel videoPanel = new Panel();
	private QuestionLibrary questionLibrary;

	private CheckBox questionSkippingCheckBox;
	private CheckBox instRespCheckBox;
	private QuestionForm questionForm;


	@Override
	public Layout getView() {
		return this;
	}

	/**
	 * 
	 * @see edu.vserver.exercises.model.Editor#initialize(edu.vserver.standardutils.Localizer,
	 *  	edu.vserver.exercises.model.ExerciseData,
	 * 		edu.vserver.exercises.model.EditorHelper)
	 */
	@Override
	public void initialize( Localizer localizer, VideoMcqExerciseData ExerData, EditorHelper<VideoMcqExerciseData> genExerInfoEditor ) {

		//this.localizer = localizer;
		this.exerInfoEditor = genExerInfoEditor;
		this.oldExerData = ( ExerData == null ? new VideoMcqExerciseData() : ExerData );
		this.questionLibrary = this.oldExerData.getQuestionLibrary();
		doLayout( this.oldExerData );
	}

	private VideoMcqExerciseData getCurrentExercise() {
		System.out.println(this.questionLibrary);
		VideoMcqExerciseData exerData = new VideoMcqExerciseData();
		exerData.setExerName(this.exerInfoEditor.getExerciseInfo().getName());
		exerData.setVideoURL(videojs.getVideoSrc());
		exerData.setMimeType(videojs.getMimeType());
		exerData.setQuestionLibrary(questionLibrary);
		exerData.setInstantResponse(this.instRespCheckBox.getValue());
		exerData.setSliderEnabled(this.questionSkippingCheckBox.getValue());
		return exerData;
	}

	private void doLayout( VideoMcqExerciseData oldExerData ) {

		this.setSpacing( true );

		videojsFrame.setVideoSource( oldExerData.getVideoURL(), oldExerData.getMimeType() );
		videojs = videojsFrame.getVideojsPlayer();
		videoPanel.setContent( videojsFrame );
		VerticalLayout controlsLayout = new VerticalLayout();
		controlsLayout.setWidth(250, Sizeable.Unit.PIXELS);
		controlsLayout.addComponent( exerInfoEditor.getInfoEditorView() );
		controlsLayout.addComponent( exerInfoEditor.getControlbar( new EditedExerciseGiver< VideoMcqExerciseData >() {

			@Override
			public VideoMcqExerciseData getCurrExerData( boolean forSaving ) {
				return getCurrentExercise();
			}
		}));

		this.addComponent( controlsLayout );
		exerPanel = new Panel();
		VerticalLayout exerLayout = new VerticalLayout();
		exerPanel.setContent( exerLayout );
		exerLayout.setSpacing( true );

		this.questionSkippingCheckBox = new CheckBox("Allow skipping questions");
		this.questionSkippingCheckBox.setValue( oldExerData.isSliderEnabled() );
		this.instRespCheckBox = new CheckBox("Give instant response");
		this.instRespCheckBox.setValue(oldExerData.isInstantResponse());

		HorizontalLayout globalVariableLayout = new HorizontalLayout();
		globalVariableLayout.setSpacing(true);
		globalVariableLayout.addComponents(new Label("<strong>Global settings: </strong>", ContentMode.HTML), this.questionSkippingCheckBox, this.instRespCheckBox);
		this.questionForm = new QuestionForm(videojs);
		exerLayout.addComponents(videoPanel, globalVariableLayout, this.questionForm );

		this.setupSavedQuestionsListView();
		this.addComponents(exerLayout, qEditPanel);
		this.repaintSavedQuestionsListView();
		this.questionForm.addQuestionFormListener(QuestionForm.QuestionFormListener.QUESTION_EDITED_EVENT,
				new QuestionForm.QuestionFormListener() {

			private static final long serialVersionUID = 8674189273630233196L;

			/**
			 * {@link QuestionForm} component's
			 * 
			 * @param eventName name of the {@link QuestionWindow.VjsListener VideojsListener} event.
			 * @param source the source object of the {@link QuestionFormListener VideojsListener} event.
			 * @param question the question currently handled.
			 */
			@Override
			public void on(String eventName, QuestionForm source, Question question) {
				Question old = source.getOriginalQuestion();
				questionLibrary.replace( old, question );
				repaintSavedQuestionsListView();
			}
		});

		this.questionForm.addQuestionFormListener(QuestionForm.QuestionFormListener.QUESTION_CREATED_EVENT,
				new QuestionForm.QuestionFormListener() {

			private static final long serialVersionUID = 8674189273630233196L;

			/**
			 * {@link QuestionWindow} Component has triggered an event.
			 * 
			 * @param eventName name of the {@link QuestionWindow.VjsListener VideojsListener} event.
			 * @param source the source object of the {@link QuestionFormListener VideojsListener} event.
			 * @param question the question currently handled.
			 */
			@Override
			public void on(String eventName, QuestionForm source, Question question) {
				questionLibrary.add( question );
				repaintSavedQuestionsListView();
			}
		});
	}

	/**
	 * Creates an edit button with click action (Click action: sets the QuestionForm to edit the question)
	 * 
	 * @param  question the question.
	 * @return created button.
	 */
	private Button createEditButton(Question question) {
		Button btn = ComponentFactory.createSmallBlueButton("Edit", true, SPH_Theme.EDIT_ICON_16PX);
		btn.setDescription("<h4 class=\"tooltip\">EDIT QUESTION</h4>" +
				"<p class=\"tooltip\">" + question.getQuestion() + "</p>");
		btn.setWidth(60, Unit.PIXELS);
		btn.setData(question);
		btn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -2352982251747900119L;

			@Override
			public void buttonClick(ClickEvent event) {
				Question q = (Question) event.getButton().getData();
				questionForm.editQuestion( q );
			}
		});
		return btn;
	}

	/**
	 * Creates a delete button with click action (Click action: removes the row and sets the deletion dialog)
	 * 
	 * @param  question the question.
	 * @return created button.
	 */
	private Button createRemoveButton(Question question) {
		Button btn = ComponentFactory.createSmallBlueButton("Del", true, SPH_Theme.DELETE_ICON_16PX);
		btn.setDescription("<h4 class=\"redText tooltip\">DELETE QUESTION?</h4>" +
				"<p class=\"tooltip\">" + question.getQuestion() + "</p>");
		btn.setWidth(60, Unit.PIXELS);
		btn.setData(question);
		btn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -6016940629549296901L;

			@Override
			public void buttonClick(ClickEvent event) {
				Button b = event.getButton();
				QuestionRemovalDialog dialog = new QuestionRemovalDialog((Question) b.getData(), new QuestionDeleter(b));
				dialog.show();
			}
		});
		return btn;
	}

	/**
	 * The action performed when the delete button gets clicked
	 *
	 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
	 * @author  Juha Mäkilä
	 * @version 0.1
	 * @since   8.11.2013
	 */
	private class QuestionDeleter implements ConfirmDialog.Callback {
		private final Button delBtn;
		/**
		 * 
		 * @param b the target delete button
		 */
		public QuestionDeleter(Button b) {
			delBtn = b;
		}

		@Override
		public void onDialogResult(boolean resultIsYes) {
			if (resultIsYes) {
				Question q = (Question) delBtn.getData();
				questionLibrary.remove(q);
				HorizontalLayout row = (HorizontalLayout) delBtn.getParent();
				questionEditButtons.removeComponent(row);
				repaintSavedQuestionsListHeading(questionLibrary.size());
				questionForm.clearForm();
			}
		}
	}

	/**
	 * Set the saved questions list view component ready for use.
	 */
	private void setupSavedQuestionsListView() {
		this.qEditPanel.setEnabled(false);
		this.questionEditButtons = new VerticalLayout();
		this.questionEditButtons.setSpacing(true);
		this.questionEditButtons.setMargin(true);
		Panel qEditScrollPanel = new Panel();
		qEditScrollPanel.setHeight(490, Unit.PIXELS);
		qEditScrollPanel.setWidth(220, Unit.PIXELS);
		qEditScrollPanel.setContent(this.questionEditButtons);
		this.qEditPanel.setContent(qEditScrollPanel);
	}

	/**
	 * Repaints the contents of the saved questions list view component and
	 * enables it to the editor view if it contains any Questions.
	 */
	private void repaintSavedQuestionsListView() {
		int no = this.questionLibrary.size();
		if (no > 0) {
			this.repaintSavedQuestionsListHeading(no);
			this.questionEditButtons.removeAllComponents();
			for (Question q : this.questionLibrary) {
				this.questionEditButtons.addComponent(createSavedQuestionRow(q));
			}
			this.qEditPanel.setEnabled(true);
		} else {
			this.qEditPanel.setEnabled(false);
		}
	}


	/**
	 * Repaints the heading text of the saved questions list view component.
	 * 
	 * @param qCount number of the saved questions.
	 */
	private void repaintSavedQuestionsListHeading(int qCount) {
		String text = "Saved Questions: <em style=\"font-size: 60%;\">" + qCount;
		text += (qCount > 1)? " units</em>" : " unit</em>";
		this.qEditPanel.setCaption(text);
	}

	/**
	 * Creates a row ({@link HorizontalLayout}) for new saved {@link Question}.
	 * 
	 * <p>the row contains edit and delete buttons for the saved {@link Question}.</p>
	 * 
	 * @param question target question
	 * @return the row created
	 */
	private HorizontalLayout createSavedQuestionRow(Question question) {
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing( true );
		Label timeLabel = new Label( Time.convertToTimeString( question.getTime() ) );
		Button editButton = createEditButton( question );
		Button removeButton = createRemoveButton( question );
		hl.addComponents( timeLabel, editButton, removeButton );
		return hl;
	}
}