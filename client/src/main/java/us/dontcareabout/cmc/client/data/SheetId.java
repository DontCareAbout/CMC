package us.dontcareabout.cmc.client.data;

import java.util.ArrayList;

import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.storage.client.Storage;

/**
 * 包含了系統如何決定 sheet id 的邏輯（{@link #get()}）、
 * 存取 local storage 內 sheet id 的 static method、
 * 以及定義 SheetId 這個 VO 的資料結構。
 */
//XXX from RQC
public class SheetId {
	private static Mapper mapper = GWT.create(Mapper.class);
	private static Storage storage = Storage.getLocalStorageIfSupported();
	private static final String KEY = "SheetId";

	private String id;
	private String name;
	private boolean select;

	public SheetId() {}

	public SheetId(String id, String name, boolean select) {
		this.id = id;
		this.name = name;
		this.select = select;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SheetId other = (SheetId) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	interface Mapper extends ObjectMapper<ArrayList<SheetId>> {}

	/**
	 * 預設的 sheet id。
	 * <p>
	 * 如果 host page 有給值則第一優先，
	 * 若無則從 local storage 中取第一個（理論上也只有一個） {@link #isSelect()} 為 true 的 id 值。
	 * 如果 local storage 沒有資料或是都沒指定 {@link #isSelect()}，則給 sample sheet。
	 */
	public static String get() {
		String js = jsValue();

		if (js != null) { return js; }

		for (SheetId id : retrieve()) {
			if (id.isSelect()) { return id.getId(); }
		}

		return "1rr293klEVOjHUKiKgAotCpXBWYIASsCMZEZEVfkanP4";
	}

	public static ArrayList<SheetId> retrieve() {
		String json = storage.getItem(KEY);
		return json == null ? new ArrayList<SheetId>() : mapper.read(json);
	}

	public static void store(ArrayList<SheetId> list) {
		storage.setItem(KEY, mapper.write(list));
	}

	public static void clear() {
		storage.removeItem(KEY);
	}

	public static native String jsValue() /*-{
		return $wnd.sheetId;
	}-*/;
}