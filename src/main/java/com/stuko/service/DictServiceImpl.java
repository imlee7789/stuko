package com.stuko.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.stuko.domain.DefinitionDTO;
import com.stuko.domain.DefinitionVO;
import com.stuko.domain.RefSiteDTO;
import com.stuko.domain.RefSiteVO;
import com.stuko.domain.WordDTO;
import com.stuko.domain.WordVO;
import com.stuko.persistence.DefinitionDAO;
import com.stuko.persistence.RefSiteDAO;
import com.stuko.persistence.WordDAO;

@Service
public class DictServiceImpl 
	implements DictService {
	
	@Inject WordDAO wordDAO;
	@Inject DefinitionDAO defDAO;
	@Inject RefSiteDAO refDAO;

	public WordDTO readWord(String courseId, String word_name) 
		throws NullPointerException {
		
		WordDTO wdto = new WordDTO();

		WordVO voIn = new WordVO();
		WordVO wordVoOut = new WordVO();

		voIn.setCourse_id(Integer.parseInt(courseId));
		voIn.setWord_name(word_name);
		wordVoOut = wordDAO.readWord(voIn);

		wdto.setWord_name(wordVoOut.getWord_name());
		wdto.setCourse_id(wordVoOut.getCourse_id());
		

		// List<DefinitionVO> to DefinitionDTO[Array] 
		List<DefinitionVO> dvoList = new ArrayList<DefinitionVO>();
		DefinitionDTO[] ddtos = null;
		WordVO wvo = new WordVO();
		wvo.setWord_name(wdto.getWord_name());
		wvo.setCourse_id(wdto.getCourse_id());
		dvoList = defDAO.readDefs(wvo);
		ddtos = new DefinitionDTO[dvoList.size()];
		
		for (int i=0; i<ddtos.length; i++) {
			DefinitionVO dvo = dvoList.get(i);
			ddtos[i] = new DefinitionDTO();
			
			ddtos[i].setId(dvo.getId());
			ddtos[i].setContent(dvo.getContent());
			ddtos[i].setInsert_ts(dvo.getInsert_ts());
			ddtos[i].setRcmd_cnt(dvo.getRcmd_cnt());

			// List<RefSiteVO> to RefSiteDTO[Array] 
			List<RefSiteVO> refvoList = new ArrayList<RefSiteVO>();
			RefSiteDTO[] refdtos = null;
			
			try {
				refvoList = refDAO.readRefSites(ddtos[i].getId());
				refdtos = new RefSiteDTO[refvoList.size()];
				
				for (int j=0; j<refvoList.size(); j++) {
					RefSiteVO refvo = refvoList.get(j);
					refdtos[j] = new RefSiteDTO();
					refdtos[j].setDescription(refvo.getDescription());
					refdtos[j].setId(refvo.getId());
					refdtos[j].setUrl(refvo.getUrl());
				}

			} catch (NullPointerException e) {
				;;
			}
			
			ddtos[i].setRefs(refdtos);
		}

		wdto.setDefs(ddtos);
		
		return wdto;
	}

	// 특정 단어에 def를 추가한다.
	// param
	// - courseId
	// - word_name
	// - DefinitionDTO
	public void createNewDef(
			String courseId, 
			String word_name, 
			DefinitionDTO defdto)
		{
//		어떤 Exception이 발생할 수 있는가?
//		throws Exception {

		int course_id = Integer.parseInt(courseId);
		
		String content = defdto.getContent();
		String login_id = defdto.getLogin_id();
		
		// 1. Create word if not exist
		WordVO voIn = new WordVO();
		WordVO wordVoOut = new WordVO();
		
		voIn.setWord_name(word_name);
		voIn.setCourse_id(course_id);
		wordVoOut = wordDAO.readWord(voIn);

		if(wordVoOut==null) {
			wordDAO.createWord(voIn);
			wordVoOut = wordDAO.readWord(voIn);
		}
		
		DefinitionVO dvo = new DefinitionVO();
		dvo.setWord_id(wordVoOut.getId());
		dvo.setContent(content);
		dvo.setLogin_id(login_id);

		defDAO.insertDef(dvo);
		
		
		// 2. Insert Ref-sites if defdto.refs is exists
		RefSiteDTO[] refs = defdto.getRefs();
		if (refs == null || refs.length == 0) {
			return;
		}

		//방금 추가한 아이디
		int def_id = defDAO.getLastInsertedId();
		
		for (RefSiteDTO refdto : refs) {
			RefSiteVO vo = new RefSiteVO();
			vo.setDef_id(def_id);
			vo.setUrl(refdto.getUrl());
			vo.setDescription(refdto.getDescription());
			
			refDAO.insertRefSite(vo);
		}
		
	}
	
	public void replaceDef(int defId, DefinitionDTO defdto) 
		throws Exception {
		
		DefinitionVO def = defDAO.readDef(defId);
		
		if (!def.getLogin_id().equals(defdto.getLogin_id())) {
			throw new Exception("DictService::replaceDef() login_id mismatched");
		}
		
		RefSiteDTO[] refs = defdto.getRefs();
		
		DefinitionVO dvoReplacement = new DefinitionVO();
		dvoReplacement.setId(defId);
		dvoReplacement.setContent(defdto.getContent());
		defDAO.updateDef(dvoReplacement);
		
		for (int i=0; i<refs.length; i++) {
			RefSiteVO refvo = new RefSiteVO();
			int refid = refs[i].getId();
			if(refid==0) {
				throw new Exception("DictService::replaceDef() ref_id is 0");
			}
			
			refvo.setId(refid);
			refvo.setDescription(refs[i].getDescription());
			refvo.setUrl(refs[i].getUrl());
			
			refDAO.updateRefSite(refvo);
		}
		
	}
	
	public void deleteDef(int defId, DefinitionDTO defdto) 
		throws Exception {
		DefinitionVO defvo = defDAO.readDef(defId);

		if (!defvo.getLogin_id().equals(defdto.getLogin_id())) {
			throw new Exception("DictService::deleteDef() login_id is mismatched");
		}
		
		defDAO.deleteDef(defId);
		
	}
	
}
