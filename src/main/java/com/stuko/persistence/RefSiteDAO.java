package com.stuko.persistence;

import java.util.List;

import com.stuko.domain.RefSiteVO;

public interface RefSiteDAO {

	public abstract List<RefSiteVO> readRefSites(int def_id) throws NullPointerException;

	public abstract void insertRefSite(RefSiteVO vo);
	
	public abstract void updateRefSite(RefSiteVO vo);
	
	public abstract void deleteRefSite(int id);
	
}
