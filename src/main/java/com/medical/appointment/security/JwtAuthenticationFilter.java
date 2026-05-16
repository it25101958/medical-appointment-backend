package com.medical.appointment.security;

import com.medical.appointment.model.Admin;
import com.medical.appointment.model.User;
import com.medical.appointment.model.enums.UserRole;
import com.medical.appointment.repository.AdminRepository;
import com.medical.appointment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = getTokenFromRequest(request);

        if (token != null && jwtTokenUtil.validateToken(token)) {
            String email = jwtTokenUtil.getUsernameFromToken(token);

            userRepository.findByEmail(email).ifPresent(user -> {
                if (Boolean.TRUE.equals(user.getIsActive())) {
                    List<SimpleGrantedAuthority> authorities = buildAuthorities(user);

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    user.getEmail(),
                                    null,
                                    authorities
                            );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            });
        }

        filterChain.doFilter(request, response);
    }

    private List<SimpleGrantedAuthority> buildAuthorities(User user) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        UserRole role = UserRole.fromInt(user.getRoleType());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));

        if (role == UserRole.ADMIN) {
            adminRepository.findById(user.getUserId())
                    .map(Admin::getAccessLevel)
                    .ifPresent(accessLevel ->
                            authorities.add(
                                    new SimpleGrantedAuthority("ROLE_" + accessLevel.name())
                            )
                    );
        }

        return authorities;
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }

        return null;
    }
}