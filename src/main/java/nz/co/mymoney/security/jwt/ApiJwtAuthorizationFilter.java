package nz.co.mymoney.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class ApiJwtAuthorizationFilter extends OncePerRequestFilter {

    private final String apiUrlPrefix;
    private final JwtUtil jwtUtil;
    private final ObjectMapper mapper;

    public ApiJwtAuthorizationFilter(String apiUrlPrefix, JwtUtil jwtUtil, ObjectMapper mapper) {
        this.apiUrlPrefix = apiUrlPrefix;
        this.jwtUtil = jwtUtil;
        this.mapper = mapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.resolveToken(request);
        String error = "Invalid Token";
        try {
            if (accessToken != null) {
                Claims claims = jwtUtil.resolveClaims(request);

                if (claims != null & jwtUtil.validateClaims(claims)) {
                    String email = claims.getSubject();
                    String role = (String) claims.get("role");

                    Authentication authentication = new UsernamePasswordAuthenticationToken(email, "", Arrays.asList(roleToGrantedAuthority(role)));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        } catch (Exception ex) {
            error = ex.getMessage();
        }

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getWriter(), error);
    }

    private GrantedAuthority roleToGrantedAuthority(String role) {
        return () -> role;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String url = request.getRequestURI();
        return !url.startsWith(apiUrlPrefix);
    }
}