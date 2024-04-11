package com.example.test.replace.Service;


import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.test.POST.DTO.PostDTO;
import com.example.test.POST.Service.DataNotFoundException;
import com.example.test.User.DTO.AdminDTO;
import com.example.test.User.DTO.UserDTO;
import com.example.test.User.Service.UserHistoryService;
import com.example.test.item.ItemType;
import com.example.test.item.DAO.ItemDAO;
import com.example.test.item.DTO.ItemDTO;
import com.example.test.paging.Criteria;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;



@Service
public class ReplaceServiceImpl implements ReplaceService{

	@Autowired
	ItemDAO itemDAO;

	
	@Autowired
	UserHistoryService userHistoryService;
	
	@Override
	public List<ItemDTO> getList(Criteria criteria) {
		// TODO Auto-generated method stub
		return itemDAO.replaceList(criteria);
	}

	@Override
	public int getTotal(Criteria cri) {
		// TODO Auto-generated method stub
		return itemDAO.getTotal(cri);
	}

	@Override
	public ItemDTO getPost(Integer itemid) {
		Optional<ItemDTO> replace = this.itemDAO.findById(itemid);
		  if (replace.isPresent()) {
	        	ItemDTO itemDto = replace.get();        	
	        	itemDto.setView(itemDto.getView()+1);        	
	        	this.itemDAO.create(itemDto);
	            	return itemDto;
	        } else {
	            throw new DataNotFoundException("question not found");
	        }	
	}

	public int create(ItemDTO itemDTO) {
			
		itemDTO.setUpdateDate(LocalDateTime.now());
		
		return this.itemDAO.create(itemDTO);
	}

	@Override
	public int modify(ItemDTO itemdto,ItemType Type, Integer mbti, Integer price, 
			 String itemName,String location, String tel, String contents, String[] ImgeUrl) {
		itemdto.setType(Type);
		itemdto.setMbti(mbti);
		itemdto.setPrice(price);
		itemdto.setItemName(itemName);
		itemdto.setLocation(location);
		itemdto.setTel(tel);
		itemdto.setContents(contents);
		itemdto.setImgeUrl(ImgeUrl);
		itemdto.setModifyDate(LocalDateTime.now());
		
		return this.itemDAO.update(itemdto);
	}

	@Override
	public void delete(ItemDTO itemDto) {
		this.itemDAO.deleteItem(itemDto);
		
	}

	@Override
	public int suggestion(ItemDTO itemDto, UserDTO user) {
		itemDto.getUprating().add(user);
        
        return this.itemDAO.create(itemDto);
	}



			// 게시물을 조회하고 조회수 증가
			@Transactional
			public ItemDTO detail(Integer itemID, HttpServletRequest request, HttpServletResponse response) {
				 Cookie oldCookie = null;
				    Cookie[] cookies = request.getCookies();
				    if (cookies != null)
				        for (Cookie cookie : cookies)
				            if (cookie.getName().equals("ReplaceView"))
				                oldCookie = cookie;

				    if (oldCookie != null) {
				        if (!oldCookie.getValue().contains("[" + itemID.toString() + "]")) {
				            itemDAO.updateCount(itemID);
				            oldCookie.setValue(oldCookie.getValue() + "_[" + itemID + "]");
				            oldCookie.setPath("/");
				            oldCookie.setMaxAge(60 * 60 * 24);
				            response.addCookie(oldCookie);
				        }
				    }
				    else {
				        itemDAO.updateCount(itemID);
				        Cookie newCookie = new Cookie("ReplaceView","[" + itemID + "]");
				        newCookie.setPath("/");
				        newCookie.setMaxAge(60 * 60 * 24);
				        response.addCookie(newCookie);
				    }

				    return itemDAO.findById(itemID).orElseThrow(() -> {
				        return new IllegalArgumentException("글 상세보기 실패: 아이디를 찾을 수 없습니다.");
				    });
				}

		
			@Override
			public void setRating(Integer itemID) {
				// TODO Auto-generated method stub
				ItemDTO item = new ItemDTO();
				
				Double ratingAvg = itemDAO.getRatingAverage(itemID);
				
				item.setRatingAvg(ratingAvg);
				
				this.itemDAO.updateRating(item);
				
				
			
			}

			@Override
			public ItemDTO getPost(Integer itemID, Principal principal) {
				Optional<ItemDTO> replace = this.itemDAO.findById(itemID);
				  if (replace.isPresent()) {
			        	ItemDTO itemDto = replace.get();        	
			        	itemDto.setView(itemDto.getView()+1);        	
			        	this.itemDAO.create(itemDto);
			        	userHistoryService.userViewItem(itemDto, principal);
			        	return itemDto;
			        } else {
			            throw new DataNotFoundException("question not found");
			        }	
			}

			@Override
			public int getLastInsertID() {
				int result = itemDAO.getLastInsertID();
				return result;
			}


		
	
	
	
	
}
