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

import sph.Time;
import sph.vaadin.ui.ConfirmDialog;

/**
 * Extends {link sph.vaadin.ui.ConfirmDialog} to question removal dialog.
 * 
 * <p><strong>copyright &copy; Ville-Video 2013</strong></p>
 *
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @author  Juha Mäkilä
 * @version 1.01
 * @since   12.11.2013
 */
public class QuestionRemovalDialog extends ConfirmDialog {

	private static final long serialVersionUID = -7875076488827327898L;

	private final Question question;

	/**
	 * Constructs a new question removal dialog.
	 * 
	 * @param question the question.
	 * @param callback the callback class that gets executed
	 */
	public QuestionRemovalDialog(final Question question, final Callback callback) {
		super("DELETE QUESTION?", callback);
		this.question = question;
		this.doLayout();
	}

	private void doLayout() {
		String qString = "<p><strong class=\"redText\">QUESTION: </strong><Strong><em>" +
				this.question.getQuestion() + "</em></strong></p>";
		qString += "<p><strong class=\"redText\">TIME SLOT: </strong>" +
				Time.convertToTimeString(this.question.getTime()) + "</p>";

		this.setDialogContent(qString);
	}
}
