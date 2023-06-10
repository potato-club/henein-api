package com.example.demo.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.dto.board.*;
import com.example.demo.entity.BoardEntity;
import com.example.demo.entity.RecommendEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.RecommandRepository;
import com.example.demo.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
@Service
@RequiredArgsConstructor
public class CommonBoardService {
    private final BoardRepository boardRepository;
    private final RecommandRepository recommandRepository;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Transactional
    public BoardResponseDto getOneService(Long id, String authentication){
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 게시글 정보가 없습니다");});
        if (authentication != null){ //사용자가 이 게시판에 대해서 추천했는지에 대한 t f 적용
            authentication = authentication.substring(7);
            String userEmail = jwtTokenProvider.getUserEmailFromAccessToken(authentication); // 정보 가져옴
            UserEntity userEntity = userRepository.findByUserEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userEmail));

            boolean value = false;
            RecommendEntity recommend = recommandRepository.findByBoardEntityAndUserEntity(boardEntity,userEntity);
            if ( recommend == null){
                BoardResponseDto boardResponseDto = new BoardResponseDto(boardEntity,value);
                return boardResponseDto;
            }
            BoardResponseDto boardResponseDto = new BoardResponseDto(boardEntity,recommend.isValue());
            return boardResponseDto;
        }
        BoardResponseDto boardResponseDto = new BoardResponseDto(boardEntity);
        return boardResponseDto;
    }
    @Transactional
    public String updateService(Long id, TestDto testDto, HttpServletRequest request, HttpServletResponse response){
        UserEntity userEntity = userService.fetchUserEntityByHttpRequest(request,response);
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 게시글 정보가 없습니다");});
        if (boardEntity.getUserEntity().getUserEmail() != userEntity.getUserEmail()){
            throw new RuntimeException("게시글 수정 권한이 없습니다.");
        }

        boardEntity.Update(testDto);
        return "수정완료";
    }
    @Transactional
    public String deleteService(Long id, HttpServletRequest request, HttpServletResponse response){
        UserEntity userEntity = userService.fetchUserEntityByHttpRequest(request, response);
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 게시글 정보가 없습니다");});
        if (boardEntity.getUserEntity().getUserEmail() != userEntity.getUserEmail()){
            throw new RuntimeException("게시글 삭제 권한이 없습니다.");
        }
        boardRepository.delete(boardEntity);
        return "삭제완료";
    }
    @Transactional
    public String updateView(Long id){
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 게시글 정보가 없습니다");});
        BoardResponseDto boardResponseDto = new BoardResponseDto(boardEntity);
        //조회수 증가부분
        ViewIncreaseDto viewIncreaseDto = new ViewIncreaseDto();
        viewIncreaseDto.setViews(boardEntity.getViews()+1);
        boardEntity.Update(viewIncreaseDto);
        boardRepository.save(boardEntity);
        return "조회수 증가완료";
    }
    @Transactional
    public String recommendThisBoard(Long id, HttpServletRequest request, HttpServletResponse response){
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 게시글 정보가 없습니다");});
        UserEntity userEntity = userService.fetchUserEntityByHttpRequest(request, response);

        RecommendEntity recommendEntity = recommandRepository.findByBoardEntityAndUserEntity(boardEntity,userEntity);
        //추천 DB에 없는 인원일때 ( 해당 게시글에 처음 추천을 누른 유저일시 )
        if (null == recommendEntity){
            RecommendEntity recommend = RecommendEntity.builder()
                    .boardEntity(boardEntity)
                    .userEntity(userEntity)
                    .value(true)
                    .build();
            BoardRecommendDTO boardRecommendDTO = new BoardRecommendDTO(boardEntity.getRecommend()+1);
            boardEntity.Update(boardRecommendDTO);

            recommandRepository.save(recommend);
            return "추천 완료";
        }
        //이미 추천한 흔적이 있는 유저들
        if (recommendEntity.isValue()){ //true ?
            BoardRecommendDTO boardRecommendDTO = new BoardRecommendDTO(boardEntity.getRecommend()-1);
            boardEntity.Update(boardRecommendDTO);
            recommendEntity.setValue(false);

            recommandRepository.save(recommendEntity);
            return "추천 취소";
        } else {
            BoardRecommendDTO boardRecommendDTO = new BoardRecommendDTO(boardEntity.getRecommend()+1);
            boardEntity.Update(boardRecommendDTO);
            recommendEntity.setValue(true);

            recommandRepository.save(recommendEntity);
            return "재추천 완료";
        }
    }
  /*  @Transactional
    public String unRecommendThisBoard(Long id){
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 게시글 정보가 없습니다");});
        BoardRecommendDTO recommandUpdateDTO = new BoardRecommendDTO();
        recommandUpdateDTO.setRecommend(boardEntity.getRecommend()-1);
        boardEntity.Update(recommandUpdateDTO);
        boardRepository.save(boardEntity);
    }*/
}
