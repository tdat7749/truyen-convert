package truyenconvert.server.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import truyenconvert.server.models.enums.Role;

import java.time.LocalDateTime;
import java.util.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users",indexes = {
        @Index(name = "idx_email",columnList = "email")
})
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Role role;

    @Column(nullable = false,name = "display_name")
    private String displayName;

    @Column(nullable = true,columnDefinition = "TEXT")
    private String avatar;

    @Column(nullable = false)
    @ColumnDefault(value = "'0'")
    private long coin;

    @Column(nullable = false,name = "monthly_ticket")
    @ColumnDefault(value = "'0'")
    private int monthlyTicket;

    @Column(nullable = false)
    @ColumnDefault(value = "'1'")
    private int level;

    @Column(nullable = false)
    @ColumnDefault(value = "'0'")
    private long exp;

    @Column(nullable = false,name = "is_lock")
    @ColumnDefault(value = "'false'")
    private boolean isLock;

    @Column(nullable = false,name = "is_verify")
    @ColumnDefault(value = "'false'")
    private boolean isVerify;

    @Column(nullable = false,name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false,name = "updated_at")
    private LocalDateTime updatedAt;

    // Bill
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "user")
    @JsonManagedReference
    private List<Bill> bills;

    // Read History
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "user")
    @JsonManagedReference
    private List<ReadHistory> readHistories;

    // Story
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "user")
    @JsonManagedReference
    private List<Book> books;

    // Comment
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "user")
    @JsonManagedReference
    private List<Comment> comments;

    // Unlock Chapter
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "unlock_chapter",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "chapter_id")
    )
    private List<Chapter> chapters;

    //Donation
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "userGave")
    @JsonManagedReference
    private List<Donation> donations;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "userReceived")
    @JsonManagedReference
    private List<Donation> receivedDonations;


    //Evaluation
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "user")
    @JsonManagedReference
    private List<Evaluation> evaluations;

    //Nomination
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "user")
    @JsonManagedReference
    private List<Nomination> nominations;

    //Marked
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "user")
    @JsonManagedReference
    private List<Marked> markeds;

    // LikeComment
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "like_comments",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    private List<Comment> likesComment;

    // END
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));

        return authorities;
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
        return !isLock;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isVerify;
    }
}
