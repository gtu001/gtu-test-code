package gtu.swagger;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


import java.util.List;
/**
 * dependencies {
    compile 'io.springfox:springfox-swagger2:2.7.0'
    compile 'io.springfox:springfox-swagger-ui:2.7.0'
    compile 'io.springfox:springfox-data-rest:2.7.0'
    }

服務啟動後我們進入 swagger 的 ui
http://localhost:8080/swagger-ui.html
 *
 */
@Api(tags = "Book")
@RestController
@RequestMapping(value = "/api")
public class SwaggerBookControllerTest001 {
    
    /**
     * 那這個 SpringDataRestConfiguration 是要幹嘛的?
剛不是提到 Spring Data Rest 的功能嗎?
除了加上依賴以外, 別忘了還要加上這個才會引入 Spring Data 的 RestAPI 喔~
     */
    @Configuration
    @EnableSwagger2
    @Import(SpringDataRestConfiguration.class)
    public static class SwaggerConfig {
    }
    
    
    @Data
    @ApiModel(description = "書本資料")
    public static class BookDto {
        @ApiModelProperty(value = "序號", required = true)
        private Integer bookid;
        @ApiModelProperty(value = "書名", required = true)
        private String name;
        @ApiModelProperty(value = "作者", required = true)
        private String author;
    }
    

    @Autowired
    private BookRepository bookRepository;

    @ApiOperation(value = "取得書本", notes = "列出所有書本")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/v1/book", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @ApiOperation(value = "新增書本", notes = "新增書本的內容")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "存檔成功")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/v1/book", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BookDto create(
            @ApiParam(required = true, value = "書本內容") @RequestBody BookDto bookDto) {
        Book book = new Book();
        book.setBookid(bookDto.getBookid());
        book.setName(bookDto.getName());
        book.setAuthor(bookDto.getAuthor());
        book = bookRepository.save(book);
        bookDto.setBookid(book.getBookid());
        return bookDto;
    }

    @ApiOperation(value = "取得書本內容", notes = "取得書本內容")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "書本資訊")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/v1/book/{bookid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BookDto get(
            @ApiParam(required = true, name = "bookid", value = "書本ID") @PathVariable Integer bookid) {
        Book book = bookRepository.findOne(bookid);
        BookDto bookDto = new BookDto();
        bookDto.setBookid(book.getBookid());
        bookDto.setName(book.getName());
        bookDto.setAuthor(book.getAuthor());
        return bookDto;
    }
}