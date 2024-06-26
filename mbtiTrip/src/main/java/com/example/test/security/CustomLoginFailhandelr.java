package com.example.test.security;

import java.io.IOException;
import java.net.URLEncoder;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.testExcepion.login.LoginException;
import com.example.testExcepion.login.LoginExceptionEnum;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javassist.expr.Instanceof;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class CustomLoginFailhandelr implements AuthenticationFailureHandler  {

	/**@author ShinSungjin
	 * Security에 사용 될  CustomFailerhander입니다입니다. 
	 * 로그인이 실패 했을 경우에 실행됩니다.
	 * */
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException, LoginException {
		// TODO Auto-generated method stub
		
		String errorMessage;
		
		//다양한 로그인 실패에 대한 예외처리를 정합니다.
		if(exception instanceof BadCredentialsException) {
			throw new LoginException(LoginExceptionEnum.LOGIN_PASSWORD_MISMATCH);
		}
	
		else if(exception instanceof  InternalAuthenticationServiceException ) {
			errorMessage = "사용자 계정을 찾을 수 없습니다. 아이디와 비밀번호를 다시 확인해주세요";
		
		}
		else if (exception instanceof UsernameNotFoundException) {
			errorMessage = "존재하지 않는 계정입니다. 회원가입 후 로그인해주세요.";
		
		} else if (exception instanceof AuthenticationCredentialsNotFoundException) {
			errorMessage = "정지된 사용자입니다. 관리자에게 문의하세요.";
			
		}
		else {
//			errorMessage = "알 수 없는 오류로 로그인 요청을 처리할 수 없습니다. 관리자에게 문의하세요.";
			errorMessage  =  exception.getClass().toString();
			
		}
		
		 /* 한글 인코딩 깨진 문제 방지 */
		
		log.info("catch FailHandelr {}", errorMessage);
		response.sendError(response.getStatus(), errorMessage);
		
	}
	
	
}


