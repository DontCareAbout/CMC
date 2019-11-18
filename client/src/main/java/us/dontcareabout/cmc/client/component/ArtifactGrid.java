package us.dontcareabout.cmc.client.component;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.sencha.gxt.cell.core.client.TextButtonCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.grid.GroupingView;

import us.dontcareabout.cmc.client.data.Artifact;
import us.dontcareabout.cmc.common.shared.MuseumUtil;
import us.dontcareabout.gxt.client.component.Grid2;
import us.dontcareabout.gxt.client.model.GetValueProvider;

public class ArtifactGrid extends Grid2<Artifact> {
	private static final int firstWidth = 100;
	private static final Properties properties = GWT.create(Properties.class);

	private ColumnConfig<Artifact, String> museumCC = new ColumnConfig<>(
		new GetValueProvider<Artifact, String>() {
			@Override
			public String getValue(Artifact object) {
				return object.museum.name();
			}
		}
	);

	public ArtifactGrid() {
		init();
	}

	public void refresh(List<Artifact> data) {
		getStore().replaceAll(data);
		checkEmpty();
	}

	@Override
	protected ColumnModel<Artifact> genColumnModel() {
		TextButtonCell open = new TextButtonCell();
		open.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				Artifact item = store.get(event.getContext().getIndex());
				Window.open(MuseumUtil.purgeUrl(item.museum, item.getUrl()), "_blank", null);
			}
		});
		ColumnConfig<Artifact, String> urlId = new ColumnConfig<>(new GetValueProvider<Artifact, String>() {
			@Override
			public String getValue(Artifact object) {
				return MuseumUtil.parseUrlId(object.museum, object.getUrl());
			}
		}, 100, "網址 ID");
		urlId.setCell(open);
		urlId.setFixed(true);

		ColumnConfig<Artifact, Double> score = new ColumnConfig<>(properties.score(), firstWidth, "重要性");
		score.setCell(new AbstractCell<Double>() {
			@Override
			public void render(Context context, Double value, SafeHtmlBuilder sb) {
				String color = value > 0 ? "red" : "green";
				String ratio = Math.abs(value) * 100 / 5 + "%";
				sb.appendHtmlConstant("<div style='height:16px;width:" + ratio + ";background-color:" + color + ";float:right'></div>");
			}
		});
		score.setFixed(true);

		ArrayList<ColumnConfig<Artifact, ?>> list = new ArrayList<>();
		list.add(museumCC);
		list.add(score);
		list.add(urlId);
		list.add(new ColumnConfig<>(properties.note(), 100, "備註"));
		return new ColumnModel<>(list);
	}

	@Override
	protected ListStore<Artifact> genListStore() {
		ListStore<Artifact> result = new ListStore<>(new ModelKeyProvider<Artifact>() {
			@Override
			public String getKey(Artifact item) {
				return MuseumUtil.artifactId(item.museum, MuseumUtil.parseUrlId(item.museum, item.getUrl()));
			}
		});
		return result;
	}

	@Override
	protected GridView<Artifact> genGridView() {
		GroupingView<Artifact> view = new GroupingView<>();
		view.setShowGroupedColumn(false);
		view.setForceFit(true);
		view.groupBy(museumCC);
		return view;
	}

	private void checkEmpty() {
		if (store.size() == 0) {
			mask("沒有符合條件的資料");
		} else {
			unmask();
		}
	}

	interface Properties extends PropertyAccess<Artifact> {
		ValueProvider<Artifact, String> note();
		ValueProvider<Artifact, Double> score();

		ValueProvider<Artifact, String> name();
		ValueProvider<Artifact, String> era();
		ValueProvider<Artifact, String> origin();
		ValueProvider<Artifact, String> material();
		ValueProvider<Artifact, String> description();
		ValueProvider<Artifact, String> dimensions();
	}
}
