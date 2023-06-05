package com.example.demo.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.dto.board.BoardListResponseDto;
import com.example.demo.dto.board.BoardRequestDto;
import com.example.demo.dto.board.BoardResponseDto;
import com.example.demo.entity.BoardEntity;
import com.example.demo.entity.S3File;
import com.example.demo.entity.UserEntity;
import com.example.demo.enumCustom.BoardType;
//import com.example.demo.error.exception.NotFoundException;
import com.example.demo.error.ErrorCode;
import com.example.demo.error.exception.NotFoundException;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.S3FileRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class BoardTypeOfService {
    private final BoardRepository boardRepository;
    private final S3FileRespository s3FileRespository;
    private final S3Service s3Service;
    private final UserService userService;

    @Transactional //
    public Page<BoardListResponseDto> getTypeOfBoard(int page, int boardType){
        BoardType board;
        PageRequest pageRequest = PageRequest.of(page-1, 20);

        switch (boardType){
            case 65: board = BoardType.Advertise; break;
            case 66: board = BoardType.Boss; break;
            case 69: {Page<BoardEntity> boardEntityList = boardRepository.findAllNotNotice(pageRequest);
                return new PageImpl<>(boardEntityList.stream().map(BoardListResponseDto::new).collect(Collectors.toList()));}
            case 70: board = BoardType.Free; break;
            case 72: board = BoardType.Humor; break;
            case 73: board = BoardType.Info; break;
            case 78: board = BoardType.Notice; break;
            default: throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION,ErrorCode.NOT_FOUND_EXCEPTION.getMessage());
        }

        Page<BoardEntity> boardEntityList = boardRepository.findByBoardType(board,pageRequest);
        return new PageImpl<>(boardEntityList.stream().map(BoardListResponseDto::new).collect(Collectors.toList()));
    }

    //===================================================================================================
    @Transactional
    public String addTypeOfBoard(BoardRequestDto boardRequestDto, HttpServletRequest request, HttpServletResponse response){
        UserEntity userEntity = userService.fetchUserEntityByHttpRequest(request, response); // jwt 로직 추가

        BoardType board;
        switch (boardRequestDto.getBoardType()){
            case "A": board = BoardType.Advertise; break;
            case "B": board = BoardType.Boss; break;
            case "F": board = BoardType.Free; break;
            case "I": board = BoardType.Info; break;
            case "H": board = BoardType.Humor; break;
            case "N": board = BoardType.Notice; break;
            default: throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION,ErrorCode.NOT_FOUND_EXCEPTION.getMessage());
        }
        BoardEntity boardEntity = new BoardEntity(boardRequestDto,board, userEntity);
//        if (boardRequestDto.getImages() != null){
//            uploadBoardFile(boardRequestDto.getImages(),boardEntity);
//        }
        boardRepository.save(boardEntity);
        return "저장 완료";
    }
    // 사진을 S3 저장소에 올리고 그 요소들을 리스트로 반환하는 기능이다.
    private List<String> uploadBoardFile(List<MultipartFile> image, BoardEntity boardEntity){
        return image.stream()
                .map(file -> s3Service.upload(file))
                .map(url ->createFile(boardEntity, url))
                .map(file -> file.getFileUrl())
                .collect(Collectors.toList());
    }
    // DB에 올린 사진의 Url과 이름을 저장하는 기능이다.
    private S3File createFile(BoardEntity boardEntity,String url){
        return s3FileRespository.save(S3File.builder()
                .fileUrl(url)
                .fileName(StringUtils.getFilename(url))
                .boardEntity(boardEntity)
                .build());
    }

}