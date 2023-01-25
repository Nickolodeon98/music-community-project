package com.content_i_like.service;

import com.content_i_like.domain.dto.member.MailDto;
import com.content_i_like.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String FROM_ADDRESS;

    public String getTempPassword(){
        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
        String str = "";

        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int)(charSet.length*Math.random());
            str += charSet[idx];
        }
        return str;
    }

    //메일 내용 생성하고 임시비밀번호로 변경
    public MailDto createMailAndChangePassword(String memberEmail){
        String str = getTempPassword();
        MailDto mailDto = MailDto.builder()
                .address(memberEmail)
                .title("내미콘 임시 비밀번호 안내")
                .message("안녕하세요. 내미콘 임시 비밀번호 관련 이메일입니다.\n" +
                        "회원님의 임시 비밀번호는 "+str+" 입니다. 로그인 후에 비밀번호를 변경해주세요.")
                .build();

        updatePassword(str, memberEmail);
        return mailDto;
    }

    //임시 비밀번호로 업데이트
    public void updatePassword(String str, String memberEmail){
        String pw = passwordEncoder.encode(str);
        Long memberNo = memberRepository.findByEmail(memberEmail).get().getMemberNo();
        memberRepository.updateMemberPassword(memberNo, pw);
    }

    public void mailSend(MailDto mailDto){
        MailDto mail = createMailAndChangePassword(mailDto.getAddress());

        System.out.println("이메일 전송!");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail.getAddress());
        message.setFrom(FROM_ADDRESS);
        message.setSubject(mail.getTitle());
        message.setText(mail.getMessage());
        mailSender.send(message);
    }
}
