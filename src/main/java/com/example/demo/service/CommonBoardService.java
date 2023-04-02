package com.example.demo.service;

import com.example.demo.dto.board.BoardRecommendDTO;
import com.example.demo.dto.board.BoardRequestDto;
import com.example.demo.dto.board.BoardResponseDto;
import com.example.demo.dto.board.ViewIncreaseDto;
import com.example.demo.entity.BoardEntity;
import com.example.demo.entity.RecommendEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.RecommandRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
@Service
@RequiredArgsConstructor
public class CommonBoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final RecommandRepository recommandRepository;
    private final JwtTokenProvider jwtTokenProvider;
    @Transactional
    public BoardResponseDto getOneService(Long id){
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 게시글 정보가 없습니다");});
        BoardResponseDto boardResponseDto = new BoardResponseDto(boardEntity);
        return boardResponseDto;
    }
    @Transactional
    public String updateService(Long id, BoardRequestDto boardRequestDto){
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 게시글 정보가 없습니다");});
        try{
            boardEntity.Update(boardRequestDto);
            boardRepository.save(boardEntity);
        } catch (NullPointerException e) {
            throw new NullPointerException("값이 NULL입니다.");
        }

        return "수정완료";
    }
    @Transactional
    public String deleteService(Long id){
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 게시글 정보가 없습니다");});
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
    public String recommendThisBoard(Long id, HttpServletRequest request){
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 게시글 정보가 없습니다");});
        String AT = jwtTokenProvider.resolveAccessToken(request);
        String userEmail = jwtTokenProvider.getUserEmailFromAccessToken(AT); // 정보 가져옴

        UserEntity userEntity = userRepository.findByEmail(userEmail).
                orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userEmail));

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
            boardRepository.save(boardEntity);
            return "추천 완료";
        }
        //이미 추천한 흔적이 있는 유저들
        if (recommendEntity.isValue()){ //true ?
            BoardRecommendDTO boardRecommendDTO = new BoardRecommendDTO(boardEntity.getRecommend()-1);
            boardEntity.Update(boardRecommendDTO);
            recommendEntity.setValue(false);

            boardRepository.save(boardEntity);
            recommandRepository.save(recommendEntity);
            return "추천 취소";
        } else {
            BoardRecommendDTO boardRecommendDTO = new BoardRecommendDTO(boardEntity.getRecommend()+1);
            boardEntity.Update(boardRecommendDTO);
            recommendEntity.setValue(true);

            boardRepository.save(boardEntity);
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
