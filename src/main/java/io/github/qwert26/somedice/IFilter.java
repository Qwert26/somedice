package io.github.qwert26.somedice;

import io.github.qwert26.somedice.util.IntIntPredicate;

public interface IFilter {
	void setFilter(IntIntPredicate filter);

	IntIntPredicate getFilter();
}
