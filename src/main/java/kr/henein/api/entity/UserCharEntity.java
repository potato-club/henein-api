package kr.henein.api.entity;

import kr.henein.api.dto.userchar.CharacterBasic;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class UserCharEntity extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name ="user", nullable = false)
    UserEntity userEntity;

    @Column
    private String ocid;

    @Column(nullable = false)
    private boolean pickByUser = false;

    @Column(nullable = false,unique = true)
    private String charName;

    @Column
    private String world;

    @Column
    private String job;

    @Column
    private int level;

    @Column(length = 1000)
    private String avatar;


    public UserCharEntity(UserEntity userEntity,String charName) {
        this.userEntity = userEntity;
        this.charName = charName;
    }
    public void update(CharacterBasic characterBasic){
        this.ocid = characterBasic.getOcid();
        this.world = characterBasic.getWorld_name();
        this.job = characterBasic.getCharacter_class();
        this.level = characterBasic.getCharacter_level();
        this.avatar = characterBasic.getCharacter_image();
    }
    public void pickThisCharacter(){
        this.pickByUser = true;
    }
    public void unPickThisCharacter(){
        this.pickByUser = false;
    }
}
