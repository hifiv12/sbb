package com.mysite.sbb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mysite.sbb.config.UserRole;
import com.mysite.sbb.domain.SiteUser;
import com.mysite.sbb.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Service
@Log4j2
public class UserSecurityService implements UserDetailsService{

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Optional<SiteUser> _siteuser = this.userRepository.findByUsername(username);
    
    if(_siteuser.isEmpty()) {
      throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
    }

    SiteUser siteUser = _siteuser.get();
    List<GrantedAuthority> authorities = new ArrayList<>();
    
    if("admin".equals(username)) {
      authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
    } else {
      authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
    }
    log.info("  !!UserSecurity!! : " + siteUser);
    log.info("!! username : " + siteUser.getUsername());
    log.info("!! userPassword : " + siteUser.getPassword());
    return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);

  }
  
}
