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
package sph.vaadin.ui;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * ConfirmDialog implements a simple dialog box for Vaadin environment.
 * 
 * <p><strong>copyright &copy; Ville-Video 2013</strong></p>
 * 
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @author  Juha Mäkilä
 * @version 1.0
 * @since   8.11.2013
 */
public class ConfirmDialog extends Window implements Button.ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4174820160780846820L;
	private final Callback callback;
	private final Button yes = ComponentFactory.createBlueButton("Confirm", true, SPH_Theme.OK_ICON_24PX);
	private final Button no = ComponentFactory.createBlueButton("Cancel", true, SPH_Theme.NOT_OK_ICON_24PX);
	private boolean isVisible;
	private final VerticalLayout baseLayout = new VerticalLayout();
	private final HorizontalLayout contentLayout = new HorizontalLayout();
	private final HorizontalLayout btnFooter = new HorizontalLayout();

	/**
	 * Constructs a new dialog window.
	 * 
	 * @param caption the caption text of the dialog window.
	 * @param callback the callback class for the dialog window.
	 */
	public ConfirmDialog(String caption, Callback callback) {
		super(caption);
		this.callback = callback;
		this.setup();
	}

	/**
	 * Constructs a new dialog window with textual content.
	 * 
	 * <p><strong>NOTE: </strong> Allows HTML content.</p>
	 * 
	 * @param caption the caption text of the dialog window.
	 * @param content the content text of the dialog window.
	 * @param callback the callback class for the dialog window.
	 */
	public ConfirmDialog(String caption, String content, Callback callback) {
		super(caption);
		this.setDialogContent(content);
		this.callback = callback;
		this.setup();
	}

	/**
	 * Constructs a new dialog window with content.
	 * 
	 * @param caption the caption text of the dialog window.
	 * @param content the content component of the dialog window.
	 * @param callback the callback class for the dialog window.
	 */
	public ConfirmDialog(String caption, Component content, Callback callback) {
		super(caption);
		this.setDialogContent(content);
		this.callback = callback;
		this.setup();
	}

	/**
	 * 
	 */
	private void setup() {
		addStyleName("confirmDialog");
		setModal(true);
		setResizable(false);
		setClosable(false);
		yes.addClickListener(this);
		yes.setWidth(100, Unit.PIXELS);
		no.addClickListener(this);
		no.setWidth(100, Unit.PIXELS);
		btnFooter.addStyleName("buttons");
		btnFooter.setMargin(true);
		btnFooter.setSpacing(true);
		btnFooter.setWidth(100, Unit.PERCENTAGE);
		btnFooter.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		btnFooter.addComponents(yes, no);
		baseLayout.setMargin(true);
		baseLayout.setSpacing(true);
		contentLayout.setWidth(100, Unit.PERCENTAGE);
		contentLayout.addStyleName("content");
		baseLayout.addComponents(contentLayout, btnFooter);
		setContent(baseLayout);
	}

	/**
	 * Sets the new textual content to the Dialog.
	 * 
	 * <p><strong>NOTE: </strong> Allows HTML content.</p>
	 * 
	 * @param content the new textual content of the Dialog.
	 */
	public void setDialogContent(String content) {
		contentLayout.removeAllComponents();
		if (content != null) {
			Label l = new Label(content, ContentMode.HTML);
			contentLayout.addComponent(l);
		}
	}

	/**
	 * Sets the new content component to the Dialog.
	 * 
	 * @param content the new component content of the Dialog.
	 */
	public void setDialogContent(Component content) {
		contentLayout.removeAllComponents();
		if (content != null) {
			contentLayout.addComponent(content);
		}
	}

	/**
	 * Shows the dialog window in the UI.
	 */
	public void show() {
		if (!isVisible) {
			UI ui = UI.getCurrent();
			ui.addWindow(this);
			isVisible = true;
		}
	}

	/**
	 * Hides the dialog window from the UI.
	 */
	public void hide() {
		if (isVisible) {
			UI ui = UI.getCurrent();
			ui.removeWindow(this);
			isVisible = false;
		}
	}

	/**
	 * Called when a Button has been clicked.
	 *
	 * @param event An event containing information about the click.
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		hide();
		callback.onDialogResult(event.getSource() == yes);
	}

	/**
	 * Callback interface for the {@link ConfirmDialog} window.
	 *
	 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
	 * @author  Juha Mäkilä
	 * @version 0.1
	 * @since   8.11.2013
	 */
	public interface Callback {

		/**
		 * Executes when a Dialog Button gets clicked.
		 * 
		 * @param resultIsYes true if the dialog is confirmed and false otherwise.
		 */
		public void onDialogResult(boolean resultIsYes);
	}

}
