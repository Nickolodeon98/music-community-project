package com.content_i_like.domain.entity;

import com.content_i_like.domain.dto.member.MemberModifyRequest;
import com.content_i_like.domain.enums.GenderEnum;
import com.content_i_like.domain.enums.MemberStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Builder
@Entity
@DynamicInsert
public class Member extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo;

    @Column(unique=true, nullable = false)
    private String email;

    private String password;

    private String profileImgUrl;

    @Column(unique=true, nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String name;

    private String snsCheck;

    private String introduction;

    @Enumerated(EnumType.STRING)
    private MemberStatusEnum status;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'UNKNOWN'")
    private GenderEnum gender;

    private Integer birth;

    @OneToMany(mappedBy = "member")
    private List<Point> pointNo;

    public void update(MemberModifyRequest request){
        this.introduction = request.getIntroduction();
        this.gender = request.getGender();
        this.birth = request.getBirth();
        this.password = request.getNewPassword();
    }

    public void updateImg(String url){
        this.profileImgUrl = url;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
