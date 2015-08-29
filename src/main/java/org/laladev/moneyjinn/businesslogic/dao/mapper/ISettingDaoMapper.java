package org.laladev.moneyjinn.businesslogic.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.laladev.moneyjinn.businesslogic.dao.data.SettingData;

/**
 * The MyBatis Mapper handling Setting information stored in the Table <code>settings</code>
 *
 * @author olivleh1
 *
 */
public interface ISettingDaoMapper {
	public SettingData getSetting(@Param("accessId") Long accessId, @Param("name") String name);

	public void setSetting(SettingData settingData);

	public void deleteSettings(Long id);
}
