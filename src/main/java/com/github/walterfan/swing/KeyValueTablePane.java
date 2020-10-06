package com.github.walterfan.swing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Properties;


/**
 * @author walter
 *
 */
public class KeyValueTablePane extends JTable {

	private static final long serialVersionUID = 1L;

	private TableModel model;
	
	private EntrySelectListener selectListener;

	public KeyValueTablePane(String keyCol, String valCol, Properties prop) {
		Object[][] entries = new Object[prop.size()][2];
		int i = 0;
		for (Map.Entry<Object, Object> entry : prop.entrySet()) {
			entries[i] = new Object[] { entry.getKey(), entry.getValue() };
			i++;
		}

		init(new String[]{keyCol, valCol}, entries);
	}
	
	
	public KeyValueTablePane(String keyCol, String valCol, Map<Object, Object> map) {
		Object[][] entries = new Object[map.size()][2];
		int i = 0;
		for (Map.Entry<Object, Object> entry : map.entrySet()) {
			entries[i] = new Object[] { "" + entry.getKey(),
					"" + entry.getValue() };
			i++;
		}

		init(new String[]{keyCol, valCol}, entries);
		this.setBorder(new EmptyBorder(5, 5, 5, 5));

	}

	public String getToolTipText(MouseEvent e) {
        String tip = null;
        java.awt.Point p = e.getPoint();
        int rowIndex = rowAtPoint(p);
        //int colIndex = columnAtPoint(p);
        //int realColumnIndex = convertColumnIndexToModel(colIndex);
        tip = model.getValueAt(rowIndex,1) + ": "+ model.getValueAt(rowIndex,1);
        //tip = super.getToolTipText(e);
        return tip;
    }

	
	public void setSelectListener(EntrySelectListener listener) {
		this.selectListener = listener;
	}
 	
	private void init(String[] arrTitle, Object[][] entries) {
		model = new EditableTableModel(arrTitle, entries);
		
		setModel(model);
		createDefaultColumnsFromModel();
		//setViewportView(table);
		
		 // true by default
        ListSelectionModel rowSM = getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                //Ignore extra messages.
                if (e.getValueIsAdjusting()) return;

                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (!lsm.isSelectionEmpty()){
                    int selectedRow = lsm.getMinSelectionIndex();
                    if(null!=selectListener) {
                    	selectListener.onSelect(model.getValueAt(selectedRow, 0)
                    		, model.getValueAt(selectedRow, 1)) ;
                    }
                }
            }
        });
    
	}
	

}

class EditableTableModel extends AbstractTableModel {
	String[] columnTitles;

	Object[][] dataEntries;

	int rowCount;

	public EditableTableModel(String[] columnTitles, Object[][] dataEntries) {
		this.columnTitles = columnTitles;
		this.dataEntries = dataEntries;
	}

	public int getRowCount() {
		return dataEntries.length;
	}

	public int getColumnCount() {
		return columnTitles.length;
	}

	public Object getValueAt(int row, int column) {
		return dataEntries[row][column];
	}

	public String getColumnName(int column) {
		return columnTitles[column];
	}

	public Class<?> getColumnClass(int column) {
		return getValueAt(0, column).getClass();
	}

	public boolean isCellEditable(int row, int column) {
		return true;
	}

	public void setValueAt(Object value, int row, int column) {
		dataEntries[row][column] = value;
	}
}