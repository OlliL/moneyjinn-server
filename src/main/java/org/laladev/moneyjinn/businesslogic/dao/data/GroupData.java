package org.laladev.moneyjinn.businesslogic.dao.data;

public class GroupData {
	private Long id;
	private String name;

	public GroupData(final Long id, final String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public final Long getId() {
		return id;
	}

	public final void setId(final Long id) {
		this.id = id;
	}

	public final String getName() {
		return name;
	}

	public final void setName(final String name) {
		this.name = name;
	}
}
