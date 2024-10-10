package com.zerobase.service;

import com.zerobase.exception.impl.AlreadyExistUserException;
import com.zerobase.exception.impl.NotExistUsernameException;
import com.zerobase.exception.impl.NotMatchPasswordException;
import com.zerobase.model.Auth;
import com.zerobase.persist.entity.MemberEntity;
import com.zerobase.persist.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("couldn't find user -> " + username));
    }

    public MemberEntity register(Auth.SignUp member) {
        // 중복된 username 이 있는 경우 예외처리
        if( this.memberRepository.existsByUsername(member.getUsername()) ) {
            throw new AlreadyExistUserException();
        }

        // 사용자가 입력한 암호를 암호화 수행
        member.setPassword(this.passwordEncoder.encode(member.getPassword()));

        return this.memberRepository.save(member.toEntity());
    }

    public MemberEntity authenticate(Auth.SignIn member) {
        // username 으로 저장되어 있는 지 확인
        MemberEntity user = this.memberRepository.findByUsername(member.getUsername())
                                                 .orElseThrow(NotExistUsernameException::new);

        // 입력받은 password 와 저장되어 있는 password 가 같은 지 확인
        if(!this.passwordEncoder.matches(member.getPassword(), user.getPassword())) {
            throw new NotMatchPasswordException();
        }

        return user;
    }
}
