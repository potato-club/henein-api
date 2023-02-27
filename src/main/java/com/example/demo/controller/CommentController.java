package com.example.demo.controller;

import com.example.demo.dto.CommentRequsetDto;
import com.example.demo.dto.CommentResponseDto;
import com.example.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/board/{boardtype}/{id}/comment")
@RequiredArgsConstructor
public class CommentController {
    final private CommentService commentService;
    @GetMapping() //넘겨주는건 게시판의 id
    public List<CommentResponseDto> getComment(@PathVariable("id") Long id){
        return commentService.getCommentOfId(id);
    }
    @PostMapping()
    public String postComment(@PathVariable("id") Long id, @RequestBody CommentRequsetDto commentRequsetDto){
        return commentService.postCommentOfId(id, commentRequsetDto);
    }
    @PutMapping("/{co-id}")
    public String updateComment(@PathVariable("co-id") Long coid, @RequestBody CommentRequsetDto commentRequsetDto){
        return commentService.updateCommentOfId(coid,commentRequsetDto);
    }
    @DeleteMapping("/{co-id}")
    public String updateComment(@PathVariable("co-id") Long coid){
        return commentService.deleteCommentOfId(coid);
    }
}
