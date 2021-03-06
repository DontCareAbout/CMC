package us.dontcareabout.cmc.client.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.base.Strings;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor.Path;
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
import com.sencha.gxt.widget.core.client.grid.RowExpander;

import us.dontcareabout.cmc.client.data.Artifact;
import us.dontcareabout.cmc.client.data.DataCenter;
import us.dontcareabout.cmc.common.shared.ArtifactM;
import us.dontcareabout.cmc.common.shared.ImageUrl;
import us.dontcareabout.cmc.common.shared.Museum;
import us.dontcareabout.cmc.common.shared.MuseumUtil;
import us.dontcareabout.cmc.common.shared.Selection;
import us.dontcareabout.gxt.client.component.Grid2;
import us.dontcareabout.gxt.client.model.GetValueProvider;

public class ArtifactGrid extends Grid2<Artifact> {
	private static final int firstWidth = 100;
	private static final Properties properties = GWT.create(Properties.class);

	private RowExpander<Artifact> rowExpander = new RowExpander<Artifact>(
		new AbstractCell<Artifact>() {
			final String row1Style = "font-size: 16px; margin: 2px 50px 0px 50px; padding: 8px; background-color: #B3729F; color: white; font-weight: bold;";
			final String row2Style = "font-size: 14px; margin: 0px 50px 4px 50px; padding: 4px 8px; border: #B3729F 3px solid; line-height: 1.5;";
			final String col1Style = "display: table-cell; padding: 8px; background-color: #B3729F; color: white; font-size: 16px; font-weight: bold; width: 150px";
			final String col2Style = "display: table-cell; padding: 5px 8px; border: #B3729F 1px solid; font-size: 16px; line-height: 1.2;";
			final int logoSize = 95;

			@Override
			public void render(Context context, Artifact value, SafeHtmlBuilder sb) {
				//上方表格區
				if (!Strings.isNullOrEmpty(value.getName())
						|| !Strings.isNullOrEmpty(value.getEra())
						|| !Strings.isNullOrEmpty(value.getOrigin())) {

					//Museum Logo
					sb.appendHtmlConstant("<div style='width:" + logoSize + "px;height:" + logoSize + "px;margin:2px 0px 4px 50px;"
						//後面都是垂直置中需要的鬼玩意
						+ "float:left;display:flex;justify-content:center;align-items:center;'>");
					sb.appendHtmlConstant("<img style='width:100%' src='" + value.getId().getMuseum().logo + "' /></div>");
					//限制 div-table 寬度的 div，因為 div-table 有 border 所以要卡 padding
					sb.appendHtmlConstant("<div style='margin:2px 50px 4px " + (logoSize + 55) + "px; padding: 0 4px 0 0;'>");
					//真正的 div-table
					sb.appendHtmlConstant("<div style='display:table;border: #b3729f solid 2px; width: 100%'>");
					twoColumn("名稱", value.getName(), sb);
					twoColumn("年代", value.getEra(), sb);
					twoColumn("來源地", value.getOrigin(), sb);
					sb.appendHtmlConstant("</div>");
					sb.appendHtmlConstant("</div>");
				}

				twoRow("材質", value.getMaterial(), sb);
				twoRow("尺寸", value.getDimensions(), sb);
				twoRow("描述", value.getDescription(), sb);
				image(value.getImage(), sb);
				twoRow("備註", value.getNote(), sb);
				sb.appendHtmlConstant("<div style='height:8px'></div>");
			}

			private void twoRow(String title, String string, SafeHtmlBuilder sb) {
				if (Strings.isNullOrEmpty(string)) { return; }

				sb.appendHtmlConstant("<div style='" + row1Style + "'>" + title + "：</div>");
				sb.appendHtmlConstant("<div style='" + row2Style + "'>" + string + "</div>");
			}

			private void twoColumn(String title, String string, SafeHtmlBuilder sb) {
				if (Strings.isNullOrEmpty(string)) { return; }

				sb.appendHtmlConstant("<div style='display:table-row;'>");
				sb.appendHtmlConstant("<div style='" + col1Style + "'>" + title + "：</div>");
				sb.appendHtmlConstant("<div style='" + col2Style + "'>" + string + "</div>");
				sb.appendHtmlConstant("</div>");
			}

			private void image(ArrayList<ImageUrl> image, SafeHtmlBuilder sb) {
				if (image.size() == 0) { return; }

				sb.appendHtmlConstant("<div style='" + row1Style + "'>圖片：</div>");
				sb.appendHtmlConstant("<div style='" + row2Style + "'>");
				for (ImageUrl iu : image) {
					sb.appendHtmlConstant(
						"<img style='margin: 0 5px 0 0; cursor: pointer;' src='" + iu.getStandard() + "' " +
						//用開新視窗的方式呈現完整圖
						(iu.getFullSize() == null ? "" : "onclick='window.open(\"" + iu.getFullSize() + "\", \"_blank\", null);'")
						+ " />"
					);
				}
				sb.appendHtmlConstant("</div>");
			}
		}
	);

