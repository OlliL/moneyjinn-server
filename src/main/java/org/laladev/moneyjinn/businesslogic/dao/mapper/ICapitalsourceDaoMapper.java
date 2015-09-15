package org.laladev.moneyjinn.businesslogic.dao.mapper;

import java.sql.Date;

//Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without
//modification, are permitted provided that the following conditions
//are met:
//1. Redistributions of source code must retain the above copyright
//notice, this list of conditions and the following disclaimer
//2. Redistributions in binary form must reproduce the above copyright
//notice, this list of conditions and the following disclaimer in the
//documentation and/or other materials provided with the distribution.
//
//THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
//ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
//FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
//DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
//OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
//HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
//LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
//OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
//SUCH DAMAGE.

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.laladev.moneyjinn.businesslogic.dao.data.CapitalsourceData;

public interface ICapitalsourceDaoMapper {
	public List<CapitalsourceData> getAllCapitalsources(@Param("userId") Long userId);

	public List<CapitalsourceData> getAllCapitalsourcesByDateRange(@Param("userId") Long userId,
			@Param("validFrom") Date validFrom, @Param("validTil") Date validTil);

	public CapitalsourceData getCapitalsourceById(@Param("userId") Long userId, @Param("accessorId") Long accessorId,
			@Param("id") Long id);

	public Integer countAllCapitalsources(@Param("userId") Long userId);

	public Integer countAllCapitalsourcesByDateRange(@Param("userId") Long userId, @Param("validFrom") Date validFrom,
			@Param("validTil") Date validTil);

	public Set<Character> getAllCapitalsourceInitials(@Param("userId") Long userId);

	public Set<Character> getAllCapitalsourceInitialsByDateRange(@Param("userId") Long userId,
			@Param("validFrom") Date validFrom, @Param("validTil") Date validTil);

	public List<CapitalsourceData> getAllCapitalsourcesByInitial(@Param("userId") Long userId,
			@Param("initial") Character initial);

	public List<CapitalsourceData> getAllCapitalsourcesByInitialAndDateRange(@Param("userId") Long userId,
			@Param("initial") Character initial, @Param("validFrom") Date validFrom, @Param("validTil") Date validTil);

	public CapitalsourceData getCapitalsourceByComment(@Param("userId") Long userId, @Param("comment") String comment,
			@Param("date") Date date);

	public void createCapitalsource(CapitalsourceData capitalsourceData);

	public void updateCapitalsource(CapitalsourceData capitalsourceData);

	public void deleteCapitalsource(@Param("userId") Long userId, @Param("accessorId") Long accessorId,
			@Param("id") Long id);

	public Boolean checkCapitalsourceInUseOutOfDate(@Param("userId") Long userId, @Param("id") Long id,
			@Param("validFrom") Date validFrom, @Param("validTil") Date validTil);

	public List<CapitalsourceData> getGroupCapitalsources(@Param("userId") Long userId);

	public List<CapitalsourceData> getGroupCapitalsourcesByDateRange(@Param("userId") Long userId,
			@Param("validFrom") Date validFrom, @Param("validTil") Date validTil);

}