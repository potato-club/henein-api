package com.example.demo.controller;

import com.example.demo.dto.comment.CommentRequsetDto;
import com.example.demo.dto.comment.CommentResponseDto;
import com.example.demo.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/board")
@RequiredArgsConstructor
@Api(tags = {"댓글 Controller"})
@Slf4j
public class CommentController {
    final private CommentService commentService;
    @Operation(summary = "게시판의 id로 댓글 전부 호출 API")
    @GetMapping("/{id}/comment") //넘겨주는건 게시판의 id, 댓글 보는건 인증 X
    public List<CommentResponseDto> getComment(@PathVariable("id") Long id){
        return commentService.getCommentOfBoard(id);
    }
    @Operation(summary = "댓글 작성 API [commentId = null]")
    @PostMapping("/comment")
    public String addCommentOfParent(@Valid @RequestBody CommentRequsetDto commentRequsetDto, @ApiIgnore HttpServletRequest request){
        log.info(commentRequsetDto.getBoardId()+"과"+commentRequsetDto.getCommentId());
        return commentService.addCommentOfParent(commentRequsetDto,request);
    }
    @Operation(summary = "대댓글 작성 API [commentId = 부모댓글의 id")
    @PostMapping("/comment/child")
    public String addCommentOfChild(@RequestBody CommentRequsetDto commentRequsetDto,@ApiIgnore HttpServletRequest request){
        return commentService.addCommentOfChild(commentRequsetDto,request);
    }
    @Operation(summary = "댓글수정 API [commentId = 수정될 댓글Id]")
    @PutMapping("/comment")
    public String updateCommentOfParent(@RequestBody CommentRequsetDto commentRequsetDto,@ApiIgnore HttpServletRequest request){
        return commentService.updateCommentOfParent(commentRequsetDto,request);
    }
    @Operation(summary = "대댓글 수정 API [commentId = 수정될 대댓글 id]")
    @PutMapping("/comment/child")
    public String updateCommentOfChild(@RequestBody CommentRequsetDto commentRequsetDto,@ApiIgnore HttpServletRequest request){
        return commentService.updateCommentOfChild(commentRequsetDto,request);
    }
    @Operation(summary = "댓글 삭제 API")
    @DeleteMapping("/comment")
    public String deleteCommentOfParent(@RequestBody CommentRequsetDto commentRequsetDto,@ApiIgnore HttpServletRequest request){
        return commentService.deleteCommentOfParent(commentRequsetDto,request);
    }
    @Operation(summary = "대댓글 수정 API")
    @DeleteMapping("/comment/parent")
    public String deleteCommentOfChild(@RequestBody CommentRequsetDto commentRequsetDto,@ApiIgnore HttpServletRequest request){
        return commentService.deleteCommentOfChild(commentRequsetDto,request);
    }
}
