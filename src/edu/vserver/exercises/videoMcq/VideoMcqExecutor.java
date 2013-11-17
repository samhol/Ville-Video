package edu.vserver.exercises.videoMcq;

import sph.vaadin.ui.ComponentFactory;
import sph.vaadin.ui.SPH_Theme;
import sph.vaadin.ui.svg.SvgDots;
import sph.vaadin.ui.videojs.Videojs;

import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

import edu.vserver.exercises.helpers.ExerciseExecutionHelper;
import edu.vserver.exercises.model.ExecutionSettings;
import edu.vserver.exercises.model.ExecutionState;
import edu.vserver.exercises.model.ExecutionStateChangeListener;
import edu.vserver.exercises.model.Executor;
import edu.vserver.exercises.model.ExerciseException;
import edu.vserver.exercises.model.SubmissionListener;
import edu.vserver.exercises.model.SubmissionResult;
import edu.vserver.exercises.model.SubmissionType;
import edu.vserver.standardutils.Localizer;
import edu.vserver.standardutils.TempFilesManager;

/**
 * VideoMcqExecutor is the student UI for video exercises.
 * 
 * <p><strong>copyright &copy; Ville-Video 2013</strong></p>
 *
 * @author  Juha Mäkilä
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @version 1.0
 * @since   16.10.2013
 */
@com.vaadin.annotations.JavaScript({
	"javascript/jquery-1.10.2.min.js",
	"javascript/jquery-ui-1.10.3.custom.min.js",
	"javascript/startButton.js"
})
public class VideoMcqExecutor extends VerticalLayout implements Executor<VideoMcqExerciseData, VideoMcqSubmissionInfo> {

	private static final long serialVersionUID = 2682119786422750060L;
	private final ExerciseExecutionHelper< VideoMcqSubmissionInfo > execHelper = new ExerciseExecutionHelper< VideoMcqSubmissionInfo >();
	private VideoMcqExerciseData exerData;
	private QuestionLibrary questionLibrary;
	private VideoMcqSubmissionInfo submInfo;

	private final HorizontalLayout videoLayout = new HorizontalLayout();
	private final Videojs vjs = new Videojs();

	private final QuestionWindow questionWindow = new QuestionWindow();

	private final HorizontalLayout dotLayout = new HorizontalLayout();
	private final Button startButton = ComponentFactory.createImageButton("Start exercise", SPH_Theme.START_ICON_128PX, true);
	private final Button fullWindowButton = ComponentFactory.createImageButton("Go to full window mode", SPH_Theme.FULLSCREEN_ICON_16PX, true);

	private Button goToStartBtn;
	private final QuestionDots questionDots = new QuestionDots();

	/**
	 * false if exercise is not submitted and true if it is submitted.
	 */
	private boolean askingEnabled;

	/**
	 * Returns the
	 *
	 * @return the askingEnabled
	 */
	private boolean isAskingEnabled() {
		return askingEnabled;
	}

	/**
	 * Sets the
	 *
	 * @param askingEnabled the askingEnabled to set
	 */
	private void setAskingEnabled(boolean askingEnabled) {
		this.askingEnabled = askingEnabled;
		if (this.askingEnabled) {
			this.goToStartBtn.setEnabled(true);
			this.vjs.unmask();
		} else {
			this.goToStartBtn.setEnabled(false);
			this.vjs.mask();
		}

	}

	@Override
	public void initialize(Localizer localizer, VideoMcqExerciseData exerciseData, VideoMcqSubmissionInfo oldSubm,
			TempFilesManager materials, ExecutionSettings fbSettings) throws ExerciseException {
		this.exerData = exerciseData;
		this.questionLibrary = exerData.getQuestionLibrary();
		this.questionWindow.setInformative(this.exerData.isInstantResponse());
		vjs.pauseAt(this.questionLibrary.getTimeSlots());
		if ( oldSubm == null ) {
			this.submInfo = new VideoMcqSubmissionInfo();
		}
		else {
			this.submInfo = oldSubm;
			//this.submInfo.setPoints(0);
		}
		setListeners();
		doLayout(this.exerData);
	}

