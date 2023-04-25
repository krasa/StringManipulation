package osmedile.intellij.stringmanip.config;

import javax.swing.*;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.*;

/**
 * @author Vojtech Krasa
 */

class MyDragListener implements DragSourceListener, DragGestureListener {
	JList list;

	DragSource ds = new DragSource();

	public MyDragListener(JList list) {
		this.list = list;
		DragGestureRecognizer dgr = ds.createDefaultDragGestureRecognizer(list, DnDConstants.ACTION_MOVE, this);

	}

	public void dragGestureRecognized(DragGestureEvent dge) {
		StringSelection transferable = new StringSelection(Integer.toString(list.getSelectedIndex()));
		ds.startDrag(dge, DragSource.DefaultMoveDrop, transferable, this);
	}

	public void dragEnter(DragSourceDragEvent dsde) {
	}

	public void dragExit(DragSourceEvent dse) {
	}

	public void dragOver(DragSourceDragEvent dsde) {
	}

	public void dragDropEnd(DragSourceDropEvent dsde) {
	}

	public void dropActionChanged(DragSourceDragEvent dsde) {
	}
}
