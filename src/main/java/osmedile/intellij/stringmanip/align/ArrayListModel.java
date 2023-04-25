//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package osmedile.intellij.stringmanip.align;

import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * from jgoodies
 */
public class ArrayListModel<E> extends ArrayList<E> implements ObservableList<E> {
	private EventListenerList listenerList;

	public ArrayListModel() {
		this(10);
	}

	public ArrayListModel(int initialCapacity) {
		super(initialCapacity);
	}

	public ArrayListModel(Collection<? extends E> c) {
		super(c);
	}

	public final void add(int index, E element) {
		super.add(index, element);
		this.fireIntervalAdded(index, index);
	}

	public final boolean add(E e) {
		int newIndex = this.size();
		super.add(e);
		this.fireIntervalAdded(newIndex, newIndex);
		return true;
	}

	public final boolean addAll(int index, Collection<? extends E> c) {
		boolean changed = super.addAll(index, c);
		if (changed) {
			int lastIndex = index + c.size() - 1;
			this.fireIntervalAdded(index, lastIndex);
		}

		return changed;
	}

	public final boolean addAll(Collection<? extends E> c) {
		int firstIndex = this.size();
		boolean changed = super.addAll(c);
		if (changed) {
			int lastIndex = firstIndex + c.size() - 1;
			this.fireIntervalAdded(firstIndex, lastIndex);
		}

		return changed;
	}

	public boolean removeAll(Collection<?> c) {
		boolean modified = false;
		Iterator e = this.iterator();

		while (e.hasNext()) {
			if (c.contains(e.next())) {
				e.remove();
				modified = true;
			}
		}

		return modified;
	}

	public boolean retainAll(Collection<?> c) {
		boolean modified = false;
		Iterator e = this.iterator();

		while (e.hasNext()) {
			if (!c.contains(e.next())) {
				e.remove();
				modified = true;
			}
		}

		return modified;
	}

	public final void clear() {
		if (!this.isEmpty()) {
			int oldLastIndex = this.size() - 1;
			super.clear();
			this.fireIntervalRemoved(0, oldLastIndex);
		}
	}

	public final E remove(int index) {
		E removedElement = super.remove(index);
		this.fireIntervalRemoved(index, index);
		return removedElement;
	}

	public final boolean remove(Object o) {
		int index = this.indexOf(o);
		boolean contained = index != -1;
		if (contained) {
			this.remove(index);
		}

		return contained;
	}

	protected final void removeRange(int fromIndex, int toIndex) {
		super.removeRange(fromIndex, toIndex);
		this.fireIntervalRemoved(fromIndex, toIndex - 1);
	}

	public final E set(int index, E element) {
		E previousElement = super.set(index, element);
		this.fireContentsChanged(index, index);
		return previousElement;
	}

	public final void addListDataListener(ListDataListener l) {
		this.getEventListenerList().add(ListDataListener.class, l);
	}

	public final void removeListDataListener(ListDataListener l) {
		this.getEventListenerList().remove(ListDataListener.class, l);
	}

	public final Object getElementAt(int index) {
		return this.get(index);
	}

	public final int getSize() {
		return this.size();
	}

	public final void fireContentsChanged(int index) {
		this.fireContentsChanged(index, index);
	}

	public final ListDataListener[] getListDataListeners() {
		return (ListDataListener[]) this.getEventListenerList().getListeners(ListDataListener.class);
	}

	private void fireContentsChanged(int index0, int index1) {
		Object[] listeners = this.getEventListenerList().getListenerList();
		ListDataEvent e = null;

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ListDataListener.class) {
				if (e == null) {
					e = new ListDataEvent(this, 0, index0, index1);
				}

				((ListDataListener) listeners[i + 1]).contentsChanged(e);
			}
		}

	}

	private void fireIntervalAdded(int index0, int index1) {
		Object[] listeners = this.getEventListenerList().getListenerList();
		ListDataEvent e = null;

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ListDataListener.class) {
				if (e == null) {
					e = new ListDataEvent(this, 1, index0, index1);
				}

				((ListDataListener) listeners[i + 1]).intervalAdded(e);
			}
		}

	}

	private void fireIntervalRemoved(int index0, int index1) {
		Object[] listeners = this.getEventListenerList().getListenerList();
		ListDataEvent e = null;

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ListDataListener.class) {
				if (e == null) {
					e = new ListDataEvent(this, 2, index0, index1);
				}

				((ListDataListener) listeners[i + 1]).intervalRemoved(e);
			}
		}

	}

	private EventListenerList getEventListenerList() {
		if (this.listenerList == null) {
			this.listenerList = new EventListenerList();
		}

		return this.listenerList;
	}
}
