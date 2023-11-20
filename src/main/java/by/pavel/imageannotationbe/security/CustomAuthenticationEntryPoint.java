package by.pavel.imageannotationbe.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@SecurityEnabled
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String[] ALLOWED_PATHS = {"/auth", "/register"};

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {
        String requestPath = request.getRequestURI().substring(request.getContextPath().length());

        // Check if the request path is allowed
        for (String allowedPath : ALLOWED_PATHS) {
            if (requestPath.equals(allowedPath)) {
                // The request path is allowed, proceed with the request
                request.getRequestDispatcher(requestPath).forward(request, response);
                return;
            }
        }

        // Check if the authorization header is given
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            // The authorization header is not given, return 401 Unauthorized
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        } else {
            // The authorization header is given, but the user's rights are insufficient, return 403 Forbidden
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
        }
    }
}
