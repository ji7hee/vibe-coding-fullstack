package com.example.vibeapp.post;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public String list(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 5;
        model.addAttribute("posts", postService.getPostList(page, pageSize));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postService.getTotalPages(pageSize));
        return "posts";
    }

    @GetMapping("/posts/{no}")
    public String detail(@PathVariable Long no, Model model) {
        Post post = postService.getPost(no);
        model.addAttribute("post", post);
        return "post_detail";
    }

    @GetMapping("/posts/new")
    public String newForm() {
        return "post_new_form";
    }

    @PostMapping("/posts/add")
    public String add(@RequestParam String title, @RequestParam String content) {
        postService.addPost(title, content);
        return "redirect:/posts";
    }
    @GetMapping("/posts/{no}/edit")
    public String editForm(@PathVariable Long no, Model model) {
        Post post = postService.getPostWithoutViewCount(no);
        model.addAttribute("post", post);
        return "post_edit_form";
    }

    @PostMapping("/posts/{no}/save")
    public String save(@PathVariable Long no, @RequestParam String title, @RequestParam String content) {
        postService.updatePost(no, title, content);
        return "redirect:/posts/" + no;
    }

    @GetMapping("/posts/{no}/delete")
    public String delete(@PathVariable Long no) {
        postService.deletePost(no);
        return "redirect:/posts";
    }
}
