package model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class StringList extends ArrayList<String>  implements ListModel<String> {
	private static final long serialVersionUID = 1L;
	private List<ListDataListener> observers = new ArrayList<ListDataListener>();
	
	@Override
	public boolean add(String e) {
		try {
			return super.add(e);
		} finally {
			fireContentsChanged();
		}
	}

	@Override
	public int getSize() {
		return this.size();
	}

	@Override
	public String getElementAt(int index) {
		return this.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		observers.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		observers.remove(l);
	}
	
	protected void fireContentsChanged() {
        for (ListDataListener l : observers) {
        	l.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, this.getSize()-1));
        }
    }
}
