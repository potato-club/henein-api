package kr.henein.api.dto.userchar;

import lombok.Getter;

import java.util.List;

@Getter
public class CharRefreshRequestDto {
    List<Long> idList;
}
