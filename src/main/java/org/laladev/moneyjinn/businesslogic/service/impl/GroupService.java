package org.laladev.moneyjinn.businesslogic.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.businesslogic.dao.GroupDao;
import org.laladev.moneyjinn.businesslogic.dao.data.GroupData;
import org.laladev.moneyjinn.businesslogic.dao.data.mapper.GroupDataMapper;
import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.businesslogic.service.CacheNames;
import org.laladev.moneyjinn.businesslogic.service.api.IGroupService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;

@Named
@EnableCaching
public class GroupService extends AbstractService implements IGroupService {

	@Inject
	private GroupDao groupDao;

	@Override
	protected void addBeanMapper() {
		super.registerBeanMapper(new GroupDataMapper());

	}

	@Override
	@Cacheable(CacheNames.ALL_GROUPS)
	public List<Group> getAllGroups() {
		final List<GroupData> groupDataList = this.groupDao.getAllGroups();
		return super.mapList(groupDataList, Group.class);
	}

	@Override
	@Cacheable(CacheNames.GROUP_BY_ID)
	public Group getGroupById(final GroupID groupId) {
		final GroupData groupData = this.groupDao.getGroupById(groupId.getId());
		return super.map(groupData, Group.class);
	}

}
