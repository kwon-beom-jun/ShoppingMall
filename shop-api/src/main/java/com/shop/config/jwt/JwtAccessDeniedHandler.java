package com.shop.config.jwt;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** [인가] 403 Forbidden 필요한 권한이 존재하지 않는 경우 에러를 리턴하기 위함 */
// AccessDeniedHandler : 권한 부여 과정에서 발생하는 접근 거부(즉, 사용자가 필요한 권한이 없는 자원에 접근하려고 할 때) 처리하고
//                       사용자가 요청한 작업을 수행할 권한이 없을 때 수행되는 로직을 정의
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    // handle : 권한 부여 실패가 발생했을 때 호출되며, HttpServletRequest, HttpServletResponse,
    //          그리고 권한 부여 실패 원인을 나타내는 AccessDeniedException을 매개변수로 받음.
    //          권한 부여 실패 시 사용자에게 반환할 응답을 정의
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        //필요한 권한이 없이 접근하려 할때 403
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        // JSON 형태로 에러 메시지 전송
        response.getWriter().write("{\"error\": \"" + accessDeniedException.getMessage() + "\"}");
    }
}
