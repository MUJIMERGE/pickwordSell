package com.smaryn.pws.back.member.service;

import com.smaryn.pws.back.member.domain.Member;
import com.smaryn.pws.back.member.repository.MemberRepository;
import jakarta.transaction.Transactional;

import java.util.Optional;

@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원 가입
     */
    public Long join(Member member) {
        member = memberRepository.save(member);
        return member.getMemberId();
    }

    /**
     * 이메일 중복 확인
     */
    public boolean isNotDuplicateEmail(String memberEmail) {
        return memberRepository.countByMemberEmail(memberEmail) == 0;
    }

    /**
     * 회원 로그인
     */
    public Optional<Member> login(Member member) {
        if (member == null) return Optional.empty();
        Optional<Member> dbMember = memberRepository.findByMemberEmail(member.getMemberEmail());
        if (dbMember.orElseGet(Member::new).getPassword().equals(member.getPassword())) {
            return dbMember;
        } else {
            return Optional.empty();
        }
    }

    /**
     * 회원 정보 변경, 비밀번호 초기화
     */
    public Optional<Member> updateMember(Member member) {
        member = memberRepository.save(member);
        return Optional.of(member);
    }

    /**
     * 회원 탈퇴
     */
    public boolean deleteMember(Member member) {
        memberRepository.delete(member);
        return isNotDuplicateEmail(member.getMemberEmail());
    }
}
