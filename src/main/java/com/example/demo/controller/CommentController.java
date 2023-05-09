package com.example.demo.controller;

import com.example.demo.dto.comment.CommentRequsetDto;
import com.example.demo.dto.comment.CommentResponseDto;
import com.example.demo.service.CommentService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/board")
@RequiredArgsConstructor
@Api(tags = {"댓글 Controller"})

public class CommentController {
    final private CommentService commentService;
    @GetMapping("/{id}/comment") //넘겨주는건 게시판의 id, 댓글 보는건 인증 X
    public List<CommentResponseDto> getComment(@PathVariable("id") Long id){
        return commentService.getCommentOfBoard(id);
    }
    @PostMapping("/comment")
    public String addCommentOfParent(@RequestBody CommentRequsetDto commentRequsetDto,HttpServletRequest request){
        return commentService.addCommentOfParent(commentRequsetDto,request);
    }
    @PostMapping("/comment/child")
    public String addCommentOfChild(@RequestBody CommentRequsetDto commentRequsetDto,HttpServletRequest request){
        return commentService.addCommentOfChild(commentRequsetDto,request);
    }
    @PutMapping("/comment")
    public String updateCommentOfParent(@RequestBody CommentRequsetDto commentRequsetDto,HttpServletRequest request){
        return commentService.updateCommentOfParent(commentRequsetDto,request);
    }
    @PutMapping("/comment/child")
    public String updateCommentOfChild(@RequestBody CommentRequsetDto commentRequsetDto,HttpServletRequest request){
        return commentService.updateCommentOfChild(commentRequsetDto,request);
    }
    @DeleteMapping("/comment/")
    public String deleteCommentOfParent(@RequestBody CommentRequsetDto commentRequsetDto, HttpServletRequest request){
        return commentService.deleteCommentOfParent(commentRequsetDto,request);
    }
    @DeleteMapping("/comment/parent")
    public String deleteCommentOfChild(@RequestBody CommentRequsetDto commentRequsetDto, HttpServletRequest request){
        return commentService.deleteCommentOfChild(commentRequsetDto,request);
    }
}
