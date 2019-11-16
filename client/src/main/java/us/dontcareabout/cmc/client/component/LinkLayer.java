package us.dontcareabout.cmc.client.component;

import com.google.gwt.user.client.Window;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.SpriteSelectionHandler;

import us.dontcareabout.cmc.client.Resources;
import us.dontcareabout.gxt.client.draw.Cursor;
import us.dontcareabout.gxt.client.draw.LImageSprite;
import us.dontcareabout.gxt.client.draw.LTextSprite;
import us.dontcareabout.gxt.client.draw.LayerContainer;
import us.dontcareabout.gxt.client.draw.LayerSprite;

//XXX from RQC
public class LinkLayer extends LayerContainer {
	private GitHub github = new GitHub();

	public LinkLayer() {
		addLayer(github);
	}

	@Override
	protected void onResize(int width, int height) {
		github.setLX(width - githubWidth - height);
		github.resize(height + githubWidth, height);
		super.onResize(width, height);
	}

	private static final int githubWidth = 105;

	private class GitHub extends LayerSprite {
		LImageSprite logo = new LImageSprite(Resources.instance.github());
		LTextSprite text = new LTextSprite("GitHub");

		GitHub() {
			text.setFontSize(32);

			add(logo);
			add(text);

			setMemberCursor(Cursor.POINTER);
			addSpriteSelectionHandler(new SpriteSelectionHandler() {
				@Override
				public void onSpriteSelect(SpriteSelectionEvent event) {
					Window.open("https://github.com/DontCareAbout/CMC", "_blank", "");
				}
			});
		}

		@Override
		protected void adjustMember() {
			final double height = getHeight() - 6;
			text.setLX(height + 6);
			text.setLY(-2);
			logo.setLY(3);
			logo.setWidth(height);
			logo.setHeight(height);
		}
	}
}
