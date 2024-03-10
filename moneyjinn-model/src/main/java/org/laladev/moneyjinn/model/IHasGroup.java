package org.laladev.moneyjinn.model;

import org.laladev.moneyjinn.model.access.Group;

public interface IHasGroup {
	Group getGroup();

	void setGroup(Group group);
}
