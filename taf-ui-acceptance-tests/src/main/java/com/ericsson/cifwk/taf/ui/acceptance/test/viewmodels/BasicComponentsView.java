package com.ericsson.cifwk.taf.ui.acceptance.test.viewmodels;

import java.util.List;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Label;
import com.ericsson.cifwk.taf.ui.sdk.Link;
import com.ericsson.cifwk.taf.ui.sdk.Select;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;

public class BasicComponentsView extends GenericViewModel {
	@UiComponentMapping(id="selectId")
	private Select select;

	@UiComponentMapping(id="textBoxId")
	private TextBox textBox;

	@UiComponentMapping(id="linkId")
	private Link link;

	@UiComponentMapping(id="labelId")
	private Label label;

	@UiComponentMapping(id="buttonId")
	private Button button;

	@UiComponentMapping("h2")
	private UiComponent heading;

	@UiComponentMapping("notFound")
	private UiComponent notFound;

	@UiComponentMapping(".commonClass")
	private List<UiComponent> multipleElementsWithSameClass;

	@UiComponentMapping("notFound")
	private List<UiComponent> notFoundMany;
	
	@UiComponentMapping(id="hidingDiv")
	private Label hidingDiv;

	@UiComponentMapping(id="hiddenDiv")
	private Label hiddenDiv;

	@UiComponentMapping(id="appearingDiv")
	private Label appearingDiv;

	@UiComponentMapping(id="quickAppearingDiv")
	private Label quickAppearingDiv;

	@UiComponentMapping(id="newPopupOpener")
	private Link newPopupOpener;

	@UiComponentMapping(id="newWindowOpener")
	private Link newWindowOpener;
	
	@UiComponentMapping(id="timestampGenerator")
	private Button timestampGenerator;
	
	@UiComponentMapping(id="timestampPresenter")
	private TextBox timestampBox;

	public Select getSelect() {
		return select;
	}

	public TextBox getTextBox() {
		return textBox;
	}

	public Link getLink() {
		return link;
	}

	public Label getLabel() {
		return label;
	}

	public Button getButton() {
		return button;
	}

	public UiComponent getGenericComponent() {
		return heading;
	}
	
	public List<UiComponent> getMultipleElementsWithSameClass() {
		return multipleElementsWithSameClass;
	}

	public UiComponent getNotFound() {
		return notFound;
	}

	public List<UiComponent> getNotFoundMany() {
		return notFoundMany;
	}

	public Label getHidingDiv() {
		return hidingDiv;
	}

	public Label getAppearingDiv() {
		return appearingDiv;
	}

	public Link getNewPopupOpener() {
		return newPopupOpener;
	}

	public Link getNewWindowOpener() {
		return newWindowOpener;
	}

	public Label getQuickAppearingDiv() {
		return quickAppearingDiv;
	}

	public Label getHiddenDiv() {
		return hiddenDiv;
	}

	public Button getTimestampGenerator() {
		return timestampGenerator;
	}

	public TextBox getTimestampBox() {
		return timestampBox;
	}
	
	
}
