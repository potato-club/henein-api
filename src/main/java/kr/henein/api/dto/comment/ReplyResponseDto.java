package kr.henein.api.dto.comment;

import kr.henein.api.entity.ReplyEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReplyResponseDto {

    private Long id;
    private int writerId;
    private String tag;
    private String comment;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime modifiedDate;
    private Boolean updated;
    public ReplyResponseDto(ReplyEntity reply, int writerId) {
        this.id = reply.getId();
        this.writerId = writerId;
        this.tag = reply.getTag();
        this.comment = reply.getComment();
        this.modifiedDate = reply.getModifiedDate();
        this.updated = reply.getUpdated();
    }
}