	private void doLayout(VideoMcqExerciseData exerData) {
		VerticalLayout exerLayout = new VerticalLayout();
		exerLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		vjs.setSource(exerData.getVideoURL(), exerData.getMimeType());
		vjs.mask();
		vjs.minimalUserControlsEnabled(true);
		vjs.supportFullscreenMode(false);

		this.videoLayout.addComponents(vjs, this.startButton);
		this.videoLayout.setWidth(760, Unit.PIXELS);
		this.videoLayout.setHeight(432, Unit.PIXELS);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setWidth(700, Unit.PIXELS);
		buttonLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		this.startButton.addStyleName("ExerStarter");
		this.startButton.setDescription("Start the excercise");

		buttonLayout.addComponents(this.startButton);
		this.buildDotLayout();
		exerLayout.addComponents(videoLayout, this.dotLayout, buttonLayout);

		questionWindow.setQuestionLibrary(questionLibrary);
		questionDots.draw(questionLibrary);
		questionDots.setClickable(exerData.isSliderEnabled());
		questionDots.setInformative(exerData.isInstantResponse());

		this.addComponent(exerLayout);

		JavaScript.getCurrent().execute("VideoMcqExecutor.setStartButton(" + vjs.getId() + ");");
	}

	@Override
	public void registerSubmitListener( SubmissionListener< VideoMcqSubmissionInfo > submitListener ) {
		execHelper.registerSubmitListener( submitListener );
	}

	@Override
	public Layout getView() {
		return this;
	}

	@Override
	public void shutdown() {
		System.out.println("shutdown()");
		vjs.detach();

	}

	@Override
	public void askReset() {
		setAskingEnabled(false);
		//askingEnabled = true;
		//submInfo.setPoints(0);
		//submInfo.increaseTries();
		questionLibrary.resetGivenAnswers();
		vjs.clearPauseAtList();
		vjs.pauseAt(this.questionLibrary.getTimeSlots());
		vjs.seekTo(0);
		vjs.mask();
		execHelper.informResetDefault();
		questionDots.draw(this.questionLibrary);
		goToStartBtn.setEnabled(true);
		fullWindowButton.setEnabled(true);
		JavaScript.getCurrent().execute("VideoMcqExecutor.showStartButton();");
	}

	@Override
	public ExecutionState getCurrentExecutionState() {
		return execHelper.getState();
	}

	@Override
	public void askSubmit(SubmissionType submType) {
		double score = questionLibrary.getScore();
		setAskingEnabled(false);
		execHelper.informOnlySubmit(score, new VideoMcqSubmissionInfo(questionWindow.getQuestionLibrary()), submType, null );
		vjs.pause();
		vjs.mask("Exercise completed!");

		vjs.clearPauseAtList();

		goToStartBtn.setEnabled(false);
		fullWindowButton.setEnabled(false);
		JavaScript.getCurrent().execute("VideoMcqExecutor.hideStartButton();");

	}

	@Override
	public void registerExecutionStateChangeListener(ExecutionStateChangeListener execStateListener) {
		execHelper.registerExerciseExecutionStateListener(execStateListener);

	}

