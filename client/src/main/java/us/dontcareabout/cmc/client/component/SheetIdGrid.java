package us.dontcareabout.cmc.client.component;

import java.util.ArrayList;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.sencha.gxt.cell.core.client.TextButtonCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import us.dontcareabout.cmc.client.data.SheetId;
import us.dontcareabout.cmc.client.ui.UiCenter;
import us.dontcareabout.cmc.client.ui.event.RefreshSheetIdStoreEvent;
import us.dontcareabout.cmc.client.ui.event.RefreshSheetIdStoreEvent.RefreshSheetIdStoreHandler;
import us.dontcareabout.gxt.client.component.Grid2;
import us.dontcareabout.gxt.client.model.GetValueProvider;

//XXX from RQC
public class SheetIdGrid extends Grid2<SheetId> {
	private static final Properties properties = GWT.create(Properties.class);
	private final static String js_name = "JS 指定值";
	private static final GetValueProvider<SheetId, Integer> orderVP = new GetValueProvider<SheetId, Integer>() {
		@Override
		public Integer getValue(SheetId object) {
			if (!object.isSelect()) { return Integer.MAX_VALUE; }
			if (SheetId.jsValue() == null) { return 1; }
			return js_name.equals(object.getName()) ? 1 : 2;
		}
	};

	public SheetIdGrid() {
		init();
		view.setForceFit(true);
		getSelectionModel().addSelectionHandler(new SelectionHandler<SheetId>() {
			@Override
			public void onSelection(SelectionEvent<SheetId> event) {
				if (event.getSelectedItem().getId().equals(SheetId.jsValue())) { return; }

				UiCenter.sheetIdSelect(event.getSelectedItem());
			}
		});
		UiCenter.addRefreshSheetIdStore(new RefreshSheetIdStoreHandler() {
			@Override
			public void onRefreshSheetIdStore(RefreshSheetIdStoreEvent event) {
				refresh();
			}
		});
		refresh();
	}

	@Override
	protected ListStore<SheetId> genListStore() {
		ListStore<SheetId> result = new ListStore<>(properties.key());
		result.addSortInfo(new StoreSortInfo<>(orderVP, SortDir.ASC));
		return result;
	}

	@Override
	protected ColumnModel<SheetId> genColumnModel() {
		ColumnConfig<SheetId, Integer> defaultSelect = new ColumnConfig<>(orderVP, 100, "預設開啟順位");
		defaultSelect.setFixed(true);
		defaultSelect.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		defaultSelect.setCell(new AbstractCell<Integer>() {
			@Override
			public void render(Context context, Integer value, SafeHtmlBuilder sb) {
				if (value == Integer.MAX_VALUE) { return; }

				sb.appendHtmlConstant(
					"<div style='margin-left:auto;margin-right:auto;width:16px;height:24px;font-size:14px;color:red;'>"
					+ value
					+ "</div>"
				);
			}
		});

		TextButtonCell reloadBtn = new TextButtonCell();
		reloadBtn.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				UiCenter.toObjectView();
				//FIXME
//				DataCenter.wantQuote(store.get(event.getContext().getIndex()).getId());
			}
		});
		ColumnConfig<SheetId, String> reload = new ColumnConfig<>(new GetValueProvider<SheetId, String>() {
			@Override
			public String getValue(SheetId object) {
				return "載入";
			}
		}, 50, "");
		reload.setFixed(true);
		reload.setCell(reloadBtn);

		ArrayList<ColumnConfig<SheetId, ?>> list = new ArrayList<>();
		list.add(defaultSelect);
		list.add(new ColumnConfig<>(properties.name(), 40, "名稱"));
		list.add(new ColumnConfig<>(properties.id(), 100, "Sheet ID"));
		list.add(reload);
		return new ColumnModel<>(list);
	}

	private void refresh() {
		ArrayList<SheetId> data = SheetId.retrieve();
		String js = SheetId.jsValue();

		if (js != null) {
			SheetId sid = new SheetId(js, js_name, true);
			data.add(sid);
		}

		store.replaceAll(data);
	}

	interface Properties extends PropertyAccess<SheetId> {
		@Path("id") ModelKeyProvider<SheetId> key();
		ValueProvider<SheetId, String> id();
		ValueProvider<SheetId, Boolean> select();
		ValueProvider<SheetId, String> name();
	}
}
