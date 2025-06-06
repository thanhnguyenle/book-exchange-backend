package com.bookexchange.controller;

import com.bookexchange.dto.request.ApiResponse;
import com.bookexchange.dto.request.ListedBookCreationRequest;
import com.bookexchange.dto.response.ListedBookDetailResponse;
import com.bookexchange.dto.response.ListedBooksResponse;
import com.bookexchange.service.ListedBookService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/listed-books")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ListedBookController {

    ListedBookService listedBookService;

    @PostMapping
    public ApiResponse<Void> createListedBook(@RequestBody @Valid ListedBookCreationRequest request) {
        listedBookService.createListedBook(request);
        return ApiResponse.<Void>builder()
                .build();
    }

    @GetMapping("/latest")
    public ApiResponse<List<ListedBooksResponse>> getLatestListedBooks() {
        return ApiResponse.<List<ListedBooksResponse>>builder()
                .result(listedBookService.getLatestListedBooks())
                .build();
    }

    @GetMapping("{id}")
    public ApiResponse<ListedBookDetailResponse> getListedDetail(@PathVariable Long id) {
        return ApiResponse.<ListedBookDetailResponse>builder()
                .result(listedBookService.getListedDetail(id))
                .build();
    }
    
    @GetMapping
    public ApiResponse<Page<ListedBooksResponse>> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer condition,
            @RequestParam(required = false) Long schoolId
    ) {
        log.info("Getting books with params: page={}, size={}, sortBy={}, sortDir={}, title={}, author={}, categoryId={}, minPrice={}, maxPrice={}, condition={}, schoolId={}",
                page, size, sortBy, sortDir, title, author, categoryId, minPrice, maxPrice, condition, schoolId);
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? 
                Sort.Direction.ASC : Sort.Direction.DESC;
        
        Page<ListedBooksResponse> books = listedBookService.getBooks(
                page, size, sortBy, direction, title, author, 
                categoryId, minPrice, maxPrice, condition, schoolId);
        
        return ApiResponse.<Page<ListedBooksResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(books)
                .build();
    }
}
