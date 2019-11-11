package com.stuko.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.stuko.domain.RefSiteVO;

@Repository
public class RefSiteDAOImpl 
	implements RefSiteDAO {

	@Inject
	private SqlSession session;

	private static String namespace = "com.stuko.mapper.RefSiteMapper";
	
	@Override
	public List<RefSiteVO> readRefSites(int def_id) throws NullPointerException {
		List<RefSiteVO> refs = new ArrayList<>();
		refs = session.selectList(namespace + ".readRefSites", def_id);
		return refs;
	}

	@Override
	public void insertRefSite(RefSiteVO vo) {
		session.insert(namespace + ".insertRefSite", vo);
	}

	@Override
	public void updateRefSite(RefSiteVO vo) {
		session.update(namespace + ".updateRefSite", vo);
	}

	@Override
	public void deleteRefSite(int id) {
		session.delete(namespace + ".deleteRefSite", id);
	}

}
