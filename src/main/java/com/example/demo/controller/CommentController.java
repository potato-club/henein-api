package com.example.demo.controller;

import com.example.demo.dto.comment.CommentRequsetDto;
import com.example.demo.dto.comment.CommentResponseDto;
import com.example.demo.dto.comment.ReplyRequestDto;
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
import javax.servlet.http.HttpServletResponse;
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
    @Operation(summary = "댓글 작성 API [commentId = null], [보안]")
    @PostMapping("{id}/comment")
    public String addCommentOfParent(@PathVariable Long id, @RequestBody CommentRequsetDto commentRequsetDto, HttpServletRequest request, HttpServletResponse response){
        return commentService.addCommentOfParent(id, commentRequsetDto,request,response);
    }
    @Operation(summary = "대댓글 작성 API [commentId = 부모댓글의 id], [보안]")
    @PostMapping("/{id}/comment/{co-id}/child")
    public String addCommentOfChild(@PathVariable("id") Long id, @PathVariable("co-id") Long coId,
                                    @RequestBody ReplyRequestDto replyRequestDto, HttpServletRequest request, HttpServletResponse response){
        return commentService.addCommentOfChild(id,coId, replyRequestDto,request,response);
    }
    @Operation(summary = "댓글수정 API [commentId = 수정될 댓글Id], [보안]")
    @PutMapping("/{id}/comment/{co-id}")
    public String updateCommentOfParent(@PathVariable("id") Long id, @PathVariable("co-id") Long coId,
                                        @RequestBody CommentRequsetDto commentRequsetDto, HttpServletRequest request, HttpServletResponse response){
        return commentService.updateCommentOfParent(id,coId,commentRequsetDto,request,response);
    }
    @Operation(summary = "대댓글 수정 API [commentId = 수정될 대댓글 id], [보안]")
    @PutMapping("/{id}/comment/child/{re-id}")
    public String updateCommentOfChild(@PathVariable("id")Long id,@PathVariable("re-id")Long reId,
                                       @RequestBody ReplyRequestDto replyRequestDto, HttpServletRequest request, HttpServletResponse response){
        return commentService.updateCommentOfChild(id,reId, replyRequestDto, request,response);
    }
    @Operation(summary = "댓글 삭제 API [보안]")
    @DeleteMapping("/{id}/comment/{co-id}")
    public String deleteCommentOfParent(@PathVariable("id") Long id, @PathVariable("co-id") Long coId,
                                         HttpServletRequest request, HttpServletResponse response){
        return commentService.deleteCommentOfParent(id,coId,request,response);
    }
    @Operation(summary = "대댓글 수정 API [보안]")
    @DeleteMapping("/{id}/comment/child/{re-id}")
    public String deleteCommentOfChild(@PathVariable("id") Long id, @PathVariable("re-id") Long reId,
                                        HttpServletRequest request, HttpServletResponse response){
        return commentService.deleteCommentOfChild(id, reId,request,response);
    }
}
