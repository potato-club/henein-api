package kr.henein.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "boardId")//JoinColum에는 Column을 사용할 수 없다. 칼럼은 일반 -DB에 사용하며 JoinColumn은 외래키 - db 이기 때문이다.
    private BoardEntity boardEntity;

    @ManyToOne
    @JoinColumn(name = "email",nullable = false)
    private UserEntity userEntity;

    @Column(nullable = false)
    private boolean value;

    public void setValue(boolean value){
        this.value = value;
    }
/*    @PreUpdate //
    public void onPreUpdate(){
        this.updateTime=LocalDateTime.now();
    }*/


}
