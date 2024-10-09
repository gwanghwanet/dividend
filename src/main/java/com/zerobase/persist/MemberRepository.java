package com.zerobase.persist;

import com.zerobase.persist.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    // username 으로 사용자 정보를 찾기 위함.
    Optional<MemberEntity> findByUsername(String username);

    // 회원 가입 시 존재하는 username 인지 확인하기 위함
    boolean existsByUsername(String username);

}
