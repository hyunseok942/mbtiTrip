package com.example.test.POST.Service;




import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


//import com.example.test.HashTag.TagService;
import com.example.test.POST.DAO.PostDAO;

import com.example.test.POST.DTO.PostDTO;
import com.example.test.POST.DTO.Post_CategoryDTO;
import com.example.test.User.DTO.UserDTO;
import com.example.test.paging.Criteria;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;






@Service
public  class PostServiceImpl implements PostService {

	@Autowired
	PostDAO postDAO;
	

	//@Autowired
	//TagService tagService;


	//해당게시글 가져옴, 조회수 증가
	@Override
	public PostDTO getPost(Integer userid) {
		 Optional<PostDTO> post = this.postDAO.findById(userid);
		  if (post.isPresent()) {
	        	PostDTO postDto = post.get();        	
	        	postDto.setViews(postDto.getViews()+1);        	
	        	this.postDAO.save(postDto);
	            	return postDto;
	        } else {
	            throw new DataNotFoundException("question not found");
	        }	
	}
	
	//생성
	@Override
	public int create(String title, String content, UserDTO user, Post_CategoryDTO category) {
		PostDTO postDto = new PostDTO();
        postDto.setTitle(title);
        postDto.setContent(content);
        postDto.setPost_category(category);
        postDto.setUpdateDate(LocalDateTime.now());
        postDto.setWriter(user);
        
       
        
        // 생성된 post 객체에서 태그 리스트 생성하기

       // tagService.createTagList(postDto);

        
        return this.postDAO.save(postDto);
	}

	//수정
	@Override
	public int modify(PostDTO postDto, String title, String content) {
		postDto.setTitle(title);
        postDto.setContent(content);
        postDto.setModifyDate(LocalDateTime.now());
        
        
        return this.postDAO.save(postDto);
	}

	//삭제
	@Override
	public void delete(PostDTO postDto) {
		 this.postDAO.delete(postDto);
		
		//tagService.deleteTagPost(postDto);
	}

	//추천
	@Override
	public int suggestion(PostDTO postDto, UserDTO userDto) {
		postDto.getSuggestion().add(userDto);
        
        return this.postDAO.save(postDto);
	}

	// 페이징 처리된 게시물 목록을 반환	
	@Override
	public List<PostDTO> list(Criteria cri) throws Exception {

		return postDAO.list(cri);
	}

	// 페이지 수를 계산하기 위해 사용
	@Override
	public int listCount(Criteria cri) throws Exception {
		// TODO Auto-generated method stub
		return postDAO.listCount(cri);
	}
	
	// 게시물을 조회하고 조회수 증가
		@Transactional
		public PostDTO detail(Integer postID, HttpServletRequest request, HttpServletResponse response) {
			 Cookie oldCookie = null;
			    Cookie[] cookies = request.getCookies();
			    if (cookies != null)
			        for (Cookie cookie : cookies)
			            if (cookie.getName().equals("postView"))
			                oldCookie = cookie;

			    if (oldCookie != null) {
			        if (!oldCookie.getValue().contains("[" + postID.toString() + "]")) {
			            postDAO.updateCount(postID);
			            oldCookie.setValue(oldCookie.getValue() + "_[" + postID + "]");
			            oldCookie.setPath("/");
			            oldCookie.setMaxAge(60 * 60 * 24);
			            response.addCookie(oldCookie);
			        }
			    }
			    else {
			        postDAO.updateCount(postID);
			        Cookie newCookie = new Cookie("postView","[" + postID + "]");
			        newCookie.setPath("/");
			        newCookie.setMaxAge(60 * 60 * 24);
			        response.addCookie(newCookie);
			    }

			    return postDAO.findById(postID).orElseThrow(() -> {
			        return new IllegalArgumentException("글 상세보기 실패: 아이디를 찾을 수 없습니다.");
			    });
			}

	
	





	
	
    

}
