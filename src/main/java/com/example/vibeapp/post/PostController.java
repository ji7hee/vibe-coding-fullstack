package com.example.vibeapp.post;

import com.example.vibeapp.post.dto.PostCreateDto;
import com.example.vibeapp.post.dto.PostResponseDto;
import com.example.vibeapp.post.dto.PostUpdateDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
        model.addAttribute("posts", postService.findAll(page, pageSize));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postService.getTotalPages(pageSize));
        return "post/posts";
    }

    @GetMapping("/posts/{no}")
    public String detail(@PathVariable Long no, Model model) {
        PostResponseDto post = postService.findById(no);
        model.addAttribute("post", post);
        return "post/post_detail";
    }

    @GetMapping("/posts/new")
    public String newForm(Model model) {
        model.addAttribute("postCreateDto", new PostCreateDto());
        return "post/post_new_form";
    }

    @PostMapping("/posts/add")
    public String add(@Valid @ModelAttribute PostCreateDto postCreateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "post/post_new_form";
        }
        postService.create(postCreateDto);
        return "redirect:/posts";
    }
    @GetMapping("/posts/{no}/edit")
    public String editForm(@PathVariable Long no, Model model) {
        PostResponseDto post = postService.findByIdWithoutViewCount(no);
        model.addAttribute("post", post);
        model.addAttribute("postUpdateDto", new PostUpdateDto(post.title(), post.content(), post.tags()));
        return "post/post_edit_form";
    }

    @PostMapping("/posts/{no}/save")
    public String save(@PathVariable Long no, @Valid @ModelAttribute PostUpdateDto postUpdateDto, 
                       BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            PostResponseDto post = postService.findByIdWithoutViewCount(no);
            model.addAttribute("post", post);
            return "post/post_edit_form";
        }
        postService.update(no, postUpdateDto);
        return "redirect:/posts/" + no;
    }

    @GetMapping("/posts/{no}/delete")
    public String delete(@PathVariable Long no) {
        postService.delete(no);
        return "redirect:/posts";
    }
}
