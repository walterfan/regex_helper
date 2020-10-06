package com.github.walterfan.swing;

import java.util.EventListener;

public interface EntrySelectListener extends EventListener {
	void onSelect(Object key, Object value);
}
