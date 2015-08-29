package org.laladev.moneyjinn.businesslogic.model.access;

/**
 * Describes a Group in the System specifically its ID and name.
 *
 * @author olivleh1
 *
 */
public class Group extends AbstractAccess<GroupID> {
	private static final long serialVersionUID = 1L;

	public Group(final GroupID id, final String name) {
		super(id, name);
	}
}