	private void setListeners() {
		if (this.exerData.isInstantResponse()) {
			this.questionDots.addDotsListener(QuestionDots.QuestionDotListener.CLICK_EVENT, new QuestionDots.QuestionDotListener() {

				private static final long serialVersionUID = -1592558255318728462L;

				@Override
				public void on(String eventName, SvgDots canvas, Integer dotIndex) {
					if (isAskingEnabled()) {
						SvgDots.Dot dot = canvas.getDot(dotIndex);
						Question q = (Question) dot.getData();
						vjs.seekTo(q.getTime());
					}
				}
			});
		}
		this.startButton.addAttachListener(new AttachListener() {

			private static final long serialVersionUID = 111802046795345210L;

			@Override
			public void attach(AttachEvent event) {
				JavaScript.getCurrent().execute("VideoMcqExecutor.setStartButton(" + vjs.getId() + ");");
				//System.out.println("startButton AttachListener()");
			}

		});
		this.startButton.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 7181459362080977100L;

			@Override
			public void buttonClick(ClickEvent event) {
				JavaScript.getCurrent().execute("VideoMcqExecutor.hideStartButton();");
				vjs.unmask();
				setAskingEnabled(true);
				if (!questionWindow.ask(0)) {
					vjs.play();
				}
			}
		});
		fullWindowButton.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -8152740888003850116L;

			@Override
			public void buttonClick(ClickEvent event) {
				vjs.fullWindowEnabled(true);
			}
		});
		vjs.addVideojsListener(Videojs.VjsListener.PAUSE_EVENT, new Videojs.VjsListener() {

			private static final long serialVersionUID = -7260155712968515580L;

			@Override
			public void on(String eventName, Videojs source, Double triggerTime) {
				if (isAskingEnabled()) {
					//System.out.println("PAUSED at time: " + triggerTime);
					questionWindow.ask(triggerTime.intValue());
				}
			}
		});
		questionWindow.addQuestionEventListener(QuestionWindow.QuestionEventListener.TIMESLOT_FINISHED_EVENT, new QuestionWindow.QuestionEventListener() {

			private static final long serialVersionUID = 172768819560745819L;

			@Override
			public void on(String eventName, QuestionWindow source, Integer timeSlot) {
				if (!questionLibrary.containsUnansweredInTimeSlot(timeSlot)) {
					vjs.removePauseAtTime(timeSlot);
					if (!questionLibrary.containsUnanswerded()) {
						exerciseFinished();
					}
				}
				questionDots.draw(questionWindow.getQuestionLibrary());
				vjs.play();
			}
		});
		questionWindow.addQuestionEventListener(QuestionWindow.QuestionEventListener.QUESTION_FINISHED_EVENT,
				new QuestionWindow.QuestionEventListener() {

			private static final long serialVersionUID = 172768819560745819L;

			@Override
			public void on(String eventName, QuestionWindow source, Integer timeSlot) {
				questionDots.draw(source.getQuestionLibrary());
			}
		});
		this.registerSubmitListener(new SubmissionListener<VideoMcqSubmissionInfo>() {

			private static final long serialVersionUID = 3956036740947573677L;

			@Override
			public void submitted(SubmissionResult<VideoMcqSubmissionInfo> submission) {
				vjs.clearPauseAtList();
				//System.out.println("submitted");
			}

		});
	}

	/**
	 * Disables exercise controls and gives a tray notification.
	 */
	private void exerciseFinished() {
		System.out.println("All questions asked");
		goToStartBtn.setEnabled(false);
		fullWindowButton.setEnabled(false);
		Notification n = new Notification("All " + questionLibrary.size() + " questions are answered!",
				Notification.Type.TRAY_NOTIFICATION);
		n.setIcon(new ThemeResource(SPH_Theme.INFO_ICON_24PX));
		n.setDelayMsec(500);
		//n.setPosition(com.vaadin.shared.Position.MIDDLE_CENTER);
		n.show(Page.getCurrent());
	}

	/**
	 * Sets up the dot layout which allows user to see and click question dots.
	 * 
	 * <p>Dot clicking is only allowed if question skipping is allowed in the exercise.</p>
	 */
	private void buildDotLayout() {
		this.goToStartBtn = ComponentFactory.createImageButton("Go to start", SPH_Theme.REWIND_ICON_20PX, true);
		if (this.exerData.isSliderEnabled()) {
			this.goToStartBtn.addClickListener(new Button.ClickListener() {

				private static final long serialVersionUID = 7181459362080977100L;

				@Override
				public void buttonClick(ClickEvent event) {
					if (vjs.getCurrentTime() > 0) {
						vjs.seekTo(0);
					}
				}
			});
			this.dotLayout.addComponent(goToStartBtn);
		}
		if (this.exerData.isSliderEnabled()) {
			this.dotLayout.addComponent(this.goToStartBtn);
		}
		this.dotLayout.addComponents(questionDots, this.fullWindowButton);
	}

	private void setInitialSettings() {

	}

}