package org.laladev.moneyjinn.businesslogic.service.api;

import java.util.List;

import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;

/**
 * <p>
 * GroupService is the Domain Service handling everything around an {@link Group}.
 * </p>
 *
 * <p>
 * GroupService is the Domain Service handling operations around an {@link Group} like getting,
 * creating, updating, deleting. Before ab {@link Group} is created or updated, the {@link Group} is
 * validated for correctness.
 * </p>
 * <p>
 * The main datasource is the Table <code>access</code>.
 * </p>
 *
 * @author Oliver Lehmann
 * @since 0.0.1
 *
 */
public interface IGroupService {
	/**
	 * Returns all {@link Group}s.
	 *
	 * @return
	 */
	public List<Group> getAllGroups();

	/**
	 * Returns the {@link Group} for the given {@link GroupID}.
	 *
	 * @param groupId
	 *            the {@link GroupID}
	 * @return
	 */
	public Group getGroupById(GroupID groupId);
}
