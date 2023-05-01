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
@CrossOrigin(origins = "http://localhost:3000")
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
    public String addCommentOfChile(@RequestBody CommentRequsetDto commentRequsetDto,HttpServletRequest request){
        return commentService.addCommentOfChild(commentRequsetDto,request);
    }
    @PutMapping("/comment")
    public String updateComment(@RequestBody CommentRequsetDto commentRequsetDto,HttpServletRequest request){
        return commentService.updateCommentOfId(commentRequsetDto,request);
    }
    @DeleteMapping("/comment")
    public String deleteComment(@RequestBody CommentRequsetDto commentRequsetDto, HttpServletRequest request){
        return commentService.deleteComment(commentRequsetDto,request);
    }
}
