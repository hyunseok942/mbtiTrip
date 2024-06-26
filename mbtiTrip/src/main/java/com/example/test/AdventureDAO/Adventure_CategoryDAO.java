package com.example.test.AdventureDAO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.test.Adventure.DTO.Adventure_CategoryDTO;
import com.example.test.POST.DTO.Post_CategoryDTO;

@Repository
public class Adventure_CategoryDAO {

	@Autowired
	SqlSessionTemplate sqlSessiontemplate ;
	
	public int insert(Map<String, Object> Adventure_Category) {
		int result = this.sqlSessiontemplate.insert("adventure_category.insert",Adventure_Category);
		return result;
	}
	


	public List<Adventure_CategoryDTO> getList() {
		// TODO Auto-generated method stub
		return sqlSessiontemplate.selectList("adventure_category.getList");
	}



	public Adventure_CategoryDTO getCategory(Integer category) {
		// TODO Auto-generated method stub
		return sqlSessiontemplate.selectOne("adventure_category.getCategory", category);
	}
}
