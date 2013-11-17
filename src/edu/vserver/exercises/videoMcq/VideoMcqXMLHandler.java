package edu.vserver.exercises.videoMcq;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.vserver.exercises.model.ExerciseException;
import edu.vserver.exercises.model.PersistenceHandler;
import edu.vserver.standardutils.TempFilesManager;
import edu.vserver.standardutils.XMLHelper;

/**
 * Class writes and reads the videoMcq XML data.
 * 
 * @author  Juha Mäkilä
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @version 1.0
 * @since   16.09.2013
 */
public final class VideoMcqXMLHandler implements PersistenceHandler<VideoMcqExerciseData, VideoMcqSubmissionInfo> {

	private static final long serialVersionUID = -3150033834959652936L;
	/**
	 * Gives a new instance of VideoMcqXMLHandler.
	 */
	public static final VideoMcqXMLHandler INSTANCE = new VideoMcqXMLHandler();

	private static final String VIDEO_URL = "videoURL";
	private static final String SLIDER_ENABLED = "sliderEnabled";

	private VideoMcqXMLHandler() {

	}

	@Override
	public VideoMcqExerciseData loadExerData(byte[] dataPres, TempFilesManager tempManager) throws ExerciseException {
		VideoMcqExerciseData exerData = new VideoMcqExerciseData();
		try {
			Document doc = XMLHelper.parseFromBytes(dataPres);
			doc.getDocumentElement().normalize();
			exerData.setVideoURL(doc.getDocumentElement().getAttribute(VIDEO_URL));
			exerData.setMimeType(doc.getDocumentElement().getAttribute("mimetype"));
			exerData.setExerName(doc.getDocumentElement().getAttribute("exerName"));
			exerData.setSliderEnabled(Boolean.parseBoolean(doc.getDocumentElement().getAttribute(SLIDER_ENABLED)));
			exerData.setInstantResponse(Boolean.parseBoolean(doc.getDocumentElement().getAttribute("instantResponse")));
			QuestionLibrary questionLibrary = new QuestionLibrary();
			NodeList questionNodes = doc.getElementsByTagName("timedQuestion");
			for (int i = 0; i < questionNodes.getLength(); i++) {
				Node questionNode = questionNodes.item(i);
				NodeList questionData = questionNode.getChildNodes();
				int time = Integer.parseInt(questionData.item(0).getTextContent());
				String question = questionData.item(1).getTextContent();
				String answer = questionData.item(2).getTextContent();
				String answerDescription = questionData.item(3).getTextContent();
				ArrayList<String> falseAnswers = new ArrayList<String>();
				NodeList falseData = questionData.item(4).getChildNodes();
				for (int j = 0; j < falseData.getLength(); j++) {
					falseAnswers.add(falseData.item(j).getTextContent());
				}
				questionLibrary.add(new Question(time, question, answer, answerDescription, falseAnswers));
			}
			exerData.setQuestionLibrary(questionLibrary);

		} catch (ParserConfigurationException e) {
			throw new ExerciseException(ExerciseException.ErrorType.EXER_LOAD_ERROR, e);
		} catch (SAXException e) {
			throw new ExerciseException(ExerciseException.ErrorType.EXER_LOAD_ERROR, e);
		} catch (IOException e) {
			throw new ExerciseException(ExerciseException.ErrorType.EXER_LOAD_ERROR, e);
		}
		return exerData;
	}

	@Override
	public byte[] saveExerData(VideoMcqExerciseData toWrite, TempFilesManager tempManager) throws ExerciseException {
		byte[] res = null;
		try {
			QuestionLibrary questionLibrary = toWrite.getQuestionLibrary();
			ArrayList<Question> questions = questionLibrary.getAllQuestions();
			Document doc = XMLHelper.createEmptyDocument();

			Element root = doc.createElement("videoMcq-exercise");
			doc.appendChild(root);
			root.setAttribute(VIDEO_URL, toWrite.getVideoURL());
			root.setAttribute("mimetype", toWrite.getMimeType());
			root.setAttribute("exerName", toWrite.getExerName());
			root.setAttribute(SLIDER_ENABLED, Boolean.toString(toWrite.isSliderEnabled()));
			root.setAttribute("instantResponse", Boolean.toString(toWrite.isInstantResponse()));

			for (Question q : questions) {
				Element timedQuestion = doc.createElement("timedQuestion");
				root.appendChild(timedQuestion);


				Element time = doc.createElement("time");
				time.setTextContent(Integer.toString(q.getTime()));
				timedQuestion.appendChild(time);

				Element question = doc.createElement("question");
				question.setTextContent(q.getQuestion());
				timedQuestion.appendChild(question);

				Element answer = doc.createElement("answer");
				answer.setTextContent(q.getCorrectAnswer(0));
				timedQuestion.appendChild(answer);

				Element answerDescription = doc.createElement("AnswerDescription");
				answerDescription.setTextContent(q.getAnswerDescription());
				timedQuestion.appendChild(answerDescription);

				Element falseAnswers = doc.createElement("falseAnswers");
				timedQuestion.appendChild(falseAnswers);

				ArrayList<String> falseList = q.getIncorrectAnswers();

				for (int j = 0; j < q.getIncorrectAnswers().size(); j++) {
					Element falseAnswer = doc.createElement("false" + (j + 1));
					falseAnswer.setTextContent(falseList.get(j));
					falseAnswers.appendChild(falseAnswer);
				}
			}
			res = XMLHelper.xmlToBytes(doc);
		} catch (ParserConfigurationException e) {
			throw new ExerciseException(ExerciseException.ErrorType.EXER_WRITE_ERROR, e);
		} catch (TransformerConfigurationException e) {
			throw new ExerciseException(ExerciseException.ErrorType.EXER_WRITE_ERROR, e);
		} catch (TransformerException e) {
			throw new ExerciseException(ExerciseException.ErrorType.EXER_WRITE_ERROR, e);
		} catch (TransformerFactoryConfigurationError e) {
			throw new ExerciseException(ExerciseException.ErrorType.EXER_WRITE_ERROR, e);
		}
		return res;
	}

	@Override
	public byte[] saveSubmission(VideoMcqSubmissionInfo subm, TempFilesManager tempManager) throws ExerciseException {
		try {
			ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
			ObjectOutputStream objOut = new ObjectOutputStream(bytesOut);
			objOut.writeObject(subm);

			return bytesOut.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public VideoMcqSubmissionInfo loadSubmission(byte[] dataPres, boolean forStatGiver, TempFilesManager tempManager)
			throws ExerciseException {
		VideoMcqSubmissionInfo res = null;
		try {
			ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(dataPres));
			res = (VideoMcqSubmissionInfo) objIn.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return res;
	}
}