	public ArtifactGrid() {
		init();
		rowExpander.initPlugin(this);
	}

	public void refresh(List<Artifact> data) {
		getStore().replaceAll(data);
		checkEmpty();

		HashMap<Museum, Selection> selection = new HashMap<>();

		for(Artifact a : data) {
			Museum m = a.getId().getMuseum();
			Selection s = selection.get(m);

			if (s == null) {
				s = new Selection();
				s.setMuseum(m);
				selection.put(m, s);
			}

			s.getUrlId().add(a.getId().getUrlId());
		}

		for (Selection s : selection.values()) {
			DataCenter.wantArtifactM(s);
		}
	}

	public void injectArtifactM(List<ArtifactM> data) {
		for (ArtifactM am : data) {
			Artifact a = getStore().findModelWithKey(am.getId().toString());

			if (a == null) { continue; }	//理論上不會發生 XD

			a.setFromMuseum(am);
		}

		getView().refresh(true);
	}

	@Override
	protected ColumnModel<Artifact> genColumnModel() {
		ColumnConfig<Artifact, Artifact> combo = new ColumnConfig<>(new GetValueProvider<Artifact, Artifact>() {
			@Override
			public Artifact getValue(Artifact object) {
				return object;
			}
		}, 60, "RowNo.");
		combo.setCell(new AbstractCell<Artifact>() {
			@Override
			public void render(Context context, Artifact value, SafeHtmlBuilder sb) {
				String notReadyStyle = value.isReady() ? "" : "color:white;background-color:black";

				sb.appendHtmlConstant("<div style='font-size:14px;text-align:center;justify-content:center;align-items:center;" + notReadyStyle + "'>" + value.getIndex() + "</div>");
			}
		});
		combo.setFixed(true);

		TextButtonCell open = new TextButtonCell();
		open.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				Artifact item = store.get(event.getContext().getIndex());
				Window.open(MuseumUtil.toUrl(item.getId()), "_blank", null);
			}
		});
		ColumnConfig<Artifact, String> urlId = new ColumnConfig<>(properties.urlId(), 100, "網址 ID");
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
		list.add(combo);
		list.add(rowExpander);
		list.add(score);
		list.add(urlId);
		list.add(new ColumnConfig<>(properties.name(), 100, "名稱"));
		list.add(new ColumnConfig<>(properties.era(), 100, "年代"));
		list.add(new ColumnConfig<>(properties.origin(), 100, "來源地"));
		list.add(new ColumnConfig<>(properties.note(), 100, "備註"));
		return new ColumnModel<>(list);
	}

	@Override
	protected ListStore<Artifact> genListStore() {
		ListStore<Artifact> result = new ListStore<>(new ModelKeyProvider<Artifact>() {
			@Override
			public String getKey(Artifact item) {
				return item.getId().toString();
			}
		});
		return result;
	}

	@Override
	protected GridView<Artifact> genGridView() {
		GridView<Artifact> view = new GridView<>();
		view.setForceFit(true);
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
		ValueProvider<Artifact, Boolean> ready();

		@Path("id.urlId")
		ValueProvider<Artifact, String> urlId();

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
