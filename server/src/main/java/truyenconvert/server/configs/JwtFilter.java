package truyenconvert.server.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.lang.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import truyenconvert.server.commons.ResponseError;
import truyenconvert.server.modules.jwt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Value("${truyencv.api-prefix}")
    private String apiPrefix;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


    public JwtFilter(JwtService jwtService, UserDetailsService userDetailsService){
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    private boolean isRequestPassed(@NonNull HttpServletRequest request){
        final List<Pair<String,String>> listEnpointPassed = Arrays.asList(
                Pair.of(String.format("%s/auth/sign-in",apiPrefix),"POST"),
                Pair.of(String.format("%s/auth/sign-up",apiPrefix),"POST"),
                Pair.of(String.format("%s/auth/sign-out",apiPrefix),"POST")
        );

        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();

        Pair<String,String> currentRequest = Pair.of(requestPath,requestMethod);

        return listEnpointPassed.contains(currentRequest);
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try{

            if(this.isRequestPassed(request)){
                filterChain.doFilter(request,response);
                return;
            }

            final String authHeader = request.getHeader("Authorization");
            final String jwt;

            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                sendErrorFilter(response,HttpStatus.BAD_REQUEST, "Bạn chưa đăng nhập");
                return;
            }
            jwt = authHeader.substring(7);
            String userName = jwtService.extractUsername(jwt);

            if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails user = userDetailsService.loadUserByUsername(userName);
                if(!user.isAccountNonLocked()){
                    throw new LockedException("Tài khoản đã bị khóa");
                }

                if(jwtService.isTokenValid(jwt,user)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request,response);
        }catch(AccessDeniedException ex){
            sendErrorFilter(response,HttpStatus.FORBIDDEN,"Access Denined");
        }
        catch(LockedException ex){
            sendErrorFilter(response,HttpStatus.FORBIDDEN,"Account has been locked");
        }
        catch(ExpiredJwtException ex){
            sendErrorFilter(response,HttpStatus.UNAUTHORIZED,"The entered session expires");
        }
    }

    private void sendErrorFilter(@NonNull HttpServletResponse response, HttpStatus httpStatus, String message) throws IOException {
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");

        ResponseError errorResponse = new ResponseError(httpStatus,httpStatus.value(),message);

        String errorEncode = new ObjectMapper().writeValueAsString(errorResponse);

        PrintWriter out = response.getWriter();
        out.print(errorEncode);
        out.flush();
    }

}
